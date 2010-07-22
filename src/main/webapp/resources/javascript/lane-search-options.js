/*
 * adds and animates show/hide toggle for search results
 * 
 */
YUI().use("lane", 'node-base','anim','event-custom', function(Y) {

	var time = new Date().getTime();
    Y.publish("lane:searchOptionsChange",{
        broadcast:2,
        emitFacade: true,
        action:null
    });
    var options = Y.Node.create('<span id="optionsToggle" class="closed"><a href="">Hide Options</a></span>'),
        optionsNode = Y.one("#searchOptions"),
//        leftGrids = Y.one(".leftGrids"),
//        middleColumn = Y.one(".middleColumn"),
//        borderReset = '1px solid #E6E4DB',
        closeResultsContainerAnim = new Y.Anim( {
            node : '.search',
            easing : Y.Easing.easeOut,
            duration : 0.3,
            to : {
                marginLeft : '-185px'
            }
        }),
        openResultsContainerAnim = new Y.Anim( {
            node : '.search',
            easing : Y.Easing.easeOut,
            duration : 0.3,
            to : {
                marginLeft : 0
            }
        }),
        closeResultsAnim = new Y.Anim( {
            node : '#searchResults',
            easing : Y.Easing.easeOut,
            duration : 0.3,
            to : {
                marginRight : '-185px'
            }
        }),
        openResultsAnim = new Y.Anim( {
            node : '#searchResults',
            easing : Y.Easing.easeOut,
            duration : 0.3,
            to : {
                marginRight : 0
            }
        }),
        closeFacetsAnim = new Y.Anim( {
            node : '#searchFacets',
            easing : Y.Easing.easeOut,
            duration : 0.3,
            to : {
            opacity : 0
        }
        }),
        openFacetsAnim = new Y.Anim( {
            node : '#searchFacets',
            easing : Y.Easing.easeOut,
            duration : 0.3,
            to : {
                opacity : 1
            }
        }),
        onControlClick = function(e){
            var parent = this.get("parentNode");
            if (parent.hasClass("closed")) {
                closeFacetsAnim.run();
                parent.removeClass("closed");
                this.set('innerHTML', 'Show Options');
                parent.addClass("open");
                Y.fire("lane:searchOptionsChange",{
                    action:"hide"
                });
            } else {
                openResultsContainerAnim.run();
                openResultsAnim.run();
                parent.removeClass("open");
                this.set('innerHTML', 'Hide Options');
                parent.addClass("closed");
                Y.fire("lane:searchOptionsChange",{
                    action:"show"
                });
            }
            e.preventDefault();
//        },
//        toggleColumnBorders = function(direction){
//            if(direction == 'close'){
//                leftGrids.setStyle("borderRight","none");
//                middleColumn.setStyle("borderLeft","none");
//            }
//            else{
//                leftGrids.setStyle("borderRight",borderReset);
//                middleColumn.setStyle("borderLeft",borderReset);
//            }
        };

    closeFacetsAnim.on('end', function() {
        closeResultsContainerAnim.run();
        if (!Y.UA.ie){ //FIXME: can't get IE to overflow text properly
            closeResultsAnim.run();
        }
        //toggleColumnBorders('close');
    });

    openResultsContainerAnim.on('end', function() {
        openFacetsAnim.run();
        //toggleColumnBorders();
    });
    
    if(optionsNode){
        optionsNode.insert(options);
        options.one("a").on("click", onControlClick);
    }

    LANE.log("lane-search-options.js:use() " + (new Date().getTime() - time));

});
