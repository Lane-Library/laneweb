(() => {

    "use strict";

    document.addEventListener("click", event => {
        // is the click on a submit button?
        if (!event.target.matches('[type="submit"]')) {
            return;
        }

        // event.target is the submit button, so find the parent form
        const form = event.target.form;

        if (form) {
            form.classList.add("submitted");
        }
    });

})();
