/**
 * 
 */
Y.lane.Banner = Y.Base.create("banner", Y.Widget, [], {
	bindUI : function() {
		this.get("contentBox").all(".banner-nav a").on("click", this._handleNavClick, this);
	},
	setNewContent : function(content) {
		this.get("contentBox").one(".banner-content").set("innerHTML", content);
	},
	setNewImage : function(src) {
		this.get("contentBox").one("img").set("src", src);
	},
    _handleNavClick : function(event) {
    	event.preventDefault();
    	this.get("contentBox").all(".banner-nav a").removeClass("banner-nav-active");
    	event.target.addClass("banner-nav-active");
        Y.io("/plain/samples/banner/banners.html" + event.target.getAttribute("href"), {
            on : {
                success : function(id, o, args) {
                	var fragment = Y.Node.create(o.responseText);
            		var childNodes = fragment.get("childNodes");
            		this.setNewImage(childNodes.item(2).getAttribute("src"));
            		var bannerContent = childNodes.item(3).one(".banner-content");
                	this.setNewContent(bannerContent ? bannerContent.get("innerHTML") : "");
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