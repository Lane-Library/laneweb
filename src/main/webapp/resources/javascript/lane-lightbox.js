(function() {

    var LightboxBg = Y.Base.create("lightboxbg", Y.Widget, []);

    Y.lane.LightboxBg = new LightboxBg({
        visible : false,
        render : true
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
        render : true,
        constrain : true
    });  
    
    Y.lane.Lightbox.get("boundingBox").append("<a id='lightboxClose'></a>");
    Y.lane.Lightbox.get("boundingBox").one("#lightboxClose").on("click", function(event) {
        event.preventDefault();
      Y.lane.Lightbox.hide();
    });
    
    Y.lane.Lightbox.after("visibleChange", function(event) {
        if (!event.newVal) {
            Y.lane.LightboxBg.hide();
        }
    });
    
    Y.lane.Lightbox.on("visibleChange", function(event) {
        if (event.newVal) {
            Y.lane.LightboxBg.show();
            var boundingBox = this.get("boundingBox"),
                width = boundingBox.get("clientWidth"),
                height = boundingBox.get("clientHeight"),
                left = boundingBox.get("offsetLeft"),
                top = boundingBox.get("offsetTop"),
                anim1 = new Y.Anim({
                    node : boundingBox,
                    duration : 0.5,
                    to : {width:width, height:height, left:left, top:top},
                    from : {width:0, height:0, left:left + (width/2), top: top + (height/2)}
                }),
                contentBox = this.get("contentBox"),
                anim2 = new Y.Anim({
                    node : contentBox,
                    duration : 0.5,
                    from : {left:contentBox.get("clientWidth")/-2, top: contentBox.get("clientHeight")/-2},
                    to : {left:0,top:0}
                });
            anim1.on("end", function() {
            	boundingBox.setStyle("overflow", "visible");
            	boundingBox.setStyle("width","auto");
            	boundingBox.setStyle("height","auto");
            });
            boundingBox.setStyle("overflow", "hidden");
            anim1.run();
            anim2.run();
        }
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
            	//TODO: failure handler use window.location
                on : {
                    success : function(id, o, args) {
                        var lightbox = Y.lane.Lightbox;
                        lightbox.setContent(o.responseText);
                        lightbox.show();
                    }
                }
            });
        }
    }, document);
})();
