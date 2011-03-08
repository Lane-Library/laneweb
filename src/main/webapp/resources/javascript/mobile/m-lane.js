(function() {
    LANE = function() {
        var $ = iui.$, 
        d = document,
        searchInput = $('searchInput'),
        pico = $('pico'),
        searchCancel = $('searchCancel'),
        backButton = $('backButton'),
        loadingElm = $('loading'),
        ipGroup,
        DISPLAY_BLOCK = 'block',
        DISPLAY_NONE = 'none',
        VISIBILITY_VISIBLE = 'visible',
        iuiGoBack,
        loadInProgress = false;

        if(searchInput){
            searchInput.addEventListener("keyup", function(e) {
                searchCancel.style.display = (!e.target.value) ? DISPLAY_NONE : DISPLAY_BLOCK;
            }, true);
            
            searchCancel.addEventListener("click", function(e) {
                searchCancel.style.display = DISPLAY_NONE;
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
                    LANE.track(e);
                }
            }, true);
        }
        
        if(pico){
            pico.addEventListener("submit", function(e) {
                var qString = '', i,
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
                    LANE.track(e);
                }
            }, true);
        }
        
        /* override iui.goBack() with scrolling memory */
        /* do this in pageHistory array instead? */
        iuiGoBack = iui.goBack;
        iui.goBack = function(){
            LANE.loadInProgress = true;
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
            LANE.track(e);
        }, true);
        d.addEventListener("load", function(e) {
            var i, inputs = d.getElementsByTagName("input"),l;

            for (i = 0; inputs.length > i; i++ ){
                if(inputs[i].type == "search"){
                    
                    // turn off autocorrect and autocapitalize on all search inputs
                    inputs[i].setAttribute('autocorrect','off');
                    inputs[i].setAttribute('autocapitalize','off');

                    // instantiate suggest object where appropriate
                    switch(inputs[i].name){
                        case 'q':
                            l = "er-mesh";
                            break;
                        case 'p':
                            l = "mesh-d";
                            break;
                        case 'i':
                            l = "mesh-i";
                            break;
                        case 'c':
                            l = "mesh-di";
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
                }
            }
            
            // turn on/off login, persistent login, logout links 
            if($('loginLink') && $('ploginLink') && $('logoutLink')) {
                if( !LANE.readCookie('webauth_at') && !LANE.readCookie('user')){
                    $('loginLink').style.visibility = VISIBILITY_VISIBLE;
                }
                else if(LANE.readCookie('webauth_at')||LANE.readCookie('user')){
                    $('ploginLink').style.visibility = VISIBILITY_VISIBLE;
                    $('logoutLink').style.visibility = VISIBILITY_VISIBLE;
                }
            }
            
        }, true);

        
        return {
            submitForm : function(form,cb) {
                var i;
                iui.addClass(form.parentNode, "loadingMask");
                loadingElm.style.display = DISPLAY_BLOCK;
                for (i = 0; i < form.elements.length; ++i)
                {
                    if (form.elements[i].type == 'search'){
                        form.elements[i].blur();
                    }
                }
                function clear() {
                    iui.removeClass(form.parentNode, "loadingMask"); 
                    loadingElm.style.display = DISPLAY_NONE;
                    //scrollTo(0, 1);
                    if(cb){
                        cb();
                    }
                }
                iui.showPageByHref(form.action, form, form.method || "GET", null, clear);
            },
            setIpGroup : function(group) {
                ipGroup = group;
            },
            getIpGroup : function() {
                return ipGroup;  
            },
            readCookie : function(name) {
                var nameEQ = name + "=", ca = document.cookie.split(';'), i, c;
                for(i=0;i < ca.length;i++) {
                    c = ca[i];
                    while (c.charAt(0)==' ') c = c.substring(1,c.length);
                    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
                }
                return null;
            },
            loadInProgress : loadInProgress
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