(function() {

	if(Y.one('#shibboleth-links')){
	
		var SHIBBOLETH_COOKIE_NAME = 'shibboleth-organization',
			SLEEPING_TIME = 10000,
			
		shibbolethAnchors = Y.all('#shibboleth-links a'),
		id, value, expdate, href, message; 	
	 	Y.on("click",function(event) {
	        id = event.target.get('id');
	        expdate= new Date();
   			expdate.setDate(expdate.getDate() + 365);
	    	Y.Cookie.set(SHIBBOLETH_COOKIE_NAME, id, { expires: expdate.toUTCString()});    
	    }, shibbolethAnchors);
	
		value = Y.Cookie.get(SHIBBOLETH_COOKIE_NAME);
		if(value && Y.one('#'+value)){
			message_contnair = Y.one('#message-contnair');
			message_contnair.set('innerHTML', "<h2>You will be a redirected to " + value +" login in 10 seconds </h2>");
			href = Y.one('#'+value).get('href');
			setTimeout(function(){
				window.location = href;
			}  ,SLEEPING_TIME);
		}		
	}

})();