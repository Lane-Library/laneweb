(function() {
    "use strict";
    let mock = function() {
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
    let singleton = false;
    L.Suggest = function() {
        if (!singleton) {
            singleton = new mock();
        }
        return singleton;
    };
})();

