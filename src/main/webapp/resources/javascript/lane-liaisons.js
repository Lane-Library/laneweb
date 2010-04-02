(function(){
    YAHOO.util.Event.onAvailable('liaisons',function() {
        var liaisons = [], showAll, hideAllBut, i, options;
        for (i = 0; i < this.childNodes.length; i++) {
            if (this.childNodes[i].nodeType == 1) {
                liaisons.push(this.childNodes[i]);
            }
        }
        showAll = function() {
            for (var i = 0; i < liaisons.length; i++) {
                liaisons[i].style.display = 'block';
            }
        };
        hideAllBut = function(liaison) {
            for (var i = 0; i < liaisons.length; i++) {
                if (liaisons[i].id == liaison) {
                    liaisons[i].style.display = 'block';
                } else {
                    liaisons[i].style.display = 'none';
                }       
            }
        };
        options = document.getElementById('liaisons-select').getElementsByTagName('option');
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
})();