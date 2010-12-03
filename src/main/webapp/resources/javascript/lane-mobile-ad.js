// advertise existence of mobile interface to iPad, iPhone, iPod and Android clients
(function() {
    var Y = LANE.Y, advert, device;
    if(navigator.userAgent.match(/(iPhone|iP.d|Android)/)){
        device = RegExp.$1;
        advert = new Y.Overlay({
            id : (device == 'iPad') ? 'ipadAd' : 'mphoneAd',
            width : "988px",
            height : (device == 'iPad') ? '45px' : '112px',
            bodyContent : "Try <a href='/././m/'>Express Lane</a> for your "+device+" &raquo;",
            xy:[6,0],
            zIndex : 2
        });
        advert.render("#custom-doc");
        advert.on("click",function(){
            window.location.href = "/././m/";
        });
    }
})();
