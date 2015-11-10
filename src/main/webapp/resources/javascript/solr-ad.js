// solr beta advert
(function() {
    var advert,
        login = Y.one("#login"),
        anim
    if(login){
        advert = Y.Node.create('<li style="font-weight:bold;"><i class="fa fa-arrow-right" id="betaAddArrow" style="font-size:24px;position:relative;right:100px;top:3px;opacity:0;color:#9a0003"></i> <span style="color:#9a0003">NEW! </span><a style="color:#007c92" href="http://lane-beta.stanford.edu/lanesearch.html">Try Lane Search beta <i class="fa fa-arrow-right" style="color:#007c92"></i></a></li>');
        login.prepend(advert);
        anim = new Y.Anim({
            node: "#betaAddArrow",
            to: {
                opacity: 1,
                right:0},
            duration: 1
        });
        anim.run();
    }
})();