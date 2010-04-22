YUI().use('node','yui2-event','yui2-container','yui2-history', function(Y) {
    // pico form functionality
    //  - remove default text values onfocus
    //  - adds auto complete mesh listener on p i c inputs
    //  - ties editing of pico to searchTermsInput for query builder effect
    var i,y,
    d = document, 
    picoForm,             //container of all pico input fields
    searchTermsInput,     //input for built query terms
    inputs,               //input elements
    acInputs,             //input elements requiring auto complete
     E = Y.YUI2.util.Event, 
     W = Y.YUI2.widget,
     H = Y.YUI2.util.History,
     queryBuilder = function(targetId){ //build query terms from pico inputs
        var qString = '', i;
        if(targetId == searchTermsInput.id) {
            return 0;
        }
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
        }
    },
    // when a suggest list item is selected ...
    //  - track the suggest-selection-event
    onItemSelect = function(sType, aArgs) {
        var item, 
            trackingObject = {};
        item = aArgs[2];
        trackingObject.title = aArgs[0].getInputEl().id + '--suggest-selection-event';
        trackingObject.path = item[0];
        LANE.tracking.track(trackingObject);
    };
    
    E.onContentReady('clinicalP', function() {
        // change text of default input values
        // add event listeners to p,i,c,o inputs for building search terms
        picoForm = new Y.Node(this).ancestor('form');//D.getAncestorByTagName(this,'form');
        inputs = picoForm.all('.picoInput');//D.getElementsByClassName('picoInput',null,picoForm);
        searchTermsInput = Y.one('#searchTerms');//document.getElementById('searchTerms');
        for (i = 0; i < inputs.size(); i++){
            //TODO: extracting pico values from request ... move to laneweb.xsl?
            if (H.getQueryStringParameter(inputs.item(i).getAttribute('name'))){
                inputs.item(i).setAttribute('value', H.getQueryStringParameter(inputs.item(i).getAttribute('name')).replace(/\+/g,' '));
            }
            if(!inputs.item(i).getAttribute('value')||inputs.item(i).getAttribute('value') == 'null'){
                inputs.item(i).setAttribute(inputs.item(i).getAttribute('title'));
            }
            E.addListener(Y.Node.getDOMNode(inputs.item(i)), 'focus', function(){
                if (this.value == this.title){
                    this.value = '';
                }
            });
            E.addListener(Y.Node.getDOMNode(inputs.item(i)), 'blur', function(){
                if (this.value === ''){
                    this.value = this.title;
                }
                queryBuilder(this.id);
            });
            E.addListener(Y.Node.getDOMNode(inputs.item(i)), 'keyup', function(){
                queryBuilder(this.id);
            });
        }
        
        // auto complete mesh on p, i, c inputs
        acInputs = picoForm.all('.acMesh');
        if(acInputs.size()){
            for (y = 0; y < acInputs.size(); y++){
                var acCont,   // container element to hang the auto complete widget on
                    acMesh,   // auto complete widget
                    acDs;     // data source
                acCont = new Y.Node(d.getElementsByName(acInputs.item(y).getAttribute('id').substring(0,1))[0]);
                acDs = new W.DS_XHR("/././apps/suggest/json", ["suggest"]);
                acDs.responseType = W.DS_XHR.TYPE_JSON;
                acDs.scriptQueryParam = "q";
                // limit added to patient, intervention, comparison
                if(acCont.getAttribute('name') == 'p'){
                    acDs.scriptQueryAppend = 'l=mesh-d';
                }
                else if(acCont.getAttribute('name') == 'i'){
                    acDs.scriptQueryAppend = 'l=mesh-i';
                }
                else if(acCont.getAttribute('name') == 'c'){
                    acDs.scriptQueryAppend = 'l=mesh-di';
                }
                acDs.connTimeout = 3000; 
                acDs.maxCacheEntries = 100;
                acMesh = new W.AutoComplete(acCont, Y.Node.getDOMNode(acInputs.item(y)), acDs);
                acMesh.minQueryLength = 3;
                acMesh.maxResultsDisplayed = 20;
                acMesh.useShadow = true;
                acMesh.useIFrame = true; // ?? needed in IE6 so select doesn't bleed through
                acMesh.autoHighlight = false;
                acMesh.animHoriz = false;
                acMesh.animVert = false;
                acMesh.queryDelay = 0.1;
                acMesh.setHeader(acInputs.item(y).getAttribute('title'));
                acMesh.itemSelectEvent.subscribe(function(sType,aArgs){queryBuilder();});
                acMesh.itemSelectEvent.subscribe(onItemSelect);
            }
        }
    });
});
/*

                            <fieldset id="picoFields">
                                <div class="acContainer">
                                    <input name="p" class="picoInput" id="clinicalP" type="text"
                                        title="patient condition"/>
                                    <div class="acMesh" id="pAcInput" title="Suggested MeSH Terms"/>
                                </div>
                                <div class="acContainer">
                                    <input name="i" class="picoInput" id="clinicalI" type="text"
                                        title="intervention"/>
                                    <div class="acMesh" id="iAcInput" title="Suggested MeSH Terms"/>
                                </div>
                                <div class="acContainer">
                                    <input name="c" class="picoInput" id="clinicalC" type="text"
                                        title="comparison"/>
                                    <div class="acMesh" id="cAcInput" title="Suggested MeSH Terms"/>
                                </div>
                                <div class="acContainer">
                                    <input name="o" class="picoInput" id="clinicalO" type="text"
                                        title="outcome"/>
                                </div>
                            </fieldset>
 */