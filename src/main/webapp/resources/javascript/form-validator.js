(function() {
    //All that is left of lane-forms.js after switching to the gallery version of formvalidator

    var forms = Y.all('.formvalidator'), i;
    for (i = 0; i < forms.size(); i++) {
        new Y.Validator( {
            form : forms.item(i).get('id'),
            defaultIndicatorDomType : 'DIV',
            defaultIncorrectIndicatorCss : 'validator',
            defaultCorrectIndicatorCss : 'indicator'
        });
    }

    
})();