(function() {




        var bassettContent = Y.one('#bassettContent'),
            model = Y.lane.Model,
            basePath = model.get(model.BASE_PATH) || "",
            diagramDisplay = false,
            accordion,
            history,

            formatAjaxUrl = function(href) {
                var url;
                href = href.replace("search.html", "/biomed-resources/bassett/bassettsView.html");
                href = href.substr(href.indexOf("/bassett/") + 8);
                href = href.split("?");
                if (href.length === 1) {
                    url = href[0];
                }
                if (href.length > 1) {
                    url = href[0] + "?" + href[1];
                }
                if (diagramDisplay) {
                    url = url + "&t=diagram";
                }
                return url;
            },

            loadContent = function(url) {
                url = basePath + "/plain/biomed-resources/bassett/raw".concat(url);
                function successHandler(id, o) {
                    var content = Y.Node.create(o.responseText),
                        container = Y.one('#bassettContent');
                    container.setContent(content);
                    registerLinksContainer(container);
                }
                Y.io(url, {on : {success : successHandler}});
            },

            handleClick = function(ev) {
                if (this.get('id') === "diagram-choice") {
                    diagramDisplay = true;
                }
                if (this.get('id') === "photo-choice") {
                    diagramDisplay = false;
                }
                url = formatAjaxUrl(this.get('href'));
                try {
                    history.addValue("bassett",url);
                } catch (e) {
                    loadContent(url);
                }
                ev.preventDefault();
            },

        initializeHistory = function() {
            history = new Y.HistoryHash();
            if(history.get('bassett')){
                loadContent(history.get('bassett'));
            }
            history.on("bassettChange",function(e) {
                loadContent(e.newVal);
            });
            history.on("bassettRemove",function() {
                loadContent(formatAjaxUrl(Y.lane.Location.get("href")));
            });
        },

        registerLinksContainer = function(container) {
            var anchor, i;
            if (container) {
                anchor = container.all('a');
                for (i = 0; i < anchor.size(); i++) {
                    if (anchor.item(i).get('rel') === null || anchor.item(i).get('rel') === "" ||  anchor.item(i).get('rel') === "propagation") {
                        anchor.item(i).on('click', handleClick);
                    }
                }
            }
        };

        if (bassettContent) {
            accordion = Y.one('#accordion');
            if (accordion) // not if largerView.html
            {
                registerLinksContainer(accordion);
                registerLinksContainer(Y.one('#bassettContent'));
                initializeHistory();
            }
        }
    })();
