(function() {

    "use strict";

    // hide table columns lacking data
    document.querySelectorAll(".table-main.hide-empty-columns").forEach(function(node) {
        let columnIndex = 0,
            rowsLength = node.querySelectorAll('.table-row').length - 1;
        node.querySelectorAll('.table-head').forEach(function() {
            // all rows emtpy colunm
            let rows = node.querySelectorAll('.table-cell:nth-of-type(' + (++columnIndex) + '):empty');

            // if all rows in the colmun are empty, hide column
            if (rows.length == rowsLength) {
                node.querySelectorAll('.table-head:nth-of-type(' + columnIndex + '),' +
                    '.table-cell:nth-of-type(' + columnIndex + ')').forEach(function(n) {
                        n.style.display = 'none';
                    });
            }
        });

    });

})();
