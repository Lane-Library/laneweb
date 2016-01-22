(function() {

	var bassettContent = Y.one('#bassettContent'), 
	 Lane = Y.lane,
	model = Lane.Model,
	
	basePath = model.get(model.BASE_PATH)|| "", 
	diagramDisplay = false,
	accordion, history,  
	subRegionToShow = 4, prevRegion, prevSubRegion, 
	
	formatAjaxUrl = function(href) {
		var url;
		href = href.replace("search.html",
				"/biomed-resources/bassett/bassettsView.html");
		href = href.substr(href.indexOf("/bassett/") + 8);
		href = href.split("?");
		if (href.length === 1) {
			url = href[0];
		}
		if (href.length > 1) {
			url = href[0] + "?" + href[1];
		}
		if (diagramDisplay) {
			url = url + "&t=diagram";
		}
		return url;
	},

	loadContent = function(url) {
		url = basePath + "/plain/biomed-resources/bassett/raw".concat(url);
		function successHandler(id, o) {
			var content = Y.Node.create(o.responseText), container = Y
					.one('#bassettContent');
			container.setContent(content);
			registerLinksContainer(container);
			Y.all('.s-pagination form[name=bassett-pagination]').on('submit', submitPagination);
		}
		Y.io(url, {
			on : {
				success : successHandler
			}
		});
	},

	handleClick = function(ev) {
		var url;
		if (this.get('id') === "diagram-choice") {
			diagramDisplay = true;
		}
		if (this.get('id') === "photo-choice") {
			diagramDisplay = false;
		}
		url = formatAjaxUrl(this.get('href'));
		try {
			history.addValue("bassett", url);
		} catch (e) {
			loadContent(url);
		}
		ev.preventDefault();
	},

	initializeHistory = function() {
		history = new Y.HistoryHash();
		if (history.get('bassett')) {
			loadContent(history.get('bassett'));
		}
		history.on("bassettChange", function(e) {
			loadContent(e.newVal);
		});
		history.on("bassettRemove", function() {
			loadContent(formatAjaxUrl(Y.lane.Location.get("href")));
		});
	},

	registerLinksContainer = function(container) {
		if (container) {
			var anchor = container.all('a');
			for (var i = 0; i < anchor.size(); i++) {
				if (anchor.item(i).get('rel') === null
						|| anchor.item(i).get('rel') === ""
						|| anchor.item(i).get('rel') === "propagation") {
					anchor.item(i).on('click', handleClick);
				}
			}
		}
	},

	// For the bassett menu

	expandSubRegion = function(event) {
		var subRegion, region = event.currentTarget.ancestor('ul'),
		display = region.all('li').item(subRegionToShow+1).getStyle('display');
		hideSubRegions(region);
		resetSubRegion();
		if (display == 'none') {
			region.one('.see-all').setHTML('hide');
			subRegion = region.all('li');
			for (i = subRegionToShow + 1; i < subRegion.size(); i++) {
				subRegion.item(i).setStyle('display', 'block');
			}
		}
	},

	hideSubRegions = function(region) {
		if (prevRegion && prevRegion.one('.see-all')) {
			prevRegion.one('.see-all').setHTML('see all');
			var subRegion = prevRegion.all('li');
			for (j = subRegionToShow; j < subRegion.size(); j++) {
				subRegion.item(j).hide();
			}
		}
		prevRegion = region;
	},

	
	surlineSubRegion = function(event) {
		var i,  li = event.currentTarget;
		resetSubRegion(li);
		li.addClass('enabled');
		i = li.one('i');
		i.removeClass('fa-circle-o');
		i.addClass('fa-check-circle');
	},

	resetSubRegion = function(subRegion) {
		if (prevSubRegion && prevSubRegion.one('i')) {
			prevSubRegion.removeClass('enabled');
			var iElement = prevSubRegion.one('i');
			iElement.addClass('fa-circle-o');
			iElement.removeClass('fa-check-circle');
		}
		prevSubRegion = subRegion;
	},

 
	
     submitPagination = function(e) {
         var page = e.target.get('page').get('value'),
         pages = e.target.get('pages');
         if (page.match('[^0-9]') || page < 1 || Number(page) > Number(pages.get('value'))){
             e.preventDefault();
             Y.all('.bassett-error').setStyle('display', 'block');
             return;
         }
         pages.remove();
    };

	if (bassettContent) {
		accordion = Y.one('#bassett-menu');
		// not if largerView.html
		if (accordion) {
			registerLinksContainer(accordion);
			registerLinksContainer(Y.one('#bassettContent'));
			var lis, seeAll = Y.all('.see-all');
			for (i = 0; i < seeAll.size(); i++) {
				seeAll.item(i).on('click', expandSubRegion);
			}
			 lis = Y.all('.region li:not(:first-child)');
			for (i = 0; i < lis.size(); i++) {
				lis.item(i).on('click', surlineSubRegion);
			}
			Y.all('.s-pagination form[name=bassett-pagination]').on('submit', submitPagination);
			initializeHistory();
		}
	}

})();
