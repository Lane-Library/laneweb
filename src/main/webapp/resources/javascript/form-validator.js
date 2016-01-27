
(function() {

    "use strict";

    var forms = Y.all('.formvalidator'), i;
    for (i = 0; i < forms.size(); i++) {
        (new Y.Validator( {
            form : forms.item(i).get('id'),
            defaultIndicatorDomType : 'DIV',
            defaultIncorrectIndicatorCss : 'validator',
            defaultCorrectIndicatorCss : 'indicator'
        }));
    }

//FormValidator class
    var FormValidator = function(form) {
        var i, node, validator, inputFields,
            nodes = form.all("input[title='required']"),
            fieldJSON = [],
            focusHandler = function(event) {
                event.target.removeClass("incorrect");
            },
            emptyHandler = function() {
                var node = this.get("inputDOM");
                return node.value === '' || node.value === node.title;
            };
        for (i = 0; i < nodes.size(); i++) {
            node = nodes.item(i);
            (new Y.lane.TextInput(node, "required"));
            //remove incorrect styling on focus
            node.on("focus", focusHandler);
            fieldJSON.push({
                type : Y.TextBaseField,
                atts : {
                    inputDOM : Y.Node.getDOMNode(node),
                    correctCss : "correct",
                    incorrectCss : "incorrect",
                    isOn : true
                }
            });
        }
        validator = new Y.Validator({
            form : Y.Node.getDOMNode(form),
            fieldJSON : fieldJSON,
            checkOnSubmit : true
        });
        inputFields = validator.get("inputFields");
        for (i = 0; i < inputFields.length; i++) {
            inputFields[i].isEmpty = emptyHandler;
            // remove class incorrect if present (set if using placeholder, see lane-textinputs.js
            nodes.item(i).removeClass("incorrect");
        }
        return {
            destroy : function() {
                var i, nodes = form.all("input[title='required']");
                Y.Event.detach("submit", validator._onFormSubmit, form);
                Y.Event.detach("reset", validator._onFormReset, form);
                for (i = 0; i < nodes.size(); i++) {
                    Y.Event.detach("focus", focusHandler, nodes.item(i));
                }
                validator.destroy();
            },
            isValid : function() {
                return validator.checkFormValues();
            }
        };
    };

    Y.namespace("lane");
    Y.lane.FormValidator = FormValidator;


})();
