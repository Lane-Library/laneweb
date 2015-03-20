(function() {
    var  Model = Y.lane.Model,
        encodedQuery = Model.get(Model.URL_ENCODED_QUERY),
        basePath = Model.get(Model.BASE_PATH) || "",
        source =  Model.get(Model.SOURCE),
        query = Model.get(Model.QUERY);

    if (Y.one("#tabs-image-search")) {
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
