(() => {

    "use strict";

    /**
     * view handles interactions with the DOM.
     * @param {NodeListOf<Element>} bookImageNodes - A NodeList of div nodes with the class 'bookcover'.
     * @returns {object} - An object with methods to interact with the view.
     */
    const view = ((bookImageNodes) => {
        // a map of bookcover ids (bcids) to img nodes
        const imageMap = new Map();

        // Initialize the imageMap.
        bookImageNodes.forEach(imageNode => {
            const bcids = imageNode.dataset.bcids?.split(',') ?? [];
            // course reserves and equipment records will have a data-bibid (change to data-bcid?)
            if (imageNode.dataset.bibid) {
                bcids.push(`bib-${imageNode.dataset.bibid}`);
            }
            bcids.forEach(bcid => {
                const nodes = imageMap.get(bcid) || [];
                imageMap.set(bcid, [...nodes, imageNode]); // Use spread syntax for immutability.
            });
        });

        // make a couple of view functions available
        return {
            /**
             * Finds images in the viewport that need their src attribute updated.
             * @param {object} viewport - An object with a nearView method.
             * @returns {string[]} - An array of bcids for images to update.
             */
            getImgsForUpdate(viewport) {
                const imagesForUpdate = new Set();
                for (const [bcid, nodes] of imageMap.entries()) {
                    if (nodes.some(node => !node.dataset.requested && viewport.nearView(node, 3))) {
                        imagesForUpdate.add(bcid);
                    }
                }
                // convert Set back to an array
                return [...imagesForUpdate];
            },

            /**
             * Updates the DOM with the new image sources.
             * @param {object} updates - An object mapping bcids to image URLs.
             */
            update(updates) {
                // process all bcids that were part of the request
                updates.bcids.forEach(bcid => {
                    const url = updates.covers[bcid];
                    const imageNodes = imageMap.get(bcid);

                    if (imageNodes) {
                        if (url) {
                            // case 132771: Use protocol-relative URLs.
                            const src = url.substring(url.indexOf(":") + 1);
                            imageNodes.forEach(node => {
                                node.innerHTML = `<img src='${src}' alt='cover image'/>`;
                            });
                        }
                        // set data-requested on all nodes for this bcid to prevent re-requesting
                        imageNodes.forEach(node => {
                            node.dataset.requested = "true";
                        });
                    }
                    // clean up the map to prevent re-processing, even if no URL was found
                    imageMap.delete(bcid);
                });
            }
        };
    })(document.querySelectorAll(".bookcover[data-bcids],.bookcover[data-bibid]"));

    /**
     * Communicates with the server to get bookcover thumbnail URLs.
     */
    const bookcoverService = (() => {
        const baseURL = `${window.model["base-path"]}/apps/bookcovers?`;

        const service = {
            /**
             * Fetches thumbnail URLs from the server using the modern Fetch API.
             * @param {string[]} bcids - An array of bookcover IDs.
             */
            async getBookCoverURLs(bcids) {
                // Take only the first 20 bcids using slice().
                const params = bcids.slice(0, 20)
                    .map(bcid => `bcid=${encodeURIComponent(bcid)}`)
                    .join('&');

                const url = `${baseURL}${params}`;

                try {
                    const response = await fetch(url);
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status} ${response.statusText}`);
                    }
                    const data = await response.json();
                    // Fire event with both the returned covers and the list of bcids that were requested.
                    service.fire("covers", { covers: data, bcids: bcids.slice(0, 20) });
                } catch (error) {
                    console.error("Failed to fetch book covers:", error);
                }
            }
        };

        // make the service an EventTarget
        L.addEventTarget(service);
        return service;
    })();

    /**
     * Controls the communication between the viewport, service, and view.
     */
    const controller = {
        covers(event) {
            view.update(event);
        },

        update(viewport) {
            const bcids = view.getImgsForUpdate(viewport);
            if (bcids.length > 0) {
                bookcoverService.getBookCoverURLs(bcids);
            }
        }
    };

    bookcoverService.on("covers", controller.covers);

    L.on("viewport:init", event => controller.update(event.viewport));
    L.on("viewport:scrolled", event => controller.update(event.viewport));

})();
