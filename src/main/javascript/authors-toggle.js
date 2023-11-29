(function() {

    "use strict";

    var initializeAuthorToggles = function() {
        document.querySelectorAll(".authorsTrigger").forEach(function(triggerNode) {
            if (!triggerNode.authorsTriggerSubscribed) {
                triggerNode.authorsTriggerSubscribed = true;
                triggerNode.addEventListener("click", function(event) {
                    var node = event.currentTarget,
                        anchorNode = node.querySelector('a'),
                        iconNode = node.querySelector('use'),
                        hideNode = node.parentNode.querySelector(".authors-hide");

                    event.stopPropagation();
                    event.preventDefault();
                    node.classList.toggle('active');
                    if (!node.classList.contains('active')) {
                        node.previousElementSibling.textContent = " - ";
                        hideNode.style.display = "block";
                        anchorNode.textContent = ' Show Less ';
                        iconNode.setAttribute('href', '/resources/svg/regular.svg#angle-double-up');
                    } else {
                        node.previousElementSibling.textContent = " ... ";
                        hideNode.style.display = "none";
                        anchorNode.textContent = ' Show More ';
                        iconNode.setAttribute('href', '/resources/svg/regular.svg#angle-double-down');
                    }
                });
            }
        });
    };

    //add trigger markup and delegate click events on class "authorsTrigger"
    if (document.querySelector('#searchResults')) {
        initializeAuthorToggles();
    }

    //reinitialize when content has changed
    L.on('lane:new-content', function() {
        initializeAuthorToggles();
    });

})();
