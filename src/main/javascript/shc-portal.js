(() => {

    "use strict";

    // guard clause to exit early if SHC PICO form not present
    if (!document.querySelector('.verticalPico')) {
        return;
    }

    window.addEventListener("load", () => {

        /**
         * Attaches a submit event listener to all forms on the page
         * to track submissions as pageviews.
         */
        const initializeFormTracking = () => {
            const forms = document.querySelectorAll('form');

            forms.forEach(form => {
                form.addEventListener("submit", event => {
                    const targetForm = event.target;
                    let title = targetForm.name;

                    if (!title && targetForm.classList.contains('search-form')) {
                        // special case for the main search form
                        title = `SHC-Epic Lane search ${L.search.getSource()}`;
                    }

                    // not title, no tracking
                    if (!title) {
                        return;
                    }

                    const urlParts = targetForm.action.match(/.*:\/\/([^/]+)\/(.*)/);
                    if (!urlParts) return;

                    // Destructure for clarity.
                    const [, host, path] = urlParts;

                    L.fire("tracker:trackablePageview", {
                        title,
                        host: host === location.host ? "" : host,
                        path: `/${path}`,
                        external: host !== location.host
                    });
                });
            });
        };

        /**
         * Sets up the PICO form functionality, including MeSH suggestions
         * and synchronizing inputs with the main search form
         */
        const initializePicoForm = () => {
            const picoForm = document.querySelector('.verticalPico');
            const picoInputs = picoForm.querySelectorAll('input[type="text"]');
            const picoQueryInput = picoForm.querySelector("input[name=q]");
            const mainQueryInput = document.querySelector(".search-form input[name=q]");

            /**
             * Build a query string from PICO inputs.
             * Example: (term1) AND (term2)
             */
            const getPicoQuery = () => {
                const terms = Array.from(picoInputs)
                    .map(input => input.value.trim())
                    .filter(value => value); // Keep only non-empty terms.

                if (terms.length === 0) return "";
                if (terms.length === 1) return terms[0];

                return terms.map(term => `(${term})`).join(' AND ');
            };

            /**
            * update the hidden and visible query inputs with the PICO query
            */
            const updateMainQuery = () => {
                const picoQuery = getPicoQuery();
                mainQueryInput.value = picoQuery;
                picoQueryInput.value = picoQuery;
            };

            picoInputs.forEach(input => {
                const sourceBase = '/apps/suggest/getSuggestionList?q={query}&l=mesh';
                const suggest = new L.Suggest(input, sourceBase);
                L.addEventTarget(suggest);

                // attach the same handler to multiple events
                suggest.on("suggest:select", updateMainQuery);
                ['blur', 'keyup'].forEach(eventType => {
                    input.addEventListener(eventType, updateMainQuery);
                });
            });

            // the PICO form is visible once initialized
            picoForm.style.visibility = "visible";
        };

        initializeFormTracking();
        initializePicoForm();
    });

})();
