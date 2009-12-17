if(typeof LANE=="undefined"||!LANE){var LANE={}
}LANE.namespace=function(){var A=arguments,E=null,C,B,D;
for(C=0;
C<A.length;
C=C+1){D=A[C].split(".");
E=LANE;
for(B=(D[0]=="LANE")?1:0;
B<D.length;
B=B+1){E[D[B]]=E[D[B]]||{};
E=E[D[B]]
}}return E
};
LANE.core=LANE.core||function(){var B=YAHOO.util.Event,A={};
B.addListener(this,"load",function(){var E=document,D,C;
D=E.getElementsByTagName("meta");
for(C=0;
C<D.length;
C++){A[D[C].getAttribute("name")]=D[C].getAttribute("content")
}B.addListener(E,"mouseover",function(G){var F=G.srcElement||G.target;
while(F){if(F.activate){F.activate(G)
}F=F.parentNode
}});
B.addListener(E,"mouseout",function(G){var F=G.srcElement||G.target;
while(F){if(F.deactivate){F.deactivate(G)
}F=F.parentNode
}});
B.addListener(E,"click",function(I){var F=I.srcElement||I.target,G,H;
while(F){if(F.clicked){F.clicked(I)
}F=F.parentNode
}if(LANE.tracking){LANE.tracking.trackEvent(I);
if(YAHOO.env.ua.webkit&&LANE.tracking.isTrackable(I)){F=I.target;
G=F;
while(G){if(G.clicked!==undefined){return 
}G=G.parentNode
}while(F){if(F.href&&(!F.rel&&!F.target)){H=function(){window.location=F.href
};
YAHOO.util.Event.preventDefault(I);
setTimeout(H,200);
break
}F=F.parentNode
}}}})
});
return{getMetaContent:function(C){return A[C]===undefined?undefined:A[C]
},log:function(E,C,D){YAHOO.log(E,C,D);
if(encodeURIComponent){E=encodeURIComponent(E)
}else{E=escape(E)
}YAHOO.util.Connect.asyncRequest("GET","/././javascriptLogger?"+E)
},importNode:function(G,D){var F,E,C;
if(G.nodeType==1){F=document.createElement(G.nodeName);
for(E=0;
E<G.attributes.length;
E++){C=G.attributes[E];
if(C.nodeValue!==undefined&&C.nodeValue!==""){F.setAttribute(C.name,C.nodeValue);
if(C.name=="class"){F.className=C.nodeValue
}}}}else{if(G.nodeType==3){F=document.createTextNode(G.nodeValue)
}}if(D&&G.hasChildNodes()){for(E=0;
E<G.childNodes.length;
E++){F.appendChild(LANE.core.importNode(G.childNodes[E],true))
}}return F
}}
}();LANE.search=LANE.search||function(){var H=document,B,I,J,F,L=YAHOO.util.Event,G=false,M,K,A,D=document.getElementById("searchIndicator"),C={startSearch:function(){if(!B.q.value){throw ("nothing to search for")
}G=true;
D.style.visibility="visible"
},stopSearch:function(){G=false;
D.style.visibility="hidden"
},isSearching:function(){return G
},getSearchString:function(){if(M===undefined){if(K===undefined){this.getEncodedSearchString()
}M=decodeURIComponent(K)
}return M
},getEncodedSearchString:function(){var N,O,P,E;
if(K===undefined){N=location.search.substring(1);
O=N.split("&");
for(E=0;
E<O.length;
E++){P=O[E].split("=");
if(P[0]=="q"){K=P[1];
break
}}if(K===undefined){K=""
}}return K
},getSearchSource:function(){var N,O,P,E;
if(A===undefined){N=location.search.substring(1);
O=N.split("&");
for(E=0;
E<O.length;
E++){P=O[E].split("=");
if(P[0]=="source"){A=P[1];
break
}}if(A===undefined){A=""
}}return A
},setSearchSource:function(N){for(var E=0;
E<J.options.length;
E++){if(J.options[E].value==N){J.selectedIndex=E;
break
}}}};
L.addListener(this,"load",function(){B=H.getElementById("searchForm");
if(B){I=H.getElementById("searchSubmit");
I.activate=function(E){this.src=this.src.replace("search_btn.gif","search_btn_f2.gif")
};
I.deactivate=function(E){this.src=this.src.replace("search_btn_f2.gif","search_btn.gif")
};
L.addListener(B,"submit",function(N){try{C.startSearch()
}catch(E){alert(E);
L.preventDefault(N)
}});
J=H.getElementById("searchSelect");
if(J){F=J.options[J.selectedIndex];
L.addListener(J,"change",function(){if(this.options[this.selectedIndex].disabled){this.selectedIndex=F.index
}else{F=this.options[this.selectedIndex]
}})
}}});
return C
}();LANE.tracking=function(){var C=[],A=function(F){var H=F.title,D,E=0,G;
if(F.rel&&F.rel.indexOf("popup")===0){G=F.rel.split(" ");
if(G[1]=="local"||G[1]=="faq"){H="YUI Pop-up ["+G[1]+"]"
}}if(!H){H=F.alt
}if(!H){D=F.getElementsByTagName("IMG");
if(D){for(E=0;
E<D.length;
E++){if(D[E].alt){H=D[E].alt;
break
}}}}if(!H){H=F.innerHTML;
if(H&&H.indexOf("<")>-1){H=H.substring(0,H.indexOf("<"))
}}if(H){H=H.replace(/\s+/g," ").replace(/^\s|\s$/g,"")
}if(!H){H="unknown"
}return H
},B=function(D){var F=D.srcElement||D.target,L,M,J,H,K,I,G,E;
if(D.type=="click"){if(F.nodeName!="A"){E=F.getElementsByTagName("a");
if(E.length>0){F=E[0]
}}while(F&&F.nodeName!="A"){F=F.parentNode;
if(F===null){throw"not trackable"
}}if(F.pathname.indexOf("secure/login.html")>-1||F.host.indexOf("laneproxy")===0){L=(F.search.substring(F.search.indexOf("//")+2));
if(L.indexOf("/")>-1){M=L.substring(L.indexOf("/"));
if(M.indexOf("?")>-1){M=M.substring(0,M.indexOf("?"))
}L=L.substring(0,L.indexOf("/"))
}else{M="/"
}J="";
H=true
}else{if(F.rel&&(F.rel.indexOf("popup local")===0||F.rel.indexOf("popup faq")===0)){L=document.location.host;
M=document.location.pathname;
J=document.location.search
}else{L=F.host;
if(L.indexOf(":")>-1){L=L.substring(0,L.indexOf(":"))
}M=F.pathname;
H=L!=document.location.host;
J=H?"":F.search
}}}if(M.indexOf("/")!==0){M="/"+M
}K=A(F);
if(LANE.search&&LANE.search.getSearchString()){I=LANE.search.getSearchString();
G=LANE.search.getSearchSource()
}return{host:L,path:M,query:J,title:K,searchTerms:I,searchSource:G,external:H}
};
return{addTracker:function(D){if(!D||D.track===undefined){throw"tracker does not implement track()"
}C.push(D)
},trackEvent:function(E){var D;
if(this.isTrackable(E)){D=B(E);
this.track(D)
}},track:function(D){for(var E=0;
E<C.length;
E++){C[E].track(D)
}},isTrackable:function(G){var I=G.srcElement||G.target,F,E,D,H;
E=document.location.host;
if(E.indexOf(":")>-1){E=E.substring(0,E.indexOf(":"))
}if(G.type=="click"){if(I.className=="eLibraryTab"){return true
}F=I;
while(F&&F.nodeName!="A"){F=F.parentNode
}if(F){if(F.rel&&F.rel.indexOf("popup ")===0){H=F.rel.split(" ");
if(H[1]=="local"||H[1]=="faq"){return true
}}D=F.host;
if(D.indexOf(":")>-1){D=D.substring(0,D.indexOf(":"))
}if(D==E){if((/secure\/login.html/).test(F.pathname)&&F.search&&F.search.indexOf("url=")>-1){return true
}if((/\.html$/).test(F.pathname)||(/\/$/).test(F.pathname)){while(F!==null){if(F.clicked&&!F.rel){return true
}F=F.parentNode
}return false
}return true
}return true
}}return false
}}
}();
LANE.track=LANE.tracking;(function(){var J=[],N=0,M={},C={},I={},H="irt-sdc.stanford.edu",E="dcssi6l0t1000004z9mg95sop_9v3k";
function B(){var O,A=new Date();
C.tz=A.getTimezoneOffset()/60*-1;
if(C.tz===0){C.tz="0"
}C.bh=A.getHours();
C.ul=navigator.appName=="Netscape"?navigator.language:navigator.userLanguage;
if(typeof (screen)=="object"){C.cd=navigator.appName=="Netscape"?screen.pixelDepth:screen.colorDepth;
C.sr=screen.width+"x"+screen.height
}if(typeof (navigator.javaEnabled())=="boolean"){C.jo=navigator.javaEnabled()?"Yes":"No"
}if(document.title){C.ti=document.title.replace(/\s+/g," ")
}C.js="Yes";
if(typeof (gVersion)!="undefined"){C.jv=gVersion
}if(parseInt(navigator.appVersion,10)>3){if((navigator.appName=="Microsoft Internet Explorer")&&document.body){C.bs=document.body.offsetWidth+"x"+document.body.offsetHeight
}else{if(navigator.appName=="Netscape"){C.bs=window.innerWidth+"x"+window.innerHeight
}}}C.fi="No";
if(window.ActiveXObject){}else{if(navigator.plugins&&navigator.plugins.length){for(O=0;
O<navigator.plugins.length;
O++){if(navigator.plugins[O].name.indexOf("Shockwave Flash")!=-1){C.fi="Yes";
C.fv=navigator.plugins[O].description.split(" ")[2];
break
}}}}C.sp="Lane";
M.dcsdat=A.getTime();
M.dcssip=window.location.hostname;
M.dcsuri=window.location.pathname;
if(window.location.search){M.dcsqry=window.location.search
}if((window.document.referrer!=="")&&(window.document.referrer!="-")){if(!(navigator.appName=="Microsoft Internet Explorer"&&parseInt(navigator.appVersion,10)<4)){M.dcsref=window.document.referrer
}}}function F(O,A){if(encodeURIComponent){return"&"+O+"="+encodeURIComponent(A)
}return"&"+O+"="+escape(A)
}function G(A){J[N]=new Image();
J[N].src=A;
N++
}function L(){var O,A,P;
if(document.all){A=document.all.tags("meta")
}else{if(document.documentElement){A=document.getElementsByTagName("meta")
}}if(typeof (A)!="undefined"){for(O=1;
O<=A.length;
O++){P=A.item(O-1);
if(P.name){if(P.name.indexOf("WT.")===0){C[P.name.substring(3)]=P.content
}else{if(P.name.indexOf("DCSext.")===0){I[P.name.substring(7)]=P.content
}else{if(P.name.indexOf("DCS.")===0){M[P.name.substring(4)]=P.content
}}}}}}}function D(){var O,A="http"+(window.location.protocol.indexOf("https:")===0?"s":"")+"://"+H+(E===""?"":"/"+E)+"/dcs.gif?";
for(O in M){if(M[O]){A+=F(O,M[O])
}}for(O in C){if(C[O]){A+=F("WT."+O,C[O])
}}for(O in I){if(I[O]){A+=F(O,I[O])
}}if(A.length>2048&&navigator.userAgent.indexOf("MSIE")>=0){A=A.substring(0,2040)+"&WT.tu=1"
}G(A)
}function K(O){var P,A=new Date();
for(P=0;
P<O.length;
P=P+2){if(O[P].indexOf("WT.")===0){if(O[P+1]!="undefined"){C[O[P].substring(3)]=O[P+1]
}}if(O[P].indexOf("DCS.")===0){if(O[P+1]!="undefined"){M[O[P].substring(4)]=O[P+1]
}}if(O[P].indexOf("DCSext.")===0){if(O[P+1]!="undefined"){I[O[P].substring(7)]=O[P+1]
}}}M.dcsdat=A.getTime();
D()
}B();
L();
D();
LANE.tracking.addTracker({track:function(A){var O=[];
if(A.host!==undefined){O.push("DCS.dcssip");
O.push(A.host)
}if(A.path!==undefined){O.push("DCS.dcsuri");
O.push(A.path)
}if(A.query!==undefined){O.push("DCS.dcsqry");
O.push(A.query)
}if(!(/\.html$/).test(A.path)||A.external){O.push("DCS.dcsref");
O.push(document.location.toString())
}if(A.title!==undefined){O.push("WT.ti");
O.push(A.title)
}if(A.searchTerms!==undefined){O.push("DCSext.keywords");
O.push(A.searchTerms)
}if(A.searchSource!==undefined){O.push("DCSext.search_type");
O.push(A.searchSource)
}if(A.external){O.push("DCSext.offsite_link");
O.push("1")
}K.call(K,O)
}})
})();YAHOO.util.Event.addListener(this,"load",function(){var A=(("https:"==document.location.protocol)?"https://ssl.":"http://www.");
YAHOO.util.Get.script(A+"google-analytics.com/ga.js",{onSuccess:function(){var D=document.location.host,B,C=function(E){if(encodeURIComponent){return encodeURIComponent(E)
}return escape(E)
};
if(_gat!==undefined){if("lane.stanford.edu"==D){B=_gat._getTracker("UA-3202241-2")
}else{B=_gat._getTracker("UA-3203486-2")
}B._setDomainName(".stanford.edu");
B._trackPageview();
B._setVar(LANE.core.getMetaContent("WT.seg_1"));
LANE.tracking.addTracker({track:function(E){if(E.external){B._trackPageview("/OFFSITE/"+C(E.title))
}else{B._trackPageview("/ONSITE/"+C(E.title)+"/"+E.path)
}}})
}}})
});(function(){LANE.namespace("search.eresources");
LANE.search.eresources=function(){var B;
return{setCurrentResult:function(C){B=C
},getCurrentResult:function(){return B
}}
}();
YAHOO.util.Event.addListener(this,"load",function(){var D=document.getElementById("eLibraryTabs"),F,E,C,G,B;
if(D){B=document.getElementById("eLibrarySearchResults");
F=D.getElementsByTagName("li");
for(E=0;
E<F.length;
E++){if(F[E].className=="eLibraryTab"||F[E].className=="eLibraryTabActive"){G=F[E].id.substring(0,F[E].id.indexOf("Tab"));
if(G){F[E].result=new A(G,F[E],B);
if(F[E].className=="eLibraryTabActive"){content=[];
for(C=0;
C<B.childNodes.length;
C++){content[C]=B.childNodes[C]
}F[E].result.setContent(content);
LANE.search.eresources.setCurrentResult(F[E].result)
}F[E].activate=function(H){if(this.className!="eLibraryTabActive"){this.style.textDecoration="underline";
this.style.cursor="pointer"
}};
F[E].deactivate=function(H){this.style.textDecoration="none";
this.style.cursor="default"
};
F[E].clicked=function(H){this.result.show();
if(YAHOO.util.History){try{YAHOO.util.History.navigate("aTab",this.result._type)
}catch(I){}}YAHOO.util.Event.preventDefault(H)
}
}}}}});
function A(D,C,B){this._type=D;
this._tab=C;
this._container=B;
this._url="/././plain/search/"+this._type+".html?source="+this._type+"&q="+LANE.search.getEncodedSearchString();
this._state="initialized";
this._callback={success:function(I){var E,H,G,F;
E=I.argument.result;
H=I.responseXML.getElementsByTagName("body")[0].childNodes;
G=[];
for(F=0;
F<H.length;
F++){G[F]=LANE.core.importNode(H[F],true)
}E.setContent(G);
LANE.search.eresources.getCurrentResult().hide();
LANE.search.eresources.setCurrentResult(E);
E.show()
},failure:function(){},argument:{result:this}};
A.prototype.setContent=function(F){var E;
if(this._content===undefined){this._content=F
}else{this._content=this._content.concat(F)
}this._count=0;
for(E=0;
E<this._content.length;
E++){if(this._content[E].getElementsByTagName){this._count=this._content[E].getElementsByTagName("dt").length;
if(this._count>0){break
}}}this.setTabCount(this._count);
this._state="searched"
};
A.prototype.setTabCount=function(F){var E=this._tab.getElementsByTagName("span")[0];
E.textContent=F
};
A.prototype.show=function(){var E;
if(this._state=="initialized"){this.getContent();
LANE.search.startSearch()
}else{if(this._state=="searching"){alert("search in progress")
}else{LANE.search.eresources.getCurrentResult().hide();
LANE.search.eresources.setCurrentResult(this);
this._tab.className="eLibraryTabActive";
for(E=0;
E<this._content.length;
E++){this._container.appendChild(this._content[E])
}LANE.search.stopSearch()
}}LANE.search.setSearchSource(this._type)
};
A.prototype.getContent=function(){var E;
if(this._state=="initialized"){this._state="searching";
E=YAHOO.util.Connect.asyncRequest("GET",this._url,this._callback)
}else{if(this._state=="searched"){this.show()
}else{if(this._state=="searching"){alert("search in progress")
}}}};
A.prototype.hide=function(){while(this._container.childNodes.length>0){this._container.removeChild(this._container.lastChild)
}this._tab.className="eLibraryTab"
}
}})();if(LANE.search.getEncodedSearchString()){YAHOO.util.Event.onAvailable("spellCheck",function(){YAHOO.util.Connect.asyncRequest("GET","/././apps/spellcheck/json?q="+LANE.search.getEncodedSearchString(),{success:function(D){var C=YAHOO.lang.JSON.parse(D.responseText),B,A;
if(C.suggestion){B=document.getElementById("spellCheck");
LANE.search.popin.fire(B);
A=B.getElementsByTagName("a")[0];
A.href="/search.html?source="+LANE.search.getSearchSource()+"&q="+encodeURIComponent(C.suggestion);
A.innerHTML=C.suggestion
}}})
})
};(function(){var B=LANE.search.getEncodedSearchString(),D=new Date().getTime(),E=function(G){var J=YAHOO.lang.JSON.parse(G.responseText),M,N,H,O,K,F,I,L;
for(M=0;
M<J.results.tabs.length;
M++){N=J.results.tabs[M].resource;
H=document.getElementById(N+"Tab");
if(H!==null){O=H.getElementsByTagName("span")[0];
K=J.results.tabs[M].hits;
if(O!==undefined&&K!==""){O.innerHTML=K
}}}F=2000;
I=(new Date().getTime())-D;
L=J.results.status;
if(L!="successful"&&(I<=60*1000)){if(I>20*1000){F=10000
}setTimeout(C,F)
}},A={success:E},C=function(){YAHOO.util.Connect.asyncRequest("GET","/././apps/search/tabs/json?q="+B+"&rd="+Math.random(),A)
};
if(B){YAHOO.util.Event.onAvailable("eLibraryTabs",C)
}})();if(LANE.search.getEncodedSearchString()){YAHOO.util.Event.onAvailable("findIt",function(){YAHOO.util.Connect.asyncRequest("GET","/././apps/sfx/json?q="+LANE.search.getEncodedSearchString(),{success:function(C){var A=YAHOO.lang.JSON.parse(C.responseText),D,B;
if(A.result){B=document.getElementById("findIt");
D=B.getElementsByTagName("a")[0];
D.href=A.openurl;
D.innerHTML=A.result;
LANE.search.popin.fire(B)
}}})
})
};if(LANE.search.getEncodedSearchString()){YAHOO.util.Event.onAvailable("queryMapping",function(){YAHOO.util.Connect.asyncRequest("GET","/././apps/querymap/json?q="+LANE.search.getEncodedSearchString(),{success:function(F){var C,E,B,D,A=document.getElementById("queryMapping");
LANE.search.querymap=YAHOO.lang.JSON.parse(F.responseText);
if(LANE.search.querymap.resourceMap){LANE.search.querymap.getResultCounts=function(){var G="/././apps/search/proxy/json?q="+LANE.search.getEncodedSearchString(),H;
for(H=0;
H<LANE.search.querymap.resourceMap.resources.length;
H++){if(!LANE.search.querymap.resourceMap.resources[H].status){G+="&r="+LANE.search.querymap.resourceMap.resources[H].id
}}G+="&rd="+Math.random();
YAHOO.util.Connect.asyncRequest("GET",G,{success:function(N){var L=YAHOO.lang.JSON.parse(N.responseText),J=LANE.search.querymap.resourceMap.resources,K,M=false,I;
for(K=0;
K<J.length;
K++){if(!J[K].status){I=L.resources[J[K].id];
if(!I.status){M=true
}J[K].status=I.status;
if(I.status=="successful"){J[K].anchor.parentNode.appendChild(document.createTextNode(": "+I.hits+" "))
}J[K].anchor.href=I.url
}}if(M){setTimeout(LANE.search.querymap.getResultCounts,2000)
}LANE.search.popin.fire(A)
}})
};
for(D=0;
D<LANE.search.querymap.resourceMap.resources.length;
D++){LANE.search.querymap.resourceMap.resources[D].status="";
E=document.createElement("span");
C=document.createElement("a");
C.title="QueryMapping: "+LANE.search.querymap.resourceMap.resources[D].label;
E.appendChild(C);
C.appendChild(document.createTextNode(LANE.search.querymap.resourceMap.resources[D].label));
A.appendChild(E);
LANE.search.querymap.resourceMap.resources[D].anchor=C
}if(document.getElementById("queryMappingDescriptor")){document.getElementById("queryMappingDescriptor").appendChild(document.createTextNode(LANE.search.querymap.resourceMap.descriptor))
}B=document.createElement("img");
B.style.display="none";
B.src="/././graphics/spacer.gif?log=QM&d="+LANE.search.querymap.resourceMap.descriptor+"&k="+LANE.search.getEncodedSearchString();
A.appendChild(B);
LANE.search.querymap.getResultCounts()
}}})
})
};(function(){LANE.namespace("search.popin");
LANE.search.popin=new YAHOO.util.CustomEvent("onPopin");
var B=["spellCheck","queryMapping","findIt"],A=function(F,E){var C,G=99,D=YAHOO.util.Dom.get(B);
for(C=0;
C<D.length;
C++){if(D[C]!==null&&D[C].style.display=="inline"){G=C
}}for(C=0;
C<D.length;
C++){if(D[C]!==null){if(E[0].id===D[C].id&&C<=G){G=C;
D[C].style.display="inline"
}else{if(C>G){D[C].style.display="none"
}}}}};
LANE.search.popin.subscribe(A)
})();(function(){LANE.namespace("search.metasearch");
var D=new Date().getTime(),B,C=true,A=function(F){var H=["cro_","mdc_","ovid-"],E,G=true;
for(E=0;
E<H.length;
E++){if(F.id.match(H[E])&&B.match(H[E])){G=false
}}if(G){B+="&r="+F.id
}};
YAHOO.util.Event.onDOMReady(function(){if(LANE.search.getEncodedSearchString()&&YAHOO.util.Dom.getElementsByClassName("metasearch","a").length>0){LANE.search.metasearch.getResultCounts=function(){B="/././apps/search/proxy/json?q="+LANE.search.getEncodedSearchString();
var E=YAHOO.util.Dom.getElementsByClassName("metasearch","a",document,A);
B+="&rd="+Math.random();
if(LANE.search.getSearchSource().match(/^(clinical|peds|research|pharmacy|history|test|textbooks)$/)!==null){C=false
}YAHOO.util.Connect.asyncRequest("GET",B,{success:function(M){var K=YAHOO.lang.JSON.parse(M.responseText),J,L=false,G,H,F,I;
for(J=0;
J<E.length;
J++){G=K.resources[E[J].id];
if(G!==undefined){if(!G.status){L=true
}if(G.url&&(C&&G.status=="successful")||(!C&&G.status&&G.status!="running")){G.name=(E[J].innerHTML)?E[J].innerHTML:"";
E[J].setAttribute("href",G.url);
if(YAHOO.env.ua.ie){E[J].innerHTML=G.name
}E[J].setAttribute("target","_blank");
YAHOO.util.Dom.removeClass(E[J],"metasearch");
H=document.createElement("span");
if(E[J].id=="uptodate"&&G.hits==150){G.hits="50+"
}H.appendChild(document.createTextNode(": "+G.hits+" "));
E[J].parentNode.appendChild(H);
if(C){if(G.status=="failed"||G.status=="canceled"){YAHOO.util.Dom.removeClass(E[J],"metasearch")
}}else{if(parseInt(G.hits,10)>0||G.status!="successful"){YAHOO.util.Dom.getAncestorByClassName(E[J].id,"searchCategory").getElementsByTagName("h3")[0].style.display="block";
YAHOO.util.Dom.getAncestorByTagName(E[J].id,"li").style.display="block"
}else{if(parseInt(G.hits,10)===0){YAHOO.util.Dom.addClass(YAHOO.util.Dom.getAncestorByTagName(E[J].id,"li"),"zeroHit")
}}}}}}F=2000;
I=(new Date().getTime())-D;
if(L&&(I<=60*1000)){if(I>20*1000){F=10000
}setTimeout(LANE.search.metasearch.getResultCounts,F)
}else{LANE.search.stopSearch()
}}})
};
YAHOO.util.Event.onAvailable("toggleZeros",function(){YAHOO.util.Event.addListener("toggleZeros","click",function(H){var E=document.getElementById("toggleZeros"),J=YAHOO.util.Dom.getElementsByClassName("zeroHit"),F=YAHOO.util.Dom.getElementsByClassName("searchCategory"),I,G,K;
if(E.innerHTML.match(/Show/)){I="block";
E.innerHTML=E.innerHTML.replace("Show","Hide")
}else{I="none";
E.innerHTML=E.innerHTML.replace("Hide","Show")
}for(G=0;
G<J.length;
G++){YAHOO.util.Dom.setStyle(J[G],"display",I)
}for(K=0;
K<F.length;
K++){if(YAHOO.util.Dom.getElementsByClassName("zeroHit","",F[K]).length==F[K].getElementsByTagName("li").length){YAHOO.util.Dom.setStyle(YAHOO.util.Dom.getFirstChild(F[K]),"display",I)
}}YAHOO.util.Event.preventDefault(H)
})
});
LANE.search.metasearch.getResultCounts();
document.getElementById("searchIndicator").style.visibility="visible"
}})
})();(function(){var B=LANE.search.getEncodedSearchString(),F=YAHOO.util.Dom,E=YAHOO.util.Event,C=YAHOO.util.History,A=function(G){var H=document.getElementById(G+"Tab").result;
if(H._state=="initialized"){H.show()
}else{if(H._state=="searched"){LANE.search.eresources.getCurrentResult().hide();
LANE.search.eresources.setCurrentResult(H);
H.show()
}else{}}},D=function(){var G=C.getBookmarkedState("aTab")||C.getQueryStringParameter("source");
C.onReady(function(){A(C.getCurrentState("aTab"))
});
C.register("aTab",G,A);
C.initialize("yui-history-field","yui-history-iframe")
};
if(B&&F.inDocument("yui-history-field")&&F.inDocument("yui-history-iframe")){E.addListener(this,"load",D)
}})();(function(){YAHOO.util.Event.addListener(window,"load",function(){var A,E,F,B,D,C;
A=YAHOO.util.Dom.getElementsByClassName("tooltips");
for(D=0;
D<A.length;
D++){E=A[D].childNodes;
for(C=0;
C<E.length;
C++){if(E[C].nodeType==1){F=E[C].id.replace(/Tooltip$/,"");
if(F&&YAHOO.util.Dom.inDocument(F)){document.getElementById(F).trackable=true;
B=E[C].style.width||"25%";
new YAHOO.widget.Tooltip(YAHOO.util.Dom.generateId(),{context:F,width:B,autodismissdelay:60000,text:E[C].innerHTML})
}}}}})
})();(function(){YAHOO.util.Event.addListener(window,"load",function(){var A,B,E,D,C;
B=function(){var F=document.createElement("div");
F.setAttribute("id","popupContainer");
document.body.appendChild(F);
A=new YAHOO.widget.Panel("popupContainer",{underlay:"none",close:true,visible:false,draggable:false,constraintoviewport:true,modal:false})
};
E=function(I,F,J,H){var G=(I.length*7>250)?I.length*7:250;
A.setHeader(I);
A.setBody(F);
A.cfg.setProperty("width",G+"px");
A.cfg.setProperty("X",J);
A.cfg.setProperty("Y",H);
A.render();
A.show()
};
C=function(G,H,I,F){if(D!==undefined&&!D.closed){D.close()
}if(H=="fullscreen"){I=screen.availWidth;
F=screen.availHeight
}var J="";
if(H=="standard"){J="resizable,toolbar=yes,location=yes,scrollbars=yes,menubar=yes"
}if(H=="console"||H=="fullscreen"){J="resizable,toolbar=no,location=no,scrollbars=no"
}if(H=="console-with-scrollbars"){J="resizable,toolbar=no,location=no,scrollbars=yes"
}if(I&&F){J+=",width="+I+",height="+F
}D=window.open(G,"newWin",J);
D.focus()
};
LANE.namespace("popups");
LANE.popups.initialize=function(I){var G,H,F;
H=I.getElementsByTagName("a");
for(G=0;
G<H.length;
G++){if(H[G].rel){F=H[G].rel.split(" ");
if(F[0]=="popup"){if(F[1]=="standard"||F[1]=="console"||F[1]=="console-with-scrollbars"||F[1]=="fullscreen"){H[G].clicked=function(K){var J=this.rel.split(" ");
YAHOO.util.Event.preventDefault(K);
C(this.href,J[1],J[2],J[3])
}
}if(F[1]=="local"){if(!A){B()
}H[G].clicked=function(L){var O,N,M,J,K=YAHOO.util.Event;
K.preventDefault(L);
O=this.rel.split(" ")[2];
N=(document.getElementById(O))?document.getElementById(O):0;
M=(N.getAttribute("title"))?N.getAttribute("title"):"";
J=(document.getElementById(O))?document.getElementById(O).innerHTML:"";
E(M,J,K.getPageX(L),K.getPageY(L))
}
}else{if(F[1]=="faq"){if(!A){B()
}H[G].clicked=function(J){var K=this.rel.split(" ")[2];
YAHOO.util.Event.preventDefault(J);
YAHOO.util.Connect.asyncRequest("GET","/././content/popup.html?id="+K,{success:function(P){var R=P.argument.id,Q=P.argument.X,O=P.argument.Y,M=P.responseXML.documentElement,N=M.getElementsByTagName("a")[0].firstChild.data,L=M.getElementsByTagName("dd")[0].firstChild.data+'&nbsp;<a href="/././howto/index.html?id='+R+'">More</a>';
P.argument.showPanel(N,L,Q,O)
},argument:{showPanel:E,X:YAHOO.util.Event.getPageX(J),Y:YAHOO.util.Event.getPageY(J),id:K}})
}
}}}}}};
LANE.popups.initialize(document)
})
})();(function(){if(YAHOO.env.ua.ie){YAHOO.util.Event.addListener(this,"load",function(){var A=YAHOO.env.ua.ie,L,F,J=document,H,G=J.getElementById("sb-bot"),B=J.getElementById("otherPortalOptions"),C=J.getElementById("legend-drop-down"),I=J.getElementById("shd"),E=J.getElementById("searchSelect"),D=J.getElementById("searchSubmit");
if(A<=6){if(B&&G){B.activate=function(){G.style.zIndex=-1;
this.className="hover"
};
B.deactivate=function(){G.style.zIndex=1;
this.className=""
}
}if(C){C.activate=function(){this.className="hover"
};
C.deactivate=function(){this.className=""
}
}if(I){var K=I.getElementsByTagName("UL");
for(F=0;
F<K.length;
F++){if(F==0){K[F].parentNode.activate=function(){E.style.visibility="hidden";
this.className="hover"
};
K[F].parentNode.deactivate=function(){this.className="";
E.style.visibility="visible"
}
}else{K[F].parentNode.activate=function(){this.className="hover"
};
K[F].parentNode.deactivate=function(){this.className=""
}
}}}if(D){D.activate=function(){this.className="hover"
};
D.deactivate=function(){this.className=""
}
}L=["ft","gft"];
for(F=0;
F<L.length;
F++){H=J.getElementById(L[F]);
if(H){H.getElementsByTagName("li")[0].style.border="none"
}}H=J.getElementById("shd");
if(H){H.getElementsByTagName("li")[0].style.backgroundImage="none"
}}else{if(A>=7&&A<8){if(B&&G){if(B){B.activate=function(){G.style.zIndex=-1
};
B.deactivate=function(){G.style.zIndex=1
}
}}}}})
}})();(function(){LANE.namespace("forms");
LANE.forms=function(){return{validateFormOnSubmit:function(D){var A,C,E,G,F,B=this.validationPatterns["require-one-of"];
this.isValid=true;
for(C=this.elements.length-1;
C>0;
C--){if(this.elements[C].type!==undefined&&this.elements[C].type.match(/^(select-one|text|textarea)$/)&&this.elements[C].onchange==LANE.forms.validateFieldOnChange){this.elements[C].onchange();
if(YAHOO.util.Dom.hasClass(this.elements[C],"invalid-field")){this.isValid=false;
A=this.elements[C]
}}if(this.elements[C].type!==undefined&&this.elements[C].type=="radio"&&this.validationPatterns[this.elements[C].name]!==undefined){F=this.elements[this.elements[C].name];
for(E=0;
E<F.length;
E++){F[E].onchange()
}if(YAHOO.util.Dom.hasClass(this.elements[C],"invalid-field")){this.isValid=false;
A=F[0]
}}}if(B!==undefined){this.isValid=false;
for(G=0;
G<B.split(",").length;
G++){A=this.elements[B.split(",")[0]];
if(this.elements[B.split(",")[G]].value){if(this.elements[B.split(",")[G]].onchange!==undefined){this.elements[B.split(",")[G]].onchange()
}if(!YAHOO.util.Dom.hasClass(this.elements[B.split(",")[G]],"invalid-field")){this.isValid=true
}}}}if(this.isValid===true){if(this.elements["validation-patterns"]){this.removeChild(this.elements["validation-patterns"])
}if(this.delayedOnsubmit){this.delayedOnsubmit()
}return true
}else{alert("Some of the fields that are required have not been filled out.\nPlease check highlighted fields below and try again.");
A.focus();
YAHOO.util.Event.preventDefault(D);
return false
}},validateFieldOnChange:function(){var B=this,A=(B.type=="select-one")?B.options[B.selectedIndex].value:this.value,E,D=false,C;
if(B.type=="radio"){E=YAHOO.util.Dom.getAncestorByTagName(B,"form").elements[B.name];
for(C=0;
C<E.length;
C++){if(E[C].checked===true){A=E[C].value;
D=true
}}B=E[0]
}if(A.search(B.pattern)==-1||(B.type=="radio"&&D===false)){YAHOO.util.Dom.addClass(B,"invalid-field");
if(LANE.forms.getFieldLabel(B)){YAHOO.util.Dom.addClass(LANE.forms.getFieldLabel(B),"invalid-label")
}}else{YAHOO.util.Dom.removeClass(B,"invalid-field");
if(LANE.forms.getFieldLabel(B)){YAHOO.util.Dom.removeClass(LANE.forms.getFieldLabel(B),"invalid-label")
}}},getFieldLabel:function(C){var B=document.getElementsByTagName("label"),A;
for(A=0;
A<B.length;
A++){if(B[A].getAttributeNode("for")&&C.name==B[A].getAttributeNode("for").value){return B[A]
}}}}
}();
YAHOO.util.Event.addListener(window,"load",function(){var E,D,C,G,F,A,B=document.getElementsByName("validation-patterns");
for(D=0;
D<B.length;
D++){E=YAHOO.util.Dom.getAncestorByTagName(B[D],"form");
F=false;
A=B[D].value.split("::");
E.validationPatterns={};
for(G=0;
G<A.length;
G++){E.validationPatterns[A[G].split("=")[0]]=A[G].split("=")[1]
}for(C=0;
C<E.elements.length;
C++){if(E.elements[C].type!==undefined&&E.validationPatterns[E.elements[C].name]!==undefined&&E.elements[C].type.match(/^(radio|select-one|text|textarea)$/)){F=true;
E.elements[C].pattern=(E.validationPatterns[E.elements[C].name]!==undefined)?E.validationPatterns[E.elements[C].name]:"\\S";
E.elements[C].onchange=E.elements[C].onblur=LANE.forms.validateFieldOnChange;
if(E.elements[C].type=="radio"){E.elements[C].onmouseup=LANE.forms.validateFieldOnChange
}}}if(F===true||E.validationPatterns["require-one-of"]!==undefined){if(E.onsubmit){E.delayedOnsubmit=E.onsubmit;
E.onsubmit=""
}YAHOO.util.Event.addListener(E,"submit",LANE.forms.validateFormOnSubmit)
}}})
})();(function(){var B=false,C=YAHOO.util.Connect,A=YAHOO.util.Event,H=YAHOO.util.History;
YAHOO.util.Event.onAvailable("bassettContent",function(){YAHOO.util.Get.script("/javascript/noversion/bubbling-min.js",{onSuccess:function(){YAHOO.util.Get.script("/javascript/noversion/accordion-min.js",{onSuccess:function(){I()
}})
}})
});
function I(){var J=document.getElementById("accordion");
if(J){E(J);
E(document.getElementById("bassettContent"));
D()
}}function E(J){var L,M,K;
if(J){L=J.getElementsByTagName("a");
for(M=0;
M<L.length;
M++){if(L[M].rel===null||L[M].rel===""){L[M].clicked=function(N){if(this.id=="diagram-choice"){B=true
}if(this.id=="photo-choice"){B=false
}K=G(this.href);
if(H){try{H.navigate("bassett",K)
}catch(O){F(K)
}}else{F(K)
}A.stopEvent(N)
}
}}}}function F(K){K="/././plain/bassett/raw".concat(K);
function J(O){var N,L,M;
L=document.getElementById("bassettContent");
N=O.responseXML.getElementsByTagName("body")[0].childNodes;
while(L.childNodes.length>0){L.removeChild(L.firstChild)
}for(M=0;
M<N.length;
M++){L.appendChild(LANE.core.importNode(N[M],true))
}E(L);
LANE.popups.initialize(L)
}C.asyncRequest("GET",K,{success:J})
}function G(J){var K;
J=J.replace("search.html","/bassett/bassettsView.html");
J=J.substr(J.indexOf("/bassett/")+8);
J=J.split("?");
if(J.length==1){K=J[0]
}if(J.length>1){K=J[0]+"?"+J[1]
}if(B){K=K+"&t=diagram"
}return K
}function D(){var J=H.getBookmarkedState("bassett")||G(window.location.toString());
F(J);
H.register("bassett",J,F);
H.initialize("yui-history-field-bassett","yui-history-iframe")
}})();(function(){YAHOO.util.Event.addListener(window,"load",function(){var G,F,D,B,C,A,E;
D=document.getElementById("searchForm");
B=document.getElementById("searchTerms");
C=document.getElementById("searchSelect");
E=function(K,J){var I,H={};
I=J[2];
H.title=C.options[C.selectedIndex].value+"--suggest-selection-event";
H.path=I[0];
LANE.tracking.track(H);
LANE.search.startSearch();
D.submit()
};
G=new YAHOO.widget.DS_XHR("/././apps/suggest/json",["suggest"]);
G.responseType=YAHOO.widget.DS_XHR.TYPE_JSON;
G.scriptQueryParam="q";
G.connTimeout=3000;
G.maxCacheEntries=100;
F=new YAHOO.widget.AutoComplete(B,"acResults",G);
F.minQueryLength=3;
F.useShadow=true;
F.animHoriz=false;
F.animVert=false;
F.autoHighlight=false;
F.itemSelectEvent.subscribe(E);
YAHOO.util.Event.addListener(B,"focus",function(){F.minQueryLength=3;
if(C.options[C.selectedIndex].value.match(/all|ej|database|book|software|video|cc|lanesite|bassett|history/)){F.dataSource.scriptQueryAppend="l="+C.options[C.selectedIndex].value
}else{if(C.options[C.selectedIndex].value.match(/clinical|peds|anesthesia|cardiology|emergency|hematology|internal-medicine|lpch-cerner|pharmacy|pulmonary/)){F.dataSource.scriptQueryAppend="l=mesh-di"
}else{if(C.options[C.selectedIndex].value.match(/research/)){F.dataSource.scriptQueryAppend="l=mesh"
}else{F.minQueryLength=-1
}}}})
})
})();