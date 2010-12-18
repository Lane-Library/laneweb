(function() {
    var Y = LANE.Y,
        form = Y.one('#search'),
        nav = Y.one('#laneNav'),
        searchTerms,
        picoIsOn = false,
        picoTextInputs = [],
        picoFields, //formAnim, navAnim,
        picoOn = function() {
            if (!picoIsOn) {
                if (!picoFields) {
                    createPicoFields();
                }
                picoFields.setStyle("display", "block");
//                formAnim.set('to',{height:124});
//                navAnim.set('to',{top:174});
//                formAnim.on('end', function() {
                    form.addClass('clinical');
//                });
//                navAnim.on('end', function() {
                    nav.addClass('clinical');
//                });
//                formAnim.run();
//                navAnim.run();
                picoIsOn = true;
            }
        },
        picoOff = function() {
            if (picoIsOn) {
                picoFields.setStyle("display", "none");
//                formAnim.set('to',{height:94});
//                navAnim.set('to',{top:144});
//                formAnim.on('end', function() {
                    form.removeClass('clinical');
//                });
//                navAnim.on('end', function() {
                    nav.removeClass('clinical');
//                });
//                formAnim.run();
//                navAnim.run();
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
                if(queryString[inputs.item(i).get('name')] != undefined){
                    inputs.item(i).set('value',queryString[inputs.item(i).get('name')])
                }
                picoTextInputs.push(new LANE.TextInput(inputs.item(i), inputs.item(i).get('title')));
                inputs.item(i).on("blur",function(){
                    searchTerms.setValue(getPicoQuery());
                });
                inputs.item(i).on("keyup",function(){
                    searchTerms.setValue(getPicoQuery());
                });
                switch(inputs.item(i).get('name')){
                    case 'p':
                        picoSuggest = new LANE.Suggest(inputs.item(i),"l=mesh-d&");
                        break;
                    case 'i':
                        picoSuggest = new LANE.Suggest(inputs.item(i),"l=mesh-i&");
                        break;
                    case 'c':
                        picoSuggest = new LANE.Suggest(inputs.item(i),"l=mesh-di&");
                        break;
                }
            }
            form.insert(picoFields);
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
        Y.publish("lane:searchPicoChange",{broadcast:2});
    if (form) {
        searchTerms = new LANE.TextInput(Y.one("#searchTerms"))
        Y.Global.on("lane:suggestSelect",  function(event) {
            if(picoIsOn && getPicoQuery()){
                searchTerms.setValue(getPicoQuery());
            }
        });
        if (form.hasClass('clinical')) {
            picoOn();
        }
//        formAnim = new Y.Anim({
//            node: '#search',
//            easing: Y.Easing.easeOut,
//            duration: 0.3
//        });
//        navAnim = new Y.Anim({
//            node: '#laneNav',
//            easing: Y.Easing.easOut,
//            duration: 0.3 
//        });
        Y.Global.on('lane:searchSourceChange', function(event) {
            if (event.newVal == 'clinical-all'||event.newVal.indexOf('peds') == 0) {
                picoOn();
            } else {
                picoOff();
            }
        });
    }
})();