YUI.add("lane-search-indicator", function(Y) {

	var time = new Date().getTime();
    Y.namespace("lane");
    Y.lane.SearchIndicator = function() {
        var indicator = Y.one("#searchIndicator");
        if (!indicator) {
            indicator = Y.Node.create("<div id='searchIndicator'><img src='/././images/backgr/search-indicator.gif'/></div>");
            indicator.setStyle("display","none");
            Y.one("body").append(indicator);
        }
        return {
            show: function() {
                indicator.setStyle("display", "block");
            },
            hide: function() {
                indicator.setStyle("display", "none");
            }
        }
    };

    LANE.log("lane-search-indicator.js:add() " + (new Date().getTime() - time));
},"1.11.0-SNAPSHOT");
