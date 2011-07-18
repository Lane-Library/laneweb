(function() {
    LANE = function() {
        var $ = iui.$, 
        d = document,
        DISPLAY_BLOCK = 'block',
        DISPLAY_NONE = 'none',
        backButton = $('backButton'),
        loadingElm = $('loading'),
        loginLink = $('loginLink'),
        logoutLink = $('logoutLink'),
        pico = $('pico'),
        searchCancel = $('searchCancel'),
        searchInput = $('searchInput'),
        iuiGoBack,
        ipGroup,
        loadInProgress = false;

        if(logoutLink){
            logoutLink.addEventListener("click", function(e) {
                e.preventDefault();
                if(true == confirm("Logout Confirmation\n\n Click \"OK\" to logout or \"Cancel\" to stay logged in.")){
                    document.location.href = e.target.href;
                }
            }, true);
        }
        
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
                }, 100);
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
                scrollTo(0,1);
            },
            setIpGroup : function(group) {
                ipGroup = group;
            },
            getIpGroup : function() {
                return ipGroup;  
            },
            validateCookie : function(cookieValid,validTomorrow){
                if(cookieValid && !validTomorrow){
                    var loginWarningIssued = confirm("Your persistent login is about to expire.\n Click \"OK\" to extend.");
                    if(loginWarningIssued == true){
                        document.location.href = '/././m/persistentlogin.html?pl=true';
                    }
                    /*
                     * TODO: force logout?
                    else{
                        document.location.href = '/././logout';
                    }
                    */
                }
                // toggle login/logout links 
                if(loginLink && logoutLink){
                    if( cookieValid ){
                        logoutLink.style.display = DISPLAY_BLOCK;
                    }
                    else{
                        loginLink.style.display = DISPLAY_BLOCK;
                    }
                }
            },
            loadInProgress : loadInProgress
        };
    }();

    addEventListener("beforeinsert", function(event){
        // chrome ... time for a new framework
        if(event.fragment.childNodes[1].tagName == 'META'){
            for(var i = 0; i < event.fragment.childNodes.length; i++){
                if(event.fragment.childNodes[i].tagName && event.fragment.childNodes[i].tagName.match(/(META|TITLE)/)){
                    event.fragment.removeChild(event.fragment.childNodes[i]);
                }
            }
        }
    }, true);
    
    addEventListener("afterinsert", function(event){
        scrollTo(0,1);
        // scroll abstract/more info panel into viewport after page load
        //if(event.insertedNode.className=="absInfo"){
        //    setTimeout(function(){
        //        //scrollTo(0,1);
        //    }, 200);
        //}
    }, true);
    
})();