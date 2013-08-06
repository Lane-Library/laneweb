(function() {
    var Lane = Y.lane,
        form = Y.one('#search'),
        nav = Y.one('#laneNav'),
        container = Y.one("#searchFormContainer"),
        searchTerms,
        picoIsOn = false,
        picoTextInputs = [],
        picoFields,
        picoOn = function() {
            if (!picoIsOn) {
                if (!picoFields) {
                    createPicoFields();
                }
                picoFields.setStyle("display", "block");
                    form.addClass('clinical');
                    container.addClass("clinical");
                    nav.addClass('clinical');
                picoIsOn = true;
            }
        },
        picoOff = function() {
            if (picoIsOn) {
                picoFields.setStyle("display", "none");
                    form.removeClass('clinical');
                    container.removeClass("clinical");
                    nav.removeClass('clinical');
                picoIsOn = false;
            }
            
        },
        PICO = '<fieldset id="picoFields">' +
               '<input name="p" id="clinicalP" type="text" title="patient condition"/>' +
               '<input name="i" id="clinicalI" type="text" title="intervention"/>' +
               '<input name="c" id="clinicalC" type="text" title="comparison"/>' +
               '<input name="o" id="clinicalO" type="text" title="outcome"/>' +
               '</fieldset>',
        createPicoFields = function() {
            var i, inputs, picoSuggest, queryString = Y.QueryString.parse(location.search);
            picoFields = Y.Node.create(PICO);
            inputs = picoFields.all('input');
            for (i = 0; i < inputs.size(); i++) {
                // set input value if found in query string
                if (queryString[inputs.item(i).get('name')] !== undefined) {
                    inputs.item(i).set('value',queryString[inputs.item(i).get('name')]);
                }
                picoTextInputs.push(new Lane.TextInput(inputs.item(i), inputs.item(i).get('title')));
                inputs.item(i).on("blur",function(){
                    searchTerms.setValue(getPicoQuery());
                });
                inputs.item(i).on("keyup",function(){
                    searchTerms.setValue(getPicoQuery());
                });
                switch(inputs.item(i).get('name')){
                    case 'p':
                        picoSuggest = new Lane.Suggest(inputs.item(i),"mesh-d");
                        break;
                    case 'i':
                        picoSuggest = new Lane.Suggest(inputs.item(i),"mesh-i");
                        break;
                    case 'c':
                        picoSuggest = new Lane.Suggest(inputs.item(i),"mesh-di");
                        break;
                }
                picoSuggest.on("select", function(event) {
                    if(picoIsOn && getPicoQuery()){
                        searchTerms.setValue(getPicoQuery());
                    }
                });
            }
            form.insert(picoFields,0);
        },
         getPicoQuery = function(){ //build query terms from pico inputs
            var qString = '', i;
            for (i = 0; i < picoTextInputs.length; i++) {
                if (picoTextInputs[i].getValue()) {
                    qString += '(' + picoTextInputs[i].getValue() + ')';
                }
            }
            if ( qString.length ){
                qString = qString.replace(/\)\(/g, ") AND (");
                if (qString.indexOf('(') === 0 && qString.indexOf(')') == qString.length - 1) {
                    qString = qString.replace(/(\(|\))/g, '');
                }
            }
            Y.fire('lane:searchPicoChange');
            return qString;
        };
        Y.publish("lane:searchPicoChange",{broadcast:1});
    if (form) {
        searchTerms = new Lane.TextInput(Y.one("#searchTerms"));
        if (form.hasClass('clinical')) {
            picoOn();
        }
        Lane.on('search:sourceChange', function(event) {
            if (event.newVal == 'clinical-all'||event.newVal.indexOf('peds') === 0) {
                picoOn();
                form.one('#clinicalP').focus();
                if(picoTextInputs[0].getValue()){
                    picoTextInputs[0].setValue(picoTextInputs[0].getValue());
                }
                else{
                    picoTextInputs[0].setValue(Lane.Search.getSearchTerms());
                }
            } else {
                picoOff();
            }
        });
    }
})();