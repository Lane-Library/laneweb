YUI().use('node', function(Y) {
    var selections = Y.all('#selections > li'),
        options = Y.all('#selections-select option'),
        i,
        showAll = function() {
            for (i = 0; i < selections.size(); i++) {
                selections.item(i).setStyle('display', 'block');
            }
        },
        hideAllBut = function(selection) {
            for (i = 0; i < selections.size(); i++) {
                if (selections.item(i).get('id') == selection) {
                    selections.item(i).setStyle('display', 'block');
                } else {
                    selections.item(i).setStyle('display', 'none');
                }
            }
        };
    for (i = 0; i < options.size(); i++) {
        Y.on('click', function() {
            if (this.get('value') === '') {
                showAll();
            } else {
                hideAllBut(this.get('value'));
            }
        }, options.item(i));
    }
});
