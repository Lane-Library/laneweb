(function() {

    var LightboxBg = Y.Base.create("lightboxbg", Y.Widget, [], {});

    var Lightbox = Y.Base.create("lightbox", Y.Widget, [ Y.WidgetPosition, Y.WidgetPositionAlign, Y.WidgetPositionConstrain ], {
        bindUI : function () {
            this.on("visibleChange", this._onVisibleChange);
            this.after("visibleChange", this._afterVisibleChange);
            // close on escape
            Y.one("doc").on("key", this.hide, "esc", this);
            this.get("background").on("click", this.hide, this);
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
        url : {
            value : null
        },
        disableBackground: {
            value: false
        },
        background: {
            value: new LightboxBg({
                visible : false,
                render : true
            })
        },
        animate : {
            value: true
        }
    };

    Y.lane.Lightbox = new Lightbox({
        visible : false,
        render : true
    });

    //TODO: put more of this initialization into the Lightbox object
    Y.on("click", function(event) {
        var href, regex,
            anchor = event.target.ancestor("a") || event.target,
            rel = anchor.get("rel"),
            model = Y.lane.Model,
            basePath = model.get(model.BASE_PATH) || "",
            animation = true,
            disableBackground;
        if (rel && rel.indexOf("lightbox") === 0) {
            event.preventDefault();
            if (rel.indexOf("lightbox-noanim") === 0){
                animation = false;
            }
            // need to dynamically create regex for getting /plain url because
            // of various base paths (eg /stage)
            regex = new RegExp("(.+)//([^/]+)(" + basePath + "/)(.+)".replace(/\//g, "\\\/"));
            href = anchor.get("href").replace(regex, "$1//$2$3plain/$4");
            disableBackground = rel.indexOf("disableBackground") > -1;
            Y.io(href, {
                on : {
                    success : function(id, o) {
                        var lightbox = Y.lane.Lightbox;
                        lightbox.set("animate", animation);
                        lightbox.set("disableBackground", disableBackground);
                        lightbox.set("url", href);
                        lightbox.setContent(o.responseText);
                        lightbox.show();
                    }
                }
            });
        }
    }, document);

    // anchor with class=autoLightbox will automatically render on page load
    var initializeAutoLightbox = function() {
        var href, autoLightboxAnchor = Y.one("a.autoLightbox");
        if (autoLightboxAnchor) {
            href = autoLightboxAnchor.get("href");
            Y.io(href, {
                on : {
                    success : function(id, o) {
                        var lightbox = Y.lane.Lightbox;
                        lightbox.set("url", href);
                        lightbox.setContent(o.responseText);
                        lightbox.show();
                    }
                }
            });
        }
    };
    initializeAutoLightbox();
})();
