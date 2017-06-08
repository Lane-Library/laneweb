// teletype functionality for PubMed/UTD through Lane pages
// typing in .teletypeInput sends keystrokes to #search #searchTerms
// if .teletypeInput has name p, i, c, o, keystrokes go to respective pico input
(function() {

    "use strict";

    var i, inputNode,
    teletypeInputs = Y.all('.teletypeInput'),
    teletype = function(inputElm,targetElm){
        var qString;
        if (!targetElm){
            return false;
        }
        if (inputElm.get('value') !== inputElm.get('title')) {
            qString = inputElm.get('value');
        }
        if (!qString){
            qString = targetElm.get('title');
        }
        targetElm.set('value', qString);
    },
    Teletype = function(sourceInput, targetInput) {
        var targetElm = targetInput || Y.one('.search-form input[name=q]'),
        teletyperForm = sourceInput.ancestor('form');
        // send #teletyperForm submit events to #search; no need to send more than one
        if (teletyperForm && teletyperForm.hasHandler === undefined) {
            teletyperForm.hasHandler = true;
            teletyperForm.on("submit",function(event){
                event.halt();
                try {
                    Y.lane.search.submit();
                } catch (error) {
                    alert(error);
                }
            });
        }
        sourceInput.on( 'keyup', function(){
            teletype(this,targetElm);
        });
        sourceInput.on( 'blur', function(){
            teletype(sourceInput,targetElm);
        });
    };

    for (i = 0; i < teletypeInputs.size(); i++) {
        // target elements can either be PICO or main search box
        if(teletypeInputs.item(i).get('name').match(/(p|i|c|o)/)){
            inputNode = Y.one("#clinical"+teletypeInputs.item(i).get('name').toUpperCase());
        }
        else{
            inputNode = Y.one("#searchTerms");
        }
        (new Teletype(teletypeInputs.item(i), inputNode));
    }
})();
