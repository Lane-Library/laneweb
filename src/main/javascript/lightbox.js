(function () {

    "use strict";

    class Lightbox {
        constructor() {
            this.lightbox = document.createElement('div');
            this.lightbox.classList.add("lightbox");
            this.lightbox.classList.add("lightbox-hidden");
            this.background = document.createElement('div');
            this.background.classList.add("lightboxbg");
            this.background.classList.add("lightboxbg-hidden");
            document.body.appendChild(this.background);
            document.body.appendChild(this.lightbox);
            this.background.addEventListener("click", this.hide.bind(this));
        };

        show() {
            this.lightbox.classList.remove("lightbox-hidden");
            this.background.classList.remove("lightboxbg-hidden");
            this.setPosition();
        }


        hide() {
            this.lightbox.classList.add("lightbox-hidden");
            this.background.classList.add("lightboxbg-hidden");
        }

        setContent(content) {
            this.lightbox.innerHTML = content;
        }

        setPosition() {
            let x = (window.innerWidth - this.lightbox.offsetWidth) / 2;
            let y = (window.innerHeight - this.lightbox.offsetHeight) / 2;
            this.lightbox.style.left = x + "px";
            this.lightbox.style.top = y + "px";
        }

    }

    L.Lightbox = new Lightbox();
})();

