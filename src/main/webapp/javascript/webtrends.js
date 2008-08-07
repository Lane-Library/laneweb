// ****************    Add by IRT          **********************//



YAHOO.util.Event.addListener(document, 'click', webtrendsOnClick);


function webtrendsOnClick(e) {
    var target, node, redirectUrl;
        target = (e.srcElement) ? e.srcElement : e.target;
        node = getNode(target);
        if(node !== null)
        {
            redirectUrl = webtrendsProcess(node);
            if(redirectUrl   && e.button =="0"  && e.returnValue )//to give some time to send the request for webtrends
            {
                redirectUrl = redirectUrl.replace("'","\\'");
                target = node.target;
                if (target && '' != target && YAHOO.env.ua.webkit) {// safari doesn't not open a new window in a method call by a setTimeout

                    window.open(redirectUrl, target);
                } else {
                    setTimeout("redirect('" + redirectUrl + "','" + target + "')", 200);
                }
                YAHOO.util.Event.preventDefault(e);
            }
        }
}


function redirect(redirectUrl, target)
{
    if (target && '' != target) {
        window.open(redirectUrl, target);
    } else {
        window.location = redirectUrl;
    }
}

function getNode(node)
{
    var ancestorNode;
    if (node.tagName == "IMG" || node.tagName == "A" || node.tagName == "AREA") {
        return node;
    }
    ancestorNode = YAHOO.util.Dom.getAncestorBy(node, getNodeByNames);
    if (ancestorNode !== null) {
        return ancestorNode;
    }
    return null;     
}


function getNodeByNames(node)
{
    if (node.tagName == "A" || node.tagName == "AREA") {
        return true;
    }
    return false;
}

function webtrendsProcess(node){
        var redirectUrl, title, host, affiliation = getMetaContent('WT.seg_1'),
            offsite, uri = '/', query, proxyUrl = 'http://laneproxy.stanford.edu/login',
            href, anchorNode;
        if(node.hostname !== undefined)
        {
            host = node.hostname;
            if (host.indexOf(':') > -1) {//safari return port
                host = host.substring(0, host.indexOf(':'));
            }
        }
        if(node.tagName == "IMG")
        {
            anchorNode = node.parentNode;
            if (anchorNode !== undefined) {
                node = anchorNode;
            }
        }
        if((node.tagName == "A" || node.tagName == "AREA") && (host && node.href  &&  "" != node.href &&  "/" != node.href) )//for anchor tag 
        {
            if(host != getMetaContent("LW.host") || node.href.indexOf("/secure/login.html?url=") >-1 || node.href.indexOf("/secure/login.html?user=") >-1)
            {
                href = node.href;
                if (href.indexOf(proxyUrl) > -1) {
                    href = href.substring(href.indexOf('url=') + 4, href.length);
                }
                if (href.indexOf('://') > -1) {
                    href = href.substring(href.indexOf('://') + 3, href.length);
                }
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
                if(host != getMetaContent("LW.host") ||node.href.indexOf("/secure/login.html?url=") >-1 || node.href.indexOf("/secure/login.html?user=") >-1)    
                {
                    offsite = 1;
                    redirectUrl = node.href;
                }
                title = getWebtrendsTitle(node);
                 dcsMultiTrack('DCS.dcssip', href,'DCS.dcsuri',uri,'DCS.dcsquery',query,'WT.ti',title,'DCSext.keywords',getMetaContent('LW.searchTerms'),'DCSext.search_type',getMetaContent('LW.source'),'DCSext.offsite_link',offsite,'WT.seg_1',affiliation);
                 if(window.pageTracker !== undefined){
                    window.pageTracker._trackPageview('/OFFSITE/' + title);
                }
             }
        }
        
        if((node.href !== undefined) && host == getMetaContent("LW.host") &&
            (node.href.indexOf('.pdf') > -1 || node.href.indexOf('.camv') >-1 || node.href.indexOf('.smil') >-1 || node.href.indexOf('.doc') >-1 
          || node.href.indexOf('.ppt') > -1 || node.href.indexOf('.xls') > -1  || node.href.indexOf('.rm') > -1 || node.href.indexOf('.xml') >-1
          || node.href.indexOf('.wmv') >-1))
        {
            title = getWebtrendsTitle(node);
            uri =  node.pathname;    
            dcsMultiTrack('DCS.dcsuri',uri,'WT.ti',title,'DCSext.keywords',getMetaContent('LW.searchTerms'),'DCSext.search_type',getMetaContent('LW.source'),'WT.seg_1',affiliation);
            if(window.pageTracker !== undefined){
                window.pageTracker._trackPageview('/ONSITE/' + title + '/' + uri);
            }
        }
        
        return redirectUrl; 
        
} 


function getWebtrendsTitle(node)
{
    var title, img;
    if (title === null || title === '') {
        title = node.title;
    }
    if (title === null || title === '') {
        title = node.alt;
    }
    if(title === null || title === '')
    {    
        img = node.getElementsByTagName("IMG");
        if(img !== undefined)
        {
            title = node.alt;
        }
    }
    if(title === null || title === '')
    {
        title = node.innerHTML;//textContent doesn't work with safari
        if (title.indexOf('<') > -1) {
            title = title.substring(0, title.indexOf('<'));
        }
    }        
    if (title === null || title === '') {
        title = 'unknown';
    }
    return title;                  
}




//*****************     End add by IRT  *******************//

var gTimeZone = -8,
    gImages= [],
    gIndex=0,
    DCS= {},
    WT= {},
    DCSext= {},
    gDomain="irt-sdc.stanford.edu",
    gDcsId="dcssi6l0t1000004z9mg95sop_9v3k";

function dcsMultiTrack() {
    var i, dCurrent;
    for (i=0;i<arguments.length;i=i+2) {
        if (arguments[i].indexOf('WT.')===0 ) {
            if (arguments[i + 1] != "undefined") {
                WT[arguments[i].substring(3)] = arguments[i + 1];
            }
        }
        if (arguments[i].indexOf('DCS.')===0 ) {
            if (arguments[i + 1] != "undefined") {
                DCS[arguments[i].substring(4)] = arguments[i + 1];
            }
        }
        if (arguments[i].indexOf('DCSext.')===0) {
            if (arguments[i + 1] != "undefined") {
                DCSext[arguments[i].substring(7)] = arguments[i + 1];
            }
        }
    }
    dCurrent=new Date();
    DCS.dcsdat=dCurrent.getTime();
    dcsTag();
}

function dcsVar(){
    var dCurrent=new Date(), i;
    WT.tz=dCurrent.getTimezoneOffset()/60*-1;
    if (WT.tz===0){
        WT.tz="0";
    }
    WT.bh=dCurrent.getHours();
    WT.ul=navigator.appName=="Netscape"?navigator.language:navigator.userLanguage;
    if (typeof(screen)=="object"){
        WT.cd=navigator.appName=="Netscape"?screen.pixelDepth:screen.colorDepth;
        WT.sr=screen.width+"x"+screen.height;
    }
    if (typeof(navigator.javaEnabled())=="boolean"){
        WT.jo=navigator.javaEnabled()?"Yes":"No";
    }
    if (document.title){
        WT.ti=document.title;
    }
    WT.js="Yes";
    if (typeof(gVersion)!="undefined"){
        WT.jv=gVersion;
    }
    if (document.body&&document.body.addBehavior){
        document.body.addBehavior("#default#clientCaps");
        if (document.body.connectionType){
            WT.ct=document.body.connectionType;
        }
        document.body.addBehavior("#default#homePage");
        WT.hp=document.body.isHomePage(location.href)?"1":"0";
    }
    if (parseInt(navigator.appVersion, 10)>3){
        if ((navigator.appName=="Microsoft Internet Explorer")&&document.body){
            WT.bs=document.body.offsetWidth+"x"+document.body.offsetHeight;
        }
        else if (navigator.appName=="Netscape"){
            WT.bs=window.innerWidth+"x"+window.innerHeight;
        }
    }
    WT.fi="No";
    if (!window.ActiveXObject && navigator.plugins&&navigator.plugins.length){
        for (i=0;i<navigator.plugins.length;i++){
            if (navigator.plugins[i].name.indexOf('Shockwave Flash')!=-1){
                WT.fi="Yes";
                WT.fv=navigator.plugins[i].description.split(" ")[2];
                break;
            }
        }
    }
    WT.sp="Lane";
    DCS.dcsdat=dCurrent.getTime();
    DCS.dcssip=window.location.hostname;
    DCS.dcsuri=window.location.pathname;
    if (window.location.search){
        DCS.dcsqry=window.location.search;
    }
    if ((window.document.referrer!=="")&&(window.document.referrer!="-")){
        if (!(navigator.appName=="Microsoft Internet Explorer"&&parseInt(navigator.appVersion, 10)<4)){
            DCS.dcsref=window.document.referrer;
        }
    }
}


function A(N,V){
    return "&"+N+"="+escape(V);
}

function dcsCreateImage(dcsSrc){
    if (document.images){
        gImages[gIndex]=new Image();
        gImages[gIndex].src=dcsSrc;
        gIndex++;
    }
}

function dcsMeta(){
    var elems, meta, i;
    if (document.all){
        elems=document.all.tags("meta");
    }
    else if (document.documentElement){
        elems=document.getElementsByTagName("meta");
    }
    if (typeof(elems)!="undefined"){
        for (i=1;i<=elems.length;i++){
            meta=elems.item(i-1);
            if (meta.name){
                if (meta.name.indexOf('WT.')===0){
                    WT[meta.name.substring(3)]=meta.content;
                }
                else if (meta.name.indexOf('DCSext.')===0){
                    DCSext[meta.name.substring(7)]=meta.content;
                }
                else if (meta.name.indexOf('DCS.')===0){
                    DCS[meta.name.substring(4)]=meta.content;
                }
            }
        }
    }
}

function dcsTag(){
    var N, P="http"+(window.location.protocol.indexOf('https:')===0?'s':'')+"://"+gDomain+(gDcsId===""?'':'/'+gDcsId)+"/dcs.gif?";
    for (N in DCS){
        if (DCS[N]) {
            P+=A(N,DCS[N]);
        }
    }
    for (N in WT){
        if (WT[N]) {
            P+=A("WT."+N,WT[N]);
        }
    }
    for (N in DCSext){
        if (DCSext[N]) {
            P+=A(N,DCSext[N]);
        }
    }
    if (P.length>2048&&navigator.userAgent.indexOf('MSIE')>=0){
        P=P.substring(0,2040)+"&WT.tu=1";
    }
    dcsCreateImage(P);
}


dcsVar();
dcsMeta();
dcsTag();
