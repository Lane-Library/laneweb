(function() {

    var LightboxBg = Y.Base.create("lightboxbg", Y.Widget, []);

    Y.lane.LightboxBg = new LightboxBg({
        visible : false,
        render : true
    });

    var Lightbox = Y.Base.create("lightbox", Y.Widget, [ Y.WidgetPosition, Y.WidgetPositionAlign, Y.WidgetPositionConstrain ], {
            setContent : function(content) {
                this.get("contentBox").set("innerHTML", content);
//                this.fire("contentChanged");
//                this.centered();
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
    
//    Y.lane.Lightbox.after("visibleChange", function(event) {
//        if (!event.newVal) {
//            Y.lane.LightboxBg.hide();
//            //TODO: figure out exactly which styles need to be reset and reset them only
//            this.get("boundingBox").setAttribute("style","");
//            this.get("contentBox").setContent("");
//        }
//    });
//    
//    Y.lane.Lightbox.on("visibleChange", function(event) {
//        if (event.newVal) {
//            Y.lane.LightboxBg.show();
//            var boundingBox = this.get("boundingBox");
//            boundingBox.setStyle("overflow", "hidden");
//            var width = boundingBox.get("clientWidth");
//            var height = boundingBox.get("clientHeight");
//            var left = boundingBox.get("offsetLeft");
//            var top = boundingBox.get("offsetTop");
//            var anim1 = new Y.Anim({
//                node : boundingBox,
//                duration : 0.3,
//                to : {width:width, height:height, left:left, top:top},
//                from : {width:0, height:0, left:left + (width/2), top: top + (height/2)}
//            });
//            var contentBox = this.get("contentBox");
//            contentBox.setStyle("position","relative");
//            var anim2 = new Y.Anim({
//                node : contentBox,
//                duration : 0.3,
//                from : {left:contentBox.get("clientWidth")/-2, top: contentBox.get("clientHeight")/-2},
//                to : {left:0,top:0}
//            });
//            anim2.on("end", function() {
//                boundingBox.setStyle("overflow", "visible");
//                contentBox.setAttribute("style","");
//            });
//            boundingBox.setStyle("width", 0);
//            boundingBox.setStyle("height", 0);
//            anim1.run();
//            anim2.run();
//        }
//    });
    

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
