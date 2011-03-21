(function() {
    Y.lane.Popup = Y.Base.create("popup", Y.Widget, [Y.WidgetStdMod, Y.WidgetPosition]);
    Y.lane.Foo = Y.Base.create("foo", Y.Widget, [Y.WidgetPosition, Y.WidgetPositionAlign]);
    var popup, container, maybeCreatePopup, maybeCreateFoo, popupWindow, showWindow, createEventHandlers, foo;
    maybeCreatePopup = function(title, body, xy) {
        if (!popup) {
            popup = new Y.lane.Popup({
                headerContent : title,
                bodyContent : body,
                xy : xy,
                width : 334
            });
            popup.plug(Y.Plugin.Drag);
            popup.dd.addHandle(popup.get("contentBox"));
            popup.get("contentBox").on("click", function(event) {
                popup.hide();
            });
        } else {
            popup.set("headerContent", title);
            popup.set("bodyContent", body);
            popup.set("xy", xy);
        }
    };
    maybeCreateFoo = function(body) {
    	if (!foo) {
    		foo = new Y.lane.Foo({
    			centered : true
    		});
    		foo.get("contentBox").on("click", function(event) {
    			foo.hide();
    		});
    	}
        foo.get("contentBox").set("innerHTML", body);
    };
    showWindow = function(url, type, strWidth, strHeight) {
        if (popupWindow !== undefined && !popupWindow.closed) {
            popupWindow.close();
        }
        if (type == 'fullscreen') {
            strWidth = screen.availWidth;
            strHeight = screen.availHeight;
        }
        var tools = '';
        if (type == 'standard') {
            tools = 'resizable,toolbar=yes,location=yes,scrollbars=yes,menubar=yes';
        }
        if (type == 'console' || type == 'fullscreen') {
            tools = 'resizable,toolbar=no,location=no,scrollbars=no';
        }
        if (type == 'console-with-scrollbars') {
            tools = 'resizable,toolbar=no,location=no,scrollbars=yes';
        }
        if (strWidth && strHeight) {
            tools += ',width=' + strWidth + ',height=' + strHeight;
        }
        popupWindow = window.open(url, 'newWin', tools);
        popupWindow.focus();
    };
    Y.on("click", function(event) {
        var args, href, popupElement, title, body,
            anchor = event.target.ancestor("a") || event.target,
            rel = anchor.get("rel");
        if (rel && rel.indexOf("popup") === 0) {
            event.preventDefault();
            args = rel.split(" ");
            if (args[1] === "local") {
                popupElement = Y.one('#' + args[2]);
                title = popupElement && popupElement.get('title') ? popupElement.get('title') : '';
                title += "<a>Close</a>";
                body = popupElement ? popupElement.get('innerHTML') : '';
                maybeCreatePopup(title, body, [event.pageX, event.pageY]);
                popup.render();
                popup.show();
            } else if (args[1] === "foo") {
                href = anchor.get("href").replace(/(.+)\/\/([^\/]+)\/(.+)/, "$1//$2/plain/$3");
                Y.io(href,{
                    on : {
                        success : function(id, o, args) {
                            var body = o.responseXML.documentElement.getElementsByTagName("body")[0];
                            maybeCreateFoo(body.innerHTML);
                            foo.render();
                            foo.centered();
                            foo.show();
                        }
                    }
                });
            } else {
                showWindow(anchor.get("href"), args[1], args[2], args[3]);
            }
        }
    });
})();
