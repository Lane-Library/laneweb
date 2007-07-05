var searching = false;

YAHOO.util.Event.addListener(window,'load',initialize);

function initialize(e) {
	YAHOO.util.Event.addListener(window, 'unload', finalize);
	YAHOO.util.Event.addListener(window, 'mouseover', handleMouseOver);
	YAHOO.util.Event.addListener(window, 'mouseout', handleMouseOut);
	YAHOO.util.Event.addListener(window, 'change', handleChange);
	initializeSearchForm(e);
}

function finalize(e) {
	searching = false;
}

function handleMouseOver(e) {
    if (e.target.activate) {
        e.target.activate(e);
    }
}

function handleMouseOut(e) {
    if (e.target.deactivate) {
        e.target.deactivate(e);
    }
}

function handleChange(e) {
	if (e.target.change) {
	    e.target.change(e);
	}
}

function initializeSearchForm(e) {
    var searchForm = document.getElementById('searchForm');
    var taglines = document.getElementById('taglines');
	var allTagline = document.getElementById('allTagline');
	var searchSelect = document.getElementById('searchSelect');
	var displayTagline = document.getElementById('displayTagline');
	var searchSubmit = document.getElementById('searchSubmit');
	searchSelect.homeOption = searchSelect.options[searchSelect.selectedIndex];
	searchSelect.change = function(e) {
		this.homeOption = this.options[this.selectedIndex];
		this.homeOption.activate;
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
	            this.displayTagline.textContent = this.tagLine.textContent;
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