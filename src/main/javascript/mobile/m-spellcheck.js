$(this).bind("pagechange", function() {
    var activePage = $(".ui-page-active"),
    resultsHeader = activePage.find('.results li:first-child'),
    qInput = activePage.find('input[name=qSearch]'),
    form = activePage.find('form');
    if(resultsHeader && qInput.val()){
        $.ajax({
            url: window.model['base-path'] + "/apps/spellcheck/json",
            dataType: "json",
            data : {q : qInput.val()},
            success: function(data) {
                if(data.suggestion) {
                    resultsHeader.prepend('<div class="spell">Did you mean: <a id="ss" title="Did you mean: ' + data.suggestion + '" href="#">'+data.suggestion+'</a></div>');
                    $(document).trigger('spellSuggestion', [ form, qInput.val(), data.suggestion ]);
                    $('#ss').click(function(){
                        $(this).trigger('spellSuggestionClick', [ form, qInput.val(), data.suggestion ]);
                        qInput.val(data.suggestion);
                        form.trigger('submit');
                    });
                }
            }
        });
    }
});
