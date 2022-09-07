(function() {

    "use strict";

    // hide table columns lacking data
    document.querySelectorAll(".table-main.hide-empty-columns").forEach(function(node) {
        var columnIndex = 0,
            rowsLength = node.querySelectorAll('.table-row').length - 1,
            isThereEmptyColunm = false;

        node.querySelectorAll('.table-head').forEach(function() {
            // all rows emtpy colunm
            var rows = node.querySelectorAll('.table-cell:nth-of-type(' + (++columnIndex) + '):empty');
            // if all rows in the colmun are empty, hide column
            if (rows.length == rowsLength) {
                isThereEmptyColunm = true;
                node.querySelectorAll('.table-head:nth-of-type(' + columnIndex + '),' +
                    '.table-cell:nth-of-type(' + columnIndex + ')').forEach(function(n) {
                        n.style.display = 'none';
                    });

            }

        });
        if (isThereEmptyColunm) {
            node.querySelectorAll('i.fa-angle-right,' +
                ' i.fa-angle-left').forEach(function(n) {
                    n.style.display = 'none';
                });
            if (node.classList.contains('print-access')) {
                node.querySelectorAll('.table-head:last-of-type,' +
                    '.table-cell:last-of-type').forEach(function(n) {
                        n.style.display = 'table-cell';
                    });
            }
        }
    });

    document.querySelectorAll(".table-main .table-head i.fa-angle-right, .table-main .table-head i.fa-angle-left").forEach(function(node) {
        node.addEventListener('click', function() {
            var table = node.closest('div.table-main');
            table.classList.toggle('shifted');
        });
    });



})();
