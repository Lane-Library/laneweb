(function() {
    // pico form functionality
    //  - remove default text values onfocus and change text color
    //  - adds auto complete mesh listener on p i inputs
    //  - ties editing of pico to searchTermsInput for query builder effect
    //  TODO: where will saving of pico values occur? laneweb.xsl or js?
    var i,y,z,
    d = document, 
    picoForm,             //container of all pico input fields
    searchTermsInput,     //input for built query terms
    inputs,               //input elements
    acInputs,             //input elements requiring auto complete
    warningPanel,         //single panel for c and o warnings
    warningContentElms,   //elements containing warning text and markup
    warningTarget,        // target element that toggles warning display
    warningViewLimit = 2, // max number of times to show warning
     D = YAHOO.util.Dom,  // shorthand for YUI modules
     E = YAHOO.util.Event, 
     W = YAHOO.widget,
     activeFacet,
     queryBuilder = function(targetId){ //build query terms from pico inputs
        var qString = '', i;
        if(targetId == searchTermsInput.id) {
            return 0;
        };
        for (i = 0; i < inputs.length; i++) {
            if (inputs[i].id != searchTermsInput.id && inputs[i].value !== '' && inputs[i].value != inputs[i].title) {
                qString += '(' + inputs[i].value + ')';
            }
        }
        if ( qString.length ){
            qString = qString.replace(/\)\(/g, ") AND (");
            if (qString.indexOf('(') === 0 && qString.indexOf(')') == qString.length - 1) {
                qString = qString.replace(/(\(|\))/g, '');
            }
            searchTermsInput.value = qString;
            D.removeClass(searchTermsInput,'inputTip');
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
//    E.onDOMReady(function(){
    E.onContentReady('pico', function() {
        picoForm = D.getAncestorByTagName(this,'form');
        YAHOO.util.Get.script('http://yui.yahooapis.com/combo?2.7.0/build/datasource/datasource-min.js&2.7.0/build/animation/animation-min.js&2.7.0/build/autocomplete/autocomplete-min.js',{
            onSuccess:function() {
            // change color and text of default input values
            // add event listeners to p,i,c,o inputs for building search terms
            inputs = D.getElementsBy(function(el){return el.type == 'text';},'input',picoForm);
            searchTermsInput = document.getElementById('searchTerms');
            searchTermsInput.title = 'use PICO search above or type keywords here';
            if (searchTermsInput.value == ''){
                searchTermsInput.value = searchTermsInput.title;
                searchTermsInput.className = 'inputTip';
            }
            for (i = 0; i < inputs.length; i++){
                if(inputs[i].value === ''){
                    inputs[i].value = inputs[i].title;
                }
                else if(inputs[i].value != inputs[i].title){
                    D.removeClass(inputs[i],'inputTip');
                }
                E.addListener(inputs[i], 'focus', function(){
                    if (this.value == this.title){
                        this.value = '';
                        D.removeClass(this,'inputTip');
                    }
                });
                E.addListener(inputs[i], 'blur', function(){
                    if (this.value === ''){
                        this.value = this.title;
                        D.addClass(this,'inputTip');
                    }
                    queryBuilder(this.id);
                });
                E.addListener(inputs[i], 'keyup', function(){
                    queryBuilder(this.id);
                });
            }
            
            // auto complete mesh on p and i inputs
            acInputs = D.getElementsByClassName('acMesh',null,picoForm);
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
    });
    E.onContentReady('facetTabs',function() {
        var facetTabs = this.getElementsByTagName('LI'),
            
            toggleFacet = function(facet) {
                var i, facetElements = D.getElementsByClassName(activeFacet);
                for (i = 0; i < facetElements.length; i++) {
                    D.setStyle(facetElements[i], 'display', 'none');
                }
                facetElements = D.getElementsByClassName(facet);
                for (i = 0; i < facetElements.length; i++) {
                    D.setStyle(facetElements[i], 'display', 'block');
                }
                D.removeClass(D.getElementsByClassName('activeFacet')[0], 'activeFacet');
                D.addClass(d.getElementById(facet + 'Tab'), 'activeFacet');
                if (facet == 'foundational') {
                    facetInput.removeAttribute('name');
                    facetInput.removeAttribute('value');
                } else {
                    facetInput.name = 'facet';
                    facetInput.value = facet;
                }
                activeFacet = facet;
            };
        facetInput = d.getElementById('facetInput');
        for (var i = 0; i < facetTabs.length; i++) {
            if (facetTabs[i].className == 'activeFacet') {
                activeFacet = facetTabs[i].id.substring(0, facetTabs[i].id.indexOf('Tab'));
            }
            facetTabs[i].clicked = function(event) {
                var target;
                E.stopEvent(event);
                if (!event.handled) {
                    toggleFacet(this.id.substring(0, this.id.indexOf('Tab')));
                    target = event.target || event.srcElement;
                    target.blur();
                    event.handled = true;
                }

            };
        }
    });
    
})();