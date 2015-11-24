(function() {


        var bassettContent = Y.one('#bassettContent'),
            model = Y.lane.Model,
            basePath = model.get(model.BASE_PATH) || "",
            diagramDisplay = false,
            accordion,
            history,
            registerLinksContainer,
            subRegionToShow = 4,

            formatAjaxUrl = function(href) {
                var url;
                href = href.replace("search.html", "/biomed-resources/bassett/bassettsView.html");
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
                    var content = Y.Node.create(o.responseText),
                        container = Y.one('#bassettContent');
                    container.setContent(content);
                    registerLinksContainer(container);
                }
                Y.io(url, {on : {success : successHandler}});
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
                    history.addValue("bassett",url);
                } catch (e) {
                    loadContent(url);
                }
                ev.preventDefault();
            },

        initializeHistory = function() {
            history = new Y.HistoryHash();
            if(history.get('bassett')){
                loadContent(history.get('bassett'));
            }
            history.on("bassettChange",function(e) {
                loadContent(e.newVal);
            });
            history.on("bassettRemove",function() {
                loadContent(formatAjaxUrl(Y.lane.Location.get("href")));
            });
        };

        registerLinksContainer = function(container) {
            var anchor, i;
            if (container) {
                anchor = container.all('a');
                for (i = 0; i < anchor.size(); i++) {
                    if (anchor.item(i).get('rel') === null || anchor.item(i).get('rel') === "" ||  anchor.item(i).get('rel') === "propagation") {
                        anchor.item(i).on('click', handleClick);
                    }
                }
            }
        };

        
       //For the bassett menu
        

	    expandSubRegion = function(event) {
			var region = event.currentTarget.ancestor('ul'),
			seeAllContent,
			seeAll = region.one('.see-all');
			if(seeAll){
				seeAllContent = seeAll.getHTML();
			}
			hideRegions();
			resetSubregion();
			if ( "see all" == seeAllContent) {
				seeAll.setHTML('hide');
				var subRegions = region.all('li');
				for (i = subRegionToShow + 1; i < subRegions.size(); i++) {
					subRegions.item(i).show();
				}
			}
	    }
        
        surlineSubRegion = function(event){
        	resetSubregion();
        	var anchor = event.currentTarget;
        	selectedSubRegion = anchor.ancestor();
        	selectedSubRegion.addClass('enabled');
        	var i = selectedSubRegion.one('i');
        	i.removeClass('fa-square-o');
        	i.addClass('fa-check-circle-o');
        }

        resetSubregion = function(){
        	var liList = Y.all('.region li:not(:first-child)');
        	for(i= 0; i< liList.size(); i++){
        		var li = liList.item(i);
        		li.removeClass('enabled');
        		var iElement = li.one('i');
        		if(iElement != null){
	        		iElement.addClass('fa-square-o');
	            	iElement.removeClass('fa-check-circle-o');
        		}
            }
        }
        
        hideRegions = function() {
			var region = Y.all('.region');
			for (i = 0; i < region.size(); i++) {
				hideSubRegion( region.item(i));
			}
			
        }
        

	    hideSubRegion = function(region) {
			var seeAll = region.one('.see-all');
			if (seeAll) {
				seeAll.setHTML('see all');
				var subRegion = region.all('li');
				for (j = 0; j < subRegion.size(); j++) {
					if (j > subRegionToShow) {
						subRegion.item(j).hide();
					}
				}
			}
		}
        
        if (bassettContent) {
            accordion = Y.one('#bassett-menu');
            // not if largerView.html
            if (accordion) {
            	hideRegions();
                registerLinksContainer(accordion);
                registerLinksContainer(Y.one('#bassettContent'));
                var seeAll = Y.all('.see-all');
                for (i = 0; i < seeAll.size(); i++) {
                    seeAll.item(i).on('click', expandSubRegion);
                }
                var li = Y.all('.region li:not(:first-child) a');
                for (i = 0; i < li.size(); i++) {
                    li.item(i).on('click', surlineSubRegion);
                }
                initializeHistory();
            }
        }
        
        
})();
