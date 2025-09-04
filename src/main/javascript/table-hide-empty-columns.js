(() => {

    "use strict";

    /**
     * Hides columns in a table if all of its data cells are empty.
     * Used in search results.
     */
    document.querySelectorAll(".table-main.hide-empty-columns").forEach(table => {
        const headers = table.querySelectorAll('.table-head');
        
        // If there are no headers, there's nothing to hide
        if (headers.length === 0) {
            return;
        }

        headers.forEach((header, index) => {
            // used with :nth-of-type, which is 1-based
            const colIndex = index + 1; 

            // Select all data cells in the current column, assuming the header is the first .table-row
            const dataCellsInColumn = table.querySelectorAll(`.table-row:not(:first-child) .table-cell:nth-of-type(${colIndex})`);

            // If there are no data cells in this column, there's nothing to hide
            if (dataCellsInColumn.length === 0) {
                return;
            }

            // An empty column is one where every single data cell is empty or only contains whitespace.
            const isColumnEmpty = Array.from(dataCellsInColumn).every(cell => 
                !cell.textContent.trim()
            );

            if (isColumnEmpty) {
                const allCellsInColumn = table.querySelectorAll(
                    `.table-head:nth-of-type(${colIndex}), .table-cell:nth-of-type(${colIndex})`
                );
                
                allCellsInColumn.forEach(cell => {
                    cell.style.display = 'none';
                });
            }
        });
    });

})();

