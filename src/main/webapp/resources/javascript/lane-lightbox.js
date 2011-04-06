(function() {

    var LightboxBg = Y.Base.create("lightboxbg", Y.Widget, []);

    Y.lane.LightboxBg = new LightboxBg({
        visible : false
    });

    var Lightbox = Y.Base.create("lightbox", Y.Widget, [ Y.WidgetPosition, Y.WidgetPositionAlign ]);

    Y.lane.Lightbox = new Lightbox({
        visible : false
    });   
    
    Y.lane.Lightbox.render();
    Y.lane.LightboxBg.render();
    
    
    Y.lane.Lightbox.get("contentBox").on("click", function(event) {
        Y.lane.Lightbox.hide();
        Y.lane.LightboxBg.hide();
    });

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
                        lightbox.get("contentBox").set("innerHTML", o.responseText);
                        lightbox.centered();
                        lightboxbg.show();
                        lightbox.show();
                    }
                }
            });
        }
    }, document);
})();
