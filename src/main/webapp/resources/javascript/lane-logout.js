(function() {

	var Y = LANE.Y,

	logout = Y.one('#logout');
	if (logout != null) {
		logout.on("click", function() {
			Y.Cookie.remove("user");
		});
	}

})();