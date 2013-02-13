(function() {
    var nodes = Y.all(".plsContainer"), i;
    if(nodes.size() > 0){
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).one('li').on('click', function(e) {
                e.preventDefault();
                this.next().toggleClass('show');
            });
        }
    }
})();
