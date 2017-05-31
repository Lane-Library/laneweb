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
                url: window.model['base-path'] + "/apps/suggest/json",
                dataType: "json",
                data : {
                  l : $.LANE.getACLimit(input),
                  q : request.term
                },
                autocompleteRequest: ++requestIndex,
                success: function( data ) {
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
        minLength : 3,
        position:{
            my:"right top",
            at:"left bottom",
            collision:"fit none"
        }
    };
};

/**
 * return appropriate data limit for this input element
 *
 * @param input
 * @returns {String}
 */
$.LANE.getACLimit = function(input){
    var i, limit,
        id = input.attr('id').toLowerCase(),
        limits = [
               {regex : /condition/, limit : "mesh-d"},
               {regex : /intervention/, limit : "mesh-i"},
               {regex : /comparison/, limit : "mesh-di"},
               {regex : /(clinical|ped)/, limit : "mesh-di"},
               {regex : /book/, limit : "Book"},
               {regex : /journal/, limit : "Journal"}
               ];
    for (i = 0; i < limits.length; i++) {
        if (id.match(limits[i].regex)) {
            limit = limits[i].limit;
            break;
        }
    }
    return limit || "er-mesh";
};

// Activate autocomplete on every input
$(this).bind("pageinit", function() {
    $(":input[data-type=search]").each(function(){
        if(!$(this).hasClass('ui-autocomplete-input')){
            // setting autocorrect off shouldn't be necessary?
            $(this).attr('autocorrect','off');
            $(this).autocomplete($.LANE.createAutocompleteObject($(this)));
        }
        $(this).bind("focus", function() {
            // create or enable autocomplete on input
            if($(this).hasClass('ui-autocomplete-input')){
                $(this).autocomplete('enable');
            }
        });
    });
});
// submit form on autocomplete select if input is "qSearch"
$("form").on("autocompleteselect", function(e, ui) {
    if(ui.item && $(e.target).attr('name') === 'qSearch'){
        $(e.target).val(ui.item.value);
        $.mobile.loading('show');
        $(this).trigger('submit');
    }
});

