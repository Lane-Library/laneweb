var searching = false;
var metaTags = new Object();

YAHOO.util.Event.addListener(window,'load',initialize);

function initialize(e) {
try {
    YAHOO.util.Event.addListener(window, 'unload', finalize);
    YAHOO.util.Event.addListener(document, 'mouseover', handleMouseOver);
    YAHOO.util.Event.addListener(document, 'mouseout', handleMouseOut);
    YAHOO.util.Event.addListener(document, 'click', handleClick);
    initializeSearchForm(e);
    initializeMetaTags(e);
    } catch(exception) { handleException(exception);  }
}

function finalize(e) {
    searching = false;
}

function handleException(exception) {
    alert(exception.name + '\n' + exception.message + '\n' + exception.fileName + '\n' + exception.lineNumber + '\n' + exception.stack);
}

function initializeMetaTags(e){
try {
	var metaTagElements = document.getElementsByTagName('meta');
	for (var i = 0; i < metaTagElements.length; i++) {
	    var key = metaTagElements[i].getAttribute('name');
	    var value =  metaTagElements[i].getAttribute('content');
	    if(key != undefined &&  value != undefined)
			window.metaTags[key] = value;		
	}
    } catch(exception) { handleException(exception) }
}

function getMetaContent(name)
{
try {
	if(name != undefined)
		return window.metaTags[name];
    } catch(exception) { handleException(exception) }
}

function handleMouseOver(e) {
try {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.activate) {
        target.activate(e);
    }
    } catch(exception) { handleException(exception) }
}

function handleMouseOut(e) {
try {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.deactivate) {
        target.deactivate(e);
    }
    } catch(exception) { handleException(exception) }
}

function handleChange(e) {
try {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.change) {
        target.change(e);
    }
    } catch(exception) { handleException(exception) }
}

function handleClick(e) {
try {
	var target = (e.srcElement) ? e.srcElement : e.target;
    if(target.tagName == "A" || target.tagName == "IMG")
    	webtrendsProcess(target);
    while (target != undefined) {
        if (target.clicked) {
            target.clicked(e);
        }
        target = target.parentNode;
    }
    } catch(exception) { handleException(exception) }
}

function webtrendsProcess(node){
	try{
		if(node.tagName == "A")
			{
				var host = getMetaContent("LW.host");
				if(host != node.hostname)
				{
					alert(node.hostname +"    "+ node.host);
					
				} 
			}
			if(node.tagName == "IMG")
			{
			}
	} catch(exception) { handleException(exception) }
} 

function initializeSearchForm(e) {
try {
    var searchForm = document.getElementById('searchForm');
    var taglines = document.getElementById('taglines');
    var allTagline = document.getElementById('allTagline');
    var searchSelect = document.getElementById('searchSelect');
    YAHOO.util.Event.addListener(searchSelect, 'change', handleChange);
    var displayTagline = document.getElementById('displayTagline');
    var searchSubmit = document.getElementById('searchSubmit');
    searchSelect.homeOption = searchSelect.options[searchSelect.selectedIndex];
    searchSelect.change = function(e) {
        if (this.options[this.selectedIndex].disabled) {
            this.selectedIndex = this.homeOption.index;
        } else {
            this.homeOption = this.options[this.selectedIndex];
        }
        this.homeOption.activate(e);
    }
    for (i = 0; i < searchSelect.options.length; i++) {
        var option = searchSelect.options[i];
        if (!option.disabled) {
            option.displayTagline = displayTagline
            option.tagLine = document.getElementById(option.value + 'Tagline');
            if (!option.tagLine) {
                option.tagLine = allTagline;
            }
            option.activate = function(e) {
                this.displayTagline.innerHTML = this.tagLine.innerHTML;
            }
            option.deactivate = function(e) {
                this.parentNode.homeOption.activate(e);
            }
        }
    }
    searchSelect.homeOption.activate();
    searchSubmit.activate = function(e) {
        this.src=this.src.replace('search_btn.gif','search_btn_f2.gif');
    }
    searchSubmit.deactivate = function(e) {
        this.src=this.src.replace('search_btn_f2.gif','search_btn.gif');
    }
    } catch(exception) { handleException(exception) }
}


function openNewWindow(url,features) {
try {
    features = (features) ? features : '';
    var w = window.open(url, 'LaneConnex', features);
    if(window.focus){
        w.focus();
    }
    
    } catch(exception) { handleException(exception) }
}