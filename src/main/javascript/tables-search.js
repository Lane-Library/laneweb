(function () {

  "use strict";

  /**
   * Filter tables list by input query
   */
  let tables = document.querySelectorAll('.table-search-container'),
    searchInput = document.querySelector('#table-search-input'),
    filtertables = function () {
      let filter = searchInput.value.toUpperCase();
      for (let j = 0; j < tables.length; j++) {
        let trs = tables[j].querySelectorAll("div [class='row']"), tds, txtValue;
        for (let i = 0; i < trs.length; i++) {
          tds = trs[i].querySelectorAll("div [class='cell']");
          txtValue = "";
          for (let y = 0; y < tds.length; y++) {
            if (tds[y]) {
              txtValue += tds[y].textContent || tds[y].innerText;
            }
          }
          if (txtValue.toUpperCase().indexOf(filter) > -1) {
            trs[i].style.display = "";
          } else {
            trs[i].style.display = "none";
          }
        }
      }
    };

  if (tables && searchInput) {
    searchInput.addEventListener("keyup", filtertables);

  }

})();