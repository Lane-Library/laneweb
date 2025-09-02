(() => {
    "use strict";

    const searchResults = document.querySelector(".lwSearchResults");

    // guardian: exit if not on a search results page
    if (!searchResults) return;

    const handleCopyPermalink = async (permalinkNode) => {

        const anchor = permalinkNode.querySelector("a");
        if (!anchor) return;

        const originalHtml = permalinkNode.innerHTML;

        try {
            await navigator.clipboard.writeText(anchor.href);

            // tracking
            L.fire("tracker:trackableEvent", {
                category: "lane:permalinkCopied",
                action: anchor.textContent.trim(),
                label: anchor.href,
            });

            // show UI confirmation
            permalinkNode.innerHTML = `<i class="fa-regular fa-check"></i> Link copied`;

            // use a timer to restore the original HTML
            setTimeout(() => {
                permalinkNode.innerHTML = originalHtml;
            }, 2000);

        } catch (err) {
            console.error("Failed to copy permalink:", err);
        }
    };

    searchResults.addEventListener("click", (event) => {
        const permalink = event.target.closest(".permalink");
        if (permalink) {
            event.preventDefault();
            event.stopPropagation();
            handleCopyPermalink(permalink);
        }
    });
})();
