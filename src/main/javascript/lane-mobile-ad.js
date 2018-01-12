//advertise existence of mobile interface to iPad, iPhone, iPod and Android clients
(function() {

    "use strict";

    var link,
        model = L.Model,
        basePath = model.get(model.BASE_PATH) || "",
        login = document.querySelector(".login"),
        template;
    if(login && navigator.userAgent.match(/(iPhone|iPod|Android)/)){
        link = basePath + '/index.html?site_preference=mobile';
        template = document.createElement('div');
        template.innerHTML = '<li><a href="'+link+'">Mobile Version</a></li>';
        login.insertBefore(template.firstChild, login.firstChild);
    }
})();
