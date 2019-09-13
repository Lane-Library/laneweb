addEventListener("load", function() {

    "use strict";

    (function() {
        var imageSurvey = document.querySelector('.imageSurvey'), 
            impressionRecorded = false,
            recordImpression = function(viewport) {
                if (imageSurvey && viewport.inView(imageSurvey) && impressionRecorded !== true) {
                    impressionRecorded = true;
                    L.fire("tracker:trackableEvent", {
                        category : "lane:imageSearchPromo",
                        action : "images viewed",
                        label : window.model["query"]
                    });
                }
        };
    
        if (imageSurvey) {
            L.fire("tracker:trackableEvent", {
                category : "lane:imageSearchPromo",
                action : "images present",
                label : window.model["query"]
            });
            imageSurvey.querySelectorAll('.surveyLinks a').forEach(function(node) {
                node.addEventListener("click", function(event) {
                    var surveyLinks = imageSurvey.querySelector('.surveyLinks'),
                        surveySent = imageSurvey.querySelector('.surveySent');
                    event.stopPropagation();
                    event.preventDefault();
                    L.fire("tracker:trackableEvent", {
                        category : "lane:imageSearchSurvey",
                        action : "response",
                        label : event.currentTarget.textContent
                    });
                    surveyLinks.style.display = 'none';
                    surveySent.style.display = 'block';
                });
            });
        }
    
        L.on([ "viewport:init", "viewport:scrolled" ], function(event) {
            recordImpression(event.viewport);
        });
    })();
});
