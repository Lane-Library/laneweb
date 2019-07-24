/**
 * append "q" querystring from PICO form elements
 *
 * @param form
 * @returns {String}
 */
$.LANE.getPicoQuery = function(form){
    var qString = "";
    form.find(":input[data-type=search]").each(function(){
        if ($(this).attr('name').match(/^(p|i|c|o)(Search)?$/) && $(this).val()) {
            qString += '(' + $(this).val() + ')';
        }
    });
    if (qString.length) {
        qString = qString.replace(/\)\(/g, ") AND (");
        if (qString.indexOf('(') === 0 && qString.indexOf(')') === qString.length - 1) {
            qString = qString.replace(/(\(|\))/g, '');
        }
    }
    return qString;
};

/**
 * verify that "qSearch" data present before submitting form
 *
 * @param event
 * @returns {Boolean}
 */
$.LANE.validateForm = function(event){
    var valid = false;
    if($(event.target).hasClass('noValidation')){
        valid = true;
    }
    $(event.target).find(":input[data-type=search]").each(function(){
        // hide keyboard by blurring all inputs
        $(this).blur();
        if ($(this).val()){
            valid = true;
        }
        // disable autocomplete widget
        if ($(this).autocomplete){
            $(this).autocomplete('disable');
        }
    });
    if(!valid){
        event.stopPropagation();
        event.preventDefault();
    }
    return valid;
};

/**
 *
 * @param event
 * @returns
 */
$.LANE.validatePicoForm = function(event){
    var inputQ = $(event.target).find("input[name=q]");
    inputQ.val($.LANE.getPicoQuery($(this)));
    return $.LANE.validateForm(event);
};


//When the homepage is loaded, attach event listeners for search tabs and search boxes
$("#_home").on("pageinit", function() {
    // shameless agent detection ... can't get android to NOT set input focus on vclick
    var eHandler = (navigator.userAgent.match(/(iPhone|iP.d)/)) ? 'vclick' : 'click';
    // Attach click event listener to the search tabs.
    // User can select Lane, Clinical, Pediatric search by clicking/tapping on the appropriate icon.
    $(".searchTabs li").bind(eHandler,function(e) {
        e.preventDefault();
        $(this).bind('click', function(clickEvent){
            clickEvent.preventDefault();
        });
        $(".searchTabs li").removeClass("selected");
        $(this).addClass("selected");
        $("#_home .search form").removeClass("selected");
        $($("#_home .search form").get($(this).index())).addClass("selected");
        if($(this).index() > 0 && $(this).index() < 3) {
            if(!$(".overlayMask").length) {
                var maskHeight = $('.ui-mobile .ui-page-active').height();
                $("#_home").append("<div class='overlayMask'></div>");
                $(".overlayMask").css("height", maskHeight + 100 + "px");
                $(".overlayMask").bind('click',function(clickEvent) {
                    clickEvent.preventDefault();
                    $($(".searchTabs li").get(0)).trigger(eHandler);
                });
            }
        }
        else if($(".overlayMask").length) {
            $(".overlayMask").remove();
        }
        window.scrollTo(0, 46);
    });
});

$(this).bind("pagebeforechange", function(e,obj) {
    if(obj && obj.options && obj.options.data){
        //jquery mobile chokes on parentheses in input values when setting url hash
        //listen to pagebeforechange events and escape parentheses before submitting
        // https://github.com/jquery/jquery-mobile/issues/13830
        // TODO: remove this once above issue fixed in jqm
        if(obj.options.data.match(/'|(\(|\))/)){
            obj.options.data = obj.options.data.replace(/'/g,'%27');
            obj.options.data = obj.options.data.replace(/\(/g,'%28');
            obj.options.data = obj.options.data.replace(/\)/g,'%29');
        }
        // jqm converts all inputs of type search to type text, replacing "Search" with "Go" button on iOS keyboards
        // in addition to being triggered by input type search, iOS will toggle "Search" on keyboard when input name has 'search' in it
        // here we rename "qSearch" param to "q" before sending to server
        if(obj.options.data.match(/qSearch=/)){
            obj.options.data = obj.options.data.replace(/qSearch=/,'q=');
        }
    }
});

// bind form validation, only adding submit listener if needed
$(this).bind("pageinit", function() {
    $("form").each(function(){
        var handler, needsListener = true, i;
        if($(this).data('events') && $(this).data('events').submit){
            for(i = 0; i < $(this).data('events').submit.length; i++){
                handler = $(this).data('events').submit[i].handler;
                if(handler === $.LANE.validateForm||handler === $.LANE.validatePicoForm){
                    needsListener = false;
                }
            }
        }
        if(needsListener){
            if($(this).hasClass('picoSearch')){
                $(this).submit($.LANE.validatePicoForm);
            }
            else{
                $(this).submit($.LANE.validateForm);
            }
        }
    });
    $(":input[data-type=search]").each(function(){
        $(this).bind("focus", function() {
            // scroll down to the top of this field
            window.scrollTo(0, $(this).offset().top - 6);
        });
    });
});
