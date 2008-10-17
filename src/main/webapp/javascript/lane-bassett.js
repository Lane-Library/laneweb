(function() {

var diagramDisplay = false,
YC = YAHOO.util.Connect,  
YE = YAHOO.util.Event;

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
 	}
	
	 	
 	function registerLinksContainer(container){
 		var anchor;    
        if (container) {
         	contentContainer = document.getElementById("bassettContent");
         	anchor = container.getElementsByTagName('a');
         	for (i = 0; i < anchor.length; i++) {
         		if( anchor[i].rel == null || anchor[i].rel == "" )
         		{
	                 anchor[i].result = new BassettResult( anchor[i], contentContainer);
	                 anchor[i].clicked = function(event) {
	             		this.result.getContent();
	                    YE.stopEvent(event);
	                };
	     		}   
            }
		}
 	}
        
    

    
    function BassettResult(anchor, container) {
    	this._anchor = anchor;
    	this._container = container;
        this._content;
        this._url = '/././plain/' + anchor.pathname.replace(/bassett/,'bassett/raw') + anchor.search ;
        this._callback = {
            success: function(o) {
                var result, content;
                result = o.argument.result;
                content = o.responseXML.getElementsByTagName('body')[0].childNodes;
                result.setContent(content);
                result.hide();
                result.show();
            },
            failure: function() { alert('failure'); },
            argument: {result:this}
    };
    }
    

BassettResult.prototype.setContent = function(content) {
     this._content = content;         
};
    
    

BassettResult.prototype.show = function() {
	    for (i = 0; i < this._content.length; i++) {
	    	this._container.appendChild(LANE.core.importNode(this._content[i], true));
	    }
	    registerLinksContainer(this._container);
};

BassettResult.prototype.getContent = function() {
	if( this._anchor && this._anchor.id == "diagram-choice")
		diagramDisplay = true;
	else if( this._anchor && this._anchor.id == "photo-choice")
		diagramDisplay = false;
	if(diagramDisplay)
		YC.asyncRequest('GET', this._url+"&t=diagram", this._callback);
	else	
		YC.asyncRequest('GET', this._url, this._callback);
};
 
BassettResult.prototype.hide = function() {
    while (this._container.childNodes.length > 0) {
        this._container.removeChild(this._container.lastChild);
    }
};


})();
