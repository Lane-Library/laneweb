
// click on login link
$(document).on("click", ".webauthLogin:contains('Logout')", function(e) {
	e.preventDefault();
	if (confirm("Do you really want to logout?")) {
		document.location.href = e.target.href;
	}
});

$.LANE.toggleLogin = function() {
	if (model['auth']) {
		$('.webauthLogin').each(function() {
			$(this).text('Logout');
			$(this).attr('href', model['base-path'] + '/logout');
			$(this).attr('data-ajax', 'false');
		});
	} else {
		$('.webauthLogin').each(
				function() {
					$(this).text('Login');
					$(this).attr('href',
							model['base-path'] + '/secure/mobile-login.html');
					$(this).attr('data-ajax', 'false');
				});
	}
};

//
// //toggle login button at every pageinit
$(this).bind("pageinit", function() {
	$.LANE.toggleLogin();
});
