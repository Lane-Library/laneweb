/*
 *
 * JavaScript for Lane Mobile
 *
 *
 */

// set LANE namespace
if (typeof ($.LANE) === "undefined") {
    $.LANE = {};
}

/**
 * scroll pages to hide header and/or search form
 * only scrolls when jqm hasn't already set scroll from previous page
 *
 * @param activePage
 */
$.LANE.scrollPage = function(activePage){
    // wait for jqm to finish its scrolling. TODO: verify this is long enough on slower devices
    var delay = 160,
    hideSearchScroll = 100,
    hideHeaderScroll = 50;
    if(activePage.find(".results li").size() > 1 || activePage.find(".absInfo").size()){
        // search results and abstract/info pages
        setTimeout(function(){
            if(window.pageYOffset < hideSearchScroll){
                window.scrollTo(0, hideSearchScroll);
            }
        },delay);
    }
    else if(activePage.attr('id') !== '_home' && activePage.find(".resourceList").size()){
        // resource list pages
        setTimeout(function(){
            if(window.pageYOffset < hideHeaderScroll){
                window.scrollTo(0, hideHeaderScroll);
            }
        },delay);
    }
};

$(this).bind("pagechange", function() {
    var activePage = $(".ui-page-active");
    // change page title when abstract/more page is active
    if(activePage.find(".absInfo").size()){
        document.title = $(".absInfo").find('a').first().text();
    }
    // default scrolling for active page
    $.LANE.scrollPage(activePage);
});
