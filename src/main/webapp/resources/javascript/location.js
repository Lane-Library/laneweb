(function() {

    var Location = function(obj) {
        this._stateProxy = obj.location;
    };

    Y.augment(Location, Y.Attribute);

    Y.lane.Location = new Location(window);

    Y.lane.TopLocation = new Location(window.top);

})();