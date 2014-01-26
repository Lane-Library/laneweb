/**
 *
 * Load contents of the Hours of Operation section from HTML document.
 */
$.LANE.getHours = function(){
    var hours = $("#hours"), rows;
    if(hours){
        $.ajax({
            url: "plain/hours.html?site_preference=mobile",
            dataType: "html",
            success: function(data) {
                if(data) {
                    hours.html(data);
                    rows = hours.find("dt").size()/hours.find("dl").size();
                    hours.find("h4").click(function() {
                        hours.toggleClass("expanded");
                        if(hours.attr("class") === "expanded") {
                            hours.css("height", "auto");
                            window.scrollTo(0, $(this).offset().top);
                            hours.find("dl").animate({
                                height: rows * 16, // calculate height based on number of DTs present
                                opacity: 1
                                }, 300, 'linear');
                        }
                        else {
                            hours.find("dl").animate({
                                height: '0',
                                opacity: 0
                                }, 300, 'linear');
                        }
                    });
                }
            }
        });
    }
};

//When the homepage is loaded load content for Hours of operation through Ajax.
$("#_home").live("pageinit", function() {
    // fetch hours content
    $.LANE.getHours();
});