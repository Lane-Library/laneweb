YUI().use("lane", 'node','io-base','event','history', function(Y) {

		var bassettContent = Y.one('#bassettContent'), diagramDisplay = false;

		
		var registerLinksContainer = function(container) {
			var anchor, i, url;
			if (container) {
				anchor = container.all('a');
				for (i = 0; i < anchor.size(); i++) {
					if (anchor.item(i).get('rel') === null || anchor.item(i).get('rel') === "" ||  anchor.item(i).get('rel') === "propagation") {
						anchor.item(i).on('click',function(ev) {
							if (this.get('id') == "diagram-choice") {
								diagramDisplay = true;
							}
							if (this.get('id') == "photo-choice") {
								diagramDisplay = false;
							}
							url = formatAjaxUrl(this.get('href'));
							if (!Y.History.navigate("bassett",url))
								loadContent(url);
							ev.preventDefault();
						});
					}
				}
			}
		};

		var loadContent = function(url) {
			url = "/././plain/bassett/raw".concat(url);
			function successHandler(id, o, args) {
				var contentNode, content, container, i;
                content = document.importNode(o.responseXML.getElementsByTagName("body")[0], true);
				content = new Y.Node(content).get('children');
				container = Y.one('#bassettContent');
				contentNode = container.get('children');
				for (i = 0; i < contentNode.size(); i++) {
					container.removeChild(contentNode.item(i));
				}
				for (i = 0; i < content.size(); i++) {
					container.append(content.item(i));
				}
				registerLinksContainer(container);
				Y.fire('lane:change');
			}
			Y.io(url, {on : {success : successHandler}});
		}

		var formatAjaxUrl = function(href) {
			var url;
			href = href.replace("search.html",
					"/bassett/bassettsView.html");
			href = href.substr(href.indexOf("/bassett/") + 8);
			href = href.split("?");
			if (href.length == 1) {
				url = href[0];
			}
			if (href.length > 1) {
				url = href[0] + "?" + href[1];
			}
			if (diagramDisplay) {
				url = url + "&t=diagram";
			}
			return url;
		};

		function initializeHistory() {
            if (Y.one("#yui-history-field-bassett") && Y.one("#yui-history-iframe")) {
                var currentState = Y.History.getBookmarkedState("bassett") ||
                formatAjaxUrl(window.location.toString());
                Y.History.register('bassett', currentState).on('history:moduleStateChange', loadContent);
                Y.History.on('history:ready', loadContent(currentState));
                Y.History.initialize("#yui-history-field-bassett", "#yui-history-iframe");
            }
		}

		if (bassettContent) {
			var accordion = Y.one('#accordion');
			if (accordion) // not if largerView.html
			{
				registerLinksContainer(accordion);
				registerLinksContainer(Y.one('#bassettContent'));
				initializeHistory();
			}
		}

	});
