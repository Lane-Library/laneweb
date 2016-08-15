(function() {

    "use strict";
    
    var lane = Y.lane,

        limits = {
            p: "mesh-d",
            i: "mesh-i",
            c: "mesh-di"
        },

        fields = [],

        PicoField = function(input, limit) {
            var ynode = new Y.Node(input),
                textInput = new lane.TextInput(ynode, input.getAttribute("placeholder")),
                suggest,
                self = this;
            if (limit) {
                suggest = new lane.Suggest(ynode, limit);
                suggest.on("select", function() {
                    self.fire("input");
                });
            }
            
            input.addEventListener("input", function() {
                self.fire("input");
            });

            this.addTarget(fields);
            return {
                enable: function(enable) {
                    input.disabled = !enable;
                },
                getValue: function() {
                    return textInput.getValue();
                },
                reset: function() {
                    textInput.reset();
                }
            }
        };
    
    Y.augment(PicoField, Y.EventTarget);
    Y.augment(fields, Y.EventTarget);

    [].forEach.call(document.querySelectorAll(".pico-fields input"), function(input) {
        fields.push(new PicoField(input, limits[input.name]));
    });

    lane.on("picoFields:change", function(event) {
        fields.forEach(function(field) {
            field.enable(event.active);
        });
    });
    
    lane.on("searchReset:reset", function() {
        fields.forEach(function(field) {
            field.reset();
        });
    });
    
    fields.on("input", function() {
        var query = "";
        fields.forEach(function(field) {
            var value = field.getValue();
            if (value) {
                query += "(" + value + ")";
            }
        });
        query = query.replace(/\)\(/g, ") AND (");
        if (query.indexOf('(') === 0 && query.indexOf(')') === query.length - 1) {
            query = query.replace(/(\(|\))/g, '');
        }
        lane.search.setQuery(query);
    });
    
    /*
    var Lane = Y.lane,
        form = Y.one('#search'),
        searchTerms,
        picoIsOn = false,
        picoTextInputs = [],
        picoFields,
        getPicoQuery = function(){
            //build query terms from pico inputs
            var qString = '', i;
            for (i = 0; i < picoTextInputs.length; i++) {
                if (picoTextInputs[i].getValue()) {
                    qString += '(' + picoTextInputs[i].getValue() + ')';
                }
            }
            if ( qString.length ){
                qString = qString.replace(/\)\(/g, ") AND (");
                if (qString.indexOf('(') === 0 && qString.indexOf(')') === qString.length - 1) {
                    qString = qString.replace(/(\(|\))/g, '');
                }
            }
            return qString;
        },
        updateQueryInput = function() {
            if (picoIsOn && getPicoQuery()) {
                searchTerms.setValue(getPicoQuery());
            }
        },
        PICO = '<fieldset class="picoFields">' +
        '<input name="p" id="clinicalP" type="text" title="patient condition"/>' +
        '<input name="i" id="clinicalI" type="text" title="intervention"/>' +
        '<input name="c" id="clinicalC" type="text" title="comparison"/>' +
        '<input name="o" id="clinicalO" type="text" title="outcome"/>' +
        '</fieldset>',
        createPicoFields = function() {
            var i, inputs, picoSuggest, queryString = {};
            if (location.search) {
                queryString = Y.QueryString.parse(location.search.substring(1));
            }
            picoFields = Y.Node.create(PICO);
            inputs = picoFields.all('input');
            for (i = 0; i < inputs.size(); i++) {
                // set input value if found in query string
                if (queryString[inputs.item(i).get('name')] !== undefined) {
                    inputs.item(i).set('value',queryString[inputs.item(i).get('name')]);
                }
                picoTextInputs.push(new Lane.TextInput(inputs.item(i), inputs.item(i).get('title')));
                inputs.item(i).on("blur", updateQueryInput);
                inputs.item(i).on("keyup", updateQueryInput);
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
                default :
                }
                picoSuggest.on("select", updateQueryInput);
            }
            form.insert(picoFields,0);
        },
        picoOn = function() {
            if (!picoIsOn) {
                if (!picoFields) {
                    createPicoFields();
                }
                picoFields.addClass("active");
                picoIsOn = true;
            }
        },
        picoOff = function() {
            if (picoIsOn) {
                picoFields.removeClass("active");
                picoIsOn = false;
            }

        };
    if (form) {
        searchTerms = new Lane.TextInput(Y.one("#searchTerms"));
        if (form.hasClass('clinical')) {
            picoOn();
        }
        Lane.on('search:sourceChange', function(event) {
            if (event.newVal === 'clinical-all'||event.newVal.indexOf('peds') === 0) {
                picoOn();
                form.one('#clinicalP').focus();
                if(picoTextInputs[0].getValue()){
                    picoTextInputs[0].setValue(picoTextInputs[0].getValue());
                }
                else{
                    picoTextInputs[0].setValue(Lane.search.getQuery());
                }
            } else {
                picoOff();
            }
        });
        Lane.on("search:reset", function() {
            for (var i = 0; i < picoTextInputs.length; i++) {
                picoTextInputs[i].reset();
            }
        });
    }
    */
})();