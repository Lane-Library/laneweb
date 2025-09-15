(() => {

    "use strict";

    const guideContainer = document.querySelector(".guide");
    if (!guideContainer) {
        return;
    }

    const defaultGuide = '#all-guides';
    const allGuidesClosed = '#off';

    /**
     * Removes the 'menuitem-active' class from all relevant guide links.
     */
    const closeAllGuides = () => {
        const activeElements = document.querySelectorAll('.menu-guide ul li a.menuitem-active, .libguides div.menuitem-active');
        activeElements.forEach(el => el.classList.remove('menuitem-active'));
    };

    /**
     * Activates a specific guide menu item and fires a content-changed event.
     * @param {string} hash - The ID selector for the guide to open.
     */
    const openGuide = (hash) => {
        if (hash !== allGuidesClosed) {
            // only add class if the element exists
            const guideToOpen = document.querySelector(hash);
            guideToOpen?.classList.add('menuitem-active');
        }
        L.fire("content-changed", { hash });
    };

    // --- Event Handlers ---

    const handleMenuChange = (event) => {
        closeAllGuides();
        openGuide(event.hash);
    };

    const handleToggleOn = (event) => {
        event.preventDefault();
        closeAllGuides();
        openGuide(event.currentTarget.hash);
    };

    const handleToggleOff = (event) => {
        event.preventDefault();
        closeAllGuides();
    };

    // --- Initializer & Event Listeners ---

    const initialize = () => {
        const hash = window.location.hash || defaultGuide;
        openGuide(hash);

        L.on("menu-changed", handleMenuChange);

        document.querySelectorAll('.guide-menu-toggle.on').forEach(anchor => {
            anchor.addEventListener('click', handleToggleOn);
        });

        document.querySelectorAll('.guide-menu-toggle.off').forEach(button => {
            button.addEventListener('click', handleToggleOff);
        });
    };

    window.addEventListener("load", initialize, { once: true });

})();