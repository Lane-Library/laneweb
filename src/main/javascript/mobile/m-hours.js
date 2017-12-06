/**
 *
 * Load contents of the Hours of Operation section from HTML document.
 */
$.LANE.getHours = function(){
    var hours = $(".hours");
    if(hours.length > 0){
        $.ajax({
            url: "plain/hours.html?site_preference=mobile",
            dataType: "html",
            success: function(data) {
                if(data) {
                    hours.html(data);
                    hours.find("h4").click(function() {
                        hours.toggleClass("expanded");
                        if(hours.hasClass("expanded")) {
                            hours.css("height", "auto");
                            window.scrollTo(0, $(this).offset().top);
                            hours.find(".hours-container").animate({
                                height: "100%",
                                opacity: 1
                                }, 300, 'linear');
                        }
                        else {
                            hours.find(".hours-container").animate({
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
$("#_home").on("pageinit", function() {
    // fetch hours content
    $.LANE.getHours();
});
