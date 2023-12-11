YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let bassettTestCase = new Y.Test.Case({

        name : 'Lane Basset Test Case',

        testSeeAllClick : function() {
            Y.one('.see-all').simulate('click');
            Y.Assert.areEqual("/plain/biomed-resources/bassett/raw/bassettsView.html?r=Abdomen", Y.one("#bassettContent span").get("text"));
            Y.Assert.areEqual("block", Y.one("#anotherItem").getStyle("display"));
        },

        testHideClick : function() {
            Y.one('.see-all').simulate('click');
            let another = Y.one("#anotherItem");
            Y.Assert.areEqual("none", another.getStyle("display"));
        },

        testSurlineSubRegion : function() {
            let li = Y.all('#bassett-menu li').item(1);
            let i = li.one("i");
            Y.Assert.isFalse(li.hasClass("enabled"));
            Y.Assert.isTrue(i.hasClass("fa-circle-o"));
            Y.Assert.isFalse(i.hasClass("fa-check-circle"));
            li.simulate('click');
            Y.Assert.isTrue(li.hasClass("enabled"));
            Y.Assert.isTrue(i.hasClass("fa-check-circle"));
            Y.Assert.isFalse(i.hasClass("fa-circle-o"));
        },

        testFoo: function() {
            Y.all('#bassett-menu a').item(2).simulate("click");
            Y.Assert.areEqual("/plain/biomed-resources/bassett/raw/bassettsView.html?r=Abdomen--Adrenal+Gland", Y.one("#bassettContent span").get("text"));
        },

        testDiagramLink : function() {
            Y.one('#diagram-choice').simulate('click');
            Y.Assert.areEqual("/plain/biomed-resources/bassett/raw/bassettsView.html?r=Abdomen--Central%20Nervous%20System&t=diagram&page=1&t=diagram", Y.one("#bassettContent span").get("text"));
        },

        testPhotoLink : function() {
            Y.one('#photo-choice').simulate('click');
            Y.Assert.areEqual("/plain/biomed-resources/bassett/raw/bassettsView.html?r=Abdomen--Central%20Nervous%20System&page=1", Y.one("#bassettContent span").get("text"));
        },

        testSubmitRemovesPages: function() {
            Y.one("form").simulate("submit");
            Y.Assert.isNull(Y.one("input[name=pages]"));
        },

        testPageValueNotNumber: function() {
            Y.one('.see-all').simulate('click');
            let form = Y.one("form"), prevented;
            Y.one('.bassett-error').setStyle('display', 'none');
            form.one("input[name=page]").set("value", "foo");
            form._node.addEventListener("submit", function(event) {
                prevented = event.defaultPrevented;
            });
            Y.one("form").simulate("submit");
            Y.Assert.isFalse(!prevented);
            Y.Assert.areEqual("block", Y.one(".bassett-error").getStyle("display"));
            Y.Assert.isNotNull(Y.one("input[name=pages]"));
        },

        testPageLessThanOne: function() {
            Y.one('.see-all').simulate('click');
            let form = Y.one("form"), prevented;
            Y.one('.bassett-error').setStyle('display', 'none');
            form.one("input[name=page]").set("value", "0");
            form._node.addEventListener("submit", function(event) {
                prevented = event.defaultPrevented;
            });
            Y.one("form").simulate("submit");
            Y.Assert.isFalse(!prevented);
            Y.Assert.areEqual("block", Y.one(".bassett-error").getStyle("display"));
            Y.Assert.isNotNull(Y.one("input[name=pages]"));
        },

        testPageMoreThanPages: function() {
            Y.one('.see-all').simulate('click');
            let form = Y.one("form"), prevented;
            Y.one('.bassett-error').setStyle('display', 'none');
            form.one("input[name=page]").set("value", "10");
            form._node.addEventListener("submit", function(event) {
                prevented = event.defaultPrevented;
            });
            Y.one("form").simulate("submit");
            Y.Assert.isFalse(!prevented);
            Y.Assert.areEqual("block", Y.one(".bassett-error").getStyle("display"));
            Y.Assert.isNotNull(Y.one("input[name=pages]"));
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(bassettTestCase);
    Y.Test.Runner.masterSuite.name = "bassett-test.js";
    Y.Test.Runner.run();

});