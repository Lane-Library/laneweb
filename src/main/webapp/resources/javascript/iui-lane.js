(function() {

    if (typeof LANE == "undefined" || !LANE) {
        LANE = {};
    }
    LANE.searchInput = document.getElementById('searchInput');
    LANE.searchTag = document.getElementById('searchTag');
    LANE.laneSearchForm = document.getElementById('laneSearch');
    LANE.searchCancel = document.getElementById('searchCancel');
    LANE.picoForm = document.getElementById('pico');
    LANE.backButton = document.getElementById('backButton');
    
    LANE.searchInput.addEventListener("focus", function(e) {
        if (LANE.searchTag.innerHTML == e.target.title) {
            LANE.searchTag.innerHTML = '';
        }
    }, true);

    LANE.searchInput.addEventListener("blur", function(e) {
        if (!e.target.value) {
            LANE.searchTag.innerHTML = e.target.title;
        }
    }, true);

    LANE.searchInput.addEventListener("keyup", function(e) {
        if (!e.target.value) {
            LANE.searchCancel.style.display = 'none';
        }
        else{
            LANE.searchCancel.style.display = 'block';
        }
    }, true);
    
    LANE.searchCancel.addEventListener("click", function(e) {
        LANE.searchCancel.style.display = 'none';
        LANE.searchInput.value = '';
        LANE.searchInput.focus();
        e.preventDefault();
    }, true);
    
    LANE.laneSearchForm.addEventListener("submit", function(e) {
        e.preventDefault();
        if(!LANE.searchInput.value){
            alert('nothing to search for');
        }
        else{
            iui.laneSubmitForm(e.target);
        }
    }, true);
    
    LANE.picoForm.addEventListener("submit", function(e) {
        var inputs, qString = '', i,
        inputs = e.target.getElementsByTagName('input'),
        qInput;
        e.preventDefault();
        for (i = 0; i < inputs.length; i++) {
            if (inputs[i].name.match(/(p|i|c|o)/) && inputs[i].value) {
                qString += '(' + inputs[i].value + ')';
            }
            else if(inputs[i].name == 'q'){
                qInput = inputs[i];
            }
        }
        if ( qString.length ){
            qString = qString.replace(/\)\(/g, ") AND (");
            if (qString.indexOf('(') === 0 && qString.indexOf(')') == qString.length - 1) {
                qString = qString.replace(/(\(|\))/g, '');
            }
        }
        qInput.value = qString;
        if(!qInput.value){
            alert('nothing to search for');
        }
        else{
            iui.laneSubmitForm(e.target);
        }
    }, true);
    
    addEventListener("afterinsert", function(event){
        // scroll panels into viewport after page load
        if(event.insertedNode.className=="absInfo"||event.insertedNode.id=="results"){
            setTimeout(function(){
                scrollTo(0,1);
            }, 200);
        }
    }, true);

    document.addEventListener("click", function(e) {
        if(e.target != LANE.backButton){
            LANE.backButton.scroll = window.pageYOffset;
        }
    }, true);

    /* override iui.goBack() with scrolling memory */
    /* do this in pageHistory array instead? */
    LANE.iuiGoBack = iui.goBack;
    iui.goBack = function(){
        LANE.iuiGoBack();
        if(LANE.backButton.scroll){
            setTimeout(function(){
                scrollTo(0,LANE.backButton.scroll);
            }, 30);
        }
    };
    
})();
