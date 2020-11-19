(function(window, document) {
	var menu = document.getElementById('menu'),
		rollback,
		WINDOW_CHANGE_EVENT = ('onorientationchange' in window) ? 'orientationchange' : 'resize';

	function toggleHorizontal() {
		[].forEach.call(
			document.getElementById('menu').querySelectorAll('.custom-can-transform'),
			function(el) {
				el.classList.toggle('pure-menu-horizontal');
			}
		);

	};

	function toggleMenu() {
		// set timeout so that the panel has a chance to roll up
		// before the menu switches states

		toggleHorizontal();


		menu.classList.toggle('open');
		document.getElementById('navbar-toggle').classList.toggle('open');
	};

	function closeMenu() {
		if (menu.classList.contains('open')) {
			toggleMenu();
		}
	}

	document.getElementById('navbar-toggle').addEventListener('click', function(e) {
		toggleMenu();
		e.preventDefault();
	});

	window.addEventListener(WINDOW_CHANGE_EVENT, closeMenu);
})(this, this.document);