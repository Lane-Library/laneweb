YUI().use('yui2-event', function(Y){
    Y.YUI2.util.Event.onAvailable('selections',function() {
        var selections = [], showAll, hideAllBut, i, options;
        for (i = 0; i < this.childNodes.length; i++) {
            if (this.childNodes[i].nodeType == 1) {
                selections.push(this.childNodes[i]);
            }
        }
        showAll = function() {
            for (var i = 0; i < selections.length; i++) {
                selections[i].style.display = 'block';
            }
        };
        hideAllBut = function(selection) {
            for (var i = 0; i < selections.length; i++) {
                if (selections[i].id == selection) {
                    selections[i].style.display = 'block';
                } else {
                    selections[i].style.display = 'none';
                }       
            }
        };
        options = document.getElementById('selections-select').getElementsByTagName('option');
        for (i = 0; i < options.length; i++) {
            options[i].clicked = function() {
                if (this.value === '') {
                    showAll();
                } else {
                    hideAllBut(this.value);
                }
            };
        }
    });
});