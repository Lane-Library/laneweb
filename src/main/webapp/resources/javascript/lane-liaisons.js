(function(){
    YAHOO.util.Event.onAvailable('liaisons',function() {
        var liaisons = [];
        for (var i = 0; i < this.childNodes.length; i++) {
            if (this.childNodes[i].nodeType == 1) {
                liaisons.push(this.childNodes[i]);
            }
        }
        var showAll = function() {
            for (var i = 0; i < liaisons.length; i++) {
                liaisons[i].style.display = 'block';
            }
        };
        var hideAllBut = function(liaison) {
            for (var i = 0; i < liaisons.length; i++) {
                if (liaisons[i].id == liaison) {
                    liaisons[i].style.display = 'block';
                } else {
                    liaisons[i].style.display = 'none';
                }       
            }
        };
        YAHOO.util.Event.addListener(document.getElementById('liaisons-select'), 'mouseup',function(event){
            if (event.target.value === '') {
                showAll();
            } else {
                hideAllBut(event.target.value);
            }
        });
    });
})();