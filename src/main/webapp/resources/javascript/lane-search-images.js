(function() {
	var Model = Y.lane.Model, BASE_PATH = Model.get(Model.BASE_PATH), source = Model
			.get(Model.SOURCE), query = Model.get(Model.QUERY);

	if (Y.one("#tabs-image-search")) {
		Y.all('form[name=paginationForm]').on(
				'submit',
				function(e) {
					var totalPages = Number(e.target.get('totalPages').get(
							'value')), page = Number(e.target.get('page').get(
							'value'));
					if (page < 1 || page > totalPages) {
						alert("Page out of range");
						e.preventDefault();
					} else {
						e.target.get('totalPages').remove();
					}
				});

		Y.all('#imageList li:not(.imageDetail)').on(
				'click',
				function(e) {
					var li = e.currentTarget, anchor = e.target, row = li
							.getAttribute("row"), id = li.get("id");
					var href = anchor.get('href');
					if (href == undefined) {
						Y.io(BASE_PATH + "/image?id=" + id, {
							on : {
								success : successHandler
							},
							"arguments" : {
								row : row,
								li : li
							}
						});
						e.stopPropagation();
						e.preventDefault();
					}
				});

		Y.on("click", function() {
			Y.all(".imageDetail").each(function(node) {
				node.hide();
			});
		}, ".image-detail-close");

	}

	function successHandler(id, o, args) {
		var image = Y.JSON.parse(o.responseText), 
		row = args.row,  
		li = args.li, 
		copyright = image.copyrightValue, 
		imageId = image.id.split("/"), imageDetail = Y.one("#imageDetail_" + row);
		Y.all(".imageDetail").each(function(node) {
			node.hide();
		});
		Y.all("div.imagedeco").each(function(node) {
			node.hide();
		});

		imageDetail.one(".image").setAttribute("src", image.src);
		imageDetail.one("h3").setContent(image.shortTitle);
		if (undefined != image.description) {
			imageDetail.one(".desc p").setContent(image.shortDescription);
		}
		
		if (undefined != image.articleTitle) {
			imageDetail.one(".article-title").show();
			imageDetail.one(".article-title p").setContent(image.shortArticleTitle);
		}else{
			imageDetail.one(".article-title").hide();
		}

		imageDetail.one(".copyright p").setContent(image.shortCopyrightText);
		imageDetail.one(".to-image a").setAttribute("href", image.pageUrl);
		li.one("div.imagedeco").show();
		if(row > 1){
			li = Y.one('li[row = "'+(row-1)+'"]');
			window.location.hash = "#"+encodeURIComponent(li.get('id'));
		}	
		imageDetail.show();
	}

	if (Y.one("#sourceFilter")) {
		Y.on("change", function() {
			var selectedValue = Y.one("#sourceFilter select option:checked")
					.get("value"), url = "/search.html?q=" + query + "&source="
					+ source;
			if (selectedValue !== "") {
				url = url + "&rid=" + selectedValue;
			}
			document.location.href = url;
		}, "#sourceFilter select");
	}

})();
