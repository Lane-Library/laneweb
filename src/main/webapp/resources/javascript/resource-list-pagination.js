(function() {
    var nodes = Y.all(".pagingButton"), i;
    if(nodes.size() > 0){
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).on('click', function(e) {
                e.preventDefault();
                this.toggleClass('pagingButtonActive');
                this.next('.pagingLabels').toggleClass('show');
            });
        }
    }
})();
