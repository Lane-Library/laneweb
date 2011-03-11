(function() {
        // bookmark manager
        // load pages from location hashes
        // expected formats: 
        //  #_sr::laneSearch::term
        //  #_sr::pico::term
        //  #_srid::laneSearch::term::12345
        //  #_srid::pico::term::pubmed_guidelines_content_10
        //  #_htmlFilenameOrPageID
        var $ = iui.$, 
        d = document,
        bookmarkedHash,
        bookmarkId, // id used in both markup and URL hash
        callback,
        searchForm,
        searchFieldset,
        searchType,
        searchTerms,
        resultId,
        resultIdElm,
        pageElm;

        d.addEventListener("load", function(e) {
            // only on native load events and when loading is not in progress
            if(!e.toString().match(/UIEvent/) && !LANE.loadInProgress){
                // specific result ID (dangerous as IDs may change!)
                if(location.hash.match(/#_(srid::(.*)::(.*)::(.*))/)){
                    bookmarkId = decodeURIComponent(RegExp.$1).replace('+',' ');
                    searchType = RegExp.$2;
                    searchTerms = decodeURIComponent(RegExp.$3).replace('+',' ');
                    resultId = decodeURIComponent(RegExp.$4).replace('+',' ');
                    LANE.loadInProgress = true;
                    searchForm = $(searchType);
                    searchFieldset = searchForm.getElementsByTagName('fieldset')[0];
                    callback = function(){
                        searchFieldset.removeChild($('tmpRid'));
                        searchFieldset.removeChild($('tmpPage'));
                        if($(bookmarkId)){
                            $(bookmarkId).setAttribute('selected','true');
                        }
                        else {
                            alert('result not found ... try your search again');
                        }
                    };
                    e.preventDefault();
                    searchForm.elements['q'].value = searchTerms;
                    resultIdElm = document.createElement('input');
                    resultIdElm.type = 'hidden';
                    resultIdElm.id = 'tmpRid';
                    resultIdElm.name = 'rid';
                    resultIdElm.value = resultId;
                    pageElm = document.createElement('input');
                    pageElm.type = 'hidden';
                    pageElm.id = 'tmpPage';
                    pageElm.name = 'page';
                    pageElm.value = 'all';
                    searchFieldset.appendChild(resultIdElm);
                    searchFieldset.appendChild(pageElm);
                    setTimeout(function(){ 
                        LANE.submitForm(searchForm,callback);
                    }, 1000);
                }
                // search result pages
                else if(location.hash.match(/#_(sr::(.*)::(.*))/)){
                    bookmarkId = decodeURIComponent(RegExp.$1).replace('+',' ');
                    searchType = RegExp.$2;
                    searchTerms = decodeURIComponent(RegExp.$3).replace('+',' ');
                    LANE.loadInProgress = true;
                    searchForm = $(searchType);
                    callback = function(){
                        $(bookmarkId).setAttribute('selected','true');
                    };
                    e.preventDefault();
                    searchForm.elements['q'].value = searchTerms;
                    setTimeout(function(){ 
                        LANE.submitForm(searchForm,callback);
                    }, 1000);
                }
                // known static pages ... could probably open this up to any hash
                else if(location.hash.match(/#_(book|db|ej|fb|hours|top|bookAZ|dbAZ|ejAZ)$/)){
                    bookmarkedHash = RegExp.$1;
                    LANE.loadInProgress = true;
                    e.preventDefault();
                    setTimeout(function(){
                        iui.showPageByHref(bookmarkedHash+".html", 
                                null, 
                                "GET", 
                                null, 
                                function(){
                            $(bookmarkedHash).setAttribute('selected','true');
                        });
                    }, 1000);
                }
            }
            
        }, true);
})();