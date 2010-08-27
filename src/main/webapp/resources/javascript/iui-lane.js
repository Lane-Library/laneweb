(function() {
    LANE = function() {
        var $ = iui.$, 
        searchInput = $('searchInput'),
        searchTag = $('searchTag'),
        searchCancel = $('searchCancel'),
        backButton = $('backButton'),
        loadingElm = $('loading'),
        iuiGoBack;

        searchInput.addEventListener("focus", function(e) {
            if (searchTag.innerHTML == e.target.title) {
                searchTag.innerHTML = '';
            }
        }, true);

        searchInput.addEventListener("blur", function(e) {
            if (!e.target.value) {
                searchTag.innerHTML = e.target.title;
            }
        }, true);

        searchInput.addEventListener("keyup", function(e) {
            if (!e.target.value) {
                searchCancel.style.display = 'none';
            }
            else{
                searchCancel.style.display = 'block';
            }
        }, true);
        
        searchCancel.addEventListener("click", function(e) {
            searchCancel.style.display = 'none';
            searchInput.value = '';
            searchInput.focus();
            e.preventDefault();
        }, true);
        
        $('laneSearch').addEventListener("submit", function(e) {
            e.preventDefault();
            if(!searchInput.value){
                alert('nothing to search for');
            }
            else{
                LANE.submitForm(e.target);
            }
        }, true);
        
        $('pico').addEventListener("submit", function(e) {
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
                LANE.submitForm(e.target);
            }
        }, true);
        
        /* override iui.goBack() with scrolling memory */
        /* do this in pageHistory array instead? */
        iuiGoBack = iui.goBack;
        iui.goBack = function(){
            iuiGoBack();
            if(backButton.scroll){
                setTimeout(function(){
                    scrollTo(0,backButton.scroll);
                }, 30);
            }
        };
        document.addEventListener("click", function(e) {
            if(e.target != backButton){
                backButton.scroll = window.pageYOffset;
            }
        }, true);

        
        return {
            submitForm : function(form) {
                iui.addClass(form.parentNode, "loadingMask");
                loadingElm.style.display = 'block';
                for (i = 0; i < form.elements.length; ++i)
                {
                    if (form.elements[i].type == 'search'){
                        form.elements[i].blur();
                    }
                }
                function clear() {
                    iui.removeClass(form.parentNode, "loadingMask"); 
                    loadingElm.style.display = 'none';
                    //scrollTo(0, 1);
                }
                iui.showPageByHref(form.action, form, form.method || "GET", null, clear);
            }
        };
    }();
    
    addEventListener("afterinsert", function(event){
        // scroll panels into viewport after page load
        if(event.insertedNode.className=="absInfo"||event.insertedNode.id=="results"){
            setTimeout(function(){
                scrollTo(0,1);
            }, 200);
        }
    }, true);

})();
