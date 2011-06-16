/**
 * A tracker implementation that sends the data back to the web
 * application for history tracking.
 */
(function() {
	
	var History, HistoryTracker;
    
    HistoryTracker = function() {
        HistoryTracker.superclass.constructor.apply(this, arguments);
    };
    
    HistoryTracker.ATTRS = {
        io : {
            value : Y.io
        }
    };
    

    Y.extend(HistoryTracker, Y.Base, {
    	
    	initializer : function() {
            var searchTerms, title, url, index;
        	title = Y.one("title").getContent();
        	index = title.indexOf(" - Lane Medical Library");
            if (index > 0) {
                title = title.substring(0, index).replace("&amp;", "&");
            } else {
                title = "Lane Medical Library";
            }
            searchTerms = Y.lane.SearchResult ? Y.lane.SearchResult.getSearchTerms() : false;
            if (searchTerms) {
            	title = "Search for: " + searchTerms;
            }
            url = window.location.pathname;
            if (window.location.search) {
            	url += window.location.search;
            }

//          Y.lane.History.addItem({label : title, url : url});
            this.track({label : title, url : url});
    	},
    	
        track : function(trackingData) {
            this.get("io")("/././history/track", {
                method : "post",
                data : Y.JSON.stringify(trackingData),
                headers: {
                    "Content-Type" : "application/json"
                }
            });
        }
    });
    
    
//    History = function() {
//    	History.superclass.constructor.apply(this, arguments);
//    };
//    
//    History.NAME = "history";
//    
//    History.ATTRS = {
//        model : {
//        	value : []
//        },
//        view : {
//        	value : null
//        }
//    };
//    
//    History.HTML_PARSER = {
//        model : function(srcNode) {
//        	var i, item, model = [], items = srcNode.all("a"), size = items.size();
//        	for (i = 0; i < size; i++) {
//        		item = items.item(i);
//        		model.push({label:item.getContent(),url:item.getAttribute("href")});
//        	}
//        	return model;
//        },
//        view : function(srcNode) {
//        	return srcNode.one("ul");
//        }
//    };
//    
//    History.SAVE_TEMPLATE = "";// <a class=\"save\" href=\"#\">save</a>";
//    
//	Y.extend(History, Y.Widget, {
//		renderUI : function() {
//			var view = this.get("view"),
//			    items = view.all("li"),
//			    size = items.size(), i;
//			for (i = 0; i < size; i++) {
//				items.item(i).append(History.SAVE_TEMPLATE);
//			}
//		},
//		bindUI : function() {
//			this.on("itemAdded", this._handleItemAdded, this);
//		},
//		addItem : function(item) {
//			if (!this.contains(item)) {
//    			this.get("model").splice(0, 0, item);
//    			this.fire("itemAdded", item);
//    			return true;
//			} else {
//				return false;
//			}
//		},
//		contains : function(item) {
//			var contains = false,
//			    model = this.get("model"),
//			    i;
//			for (i = 0; i < model.length; i++) {
//				if (model[i].label == item.label && model[i].url == item.url) {
//					contains = true;
//					break;
//				}
//			}
//			return contains;
//		},
//		_handleItemAdded : function(event) {
//			this.get("view").prepend("<li><a href=\"" + event.url + "\">" + event.label + "</a>" + History.SAVE_TEMPLATE + "</li>");
//	        Y.lane.HistoryTracker.track({
//	            label : event.label,
//	            url : event.url
//	        });
//		}
//	});
    
    if (Y.one("#history")) {
//    	Y.lane.History = new History({srcNode:"#history",render:true});
        Y.lane.HistoryTracker = new HistoryTracker();
        
        //TODO: figure out why this doesn't work when in initializer:
        Y.on("trackable", function(link, event) {
            var trackingData = link.get("trackingData");
            if (trackingData.external) {
            	Y.lane.HistoryTracker.track({
                    label : trackingData.title,
                    url : link.get("url")
                });
            }
        });
    }
    
})();