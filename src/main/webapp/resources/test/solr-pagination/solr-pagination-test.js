/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    var solrPaginationTestCase = new Y.Test.Case({
        name: "Solr Pagination TestCase",
        testSubmitInvalid1: function() {
            var form = Y.one("form"),
            parentDiv = Y.one(".s-pagination"),
            pages = form.get('pages');
            form.one("input[name=page]").set('value','999');
            form.simulate("submit");
            Y.Assert.areNotEqual(null, parentDiv.one(".error"));
            form.append(pages);
            parentDiv.one(".error").remove();
        },
        testSubmitInvalid2: function() {
            var form = Y.one("form"),
            parentDiv = Y.one(".s-pagination"),
            pages = form.get('pages');
            form.one("input[name=page]").set('value','0');
            form.simulate("submit");
            Y.Assert.areNotEqual(null, parentDiv.one(".error"));
            form.append(pages);
            parentDiv.one(".error").remove();
        },
        testSubmitValid: function() {
            var form = Y.one("form"),
            parentDiv = Y.one(".s-pagination"),
            pages = form.get('pages');
            form.one("input[name=page]").set('value','23');
            form.simulate("submit");
            Y.Assert.areEqual(null, parentDiv.one(".error"));
            form.append(pages);
        }
    });

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(solrPaginationTestCase);
    Y.Test.Runner.masterSuite.name = "solr-pagination-test.js";
    Y.Test.Runner.run();
});
