(() => {

    "use strict";

    const spellCheckContainer = document.querySelector('.spellCheck');
    const model = L.Model;
    const encodedQuery = model.get(model.URL_ENCODED_QUERY);
    const basePath = model.get(model.BASE_PATH) || "";

    if (!spellCheckContainer || !encodedQuery) {
        return;
    }

    /**
     * Creates the corrected search URL using the robust URL API.
     * @param {string} suggestion - The suggested search term.
     * @returns {string} The newly constructed URL.
     */
    const createCorrectedUrl = (suggestion) => {
        const url = new URL(window.location.href);
        url.searchParams.set('q', suggestion);
        url.searchParams.set('laneSpellCorrected', decodeURIComponent(encodedQuery));
        url.hash = '';
        return url.toString();
    };

    /**
     * Updates the DOM to display the spellcheck suggestion.
     * @param {string} suggestion - The suggested search term.
     */
    const updateSpellcheckUI = (suggestion) => {
        const link = spellCheckContainer.querySelector('a');
        if (link) {
            link.href = createCorrectedUrl(suggestion);
            link.textContent = suggestion;
            spellCheckContainer.style.visibility = "visible";
        }
    };

    /**
     * Fires a tracking event for the spellcheck suggestion and original query.
     * @param {string} suggestion - The suggested search term.
     */
    const trackSuggestion = (suggestion) => {
        L.fire("tracker:trackableEvent", {
            category: "lane:spellSuggest",
            action: `query=${decodeURIComponent(encodedQuery)}`,
            label: `suggestion=${suggestion}`
        });
    };

    const fetchAndApplySuggestion = async () => {
        try {
            const response = await fetch(`${basePath}/apps/spellcheck/json?q=${encodedQuery}`);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status} ${response.statusText}`);
            }

            const { suggestion } = await response.json();

            if (suggestion) {
                updateSpellcheckUI(suggestion);
                trackSuggestion(suggestion);
            }
        } catch (error) {
            console.error('Spellcheck fetch failed:', error);
        }
    };

    fetchAndApplySuggestion();

})();

