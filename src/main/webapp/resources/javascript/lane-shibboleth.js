(function() {

	if(Y.one('#shibboleth-links')){
	
		var SHIBBOLETH_COOKIE_NAME = 'shibboleth-organization',
			SLEEPING_TIME = 10000,
		shibbolethAnchors = Y.all('#shibboleth-links a'),
		id, value, expdate, href; 	
	 	Y.on("click",function(event) {
	        id = event.target.get('id');
	        expdate= new Date();
   			expdate.setDate(expdate.getDate() + 365);
	    	Y.Cookie.set(SHIBBOLETH_COOKIE_NAME, id, { expires: expdate.toUTCString()});    
	    }, shibbolethAnchors);
	
		value = Y.Cookie.get(SHIBBOLETH_COOKIE_NAME);
		if(value && Y.one('#'+value)){
			href = Y.one('#'+value).get('href');
			setTimeout(function(){
				window.location = href;
			}  ,SLEEPING_TIME);
		}		
	}

})();