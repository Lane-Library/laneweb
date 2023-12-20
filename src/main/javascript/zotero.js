(function() {

    "use strict";

    /**
     * LANEWEB-11068: Basic Zotero support for DOIs
     * Zotero does a decent job of scraping free-text DOIs and expanding them to complete citations.
     * For more comprehensive Zotero support, consider storing metadata objects (COinS?) in Solr at index time.
     */
    let dois = [];
    document.querySelectorAll("li[data-doi]").forEach(function(node) {
        let doi = node.dataset.doi;
        if (!dois.includes(doi)) {
            dois.push(doi);
        }
    })
    if (dois.length) {
        document.body.insertAdjacentHTML("beforeend",
            '<span class="zotero-metadata">doi:' + dois.join(' doi:') + '</span>');
    }

})();