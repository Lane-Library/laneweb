(function() {

    "use strict";

    var LightboxBg = Y.Base.create("lightboxbg", Y.Widget, [], {});

    var Lightbox = Y.Base.create("lightbox", Y.Widget, [ Y.WidgetPosition, Y.WidgetPositionAlign, Y.WidgetPositionConstrain ], {
        bindUI : function () {
            var doc = Y.one("doc");
            this.on("visibleChange", this._onVisibleChange);
            this.after("visibleChange", this._afterVisibleChange);
            // close on escape
            doc.on("key", this.hide, "esc", this);
            doc.on("click", this._lightboxLinkClick, this);
            this.get("background").on("click", this.hide, this);
            this.set("drag", new Y.DD.Drag({node: ".yui3-lightbox"}));
        },
        setContent : function(content) {
            this.get("contentBox").set("innerHTML", content);
            this.fire("contentChanged");
            this.centered();
        },
        _afterVisibleChange : function(event) {
            if (!event.newVal) {
                this.get("background").hide();
                this.set("disableBackground", false);
            }
        },
        _animate : function() {
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
            anim2.on("end", function() {
                boundingBox.setStyle("overflow", "visible");
                boundingBox.setStyle("width","auto");
                boundingBox.setStyle("height","auto");
                this.constrain([left, top], true);
                this.fire("animEnd");
            }, this);
            boundingBox.setStyle("overflow", "hidden");
            boundingBox.setStyle("width", "0");
            boundingBox.setStyle("height", "0");
            anim1.run();
            anim2.run();
        },
        _lightboxLinkClick: function(event) {
            var lightbox, model, basePath,  hash, url, regex, disableBackground,
                anchor = event.target.ancestor("a") || event.target,
                disableAnimation,
                rel = anchor.get("rel");
            if (rel && rel.indexOf("lightbox") === 0) {
                lightbox = this;
                model = Y.lane.Model;
                basePath = model.get(model.BASE_PATH) || "";
                hash = anchor.get("hash");
                event.preventDefault();
                if (lightbox.get("visible")) {
                    lightbox.hide();
                }
                // need to dynamically create regex for getting /plain url because
                // of various base paths (eg /stage)
                regex = new RegExp("(" + basePath + ")(.+)".replace(/\//g, "\\\/"));
                // case 112216
                url = anchor.get("pathname") + anchor.get('search');
                // first replace takes care of missing leading slash in IE < 10
                url = url.replace(/(^\/?)/,"/").replace(regex, "$1/plain$2");
                disableBackground = rel.indexOf("disableBackground") > -1;
                disableAnimation = rel.indexOf("disableAnimation") === -1;
                Y.io(url, {
                    on : {
                        success : function(id, o) {
                            lightbox.set("animate", disableAnimation);
                            lightbox.set("hash", hash);
                            lightbox.set("disableBackground", disableBackground);
                            lightbox.setContent(o.responseText);
                            lightbox.show();
                        }
                    }
                });
            }
        },
        _onVisibleChange : function(event) {
            if (event.newVal) {
                if (!this.get("disableBackground")) {
                    this.get("background").show();
                }
                if (this.get('animate')){
                    this._animate();
                }
            }
        }
    });

    Lightbox.ATTRS = {
        background: {
            value: new LightboxBg({
                visible : false,
                render : true
            })
        },
        animate : {
            value: true
        },
        disableBackground: {
            value: false
        },
        hash : {
            value : null
        }
    };

    Y.lane.Lightbox = new Lightbox({
        visible : false,
        render : true
    });

    // anchor with class=autoLightbox will automatically render on page load
    var initializeAutoLightbox = function() {
        var href, hash, autoLightboxAnchor = Y.one("a.autoLightbox");
        if (autoLightboxAnchor) {
            href = autoLightboxAnchor.get("href");
            hash = autoLightboxAnchor.get("hash");
            Y.io(href, {
                on : {
                    success : function(id, o) {
                        var lightbox = Y.lane.Lightbox;
                        lightbox.set("url", href);
                        lightbox.set("hash", hash);
                        lightbox.setContent(o.responseText);
                        lightbox.show();
                    }
                }
            });
        }
    };
    initializeAutoLightbox();
})();
