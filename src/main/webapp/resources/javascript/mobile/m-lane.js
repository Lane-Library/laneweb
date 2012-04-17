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
 * Initialize and return an object to be used by each of the autocomplete instances attached to search boxes.
 * The function expects an input element
 * 
 * @param input
 * @returns {AutocompleteObj}
 */
$.LANE.createAutocompleteObject = function(input) {
    var requestIndex = 0;
    return {
        source : function(request, response) {
            if ( self.xhr ) {
                self.xhr.abort();
            }
            self.xhr = $.ajax({
                url: "/././apps/suggest/json",
                dataType: "json",
                data : {
                  l : $.LANE.getACLimit(input),
                  q : request.term
                },
                autocompleteRequest: ++requestIndex,
                success: function( data, status ) {
                    if ( this.autocompleteRequest === requestIndex ) {
                        response($.map(data.suggest, function(item) {
                            return {
                                label : item,
                                value : item
                            };
                        }));
                    }
                },
                error: function() {
                    if ( this.autocompleteRequest === requestIndex ) {
                        response( [] );
                    }
                }
            });
        },
        delay : 150,
        minLength : 3
    };
};

/**
 * return appropriate data limit for this input element
 * 
 * @param input
 * @returns {String}
 */
$.LANE.getACLimit = function(input){
    var id = input.attr('id').toLowerCase();
    if(id.match(/condition/)){
        return "mesh-d";
    }
    else if(id.match(/intervention/)){
        return "mesh-i";
    }
    else if(id.match(/comparison/)||id.match(/(clinical|ped)/)){
        return "mesh-di";
    }
    else if(id.match(/book/)){
        return "book";
    }
    else if(id.match(/journal/)){
        return "ej";
    }
    else{
        return "er-mesh";
    }
};
    
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
                        if(hours.attr("class") == "expanded") {
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
        if (qString.indexOf('(') === 0 && qString.indexOf(')') == qString.length - 1) {
            qString = qString.replace(/(\(|\))/g, '');
        }
    }
    return qString;
};

/**
 * scroll pages to hide header and/or search form
 * only scrolls when jqm hasn't already set scroll from previous page  
 * 
 * @param activePage
 */
$.LANE.scrollPage = function(activePage){
    var delay = 50, // wait for jqm to finish its scrolling. TODO: verify this is long enough on slower devices
    hideSearchScroll = 96,
    hideHeaderScroll = 46;
    if(activePage.find(".results").size() || activePage.find(".absInfo").size()){ // search results and abstract/info pages
        setTimeout(function(){
            if(window.pageYOffset < $.mobile.minScrollBack){
                window.scrollTo(0, hideSearchScroll);
            }
        },delay);
    }
    else if(activePage.attr('id') != '_home' && activePage.find(".resourceList").size()){ // resource list pages
        setTimeout(function(){
            if(window.pageYOffset < $.mobile.minScrollBack){
                window.scrollTo(0, hideHeaderScroll);
            }
        },delay);
    }
};

/**
 * verify that "qSearch" data present before submitting form
 * 
 * @param event
 * @returns {Boolean}
 */
$.LANE.validateForm = function(event){
    var valid = false, qInput = $(event.target).find("input[name=qSearch]");
    if($(event.target).hasClass('noValidation')){
        valid = true;
    };
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
        alert('nothing to search for');
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


//When the homepage is loaded, attach event listeners for search tabs and search boxes, and load content for Hours of operation through Ajax.
$("#_home").live("pageinit", function() {
    // fetch hours content
    $.LANE.getHours();

    // Attach click event listener to the search tabs.
    // User can select Lane, Clinical, Pediatric search by clicking/tapping on the appropriate icon.
    $("#searchTabs li").click(function() {
        $("#_home .search form").removeClass("selected");
        $($("#_home .search form").get($(this).index())).addClass("selected");
        if($(this).index() > 0 && $(this).index() < 3) {
            if(!$("#overlayMask").length) {
                var maskHeight = $('.ui-mobile .ui-page-active').height();
                $("#_home").append("<div id='overlayMask'></div>");
                $("#overlayMask").css("height", maskHeight + 100 + "px");
                $("#overlayMask").click(function() {
                    $($("#searchTabs li").get(0)).trigger("vclick");
                    $($("#searchTabs li").get(0)).trigger("click");
                });
            }
        }
        else if($("#overlayMask").length) {
            $("#overlayMask").remove();
        }
        window.scrollTo(0, 46);
    });
    
    // attach vclick listener (doesn't have 700 ms delay)
    // set LI background to red to avoid flicker
    $("#searchTabs li").bind('vclick',function(e) {
        e.stopPropagation();
        $("#searchTabs li").removeClass("selected");
        $(this).addClass("selected");
        // shameless agent detection ... remove if find solution for vclick on android (below)
        if(navigator.userAgent.match(/(iPhone|iP.d)/)){ 
            e.preventDefault();
            $(this).trigger("click");
        }
    });
    
    // ideally, only use vclick handler, but can't get android to NOT set input focus
    /* 
    $("#searchTabs li").bind('vclick',function(e) {
        e.preventDefault();
        $("li").bind('click', function(e){
            e.preventDefault();
        });
        $("#searchTabs li").removeClass("selected");
        $(this).addClass("selected");
        $("#_home .search form").removeClass("selected");
        $($("#_home .search form").get($(this).index())).addClass("selected");
        if($(this).index() > 0 && $(this).index() < 3) {
            if(!$("#overlayMask").length) {
                var maskHeight = $('.ui-mobile .ui-page-active').height();
                $("#_home").append("<div id='overlayMask'></div>");
                $("#overlayMask").css("height", maskHeight + 100 + "px");
                $("#overlayMask").bind('click',function(e) {
                    e.preventDefault();
                    $($("#searchTabs li").get(0)).trigger("vclick");
                });
            }
        }
        else if($("#overlayMask").length) {
            $("#overlayMask").remove();
        }
        window.scrollTo(0, 46);
    });
    */
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

$(this).bind("pagechange", function() {
    var activePage = $(".ui-page-active");
    // change page title when abstract/more page is active
    if(activePage.find(".absInfo").size()){
        document.title = $(".absInfo").find('a').first().text();
    }
    // default scrolling for active page
    $.LANE.scrollPage(activePage);
});

$(this).bind("pageinit", function() {
    // Activate autocomplete on every input
    $(":input[data-type=search]").each(function(){
        if(!$(this).hasClass('ui-autocomplete-input')){
            $(this).attr('autocorrect','off'); // TODO: shouldn't be necessary?
            $(this).autocomplete($.LANE.createAutocompleteObject($(this)));
        }
    });
    // bind form validation, only adding submit listener if needed
    $("form").each(function(){
        var handler, needsListener = true, i;
        if($(this).data('events') && $(this).data('events').submit){
            for(i = 0; i < $(this).data('events').submit.length; i++){
                handler = $(this).data('events').submit[i].handler;
                if(handler == $.LANE.validateForm||handler == $.LANE.validatePicoForm){
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
});

// submit form on autocomplete select if input is "qSearch"
$("form").live("autocompleteselect", function(e, ui) {
    if(ui.item && $(e.target).attr('name') == 'qSearch'){
        $(e.target).val(ui.item.value);
        $.mobile.showPageLoadingMsg();
        //$(this)[0].submit();
        $(this).trigger('submit');
    }
});

$("form").live("focus", function() {
    $(this).find(":input[data-type=search]").each(function(){
        $(this).bind("focus", function() {
            // scroll down to the top of this field
            window.scrollTo(0, $(this).offset().top - 6);
            // create or enable autocomplete on input
            if($(this).hasClass('ui-autocomplete-input')){
                $(this).autocomplete('enable');
            }
        });
    });    
});