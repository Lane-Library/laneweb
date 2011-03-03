/**
 * A tracker implementation that sends the data back to the web
 * application for history tracking.
 */
(function() {
    
    var HistoryTracker = function() {
        HistoryTracker.superclass.constructor.apply(this, arguments);
    };
    
    HistoryTracker.ATTRS = {
            io : {
                value : Y.io
            }
    };
    

    Y.extend(HistoryTracker, Y.Base, {
        track : function(trackingData) {
            this.get("io")("/././history/track", {
                method : "post",
                data : Y.JSON.stringify(trackingData),
                headers: {
                    "Content-Type" : "application/json"
                }
            });
        }
    });
    
    LANE.tracking.addTracker(new HistoryTracker());
    
})();