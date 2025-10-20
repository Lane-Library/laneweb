{

    "use strict";

    const form = document.querySelector('.search-form');
    // table search inputs (e.g. course reserves, liaisons, equipment) should not get solr suggestions: LANEWEB-11444
    const queryInput = form?.querySelector('input[name=q]:not(#table-search-input)');

    if (form && queryInput) {
        const sourceBase = '/apps/suggest/getSuggestionList?q={query}&l=';

        // Model: Holds the application state
        const model = {
            suggest: new L.Suggest(queryInput),
            source: form.querySelector('input[name=source]').value,
        };

        // View: Handles DOM updates and user-facing actions.
        const view = {
            search(query) {
                queryInput.value = query;
                queryInput.readOnly = true;
                L.search.search();
            },
        };

        // Controller: Connects the model and view, handles logic.
        const controller = {
            sourceChange(event) {
                const source = event.newVal;
                const suggestLimitInput = form.querySelector('input[name=suggest-limit]');
                let suggestEndpoint;

                if (suggestLimitInput) {
                    suggestEndpoint = `${sourceBase}${suggestLimitInput.value}`;
                } else if (source.match(/^(all|catalog)/)) {
                    suggestEndpoint = `${sourceBase}er-mesh`;
                } else {
                    suggestEndpoint = `${sourceBase}mesh`;
                }

                model.suggest.setSourceEndpoint(suggestEndpoint);
                model.source = source;
            },

            suggestion(event) {
                L.fire('tracker:trackableEvent', {
                    category: 'lane:suggestSelect',
                    action: model.source,
                    label: event.suggestion,
                });
                view.search(event.suggestion);
            },
        };

        // --- Initialization ---
        L.addEventTarget(model.suggest);
        model.suggest.on('suggest:select', controller.suggestion);
        L.on('search:sourceChange', controller.sourceChange);

        // Trigger the initial source setup.
        controller.sourceChange({ newVal: model.source });
    }
}
