YUI().use('lane-suggest','lane-textinputs', 'lane', 'node','anim', 'event-custom', function(Y) {
    var form = Y.one('#search'),
        nav = Y.one('#laneNav'),
        fields = form.one('#searchFields'),
        picoIsOn = false,
        picoFields, formAnim, navAnim,
        picoOn = function() {
            if (!picoIsOn) {
                if (!picoFields) {
                    createPicoFields();
                }
//				formAnim.set('to',{height:124});
//				navAnim.set('to',{top:174});
//				formAnim.on('end', function() {
					form.addClass('clinical');
				    fields.insert(picoFields, 2);
//				});
//				navAnim.on('end', function() {
                    nav.addClass('clinical');
//				});
//				formAnim.run();
//				navAnim.run();
                picoIsOn = true;
            }
        },
        picoOff = function() {
            if (picoIsOn) {
//				formAnim.set('to',{height:94});
//				navAnim.set('to',{top:144});
//				formAnim.on('end', function() {
                    picoFields.remove();
					form.removeClass('clinical');
//				});
//				navAnim.on('end', function() {
                    nav.removeClass('clinical');
//				});
//				formAnim.run();
//				navAnim.run();
                picoIsOn = false;
            }
            
        },
        togglePico = function(search) {
            if (search.getSearchSource() == 'clinical-all') {
                picoOn();
            } else {
                picoOff();
            }
        },
        PICO = '<fieldset id="picoFields">' +
               '<input name="p" id="clinicalP" type="text" title="patient condition"/>' +
               '<input name="i" id="clinicalI" type="text" title="intervention"/>' +
               '<input name="c" id="clinicalC" type="text" title="comparison"/>' +
               '<input name="o" id="clinicalO" type="text" title="outcome"/>' +
               '</fieldset>',
        createPicoFields = function() {
            var i, inputs, picoSuggest;
            picoFields = Y.Node.create(PICO);
            inputs = picoFields.all('input');
            for (i = 0; i < inputs.size(); i++) {
                new Y.lane.TextInput(inputs.item(i), inputs.item(i).get('title'));
                switch(inputs.item(i).get('name')){
                	case 'p':
                		picoSuggest = new Y.lane.Suggest(inputs.item(i),"l=mesh-d&");
                		picoSuggest.on("lane:suggestSelect",function(e){
                			Y.log("p->"+e.suggestion);
                			//picoSearchTerms.setP(e.suggestion);
                        });
                		break;
                	case 'i':
                		picoSuggest = new Y.lane.Suggest(inputs.item(i),"l=mesh-i&");
                		picoSuggest.on("lane:suggestSelect",function(e){
                   			Y.log("i->"+e.suggestion);
                   			//picoSearchTerms.setI(e.suggestion);
                        });
                		break;
                	case 'c':
                		picoSuggest = new Y.lane.Suggest(inputs.item(i),"l=mesh-di&");
                		picoSuggest.on("lane:suggestSelect",function(e){
                   			Y.log("c->"+e.suggestion);
                   			//picoSearchTerms.setC(e.suggestion);
                        });
                		break;
                }
            }
        };
    if (form.hasClass('clinical')) {
        createPicoFields();
        fields.insert(picoFields, 2);
        picoIsOn = true;
    }
//	formAnim = new Y.Anim({
//		node: '#search',
//		easing: Y.Easing.easeOut,
//		duration: 0.3
//	});
//	navAnim = new Y.Anim({
//		node: '#laneNav',
//		easing: Y.Easing.easOut,
//		duration: 0.3
//	});
    Y.Global.on('lane:searchSourceChange', togglePico);
});
//    // pico form functionality
//    //  - remove default text values onfocus
//    //  - adds auto complete mesh listener on p i c inputs
//    //  - ties editing of pico to searchTermsInput for query builder effect
//    var i,y,
//        picoForm,             //container of all pico input fields
//        searchTermsInput,     //input for built query terms
//        inputs,               //input elements
//        acInputs,             //input elements requiring auto complete
//         W = Y.YUI2.widget,
//         H = Y.YUI2.util.History,
//         queryBuilder = function(targetId){ //build query terms from pico inputs
//            var qString = '', i;
//            if(targetId == searchTermsInput.id) {
//                return 0;
//            }
//            for (i = 0; i < inputs.length; i++) {
//                if (inputs[i].id != searchTermsInput.id && inputs[i].value !== '' && inputs[i].value != inputs[i].title) {
//                    qString += '(' + inputs[i].value + ')';
//                }
//            }
//            if ( qString.length ){
//                qString = qString.replace(/\)\(/g, ") AND (");
//                if (qString.indexOf('(') === 0 && qString.indexOf(')') == qString.length - 1) {
//                    qString = qString.replace(/(\(|\))/g, '');
//                }
//                searchTermsInput.value = qString;
//            }
//        },
//        // when a suggest list item is selected ...
//        //  - track the suggest-selection-event
//        onItemSelect = function(sType, aArgs) {
//            var item, 
//                trackingObject = {};
//            item = aArgs[2];
//            trackingObject.title = aArgs[0].getInputEl().id + '--suggest-selection-event';
//            trackingObject.path = item[0];
//            LANE.tracking.track(trackingObject);
//        },
//        picofield = Y.one('#clinicalP'),
//        form = Y.one('#search'),
//        nav = Y.one('#laneNav'),
//        togglePico = function(search) {
//            if (search.getSearchSource() == 'clinical-all') {
//                form.addClass('clinical');
//                nav.addClass('clinical');
//            } else {
//                form.removeClass('clinical');
//                nav.removeClass('clinical');
//            }
//        };
//    Y.Global.on('lane:searchSourceChange', togglePico);
//    if (picofield) {
//        // change text of default input values
//        // add event listeners to p,i,c,o inputs for building search terms
//        picoForm = picofield.ancestor('form');//D.getAncestorByTagName(this,'form');
//        inputs = picoForm.all('.picoInput');//D.getElementsByClassName('picoInput',null,picoForm);
//        searchTermsInput = Y.one('#searchTerms');//document.getElementById('searchTerms');
//        for (i = 0; i < inputs.size(); i++) {
//            //TODO: extracting pico values from request ... move to laneweb.xsl?
//            if (H.getQueryStringParameter(inputs.item(i).get('name'))) {
//                inputs.item(i).set('value', H.getQueryStringParameter(inputs.item(i).get('name')).replace(/\+/g, ' '));
//            }
//            if (!inputs.item(i).get('value') || inputs.item(i).get('value') == 'null') {
//                inputs.item(i).set('value', inputs.item(i).get('title'));
//            }
//            Y.on('focus', function() {
//                if (this.get('value') == this.title) {
//                    this.set('value', '');
//                }
//            }, intputs.item(i));
//            Y.on('blur', function() {
//                if (this.get('value') === '') {
//                    this.set('value', this.get('title'));
//                }
//                queryBuilder(this.get('id'));
//            }, inputs.item(i));
//            Y.on('keyup', function() {
//                queryBuilder(this.get('id'));
//            }, inputs.item(i));
//        }
//        
//        // auto complete mesh on p, i, c inputs
//        acInputs = picoForm.all('.acMesh');
//        if (acInputs.size()) {
//            for (y = 0; y < acInputs.size(); y++) {
//                var acCont, // container element to hang the auto complete widget on
//                    acMesh, // auto complete widget
//                    acDs; // data source
//                    acCont = Y.one('#' + acInputs.item(y).get('id').substring(0, 1));
//                    acDs = new Y.YUI2.util.XHRDataSource("/././apps/suggest/json");
//                    acDs.responseType =Y.YUI2.util.XHRDataSource.TYPE_JSON;
//                    acDs.responseSchema = {
//                        resultsList: 'suggest'
//                    };
//                    acDs.scriptQueryParam = "q";
//                // limit added to patient, intervention, comparison
//                if (acCont.get('name') == 'p') {
//                    acDs.scriptQueryAppend = 'l=mesh-d';
//                } else if (acCont.get('name') == 'i') {
//                    acDs.scriptQueryAppend = 'l=mesh-i';
//                } else if (acCont.get('name') == 'c') {
//                    acDs.scriptQueryAppend = 'l=mesh-di';
//                }
//                acDs.connTimeout = 3000;
//                acDs.maxCacheEntries = 100;
//                acMesh = new W.AutoComplete(acCont, Y.Node.getDOMNode(acInputs.item(y)), acDs);
//                acMesh.minQueryLength = 3;
//                acMesh.maxResultsDisplayed = 20;
//                acMesh.useShadow = true;
//                acMesh.useIFrame = true; // ?? needed in IE6 so select doesn't bleed through
//                acMesh.autoHighlight = false;
//                acMesh.animHoriz = false;
//                acMesh.animVert = false;
//                acMesh.queryDelay = 0.1;
//                acMesh.setHeader(acInputs.item(y).get('title'));
//                acMesh.itemSelectEvent.subscribe(function(sType, aArgs) {
//                    queryBuilder();
//                });
//                acMesh.itemSelectEvent.subscribe(onItemSelect);
//            }
//        }
//    }
//});
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