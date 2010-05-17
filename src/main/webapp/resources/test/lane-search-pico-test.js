/**
 * @author ceyates
 */
YUI().use('lane-textinput','lane-suggest','node','anim','console', 'test', function(Y){
    
    Y.publish('lane:searchSourceChange',{broadcast:2});

    var searchpicoTestCase = new Y.Test.Case({
        name: "Lane Search PICO Testcase",
        testSourceChangeClinical : function() {
            var nav = Y.one('#laneNav');
            var search = Y.one('#search');
            Y.fire('lane:searchSourceChange', {
                    newVal:'clinical-all',
                    oldVal:'all-all'
                });
//            this.wait(function() {
                Y.Assert.isTrue(nav.hasClass('clinical'), 'nav not class clinical');
                Y.Assert.isTrue(search.hasClass('clinical'), 'search form not class clinical');
                Y.Assert.isTrue(Y.Lang.isObject(Y.one('#picoFields')), 'no pico fields');
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
                Y.Assert.isFalse(nav.hasClass('clinical'), 'nav class clinical');
                Y.Assert.isFalse(search.hasClass('clinical'), 'search form class clinical');
                Y.Assert.areEqual("none", picoFields.getStyle("display"));
//            }, 1000);
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(searchpicoTestCase);
    Y.Test.Runner.run();
});