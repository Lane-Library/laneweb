(function() {

    "use strict";

    document.querySelectorAll('.s-pagination form[name=pagination]').forEach(function(form) {
        form.addEventListener("submit", function(event) {
            let p = this.page,
                parent = this.parentNode,
                page = Number(p.value.replace(/[^\d]/g,'')),
                pages = this.pages,
                template;
            p.value = page;
            if (page < 1 || page > Number(pages.value)) {
                event.preventDefault();
                if (!parent.querySelector(".error")) {
                    template = document.createElement('div');
                    template.innerHTML = '<div class="error">ERROR: page out of range</div>';
                    parent.insertBefore(template.firstChild, parent.firstChild);
                }
            } else {
                this.removeChild(pages);
            }
        });
    });

})();
