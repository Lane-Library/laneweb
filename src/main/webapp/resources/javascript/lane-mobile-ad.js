//advertise existence of mobile interface to iPad, iPhone, iPod and Android clients
(function() {
    var advert, device, link,
        model = Y.lane.Model,
        basePath = model.get(model.BASE_PATH) || "";
    if(navigator.userAgent.match(/(iPhone|iPod|Android)/) && !location.search.match(/mad=0/)){
        device = RegExp.$1;
        link = basePath + '/index.html?site_preference=mobile';
        advert = Y.Node.create('<div id="mphoneAd"><div><div>Try <a href="'+link+'">Lane Mobile</a> for your '+device+' &raquo;</div></div></div>');
        Y.one("#custom-doc").insert(advert);
        advert.on("click",function(){
            window.location.href = link;
        });
    }
})();