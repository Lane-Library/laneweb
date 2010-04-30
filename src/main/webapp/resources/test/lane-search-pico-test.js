/**
 * @author ceyates
 */
YUI().use('lane-suggest','console', 'test', function(Y){
    
    Y.publish('lane:searchSourceChange',{broadcast:2});

    var searchpicoTestCase = new Y.Test.Case({
        name: "Lane Search PICO Testcase",
        testSourceChangeClinical : function() {
            var nav = Y.one('#search');
            var search = Y.one('#search');
            Y.fire('lane:searchSourceChange', {getSearchSource:function(){return 'clinical-all'}});
            Y.Assert.isTrue(nav.hasClass('clinical'), 'nav not class clinical');
            Y.Assert.isTrue(search.hasClass('clinical'), 'search form not class clinical');
            Y.Assert.isTrue(Y.Lang.isObject(Y.one('#picoFields')), 'no pico fields');
        },
        testSourceChangeNotClinical : function() {
            var nav = Y.one('#search');
            var search = Y.one('#search');
            Y.fire('lane:searchSourceChange', {getSearchSource:function(){return 'not-clinical'}});
            Y.Assert.isFalse(nav.hasClass('clinical'), 'nav class clinical');
            Y.Assert.isFalse(search.hasClass('clinical'), 'search form class clinical');
            Y.Assert.isFalse(Y.Lang.isObject(Y.one('#picoFields')), 'has pico fields');
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(searchpicoTestCase);
    Y.Test.Runner.run();
});