(function() {

	"use strict";

	var Model = L.Model,
		BASE_PATH = Model.get(Model.BASE_PATH);

	if (document.querySelector("#tabs-image-search")) {

		//Check # page value before to submit form
		document.querySelectorAll('form[name=paginationForm]').forEach(function(node) {
			node.addEventListener('submit', function(e) {
				var totalPages = Number(e.target.totalPages.value),
					page = Number(e.target.page.value);
				if (page < 1 || page > totalPages) {
					L.showMessage("Page out of range");
					e.preventDefault();
				} else {
					e.target.totalPages.remove();
				}
			});
		});

		// To hide the images if there is an error 
		document.querySelectorAll('#imageList img').forEach(function(img) {
			img.onerror = function() {
				var level = 3, el = this;
				while ((--level >= 0) && el) {
					el = el.parentNode;
				}
				el.style.display = 'none'
			};
		})

		//On click on Image to open the imageDetail
		document.querySelectorAll('#imageList  div[imgIndex]').forEach(function(node) {
			node.addEventListener('click', function(e) {
				var div = e.currentTarget,
					id = div.id;

				L.io(BASE_PATH + "/image?id=" + id, {
					on: {
						success: successHandler
					},
					"arguments": {
						div: div
					}
				});
				e.stopPropagation();
				e.preventDefault();
			});
		});

		//To close the image detail
		document.querySelectorAll("#image-detail-close").forEach(function(node) {
			node.addEventListener("click", function(e){
                cleanDetailImageWindow();
                e.stopPropagation();
                e.preventDefault();
            });
		});



		//Admin on click on the id
		document.querySelectorAll(".imagedeco-admin").forEach(function(node) {
			node.addEventListener("click", function(e) {
				var href = e.target.href;
				L.io(href, {
					on: {
						success: confirmAdminAction
					}
				});
				e.stopPropagation();
				e.preventDefault();
			});
		});
	}

	function confirmAdminAction(unused, o) {
		var image = JSON.parse(o.responseText),
			id = "#".concat(image.id.split('.').join('\\.').split('/').join('\\/')),
			div = document.querySelector(id);
		if (image.enable) {
			div.classList.remove('admin-disable');
			div.classList.add('admin-enable');

		} else {
			div.classList.remove('admin-enable');
			div.classList.add('admin-disable');
		}
	}

	function successHandler(id, o, args) {
		var image = JSON.parse(o.responseText),
			div = args.div,
			imageDetail = document.querySelector("#imageDetail");
		cleanDetailImageWindow();
        document.querySelector('.menu-overlay').style.display = 'block';
		imageDetail.querySelector(".image").setAttribute("src", image.src);
		imageDetail.querySelector("h3").innerHTML = image.shortTitle + "&nbsp;";
		if (null != image.description && '' != image.description ) {
			imageDetail.querySelector(".desc p").innerHTML = image.shortDescription;
		}
      	if (null != image.articleTitle && '' != image.articleTitle) {
			imageDetail.querySelector(".article-title").removeAttribute("hidden");
			imageDetail.querySelector(".article-title").style.display = "";
			imageDetail.querySelector(".article-title p").innerHTML = image.shortArticleTitle;
		} else {
			imageDetail.querySelector(".article-title").hidden = "hidden";
			imageDetail.querySelector(".article-title").style.display = "none";
		}

		imageDetail.querySelector(".copyright p").innerHTML = image.shortCopyrightText;
		imageDetail.querySelector(".to-image a").href = image.pageUrl;

		div.querySelector("#imagedecorator").classList.remove('imagedecoHidden');
		div.querySelector("#imagedecorator").classList.add('imagedeco');
		imageDetail.classList.remove('imageDetailHidden');
		imageDetail.classList.add('imageDetail');

		div.appendChild(imageDetail);
		window.location.hash = "#" + encodeURIComponent(div.id);

	}

	function cleanDetailImageWindow() {
		document.querySelectorAll(".imageDetail").forEach(function(node) {
			node.classList.remove('imageDetail');
			node.classList.add('imageDetailHidden');
		});
		document.querySelectorAll(".imagedeco").forEach(function(node) {
			node.classList.remove('imagedeco');
			node.classList.add('imagedecoHidden');
		});
        document.querySelector('.menu-overlay').style.display = 'none';  
      
	}

})();
