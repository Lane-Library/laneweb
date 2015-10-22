// solr beta advert
(function() {
    var advert,
        login = Y.one("#login");
    if(login){
        advert = Y.Node.create('<li style="font-weight:bold;margin-right:90px;"><span style="color:#9a0003">NEW! </span><a style="color:#007c92" href="http://lane-beta.stanford.edu/lanesearch.html">Try Lane Search beta <i class="fa fa-arrow-right" style="color:#007c92"></i></a></li>');
        login.prepend(advert);
    }
})();