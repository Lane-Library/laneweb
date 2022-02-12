(function() {

    "use strict";

    // hide table columns lacking data
    document.querySelectorAll("table.hide-empty-columns").forEach(function(node) {
        var columnIndex = 0;

        node.querySelectorAll('th').forEach(function() {

            // all rows of each column
            var rows = node.querySelectorAll('tr td:nth-child(' + (columnIndex++) + ')'),
                rowsLength = rows.length,
                emptyRows = 0;

            rows.forEach(function(row) {
                if (row.textContent.trim().length == 0) {
                    emptyRows++;
                }
            });

            // if all rows in the colmun are empty, hide column
            if (emptyRows > 0 && emptyRows == rowsLength) {
                node.querySelectorAll('tr th:nth-child(' + (columnIndex - 1) + ')').forEach(function(n) {
                    n.style.display = 'none';
                });
                node.querySelectorAll('tr td:nth-child(' + (columnIndex - 1) + ')').forEach(function(n) {
                    n.style.display = 'none';
                });
            }
        });
    });
})();
