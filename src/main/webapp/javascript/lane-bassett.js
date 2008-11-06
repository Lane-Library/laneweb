(function() {

var diagramDisplay = false,
YC = YAHOO.util.Connect,  
YE = YAHOO.util.Event,
YH = YAHOO.util.History;

YAHOO.util.Event.onAvailable('bassettContent',function() {
	YAHOO.util.Get.script( "/javascript/noversion/bubbling-min.js", {
        onSuccess: function(){
			YAHOO.util.Get.script( "/javascript/noversion/accordion-min.js", {
       			 onSuccess: function(){
       			 	init();	        	
        		}
			});		        	
        }
	});
})
	
	function init(){
		var accordion = document.getElementById('accordion');
 		registerLinksContainer(accordion );
 		registerLinksContainer( document.getElementById('bassettContent'));	
 		if(accordion)
 			initializeHistory();
 						
 	}
	
	 	
 	function registerLinksContainer(container){
 		var anchor, i, url;    
        if (container) {
         	anchor = container.getElementsByTagName('a');
         	for (i = 0; i < anchor.length; i++) {
         		if( anchor[i].rel == null || anchor[i].rel == "" )
         		{
	                 anchor[i].clicked = function(event) {
	        			if( this.id == "diagram-choice")
							diagramDisplay = true;
						if( this.id == "photo-choice")
							diagramDisplay = false;
	        
	            		url = formatAjaxUrl(this.href);
	            		  if (YH) {
                                try {
                                    YH.navigate("bassett", url);
                                } catch (e) {
                                	loadContent(url);
                                }
                            }
                            else
                            	loadContent(url);
	            		YE.stopEvent(event);
	                };
	     		}   
            }
		}
 	}
 	        
	    
	function loadContent(url) {
		url = "/././plain/bassett/raw".concat(url);
		function successHandler(o) {
	        var content, container,i;
	        container = document.getElementById('bassettContent')  
	        content = o.responseXML.getElementsByTagName('body')[0].childNodes;
	    	while (container.childNodes.length > 0) {
	        	container.removeChild(container.firstChild);
	    	}
	    	for (i = 0; i < content.length; i++) {
		    	container.appendChild(LANE.core.importNode(content[i], true));
		    }
		    registerLinksContainer(container);
		    LANE.popups.initialize(container);
	    }
        YC.asyncRequest("GET", url,
        {
			success:successHandler
        });
	}
	    
	     
	 formatAjaxUrl = function(href)
	 {
	 	var url;
	 	href = href.replace("search.html", "/bassett/bassettsView.html");
		href = href.substr(href.indexOf("/bassett/")+8);
		href = href.split("?");
		if(href.length == 1)
			url =  href[0];
		if(href.length > 1)
			url =	 href[0]+"?" + href[1] ;
		if(diagramDisplay)
		 	url = url +"&t=diagram";
	    return url;
	 } 


	initializeHistory = function(){
		var  initial = YH.getBookmarkedState("bassett") || window.location.toString();
		YH.register("bassett",  formatAjaxUrl(initial), loadContent);
		YH.initialize("yui-history-field-bassett", "yui-history-iframe");
    };


})();
