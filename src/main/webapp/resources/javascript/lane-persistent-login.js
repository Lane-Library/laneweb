(function() {

	var model = Y.lane.Model,
	basePath = model.get(model.BASE_PATH)|| "",  
	location = Y.lane.Location,
	// isStanfordActive == true only if user is from stanford and is active in the LDAP
	// See UserDataBinder.java
	isStanfordActive = model.get(model.IS_ACTIVE_SUNETID);


	if(Y.one('#persistent')){
		var div = Y.one('#persistent div.header'),
		messageDiv = Y.one('#persistent .message'),
		isPersistent = Y.Node.create("<div class='is-persistent'><input type='checkbox' id='is-persistent-login' /> <label>Log me in for 2 weeks (DON’T check if on a shared computer)</label></div>");
		div.insert(isPersistent, 'after');
		
		Y.on('change', 	function(event) {
			var checkbox = event.target;
		    if(checkbox.get('checked')) {
		    	Y.Cookie.set("isPersistent", "yes");
		    } else {
		    	Y.Cookie.remove("isPersistent");
		    }           
		}
		, '#is-persistent-login');
		
		
//		messageDiv.setStyle('display', 'none');
//		
//		Y.on('click', 	function(event) {
//			
//			messageDiv.setStyle('display', 'block');     
//		}
//		, '#show-message');
//		

	}
	

	// for the static page myaccounts.html Click on YES this way the user
    // will not have to go through webauth.
    if(Y.one('#persistent-login')){
        Y.on('click',function(event) {
            event.preventDefault();
            if (isStanfordActive) {
                location.set("href", basePath + '/persistentLogin.html?pl=renew&url=/myaccounts.html');
            } else {
                location.set("href", basePath + '/secure/persistentLogin.html?pl=true');
            }
        }, '#persistent-login');
    }
    
})();
