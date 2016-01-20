Y.applyConfig({fetchCSS:true});
Y.use('console','test', function(Y) {

    var mobileAdTestCase = new Y.Test.Case({
        name: 'back-to-top Test Case'
    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(mobileAdTestCase);
    Y.Test.Runner.masterSuite.name = "back-to-top-test.js";
    Y.Test.Runner.run();

});
