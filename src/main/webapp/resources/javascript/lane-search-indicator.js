(function() {
    LANE.SearchIndicator = function() {
        var Y = LANE.Y,
            indicator = Y.one("#searchIndicator");
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
})();
