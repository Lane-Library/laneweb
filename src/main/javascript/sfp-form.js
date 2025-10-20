(() => {

    "use strict";

    const form = document.querySelector("#sfp-form");

    // bail out if the form doesn't exist
    if (!form) return;

    /**
     * Class to control the state and behavior of the SFP form (#sfp-form).
     */
    class SFPFormController {
        constructor(formElement) {
            this.form = formElement;

            // --- centralized selectors ---
            this.selectors = {
                typeInput: "#sfp-type",
                choiceButtons: ".contacts-choice li",
                contentPrefix: "#sfp-",
                activeClass: "active",
                suggestionContainer: ".sfp-suggestion",
                choiceContainer: ".contacts-choice"
            };

            this.typeInput = this.form.querySelector(this.selectors.typeInput);
            this.choiceButtons = this.form.querySelectorAll(this.selectors.choiceButtons);

            // if critical child elements are missing, bail and log an error
            if (!this.typeInput || this.choiceButtons.length === 0) {
                console.error("SFPFormController: Could not find required child elements (type input or choice buttons).");
                return;
            }

            // --- State ---
            this.defaultType = this.form.dataset.defaultType || "book";
            this.currentType = this.defaultType;

            this.init();
        }

        init = () => {
            this.choiceButtons.forEach(btn => {
                btn.addEventListener("click", this._handleTypeChange);
            });
            this.form.addEventListener("submit", this._cleanupFormOnSubmit);
            this.setFormType(this.defaultType);
        }

        /**
         * cleanup method to remove event listeners
         */
        destroy = () => {
            this.choiceButtons.forEach(btn => {
                btn.removeEventListener("click", this._handleTypeChange);
            });
            this.form.removeEventListener("submit", this._cleanupFormOnSubmit);
        }

        setFormType = (type) => {
            if (!type) return;
            this.currentType = type;

            this.form.querySelectorAll(`.${this.selectors.activeClass}`).forEach(el => el.classList.remove(this.selectors.activeClass));

            const newActiveButton = this.form.querySelector(`#${type}`);
            const newActiveContent = this.form.querySelector(`${this.selectors.contentPrefix}${type}`);

            newActiveButton?.classList.add(this.selectors.activeClass);
            newActiveContent?.classList.add(this.selectors.activeClass);

            this.typeInput.value = this.currentType;
        }

        _handleTypeChange = (event) => {
            this.setFormType(event.currentTarget.id);
        }

        _cleanupFormOnSubmit = () => {
            this.form.querySelectorAll(`${this.selectors.contentPrefix}book, ${this.selectors.contentPrefix}journal`)
                .forEach(el => {
                    if (!el.id.endsWith(this.currentType)) el.remove();
                });
            this.form.querySelector(this.selectors.suggestionContainer)?.remove();
            this.form.querySelector(this.selectors.choiceContainer)?.remove();
        }
    }

    form.sfpController = new SFPFormController(form);

})();
