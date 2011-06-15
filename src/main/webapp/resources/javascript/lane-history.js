/**
 * A tracker implementation that sends the data back to the web
 * application for history tracking.
 */
(function() {
	
//	var History, HistoryTracker;
//    
//    HistoryTracker = function() {
//        HistoryTracker.superclass.constructor.apply(this, arguments);
//    };
//    
//    HistoryTracker.ATTRS = {
//        io : {
//            value : Y.io
//        }
//    };
//    
//
//    Y.extend(HistoryTracker, Y.Base, {
//        track : function(trackingData) {
//            this.get("io")("/././history/track", {
//                method : "post",
//                data : Y.JSON.stringify(trackingData),
//                headers: {
//                    "Content-Type" : "application/json"
//                }
//            });
//        }
//    });
//    
//    Y.lane.HistoryTracker = new HistoryTracker();
//    
//    Y.on("domready", function() {
//        //only track history if <meta name="emrid"/>
//        var emridMeta = Y.one("meta[name='emrid']"),
//            searchTerms, emrid, title, url, index;
//        if (emridMeta) {
//            //shorten title:
//        	title = Y.one("title").getContent();
//        	index = title.indexOf(" - Lane Medical Library");
//            if (index > 0) {
//                title = title.substring(0, index).replace("&amp;", "&");
//            } else {
//                title = "Lane Medical Library";
//            }
//            searchTerms = Y.lane.SearchResult.getSearchTerms();
//            if (searchTerms) {
//            	title = "Search for: " + searchTerms;
//            }
//            url = window.location.pathname;
//            if (window.location.search) {
//            	url += window.location.search;
//            }
//            Y.lane.HistoryTracker.track({
//                label : title,
//                url : url
//            });
//            
//            Y.on("trackable", function(link, event) {
//                var trackingData = link.get("trackingData");
//                if (trackingData.external) {
//                    Y.lane.HistoryTracker.track({
//                        label : trackingData.title,
//                        url : link.get("url")
//                    });
//                }
//            });
//        }
//    });
    
    var History = function() {
    	History.superclass.constructor.apply(this, arguments);
    };
    
    History.NAME = "history";
    
    History.ATTRS = {
        links : {
        	value : []
        },
        io : {
        	value : Y.io
        }
    };
    
    History.HTML_PARSER = {
        links : function(srcNode) {
        	var i, item, links = [], items = srcNode.all("a"), size = items.size();
        	for (i = 0; i < size; i++) {
        		item = items.item(i);
        		links.push({label:item.getContent(),url:item.getAttribute("href")});
        	}
        	return links;
        }
    };
    
    History.SAVE_TEMPLATE = "";// <a class=\"save\" href=\"#\">save</a>";
    
    if (Y.one("#history")) {
    	Y.extend(History, Y.Widget, {
    		renderUI : function() {
    			var contentBox = this.get("contentBox"),
    			    items = contentBox.all("li"),
    			    size = items.size(), i;
    			for (i = 0; i < size; i++) {
    				items.item(i).append(History.SAVE_TEMPLATE);
    			}
    		},
    		bindUI : function() {
    			this.on("itemAdded", this._handleItemAdded, this);
    		},
    		addItem : function(item) {
    			this.get("links").splice(0, 0, item);
    			this.fire("itemAdded", item);
    		},
    		_handleItemAdded : function(event) {
    			this.get("contentBox").prepend("<li><a href=\"" + event.url + "\">" + event.label + "</a>" + History.SAVE_TEMPLATE + "</li>");
    		}
    	});
    	Y.lane.History = new History({srcNode:"#history",render:true});
    }
    
})();