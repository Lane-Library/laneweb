(function() {
    var gImages=[],
        gIndex=0,
        DCS={},
        WT={},
        DCSext={},
        gDomain="irt-sdc.stanford.edu",
        gDcsId="dcssi6l0t1000004z9mg95sop_9v3k",

   dcsVar = function(){
        var i, dCurrent=new Date();
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
//        if (window.ActiveXObject){
            //gFV is not set, it appears that this script expects it to be set to the
            //flash version in a separate script element, which we have never done
            //so have never recorded the flash version for ie
//            if ((typeof(gFV)!="undefined")&&(gFV.length>0)){
//                WT.fi="Yes";
//                WT.fv=gFV;
//            }
//        }
//        else 
        if (navigator.plugins&&navigator.plugins.length){
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
    },


    A = function(N,V){
        if (encodeURIComponent) {
            return "&"+N+"="+encodeURIComponent(V);
        }
        return "&"+N+"="+escape(V);
    },
    
    dcsCreateImage = function(dcsSrc){
        gImages[gIndex]=new Image();
        gImages[gIndex].src=dcsSrc;
        gIndex++;
    },

    dcsMeta = function(){
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
    },

    dcsTag = function(){
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
    },
    
    dcsMultiTrack = function(args) {
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
        };
    dcsVar();
    dcsMeta();
    dcsTag();
    
    Y.on("trackable", function(link, event) {
        if (link.get("trackable")) {
            var trackingData = link.get("trackingData"),
                args = [];
            if (trackingData.host !== undefined) {
                args.push('DCS.dcssip');
                args.push(trackingData.host);
            }
            if (trackingData.path !== undefined) {
                args.push('DCS.dcsuri');
                args.push(trackingData.path);
            }
            if (trackingData.query !== undefined) {
                args.push('DCS.dcsqry');
                args.push(trackingData.query);
            }
            if (!(/\.html$/).test(trackingData.path) || trackingData.external) {
                args.push('DCS.dcsref');
                args.push(document.location.toString());
            }
            if (trackingData.title !== undefined) {
                args.push('WT.ti');
                args.push(trackingData.title);
            }
            if (trackingData.searchTerms !== undefined) {
                args.push('DCSext.keywords');
                args.push(trackingData.searchTerms);
            }
            if (trackingData.searchSource !== undefined) {
                args.push('DCSext.search_type');
                args.push(trackingData.searchSource);
            }
            if (trackingData.external) {
                args.push('DCSext.offsite_link');
                args.push('1');
            }
            dcsMultiTrack.call(dcsMultiTrack, args);
        }
    });
})();
