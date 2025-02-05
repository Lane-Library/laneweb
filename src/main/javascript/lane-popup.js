(function () {

    "use strict";

    class Popup {
        constructor() {
        };

        maybeCreatePopup(title, body, width, xy) {
            if (width === "0px" || width === "auto") {
                width = 350;
            }
            let titleDiv, contentDiv, bodyDiv, closedDiv;
            this.clearPopup();
            this.popupWindow = document.createElement('div');
            this.popupWindow.classList.add("popup");
            contentDiv = document.createElement('div');
            contentDiv.classList.add("popup-content");
            titleDiv = document.createElement('div');
            titleDiv.classList.add("widget-hd");
            titleDiv.innerHTML = title;
            this.popupWindow.appendChild(titleDiv);
            bodyDiv = document.createElement('div');
            bodyDiv.classList.add("widget-bd");
            bodyDiv.innerHTML = body;
            contentDiv.appendChild(titleDiv);
            contentDiv.appendChild(bodyDiv);
            this.popupWindow.appendChild(contentDiv);
            this.popupWindow.style.width = width + "px";
            this.popupWindow.style.position = "absolute";
            this.popupWindow.style.maxWidth = "90%";

            closedDiv = document.createElement('div');
            closedDiv.classList.add("fa-close")
            closedDiv.classList.add("fa");
            closedDiv.classList.add("close");
            this.popupWindow.appendChild(closedDiv);

            document.body.appendChild(this.popupWindow);
            this.popupWindow.draggable = true;
            let xlocation = this.calWidth(xy[0])
            this.popupWindow.style.left = xlocation + "px ";
            this.popupWindow.style.top = xy[1] + "px ";

            closedDiv.addEventListener("click", () => {
                this.clearPopup();
            });
            this.setDraggable();
            this.setDropZone();
        };

        clearPopup() {
            document.body.querySelectorAll(".popup").forEach(popup => {
                document.body.removeChild(popup);
            })
        }

        setDraggable() {
            this.popupWindow.addEventListener("dragstart", (event) => {
                let target = event.target;
                this.offsetX = event.clientX - target.offsetLeft;
                this.offsetY = event.clientY - target.offsetTop;
            });
            this.popupWindow.addEventListener("drag", (event) => {
                event.preventDefault();
                let target = event.target;
                target.style.left = event.clientX - this.offsetX + 'px';
                target.style.top = event.clientY - this.offsetY + 'px';
            })
        }

        setDropZone() {
            let content = document.body;
            content.addEventListener("dragover", (event) => { event.preventDefault(); });
            content.addEventListener("drop", (event) => { event.preventDefault(); });
            content.addEventListener("dropend", (event) => { event.preventDefault(); });
        }

        calWidth(x) {
            let windowWidth = this.popupWindow.offsetWidth,
                screenWidth = document.body.clientWidth;
            if (x + windowWidth > screenWidth) {
                x = screenWidth - 20 - windowWidth;
            }
            return x;
        }

        showWindow(url, type, width, height) {
            let strWidth = width, popupWindow,
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
    }

    document.addEventListener("click", function (event) {
        let args, popupElement, title, body,
            anchor = event.target.closest("a"),
            rel = anchor && anchor.rel;
        if (rel && rel.indexOf("popup") === 0) {
            event.preventDefault();
            let popup = new Popup();
            args = rel.split(" ");
            if (args[1] === "local") {
                popupElement = document.querySelector('#' + args[2]);
                title = popupElement && popupElement.title ? popupElement.title : '';
                body = popupElement ? popupElement.innerHTML : '';
                popup.maybeCreatePopup(title, body, popupElement.style.width, [event.pageX, event.pageY]);
            } else {
                popup.showWindow(anchor.href, args[1], args[2], args[3]);
            }
        }
    });
})();
