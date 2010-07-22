/*
   This code makes the columns the same height so that their borders are the same.
*/
YUI().use("node", function(Y) {

	var time = new Date().getTime();
    
    Y.on("domready", function() {
        var i, height, region, maxHeight = 0,
            columns = Y.all(".leftColumn, .middleColumn, .rightColumn");
        for (i = 0; i < columns.size(); i++) {
            region = columns.item(i).get("region");
            height = region.bottom - region.top;
            maxHeight = height > maxHeight ? height : maxHeight;
        }
        columns.setStyle("height", maxHeight + "px");
    });

    LANE.log("lane-column-height.js:use() " + (new Date().getTime() - time));
    
});
