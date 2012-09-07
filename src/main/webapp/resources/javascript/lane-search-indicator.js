(function() {
	var basePath = Y.lane.Model.get("base-path") || "";
    LANE.SearchIndicator = function() {
        var indicator = Y.one("#searchIndicator");
        if (!indicator) {
            indicator = Y.Node.create("<div id='searchIndicator'><img src='" + basePath + "/resources/images/search-indicator.gif'/></div>");
            indicator.setStyle("display","none");
            Y.one("body").append(indicator);
        }
        return {
            show: function() {
                indicator.setStyle("display", "block");
                // IE requires a kick-start to animate indicator gif after form submitted
                if (Y.UA.ie){
                    setTimeout(function(){
                        indicator.one('img').set('src',indicator.one('img').get('src'));
                    },100);
                }
            },
            hide: function() {
                indicator.setStyle("display", "none");
            }
        };
    };
})();
