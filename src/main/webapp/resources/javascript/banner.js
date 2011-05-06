/**
 * 
 */
Y.lane.Banner = Y.Base.create("banner", Y.Widget, [], {
	bindUI : function() {
		this.get("contentBox").all(".banner-nav a").on("click", this._handleNavClick, this);
	},
	setNewContent : function(imgSrc, content) {
		var contentBox = this.get("contentBox");
		contentBox.one("img").set("src", imgSrc);
		contentBox.one(".banner-content").set("innerHTML", content);
	},
	setNewImage : function(src) {
	},
    _handleNavClick : function(event) {
    	event.preventDefault();
    	this.get("contentBox").all(".banner-nav a").removeClass("banner-nav-active");
    	event.target.addClass("banner-nav-active");
        Y.io("/plain/samples/banner/banners.html" + event.target.getAttribute("href"), {
            on : {
                success : function(id, o, args) {
                	var fragment = Y.Node.create(o.responseText),
            		    childNodes = fragment.get("childNodes"),
            		    imgSrc = childNodes.filter("img").getAttribute("src"),
            		    bannerContent = childNodes.filter("div").item(0).one(".banner-content");
                	this.setNewContent(imgSrc, bannerContent ? bannerContent.get("innerHTML") : "");
                },
                failure : function() {
                    window.location = event.target.get("href");
                }
            },
            context : this
        });
    }
});

if (Y.one("#banner")) {
	var banner = new Y.lane.Banner({
		srcNode : "#banner",
		render : true
	});
}