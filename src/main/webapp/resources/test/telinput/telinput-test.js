YUI().use('node-event-simulate', 'console', 'test', function(T){

    var telinputTestCase = new T.Test.Case({
        name: "Lane telinput Testcase",
        input: T.one("input[type='tel']"),
        
        setUp: function() {
            this.input.set("value","");
        },
        
        testFirstCharOne: function() {
            this.input.set("value", "1");
            this.input.simulate("keyup");
            T.Assert.areEqual("1–", this.input.get("value"));
        },
        
        testFirstCharNotOne: function() {
            this.input.set("value", "2");
            this.input.simulate("keyup");
            T.Assert.areEqual("2", this.input.get("value"));
        },
        
        testAreaCode: function() {
            this.input.set("value", "617");
            this.input.simulate("keyup");
            T.Assert.areEqual("617–", this.input.get("value"));
        },
        
        testHas1dash: function() {
            this.input.set("value", "1–6");
            this.input.simulate("keyup");
            T.Assert.areEqual("1–6", this.input.get("value"));
        },
        
        testHas1dashAreaCode: function() {
            this.input.set("value", "1–617");
            this.input.simulate("keyup");
            T.Assert.areEqual("1–617–", this.input.get("value"));
        },
        
        testExchange: function() {
            this.input.set("value", "617–456");
            this.input.simulate("keyup");
            T.Assert.areEqual("617–456–", this.input.get("value"));
        },
    });
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    
    T.Test.Runner.add(telinputTestCase);
    T.Test.Runner.run();
});