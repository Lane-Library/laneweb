(function() {

    /* javascript to set the height of a group of boxes to the that of the tallest one.
     * The boxes are grouped using the same-height-n class where n is a number 0-9 */

    "use strict";

    var maxHeight,
        // get all the nodes with same-height-n
        nodes = Y.all("*[class^='same-height-'], *[class*=' same-height-']"),
        size = nodes.size(),
        heights,
        i,
        clazz = "",
        classes = {},
        regex = /.*(same-height-\d).*/,
        matches;

    // find the class for each group
    for (i = 0; i < size; i++) {
        matches = regex.exec(nodes.item(i).getAttribute("class"));
        if (matches.length === 2) {
            classes[matches[1]] = "." + matches[1];
        }
    }

    for (clazz in classes) {
        if (classes.hasOwnProperty(clazz)) {
            // for each group find the tallest
            nodes = Y.all(classes[clazz]);
            size = nodes.size();
            heights = [];
            for (i = 0; i < size; i++) {
                heights.push(nodes.item(i).get("clientHeight"));
            }
            maxHeight = Math.max.apply(null, heights);
            // set all of the group to the tallest's height
            for (i = 0; i < size; i++) {
                nodes.item(i).setStyle("min-height", maxHeight + "px");
            }
        }
    }


})();