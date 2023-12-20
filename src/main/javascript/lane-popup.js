(function() {

    "use strict";

    L.Popup = Y.Base.create("popup", Y.Widget, [Y.WidgetStdMod, Y.WidgetPosition, Y.WidgetPositionConstrain]);

    let popup, maybeCreatePopup, popupWindow, showWindow;

    maybeCreatePopup = function(title, body, w, xy) {
        let boundingBox,
            width = w;
        if (width === "0px" || width === "auto") {
            width = 350;
        }
        if (!popup) {
            popup = new L.Popup({
                visible : false,
                constrain : true,
                render : true
            });
            boundingBox = popup.get("boundingBox");
            boundingBox.append("<div class='close fa fa-close'></div>");
            boundingBox.one(".close").on("click", function() {
                popup.hide();
            });
            (new Y.DD.Drag({node: ".yui3-popup"}));
        }
        popup.set("headerContent", title);
        popup.set("bodyContent", body);
        popup.set("width", width);
        popup.set("xy", xy);
    };
    showWindow = function(url, type, width, height) {
        let strWidth = width,
            strHeight = height;
        if (popupWindow !== undefined && !popupWindow.closed) {
            popupWindow.close();
        }
        if (type === 'fullscreen') {
            strWidth = screen.availWidth;
            strHeight = screen.availHeight;
        }
        let tools = '';
        if (type === 'standard') {
            tools = 'resizable,toolbar=yes,location=yes,scrollbars=yes,menubar=yes';
        }
        if (type === 'console' || type === 'fullscreen') {
            tools = 'resizable,toolbar=no,location=no,scrollbars=no';
        }
        if (type === 'console-with-scrollbars') {
            tools = 'resizable,toolbar=no,location=no,scrollbars=yes';
        }
        if (strWidth && strHeight) {
            tools += ',width=' + strWidth + ',height=' + strHeight;
        }
        popupWindow = window.open(url, 'newWin', tools);
        popupWindow.focus();
    };
    document.addEventListener("click", function(event) {
        let args, popupElement, title, body,
            anchor = event.target.closest("a"),
            rel = anchor && anchor.rel;
        if (rel && rel.indexOf("popup") === 0) {
            event.preventDefault();
            args = rel.split(" ");
            if (args[1] === "local") {
                popupElement = document.querySelector('#' + args[2]);
                title = popupElement && popupElement.title ? popupElement.title : '';
                body = popupElement ? popupElement.innerHTML : '';
                maybeCreatePopup(title, body, popupElement.style.width, [event.pageX, event.pageY]);
                popup.show();
            } else {
                showWindow(anchor.href, args[1], args[2], args[3]);
            }
        }
    });
})();
