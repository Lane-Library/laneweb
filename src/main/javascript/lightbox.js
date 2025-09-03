(() => {
    "use strict";
    class Lightbox {
        #lightboxEl;
        #backgroundEl;

        constructor() {
            this.#lightboxEl = document.createElement('div');
            this.#lightboxEl.classList.add("lightbox");

            this.#backgroundEl = document.createElement('div');
            this.#backgroundEl.classList.add("lightboxbg");

            document.body.append(this.#backgroundEl, this.#lightboxEl);

            this.#backgroundEl.addEventListener("click", () => this.hide());
        }

        show() {
            document.body.classList.add("lightbox-active");
            this.setPosition();
        }

        hide() {
            document.body.classList.remove("lightbox-active");
        }

        /**
         * Sets the content of the lightbox. Can accept an HTML string or a DOM Node.
         * @param {string|Node} content
         */
        setContent(content) {
            if (typeof content === 'string') {
                this.#lightboxEl.innerHTML = content;
            } else if (content instanceof Node) {
                this.#lightboxEl.replaceChildren(content);
            }
        }

        /**
         * Sets the position of the lightbox to be centered in the viewport.
         */
        setPosition() {
            const x = (window.innerWidth - this.#lightboxEl.offsetWidth) / 2;
            const y = (window.innerHeight - this.#lightboxEl.offsetHeight) / 2;

            this.#lightboxEl.style.left = `${x}px`;
            this.#lightboxEl.style.top = `${y}px`;
        }
    }

    L.Lightbox = new Lightbox();
})();