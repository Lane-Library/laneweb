(function() {

    var LightboxBg = Y.Base.create("lightboxbg", Y.Widget, []);

    Y.lane.LightboxBg = new LightboxBg({
        visible : false
    });

    var Lightbox = Y.Base.create("lightbox", Y.Widget, [ Y.WidgetPosition, Y.WidgetPositionAlign, Y.WidgetPositionConstrain ], {
    		setContent : function(content) {
    			this.get("contentBox").set("innerHTML", content);
    			this.fire("contentChanged");
    			this.centered();
    		}
    });

    Y.lane.Lightbox = new Lightbox({
        visible : false,
        constrain : Y.one("body")
    });  
    
    Y.lane.Lightbox.get("boundingBox").append("<a id='lightboxClose'></a>");
    Y.lane.Lightbox.get("boundingBox").one("#lightboxClose").on("click", function(event) {
    	event.preventDefault();
      Y.lane.Lightbox.hide();
      Y.lane.LightboxBg.hide();
    });
    Y.lane.Lightbox.render();
    Y.lane.LightboxBg.render();
    

    Y.on("click", function(event) {
        var args, href, popupElement, title, body, regex, anchor = event.target
                .ancestor("a")
                || event.target, rel = anchor.get("rel");
        if (rel && rel.indexOf("lightbox") === 0) {
            event.preventDefault();
            // need to dynamically create regex for getting /plain url because
            // of various base paths (eg /stage)
            regex = new RegExp("(.+)//([^/]+)(/././)(.+)".replace(/\//g, "\\\/"));
            href = anchor.get("href").replace(regex, "$1//$2$3plain/$4");
            Y.io(href, {
                on : {
                    success : function(id, o, args) {
                        var lightbox = Y.lane.Lightbox,
                            lightboxbg = Y.lane.LightboxBg;
                        lightbox.setContent(o.responseText);
                        lightboxbg.show();
                        lightbox.show();
                    }
                }
            });
        }
    }, document);
})();
