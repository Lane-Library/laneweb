(function() {
	var Model = Y.lane.Model,
	    encodedQuery = Model.get(Model.URL_ENCODED_QUERY),
	    basePath = Model.get(Model.BASE_PATH) || "",

	makeRequest = function() {
		Y.io(basePath + '/facet/images/copyright?query=' + encodedQuery+ '&rd=' + Math.random(), {
			on : {
				success : function(id, o) {
					var messages = Y.JSON.parse(o.responseText);
					for (var i = 0; i < messages.length; ++i) {
						Y.one("#copyright" + messages[i].value).set("innerHTML","(" + messages[i].valueCount + ")");
					}
				}
			}
		});
	};

	if (Y.one("#tabs-image-search")) {
		makeRequest();
		if (Y.all('form[name=paginationForm]')) {
			Y.all('form[name=paginationForm]').on('submit',
			function(e) {
				var totalPages = Number(e.target.get('totalPages').get('value')), 
				page = Number( e.target.get('page').get('value'));
				if (page < 1 || page > totalPages ) {
					alert("Page out of range");
					e.preventDefault();
				}
				else{
					e.target.get('totalPages').remove();
				}
			});
		}
	}

})();
