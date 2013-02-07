(function() {
    var nodes = Y.all(".resourceListPagination select"), i;
    if(nodes.size() > 0){
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).on('change', function() {
                if(this.get('selectedIndex') > 0){
                    window.location.href = this.get('value');
                }
            });
        }
        // ie doesn't resize select correctly
        if (Y.UA.ie) {
            for (i = 0; i < nodes.size(); i++) {
                nodes.item(i).on('focus', function() {
                    //nodes.item(i).on(['focus', 'mouseover'], function() {
                    this.setStyle('width','auto');
                    this.removeClass('clicked');
                });
                nodes.item(i).on('click', function() {
                    this.toggleClass('clicked');
                });
                nodes.item(i).on('mouseout', function() {
                    if(!this.hasClass('clicked')){
                        this.setStyle('width','');
                    }
                });
                nodes.item(i).on('blur', function() {
                    this.setStyle('width','');
                    this.removeClass('clicked');
                });
            }
        }
    }
})();
