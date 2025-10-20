(() => {

    "use strict";

    document.querySelectorAll('.hldgsTrigger').forEach(node => {
        node.addEventListener("click", (event) => {
            event.preventDefault();

            const eresource = node.closest("li");
            const ancestor = node.closest(".hldgsContainer");
            const wasActive = ancestor.classList.contains("active");

            ancestor.classList.toggle("active");

            // Use a ternary operator and template literal for a concise label
            const actionLabel = `${ancestor.querySelector('.hldgsHeader').textContent.trim()} -- ${wasActive ? 'close' : 'open'}`;

            L.fire("tracker:trackableEvent", {
                category: "lane:hldgsTrigger",
                action: actionLabel,
                label: eresource.querySelector('.primaryLink').textContent
            });
        });
    });

})();
