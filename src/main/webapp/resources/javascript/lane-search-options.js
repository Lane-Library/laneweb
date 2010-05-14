/*
 * adds and animates show/hide toggle for search results
 * 
 */
YUI().use('node-base','anim', function(Y) {
	var options = Y.Node.create('<span id="optionsToggle" class="closed"><a href="">Hide Options</a></span>'),
		optionsNode = Y.one("#searchOptions"),
		closeResultsAnim = new Y.Anim( {
			node : '#searchResults',
			easing : Y.Easing.easeOut,
			duration : 0.3,
			to : {
				marginLeft : '-185px'
			}
		}),
		openResultsAnim = new Y.Anim( {
			node : '#searchResults',
			easing : Y.Easing.easeOut,
			duration : 0.3,
			to : {
				marginLeft : 0
			}
		}),
		closePopinAnim = new Y.Anim( {
			node : '#popInContent',
			easing : Y.Easing.easeOut,
			duration : 0.3,
			to : {
			marginLeft : '-185px'
		}
		}),
		openPopinAnim = new Y.Anim( {
			node : '#popInContent',
			easing : Y.Easing.easeOut,
			duration : 0.3,
			to : {
			marginLeft : 0
		}
		}),
		closeFacetsAnim = new Y.Anim( {
			node : '#searchFacets',
			easing : Y.Easing.easeOut,
			duration : 0.3,
			to : {
			opacity : 0
		}
		}),
		openFacetsAnim = new Y.Anim( {
			node : '#searchFacets',
			easing : Y.Easing.easeOut,
			duration : 0.3,
			to : {
				opacity : 1
			}
		}),
		onControlClick = function(e){
			var parent = this.get("parentNode");
			if (parent.hasClass("closed")) {
				closeFacetsAnim.run();
				parent.removeClass("closed");
				this.set('innerHTML', 'Show Options');
				parent.addClass("open");
			} else {
				openResultsAnim.run();
				openPopinAnim.run();
				parent.removeClass("open");
				this.set('innerHTML', 'Hide Options');
				parent.addClass("closed");
			}
			e.preventDefault();
		};

	closeFacetsAnim.on('end', function() {
		closePopinAnim.run();
		closeResultsAnim.run();
	});

	openResultsAnim.on('end', function() {
		openFacetsAnim.run();
	});
	
	if(optionsNode){
		optionsNode.insert(options);
		options.one("a").on("click", onControlClick);
	}
});
