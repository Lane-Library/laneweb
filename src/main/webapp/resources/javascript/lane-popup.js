(function() {
    Y.lane.Popup = Y.Base.create("popup", Y.Widget, [Y.WidgetStdMod, Y.WidgetPosition, Y.WidgetPositionConstrain]);

    var popup, container, maybeCreatePopup, popupWindow, showWindow;
    
    maybeCreatePopup = function(title, body, width, xy) {
    	var boundingBox;
    	width = width || 350;
    	if (!popup) {
    		popup = new Y.lane.Popup({
    			visible : false,
    			constrain : true,
    			render : true
    		});
    		boundingBox = popup.get("boundingBox");
    		boundingBox.append("<a id='popupClose'></a>");
    		boundingBox.one("#popupClose").on("click", function() {
    			popup.hide();
    		});
//    		popup.plug(Y.Plugin.Drag);
//    		popup.dd.addHandle(popup.get("contentBox"));
    	}
    	popup.set("headerContent", title);
    	popup.set("bodyContent", body);
    	popup.set("width", width);
    	popup.set("xy", xy);
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
        var args, href, popupElement, title, body, regex,
            anchor = event.target.ancestor("a") || event.target,
            rel = anchor.get("rel");
        if (rel && rel.indexOf("popup") === 0) {
            event.preventDefault();
            args = rel.split(" ");
            if (args[1] === "local") {
                popupElement = Y.one('#' + args[2]);
                title = popupElement && popupElement.get('title') ? popupElement.get('title') : '';
                body = popupElement ? popupElement.get('innerHTML') : '';
                maybeCreatePopup(title, body, popupElement.getStyle("width"), [event.pageX, event.pageY]);
                popup.show();
            } else {
                showWindow(anchor.get("href"), args[1], args[2], args[3]);
            }
        }
    }, document);
})();
