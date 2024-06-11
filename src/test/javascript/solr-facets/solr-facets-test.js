/**
 * @author ryanmax
 */
YUI({ fetchCSS: false }).use("test", "test-console", function (Y) {

    "use strict";

    L.Model.set(L.Model.URL_ENCODED_QUERY, "foo");

    let dateSolrForm = document.querySelector("#solr-date-form"),
        startYearInput = document.querySelector(".date.start"),
        endYearInput = document.querySelector(".date.end"),
        errorMessage = document.querySelector("#facet-error-message"),
        submit = document.createEvent("Event");

    let solrFacetsTestCase = new Y.Test.Case({

        name: "Solr Facets Test Case",

        // Check Min Start Date
        testCheckStartValue: function () {
            startYearInput.value = 100;
            submit = new Event("submit", { bubbles: true, cancelable: true });
            dateSolrForm.dispatchEvent(submit);
            Y.Assert.areEqual("Please select a value that is no less than 1400.", errorMessage.textContent);
        },

        // Check Null Start Date
        testCheckNullValue: function () {
            startYearInput.value = '';
            submit = new Event("submit", { bubbles: true, cancelable: true });
            dateSolrForm.dispatchEvent(submit);
            Y.Assert.areEqual("Please enter a number.", errorMessage.textContent);
        },

        // Check if start Date bigger than than end Data
        testCheckEndDatesmallerThanStartDate: function () {
            startYearInput.value = 1963;
            endYearInput.value = 1900;
            dateSolrForm.dispatchEvent(submit);
            Y.Assert.areEqual("The start year should be smaller than the end year", errorMessage.textContent);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(solrFacetsTestCase);
    Y.Test.Runner.masterSuite.name = "solr-facets-test.js";
    Y.Test.Runner.run();

});
