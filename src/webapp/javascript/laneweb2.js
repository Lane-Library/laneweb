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
/* commenting out temporarily because causing error alert
try {
	var target = (e.srcElement) ? e.srcElement : e.target;
    webtrendsProcess(target);
    while (target != undefined) {
        if (target.clicked) {
            target.clicked(e);
        }
        target = target.parentNode;
    }
    } catch(exception) { handleException(exception) }
    */
}

function webtrendsProcess(node){
	try{
		var title;
		var host;
		if(node.hostname != undefined)
		{
			host = node.hostname;
			if(host.indexOf(':')>-1)//safari return port
				host = host.substring(0,host.indexOf(':'));
		}
		if(node.tagName == "IMG")
		{
			var anchorNode = node.parentNode;
			if(anchorNode != undefined)
				node = anchorNode;
		}
	
		if(node.tagName == "A" || node.tagName == "AREA")//for anchor tag 
		{
			if(host != getMetaContent("LW.host") || node.href.indexOf("/secure/login.html?url=") >-1 )
			{
				var href;
				var uri= '/';
				var query ;
				var proxyUrl = 'http://laneproxy.stanford.edu/login';
				var href = node.href;
				if(href.indexOf(proxyUrl) > -1 || node.href.indexOf("/secure/login.html?url=") >-1)
					href = href.substring( href.indexOf('url=')+4 , href.length);
				if(href.indexOf('://')>-1)		
					href = href.substring(href.indexOf('://')+3, href.length);
				if(href.indexOf('?')>-1)
				{
					query = href.substring(href.indexOf('?')+1, href.length);
					href = href.substring(0,href.indexOf('?'));
				}
				if(href.indexOf('/')>-1)
				{	
					uri =  href.substring(href.indexOf('/'), href.length);	
					href = href.substring(0,href.indexOf('/'));
				}	
				title = getWebtrendsTitle(node);	
				//alert('DCS.dcssip:'+ href +'\nDCS.dcsuri:'+uri + '\nDCS.dcsquery:' +query  +'\nWT.ti:'+title +'\nDCSext.keywords'+getMetaContent('LW.keywords')+'\nDCSext.search_type:'+getMetaContent('LW.source')+'\nDCSext.offsite_link:1');
			 	if(getMetaContent('LW.keywords') != undefined)
			 	{
			 		if(query != undefined )
						dcsMultiTrack('DCS.dcssip', href,'DCS.dcsuri',uri,'DCS.dcsquery',query,'WT.ti',title,'DCSext.keywords',getMetaContent('LW.keywords'),'DCSext.search_type',getMetaContent('LW.source'),'DCSext.offsite_link','1');
					else	
						dcsMultiTrack('DCS.dcssip', href,'DCS.dcsuri',uri,'WT.ti',title,'DCSext.keywords',getMetaContent('LW.keywords'),'DCSext.search_type',getMetaContent('LW.source'),'DCSext.offsite_link','1');
				}
				else
				{
					if(query != undefined )
						dcsMultiTrack('DCS.dcssip', href,'DCS.dcsuri',uri,'DCS.dcsquery',query,'WT.ti',title,'DCSext.offsite_link','1');
					else	
						dcsMultiTrack('DCS.dcssip', href,'DCS.dcsuri',uri,'WT.ti',title,'DCSext.offsite_link','1');
			 	}
			 }
		}
		
		if((node.href != undefined) && host == getMetaContent("LW.host") &&
			(node.href.indexOf('.pdf') > -1 || node.href.indexOf('.camv') >-1 || node.href.indexOf('.smil') >-1 || node.href.indexOf('.doc') >-1 
		  || node.href.indexOf('.ppt') > -1 || node.href.indexOf('.xls') > -1  || node.href.indexOf('.rm') > -1 || node.href.indexOf('.xml') >-1))
		{
			title = getWebtrendsTitle(node);
			uri =  node.pathname;	
			//alert('\nDCS.dcsuri:'+uri + '\nWT.ti:'+title +'\nDCSext.keywords'+getMetaContent('LW.keywords')+'\nDCSext.search_type:'+getMetaContent('LW.source'));
			if(getMetaContent('LW.keywords') != undefined)
				dcsMultiTrack('DCS.dcsquery',query,'WT.ti',title,'DCSext.keywords',getMetaContent('LW.keywords'),'DCSext.search_type',getMetaContent('LW.source'));
			else
				dcsMultiTrack('DCS.dcsquery',query,'WT.ti',title);
		}
			
		
	} catch(exception) { handleException(exception) }
} 


function getWebtrendsTitle(node)
{
	var title;
	if(title == null || title == '')
	 	title = node.title;
	if(title == null || title == '')
		title = node.alt;
	if(title == null || title == '')
	{	
		var img = node.getElementsByTagName("IMG");
		if(img != undefined)
		{
			title = node.alt;
		}
	}
	if(title == null || title == '')
	{
		title = node.innerHTML;//textContent doesn't work with safari
		if(title.indexOf('<')>-1 )
			title = title.substring(0,title.indexOf('<'));
	}		
	if(title == null || title == '')
		title = 'unknow';
	return title				
	
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
        if (taglines) {
        this.homeOption.activate(e);
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

function email(obfuscatedEmail) {
    document.location = obfuscatedEmail.replace(/\|/g,'');
    return false;
}