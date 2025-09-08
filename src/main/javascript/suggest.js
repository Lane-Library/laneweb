(function () {

    "use strict";

    class Suggest {

        static get DEFAULTS() {
            return {
                QUERY_LENGTH: 3,
                QUERY_DELAY: 100,
                BASE_PATH: '',
            };
        }

        static get CSS() {
            return {
                suggestionContainer: 'aclist-content',
                list: 'aclist-list',
                item: 'aclist-item',
                itemActive: 'aclist-item-active',
            };
        }

        constructor(input, sourceEndpoint, options = {}) {
            const defaults = {
                minQueryLength: Suggest.DEFAULTS.QUERY_LENGTH,
                queryDelay: Suggest.DEFAULTS.QUERY_DELAY,
                basePath: (L.Model && L.Model.get(L.Model.BASE_PATH)) || Suggest.DEFAULTS.BASE_PATH
            };

            // Merge options with defaults
            const settings = Object.assign({}, defaults, options);

            this._input = input;
            this.sourceEndpoint = settings.basePath + sourceEndpoint;
            this.queryLength = settings.minQueryLength;
            this.queryDelay = settings.queryDelay;

            this.cache = new Map();
            this.suggestions = [];
            this.activeSuggestionIndex = -1;
            this._queryDelayTimer = null;

            // Pre-bind 'this' for event listeners to ensure they can be removed correctly.
            this.handleInput = this.handleInput.bind(this);
            this.handleKeydown = this.handleKeydown.bind(this);
            this.handleGlobalClick = this.handleGlobalClick.bind(this);
            this.handleListClick = this.handleListClick.bind(this);
            this.handleListMouseover = this.handleListMouseover.bind(this);
            this.disable = this.disable.bind(this);

            this.init();
        }

        init() {
            this._input.autocomplete = 'off';
            this._input.setAttribute('aria-controls', 'suggest-list');
            this._input.setAttribute('aria-autocomplete', 'list');

            this._container = document.createElement('div');
            this._container.className = Suggest.CSS.suggestionContainer;
            this._container.innerHTML = `<ul id="suggest-list" class="${Suggest.CSS.list}" role="listbox"></ul>`;
            this._list = this._container.querySelector('ul');
            this._input.after(this._container);
            this.hideSuggestions();

            this.bindUI();
        }

        bindUI() {
            this._input.addEventListener('input', this.handleInput);
            this._input.addEventListener('keydown', this.handleKeydown);
            // Use event delegation on the list
            this._list.addEventListener('click', this.handleListClick);
            this._list.addEventListener('mouseover', this.handleListMouseover);

            document.addEventListener('click', this.handleGlobalClick);
            L.on("search:search", this.disable);
        }

        unbindUI() {
            this._input.removeEventListener('input', this.handleInput);
            this._input.removeEventListener('keydown', this.handleKeydown);
            this._list.removeEventListener('click', this.handleListClick);
            this._list.removeEventListener('mouseover', this.handleListMouseover);

            document.removeEventListener('click', this.handleGlobalClick);
        }

        setSourceEndpoint(newEndpoint) {
            const basePath = (L.Model && L.Model.get(L.Model.BASE_PATH)) || Suggest.DEFAULTS.BASE_PATH;
            this.sourceEndpoint = basePath + newEndpoint;
        }

        handleInput() {
            clearTimeout(this._queryDelayTimer);
            this._queryDelayTimer = setTimeout(() => {
                const query = this._input.value;
                this.fetchSuggestions(query);
            }, this.queryDelay);
        }

        fetchSuggestions(query) {
            if (query.length < this.queryLength) {
                this.hideSuggestions();
                return;
            }

            if (this.cache.has(query)) {
                this.renderSuggestions(this.cache.get(query));
                return;
            }

            const urlEndpoint = this.sourceEndpoint.replace("{query}", encodeURIComponent(query));

            fetch(urlEndpoint)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    const suggestions = data || [];
                    this.cache.set(query, suggestions);
                    this.renderSuggestions(suggestions);
                })
                .catch(error => {
                    console.error("Failed to fetch suggestions:", error);
                    this.hideSuggestions();
                });
        }

        renderSuggestions(suggestions) {
            this.suggestions = suggestions;
            this.activeSuggestionIndex = -1;

            if (suggestions.length === 0) {
                this.hideSuggestions();
                return;
            }

            this._list.innerHTML = suggestions.map((suggestion, index) =>
                `<li class="${Suggest.CSS.item}" role="option" id="suggest-item-${index}" data-index="${index}">${suggestion}</li>`
            ).join('');

            this.showSuggestions();
        }

        handleKeydown(event) {
            if (this._container.hidden || this.suggestions.length === 0) return;

            switch (event.key) {
                case 'ArrowDown':
                    event.preventDefault();
                    this.activeSuggestionIndex = (this.activeSuggestionIndex + 1) % this.suggestions.length;
                    this.updateActiveSuggestion();
                    break;
                case 'ArrowUp':
                    event.preventDefault();
                    this.activeSuggestionIndex = (this.activeSuggestionIndex - 1 + this.suggestions.length) % this.suggestions.length;
                    this.updateActiveSuggestion();
                    break;
                case 'Enter':
                case 'Tab':
                    if (this.activeSuggestionIndex > -1) {
                        event.preventDefault();
                        this.selectSuggestion(this.activeSuggestionIndex);
                    }
                    break;
                case 'Escape':
                    event.preventDefault();
                    this.hideSuggestions();
                    break;
            }
        }

        updateActiveSuggestion() {
            Array.prototype.forEach.call(this._list.children, (item, index) => {
                if (index === this.activeSuggestionIndex) {
                    item.classList.add(Suggest.CSS.itemActive);
                    this._input.setAttribute('aria-activedescendant', item.id);
                    item.scrollIntoView({
                        block: 'nearest'
                    });
                } else {
                    item.classList.remove(Suggest.CSS.itemActive);
                }
            });
        }

        selectSuggestion(index) {
            const selectedText = this.suggestions[index];
            if (typeof selectedText !== 'undefined') {
                this._input.value = selectedText;
                this.fire("suggest:select", {
                    suggestion: selectedText,
                    input: this._input
                });
                this.hideSuggestions();
            }
        }

        handleGlobalClick(event) {
            // hide suggestions if the click is:
            //  in the input
            //  or anywhere outside the input or suggestions container
            if (event.target === this._input || (!this._input.contains(event.target) && !this._container.contains(event.target))) {
                this.hideSuggestions();
            }
        }

        handleListClick(event) {
            const targetItem = event.target.closest(`.${Suggest.CSS.item}`);
            if (targetItem) {
                const index = parseInt(targetItem.dataset.index, 10);
                this.selectSuggestion(index);
            }
        }

        handleListMouseover(event) {
            const targetItem = event.target.closest(`.${Suggest.CSS.item}`);
            if (targetItem) {
                this.activeSuggestionIndex = parseInt(targetItem.dataset.index, 10);
                this.updateActiveSuggestion();
            }
        }

        showSuggestions() {
            this._container.hidden = false;
            this._input.setAttribute('aria-expanded', 'true');
        }

        hideSuggestions() {
            this._list.innerHTML = '';
            this._container.hidden = true;
            this.suggestions = [];
            this.activeSuggestionIndex = -1;
            this._input.setAttribute('aria-expanded', 'false');
            this._input.removeAttribute('aria-activedescendant');
        }

        disable() {
            this.hideSuggestions();
            this.unbindUI();
            this._input.readOnly = true;
            clearTimeout(this._queryDelayTimer);
        }
    }

    //Add EventTarget attributes to the Suggest prototype
    if (L && L.addEventTarget) {
        L.addEventTarget(Suggest, {
            prefix: 'suggest'
        });
    }

    //Make the Suggest constructor globally accessible
    if (L) {
        L.Suggest = Suggest;
    }

})();