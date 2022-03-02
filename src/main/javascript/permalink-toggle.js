(function() {

    "use strict";

    if (document.querySelector(".permalink")) {

        var handlePermalinkClick = function(node, event) {
            event.stopPropagation();
            event.preventDefault();
            var anchor = node.querySelector('a'), copyNode, originalHtml = node.innerHTML;
            anchor.insertAdjacentHTML("afterend",'<input value="' + anchor.href + '"/>');
            copyNode = node.querySelector("input");
            copyNode.select();
            copyNode.setSelectionRange(0, 99999);
            document.execCommand("copy");
            copyNode.blur();
            node.innerHTML = '<i class="fa-regular fa-check"></i> Link copied';
            L.fire("tracker:trackableEvent", {
                category: "lane:permalinkCopied",
                action: event.target.textContent,
                label: copyNode.value
            });
            setTimeout(function() {
                node.innerHTML = originalHtml;
            }, 2000);
        };

        document.querySelector(".lwSearchResults").addEventListener("click", function(event) {
            var permalink = event.target.closest(".permalink");
            if (permalink) {
                handlePermalinkClick(permalink, event);
            }
        });

    }

})();
