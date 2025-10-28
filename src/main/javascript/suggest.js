(() => {

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

        #input;
        #list;
        #container;
        #cache;
        #suggestions;
        #activeSuggestionIndex;
        #queryDelayTimer;
        #abortController = null;

        constructor(input, sourceEndpoint, options = {}) {
            const defaults = {
                minQueryLength: Suggest.DEFAULTS.QUERY_LENGTH,
                queryDelay: Suggest.DEFAULTS.QUERY_DELAY,
                basePath: (L.Model && L.Model.get(L.Model.BASE_PATH)) || Suggest.DEFAULTS.BASE_PATH
            };

            // Merge options with defaults
            const settings = { ...defaults, ...options };

            this.#input = input;
            this.sourceEndpoint = settings.basePath + sourceEndpoint;
            this.queryLength = settings.minQueryLength;
            this.queryDelay = settings.queryDelay;

            this.#cache = new Map();
            this.#suggestions = [];
            this.#activeSuggestionIndex = -1;
            this.#queryDelayTimer = null;

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
            this.#input.autocomplete = 'off';
            this.#input.setAttribute('aria-controls', 'suggest-list');
            this.#input.setAttribute('aria-autocomplete', 'list');
            this.#input.setAttribute('role', 'combobox');

            this.#container = document.createElement('div');
            this.#container.className = Suggest.CSS.suggestionContainer;
            this.#container.innerHTML = `<ul id="suggest-list" class="${Suggest.CSS.list}" role="listbox"></ul>`;
            this.#list = this.#container.querySelector('ul');
            this.#input.after(this.#container);
            this.hideSuggestions();

            this.bindUI();
        }

        bindUI() {
            this.#input.addEventListener('input', this.handleInput);
            this.#input.addEventListener('keydown', this.handleKeydown);
            // Use event delegation on the list
            this.#list.addEventListener('click', this.handleListClick);
            this.#list.addEventListener('mouseover', this.handleListMouseover);

            document.addEventListener('click', this.handleGlobalClick);
            L.on("search:search", this.disable);
        }

        unbindUI() {
            this.#input.removeEventListener('input', this.handleInput);
            this.#input.removeEventListener('keydown', this.handleKeydown);
            this.#list.removeEventListener('click', this.handleListClick);
            this.#list.removeEventListener('mouseover', this.handleListMouseover);

            document.removeEventListener('click', this.handleGlobalClick);
        }

        setSourceEndpoint(newEndpoint) {
            const basePath = (L.Model && L.Model.get(L.Model.BASE_PATH)) || Suggest.DEFAULTS.BASE_PATH;
            this.sourceEndpoint = basePath + newEndpoint;
        }

        handleInput() {
            clearTimeout(this.#queryDelayTimer);
            this.#queryDelayTimer = setTimeout(() => {
                const query = this.#input.value;
                this.fetchSuggestions(query);
            }, this.queryDelay);
        }

        async fetchSuggestions(query) {
            if (query.length < this.queryLength) {
                this.hideSuggestions();
                return;
            }

            if (this.#cache.has(query)) {
                this.renderSuggestions(this.#cache.get(query));
                return;
            }

            // Abort any previous fetch to prevent race conditions.
            this.#abortController?.abort();
            this.#abortController = new AbortController();
            const { signal } = this.#abortController;

            const urlEndpoint = this.sourceEndpoint.replace("{query}", encodeURIComponent(query));

            try {
                const response = await fetch(urlEndpoint, { signal });
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status} ${response.statusText}`);
                }
                const data = await response.json() || [];
                this.#cache.set(query, data);
                this.renderSuggestions(data);
            } catch (error) {
                if (error.name !== 'AbortError') { // Don't log aborted fetches as errors
                    console.error("Failed to fetch suggestions:", error);
                    this.hideSuggestions();
                }
            }
        }

        renderSuggestions(suggestions) {
            this.#suggestions = suggestions;
            this.#activeSuggestionIndex = -1;

            if (suggestions.length === 0) {
                this.hideSuggestions();
                return;
            }

            const fragment = document.createDocumentFragment();
            suggestions.forEach((suggestion, index) => {
                const li = document.createElement('li');
                li.className = Suggest.CSS.item;
                li.role = 'option';
                li.id = `suggest-item-${index}`;
                li.dataset.index = index;
                li.textContent = suggestion;
                fragment.appendChild(li);
            });

            this.#list.innerHTML = '';
            this.#list.appendChild(fragment);

            this.showSuggestions();
        }

        handleKeydown(event) {
            if (this.#container.hidden || this.#suggestions.length === 0) return;

            switch (event.key) {
                case 'ArrowDown':
                    event.preventDefault();
                    this.#activeSuggestionIndex = (this.#activeSuggestionIndex + 1) % this.#suggestions.length;
                    this.updateActiveSuggestion();
                    break;
                case 'ArrowUp':
                    event.preventDefault();
                    this.#activeSuggestionIndex = (this.#activeSuggestionIndex - 1 + this.#suggestions.length) % this.#suggestions.length;
                    this.updateActiveSuggestion();
                    break;
                case 'Enter':
                case 'Tab':
                    if (this.#activeSuggestionIndex > -1) {
                        event.preventDefault();
                        this.selectSuggestion(this.#activeSuggestionIndex);
                    }
                    break;
                case 'Escape':
                    event.preventDefault();
                    this.hideSuggestions();
                    break;
            }
        }

        updateActiveSuggestion() {
            for (const item of this.#list.children) {
                const index = parseInt(item.dataset.index, 10);
                if (index === this.#activeSuggestionIndex) {
                    item.classList.add(Suggest.CSS.itemActive);
                    this.#input.setAttribute('aria-activedescendant', item.id);
                    item.scrollIntoView({ block: 'nearest' });
                } else {
                    item.classList.remove(Suggest.CSS.itemActive);
                }
            }
        }

        selectSuggestion(index) {
            const selectedText = this.#suggestions[index];
            if (typeof selectedText !== 'undefined') {
                this.#input.value = selectedText;
                this.fire("suggest:select", {
                    suggestion: selectedText,
                    input: this.#input
                });
                this.hideSuggestions();
            }
        }

        handleGlobalClick(event) {
            // hide suggestions if the click is:
            //  in the input
            //  or anywhere outside the input or suggestions container
            if (event.target === this.#input || (!this.#input.contains(event.target) && !this.#container.contains(event.target))) {
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
                this.#activeSuggestionIndex = parseInt(targetItem.dataset.index, 10);
                this.updateActiveSuggestion();
            }
        }

        showSuggestions() {
            this.#container.hidden = false;
            this.#input.setAttribute('aria-expanded', 'true');
        }

        hideSuggestions() {
            this.#list.innerHTML = '';
            this.#container.hidden = true;
            this.#suggestions = [];
            this.#activeSuggestionIndex = -1;
            this.#input.setAttribute('aria-expanded', 'false');
            this.#input.removeAttribute('aria-activedescendant');
        }

        disable() {
            this.hideSuggestions();
            this.unbindUI();
            this.#input.readOnly = true;
            clearTimeout(this.#queryDelayTimer);
        }
    }

    // --- Global Setup ---
    L.addEventTarget(Suggest, { prefix: 'suggest' });
    L.Suggest = Suggest;

})();