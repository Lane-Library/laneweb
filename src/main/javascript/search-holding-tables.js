(function() {

    "use strict";

    if (document.querySelector("#searchResults")) {

        document.querySelectorAll(".table-main .table-head i.fa-angle-right, .table-main .table-head i.fa-angle-left").forEach(function(node) {
            node.addEventListener('click', function() {
                var table = node.closest('div.table-main');
                table.classList.toggle('shifted');
            });
        });

    }

})();
