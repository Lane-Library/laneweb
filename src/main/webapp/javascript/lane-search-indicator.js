// spinning wheel icon for search in progress
LANE.search.indicator =  function() {
    var indicator = document.getElementById('searchIndicator');
		return {
	    	show: function(){
	    		if( null === indicator ){
	    			return false;
	    		}
				indicator.style.visibility = 'visible';
	    	},
	    	hide: function(){
	    		if( null === indicator ){
	    			return false;
	    		}
				indicator.style.visibility = 'hidden';
	    	},
	    	setMessage: function(message){
	    		if( null === indicator ){
	    			return false;
	    		}
				indicator.alt = message;
	    	}
		};
}();