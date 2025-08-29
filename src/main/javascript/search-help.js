{

    "use strict";

    const helpNode = document.querySelector(".search-help a");

    // guard clause: exit if no help link present
    if (helpNode) {
        const basePath = window.model?.["base-path"] ?? "";

        const updateHelpLink = ({ newVal }) => {
            // destructure to get the `source` value
            // then destructure again using the computed `source` as a key for `help` value
            const { source, [source]: { help } } = newVal;

            if (help) {
                helpNode.href = `${basePath}${help}`;
            }
        };

        L.on("searchDropdown:change", updateHelpLink);

    }

}
