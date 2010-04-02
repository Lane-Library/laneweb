(function(){
    YAHOO.util.Event.onAvailable('liaison-form',function() {
        var liaisons = document.getElementById('liaisons').getElementsByTagName('li');
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
        YAHOO.util.Event.addListener(this.getElementsByTagName('select')[0], 'mouseup',function(event){
            if (event.target.value === '') {
                showAll();
            } else {
                hideAllBut(event.target.value);
            }
        });
    });
})();