var date=new Date();
var startTime=date.getTime();
var GLOBALS=new Object();
GLOBALS.basePath='/./.';
GLOBALS.httpRequestInterval="1500";
GLOBALS.incrementalSearchWait="2500";
GLOBALS.metasearchProxyPath=GLOBALS.basePath+"/content/search-proxy";
GLOBALS.needsProxy=getMetaContent(document,"lw_proxyLinks");
GLOBALS.proxyPrefix=GLOBALS.basePath+"/secure/login.html?url=";
GLOBALS.searchPath=GLOBALS.basePath+"/search.html";
window.onerror=errorLogger;
function errorLogger(_1,_2,_3){
var _4=document.createElement("img");
_4.src=GLOBALS.basePath+"/javascript/ErrorLogger.js?url="+_2+"&line="+_3+"&msg="+_1;
_4.className="hide";
return false;
}
var eLibraryTabLabels=new Array("All","eJournals","Databases","eBooks","Biotools","Videos","medCalcs","Lane FAQs");
var eLibraryTabIDs=new Array("all","ej","database","book","biotools","video","cc","faq");
var eLibraryResultCounts=[];
var eLibraryActiveTab=null;
function geteLibraryTabCount(_5){
var _6=0;
if(document.getElementById(_5)){
_6=document.getElementById(_5).getElementsByTagName("dt").length;
}
return _6;
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
_b="dcsMultiTrack('WT.ti','LaneConnex search "+eLibraryTabIDs[i]+" tab','DCSext.keywords','"+keywords+"','DCSext.tab_view','"+eLibraryTabIDs[i]+"');";
}
_8=_8+"<div id=\""+eLibraryTabIDs[i]+"Tab\" class=\"eLibraryTab\" title=\""+_a+"\" name=\""+eLibraryTabIDs[i]+"\" onclick=\"javascript:showeLibraryTab('"+eLibraryTabIDs[i]+"');"+_b+"\">"+eLibraryTabLabels[i]+"<br /><span class=\"tabHitCount\">"+intToNumberString(eLibraryResultCounts[eLibraryTabIDs[i]])+"</span></div>";
}
document.getElementById("eLibraryTabs").innerHTML=_8;
}
function showeLibraryTab(_c){
if(!_c){
_c="all";
if((getQueryContent("direct")=="1"||getQueryContent("direct")=="true")&&eLibraryTabIDs.contains(getQueryContent("source"))){
_c=getQueryContent("source");
}else{
if(readCookie("LWeLibSource")&&eLibraryTabIDs.contains(readCookie("LWeLibSource"))){
_c=readCookie("LWeLibSource");
}else{
if(eLibraryTabIDs.contains(getQueryContent("source"))){
_c=getQueryContent("source");
}
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
var _1e="";
if(isDefined(window,"dcsMultiTrack")){
_1e="dcsMultiTrack('WT.ti','LaneConnex sort by click: "+readCookie("LWeLibNextSort")+"','DCSext.keywords','"+keywords+"');";
}
_1b+="Sorted by <select name=\"sortBy\" onchange=\""+_1e+"sorteLibraryResults();\" style=\"font-size: 95%; font-weight: 400;\">"+_1d+"</select>&nbsp;&nbsp;&nbsp;&nbsp;";
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
var _1f=document.getElementById(eLibraryActiveTab+"TabTipText").innerHTML;
document.getElementById("tabTip").innerHTML=_1f;
document.getElementById("tabTip").className="tabTip";
}else{
document.getElementById("tabTip").className="hide";
}
}
function IOClient(){
}
IOClient.prototype={request:null,type:null,url:null,init:function(_20,url){
this.type=_20;
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
var _22=this;
this.request.onreadystatechange=function(){
_22.readyStateChange(_22);
};
this.request.open("GET",this.url,true);
if(this.delay){
setTimeout("self.request.send(null);",this.delay);
}else{
this.request.send(null);
}
},readyStateChange:function(_23){
if(_23.request.readyState==4){
if(_23.request.status==200){
this.process();
}
}
},process:function(){
switch(this.type){
case "elib-meta":
var _24=this.request.responseXML.documentElement;
var _25=_24.getElementsByTagName("engine");
var _26=_24.getAttribute("id");
var _27=_24.getAttribute("status");
var _28=_24.getElementsByTagName("query")[0].firstChild.data;
var _29=(_24.getElementsByTagName("spell").length>0)?_24.getElementsByTagName("spell")[0].firstChild.data:null;
if(_29&&!document.getElementById("spellResults")){
var _2a="Did you mean: <a href=\""+GLOBALS.searchPath+"?keywords="+escape(_29)+"\"><i><strong>"+_29+"</strong></i></a><br />";
var _2b=document.getElementsByTagName("body").item(0);
var _2c=document.createElement("div");
_2c.className="hide";
_2c.innerHTML=_2a;
_2c.setAttribute("id","spellResults");
_2b.appendChild(_2c);
refreshPopInContent();
}
if(_27=="successful"||_27=="running"){
var _2d=0;
var _2e=0;
var _2f=0;
var _30=0;
document.getElementById("clinicalMetaCount").parentNode.href=GLOBALS.basePath+"/search.html?source=clinical&id="+_26+"&keywords="+keywords;
document.getElementById("researchMetaCount").parentNode.href=GLOBALS.basePath+"/search.html?source=research&id="+_26+"&keywords="+keywords;
setCookie("LWeLibMetaState","id="+_26+"&keywords="+keywords);
for(var i=0;i<_25.length;i++){
var _32=_25[i].getElementsByTagName("resource");
for(var j=0;j<_32.length;j++){
if(_32[j].getElementsByTagName("hits").length>0&&_32[j].getElementsByTagName("hits")[0].firstChild.data>0){
if(GLOBALS.clinicalEngines.contains(_32[j].getAttribute("id"))){
_2f+=parseInt(_32[j].getElementsByTagName("hits")[0].firstChild.data);
_2d++;
}
if(GLOBALS.researchEngines.contains(_32[j].getAttribute("id"))){
_30+=parseInt(_32[j].getElementsByTagName("hits")[0].firstChild.data);
_2e++;
}
}
var _34=_25[i].getAttribute("status");
if(_34=="successful"&&(parseInt(_2f)>0||parseInt(_30)>0)){
document.getElementById("clinicalMetaCount").innerHTML=intToNumberString(_2d);
document.getElementById("researchMetaCount").innerHTML=intToNumberString(_2e);
}
if(document.getElementById(_32[j].getAttribute("id")+"SearchResults")&&_32[j].getElementsByTagName("hits").length>0){
var _35=document.getElementById(_32[j].getAttribute("id")+"SearchResults");
_35.getElementsByTagName("a")[0].href=_32[j].getElementsByTagName("url")[0].firstChild.data;
_35.getElementsByTagName("a")[0].innerHTML=_32[j].getElementsByTagName("description")[0].firstChild.data+"<br /><span class=\"tabHitCount\">"+intToNumberString(_32[j].getElementsByTagName("hits")[0].firstChild.data)+"</span>";
if(_32[j].getAttribute("id")=="google"){
document.getElementById(_32[j].getAttribute("id")+"SearchResults").className="metaSearchResultsRightCorner";
}else{
document.getElementById(_32[j].getAttribute("id")+"SearchResults").className="metaSearchResults";
}
}
}
}
}
var _36=new Date();
if(_36.getTime()-startTime>60*1000){
_27="halted";
}
if(_27=="running"){
Object.neweLibMetaRequest=new IOClient();
Object.neweLibMetaRequest.init("elib-meta",GLOBALS.metasearchProxyPath+"?id="+_26+"&secs="+_36.getSeconds());
setTimeout("Object.neweLibMetaRequest.get();",GLOBALS.httpRequestInterval);
return 0;
}
break;
case "incremental":
var dom=string2dom(this.request.responseText);
var _38=dom.documentElement;
var _39=_38.getElementsByTagName("div");
for(var d=0;d<_39.length;d++){
if(_39[d].getAttribute("id")=="incrementalSearchResults"){
var _3b=_39[d].getElementsByTagName("li");
break;
}
}
var _3c=document.getElementById("incrementalSearchResults").getElementsByTagName("li");
document.getElementById("incrementalSearchResults").className="unhide";
var _3d=getMetaContent(_38,"lw_searchParameters","status");
var _3e=getMetaContent(_38,"lw_searchParameters","query");
var _3f=document.getElementById("incrementalResultsProgressBar");
var _40=document.getElementById("incrementalResultsDetails");
var _41=0;
var _42=0;
var _43=true;
for(var i=0;i<_3b.length;i++){
var _45=_3c[i].getElementsByTagName("a")[0];
var _46=_3b[i].getElementsByTagName("a")[0];
var _47=_46.getAttribute("type");
if(_47=="running"){
_43=false;
}else{
_42++;
}
if(_45.getAttribute("href")!=_46.getAttribute("href")){
var _48=_45.innerHTML;
if(navigator.appVersion.indexOf("Safari")>-1){
_45.setAttribute("href",_46.getAttribute("href").replace(/&#38;/g,"&"));
}else{
_45.setAttribute("href",_46.getAttribute("href"));
}
_45.innerHTML=_48;
}
if(_47=="running"||_3b[i].getElementsByTagName("span")[0].childNodes[0].nodeValue==0){
_3c[i].className="hide";
}else{
_3c[i].parentNode.parentNode.getElementsByTagName("h3")[0].className="";
_3c[i].className="";
_41++;
}
if(_47=="successful"&&_45.getAttribute("type")!=_47){
_45.setAttribute("type",_47);
if(_3b[i].getElementsByTagName("span").length>0){
var _49=_3b[i].getElementsByTagName("span")[0].childNodes[0].nodeValue;
if(_49=="0"&&_47=="successful"){
_49=" 0";
}
_3c[i].appendChild(document.createTextNode(_49));
}
}
}
var _4a=new Date();
if(_4a.getTime()-startTime>60*1000||haltIncremental){
_43=true;
}
if(!_43){
var _4b=0;
if(_3b.length>0&&_42>0){
_4b=100*(_42/_3b.length);
}else{
_4b=1;
}
_3f.innerHTML="<table><tr><td nowrap>Still searching...</td><td nowrap><div style=\"position:relative;left:2px;top:2px;border:1px solid #b2b193; width:200px;\"><img width=\""+_4b+"%\" height=\"15\" src=\""+GLOBALS.basePath+"/images/templates/default/incrementalResultsProgressBar.gif\" alt=\"progress bar\" /></div></td><td nowrap>&nbsp;"+_42+" of "+_3b.length+" sources searched. <a href=\"javascript:haltIncremental=true;void(0);\">Stop Search</a></td></tr></table>";
setTimeout("getIncrementalResults();",GLOBALS.httpRequestInterval);
return 0;
}else{
_3f.innerHTML="";
if(_3b.length>_41){
_3f.innerHTML="Results in <strong>"+_41+"</strong> of <strong>"+_3b.length+"</strong> sources for <strong>"+_3e+"</strong> [<a id=\"zerotoggle\" href=\"javascript:toggleIncrementalZeros('true');\">Show Details</a>]";
}else{
if(_3b.length==_41){
_3f.innerHTML="Results in <strong>"+_41+"</strong> of <strong>"+_3b.length+"</strong> sources contain <strong>"+_3e+"</strong>";
}
}
}
break;
case "sfx":
var _4c=this.request.responseXML.documentElement;
var _4d=_4c.getElementsByTagName("openurl")[0].firstChild.data;
var _4e=_4c.getElementsByTagName("result")[0].firstChild.data;
if(_4e!=0){
var _4f="";
if(isDefined(window,"dcsMultiTrack")){
var _50=_4d.substring(_4d.indexOf("?")+1,_4d.length);
_4f="onclick=\"dcsMultiTrack('DCS.dcssip','sfx.stanford.edu','DCS.dcsuri','local','DCS.dcsquery','"+_50+"','WT.ti','SFX','DCSext.keywords','"+keywords+"','DCSext.offsite_link','1');\"";
}
var _51="FindIt@Stanford eJournal: <a title=\"Fulltext access to "+_4e+"\" href=\""+_4d+"\" "+_4f+"><b>"+_4e.replace(/ \[.*\]/,"")+"</b></a><br />";
var _52=document.getElementsByTagName("body").item(0);
var _53=document.createElement("div");
_53.className="hide";
_53.innerHTML=_51;
_53.setAttribute("id","sfxResults");
_52.appendChild(_53);
refreshPopInContent();
}
break;
}
}};
Array.prototype.contains=function(_54){
for(var i=0,elms=this.length;i<elms&&this[i]!==_54;i++){
}
return i<elms;
};
Array.prototype.sortByAlpha=function(){
var _56=[];
var _57=[];
var _58=/^(a|an|the|de|die|la|le|los|las|les) /;
for(var i=0;i<this.length;i++){
_56[_56.length]=this[i].toString().toLowerCase().replace(_58,"");
_57[this[i].toString().toLowerCase().replace(_58,"")]=i;
}
_56.sort();
var _5a=[];
for(var i=0;i<_56.length;i++){
_5a[i]=this[_57[_56[i]]];
}
return _5a;
};
function isDefined(_5c,_5d){
return (typeof (eval(_5c)[_5d])=="undefined")?false:true;
}
function cleanKW(_5e){
_5e=escape(_5e.replace(/&amp;/g,"&"));
return _5e;
}
function getMetaContent(_5f,_60,_61,_62,_63){
var _64="";
if(!_62){
_62=";";
}else{
if(_62=="&amp;"){
_62="&";
}
}
if(!_63){
_63="=";
}
var _65=_5f.getElementsByTagName("meta");
for(var i=0;i<_65.length;i++){
if(_65[i].getAttribute("name")==_60){
if(_61){
var _67=[];
_67=_65[i].getAttribute("content").split(_62);
for(var y=0;y<_67.length;y++){
var _69=[];
_69=_67[y].split(_63);
if(_69[0]==_61){
_64=_69[1];
}
}
}else{
_64=_65[i].getAttribute("content");
}
}
}
return _64;
}
function getQueryContent(_6a,_6b){
if(!_6b){
_6b=location.search;
}
_6a+="=";
if(_6b.length>0){
var _6c=_6b.indexOf(_6a);
if(_6c!=-1){
_6c+=_6a.length;
var _6d=_6b.indexOf("&",_6c);
if(_6d==-1){
_6d=_6b.length;
}
return unescape(_6b.substring(_6c,_6d));
}
}
return 0;
}
function intToNumberString(_6e){
_6e=_6e.toString();
var _6f=/(\d+)(\d{3})/;
while(_6f.test(_6e)){
_6e=_6e.replace(_6f,"$1"+","+"$2");
}
return _6e;
}
function toggleNode(_70,_71,_72,_73,_74){
if(_71.className!="hide"){
_71.className="hide";
_70.innerHTML=_72;
if(_74){
document.getElementById(_74).className="";
}
}else{
_71.className="";
_70.innerHTML=_73;
if(_74){
document.getElementById(_74).className="hide";
}
}
}
function readCookie(_75){
var _76=_75+"=";
var _77=document.cookie.split(";");
for(var i=0;i<_77.length;i++){
var c=_77[i];
while(c.charAt(0)==" "){
c=c.substring(1,c.length);
}
if(c.indexOf(_76)==0){
var _7a=c.substring(_76.length,c.length);
return _7a;
}
}
return null;
}
function removeCookie(_7b){
if(readCookie(_7b)){
document.cookie=_7b+"="+"; expires=Thu, 01-Jan-70 00:00:01 GMT";
return true;
}
return false;
}
function setCookie(_7c,_7d){
document.cookie=_7c+"="+_7d+"; path=/; ";
}
function searchFormSelect(_7e){
var _7f="searchForm";
if(typeof (_7e)=="number"){
document[_7f].source.selectedIndex=_7e;
}else{
for(var i=0;i<document[_7f].source.options.length;i++){
if(document[_7f].source.options[i].value==_7e||document[_7f].source.options[i].text.indexOf(_7e)>-1){
document[_7f].source.selectedIndex=i;
}
}
}
document[_7f].source.onchange();
}
function string2dom(_81,_82){
if(!_82){
_82="text/xml";
}
if(isDefined("window","DOMParser")){
var _83=new DOMParser();
return _83.parseFromString(_81,_82);
}else{
if(isDefined("window","ActiveXObject")){
var _84=new ActiveXObject("MSXML.DOMDocument");
if(_84){
_84.async=false;
_84.loadXML(_81);
if(_84.parseError!=0){
alert("Document parse error\nCode:"+_84.parseError.errorCode+"\nLine:"+_84.parseError.line+"\nReason:"+_84.parseError.reason);
}
return _84;
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
var _86=getMetaContent(document,"lw_searchParameters","source");
var _87=new Date();
var url=GLOBALS.basePath+"/content/search.html?id="+id+"&source="+_86+"&secs="+_87.getSeconds();
var _89=new IOClient();
_89.init("incremental",url);
_89.get();
}
function toggleIncrementalZeros(_8a){
var _8b=document.getElementById("incrementalSearchResults").getElementsByTagName("h3");
var _8c=document.getElementById("incrementalSearchResults").getElementsByTagName("li");
var _8d=document.getElementById("zerotoggle");
if(_8a=="true"){
_8d.href="javascript:toggleIncrementalZeros('false');";
_8d.innerHTML="Hide Details";
for(var i=0;i<_8b.length;i++){
if(_8b[i].className=="hide"){
_8b[i].className="unhide";
}
}
for(var i=0;i<_8c.length;i++){
if(_8c[i].className=="hide"){
_8c[i].className="unhide";
}
}
}else{
if(_8a=="false"){
_8d.href="javascript:toggleIncrementalZeros('true');";
_8d.innerHTML="Show Details";
for(var i=0;i<_8b.length;i++){
if(_8b[i].className=="unhide"){
_8b[i].className="hide";
}
}
for(var i=0;i<_8c.length;i++){
if(_8c[i].className=="unhide"){
_8c[i].className="hide";
}
}
}
}
var _92="";
if(isDefined(window,"dcsMultiTrack")){
dcsMultiTrack("WT.ti","Metasearch Show/Hide Details clicked");
}
}
var searching=false;
function submitSearch(){
var _93="searchForm";
var _94=document[_93].source.options[document[_93].source.selectedIndex].value;
var _95=cleanKW(document[_93].keywords.value);
var _96="Please enter one or more search terms.";
if(eLibraryTabIDs.contains(_94)){
setCookie("LWeLibSource",_94);
}
if(_95==""){
alert(_96);
return false;
}else{
if(_94.match(/(research|clinical|peds)/)){
var _97=GLOBALS.searchPath+"?source="+_94+"&keywords="+_95+"&w="+GLOBALS.incrementalSearchWait;
window.location=_97;
return false;
}else{
if(_94=="biomedsem"){
var _98="http://med.stanford.edu/seminars/search?searchString="+_95+"&Submit=Go";
window.location=_98;
return false;
}else{
if(_94=="catalog"){
var _99="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&SL=none&SAB1="+_95+"&BOOL1=all+of+these&FLD1=Keyword+Anywhere++%5BLKEY%5D+%28LKEY%29&GRP1=AND+with+next+set&SAB2=&BOOL2=all+of+these&FLD2=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&GRP2=AND+with+next+set&SAB3=&BOOL3=all+of+these&FLD3=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&CNT=50";
window.location=_99;
return false;
}else{
if(_94=="google"){
var _9a="http://www.google.com/search?hl=en&q="+_95;
if(GLOBALS.needsProxy=="true"){
_9a=GLOBALS.proxyPrefix+_9a;
}
window.location=_9a;
return false;
}else{
if(_94=="pubmed"){
var _9b="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?otool=stanford&CMD=search&DB=PubMed&term="+_95;
if(GLOBALS.needsProxy=="true"){
_9b=GLOBALS.proxyPrefix+_9b;
}
window.location=_9b;
return false;
}else{
if(_94=="stanford_who"){
var _9c="https://stanfordwho.stanford.edu/lookup?search="+_95;
window.location=_9c;
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
function lastSelectValue(_9d){
var val=_9d.options[_9d.selectedIndex].value;
if((val=="----------------")||(val=="")){
if(lastIndex){
_9d.selectedIndex=lastIndex;
}else{
_9d.selectedIndex=0;
}
}else{
if(arguments.length>1&&arguments[1]=="portals"){
window.location=GLOBALS.basePath+_9d.options[_9d.selectedIndex].value;
}
}
}

