// advertise existence of mobile interface to iPad, iPhone, iPod and Android clients
(function() {
    var Y = LANE.Y, advert, id, device;
    if(navigator.userAgent.match(/(iPhone|iP.d|Android)/) && !location.search.match(/mad=0/)){
        device = RegExp.$1;
        id = (device == 'iPad') ? 'ipadAd' : 'mphoneAd';
        advert = Y.Node.create('<div id="'+id+'"><div><div>Try <a href="/././m/">Express Lane</a> for your '+device+' &raquo;</div></div></div>');
        Y.one("#custom-doc").insert(advert);
        advert.on("click",function(){
            window.location.href = "/././m/";
        });
    }
})();
