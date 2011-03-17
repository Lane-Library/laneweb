(function() {
    var spellCheck = Y.one('#spellCheck'),
        searchTerms = Y.lane.SearchResult.getEncodedSearchTerms(), trackingImg;
    if (spellCheck && searchTerms) {
        //get the suggestion
        Y.io('/././apps/spellcheck/json?q=' + searchTerms, {
            on: {
                success:function(id, o) {
                    var sc = Y.JSON.parse(o.responseText), a;
                    if (sc.suggestion) {
                        //if there is a suggestion show the spellcheck markup 
                        //and add the suggestion to the href
                        //and add laneSpellCorrected=<old term> for tracking
                        a = spellCheck.one('a');
                        a.set('href', document.location.href.replace('q=' + searchTerms, 'q=' + encodeURIComponent(sc.suggestion) + '&laneSpellCorrected=' + searchTerms));
                        a.set('innerHTML', sc.suggestion);
                        Y.Global.fire('lane:popin', spellCheck);
                        // track spelling suggestion and original term
                        trackingImg = document.createElement('img');
                        trackingImg.style.display = "none";
                        trackingImg.src = "/././resources/images/spacer.gif?log=laneSpellSuggest&q=" + searchTerms + "&s=" + sc.suggestion;
                        spellCheck.append(trackingImg);
                    }
                }
            }
        });
    }
})();

