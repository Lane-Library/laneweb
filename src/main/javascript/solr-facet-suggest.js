(function() {

  "use strict";
  
if (document.querySelector(".solrFacets")) {

        var model = L.Model,
            basePath = model.get(model.BASE_PATH) || "",
            searchForm = document.querySelector(".search-form"),
            RESULT_NOT_FOUND ="No match found",
            facetsNode = searchForm.querySelector("input[name=facets]"),
            
            FacetSuggestion = function(input) {
                var facet = input.dataset.facet,
                searchTerm = input.dataset.searchterm,
                facets = input.dataset.facets,
                SOURCE_BASE = basePath + '/apps/solr/facet/suggest?q=' + searchTerm + '&contains={query}&facet=' + facet +'&facets='+ encodeURI(facets),
                suggest = new L.Suggest(input, 1, SOURCE_BASE),
                model = function(suggest, input) {
                    return {
                        suggest: suggest,
                        suggestionNode: input,
                    };
                }(suggest, input),
                view = function() {
                    return {
                        search: function(query) {
                            if (facetsNode && facetsNode.value != '') {
                                facetsNode.value = facetsNode.value + '::' + facet + ':"' + query + '"';
                            } else {
                                facetsNode.disabled = false;
                                facetsNode.value = facet + ':"' + query + '"';
                            }
                            if(query != RESULT_NOT_FOUND){
                                searchForm.submit();
                            }
                            else{
                                model.suggestionNode.value ='';
                             }
                            
                            
                        }
                    };
                }(),
                controller = function() {
                    return {
                        suggestion: function(event) {
                            view.search(event.suggestion);
                        }
                    };

                }();
                model.suggest.on("suggest:select", controller.suggestion);
            }

        document.querySelectorAll(".facet-suggestion").forEach(function(input) {
            new FacetSuggestion(input);
        });
    }
})();

