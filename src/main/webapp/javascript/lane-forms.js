/*
 * LANE.forms 
 * Provides form/field validation
 * Use: markup examined for "validation-patterns" elements
 *      "validation-patterns" value must contain pairs of fieldname=pattern values separated by "::"
 *      example: "full-name=.::email=([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+.[a-zA-Z]+)::pager-phone-number=[0-9-\.\(\)]::affiliation=."
 *      "validation-patterns" can also be used to make one of a set of fields required; pattern validation will be checked as well if supplied
 *      example: "require-one-of=fieldname1,fieldname2,fieldname3"
 */
 (function(){
    LANE.namespace('forms');
    
	LANE.forms = function () {
	    return {
		    /* Validate form at submit time
		     * @method validateFormOnSubmit
		     * @param {Event} e
		     * @return {Boolean} Is form valid
		     */
		    validateFormOnSubmit: function(e) {
			    var ff,     // field focus
			    	i,z,y,  // iterators
			    	radios, // radio inputs
			    	reqOneOfSet = this.validationPatterns["require-one-of"]; // list of fields where any one value found validates requirement ... equiv to OR

				// loop through select, text, textarea and radio elements and check against validation patterns
			    for ( i = this.elements.length - 1; i > 0; i--) { // iterate backwards to keep focus at topmost invalid field
			        if ( this.elements[i].type !== undefined && this.elements[i].type.match(/^(select-one|text|textarea)$/) && this.elements[i].onchange == LANE.forms.validateFieldOnChange ) {
			            this.elements[i].onchange();
			            if ( YAHOO.util.Dom.hasClass(this.elements[i], "invalid-field") ) {
			                this.isValid = false;
			                ff = this.elements[i];
			            }
			        } 
			        if ( this.elements[i].type !== undefined && this.elements[i].type == "radio" && this.validationPatterns[this.elements[i].name] !== undefined ) {
	                    radios = this.elements[this.elements[i].name];
	                    for ( z = 0; z < radios.length; z++ ) {
	                        radios[z].onchange();
	                    }
			            if ( YAHOO.util.Dom.hasClass(this.elements[i], "invalid-field") ) {
			                this.isValid = false;
			                ff = radios[0];
			            }
			        }
			    }

				// if we have a set of OR'd fields, loop through them to make sure at least one value is present			    
			    if ( reqOneOfSet !== undefined ){
	                this.isValid = false;
			    	for ( y = 0; y < reqOneOfSet.split(",").length; y++ ){
				    	ff = this.elements[reqOneOfSet.split(",")[0]]; // set focus to first field in req list
						if ( this.elements[reqOneOfSet.split(",")[y]].value ){
				            ( this.elements[reqOneOfSet.split(",")[y]].onchange !== undefined ) ? this.elements[reqOneOfSet.split(",")[y]].onchange(): ''; // fire on change validation if field has validation pattern as well
				            if ( !YAHOO.util.Dom.hasClass(this.elements[reqOneOfSet.split(",")[y]], "invalid-field") ) {
				                this.isValid = true;
				            }
						}
			    	}
			    }
			    
			    if ( this.isValid === true ) {
			    	// remove validation-patterns element so it's not sent as form data
			    	( this.elements["validation-patterns"] ) ? this.removeChild(this.elements["validation-patterns"]) : '';
				    // call any delayed onsubmit JS we may have found and overridden in markup
				    if ( this.delayedOnsubmit ){
				    	this.delayedOnsubmit();
				    }
				    return true;
			    }
			    else {
			        alert("Some of the fields that are required have not been filled out.\n" +
			        "Please check highlighted fields below and try again.");
			        ff.focus();
			        YAHOO.util.Event.preventDefault(e);
			        return false;
			    }
		    },
		    /* Validate field values as they change
		     * @method validateFieldOnChange
		     */
		    validateFieldOnChange: function() {
			    var field = this, 
			    	value = (field.type == "select-one") ? field.options[field.selectedIndex].value : this.value,
			    	radios,
			    	radioSelected = false,
			    	z;
			    // radios checked for a) if one selected and b) if selected value passes pattern
			    if ( field.type == "radio" ) {
			        radios = YAHOO.util.Dom.getAncestorByTagName(field, 'form').elements[field.name];
			        for (z = 0; z < radios.length; z++) {
			            if (radios[z].checked === true) {
			                value = radios[z].value;
					    	radioSelected = true; 
			            }
			        }
			        field = radios[0];
			    }
			    if ( value.search(field.pattern) == -1 || (field.type == "radio" && radioSelected === false) ) {
			        YAHOO.util.Dom.addClass(field, 'invalid-field');
			        ( LANE.forms.getFieldLabel(field) ) ? YAHOO.util.Dom.addClass(LANE.forms.getFieldLabel(field), 'invalid-label'): '';
			    } else {
			        YAHOO.util.Dom.removeClass(field, 'invalid-field');
			        ( LANE.forms.getFieldLabel(field) ) ? YAHOO.util.Dom.removeClass(LANE.forms.getFieldLabel(field), 'invalid-label'): '';
			    }
		    },
		    /* Convenience method for fetching first label for a given element
		     * @method getFieldLabel
		     * @param {HTMLElement} elm
		     * @return {String} First label with attribute "for" that matches elm name 
		     */
			getFieldLabel: function(elm) {
			    var labels = document.getElementsByTagName('label'), i;
			    for (i = 0; i < labels.length; i++) {
			        if (labels[i].getAttributeNode('for') && elm.name == labels[i].getAttributeNode('for').value) {
			            return labels[i];
			        }
			    }
			}
	    };
	}();

    YAHOO.util.Event.addListener(window, 'load', function(){
    	var form,
    		i,j,y,
    		needsValidation,
    		patternArray,
    		validationFields = document.getElementsByName('validation-patterns');
    		
		for ( i = 0; i < validationFields.length; i++ ){
			form = YAHOO.util.Dom.getAncestorByTagName(validationFields[i],'form');
			needsValidation = false;
			patternArray = validationFields[i].value.split('::');
			form.validationPatterns = {};
			for ( y = 0; y < patternArray.length; y++ ){
	        	form.validationPatterns[patternArray[y].split('=')[0]] = patternArray[y].split('=')[1];
	        }
			for ( j = 0; j < form.elements.length; j++ ){
	            if ( form.elements[j].type !== undefined && form.validationPatterns[form.elements[j].name] !== undefined && form.elements[j].type.match(/^(radio|select-one|text|textarea)$/) ) {
	                needsValidation = true;
	                // set pattern; if fieldname listed w/o pattern, assume any non-whitespace character
	                form.elements[j].pattern = ( form.validationPatterns[form.elements[j].name] !== undefined ) ? form.validationPatterns[form.elements[j].name] : "\\S";
	                form.elements[j].onchange = form.elements[j].onblur = LANE.forms.validateFieldOnChange;
	                if (form.elements[j].type == "radio") {
						form.elements[j].onmouseup = LANE.forms.validateFieldOnChange;
	                }
	            }
	        }

	        if ( needsValidation === true || form.validationPatterns["require-one-of"] !== undefined ){
		    	form.isValid = true; // assume form is valid before onSubmit and onChange checking
	        	// override any onsubmit calls found in markup; delay until after form validation has run
	        	if ( form.onsubmit ){
	        		form.delayedOnsubmit = form.onsubmit;
	        		form.onsubmit = '';
	        	}
	        	YAHOO.util.Event.addListener(form,'submit',LANE.forms.validateFormOnSubmit)
	        }
		}
    });
})();

