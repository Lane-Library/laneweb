(() => {

    "use strict";

    /**
     * Manages a single, draggable, in-page popup element.
     * Also provides a static utility to open new browser windows.
     */
    class Popup {
        #popupElement = null;
        #offsetX = 0;
        #offsetY = 0;

        #handleDragStart = (event) => {
            // Calculate the offset from the cursor to the top-left of the element.
            this.#offsetX = event.clientX - this.#popupElement.offsetLeft;
            this.#offsetY = event.clientY - this.#popupElement.offsetTop;

            document.addEventListener('dragover', this.#handleDragOver);
            document.addEventListener('drop', this.#handleDrop);
        };

        #handleDragOver = (event) => {
            // This is necessary to allow a 'drop' event to fire.
            event.preventDefault();
        };

        #handleDrop = (event) => {
            event.preventDefault();
            // Move the popup to the final drop position.
            this.#popupElement.style.left = `${event.clientX - this.#offsetX}px`;
            this.#popupElement.style.top = `${event.clientY - this.#offsetY}px`;
            // clean up drag-and-drop listeners
            document.removeEventListener('dragover', this.#handleDragOver);
            document.removeEventListener('drop', this.#handleDrop);
        };

        /**
         * Hides and removes the current in-page popup from the DOM.
         */
        hide() {
            this.#popupElement?.remove();
            this.#popupElement = null;
            // clean up drag-and-drop listeners
            document.removeEventListener('dragover', this.#handleDragOver);
            document.removeEventListener('drop', this.#handleDrop);
        }

        /**
         * Creates and displays a new in-page popup, replacing any existing one.
         * @param {string} title - The title text for the popup header.
         * @param {string} body - The HTML content for the popup body.
         * @param {string} width - The desired width (e.g., "400px", "auto").
         * @param {{x: number, y: number}} position - The initial coordinates.
         */
        show(title, body, width, position) {
            // show only one popup at a time
            this.hide();

            const popupWidth = (width === "0px" || width === "auto") ? 350 : parseInt(width, 10);

            const template = `
                <div class="popup-content">
                    <div class="widget-hd">${title}</div>
                    <div class="widget-bd">${body}</div>
                </div>
                <div class="fa fa-close close" title="Close"></div>
            `;

            this.#popupElement = document.createElement('div');
            this.#popupElement.className = 'popup';
            this.#popupElement.innerHTML = template;
            this.#popupElement.draggable = true;
            this.#popupElement.style.cssText = `
                width: ${popupWidth}px;
                max-width: 90%;
                position: absolute;
                left: ${position.x}px;
                top: ${position.y}px;
            `;

            document.body.appendChild(this.#popupElement);
            this.#ensureInBounds();

            this.#popupElement.querySelector('.close').addEventListener('click', () => this.hide());
            this.#popupElement.addEventListener('dragstart', this.#handleDragStart);
        }

        /**
         * Adjusts the popup's horizontal position to keep it within the viewport.
         */
        #ensureInBounds() {
            const popupRect = this.#popupElement.getBoundingClientRect();
            const screenWidth = document.documentElement.clientWidth;
            if (popupRect.right > screenWidth) {
                const newLeft = screenWidth - popupRect.width - 20; // 20px buffer
                this.#popupElement.style.left = `${Math.max(0, newLeft)}px`;
            }
        }

        /**
         * A static utility method to open a new browser window.
         * It does not rely on any instance state.
         * @param {string} url - The URL to open.
         * @param {string} type - The type of window ('fullscreen', 'standard', etc.).
         * @param {string} width - The desired width.
         * @param {string} height - The desired height.
         */
        static showBrowserPopup(url, type, width, height) {
            const toolbars = {
                'standard': 'resizable,toolbar=yes,location=yes,scrollbars=yes,menubar=yes',
                'console': 'resizable,toolbar=no,location=no,scrollbars=no',
                'console-with-scrollbars': 'resizable,toolbar=no,location=no,scrollbars=yes',
                'fullscreen': 'resizable,toolbar=no,location=no,scrollbars=no'
            };

            let tools = toolbars[type] || '';
            let finalWidth = width;
            let finalHeight = height;

            if (type === 'fullscreen') {
                finalWidth = window.screen.availWidth;
                finalHeight = window.screen.availHeight;
            }

            if (finalWidth && finalHeight) {
                tools += `,width=${finalWidth},height=${finalHeight}`;
            }

            const newWindow = window.open(url, 'newWin', tools);
            newWindow?.focus();
        }
    }

    // Singleton Popup Manager
    const popupManager = new Popup();

    // --- Global Event Handling ---

    document.addEventListener("click", (event) => {
        const anchor = event.target.closest("a");

        if (!anchor?.rel?.startsWith("popup")) {
            return;
        }

        event.preventDefault();

        // parse the 'rel' attribute
        const [, type, ...args] = anchor.rel.split(" ");

        if (type === "local") {
            const [popupId] = args;
            const popupElement = document.querySelector(`#${popupId}`);
            if (!popupElement) return;

            const title = popupElement.title || '';
            const body = popupElement.innerHTML;
            const width = popupElement.style.width;

            // Call the method on our single popupManager instance.
            popupManager.show(title, body, width, { x: event.pageX, y: event.pageY });
        } else {
            // Call the static method directly on the class.
            Popup.showBrowserPopup(anchor.href, type, ...args);
        }
    });
})();
