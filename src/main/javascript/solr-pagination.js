(() => {
    "use strict";

    document.querySelectorAll('.s-pagination form[name=pagination]').forEach(form => {
        form.addEventListener("submit", event => {
            const currentForm = event.currentTarget;
            const pageInput = currentForm.page;
            const totalPagesInput = currentForm.pages;

            // parse int from input and default to 0 if input is empty or invalid
            const requestedPage = parseInt(pageInput.value.replace(/[^\d]/g, ''), 10) || 0;
            const totalPages = parseInt(totalPagesInput.value, 10);

            // update the input field with the sanitized value
            pageInput.value = requestedPage;

            const isPageInvalid = requestedPage < 1 || requestedPage > totalPages;

            if (isPageInvalid) {
                event.preventDefault();
                const formParent = currentForm.parentNode;

                // only add the error message if it doesn't already exist
                if (!formParent.querySelector(".error")) {
                    const errorMessageHTML = '<div class="error">ERROR: page out of range</div>';
                    formParent.insertAdjacentHTML('afterbegin', errorMessageHTML);
                }
            } else {
                // if valid, remove the hidden 'pages' input before submitting
                currentForm.removeChild(totalPagesInput);
            }
        });
    });

})();
