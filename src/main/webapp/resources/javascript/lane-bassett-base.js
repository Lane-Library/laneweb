(function() {
	var Y = LANE.Y,
	    bassettContent = Y.one('#bassettContent'),
	    accordion = Y.one('#accordion');
	function Bassett(config) {
		Bassett.superclass.constructor.apply(this, arguments);
	};
	Bassett.NAME = "bassett";
	Bassett.ATTRS = {
		content : {
			valueFn : function() {
				Y.one('#bassettContent');
			}
		},
		accordion : {
			valueFn : function() {
				Y.one('#accordion');
			}
		},
		diagramDisplay : {
			value : false
		},
		history : {
			valueFn : function() {
				return new Y.HistoryHash();
			}
		},
		io : {
			value : Y.io
		}
	};
	Y.extend(Bassett, Y.Base, {
		initializer : function(cfg) {
			this._initializeHistory();
			this._registerLinksContainer(this.get("content"));
			this._registerLinksContainer(this.get("accordion"));
		},
		_failureHandler : function(o) {
			alert(o);
		},
		_formatAjaxUrl : function(href) {
			var url;
			href = href.replace("search.html",
					"/biomed-resources/bassett/bassettsView.html");
			href = href.substr(href.indexOf("/bassett/") + 8);
			href = href.split("?");
			if (href.length == 1) {
				url = href[0];
			}
			if (href.length > 1) {
				url = href[0] + "?" + href[1];
			}
			if (this.get("diagramDisplay")) {
				url = url + "&t=diagram";
			}
			return url;
		},
		_initializeHistory : function() {
			var history = this.get("history"), bassettHistory = history
					.get("bassett");
			if (bassettHistory) {
				this._loadContent(bassettHistory);
			}
			history.on("bassettChange", function(e) {
				this._loadContent(e.newVal);
			});
			history.on("bassettRemove", function(e) {
				this._loadContent(this._formatAjaxUrl(window.location
						.toString()));
			});
		},
		_loadContent : function(url) {
			url = "/././plain/biomed-resources/bassett/raw".concat(url);
			this.get("io").call(this, url, {
				on : {
					success : this._successHandler,
					failure : this._failureHandler
					},
				arguments : {
					bassett : this
				}
			});

		},
		_registerLinksContainer : function(container) {
			var anchor, i;
			if (container) {
				anchor = container.all('a');
				for (i = 0; i < anchor.size(); i++) {
					if (anchor.item(i).get('rel') === null
							|| anchor.item(i).get('rel') === ""
							|| anchor.item(i).get('rel') === "propagation") {
						anchor.item(i).on('click', function(ev) {
							var url;
							ev.preventDefault();
							if (ev.target.get('id') == "diagram-choice") {
								this.set("diagramDisplay", true);
							}
							if (ev.target.get('id') == "photo-choice") {
								this.set("diagramDisplay", false);
							}
							url = this._formatAjaxUrl(ev.target.get('href'));
							try {
								this.get("history").addValue("bassett", url);
							} catch (e) {
								this._loadContent(url);
							}
						}, this);
					}
				}
			}
		},
		_successHandler : function(id, o, args) {
			var content = new Y.Node(o.responseText),
			    container = o.bassett.get("content");
			container.setContent(content);
			_registerLinksContainer(container);
			Y.fire('lane:change');
		}
	});

	if (bassettContent && accordion) {
		LANE.bassett = new Bassett({
			content : bassettContent,
			accordion : accordion
		});
	}
})();
