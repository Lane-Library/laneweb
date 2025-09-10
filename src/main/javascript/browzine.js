(() => {

    "use strict";

    const Model = L.Model;
    const BASE_PATH = Model.get(Model.BASE_PATH) ?? "";

    // view handles interactions with the DOM, created with a NodeList
    // of search result li nodes with data-doi attributes.
    const view = ((searchResultNodes) => {

        const doiMap = new Map();

        // initialize the doiMap
        searchResultNodes.forEach(node => {
            const doi = node.dataset.doi.toLowerCase();
            doiMap.set(doi, node);
        });

        return {
            /**
             * Returns a list of DOIs for articles that are in the viewport and need data.
             * @param {object} viewport - An object with a nearView method.
             * @returns {string[]}
             */
            getDoisForUpdate(viewport) {
                const doisForUpdate = [];
                for (const [doi, node] of doiMap.entries()) {
                    if (!node.fetched && viewport.nearView(node, 3)) {
                        doisForUpdate.push(doi);
                    }
                }
                return doisForUpdate;
            },

            /**
             * Adds PDF links and cover images to search result nodes.
             * @param {object} article - The article data from the API.
             */
            update(article) {
                const { retractionNoticeUrl, fullTextFile: fulltextUrl, contentLocation, doi: rawDoi } = article.data;
                const doi = rawDoi?.toLowerCase();

                const coverImageUrl = article.included?.[0]?.coverImageUrl ?? null;

                if (doiMap.has(doi)) {
                    const node = doiMap.get(doi);
                    node.fetched = true;

                    if (retractionNoticeUrl) {
                        addRetractedArticleLink(node, 'xmark', 'Retracted Article', retractionNoticeUrl);
                    } else if (fulltextUrl) {
                        addFulltextLink(node, 'Direct to PDF', fulltextUrl);
                    } else if (contentLocation) {
                        addFulltextLink(node, 'Direct to Full Text', contentLocation);
                    }

                    if (coverImageUrl) {
                        node.querySelector('.bookcover').innerHTML = `<img src="${coverImageUrl}" alt="Article cover image"/>`;
                    }
                    // clean up prevent re-processing
                    doiMap.delete(doi);
                }
            },

            /**
             * Marks a DOI as fetched to prevent redundant API calls (e.g., for 404s).
             * @param {string} doi
             */
            markAsFetched(doi) {
                const node = doiMap.get(doi.toLowerCase());
                if (node) {
                    node.fetched = true;
                }
            }
        };
    })(document.querySelectorAll("li[data-doi]"));

    // Helper function to add a full-text link.
    const addFulltextLink = (node, label, url) => {
        const link = node.querySelector('.resource-detail .hldgsContainer span a');
        if (link) {
            link.href = url;
            link.querySelector('span').textContent = label;
        }
    };

    // Helper function to add a link for a retracted article.
    const addRetractedArticleLink = (node, type, label, url) => {
        const div = document.createElement('div');
        const link = document.createElement('a');
        link.className = 'bzFT';
        link.href = url;
        link.innerHTML = `<i class="fa-light fa-file-${type}"></i> ${label}`;

        div.append(link);
        node.querySelector('.sourceInfo')?.append(div);
    };

    // Service to communicate with the server to fetch article data for each DOI.
    const articleLookupService = (() => {
        const baseURL = `${BASE_PATH}/apps/browzine/doi/`;

        const service = {
            /**
             * Fetches article data from the API using async/await.
             * @param {string} doi
             */
            async getArticleData(doi) {
                const url = `${baseURL}${doi}`;
                try {
                    const response = await fetch(url);

                    if (response.status === 404) {
                        // fire article:notfound to mark this doi as fetched and then stop further processing
                        service.fire("article:notfound", { doi });
                        return;
                    }

                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status} ${response.statusText}`);
                    }

                    const article = await response.json();
                    service.fire("article", { article });

                } catch (error) {
                    console.error(`Failed to fetch article data for DOI ${doi}:`, error);
                    // mark as fetched to prevent retrying a failing DOI
                    service.fire("article:notfound", { doi });
                }
            }
        };

        L.addEventTarget(service);
        return service;
    })();

    // Controls the communication between the viewport, service, and view.
    const controller = {
        article({ article }) {
            view.update(article);
        },

        articleNotFound({ doi }) {
            view.markAsFetched(doi);
        },

        update(viewport) {
            const dois = view.getDoisForUpdate(viewport);
            if (dois.length > 0) {
                dois.forEach(doi => {
                    if (doi.trim()) {
                        articleLookupService.getArticleData(doi);
                    }
                });
            }
        }
    };

    // --- Event Listener Setup ---
    articleLookupService.on("article", controller.article);
    articleLookupService.on("article:notfound", controller.articleNotFound);

    // Use arrow functions for concise event handlers.
    L.on("viewport:init", event => controller.update(event.viewport));
    L.on("viewport:scrolled", event => controller.update(event.viewport));

})();
