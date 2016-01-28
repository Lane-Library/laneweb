(function() {

    "use strict";

    var Location = function() {
        this._stateProxy = window.location;
    };

    Y.augment(Location, Y.Attribute);

    Y.lane.Location = new Location();

})();