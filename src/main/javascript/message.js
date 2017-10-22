// show a message to the user.  Initially just calls alert()
(function() {

    "use strict";

    Y.lane = Y.lane || {};
    Y.lane.showMessage = function(message) {
        alert(message);
    }

})();