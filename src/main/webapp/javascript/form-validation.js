YAHOO.util.Event.addListener(window,'load',initializeValidation);

    function initializeValidation( ) {
        for(var i = 0; i < document.forms.length; i++) {
            var needsValidation = false;
            var form = document.forms[i];
            var patterns = (form.elements["validation-patterns"] != undefined ) ? form.elements["validation-patterns"].value.split('::') : '';
            var validationPatterns = new Object();
            for(var y = 0; y < patterns.length; y++){
                var tmp = patterns[y].split('=');
                validationPatterns[tmp[0]] = tmp[1];
            }
            form.validationPatterns = validationPatterns;
            
            for(j = 0; j < form.elements.length; j++) {
                var required = false;
                var elm = form.elements[j];

                if (elm.type != "radio" && elm.type != "select-one" && elm.type != "text" && elm.type != "textarea") continue;
                
                required = form.validationPatterns[elm.name] != undefined;
                
                if(required){
                    needsValidation = true;
                    elm.pattern = form.validationPatterns[elm.name];
                    if (!elm.pattern) elm.pattern = "\\S";
                    elm.onchange = elm.onblur = validateOnChange;
                    if (elm.type == "radio") elm.onmouseup = validateOnChange;
                }
            }

            if (needsValidation) form.onsubmit = validateOnSubmit;
        }
    }

    function validateOnChange( ) {
        var field = this;
        var label = getFieldLabel(field);
        var pattern = this.pattern;
        var value = (field.type == "select-one") ? field.options[field.selectedIndex].value : this.value;

        if(field.type == "radio"){
            var radios = YAHOO.util.Dom.getAncestorByTagName(field,'form').elements[field.name];
            for(var z = 0; z < radios.length; z++){
                if (radios[z].checked == true) value = radios[z].value;
            }
            field = radios[0];
        }

        if (value.search(pattern) == -1){
            YAHOO.util.Dom.addClass(field,'invalid-field');
            if (label) YAHOO.util.Dom.addClass(label,'invalid-label');
        }
        else{
            YAHOO.util.Dom.removeClass(field,'invalid-field');
            if (label) YAHOO.util.Dom.removeClass(label,'invalid-label');
        }
    }

    function validateOnSubmit( ) {
        var focus;
        var invalid = false;
        
        for(var i = this.elements.length-1; i > 0; i--) {
            var elm = this.elements[i];
            if ((elm.type == "select-one" || elm.type == "text" || elm.type == "textarea") && elm.onchange == validateOnChange) {
                elm.onchange( );
                elm.onblur( );
                if (YAHOO.util.Dom.hasClass(elm,"invalid-field")){
                    invalid = true;
                    focus = elm;
                }
            }
            else if(elm.type == "radio"){
                if (this.validationPatterns[elm.name] != undefined){
                    var label = getFieldLabel(elm);
                    var passed = false;
                    var radios = this.elements[elm.name];
                    for(var z = 0; z < radios.length; z++){
                        radios[z].onchange( );
                        radios[z].onblur( );
                        radios[z].onmouseup( );
                        if (radios[z].checked == true) passed = true;
                    }
                    if(!passed){
                        invalid = true;
                        focus = radios[0];
                        if (label) YAHOO.util.Dom.addClass(label,'invalid-label');
                    }
                    else if (passed && label) {
                        YAHOO.util.Dom.removeClass(label,'invalid-label');                    
                    }
                }
            }
        }
        if (invalid) {
            alert("Some of the fields that are required have not been filled out.\n" +
                  "Please check highlighted fields below and try again.");
            focus.focus();
            return false;
        }
        this.removeChild(this.elements["validation-patterns"]);
        return true;
    }
    
    function getFieldLabel(fieldEl){
        var labels = document.getElementsByTagName('label');
        for(var i = 0; i < labels.length; i++){
            if(labels[i].getAttributeNode('for') && fieldEl.name == labels[i].getAttributeNode('for').value){
                return labels[i];
            }
        }    
    }