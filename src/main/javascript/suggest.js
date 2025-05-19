
(function () {

    "use strict";

    let model = L.Model,
        basePath = model.get(model.BASE_PATH) || "",
        //DEFAULT_SOURCE_BASE = basePath + `/apps/suggest/getSuggestionList?q={query}&l=`,
        DEFAULT_QUERY_LENGTH = 3,
        SELECT = "suggest:select";

    class Suggest {
        constructor(input, sourceBase, minQueryLength) {
            this._input = input;
            this._input.autocomplete = 'off';
            this._ac = [];
            this.selectedItem = null;
            this.limit = null;
            this.queryLength = minQueryLength || DEFAULT_QUERY_LENGTH;
            this.sourceBase = basePath + sourceBase;
            this.isKeyDown = false;
            this.bindUI();
        }



        bindUI() {
            L.on("search:search", (e) => {
                this._destroy();
            });
            this._input.addEventListener('input', (event) => this._displaySuggestions(event));
        }


        _displaySuggestions() {
            let query = this._input.value,
                urlEndpoint = this.sourceBase.replace("{query}", encodeURIComponent(query));
            if (this.limit != null) {
                urlEndpoint += this.limit;
            }
            if (query.length >= this.queryLength) {
                fetch(urlEndpoint)
                    .then(response => response.json())
                    .then(data => {
                        let suggestions = data || [],
                            suggestionContainer = document.createElement('div'),
                            dropdown = document.createElement('ul');
                        dropdown.className = 'aclist-list';
                        suggestions.forEach(suggestion => {
                            let item = document.createElement('li');
                            item.className = 'aclist-item';
                            item.textContent = suggestion;
                            dropdown.appendChild(item);
                            suggestionContainer.className = 'aclist-content';
                        });
                        this._destroy();
                        this._ac = dropdown.querySelectorAll('.aclist-item');
                        suggestionContainer.appendChild(dropdown);
                        this._handleEvents();
                        this._input.after(suggestionContainer);

                    });
            } else {
                this._destroy();
            }
        }


        _handleEvents() {
            this._ac.forEach(item => {
                item.addEventListener('click', (event) => this._handleMouseClickItemChange(event));
                item.addEventListener('mouseenter', (event) => this._handleMouseEnterItemChange(event));
                item.addEventListener('mouseleave', (event) => this._handleMouseLeaveItemChange(event));
            });
            this._input.addEventListener('keydown', (event) => this._handleKeysDownChange(event));
            this._input.addEventListener('keyup', (event) => this._handleKeysUpChange(event));
            document.addEventListener("click", (event) => this._destroyOnClick(event));
        }

        _destroy() {
            if (this._ac.length !== 0) {
                this._ac.forEach(item => {
                    item.removeEventListener('click', (event) => this._handleMouseClickItemChange(event));
                    item.removeEventListener('mouseenter', (event) => this._handleMouseEnterItemChange(event));
                    item.removeEventListener('mouseleave', (event) => this._handleMouseLeaveItemChange(event));
                });
                this._input.removeEventListener('keydown', (event) => this._handleKeysDownChange(event));
                this._input.removeEventListener('keyup', (event) => this._handleKeysUpChange(event));
                document.removeEventListener("click", (event) => this._destroyOnClick(event));
                this._ac = [];
                document.querySelectorAll('.aclist-content').forEach(item => item.remove());
                this.selectedItem = null;
            }
        }


        _destroyOnClick(event) {
            if (!event.target.classList.contains('aclist-item')) {
                this._destroy();
            }
        }


        _updateInputValue(event) {
            this._input.value = event.target.textContent;
            this.fire(SELECT, {
                suggestion: event.target.textContent,
                input: this._input
            });
            this._destroy();
        }

        _handleKeysDownChange(event) {
            if (this._ac.length > 0) {
                if (event.key === 'ArrowDown' && !this.isKeyDown) {
                    this._handleArrowDownChange();
                    this.setKeyDown();
                } else if (event.key === 'ArrowUp' && !this.isKeyDown) {
                    this._handlArrowUpChange();
                    this.setKeyDown();
                    event.preventDefault();
                } else if (event.key === 'Enter' && this.selectedItem != null) {
                    this._updateInputValue({ target: this._ac[this.selectedItem] });
                    event.preventDefault();
                } else if (event.key === 'Tab' && this.selectedItem != null) {
                    this._updateInputValue({ target: this._ac[this.selectedItem] });
                    event.preventDefault();
                }
            }
        };

        setKeyDown() {
            this.isKeyDown = true;
            setTimeout(() => {
                this.isKeyDown = false;
            }, 100);
        }


        _removedActiveClass() {
            this._ac.forEach(item => {
                if (item.classList.contains('aclist-item-active')) {
                    item.classList.remove('aclist-item-active');
                }
            });
        }


        _handleKeysUpChange(event) {
            this._input.addEventListener('keyup', (event) => {
                if (event.key === 'ArrowDown' || event.key === 'ArrowUp' && this.isKeyDown) {
                    this.isKeyDown = false;
                }
            });
        }


        _handleArrowDownChange(event) {
            this._removedActiveClass();
            if (this.selectedItem == null) {
                this.selectedItem = 0;
            } else {
                if (++this.selectedItem === this._ac.length) {
                    this.selectedItem = 0;
                }
            }
            this._ac[this.selectedItem].classList.add('aclist-item-active');
        }

        _handlArrowUpChange(event) {
            this._removedActiveClass();
            if (!this.selectedItem) {
                this.selectedItem = this._ac.length - 1;
            } else {

                if (--this.selectedItem == -1) {
                    this.selectedItem = this._ac.length - 1;
                }
            }
            this._ac[this.selectedItem].classList.add('aclist-item-active');
        }

        _handleMouseEnterItemChange(event) {
            this.selectedItem = Array.from(this._ac).indexOf(event.target);
            event.target.classList.add('aclist-item-active');
        }

        _handleMouseLeaveItemChange(event) {
            this._removedActiveClass();
        }

        _handleMouseClickItemChange(event) {
            this._updateInputValue(event);
        }

        /**
         * Set the limit parameter for the request, setting it to the default if the value is empty.
         * @method setLimit
         * @param limit {String} the limit parameter
         */
        setLimit(limit) {
            this.limit = limit;
        }
    };

    //Add EventTarget attributes to the Suggest prototype
    L.addEventTarget(Suggest, {
        prefix: 'suggest'
    });

    //Add EventTarget attributes to the Suggest prototype
    L.addEventTarget(Suggest);

    //make the Suggest constructor globally accessible
    L.Suggest = Suggest;
})();
