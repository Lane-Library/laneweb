/**
 * @author ceyates
 */
YUI().use('node','anim','console', 'test', function(Y){
    
    Y.publish('lane:searchSourceChange',{broadcast:2});

    var searchpicoTestCase = new Y.Test.Case({
        name: "Lane Search PICO Testcase",
        testSourceChangeClinical : function() {
            var nav = Y.one('#laneNav');
            var search = Y.one('#search');
            Y.fire('lane:searchSourceChange', {
                getSearchSource:function(){
                    return 'clinical-all';
                }
            });
            this.wait(function() {
                Y.Assert.isTrue(nav.hasClass('clinical'), 'nav not class clinical');
                Y.Assert.isTrue(search.hasClass('clinical'), 'search form not class clinical');
                Y.Assert.isTrue(Y.Lang.isObject(Y.one('#picoFields')), 'no pico fields');
            }, 1000);
        },
        testSourceChangeNotClinical : function() {
            var nav = Y.one('#laneNav');
            var search = Y.one('#search');
            Y.fire('lane:searchSourceChange', {getSearchSource:function(){return 'not-clinical';}});
            this.wait(function() {
                Y.Assert.isFalse(nav.hasClass('clinical'), 'nav class clinical');
                Y.Assert.isFalse(search.hasClass('clinical'), 'search form class clinical');
            }, 1000);
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(searchpicoTestCase);
    Y.Test.Runner.run();
});