(() => {

    "use strict";

    const bassettContent = document.querySelector('#bassettContent');
    const accordion = document.querySelector('#bassett-menu');

    // exit if core elements aren't on the page
    if (!bassettContent || !accordion) {
        return;
    }

    // --- State and Constants ---
    const model = L.Model;
    const basePath = model.get(model.BASE_PATH) || "";
    const HIDE_TEXT = 'Hide';
    const SHOW_ALL_TEXT = 'See All';
    const SUB_REGIONS_TO_SHOW = 4;
    let diagramDisplay = false;

    // --- Core Functions ---
    const formatAjaxUrl = (hrefString) => {
        const url = new URL(hrefString.replace("search.html", "/bassett/bassettsView.html"));
        let path = url.pathname.substring(url.pathname.indexOf('/bassett/') + 8) + url.search;

        if (diagramDisplay && !path.includes('t=diagram')) {
            path += path.includes('?') ? '&t=diagram' : '?t=diagram';
        }
        return path;
    };

    const submitPagination = (event) => {
        const pageInput = event.target.page;
        const pagesTotal = event.target.pages;

        if (!/^\d+$/.test(pageInput.value) || Number(pageInput.value) < 1 || Number(pageInput.value) > Number(pagesTotal.value)) {
            event.preventDefault();
            document.querySelectorAll(".bassett-error").forEach(node => node.style.display = "block");
            return;
        }
        pagesTotal.remove();
    };

    const loadContent = async (pathSegment) => {
        const url = `${basePath}/plain/bassett/${pathSegment}`;
        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status} ${response.statusText}`);

            bassettContent.innerHTML = await response.text();

            registerLinksContainer(bassettContent);
            document.querySelectorAll('.s-pagination form[name=bassett-pagination]').forEach(node => {
                node.addEventListener('submit', submitPagination);
            });
        } catch (error) {
            console.error('Error fetching content:', error);
            bassettContent.innerHTML = '<p class="error">Sorry, content could not be loaded.</p>';
        }
    };

    const handleClick = (event) => {
        event.preventDefault();
        // use event.currentTarget instead of `this` to be compatible with arrow functions
        const link = event.currentTarget;

        if (link.id === "diagram-choice") diagramDisplay = true;
        if (link.id === "photo-choice") diagramDisplay = false;

        const url = formatAjaxUrl(link.href);
        window.history.pushState({ bassett: url }, "", "");
        loadContent(url);
    };

    const initializeHistory = () => {
        window.addEventListener("popstate", (event) => {
            const path = event.state?.bassett ?? formatAjaxUrl(document.location.href);
            loadContent(path);
        });

        if (window.history.state?.bassett) {
            loadContent(window.history.state.bassett);
        }
    };

    const registerLinksContainer = (container) => {
        if (container) {
            container.querySelectorAll('a')
                .forEach(anchor => {
                    if (anchor.hostname === window.location.hostname && !anchor.rel) {
                        anchor.addEventListener('click', handleClick);
                    }
                });
        }
    };

    // --- Menu Functions ---

    const resetSubRegions = () => {
        accordion.querySelectorAll('.region li.enabled').forEach(li => {
            li.classList.remove('enabled');
            const icon = li.querySelector('i');
            if (icon) {
                icon.classList.replace('fa-square-check', 'fa-square');
                icon.classList.replace('fa-solid', 'fa-regular');
            }
        });
    };

    const hideSubRegions = (event) => {
        const region = event.currentTarget.closest("ul");
        const subRegions = region.querySelectorAll('li');
        resetSubRegions();
        region.querySelector('.see-all').innerHTML = SHOW_ALL_TEXT;
        for (let i = SUB_REGIONS_TO_SHOW; i < subRegions.length; i++) {
            subRegions[i].style.display = "none";
        }
    };

    const expandSubRegion = (event) => {
        const region = event.currentTarget.closest("ul");
        const subRegions = region.querySelectorAll('li');
        resetSubRegions();
        region.querySelector('.see-all').innerHTML = HIDE_TEXT;
        for (let i = SUB_REGIONS_TO_SHOW + 1; i < subRegions.length; i++) {
            subRegions[i].style.display = 'block';
        }
    };

    const displaySubRegion = (event) => {
        const seeAllContent = event.currentTarget.innerHTML;
        if (seeAllContent === HIDE_TEXT) {
            hideSubRegions(event);
        } else {
            expandSubRegion(event);
        }
    };

    const surlineSubRegion = (event) => {
        resetSubRegions();
        const li = event.currentTarget;
        li.classList.add('enabled');
        const icon = li.querySelector('i');
        if (icon) {
            icon.classList.replace('fa-square', 'fa-square-check');
            icon.classList.replace('fa-regular', 'fa-solid');
        }
    };

    registerLinksContainer(accordion);
    registerLinksContainer(bassettContent);

    document.querySelectorAll('.see-all').forEach(node => {
        node.addEventListener('click', displaySubRegion);
    });
    document.querySelectorAll('.region li:not(:first-child)').forEach(node => {
        node.addEventListener('click', surlineSubRegion);
    });
    document.querySelectorAll('.s-pagination form[name=bassett-pagination]').forEach(node => {
        node.addEventListener('submit', submitPagination);
    });

    initializeHistory();

})();
