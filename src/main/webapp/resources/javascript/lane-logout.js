(function() {

	var Y = LANE.Y,

	logout = Y.one('#logout');
	if (logout != null) {
		logout.on("click", function() {
			Y.Cookie.remove("user");
		});
	}
	pl_button = Y.one('#persistence-login');
	pl = Y.one('#pl');
	if (null != pl_button)
		pl_button.on("click", function() {
			if (false == pl.get('checked')) 
				Y.Cookie.remove("user");
		});

})();