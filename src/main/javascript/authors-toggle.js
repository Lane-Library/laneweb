(() => {

    "use strict";

    const searchResultsContainer = document.querySelector('#searchResults');

    // exit if not on search results page
    if (!searchResultsContainer) {
        return;
    }

    // attach a single delegated event listener to the container
    searchResultsContainer.addEventListener("click", event => {
        const triggerNode = event.target.closest('.authorsTrigger');

        // exit if the click was not on a trigger
        if (!triggerNode) {
            return;
        }

        // act on the trigger
        event.stopPropagation();
        event.preventDefault();

        const anchorNode = triggerNode.querySelector('a');
        const iconNode = triggerNode.querySelector('i');
        const hideNode = triggerNode.parentNode.querySelector(".authors-hide");
        const isActive = triggerNode.classList.contains('active');

        triggerNode.previousElementSibling.textContent = isActive ? " ... " : " - ";
        hideNode.style.display = isActive ? "none" : "block";
        anchorNode.textContent = isActive ? ' Show More ' : ' Show Less ';
        iconNode.classList.toggle('fa-angle-double-up', !isActive);
        iconNode.classList.toggle('fa-angle-double-down', isActive);
        triggerNode.classList.toggle('active');
    });

})();
