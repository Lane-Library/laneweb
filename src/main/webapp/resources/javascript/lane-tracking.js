Y.on("click", function(event) {
	var target = event.target,
	    ancestor = target.ancestor("a"),
	    link;
	if (ancestor) {
		link = ancestor.plug(Y.lane.LinkPlugin).link;
	} else {
		link = target.plug(Y.lane.LinkPlugin).link;
	}
	Y.fire("trackable", link, event);
});


//put in a delay for webkit (safari) to make the tracking request:
if (Y.UA.webkit) {
	Y.on("trackable", function(link, event) {
		var node, href, goToHref, trackingData = link.get("trackingData");
		if (link.get("trackable") && trackingData.external) {
			node = link.get("host");
			href = node.get("href");
			goToHref = function() {
	            window.location = href;
			};
            //  don't prevent default if it's
            //  a popup or facet
            //    (can't halt facet click propagation b/c they need to be tracked)
			if (href && (!node.get('rel') && !node.get('target'))
                     && !node.get('parentNode').hasClass('searchFacet')) {
                event.preventDefault();
                setTimeout(goToHref, 100);
			}
		}
	});
}