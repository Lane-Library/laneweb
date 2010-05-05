YUI().use('node', function(Y) {
	var qlNode = Y.one('#qlinks'), qlOptions = qlNode.all("option");
	qlNode.on('change', function() {
		var i = qlNode.get('selectedIndex'), v = qlOptions.item(i).get('value');
		if (i && v) {
			// TODO: add tracking
			window.location = v;
		}
	});
});
