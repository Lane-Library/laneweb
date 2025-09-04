(() => {

    "use strict";

    const slideContainer = document.querySelector("#slide-show");

    // guardian clause to bail if slideshow container not present
    if (!slideContainer) {
        return;
    }

    /**
     * A paged slideshow component.
     * It navigates "pages" of slides, lazy-loads images, and is responsive.
     */
    class PagedSlideshow {
        constructor(container) {
            this.container = container;
            this.slides = Array.from(container.querySelectorAll(".slide"));
            this.previousButton = document.getElementById("previous-slide");
            this.nextButton = document.getElementById("next-slide");

            // guardian clause to bail if essential elements are missing
            if (this.slides.length === 0 || !this.previousButton || !this.nextButton) {
                console.error("Slideshow is missing required elements.");
                return;
            }

            // --- State ---
            this.currentIndex = 0;       // first visible slide
            this.visibleSlides = 1;      // number of slides in a page
            this.imagesLoaded = false;
            this.slidePadding = 24;

            // Bind `this` for all methods used as event handlers.
            this.nextPage = this.nextPage.bind(this);
            this.previousPage = this.previousPage.bind(this);
            this.recalculateLayout = this.recalculateLayout.bind(this);
            this.lazyLoadImages = this.lazyLoadImages.bind(this);

            this.init();
        }

        init() {
            this.bindEvents();
            this.recalculateLayout();
        }

        bindEvents() {
            // wire the buttons to the paged navigation methods
            this.nextButton.addEventListener("click", this.nextPage);
            this.previousButton.addEventListener("click", this.previousPage);

            ['resize', 'orientationchange', 'load'].forEach(event => {
                window.addEventListener(event, this.recalculateLayout);
            });

            L.on("viewport:init", this.lazyLoadImages);
            L.on("viewport:scrolled", this.lazyLoadImages);
        }

        nextPage() {
            // move the index forward by the number of visible slides
            this.currentIndex += this.visibleSlides;
            this.updateDisplay();
        }

        previousPage() {
            // move the index back by the number of visible slides
            this.currentIndex -= this.visibleSlides;
            this.updateDisplay();
        }

        /**
         * Hide or show slides based on the current page index
         */
        updateDisplay() {
            // restrict the index to valid bounds (cannot go below 0 or past the end)
            this.currentIndex = Math.max(0, Math.min(this.currentIndex, this.slides.length - this.visibleSlides));

            this.slides.forEach((slide, index) => {
                const isVisible = index >= this.currentIndex && index < (this.currentIndex + this.visibleSlides);
                slide.classList.toggle('hidden', !isVisible);
            });

            this.updateButtons();
        }

        updateButtons() {
            this.previousButton.classList.toggle('disable', this.currentIndex === 0);
            const isAtEnd = this.currentIndex >= this.slides.length - this.visibleSlides;
            this.nextButton.classList.toggle('disable', isAtEnd);
        }

        recalculateLayout() {
            // us the parent element's width to determine the available space
            const containerWidth = this.container.parentElement.offsetWidth;
            const slideWidth = this.slides[0].offsetWidth + this.slidePadding;

            // ensure slideWidth is not zero to prevent divide-by-zero errors
            this.visibleSlides = slideWidth > 0 ? Math.floor(containerWidth / slideWidth) : 1;

            this.updateDisplay();
        }

        lazyLoadImages({ viewport }) {
            if (this.imagesLoaded || !viewport.nearView(this.container, 2)) {
                return;
            }

            this.slides.forEach(slide => {
                const img = slide.querySelector("img");
                if (img && img.dataset.src) {
                    img.src = img.dataset.src;
                }
            });

            this.imagesLoaded = true;
        }
    }

    new PagedSlideshow(slideContainer);

})();
