var searching = false;
var metaTags = new Object();

YAHOO.util.Event.addListener(window,'load',initialize);

function initialize(e) {
    YAHOO.util.Event.addListener(window, 'unload', finalize);
    YAHOO.util.Event.addListener(document, 'mouseover', handleMouseOver);
    YAHOO.util.Event.addListener(document, 'mouseout', handleMouseOut);
    YAHOO.util.Event.addListener(document, 'click', handleClick);
    initializeSearchForm(e);
    initializeMetaTags(e);
    if (window.initSearch) {
        window.initSearch(e);
    }
}

function finalize(e) {
    searching = false;
}

function initializeMetaTags(e){
	var metaTagElements = document.getElementsByTagName('meta');
	for (var i = 0; i < metaTagElements.length; i++) {
	    var key = metaTagElements[i].getAttribute('name');
	    var value =  metaTagElements[i].getAttribute('content');
	    if(key != undefined &&  value != undefined)
			window.metaTags[key] = value;		
	}
}

function getMetaContent(name)
{
	if(name != undefined)
		return window.metaTags[name];
}

function handleMouseOver(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.activate) {
        target.activate(e);
    }
}

function handleMouseOut(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.deactivate) {
        target.deactivate(e);
    }
}

function handleChange(e) {
    var target = (e.srcElement) ? e.srcElement : e.target;
    if (target.change) {
        target.change(e);
    }
}

function handleClick(e) {
	var target = (e.srcElement) ? e.srcElement : e.target;
    if(target.tagName == "A" || target.tagName == "IMG")
    	webtrendsProcess(target);
    while (target != undefined) {
        if (target.clicked) {
            target.clicked(e);
        }
        target = target.parentNode;
    }
}

function webtrendsProcess(node)
{
	var DCS_dcsuri;
	var DCS_dcsquery;
	var WT_ti;
	
	
}

 

function initializeSearchForm(e) {
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
}


function openNewWindow(url,features) {
    features = (features) ? features : '';
    var w = window.open(url, 'LaneConnex', features);
    if(window.focus){
        w.focus();
    }
}