(function() {
    // pico form functionality
    //  - remove default text values onfocus and change text color
    //  - adds auto complete mesh listener on p i inputs
    //  - ties editing of pico to searchTermsInput for query builder effect
    //  TODO: where will saving of pico values occur? laneweb.xsl or js?
    var i,y,z,
    d = document, 
    picoContainer,             //container div for pico inputs fields
    searchTermsInput,     //input for built query terms
    inputs,               //input elements
    acInputs,             //input elements requiring auto complete
    warningPanel,         //single panel for c and o warnings
    warningContentElms,   //elements containing warning text and markup
    warningTarget,        // target element that toggles warning display
    warningViewLimit = 2, // max number of times to show warning
     D = YAHOO.util.Dom,   // shorthand for YUI modules
     E = YAHOO.util.Event, 
     W = YAHOO.widget,
     queryBuilder = function(target){ //build query terms from pico inputs
        var qString = '', i;
        if ( target === undefined ) {
            target = searchTermsInput;
        }
        for (i = 0; i < inputs.length; i++) {
            if (inputs[i].id != target.id && inputs[i].value !== '' && inputs[i].value != inputs[i].initState.value) {
                qString += '(' + inputs[i].value + ')';
            }
        }
        if ( qString.length ){
            qString = qString.replace(/\)\(/g, ") AND (");
            if (qString.indexOf('(') === 0 && qString.indexOf(')') == qString.length - 1) {
                qString = qString.replace(/(\(|\))/g, '');
            }
            target.value = qString;
            D.removeClass(target,'inputTip');
        }
        else{
            target.value = target.initState.value;
            D.addClass(target,'inputTip');
        }
    },
    createWarningPanel = function(){
        var container = document.createElement('div'), id = 'warningPanel';
        container.setAttribute('id', id);
        document.body.appendChild(container);
        warningPanel = new YAHOO.widget.Panel(id, {
            underlay: 'none',
            close: false,
            visible: false,
            draggable: false,
            constraintoviewport: true,
            modal: false
        });
    },
    showWarningPanel = function(sourceEl,width,X,Y){
        if(sourceEl.viewed === undefined || sourceEl.viewed < warningViewLimit ){
            warningPanel.setBody(sourceEl.innerHTML);
            warningPanel.cfg.setProperty('width', width+'px');
            warningPanel.cfg.setProperty('X',X);
            warningPanel.cfg.setProperty('Y',Y);
            warningPanel.render();
            warningPanel.show();
            sourceEl.viewed = (sourceEl.viewed === undefined) ? 1 : sourceEl.viewed+1;
        }
    };
    
    // initialize on load
    //E.addListener(this,'load',function(){
    E.onDOMReady(function(){
        picoContainer = d.getElementById('pico');
        if (picoContainer) {
            // change color and text of default input values
            // add event listeners to p,i,c,o inputs for building search terms
            inputs = D.getElementsBy(function(el){return el.type == 'text';},'input',picoContainer);
            searchTermsInput = document.getElementById('searchTerms');
            for (i = 0; i < inputs.length; i++){
                inputs[i].initState = {
                    'value' : inputs[i].value
                };
                E.addListener(inputs[i], 'focus', function(e,initObj){
                    if (this.value == initObj.value){
                        this.value = '';
                        D.removeClass(this,'inputTip');
                    }
                },inputs[i].initState);
                E.addListener(inputs[i], 'blur', function(e,initObj){
                    if (this.value === ''){
                        this.value = initObj.value;
                        D.addClass(this,'inputTip');
                    }
                },inputs[i].initState);
                if ( inputs[i].id != searchTermsInput.id ){
                    E.addListener(inputs[i], 'blur', function(e,initObj){
                        queryBuilder();
                    },inputs[i].initState);
                    E.addListener(inputs[i], 'keyup', function(e,initObj){
                        queryBuilder();
                    },inputs[i].initState);
                }
            }
            
            // auto complete mesh on p and i inputs
            acInputs = D.getElementsByClassName('acMesh',null,picoContainer);
            if(acInputs.length){
                for (y = 0; y < acInputs.length; y++){
                    var acCont,   // container element to hang the auto complete widget on
                        acMesh,   // auto complete widget
                        acDs;     // data source
                    acCont = d.getElementById(acInputs[y].id.substring(0,1));
                    acDs = new W.DS_XHR("/././apps/mesh-suggest/json", ["mesh"]);
                    acDs.responseType = W.DS_XHR.TYPE_JSON;
                    acDs.scriptQueryParam = "q";
                    // limit added to patient and intervention
                    if(acCont.id === 'p' || acCont.id === 'i'){
                        acDs.scriptQueryAppend = 'l='+acCont.id;
                    }
                    acDs.connTimeout = 3000; 
                    acDs.maxCacheEntries = 100;
                    acMesh = new W.AutoComplete(acCont, acInputs[y], acDs);
                    acMesh.minQueryLength = 3;
                    acMesh.maxResultsDisplayed = 20;
                    acMesh.useShadow = true;
                    acMesh.useIFrame = true; // ?? needed in IE6 so select doesn't bleed through
                    acMesh.autoHighlight = false;
                    acMesh.animHoriz = false;
                    acMesh.animVert = false;
                    acMesh.queryDelay = 0.1;
                    acMesh.setHeader(acInputs[y].title);
                    acMesh.itemSelectEvent.subscribe(function(sType,aArgs){queryBuilder();});
                }
            }
            
            // create warnings on c and o inputs
            warningContentElms = D.getElementsByClassName('inputWarning');
            for (z = 0; z < warningContentElms.length; z++){
                warningTarget = d.getElementById(warningContentElms[z].id.replace('Warning',''));
                if(!warningPanel){
                    createWarningPanel();
                }
                YAHOO.util.Event.on(warningTarget, 'focus', function() {
                    showWarningPanel(d.getElementById(this.id+"Warning"),this.offsetWidth,D.getX(this),D.getY(this)+24);
                });
                YAHOO.util.Event.on(warningTarget, 'blur', function() {
                    warningPanel.hide();
                });
            }
            
        }
    });
    
})();