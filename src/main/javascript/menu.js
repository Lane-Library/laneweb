(() => {

    "use strict";

    /**
     * Manages the state and interactions of various menu components
     */
    class Menu {

        #mainNavItems;
        #hashLinks;

        constructor() {
            this.#mainNavItems = document.querySelectorAll("nav ul li.nav-menu");
            this.#hashLinks = document.querySelectorAll(".menu-container.hoverline ul li a[href^='#']");

            this.#initializeMobileToggles();
            this.#updateMainNav();
            this.#initializeHashNav();

            // Set initial active state from URL hash on page load.
            this.#updateHashNav(window.location.hash);

            this.#bindCustomEvents();
        }

        /**
         * Sets up event listeners for mobile menu toggles ('active' class added when  h2 is clicked)
         */
        #initializeMobileToggles = () => {
            document.querySelectorAll('.menu-container.mobile h2, .menu-container.phone h2').forEach(menuHeader => {
                if (!menuHeader.dataset.menuToggle) {
                    menuHeader.addEventListener('click', (event) => {
                        event.preventDefault();
                        menuHeader.closest(".menu-container")?.classList.toggle('active');
                    });
                    menuHeader.dataset.menuToggle = 'true';
                }
            });
        }

        /**
         * Sets the active state ('red' clas) for the main navigation based on the current page URL.
         */
        #updateMainNav() {
            const { pathname: currentPath, hash: currentHash } = window.location;
            this.#mainNavItems.forEach(navItem => {
                const hasActiveLink = Array.from(navItem.querySelectorAll("a")).some(
                    link => link.pathname === currentPath && link.hash === currentHash
                );
                if (hasActiveLink) {
                    navItem.querySelector('span')?.classList.add("red", "btm-brdr-red");
                }
            });
        }

        /**
         * Initializes click handlers for hash-based navigation menus.
         */
        #initializeHashNav() {
            this.#hashLinks.forEach(link => {
                link.addEventListener('click', (event) => {
                    this.#updateHashNav(event.currentTarget.hash);
                    L.fire("menu-changed", { hash: event.currentTarget.hash });
                });
            });
        }

        /**
         * Updates the active state of hash-based menu links like on guides.html and mobile-applications.html
         * @param {string} hash - The hash to select (e.g., "#section1").
         */
        #updateHashNav = (hash) => {
            // do nothing if hash is missing or just '#'
            if (!hash || hash === '#' || this.#hashLinks.length === 0) return;

            this.#hashLinks.forEach(link => link.classList.remove('menuitem-active'));

            const selectedLink = document.querySelector(`.menu-container.hoverline ul li a[href="${hash}"]`);
            selectedLink?.classList.add('menuitem-active');
        }

        /**
         * Binds listeners for custom application events.
         */
        #bindCustomEvents() {
            L.on("solrFacets:loaded", this.#initializeMobileToggles);
            L.on("content-changed", (event) => this.#updateHashNav(event.hash));
        }
    }

    // Initialize the menu component and attach to L namespace (for consistency).
    L.Menu = new Menu();

})();
