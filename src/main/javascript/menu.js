(() => {

    "use strict";

    // Toggle the 'active' class on mobile menus when their h2 is clicked
    document.querySelectorAll('.menu-container.mobile h2, .menu-container.phone h2').forEach(menuHeader => {
        menuHeader.addEventListener('click', (event) => {
            event.preventDefault();
            menuHeader.closest(".menu-container")?.classList.toggle('active');
        });
    });

    // Add 'red' class to the span of the nav item that matches the current page URL
    document.querySelectorAll("nav ul li.nav-menu").forEach(navItem => {
        const { pathname: currentPath, hash: currentHash } = window.location;
        const hasActiveLink = Array.from(navItem.querySelectorAll("a")).some(
            link => link.pathname === currentPath && link.hash === currentHash
        );

        if (hasActiveLink) {
            navItem.querySelector('span')?.classList.add("red", "btm-brdr-red");
        }
    });

    const hashLinks = document.querySelectorAll(".menu-container.hoverline ul li a[href^='#']");

    /** Remove 'menuitem-active' class from all hash-based menu links like on guides.html and mobile-applications.html. */
    const clearActiveHashLinks = () => {
        hashLinks.forEach(link => link.classList.remove('menuitem-active'));
    };

    /**
     * Finds a hash link by its href and sets it as active.
     * @param {string} hash - The hash to select (e.g., "#section1").
     */
    const selectMenuByHash = (hash) => {
        // do nothing if hash is missing or just '#'
        if (!hash || hash === '#') return;

        clearActiveHashLinks();

        const selectedLink = document.querySelector(`.menu-container.hoverline ul li a[href="${hash}"]`);
        selectedLink?.classList.add('menuitem-active');
    };

    // add listeners if hash-based links exist
    if (hashLinks.length > 0) {
        hashLinks.forEach(link => {
            link.addEventListener('click', (event) => {
                const newHash = event.currentTarget.hash;
                clearActiveHashLinks();
                event.currentTarget.classList.add('menuitem-active');
                L.fire("menu-changed", { hash: newHash });
            });
        });

        // On initial page load, set the active menu item from the URL hash.
        selectMenuByHash(window.location.hash);
    }

    // Listen for external events that change the content and update the menu.
    // Use destructuring to pull the 'hash' property from the event object.
    L.on("content-changed", ({ hash }) => {
        selectMenuByHash(hash);
    });

})();
