(function() {

    "use strict";

    var Model = Y.lane.Model,
        BASE_PATH = Model.get(Model.BASE_PATH),
        source = Model.get(Model.URL_ENCODED_SOURCE),
        query = Model.get(Model.URL_ENCODED_QUERY);

    if (Y.one("#tabs-image-search")) {

        //Check # page value before to submit form
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


        //On click on Image to open the imageDetail
        Y.all('#imageList li:not(.imageDetailHidden)').on('click',
            function(e)
            {
                var li = e.currentTarget, row = li
                        .getAttribute("row"), id = li.get("id");
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
        });

        //To close the image detail
        Y.on("click", function() {
            cleanDetailImageWindow();
        }, "#image-detail-close");


        //Admin on click on the id
        Y.on("click", function(e) {
            var href = e.target.get('href');
            Y.io(href, {
                on : {
                    success : confirmAdminAction
                    }
            });
            e.stopPropagation();
            e.preventDefault();
        }, ".imagedeco-admin");


    }


    function confirmAdminAction(id, o){
        var image = Y.JSON.parse(o.responseText),
        id = "#" .concat(image.id.split('.').join('\\.').split('/').join('\\/')),
        div = Y.one( id);
        if(image.enable){
            div.removeClass('admin-disable');
            div.addClass('admin-enable');

        }else{
            div.removeClass('admin-enable');
            div.addClass('admin-disable');
        }
    }

    function successHandler(id, o, args) {
        var image = Y.JSON.parse(o.responseText),
        row = args.row,
        li = args.li,
        imageDetail = Y.one("#imageDetail_" + row);
        cleanDetailImageWindow();

        imageDetail.one(".image").setAttribute("src", image.src);
        imageDetail.one("h3").setContent(image.shortTitle);
        if (undefined !== image.description) {
            imageDetail.one(".desc p").setContent(image.shortDescription);
        }

        if (undefined !== image.articleTitle) {
            imageDetail.one(".article-title").show();
            imageDetail.one(".article-title p").setContent(image.shortArticleTitle);
        }else{
            imageDetail.one(".article-title").hide();
        }

        imageDetail.one(".copyright p").setContent(image.shortCopyrightText);
        imageDetail.one(".to-image a").setAttribute("href", image.pageUrl);

        li.one("div").removeClass('imagedecoHidden');
        li.one("div").addClass('imagedeco');
        imageDetail.removeClass('imageDetailHidden');
        imageDetail.addClass('imageDetail');

        if(row > 1){
            li = Y.one('li[row = "'+(row-1)+'"]');
            window.location.hash = "#"+encodeURIComponent(li.get('id'));
        }
    }

    function cleanDetailImageWindow(){
        Y.all(".imageDetail").each(function(node) {
            node.removeClass('imageDetail');
            node.addClass('imageDetailHidden');
        });
        Y.all(".imagedeco").each(function(node) {
                node.removeClass('imagedeco');
                node.addClass('imagedecoHidden');
        });

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
