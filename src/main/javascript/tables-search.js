(() => {

    "use strict";

    /**
     * Filter table-like structures based on a user's text input.
     * Used on course reserves, liaisons, and equipment tables.
     */
    const searchInput = document.querySelector('#table-search-input');
    const tableContainers = document.querySelectorAll('.table-search-container');

    // Do nothing if the search input or table containers are not found
    if (!searchInput || tableContainers.length === 0) {
        return;
    }

    const filterRows = () => {
        const query = searchInput.value.toUpperCase();

        tableContainers.forEach(container => {
            const rows = container.querySelectorAll(".row");

            rows.forEach(row => {
                const cells = row.querySelectorAll(".cell");

                // get textContent from all cells and join them into a single string
                const rowText = Array.from(cells, cell => cell.textContent || "")
                                     .join(" ")
                                     .toUpperCase();

                const isMatch = rowText.includes(query);
                row.style.display = isMatch ? "" : "none";
            });
        });
    };

    searchInput.addEventListener("keyup", filterRows);
    L.on("searchReset:reset", filterRows);

})();
