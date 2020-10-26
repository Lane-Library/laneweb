(function() {

    "use strict";

    if (document.querySelector(".permalink")) {

        var handlePermalinkClick = function(node, event) {
            event.stopPropagation();
            event.preventDefault();
            node.classList.toggle("active");
            var isActive = node.classList.contains("active"),
                li = node.closest('li'),
                sourceInfo = li.querySelector('.sourceInfo'),
                expandedPermalink = li.querySelector('.expandedPermalink');

            if (isActive) {
                sourceInfo.insertAdjacentHTML("afterend",
                  '<div class="expandedPermalink no-bookmarking">' +
                  ' <div>' +
                  '   <input value="' + node.querySelector('a').href + '"/>' +
                  ' </div>' +
                  ' <div>' +
                  '   <i class="fa fa-clipboard"></i> <a class="copyPermalink" href="#"> Copy permanent link to clipboard </a>' +
                  ' </div>' +
                  '</div>');
                document.querySelector(".copyPermalink").addEventListener("click", function(e) {
                    handlePermalinkCopyClick(e);
                });
            } else if (expandedPermalink) {
                expandedPermalink.remove();
            }
            L.fire("tracker:trackableEvent", {
                category: "lane:permalinkTrigger",
                action: event.target.textContent,
                label: node.querySelector('a').href
            });
        },
        handlePermalinkCopyClick = function(event) {
            var expandedPermalink = event.target.closest('.expandedPermalink'), 
                copyNode = expandedPermalink.querySelector('input'),
                statusNode = expandedPermalink.querySelector('div:nth-child(2)');
            event.stopPropagation();
            event.preventDefault();
            copyNode.select();
            copyNode.setSelectionRange(0, 99999);
            document.execCommand("copy");
            copyNode.blur();
            statusNode.innerHTML = '<i class="fa fa-check"></i> Permanent link copied';
            L.fire("tracker:trackableEvent", {
                category: "lane:permalinkCopied",
                action: event.target.textContent,
                label: copyNode.value
            });
        };

        document.querySelector(".lwSearchResults").addEventListener("click", function(event) {
            var permalink = event.target.closest(".permalink");
            if (permalink) {
                handlePermalinkClick(permalink, event);
            }
        });

    }

})();
