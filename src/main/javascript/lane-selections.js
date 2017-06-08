(function() {

    "use strict";

    var selections = Y.all('#selections > li'),
        select = Y.one('#selections-select'),
        i,
        showAll = function() {
            for (i = 0; i < selections.size(); i++) {
                selections.item(i).setStyle('display', 'block');
            }
        },
        hideAllBut = function(selection) {
            for (i = 0; i < selections.size(); i++) {
                if (selections.item(i).get('id') === selection) {
                    selections.item(i).setStyle('display', 'block');
                } else {
                    selections.item(i).setStyle('display', 'none');
                }
            }
        };
    Y.on('change', function() {
        if (this.get('value') === '') {
            showAll();
        } else {
            hideAllBut(this.get('value'));
        }
    }, select );
})();
