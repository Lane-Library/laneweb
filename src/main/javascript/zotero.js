(() => {
    "use strict";

    /**
     * LANEWEB-11068: Basic Zotero support for DOIs
     * Zotero does a decent job of scraping free-text DOIs and expanding them to complete citations.
     * For more comprehensive Zotero support, consider storing metadata objects (COinS?) in Solr at index time.
     */
    const doiNodes = document.querySelectorAll("li[data-doi]");

    // get a unique array of doi values from the doiNodes
    const uniqueDois = [...new Set(Array.from(doiNodes, node => node.dataset.doi))];

    if (uniqueDois.length > 0) {
        const zoteroString = `doi:${uniqueDois.join(' doi:')}`;

        const zoteroHtml = `<span class="zotero-metadata">${zoteroString}</span>`;

        document.body.insertAdjacentHTML("beforeend", zoteroHtml);
    }

})();
