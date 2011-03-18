(function() {
	Y.lane.Popup = Y.Base.create("popup", Y.Widget, [Y.WidgetStdMod, Y.WidgetPosition, Y.WidgetPositionConstrain]);
    var panel, container, maybeCreatePanel, showFooPanel, popupWindow, showWindow, createEventHandlers;
    maybeCreatePanel = function() {
    	if (!panel) {
            container = Y.Node.create('<div id="popupContainer"/>');
            Y.one('body').append(container);
            panel = new Y.Overlay({
                srcNode: container
            });
    	}
    };
//    showLocalPanel = function(title, body, cX, cY) {
//        var width = (title.length * 7 > 334) ? title.length * 7 : 334;
//        panel.set('headerContent',title + '<a>Close</a>');
//        panel.set('bodyContent', body);
//        panel.set('x', cX);
//        panel.set('y', cY);
//        panel.set('width',width);
//        panel.render();
//        panel.show();
//        container.plug(Y.Plugin.Drag);
//        container.dd.addHandle(Y.one('#popupContainer .yui3-widget-hd'));
//        Y.on("click",function(){
//            panel.hide();
//        },Y.one('#popupContainer .yui3-widget-hd a'));
//        
//    };
    showFooPanel = function(body) {
        panel.set('bodyContent', body);
        panel.render();
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
    			body = popupElement ? popupElement.get('innerHTML') : '';
    			var bar = new Y.lane.Popup({
    				headerContent : title + "<a>Close</a>",
    			    bodyContent : body,
    			    x : event.pageX,
    			    y : event.pageY,
    			    width : 334,
    			    constrain : true
    			});
    			bar.render();
    			bar.get("contentBox").one("a").on("click", function(event) {
    				bar.destroy();
    			});
    		} else if (args[1] === "foo") {
    			href = anchor.get("href").replace(/(.+)\/\/([^\/]+)\/(.+)/, "$1//$2/plain/$3");
    			Y.io(href,{
    				on : {
    					success : function(id, o, args) {
        					maybeCreatePanel();
        					var body = o.responseXML.documentElement.getElementsByTagName("body")[0];
        					showFooPanel(body.innerHTML);
    				    }
    				}
    			});
    		} else {
    			showWindow(anchor.get("href"), args[1], args[2], args[3]);
    		}
    	}
    });
})();
