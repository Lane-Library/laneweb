(function () {

    "use strict";

    // backToTop node should be present in footer
    const backToTop = document.querySelector('.back-to-top');

    // exit if no backToTop node
    if (!backToTop) {
        return;
    }

    const SCROLL_THRESHOLD = 270;
    const FADE_OUT_DELAY = 450;

    /**
     * @method fadeIn display the backToTop node
     * @private
     */
    const fadeIn = () => {
        backToTop.style.visibility = "visible";
        backToTop.classList.add("active");
    };

    /**
     * @method fadeOut hide the backToTop node
     * @private
     */
    const fadeOut = () => {
        setTimeout(() => {
            backToTop.style.visibility = "hidden";
        }, FADE_OUT_DELAY);
        backToTop.classList.remove("active");
    };

    /**
     * @method scrollToTop scroll the browser back to the top
     * @private
     */
    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    // respond to scroll events and decide if the backToTop node needs to be hidden or displayed
    document.addEventListener("scroll", function () {
        if (window.scrollY > SCROLL_THRESHOLD && !backToTop.classList.contains("active")) {
            fadeIn();
        } else if (window.scrollY <= SCROLL_THRESHOLD && backToTop.classList.contains("active")) {
            fadeOut();
        }
    });

    // call the scrollToTop function when backToTop clicked
    backToTop.addEventListener("click", scrollToTop);

})();
