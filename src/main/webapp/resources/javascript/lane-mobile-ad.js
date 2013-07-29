//advertise existence of mobile interface to iPad, iPhone, iPod and Android clients
(function() {
    var advert, link,
        model = Y.lane.Model,
        basePath = model.get(model.BASE_PATH) || "";
    if(navigator.userAgent.match(/(iPhone|iPod|Android)/)){
        link = basePath + '/index.html?site_preference=mobile';
        advert = Y.Node.create('<li><a href="'+link+'">Mobile Version</a></li>');
        Y.one("#login").prepend(advert);
    }
})();