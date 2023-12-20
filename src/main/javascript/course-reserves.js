(function() {

    "use strict";
    
    /**
     * Filter course reserves list by input query
     */
    let reservesList = document.querySelector('#course-reserves-list'),
        reservesSearch = document.querySelector('#course-reserves-search'),
        filterReservesList = function() {
            let filter = reservesSearch.value.toUpperCase(), 
            trs = reservesList.querySelectorAll("tr"), tds, txtValue;
            for (let i = 0; i < trs.length; i++) {
              tds = trs[i].querySelectorAll("td");
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
        };

    if (reservesList && reservesSearch) {
        reservesSearch.addEventListener("keyup", filterReservesList);
        
    }

})();