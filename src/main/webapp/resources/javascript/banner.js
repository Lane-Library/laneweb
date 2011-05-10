/**
 * 
 */
Y.lane.Banner = Y.Base.create("banner", Y.Widget, [], {
    autoNext : function() {
    	var self = this;
    	var recursion = function() {
    		if (self.get("automate")) {
        		self.next();
        		self.autoNext();
    		}
    	};
    	window.setTimeout(recursion, 5000);
    },
	bindUI : function() {
		this.get("navNodes").on("click", this._handleNavClick, this);
		this.on("indexChange", this._handleIndexChange);
		this.autoNext();
	},
	next : function() {
		var index = this.get("index"),
		    navNodes = this.get("navNodes"),
		    next = (index + 1) >= navNodes.size() ? 0 : index + 1;
		this.set("index", next);
	},
	setNewContent : function(imgSrc, content) {
		var contentBox = this.get("contentBox");
		contentBox.one("img").set("src", imgSrc);
		contentBox.one(".banner-content").set("innerHTML", content);
	},
	_handleIndexChange : function(event) {
		var navNodes = this.get("navNodes");
		navNodes.item(event.prevVal).removeClass("banner-nav-active");
		navNodes.item(event.newVal).addClass("banner-nav-active");
        Y.io("/././plain/samples/banner/banners.html?banner=" + (event.newVal + 1), {
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
	},
    _handleNavClick : function(event) {
    	event.preventDefault();
    	this.set("automate", false);
    	this.set("index", this.get("navNodes").indexOf(event.target));
    }
});

Y.lane.Banner.ATTRS = {
	automate : {},
	index : {
		value : 0
	},
	navNodes : {
		valueFn : function() {
			return this.get("contentBox").all(".banner-nav a");
		}
	}
};

if (Y.one("#banner")) {
	var banner = new Y.lane.Banner({
		srcNode : "#banner",
		render : true,
		automate : true
	});
}