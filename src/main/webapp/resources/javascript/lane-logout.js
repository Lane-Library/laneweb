(function() {

	var Y = LANE.Y,
        logout = Y.one('#logout'),
	    pl_button = Y.one('#persistence-login'),
	    pl = Y.one('#pl');
	    
	if (logout !== null) {
		logout.on("click", function() {
			Y.Cookie.remove("user");
		});
	}
	
	if (null !== pl_button)
		pl_button.on("click", function() {
			if (false === pl.get('checked')) 
				Y.Cookie.remove("user");
		});

})();