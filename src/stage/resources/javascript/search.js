var date=new Date();
var startTime=date.getTime();
var GLOBALS=new Object();
GLOBALS.basePath="/./.";
GLOBALS.httpRequestInterval="1500";
GLOBALS.incrementalSearchWait="2500";
GLOBALS.metasearchProxyPath=GLOBALS.basePath+"/content/search-proxy";
GLOBALS.needsProxy=getMetaContent(document,"lw_proxyLinks");
GLOBALS.proxyPrefix="http://laneproxy.stanford.edu/login?url=";
GLOBALS.searchPath=GLOBALS.basePath+"/search.html";
window.onerror=errorLogger;
function errorLogger(_1,_2,_3){
var _4=document.createElement("img");
_4.src=GLOBALS.basePath+"/javascript/ErrorLogger.js?url="+_2+"&line="+_3+"&msg="+_1;
_4.className="hide";
return false;
}
var eLibraryTabLabels=new Array("All","eJournals","Databases","eBooks","medCalcs","Lane Services");
var eLibraryTabIDs=new Array("all","ej","database","book","cc","faq");
var eLibraryResultCounts=[];
var eLibraryActiveTab=null;
function geteLibraryTabCount(_5){
var _6=document.getElementById(_5).getElementsByTagName("dt");
return _6.length;
}
function initeLibraryTabs(){
for(var i=0;i<eLibraryTabIDs.length;i++){
eLibraryResultCounts[eLibraryTabIDs[i]]=geteLibraryTabCount(eLibraryTabIDs[i]);
}
var _8="";
for(var i=0;i<eLibraryTabLabels.length;i++){
var _a="";
if(document.getElementById(eLibraryTabIDs[i]+"SearchTagline")){
_a=document.getElementById(eLibraryTabIDs[i]+"SearchTagline").innerHTML;
}
var _b="";
if(isDefined(window,"dcsMultiTrack")){
_b="dcsMultiTrack('WT.ti','Laneconnex search "+eLibraryTabIDs[i]+" tab','DCSext.keywords','cancer','DCSext.tab_view','"+eLibraryTabIDs[i]+"');";
}
_8=_8+"<div id=\""+eLibraryTabIDs[i]+"Tab\" class=\"eLibraryTab\" title=\""+_a+"\" name=\""+eLibraryTabIDs[i]+"\" onclick=\"javascript:showeLibraryTab('"+eLibraryTabIDs[i]+"');"+_b+"\">"+eLibraryTabLabels[i]+"<br /><span class=\"tabHitCount\">"+intToNumberString(eLibraryResultCounts[eLibraryTabIDs[i]])+"</span></div>";
}
document.getElementById("eLibraryTabs").innerHTML=_8;
}
function showeLibraryTab(_c){
if(!_c){
_c="all";
if(readCookie("LWeLibSource")&&eLibraryTabIDs.contains(_c)){
_c=readCookie("LWeLibSource");
}else{
if(eLibraryTabIDs.contains(getQueryContent("source"))){
_c=getQueryContent("source");
}
}
}
tabLinks=document.getElementById("eLibraryTabs").getElementsByTagName("div");
for(var i=0;i<tabLinks.length;i++){
if(tabLinks[i].getAttribute("id")==_c+"Tab"){
tabLinks[i].className="eLibraryTabActive";
}else{
tabLinks[i].className="eLibraryTab";
}
}
var _e=document.getElementById("eLibrarySearchResults");
var _f=document.getElementById("eLibrarySearchResults").getElementsByTagName("div");
for(var i=0;i<_f.length;i++){
if(_f[i].getAttribute("id")==_c){
_f[i].className="";
}else{
_f[i].className="hide";
}
}
searchFormSelect(_c);
eLibraryActiveTab=_c;
refreshPopInContent();
setCookie("LWeLibSource",_c);
}
var relevanceSortedResults;
function sorteLibraryResults(){
var _11=document.getElementById("eLibrarySearchResults");
var _12="";
if(_11.getAttribute("name")=="relevance-sort"){
_12="alpha-sort";
_11.innerHTML=relevanceSortedResults;
showeLibraryTab(eLibraryActiveTab);
}else{
_12="relevance-sort";
relevanceSortedResults=document.getElementById("eLibrarySearchResults").innerHTML;
for(var i=0;i<eLibraryTabIDs.length;i++){
var _14=eLibraryTabIDs[i];
var div=document.getElementById(_14);
var dl=document.getElementById(_14).getElementsByTagName("dl");
var _17=[];
var _18="";
for(var p=0;p<div.getElementsByTagName("dt").length;p++){
_17[p]=[div.getElementsByTagName("dt")[p].innerHTML,"<dt>"+div.getElementsByTagName("dt")[p].innerHTML+"</dt>"+"<dd>"+div.getElementsByTagName("dd")[p].innerHTML+"</dd>"];
}
_17=_17.sortByAlpha();
for(p=0;p<_17.length;p++){
_18=_18+_17[p][1];
}
var _1a=(dl.length)?dl[0].className:"";
div.innerHTML="<dl class=\""+_1a+"\">"+_18+"</dl>";
}
}
_11.setAttribute("name",_12);
setCookie("LWeLibNextSort",_12);
refreshPopInContent();
}
function refreshPopInContent(){
var _1b="";
if(document.getElementById("popInContent")){
document.getElementById("popInContent").className="hide";
}
if(eLibraryResultCounts[eLibraryActiveTab]!=0){
var _1c=new Array("Relevance","A-Z");
var _1d="";
if(document.getElementById("eLibrarySearchResults").getAttribute("name")=="relevance-sort"){
_1d="<option>"+_1c[0]+"</option><option selected=\"true\">"+_1c[1]+"</option>";
}else{
_1d="<option selected=\"true\">"+_1c[0]+"</option><option>"+_1c[1]+"</option>";
}
_1b+="Sorted by <select name=\"sortBy\" onchange=\"sorteLibraryResults();\" style=\"font-size: 95%; font-weight: 400;\">"+_1d+"</select>&nbsp;&nbsp;&nbsp;&nbsp;";
}else{
if(document.getElementById(eLibraryActiveTab+"TabZeroResultsText")&&!(document.getElementById("sfxResults")&&(eLibraryActiveTab=="all"||eLibraryActiveTab=="ej"))){
_1b=document.getElementById(eLibraryActiveTab+"TabZeroResultsText").innerHTML;
}
}
if(document.getElementById("sfxResults")&&(eLibraryActiveTab=="all"||eLibraryActiveTab=="ej")){
_1b+=document.getElementById("sfxResults").innerHTML;
}
if(document.getElementById("spellResults")){
_1b=document.getElementById("spellResults").innerHTML;
}
if(_1b!=""){
document.getElementById("popInContent").innerHTML=_1b;
document.getElementById("popInContent").className="popInContent";
}
if(document.getElementById(eLibraryActiveTab+"TabTipText")){
var _1e=document.getElementById(eLibraryActiveTab+"TabTipText").innerHTML;
document.getElementById("tabTip").innerHTML=_1e;
document.getElementById("tabTip").className="tabTip";
}else{
document.getElementById("tabTip").className="hide";
}
}
function IOClient(){
}
IOClient.prototype={request:null,type:null,url:null,init:function(_1f,url){
this.type=_1f;
this.url=url.replace(/amp;/g,"");
if(navigator.userAgent.indexOf("Mac")>-1&&(navigator.appVersion.indexOf("MSIE 5")>-1||navigator.appVersion.indexOf("MSIE 6")>-1)){
alert("Unsupported browser");
window.location.href=GLOBALS.basePath+"/howto/index.html?id=_869";
}
if(window.XMLHttpRequest){
this.request=new XMLHttpRequest();
}else{
if(window.ActiveXObject){
this.request=new ActiveXObject("Msxml2.XMLHTTP");
}else{
alert("unsupported browser");
}
}
},get:function(){
var _21=this;
this.request.onreadystatechange=function(){
_21.readyStateChange(_21);
};
this.request.open("GET",this.url,true);
if(this.delay){
setTimeout("self.request.send(null);",this.delay);
}else{
this.request.send(null);
}
},readyStateChange:function(_22){
if(_22.request.readyState==4){
if(_22.request.status==200){
this.process();
}
}
},process:function(){
switch(this.type){
case "elib-meta":
var _23=this.request.responseXML.documentElement;
var _24=_23.getElementsByTagName("engine");
var _25=_23.getAttribute("id");
var _26=_23.getAttribute("status");
var _27=_23.getElementsByTagName("query")[0].firstChild.data;
var _28=(_23.getElementsByTagName("spell").length>0)?_23.getElementsByTagName("spell")[0].firstChild.data:null;
if(_28&&!document.getElementById("spellResults")){
var _29="Did you mean: <a href=\""+GLOBALS.searchPath+"?keywords="+_28+"\"><i><strong>"+_28+"</strong></i></a><br />";
var _2a=document.getElementsByTagName("body").item(0);
var _2b=document.createElement("div");
_2b.className="hide";
_2b.innerHTML=_29;
_2b.setAttribute("id","spellResults");
_2a.appendChild(_2b);
refreshPopInContent();
if(document.getElementById("tabTip")){
document.getElementById("tabTip").className="hide";
}
}
if(_26=="successful"||_26=="running"){
var _2c=0;
var _2d=0;
var _2e=0;
var _2f=0;
document.getElementById("clinicalMetaCount").parentNode.href=GLOBALS.basePath+"/search.html?source=clinical&id="+_25+"&keywords="+keywords;
document.getElementById("researchMetaCount").parentNode.href=GLOBALS.basePath+"/search.html?source=research&id="+_25+"&keywords="+keywords;
for(var i=0;i<_24.length;i++){
var _31=_24[i].getElementsByTagName("resource");
for(var j=0;j<_31.length;j++){
if(_31[j].getElementsByTagName("hits").length>0&&_31[j].getElementsByTagName("hits")[0].firstChild.data>0){
if(GLOBALS.clinicalEngines.contains(_31[j].getAttribute("id"))){
_2e+=parseInt(_31[j].getElementsByTagName("hits")[0].firstChild.data);
_2c++;
}
if(GLOBALS.researchEngines.contains(_31[j].getAttribute("id"))){
_2f+=parseInt(_31[j].getElementsByTagName("hits")[0].firstChild.data);
_2d++;
}
}
var _33=_24[i].getAttribute("status");
if(_33=="successful"&&(parseInt(_2e)>0||parseInt(_2f)>0)){
document.getElementById("clinicalMetaCount").innerHTML=intToNumberString(_2c);
document.getElementById("researchMetaCount").innerHTML=intToNumberString(_2d);
}
if(document.getElementById(_31[j].getAttribute("id")+"SearchResults")&&_31[j].getElementsByTagName("hits").length>0){
var _34=document.getElementById(_31[j].getAttribute("id")+"SearchResults");
_34.getElementsByTagName("a")[0].innerHTML=_31[j].getElementsByTagName("description")[0].firstChild.data+"<br /><span class=\"tabHitCount\">"+intToNumberString(_31[j].getElementsByTagName("hits")[0].firstChild.data)+"</span>";
if(_31[j].getAttribute("id")=="google"){
document.getElementById(_31[j].getAttribute("id")+"SearchResults").className="metaSearchResultsRightCorner";
}else{
document.getElementById(_31[j].getAttribute("id")+"SearchResults").className="metaSearchResults";
}
}
}
}
}
var _35=new Date();
if(_35.getTime()-startTime>60*1000){
_26="halted";
}
if(_26=="running"){
Object.neweLibMetaRequest=new IOClient();
Object.neweLibMetaRequest.init("elib-meta",GLOBALS.metasearchProxyPath+"?id="+_25+"&secs="+_35.getSeconds());
setTimeout("Object.neweLibMetaRequest.get();",GLOBALS.httpRequestInterval);
return 0;
}
break;
case "incremental":
var dom=string2dom(this.request.responseText);
var _37=dom.documentElement;
var _38=_37.getElementsByTagName("div");
for(var d=0;d<_38.length;d++){
if(_38[d].getAttribute("id")=="incrementalSearchResults"){
var _3a=_38[d].getElementsByTagName("li");
break;
}
}
var _3b=document.getElementById("incrementalSearchResults").getElementsByTagName("li");
document.getElementById("incrementalSearchResults").className="unhide";
var _3c=getMetaContent(_37,"lw_searchParameters","status");
var _3d=getMetaContent(_37,"lw_searchParameters","query");
var _3e=document.getElementById("incrementalResultsProgressBar");
var _3f=document.getElementById("incrementalResultsDetails");
var _40=0;
var _41=0;
var _42=true;
for(var i=0;i<_3a.length;i++){
var _44=_3b[i].getElementsByTagName("a")[0];
var _45=_3a[i].getElementsByTagName("a")[0];
var _46=_45.getAttribute("type");
if(_46=="running"){
_42=false;
}else{
_41++;
}
if(_46=="running"||_3a[i].getElementsByTagName("span")[0].childNodes[0].nodeValue=="timed out"||_3a[i].getElementsByTagName("span")[0].childNodes[0].nodeValue==0){
_3b[i].className="hide";
}else{
_3b[i].parentNode.parentNode.getElementsByTagName("h3")[0].className="";
_3b[i].className="";
_40++;
}
if(_44.getAttribute("type")!=_46){
_44.setAttribute("type",_46);
if(_3a[i].getElementsByTagName("span").length>0){
var _47=_3a[i].getElementsByTagName("span")[0].childNodes[0].nodeValue;
if(_47=="0"&&_46=="successful"){
_47=" 0";
}
_3b[i].appendChild(document.createTextNode(_47));
}
}
}
var _48=new Date();
if(_48.getTime()-startTime>60*1000||haltIncremental){
_42=true;
}
if(!_42){
var _49=0;
if(_3a.length>0&&_41>0){
_49=100*(_41/_3a.length);
}else{
_49=1;
}
_3e.innerHTML="<table><tr><td nowrap>Still searching...</td><td nowrap><div style=\"position:relative;left:2px;top:2px;border:1px solid #b2b193; width:200px;\"><img width=\""+_49+"%\" height=\"15\" src=\""+GLOBALS.basePath+"/images/templates/default/incrementalResultsProgressBar.gif\" alt=\"progress bar\" /></div></td><td nowrap>&nbsp;"+_41+" of "+_3a.length+" sources searched. <a href=\"javascript:haltIncremental=true;void(0);\">Stop Search</a></td></tr></table>";
setTimeout("getIncrementalResults();",GLOBALS.httpRequestInterval);
return 0;
}else{
_3e.innerHTML="";
if(_3a.length>_40){
_3e.innerHTML="Results in <strong>"+_40+"</strong> of <strong>"+_3a.length+"</strong> sources for <strong>"+_3d+"</strong> [<a id=\"zerotoggle\" href=\"javascript:toggleIncrementalZeros('true');\">Show Details</a>]";
}else{
if(_3a.length==_40){
_3e.innerHTML="Results in <strong>"+_40+"</strong> of <strong>"+_3a.length+"</strong> sources contain <strong>"+_3d+"</strong>";
}
}
}
break;
case "sfx":
var _4a=this.request.responseXML.documentElement;
var _4b=_4a.getElementsByTagName("openurl")[0].firstChild.data;
var _4c=_4a.getElementsByTagName("result")[0].firstChild.data;
if(_4c!=0){
var _4d="";
if(isDefined(window,"dcsMultiTrack")){
var _4e=_4b.substring(_4b.indexOf("?")+1,_4b.length);
_4d="onclick=\"dcsMultiTrack('DCS.dcssip','sfx.stanford.edu','DCS.dcsuri','local','DCS.dcsquery','"+_4e+"','WT.ti','SFX','DCSext.keywords','"+keywords+"','DCSext.offsite_link','1');";
}
var _4f="FindIt@Stanford eJournal: <a target=\"_blank\" title=\"Fulltext access to "+_4c+"\" href=\""+_4b+"\" "+_4d+"><b>"+_4c.replace(/ \[.*\]/,"")+"</b></a><br />";
var _50=document.getElementsByTagName("body").item(0);
var _51=document.createElement("div");
_51.className="hide";
_51.innerHTML=_4f;
_51.setAttribute("id","sfxResults");
_50.appendChild(_51);
refreshPopInContent();
}
break;
}
}};
Array.prototype.contains=function(_52){
for(var i=0,elms=this.length;i<elms&&this[i]!==_52;i++){
}
return i<elms;
};
Array.prototype.sortByAlpha=function(){
var _54=[];
var _55=[];
var _56=/^(a|an|the|de|die|la|le|los|las|les) /;
for(var i=0;i<this.length;i++){
_54[_54.length]=this[i].toString().toLowerCase().replace(_56,"");
_55[this[i].toString().toLowerCase().replace(_56,"")]=i;
}
_54.sort();
var _58=[];
for(var i=0;i<_54.length;i++){
_58[i]=this[_55[_54[i]]];
}
return _58;
};
function isDefined(_5a,_5b){
return (typeof (eval(_5a)[_5b])=="undefined")?false:true;
}
function cleanKW(_5c){
_5c=escape(_5c.replace(/&amp;/g,"&"));
return _5c;
}
function getMetaContent(_5d,_5e,_5f,_60,_61){
var _62="";
if(!_60){
_60=";";
}else{
if(_60=="&amp;"){
_60="&";
}
}
if(!_61){
_61="=";
}
var _63=_5d.getElementsByTagName("meta");
for(var i=0;i<_63.length;i++){
if(_63[i].getAttribute("name")==_5e){
if(_5f){
var _65=[];
_65=_63[i].getAttribute("content").split(_60);
for(var y=0;y<_65.length;y++){
var _67=[];
_67=_65[y].split(_61);
if(_67[0]==_5f){
_62=_67[1];
}
}
}else{
_62=_63[i].getAttribute("content");
}
}
}
return _62;
}
function getQueryContent(_68,_69){
if(!_69){
_69=location.search;
}
var _6a=_6a+"=";
if(_69.length>0){
startParam=_69.indexOf(_6a);
if(startParam!=-1){
startParam+=_6a.length;
endParam=_69.indexOf("&",startParam);
if(endParam==-1){
endParam=_69.length;
}
return unescape(_69.substring(startParam,endParam));
}
}
return 0;
}
function intToNumberString(_6b){
_6b=_6b.toString();
var _6c=/(\d+)(\d{3})/;
while(_6c.test(_6b)){
_6b=_6b.replace(_6c,"$1"+","+"$2");
}
return _6b;
}
function toggleNode(_6d,_6e,_6f,_70,_71){
if(_6e.className!="hide"){
_6e.className="hide";
_6d.innerHTML=_6f;
if(_71){
document.getElementById(_71).className="";
}
}else{
_6e.className="";
_6d.innerHTML=_70;
if(_71){
document.getElementById(_71).className="hide";
}
}
}
function readCookie(_72){
var _73=_72+"=";
var _74=document.cookie.split(";");
for(var i=0;i<_74.length;i++){
var c=_74[i];
while(c.charAt(0)==" "){
c=c.substring(1,c.length);
}
if(c.indexOf(_73)==0){
var _77=c.substring(_73.length,c.length);
return _77;
}
}
return null;
}
function removeCookie(_78){
if(readCookie(_78)){
document.cookie=_78+"="+"; expires=Thu, 01-Jan-70 00:00:01 GMT";
return true;
}
return false;
}
function setCookie(_79,_7a){
document.cookie=_79+"="+_7a+"; path=/; ";
}
function searchFormSelect(_7b){
var _7c="searchForm";
if(typeof (_7b)=="number"){
document[_7c].source.selectedIndex=_7b;
}else{
for(var i=0;i<document[_7c].source.options.length;i++){
if(document[_7c].source.options[i].value==_7b||document[_7c].source.options[i].text.indexOf(_7b)>-1){
document[_7c].source.selectedIndex=i;
}
}
}
document[_7c].source.onchange();
}
function string2dom(_7e,_7f){
if(!_7f){
_7f="text/xml";
}
if(isDefined("window","DOMParser")){
var _80=new DOMParser();
return _80.parseFromString(_7e,_7f);
}else{
if(isDefined("window","ActiveXObject")){
var _81=new ActiveXObject("MSXML.DOMDocument");
if(_81){
_81.async=false;
_81.loadXML(_7e);
if(_81.parseError!=0){
alert("Document parse error\nCode:"+_81.parseError.errorCode+"\nLine:"+_81.parseError.line+"\nReason:"+_81.parseError.reason);
}
return _81;
}else{
alert("Error: can't create ActiveXObject('MSXML.DOMDocument') object");
}
}
}
return null;
}
var haltIncremental=false;
function getIncrementalResults(){
var id=getMetaContent(document,"lw_searchParameters","id");
var _83=getMetaContent(document,"lw_searchParameters","source");
var _84=new Date();
var url=GLOBALS.basePath+"/search.html?id="+id+"&source="+_83+"&secs="+_84.getSeconds();
var _86=new IOClient();
_86.init("incremental",url);
_86.get();
}
function toggleIncrementalZeros(_87){
var _88=document.getElementById("incrementalSearchResults").getElementsByTagName("h3");
var _89=document.getElementById("incrementalSearchResults").getElementsByTagName("li");
var _8a=document.getElementById("zerotoggle");
if(_87=="true"){
_8a.href="javascript:toggleIncrementalZeros('false');";
_8a.innerHTML="Hide Details";
for(var i=0;i<_88.length;i++){
if(_88[i].className=="hide"){
_88[i].className="unhide";
}
}
for(var i=0;i<_89.length;i++){
if(_89[i].className=="hide"){
_89[i].className="unhide";
}
}
}else{
if(_87=="false"){
_8a.href="javascript:toggleIncrementalZeros('true');";
_8a.innerHTML="Show Details";
for(var i=0;i<_88.length;i++){
if(_88[i].className=="unhide"){
_88[i].className="hide";
}
}
for(var i=0;i<_89.length;i++){
if(_89[i].className=="unhide"){
_89[i].className="hide";
}
}
}
}
}
var searching=false;
function submitSearch(){
var _8f="searchForm";
var _90=document[_8f].source.options[document[_8f].source.selectedIndex].value;
var _91=document[_8f].keywords.value;
var _92="Please enter one or more search terms.";
if(eLibraryTabIDs.contains(_90)){
setCookie("LWeLibSource",_90);
}
if(_91==""){
alert(_92);
return false;
}else{
if(_90.match(/(research|clinical|peds)/)){
var _93=GLOBALS.searchPath+"?source="+_90+"&keywords="+_91+"&w="+GLOBALS.incrementalSearchWait;
window.location=_93;
return false;
}else{
if(_90=="biomedsem"){
openSearchResult("http://med.stanford.edu/seminars/searchresults.jsp?searchString="+_91+"&Submit=Go");
return false;
}else{
if(_90=="catalog"){
var _94="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&SL=none&SAB1="+_91+"&BOOL1=all+of+these&FLD1=Keyword+Anywhere++%5BLKEY%5D+%28LKEY%29&GRP1=AND+with+next+set&SAB2=&BOOL2=all+of+these&FLD2=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&GRP2=AND+with+next+set&SAB3=&BOOL3=all+of+these&FLD3=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&CNT=50";
openSearchResult(_94);
return false;
}else{
if(_90=="google"){
openSearchResult("http://www.google.com/search?hl=en&q="+_91);
return false;
}else{
if(_90=="pubmed"){
openSearchResult("http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?otool=stanford&CMD=search&DB=PubMed&term="+_91);
return false;
}else{
if(_90=="stanford_who"){
openSearchResult("https://stanfordwho.stanford.edu/lookup?search="+_91);
return false;
}
}
}
}
}
}
}
if(searching){
alert("a search is already in progress");
return false;
}
searching=true;
return true;
}
var lastIndex=0;
function lastSelectValue(_95){
var val=_95.options[_95.selectedIndex].value;
if((val=="----------------")||(val=="")){
if(lastIndex){
_95.selectedIndex=lastIndex;
}else{
_95.selectedIndex=0;
}
}else{
lastIndex=_95.selectedIndex;
if(arguments.length>1&&arguments[1]=="portals"){
window.location=GLOBALS.basePath+_95.options[_95.selectedIndex].value;
}
}
}

