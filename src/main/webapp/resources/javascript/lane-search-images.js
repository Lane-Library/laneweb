(function() {
	var  Model = Y.lane.Model,
	    encodedQuery = Model.get(Model.URL_ENCODED_QUERY),
	    basePath = Model.get(Model.BASE_PATH) || "",
	    source =  Model.get(Model.SOURCE),
	    query = Model.get(Model.QUERY), 
	    
	makeRequest = function() {
		Y.io(basePath + '/facet/images/copyright?query=' + encodedQuery+ '&rd=' + Math.random(), {
			on : {
				success : function(id, o) {
					var messages = Y.JSON.parse(o.responseText);
					for (var i = 0; i < messages.length; ++i) {
						if(messages[i].valueCount > 1){
							Y.one("#copyright" + messages[i].value).set("innerHTML", messages[i].valueCount + " images with");
						}else{
							Y.one("#copyright" + messages[i].value).set("innerHTML", messages[i].valueCount + " image with");
						}
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

	
	if (Y.one("#sourceFilter")){
		Y.on("change", 
				function(e){
					var selectedValue = Y.one("#sourceFilter select option:checked").get("value"),
					url = "/search.html?q="+query+"&source="+source;
					if(selectedValue !== ""){
						url = url + "&rid="+selectedValue;
					}
					document.location.href = url;
			}
		,"#sourceFilter select");
	}
	
})();
