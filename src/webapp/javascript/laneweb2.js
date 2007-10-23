var searching = false;
var metaTags = new Object();


YAHOO.util.Event.addListener(window,'load',initialize);
window.onerror = handleMessage;
    	

function handleMessage( message, url, line)
{
	var parameter = "userAgent="+navigator.userAgent+"&message=".concat(message).concat("&url=").concat(url).concat("&line=").concat(line);
	if(getMetaContent("LW.debug") == "y")
	{
		if (url != null)
			message = message.concat("\nurl --> ").concat(url);
		if (line != null)
			message = message.concat("\nline --> ").concat(line);
		YAHOO.log(message, "error");
	}
	else
		YAHOO.util.Connect.asyncRequest('GET', '/././javascriptLogger?'+parameter);
		return true;
}




function handleFailure(o){
	handleMessage( "Status: "+o.status+ "statusText: "+o.statusText,  o.argument.file, o.argument.line);
}


function log(message)
{	
	handleMessage(message);
}



function initialize(e) {
	initializeMetaTags(e);
	initializeLogger();
	YAHOO.util.Event.addListener(window, 'unload', finalize);
    YAHOO.util.Event.addListener(document, 'mouseover', handleMouseOver);
    YAHOO.util.Event.addListener(document, 'mouseout', handleMouseOut);
    YAHOO.util.Event.addListener(document, 'click', handleClick);
    initializeSearchForm(e);
    if (YAHOO.env.ua.ie) {
    //TODO figure out why this doesn't work with the activate/deactivate business
        var otherPortals = document.getElementById('otherPortalOptions');
        if (otherPortals) {
            YAHOO.util.Event.addListener(otherPortals, 'mouseover',function(e) {this.className='hover'});
            YAHOO.util.Event.addListener(otherPortals, 'mouseout',function(e) {this.className=''});
        }
    }
}


function initializeLogger()
{
	if(getMetaContent("LW.debug") == "y")
	{
		document.body.className = "yui-skin-sam";	
		var myLogReader = new YAHOO.widget.LogReader();
		var logMessage = "context ==> "+context;
		logMessage = logMessage.concat("\nquery_string ==> "+query_string); 
		logMessage = logMessage.concat("\nrequest_uri ==> "+request_uri);
		logMessage = logMessage.concat("\nhref ==> "+href);
		logMessage = logMessage.concat("\nticket ==> "+ticket);
		logMessage = logMessage.concat("\nsunetid ==> "+sunetid);
		logMessage = logMessage.concat("\nproxy_links ==> "+proxy_links);
		logMessage = logMessage.concat("\naffiliation ==> "+affiliation);
		logMessage = logMessage.concat("\nsearch_form_select ==> "+search_form_select);
		logMessage = logMessage.concat("\nsource ==> "+source);
		logMessage = logMessage.concat("\nkeywords ==> "+keywords+"\n");
		YAHOO.log(logMessage , "info");
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
    while (target != undefined) {
        if (target.clicked) {
            target.clicked(e);
        }
        target = target.parentNode;
    }
}

function handleSubmit(e) {
        var target = (e.srcElement) ? e.srcElement : e.target;
        if (target.submit) {
            target.submit(e);
        }
}

function initializeSearchForm(e) {
    var searchForm = document.getElementById('searchForm');
    var searchIndicator = document.getElementById('searchIndicator');
    YAHOO.util.Event.addListener(searchForm, 'submit', handleSubmit);
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
        if (taglines) {
        this.homeOption.activate(e);
        }
    }
    searchForm.submit = function(e) {
	    if(this.keywords.value == '')
	    {
	    	alert('Please enter one or more search terms.');
	    	YAHOO.util.Event.stopEvent(e);
	    }
	    else
	    {    
	    	searchIndicator.style.visibility = 'visible';
	        var formTarget = searchSelect.homeOption.value;
	        if( formTarget.match(/^http/) ){
	            formTarget = formTarget.replace(/\{keywords\}/g,this.keywords.value);
	            window.location = formTarget;
	            YAHOO.util.Event.preventDefault(e);
	        }
        }
    }
    //TODO this isn't used in new design:
    if (taglines) {
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
    }
    //TODO can remove this if() after redesign rollout:
    if (searchSubmit) {
    searchSubmit.activate = function(e) {
        this.src=this.src.replace('search_btn.gif','search_btn_f2.gif');
    }
    searchSubmit.deactivate = function(e) {
        this.src=this.src.replace('search_btn_f2.gif','search_btn.gif');
    }
    }
}


function openNewWindow(url,features) {
    features = (features) ? features : '';
    var w = window.open(url, 'LaneConnex', features);
    if(window.focus){
        w.focus();
    }
}

function email(obfuscatedEmail) {
    document.location = obfuscatedEmail.replace(/\|/g,'');
    return false;
}