(() => {
    "use strict";

    const navMenus = document.querySelectorAll(".nav-menu");
    const openButton = document.querySelector("#nav-toggle-on");
    const closeElements = document.querySelectorAll(".menu-overlay, #nav-toggle-off");
    const superHeader = document.querySelector("header:first-of-type");
    const blurableNodes = document.querySelectorAll(".content, footer, .mobile-screen-menu.lrg-screen-hide");

    // exit if core navigation elements don't exist
    if (navMenus.length === 0 || !openButton || closeElements.length === 0) {
        return;
    }

    /** Toggles visibility of the header above the main navigation
     *
     */
    const toggleSuperHeader = () => {
        if (superHeader) {
            superHeader.classList.toggle('medium-screen-hide');
        }
    };

    /**
     * Toggles 'blur' class on background elements
     * @param {boolean} shouldBlur - True to add the class, false to remove it.
     */
    const toggleBlur = (shouldBlur) => {
        blurableNodes.forEach(node => node.classList.toggle('blur', shouldBlur));
    };

    /** Handles clicks on main navigation items to toggle submenus on small screens */
    const handleSubmenuToggle = (event) => {
        const nav = event.currentTarget;
        const clickTarget = event.target;
        const navContent = nav.querySelector('.dropdown-content');

        // Check if we are on a small screen and the click was on the menu item
        // itself (not a link within its dropdown).
        const isSmallMedia = window.matchMedia("(max-width: 1099px)").matches;
        if (isSmallMedia && navContent && !clickTarget.closest('a[href]')) {
            nav.classList.toggle('nav-menu-active-on-click');
            navContent.classList.toggle('dropdown-content-on-click');
        }
    };

    /** Handles opening the main mobile navigation */
    const handleOpenMenu = () => {
        toggleSuperHeader();
        toggleBlur(true);
    };

    /** Handles closing the main mobile navigation */
    const handleCloseMenu = () => {
        window.location.hash = "#";
        toggleSuperHeader();
        toggleBlur(false);
    };

    // --- Event Listeners ---
    navMenus.forEach(menu => menu.addEventListener("click", handleSubmenuToggle));
    openButton.addEventListener("click", handleOpenMenu);
    closeElements.forEach(elem => elem.addEventListener("click", handleCloseMenu));

})();
