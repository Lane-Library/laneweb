(function() {

    "use strict";

    if (document.querySelector("#solr-date-form")) {

        let ERROR_MESSAGE_YEAR_START_GREATER_THAN_YEAR_END = "The start year should be smaller than the end year", 
            searchForm = document.querySelector(".search-form"),
            dateSolrForm = document.querySelector("#solr-date-form"),
            startYearInput = document.querySelector(".date.start"),
            endYearInput = document.querySelector(".date.end"),
            yearEndwith2Columns = /year:\[.*\]::/i,
            maybeStartwith2Coulnms = /(::)?year:\[.*\]/i,
            facets = searchForm.querySelector("input[name=facets]"),
            errorMessage  = document.querySelector("#facet-error-message"),
            model = function(input1, input2) {
                let m = {
                    startYear: input1,
                    endYear: input2,
                }
                return m;
            }(startYearInput, endYearInput),

            view = function() {
                return {
                    submitSearchForm: function() { 
                     if (!model.startYear.checkValidity()) {
                        errorMessage.textContent = model.startYear.validationMessage;
                    }
                    else if (!model.endYear.checkValidity()) {
                        errorMessage.textContent = model.endYear.validationMessage;
                    }
                    else if ( model.startYear.value > model.endYear.value) {
                        errorMessage.textContent = ERROR_MESSAGE_YEAR_START_GREATER_THAN_YEAR_END;
                        
                    }else{
                        searchForm.submit();   
                    }
                }
                }
            }(),
            controller = function() {
                return {
                    resetYearFromFacets: function() {
                        if (yearEndwith2Columns.exec(facets.value) != null) {
                            facets.value = facets.value.replace(yearEndwith2Columns, '');
                        }
                        else if (maybeStartwith2Coulnms.exec(facets.value) != null) {
                            facets.value = facets.value.replace(maybeStartwith2Coulnms, '');
                        }
                    },
                    setYearFromFacets: function() {
                        let currentYearFacetValue = 'year:[' + model.startYear.value + ' TO ' + model.endYear.value + ']';
                        if (model.startYear.value != '' || model.endYear.value != '') {
                            if (facets && facets.value != '') {
                                facets.value = facets.value + '::' + currentYearFacetValue;
                            } else {
                                facets.disabled = false;
                                facets.value = currentYearFacetValue;
                            }
                        }
                    },
                    
                }
            }();



        dateSolrForm.addEventListener("submit", function(event) {
            controller.resetYearFromFacets();
            controller.setYearFromFacets();
            view.submitSearchForm();
            event.preventDefault();
        });

    }



})();
