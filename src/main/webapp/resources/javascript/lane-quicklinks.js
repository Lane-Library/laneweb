YUI().use('node', function(Y) {
	var qlNode = Y.one('#qlinks'), qlOptions = qlNode.all("option");
	qlNode.on('change', function() {
		var i = qlNode.get('selectedIndex');
		if (i) {
			// TODO: add tracking
			window.location = qlOptions.item(i).get('value');
		}
	});
});
