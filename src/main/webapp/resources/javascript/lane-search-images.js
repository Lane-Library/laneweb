(function() {

    "use strict";

    var Model = Y.lane.Model,
        BASE_PATH = Model.get(Model.BASE_PATH);

    if (Y.one("#tabs-image-search")) {

        //Check # page value before to submit form
        Y.all('form[name=paginationForm]').on(
                'submit',
                function(e) {
                    var totalPages = Number(e.target.get('totalPages').get('value')),
                        page = Number(e.target.get('page').get('value'));
                    if (page < 1 || page > totalPages) {
                        alert("Page out of range");
                        e.preventDefault();
                    } else {
                        e.target.get('totalPages').remove();
                    }
                });

        //On click on Image to open the imageDetail
        Y.all('#imageList  div[class=yui3-u-1-5]').on('click',
            function(e) {
                var div = e.currentTarget,
                    row = div.getAttribute("row"),
                    id = div.get("id");

                Y.io(BASE_PATH + "/image?id=" + id, {
                    on : {
                        success : successHandler
                    },
                    "arguments" : {
                        row : row,
                        div : div
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

    function confirmAdminAction(unused, o){
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
        div = args.div,
        imageDetail = Y.one("#imageDetail_" + row);
        cleanDetailImageWindow();

        imageDetail.one(".image").setAttribute("src", image.src);
        imageDetail.one("h3").setContent(image.shortTitle + "&nbsp;");
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

        div.one("#imagedecorator").removeClass('imagedecoHidden');
        div.one("#imagedecorator").addClass('imagedeco');
        imageDetail.removeClass('imageDetailHidden');
        imageDetail.addClass('imageDetail');
        if(row > 1){
            div = Y.one('div[row = "'+(row-1)+'"]');
            window.location.hash = "#"+encodeURIComponent(div.get('id'));
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

})();
