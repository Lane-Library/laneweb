(() => {

    "use strict";

    // backToTop node should be present in footer
    const backToTop = document.querySelector('.back-to-top');

    // exit if no backToTop node
    if (!backToTop) {
        return;
    }

    const SCROLL_THRESHOLD = 270;
    let isScrolling;

    /**
     * checks scroll position and toggles the button's visibility
     * @private
     */
    const handleScroll = () => {
        backToTop.classList.toggle('active', window.scrollY > SCROLL_THRESHOLD);
    };

    /**
     * scroll the browser back to the top
     * @private
     */
    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    // Listen for scrolls, using a throttle to prevent performance issues
    document.addEventListener('scroll', () => {
        if (isScrolling) return;

        isScrolling = true;
        // Use requestAnimationFrame for a smooth, performant check tied to browser repaints
        requestAnimationFrame(() => {
            handleScroll();
            isScrolling = false;
        });
    });

    backToTop.addEventListener('click', scrollToTop);

})();
