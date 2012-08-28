/**
 * @author ceyates
 */
YUI().use('console', "node-event-simulate", 'test', function(T){
    
//    Y.publish('lane:searchSourceChange',{broadcast:2});

    var searchpicoTestCase = new T.Test.Case({
        name: "Lane Search PICO Testcase",
        testSourceChangeClinical : function() {
            var nav = Y.one('#laneNav');
            var search = Y.one('#search');
            Y.fire('lane:searchSourceChange', {
                    newVal:'clinical-all',
                    oldVal:'all-all'
                });
//            this.wait(function() {
                T.Assert.isTrue(nav.hasClass('clinical'), 'nav not class clinical');
                T.Assert.isTrue(search.hasClass('clinical'), 'search form not class clinical');
                T.Assert.isTrue(T.Lang.isObject(Y.one('#picoFields')), 'no pico fields');
//            }, 1000);
        },
        testSourceChangeNotClinical : function() {
            var nav = Y.one('#laneNav');
            var search = Y.one('#search');
            var picoFields = Y.one("#picoFields");
            Y.fire('lane:searchSourceChange', {
                    newVal:'all-all',
                    oldVal:'clinical-all'
                });
//            this.wait(function() {
                T.Assert.isFalse(nav.hasClass('clinical'), 'nav class clinical');
                T.Assert.isFalse(search.hasClass('clinical'), 'search form class clinical');
                T.Assert.areEqual("none", picoFields.getStyle("display"));
//            }, 1000);
        },
        testSetPatientCondition: function() {
            var clinicalP = T.one("#clinicalP");
            var clinicalI = T.one("#clinicalI");
            var searchTerms = Y.one("#searchTerms");
            Y.publish("lane:suggestSelect",{
                broadcast:2
            });
            Y.fire('lane:searchSourceChange', {
                newVal:'clinical-all',
                oldVal:'all-all'
            });
            clinicalP.set("value", "foo");
            clinicalP.simulate("blur");
            T.Assert.areEqual("foo", searchTerms.get("value"));
            clinicalI.set("value","bar");
            clinicalI.simulate("blur");
            T.Assert.areEqual("(foo) AND (bar)", searchTerms.get("value"));
        }
    });
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    
    T.Test.Runner.add(searchpicoTestCase);
    T.Test.Runner.run();
});