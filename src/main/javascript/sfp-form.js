(function () {
    "use strict";


    const Model = {
        currentType: null,
        defaultType: "book",
        setType(type) {
            this.currentType = type;
        },
        getType() {
            return this.currentType;
        }
    };

    const View = {
        form: document.querySelector("#sfp-form"),
        typeInput: null,

        init() {
            if (!this.form) return;
            this.typeInput = this.form.querySelector("#sfp-type");
        },

        removeActive() {
            this.form.querySelectorAll(".active").forEach(el => el.classList.remove("active"));
        },

        setActiveButton(type) {
            const button = this.form.querySelector(`#${type}`);
            if (button) button.classList.add("active");
        },

        setInputType(type) {
            if (this.typeInput) this.typeInput.value = type;
        },

        displayDiv(type) {
            const div = this.form.querySelector(`#sfp-${type}`);
            if (div) div.classList.add("active");
        },

        //Before the form is submitted, remove the buttons and the input not used
        // in the form by the type and remove the submit button in case of going back after submit 
        removeDuplicateElements(type) {
            if (type !== "book") {
                this.form.querySelector("#sfp-book").remove();
            }
            if (type !== "journal") {
                this.form.querySelector("#sfp-journal").remove();
            }
            this.form.querySelector(".sfp-suggestion").remove();
            this.form.querySelector("button").remove();
        }
    };

    // Controller
    const Controller = {
        init() {
            View.init();
            if (!View.form) return;

            this.bindEvents();
            this.setFormType(Model.defaultType);
        },

        bindEvents() {
            View.form.querySelectorAll(".contacts-choice li").forEach(btn => {
                btn.addEventListener("click", e => {
                    const type = e.currentTarget.id;
                    if (type) this.setFormType(type);
                });
            });

            View.form.addEventListener("submit", () => {
                View.removeDuplicateElements(Model.getType());
            });
        },

        setFormType(type) {
            Model.setType(type);
            View.removeActive();
            View.setInputType(type);
            View.setActiveButton(type);
            View.displayDiv(type);
        }
    };

    // Initialize the application
    document.addEventListener("DOMContentLoaded", Controller.init());

})();