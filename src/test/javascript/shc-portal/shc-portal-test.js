YUI().use("test", "test-console", function(Y) {

    "use strict";

    document.querySelectorAll("form").forEach(function(form) {
        form.addEventListener("submit", function(event) {
            event.preventDefault();
        });
    });

    var shcPortalTestCase = new Y.Test.Case({

        name: "SHC Portal TestCase",

        "test tracking uptodate": function() {
            var event;
            var handle = L.on("tracker:trackablePageview", function(e) {
                event = e;
            });
            var submit = document.createEvent("Event");
            submit.initEvent("submit", true, true); 
            document.querySelector("form").dispatchEvent(submit);
            handle.detach();
            Y.Assert.areEqual("SHC-Epic UpToDate Search", event.title);
            Y.Assert.areEqual("www-uptodate-com.laneproxy.stanford.edu", event.host);
            Y.Assert.areEqual("/contents/search", event.path);
            Y.Assert.isTrue(event.external);
        },

        "test tracking lane search": function() {
            var event;
            var handle = L.on("tracker:trackablePageview", function(e) {
                event = e;
            });
            var submit = document.createEvent("Event");
            submit.initEvent("submit", false, true); 
            document.querySelector(".search-form").dispatchEvent(submit);
            handle.detach();
            Y.Assert.areEqual("SHC-Epic Lane search all-all", event.title);
            Y.Assert.areEqual("", event.host);
            Y.Assert.areEqual(L.Model.get(L.Model.BASE_PATH) + "/search.html", event.path);
            Y.Assert.isFalse(event.external);
        },

        "test tracking unexpected search": function() {
            var event = null;
            var handle = L.on("tracker:trackablePageview", function(e) {
                event = e;
            });
            var submit = document.createEvent("Event");
            submit.initEvent("submit", false, true); 
            document.querySelector("#unexpected").dispatchEvent(submit);
            handle.detach();
            Y.Assert.isNull(event);
        },

        "test pico": function() {
            var pInput = document.querySelector("input[name='p']");
            var iInput = document.querySelector("input[name='i']");
            var qInput = document.querySelectorAll("input[name='q']");
            pInput.value = "P";
            iInput.value = "I";
            qInput.item(0).value = "";
            qInput.item(1).value = "";
            var blur = document.createEvent("Event");
            blur.initEvent("blur", false, true); 
            pInput.dispatchEvent(blur);
            Y.Assert.areEqual("(P) AND (I)", document.querySelectorAll("input[name='q']").item(0).value);
            Y.Assert.areEqual("(P) AND (I)", document.querySelectorAll("input[name='q']").item(1).value);
        },

        "test pico no query": function() {
            var pInput = document.querySelector("input[name='p']");
            var iInput = document.querySelector("input[name='i']");
            var qInput = document.querySelectorAll("input[name='q']");
            pInput.value = "";
            iInput.value = "";
            qInput.item(0).value = "";
            qInput.item(1).value = "";
            var blur = document.createEvent("Event");
            blur.initEvent("blur", false, true); 
            pInput.dispatchEvent(blur);
            Y.Assert.areEqual("", document.querySelectorAll("input[name='q']").item(0).value);
            Y.Assert.areEqual("", document.querySelectorAll("input[name='q']").item(1).value);
        },

        "test pico parens query": function() {
            var pInput = document.querySelector("input[name='p']");
            var iInput = document.querySelector("input[name='i']");
            var qInput = document.querySelectorAll("input[name='q']");
            pInput.value = "(A";
            iInput.value = "";
            qInput.item(0).value = "";
            qInput.item(1).value = "";
            var blur = document.createEvent("Event");
            blur.initEvent("blur", false, true); 
            pInput.dispatchEvent(blur);
            Y.Assert.areEqual("A", document.querySelectorAll("input[name='q']").item(0).value);
            Y.Assert.areEqual("A", document.querySelectorAll("input[name='q']").item(1).value);
        },

        "test keyup": function() {
            var pInput = document.querySelector("input[name='p']");
            var iInput = document.querySelector("input[name='i']");
            var qInput = document.querySelectorAll("input[name='q']");
            pInput.value = "P";
            iInput.value = "I";
            qInput.item(0).value = "";
            qInput.item(1).value = "";
            var keyup = document.createEvent("Event");
            keyup.initEvent("keyup", false, true); 
            pInput.dispatchEvent(keyup);
            Y.Assert.areEqual("(P) AND (I)", document.querySelectorAll("input[name='q']").item(0).value);
            Y.Assert.areEqual("(P) AND (I)", document.querySelectorAll("input[name='q']").item(1).value);
        },

        "test keyup no values": function() {
            var pInput = document.querySelector("input[name='p']");
            var iInput = document.querySelector("input[name='i']");
            var qInput = document.querySelectorAll("input[name='q']");
            pInput.value = "";
            iInput.value = "";
            qInput.item(0).value = "";
            qInput.item(1).value = "";
            var keyup = document.createEvent("Event");
            keyup.initEvent("keyup", false, true); 
            iInput.dispatchEvent(keyup);
            Y.Assert.areEqual("", document.querySelectorAll("input[name='q']").item(0).value);
            Y.Assert.areEqual("", document.querySelectorAll("input[name='q']").item(1).value);
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(shcPortalTestCase);
    Y.Test.Runner.masterSuite.name = "shc-portal-test.js";
    Y.Test.Runner.run();

});