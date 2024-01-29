(function() {
    "use strict";
    var mock = function() {
        return {
            fire: function() {
                this.fn.call(this, {suggestion: "suggestion"});
            },
            setLimit: function(limit) {
                this.limit = limit;
            },
            on: function(type, fn) {
                this.type = type;
                this.fn = fn;
            }
        };
    };
    var singleton = false;
    L.Suggest = function() {
        if (!singleton) {
            singleton = new mock();
        }
        return singleton;
    };
})();

