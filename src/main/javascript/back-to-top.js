(() => {

    "use strict";

    // backToTop node should be present in footer
    const backToTop = document.querySelector('.back-to-top');

    // exit if no backToTop node
    if (!backToTop) {
        return;
    }

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

    backToTop.addEventListener('click', scrollToTop);

})();
