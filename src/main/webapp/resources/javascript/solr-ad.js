// solr beta advert
(function() {
    var advert,
        login = Y.one("#login"),
        anim;
    if(login){
        advert = Y.Node.create('<li style="font-weight:bold;position:relative"><i class="fa fa-arrow-right" id="betaAdArrow" style="font-size:24px;position:absolute;left:-100px;top:-5px;opacity:0;color:#9a0003"></i> <span style="color:#9a0003">NEW! </span><a style="color:#007c92" href="http://lane-beta.stanford.edu/lanesearch.html">Try Lane Search beta <i class="fa fa-arrow-right" style="color:#007c92"></i></a></li>');
        login.prepend(advert);
        anim = new Y.Anim({
            node: "#betaAdArrow",
            to: {
                opacity: 1,
                left:-10},
            duration: 1
        });
        anim.run();
    }
})();