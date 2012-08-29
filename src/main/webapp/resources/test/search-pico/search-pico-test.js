Y.applyConfig({fetchCSS:true});
Y.use('console', "node-event-simulate", 'test', function(Y){

    var searchpicoTestCase = new Y.Test.Case({
        name: "Lane Search PICO Testcase",
        
        testSourceChangeClinical : function() {
            Y.fire('lane:searchSourceChange', {
                newVal:'clinical-all',
                oldVal:'all-all'
            });
            var nav = Y.one('#laneNav');
            var search = Y.one('#search');
                Y.Assert.isTrue(nav.hasClass('clinical'), 'nav not class clinical');
                Y.Assert.isTrue(search.hasClass('clinical'), 'search form not class clinical');
                Y.Assert.isTrue(Y.Lang.isObject(Y.one('#picoFields')), 'no pico fields');
        },
        testSourceChangeNotClinical : function() {
            Y.fire('lane:searchSourceChange', {
                newVal:'all-all',
                oldVal:'clinical-all'
            });
            var nav = Y.one('#laneNav');
            var search = Y.one('#search');
            var picoFields = Y.one("#picoFields");
            Y.Assert.isFalse(nav.hasClass('clinical'), 'nav class clinical');
                Y.Assert.isFalse(search.hasClass('clinical'), 'search form class clinical');
                Y.Assert.areEqual("none", picoFields.getStyle("display"));
        },
        testSetPatientCondition: function() {
            //simulate blur broken in IE http://yuilibrary.com/projects/yui3/ticket/2531702
            if (!Y.UA.ie) {
                Y.fire('lane:searchSourceChange', {
                    newVal:'clinical-all',
                    oldVal:'all-all'
                });
                var clinicalP = Y.one("#clinicalP");
                var clinicalI = Y.one("#clinicalI");
                var searchTerms = Y.one("#searchTerms");
                clinicalP.set("value", "foo");
                clinicalP.simulate("blur");
                Y.Assert.areEqual("foo", searchTerms.get("value"));
                clinicalI.set("value","bar");
                clinicalI.simulate("blur");
                Y.Assert.areEqual("(foo) AND (bar)", searchTerms.get("value"));
            }
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(searchpicoTestCase);
    Y.Test.Runner.run();
});