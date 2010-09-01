(function() {
    LANE = function() {
        var $ = iui.$, 
        searchInput = $('searchInput'),
        backButton = $('backButton'),
        loadingElm = $('loading'),
        fullscreenMessage = $('fsMsg'),
        d = document,
        iuiGoBack;

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
        d.addEventListener("click", function(e) {
            if(e.target != backButton){
                backButton.scroll = window.pageYOffset;
            }
        }, true);
        d.addEventListener("load", function() {
            var i, inputs = d.getElementsByTagName("input"),l;
            for (i = 0; inputs.length > i; i++ ){
                if(inputs[i].type == "search"){
                    
                    // turn off autocorrect and autocapitalize on all search inputs
                    inputs[i].setAttribute('autocorrect','off');
                    inputs[i].setAttribute('autocapitalize','off');

                    // instantiate suggest object where appropriate
                    switch(inputs[i].name){
                        case 'q':
                            l = "er-mesh"
                            break;
                        case 'p':
                            l = "mesh-d"
                            break;
                        case 'i':
                            l = "mesh-i"
                            break;
                        case 'c':
                            l = "mesh-di"
                            break;
                    }
                    if(inputs[i].name!="o"){
                        new LANE.Suggest(inputs[i].id, {
                            minChars : 3,
                            idleDelay : 200,
                            requestLimit : l,
                            suggestUrl : '/././apps/suggest/json'
                        });
                    }
                    
                    // add listeners for clearing input values
                    if(inputs[i].previousElementSibling.className == 'clear'){
                        inputs[i].addEventListener("keyup", function(e) {
                            console.log("inut keyup");
                            console.log(e);
                            if (!e.target.value) {
                                e.target.previousElementSibling.style.display = 'none';
                            }
                            else{
                                e.target.previousElementSibling.style.display = 'block';
                            }
                        }, true);
                        inputs[i].previousElementSibling.addEventListener("click", function(e) {
                            e.target.parentNode.style.display = 'none';
                            e.target.parentNode.nextElementSibling.value = '';
                            e.target.parentNode.nextElementSibling.focus();
                            e.preventDefault();
                        }, true);
                    }
                }
            }
            
            // hide full screen message if we're in standalone app mode
            if(fullscreenMessage){
                fullscreenMessage.style.display = (window.navigator.standalone) ? 'none' : 'block';
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