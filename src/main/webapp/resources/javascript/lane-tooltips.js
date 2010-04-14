(function() {
	LANE.namespace('tooltips');
	LANE.tooltips = function() {
		return {
			initialize : function() {
				var tooltipContainerArray, // array of tooltip containers
				    tooltipContainer, // tooltips
				    tooltipId, // id of te element
				    tooltipWidth, // width of tooltip
				    simpleTooltipIds = [], // array of contexts that need tt made
										// from their title attribute
				    tooltipElement,
				    YUD = YAHOO.util.Dom, YUW = YAHOO.widget, i, j;

				tooltipContainerArray = YUD.getElementsByClassName('tooltips');

				for (i = 0; i < tooltipContainerArray.length; i++) {
					tooltipContainer = tooltipContainerArray[i].childNodes;
					for (j = 0; j < tooltipContainer.length; j++) {
						if (tooltipContainer[j].nodeType == 1) {
							tooltipId = tooltipContainer[j].id.replace(/Tooltip$/, '');
							if (tooltipId && YUD.inDocument(tooltipId)) {
								tooltipElement = document.getElementById(tooltipId);
								tooltipElement.trackable = true;
								// tooltips where content is in the markup
								// (abstracts, etc.)
								if (tooltipContainer[j].innerHTML
										&& tooltipContainer[j].innerHTML != tooltipElement.title) {
									if (tooltipContainer[j].style.width) {
										tooltipWidth = tooltipContainer[j].style.width;
									} else if (tooltipContainer[j].innerHTML.length > 500) {
										tooltipWidth = '60%';
									} else {
										tooltipWidth = '25%';
									}
									var shat = new YUW.Tooltip(tooltipId + "-yuitt", {
										context : tooltipElement,
										width : tooltipWidth,
										autodismissdelay : 60000,
										text : tooltipContainer[j].innerHTML
									});
								} else {
								    // just use title attribute for all other
								    // tooltips
									simpleTooltipIds.push(tooltipId);
								}
							}
						}
					}
					// simple, single tooltip for all contexts using title
					// attribute
					new YUW.Tooltip("simpleTT-yui", {
						context : simpleTooltipIds,
						autodismissdelay : 60000
					});
				}
			}
		}
	}();

	YAHOO.util.Event.onDOMReady(LANE.tooltips.initialize);

})();
