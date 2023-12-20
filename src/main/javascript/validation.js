(function() {

    "use strict";

    document.querySelectorAll('[type="submit"]').forEach(function(node) {
        node.addEventListener("click", function(event) {
            let form = event.target.form;
            if (form) {
                form.classList.add("submitted");
            }
        });
    });

})();
