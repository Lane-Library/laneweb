// ****************    Add by IRT          **********************//


//
//YAHOO.util.Event.addListener(document, 'click', webtrendsOnClick);
//
//
//function webtrendsOnClick(e) {
//        var target = (e.srcElement) ? e.srcElement : e.target;
//        var node = getNode(target);
//        if(node != null)
//        {
//            var redirectUrl = webtrendsProcess(node);
//            if(redirectUrl   && e.button =="0"  && e.returnValue )//to give some time to send the request for webtrends
//            {
//                redirectUrl = redirectUrl.replace("'","\\'");
//                var target = node.target;
//                if(target && '' != target && YAHOO.env.ua.webkit)// safari doesn't not open a new window in a method call by a setTimeout
//                    window.open(redirectUrl, target);
//                else
//                     setTimeout("redirect('"+redirectUrl+"','"+target+"')",200);
//                YAHOO.util.Event.preventDefault(e);
//            }
//        }
//}
//
//
//function redirect(redirectUrl, target)
//{
//    if(target && '' != target)
//        var newWindow = window.open(redirectUrl, target);
//    else
//         window.location = redirectUrl;     
//}
//
//function getNode(node)
//{
//    if(node.tagName == "IMG" || node.tagName == "A" || node.tagName == "AREA")
//        return node;
//    var ancestorNode = YAHOO.util.Dom.getAncestorBy(node, getNodeByNames);
//    if(ancestorNode != null)
//        return ancestorNode; 
//    return null;     
//}
//
//
//function getNodeByNames(node)
//{
//    if(node.tagName == "A" || node.tagName == "AREA")
//        return true;
//    else
//        return false;
//}
//
//function webtrendsProcess(node){
//        var redirectUrl;
//        var title;
//        var host;
//        var affiliation = getMetaContent('WT.seg_1');
//        if(node.hostname != undefined)
//        {
//            host = node.hostname;
//            if(host.indexOf(':')>-1)//safari return port
//                host = host.substring(0,host.indexOf(':'));
//        }
//        if(node.tagName == "IMG")
//        {
//            var anchorNode = node.parentNode;
//            if(anchorNode != undefined)
//                node = anchorNode;
//        }
//        if((node.tagName == "A" || node.tagName == "AREA") && (host && node.href  &&  "" != node.href &&  "/" != node.href) )//for anchor tag 
//        {
//            if(host != getMetaContent("LW.host") || node.href.indexOf("/secure/login.html?url=") >-1 || node.href.indexOf("/secure/login.html?user=") >-1)
//            {
//                var offsite;
//                var uri= '/';
//                var query;
//                var proxyUrl = 'http://laneproxy.stanford.edu/login';
//                var href = node.href;
//                if(href.indexOf(proxyUrl) > -1 )
//                    href = href.substring( href.indexOf('url=')+4 , href.length);
//                if(href.indexOf('://')>-1)        
//                    href = href.substring(href.indexOf('://')+3, href.length);
//                if(href.indexOf('?')>-1)
//                {
//                    query = href.substring(href.indexOf('?')+1, href.length);
//                    href = href.substring(0,href.indexOf('?'));
//                }
//                if(href.indexOf('/')>-1)
//                {    
//                    uri =  href.substring(href.indexOf('/'), href.length);    
//                    href = href.substring(0,href.indexOf('/'));
//                }
//                if(host != getMetaContent("LW.host") ||node.href.indexOf("/secure/login.html?url=") >-1 || node.href.indexOf("/secure/login.html?user=") >-1)    
//                {
//                    offsite = 1;
//                    redirectUrl = node.href;
//                }
//                title = getWebtrendsTitle(node);
//                //alert('DCS.dcssip:'+ href +'\nDCS.dcsuri:'+uri + '\nDCS.dcsquery:' +query  +'\nWT.ti:'+title +'\nDCSext.keywords:'+getMetaContent('LW.searchTerms')+'\nDCSext.search_type:'+getMetaContent('LW.source')+'\nDCSext.offsite_link:1');
//                 dcsMultiTrack('DCS.dcssip', href,'DCS.dcsuri',uri,'DCS.dcsquery',query,'WT.ti',title,'DCSext.keywords',getMetaContent('LW.searchTerms'),'DCSext.search_type',getMetaContent('LW.source'),'DCSext.offsite_link',offsite,'WT.seg_1',affiliation);
//                 //alert('/OFFSITE/' + title);
//                 if(window.pageTracker != undefined){
//                    window.pageTracker._trackPageview('/OFFSITE/' + title);
//                }
//             }
//        }
//                return {
//                host:'www.google.com',
//                uri:'/foo/bar.html',
//                query:'',
//                title:getTrackedTitle(node),
//                searchTerms:'',
//                searchSource:'',
//                external:false
//            };
//        
//        if((node.href != undefined) && host == getMetaContent("LW.host") &&
//            (node.href.indexOf('.pdf') > -1 || node.href.indexOf('.camv') >-1 || node.href.indexOf('.smil') >-1 || node.href.indexOf('.doc') >-1 
//          || node.href.indexOf('.ppt') > -1 || node.href.indexOf('.xls') > -1  || node.href.indexOf('.rm') > -1 || node.href.indexOf('.xml') >-1
//          || node.href.indexOf('.wmv') >-1))
//        {
//            title = getWebtrendsTitle(node);
//            uri =  node.pathname;    
//            //alert('\nDCS.dcsuri:'+uri + '\nWT.ti: '+title +'\nDCSext.keywords: '+getMetaContent('LW.searchTerms')+'\nDCSext.search_type: '+getMetaContent('LW.source'));
//            dcsMultiTrack('DCS.dcsuri',uri,'WT.ti',title,'DCSext.keywords',getMetaContent('LW.searchTerms'),'DCSext.search_type',getMetaContent('LW.source'),'WT.seg_1',affiliation);
//            //alert('/ONSITE/' + title + uri);
//            if(window.pageTracker != undefined){
//                window.pageTracker._trackPageview('/ONSITE/' + title + '/' + uri);
//            }
//        }
//        
//        return redirectUrl; 
//        
//} 
//
//
//function getWebtrendsTitle(node)
//{
//    var title;
//    if(title == null || title == '')
//         title = node.title;
//    if(title == null || title == '')
//        title = node.alt;
//    if(title == null || title == '')
//    {    
//        var img = node.getElementsByTagName("IMG");
//        if(img != undefined)
//        {
//            title = node.alt;
//        }
//    }
//    if(title == null || title == '')
//    {
//        title = node.innerHTML;//textContent doesn't work with safari
//        if(title.indexOf('<')>-1 )
//            title = title.substring(0,title.indexOf('<'));
//    }        
//    if(title == null || title == '')
//        title = 'unknown';
//    return title                    
//}

//LANE.namespace('webtrends');

(function() {
    var gTimeZone = -8,
    	gImages=[],
    	gIndex=0,
    	DCS={},
    	WT={},
    	DCSext={},
    	gQP=[],
        //gDomain = 'lane-local.stanford.edu',
    	//gDomain='irt-lane-stage.stanford.edu',
        gDomain="irt-sdc.stanford.edu",
    	gDcsId="dcssi6l0t1000004z9mg95sop_9v3k";

    function dcsVar(){
        var i, pos, front, end, dCurrent=new Date();
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
            WT.ti=document.title.replace(/\s+/g,' ');
        }
        WT.js="Yes";
        if (typeof(gVersion)!="undefined"){
            WT.jv=gVersion;
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
        if (window.ActiveXObject){
            if ((typeof(gFV)!="undefined")&&(gFV.length>0)){
                WT.fi="Yes";
                WT.fv=gFV;
            }
        }
        else if (navigator.plugins&&navigator.plugins.length){
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
            if (gQP.length>0){
                for (i=0;i<gQP.length;i++){
                    pos=DCS.dcsqry.indexOf(gQP[i]);
                    if (pos!=-1){
                        front=DCS.dcsqry.substring(0,pos);
                        end=DCS.dcsqry.substring(pos+gQP[i].length,DCS.dcsqry.length);
                        DCS.dcsqry=front+end;
                    }
                }
            }
        }
        if ((window.document.referrer!=="")&&(window.document.referrer!="-")){
            if (!(navigator.appName=="Microsoft Internet Explorer"&&parseInt(navigator.appVersion, 10)<4)){
                DCS.dcsref=window.document.referrer;
            }
        }
    }


    function A(N,V){
        if (encodeURIComponent) {
            return "&"+N+"="+encodeURIComponent(V);
        }
        return "&"+N+"="+escape(V);
    }
    
    function dcsCreateImage(dcsSrc){
        gImages[gIndex]=new Image();
        gImages[gIndex].src=dcsSrc;
        gIndex++;
    }

    function dcsMeta(){
        var i, elems, meta;
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
    
    function dcsMultiTrack(args) {
            var i, dCurrent = new Date();
            for (i=0;i<args.length;i=i+2) {
                if (args[i].indexOf('WT.')===0 ) {
                    if (args[i + 1] != "undefined") {
                        WT[args[i].substring(3)] = args[i + 1];
                    }
                }
                if (args[i].indexOf('DCS.')===0 ) {
                    if (args[i + 1] != "undefined") {
                        DCS[args[i].substring(4)] = args[i + 1];
                    }
                }
                if (args[i].indexOf('DCSext.')===0) {
                    if (args[i + 1] != "undefined") {
                        DCSext[args[i].substring(7)] = args[i + 1];
                    }
                }
            }
            DCS.dcsdat=dCurrent.getTime();
            dcsTag();
        }
    dcsVar();
    dcsMeta();
    dcsTag();
    
    LANE.track.addTracker({
        track: function(trackable){
            var args = [], t = trackable;
            if (t.host !== undefined) {
                args.push('DCS.dcssip');
                args.push(t.host);
            }
            if (t.path !== undefined) {
                args.push('DCS.dcsuri');
                args.push(t.path);
            }
            if (t.query !== undefined) {
                args.push('DCS.dcsquery');
                args.push(t.query);
            }
            if (t.title !== undefined) {
                args.push('WT.ti');
                args.push(t.title);
            }
            if (t.searchTerms !== undefined) {
                args.push('DCSext.keywords');
                args.push(t.searchTerms);
            }
            if (t.searchSource !== undefined) {
                args.push('DCSext.search_type');
                args.push(t.searchSource);
            }
            if (t.external) {
                args.push('DCSext.offsite_link');
                args.push('1');
            }
            dcsMultiTrack.call(dcsMultiTrack, args);
        }
    });
})();
