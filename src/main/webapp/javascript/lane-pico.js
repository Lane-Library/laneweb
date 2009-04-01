(function() {
    // pico form functionality
    //  - remove default text values onfocus and change text color
    //  - adds auto complete mesh listener on p i inputs
    //  - ties editing of pico to searchTermsInput for query builder effect
    //  TODO: where will saving of pico values occur? laneweb.xsl or js?
	//  TODO: add caution on c and o fields
	//  TODO: move style.color switching to class switching
    var d = document, 
	picoCont,             //container div for pico inputs fields
	searchTermsInput,     //input for built query terms
	inputs,               //input elements
	acInputs,             //input elements requiring auto complete
 	D = YAHOO.util.Dom,   // shorthand for YUI modules
 	E = YAHOO.util.Event, 
 	W = YAHOO.widget,
 	queryBuilder = function(target){ //build query terms from pico inputs
		var qString = '';
		if ( target === undefined ) 
			target = searchTermsInput;
		for (var i = 0; i < inputs.length; i++) {
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
	};
	
    // initialize on load
    E.addListener(this,'load',function(){
        picoCont = d.getElementById('pico');
        if (picoCont) {
			// change color and text of default input values
			// add event listeners to p,i,c,o inputs for building search terms
            inputs = D.getElementsBy(function(el){return el.type == 'text';},'input',picoCont);
			searchTermsInput = document.getElementById('searchTerms');
			for (var i = 0; i < inputs.length; i++){
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
			acInputs = D.getElementsByClassName('acMesh',null,picoCont);
			if(acInputs.length){
				for (var i = 0; i < acInputs.length; i++){
					var elm = d.getElementById(acInputs[i].id.substring(0,1));
					var ds = new W.DS_XHR("/././apps/mesh-suggest/json", ["mesh"]);
					ds.responseType = W.DS_XHR.TYPE_JSON;
					ds.scriptQueryParam = "q";
					// limit added to patient and intervention
					if(elm.id === 'p' || elm.id === 'i'){
						ds.scriptQueryAppend = 'l='+elm.id;
					}
					ds.connTimeout = 3000; 
					ds.maxCacheEntries = 100;
					var acMesh = new W.AutoComplete(elm, acInputs[i], ds);
					acMesh.minQueryLength = 3;
					acMesh.maxResultsDisplayed = 20; //TODO: change this to 100 once scrolling works
					acMesh.useShadow = true;
					acMesh.useIFrame = true; // ?? needed in IE6 so select doesn't bleed through
					acMesh.autoHighlight = false;
					acMesh.animHoriz = true;
					acMesh.setHeader(acInputs[i].title);
					//TODO: may need a time delay on queryBulder for IE6
					acMesh.itemSelectEvent.subscribe(function(sType,aArgs){queryBuilder();});
				}
			}
        }
    });
})();