YUI().use("lane", 'node','event-custom', function(Y) {

	var time = new Date().getTime();
	if (Y.one("#qlinks")) {
    Y.publish("lane:quickLinkClick",{
        broadcast:2,
        emitFacade: true,
        linkName:null
    });
    var qlNode = Y.one('#qlinks'), qlOptions = qlNode.all("option");
    qlNode.on('change', function() {
        var i = qlNode.get('selectedIndex'), v = qlOptions.item(i).get('value');
        if (i && v) {
            function f(){
                window.location.href = v;
            }
            Y.fire("lane:quickLinkClick",{
                linkName:qlOptions.item(i).get('textContent')
            });
            setTimeout(f,200); // delay for tracking
        }
    });
	}

    LANE.log("lane-quicklinks.js:use() " + (new Date().getTime() - time));
});
