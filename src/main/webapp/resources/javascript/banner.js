/**
 *
 */
Y.lane.Banner = Y.Base.create("banner", Y.Widget, [], {
    autoNext : function() {
    	var self = this;
    	var recursion = function() {
    		if (self.get("automate")) {
        		self.next();
        		if (self.get("cycleCount") < self.get("maxCycles")) {
            		self.autoNext();
        		}
    		}
    	};
    	window.setTimeout(recursion, 10000);
    },
	bindUI : function() {
		var navNodes = this.get("navNodes"),
		    doc = Y.one("doc");
		if (navNodes.size() > 0) {
		    // prev/next on left/right arrows
            doc.on("key", this.prev, "up:37", this);
            doc.on("key", this.next, "up:39", this);
			navNodes.on("click", this._handleNavClick, this);
			this.on("indexChange", this._handleIndexChange);
			this.autoNext();
		}
	},
	next : function() {
		var index = this.get("index"),
		    navNodes = this.get("navNodes"),
		    next = (index + 1) >= navNodes.size() ? 0 : index + 1;
		if (next === 0) {
			this.set("cycleCount", this.get("cycleCount") + 1);
		}
		this.set("index", next);
	},
	prev : function() {
	    var index = this.get("index"),
	    navNodes = this.get("navNodes"),
	    prev = (index - 1) < 0 ? navNodes.size() - 1 : index - 1;
	    this.set("index", prev);
	},
	setNewContent : function(imgSrc, content) {
		var contentBox = this.get("contentBox");
		contentBox.one("img").set("src", imgSrc);
		contentBox.one(".banner-content").set("innerHTML", content);
	},
	_handleIndexChange : function(event) {
		var navNodes = this.get("navNodes"),
		    model = Y.lane.Model,
		    basePath = model.get(model.BASE_PATH) || "";
		//don't do anything if the index hasn't actually changed
		if (event.newVal !== event.prevVal) {
	        navNodes.item(event.prevVal).removeClass("banner-nav-active");
	        navNodes.item(event.newVal).addClass("banner-nav-active");
	        Y.io(basePath + "/plain/includes/banner/banners.html?banner=" + (event.newVal + 1), {
	            on : {
	                success : function(id, o) {
	                    var fragment = Y.Node.create(o.responseText),
	                        childNodes = fragment.get("childNodes"),
	                        imgSrc = childNodes.filter("img").item(0).getAttribute("src"),
	                        bannerContent = childNodes.filter(".banner-content").item(0);
	                    this.setNewContent(imgSrc, bannerContent ? bannerContent.get("innerHTML") : "");
	                }
	            },
	            context : this
	        });
		}
	},
    _handleNavClick : function(event) {
    	event.preventDefault();
    	this.set("automate", false);
    	this.set("index", this.get("navNodes").indexOf(event.target));
    }
});

Y.lane.Banner.ATTRS = {
	automate : {},
	cycleCount : {
		value: 0
	},
	index : {
		value : 0
	},
	maxCycles : {
		value : 2
	},
	navNodes : {
		valueFn : function() {
			return this.get("contentBox").all(".banner-nav a");
		}
	}
};

(function() {
	if (Y.one("#banner")) {
		var banner = new Y.lane.Banner({
			srcNode : "#banner",
			automate : true
		});
		banner.render();
	}
})();