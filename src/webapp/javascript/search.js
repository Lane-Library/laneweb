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
var eLibraryTabLabels=new Array("All","eJournals","Databases","eBooks","Biotools","medCalcs","Lane FAQs");
var eLibraryTabIDs=new Array("all","ej","database","book","biotools","cc","faq");
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
_b="dcsMultiTrack('WT.ti','LaneConnex search "+eLibraryTabIDs[i]+" tab','DCSext.keywords','cancer','DCSext.tab_view','"+eLibraryTabIDs[i]+"');";
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
var _29="Did you mean: <a href=\""+GLOBALS.searchPath+"?keywords="+escape(_28)+"\"><i><strong>"+_28+"</strong></i></a><br />";
var _2a=document.getElementsByTagName("body").item(0);
var _2b=document.createElement("div");
_2b.className="hide";
_2b.innerHTML=_29;
_2b.setAttribute("id","spellResults");
_2a.appendChild(_2b);
refreshPopInContent();
}
if(_26=="successful"||_26=="running"){
var _2c=0;
var _2d=0;
var _2e=0;
var _2f=0;
document.getElementById("clinicalMetaCount").parentNode.href=GLOBALS.basePath+"/search.html?source=clinical&id="+_25+"&keywords="+keywords;
document.getElementById("researchMetaCount").parentNode.href=GLOBALS.basePath+"/search.html?source=research&id="+_25+"&keywords="+keywords;
setCookie("LWeLibMetaState","id="+_25+"&keywords="+keywords);
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
_34.getElementsByTagName("a")[0].href=_31[j].getElementsByTagName("url")[0].firstChild.data;
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
if(_44.getAttribute("href")!=_45.getAttribute("href")){
var _47=_44.innerHTML;
if(navigator.appVersion.indexOf("Safari")>-1){
_44.setAttribute("href",_45.getAttribute("href").replace(/&#38;/g,"&"));
}else{
_44.setAttribute("href",_45.getAttribute("href"));
}
_44.innerHTML=_47;
}
_40++;
}
if(_44.getAttribute("type")!=_46){
_44.setAttribute("type",_46);
if(_3a[i].getElementsByTagName("span").length>0){
var _48=_3a[i].getElementsByTagName("span")[0].childNodes[0].nodeValue;
if(_48=="0"&&_46=="successful"){
_48=" 0";
}
_3b[i].appendChild(document.createTextNode(_48));
}
}
}
var _49=new Date();
if(_49.getTime()-startTime>60*1000||haltIncremental){
_42=true;
}
if(!_42){
var _4a=0;
if(_3a.length>0&&_41>0){
_4a=100*(_41/_3a.length);
}else{
_4a=1;
}
_3e.innerHTML="<table><tr><td nowrap>Still searching...</td><td nowrap><div style=\"position:relative;left:2px;top:2px;border:1px solid #b2b193; width:200px;\"><img width=\""+_4a+"%\" height=\"15\" src=\""+GLOBALS.basePath+"/images/templates/default/incrementalResultsProgressBar.gif\" alt=\"progress bar\" /></div></td><td nowrap>&nbsp;"+_41+" of "+_3a.length+" sources searched. <a href=\"javascript:haltIncremental=true;void(0);\">Stop Search</a></td></tr></table>";
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
var _4b=this.request.responseXML.documentElement;
var _4c=_4b.getElementsByTagName("openurl")[0].firstChild.data;
var _4d=_4b.getElementsByTagName("result")[0].firstChild.data;
if(_4d!=0){
var _4e="";
if(isDefined(window,"dcsMultiTrack")){
var _4f=_4c.substring(_4c.indexOf("?")+1,_4c.length);
_4e="onclick=\"dcsMultiTrack('DCS.dcssip','sfx.stanford.edu','DCS.dcsuri','local','DCS.dcsquery','"+_4f+"','WT.ti','SFX','DCSext.keywords','"+keywords+"','DCSext.offsite_link','1');\"";
}
var _50="FindIt@Stanford eJournal: <a title=\"Fulltext access to "+_4d+"\" href=\""+_4c+"\" "+_4e+"><b>"+_4d.replace(/ \[.*\]/,"")+"</b></a><br />";
var _51=document.getElementsByTagName("body").item(0);
var _52=document.createElement("div");
_52.className="hide";
_52.innerHTML=_50;
_52.setAttribute("id","sfxResults");
_51.appendChild(_52);
refreshPopInContent();
}
break;
}
}};
Array.prototype.contains=function(_53){
for(var i=0,elms=this.length;i<elms&&this[i]!==_53;i++){
}
return i<elms;
};
Array.prototype.sortByAlpha=function(){
var _55=[];
var _56=[];
var _57=/^(a|an|the|de|die|la|le|los|las|les) /;
for(var i=0;i<this.length;i++){
_55[_55.length]=this[i].toString().toLowerCase().replace(_57,"");
_56[this[i].toString().toLowerCase().replace(_57,"")]=i;
}
_55.sort();
var _59=[];
for(var i=0;i<_55.length;i++){
_59[i]=this[_56[_55[i]]];
}
return _59;
};
function isDefined(_5b,_5c){
return (typeof (eval(_5b)[_5c])=="undefined")?false:true;
}
function cleanKW(_5d){
_5d=escape(_5d.replace(/&amp;/g,"&"));
return _5d;
}
function getMetaContent(_5e,_5f,_60,_61,_62){
var _63="";
if(!_61){
_61=";";
}else{
if(_61=="&amp;"){
_61="&";
}
}
if(!_62){
_62="=";
}
var _64=_5e.getElementsByTagName("meta");
for(var i=0;i<_64.length;i++){
if(_64[i].getAttribute("name")==_5f){
if(_60){
var _66=[];
_66=_64[i].getAttribute("content").split(_61);
for(var y=0;y<_66.length;y++){
var _68=[];
_68=_66[y].split(_62);
if(_68[0]==_60){
_63=_68[1];
}
}
}else{
_63=_64[i].getAttribute("content");
}
}
}
return _63;
}
function getQueryContent(_69,_6a){
if(!_6a){
_6a=location.search;
}
_69+="=";
if(_6a.length>0){
var _6b=_6a.indexOf(_69);
if(_6b!=-1){
_6b+=_69.length;
var _6c=_6a.indexOf("&",_6b);
if(_6c==-1){
_6c=_6a.length;
}
return unescape(_6a.substring(_6b,_6c));
}
}
return 0;
}
function intToNumberString(_6d){
_6d=_6d.toString();
var _6e=/(\d+)(\d{3})/;
while(_6e.test(_6d)){
_6d=_6d.replace(_6e,"$1"+","+"$2");
}
return _6d;
}
function toggleNode(_6f,_70,_71,_72,_73){
if(_70.className!="hide"){
_70.className="hide";
_6f.innerHTML=_71;
if(_73){
document.getElementById(_73).className="";
}
}else{
_70.className="";
_6f.innerHTML=_72;
if(_73){
document.getElementById(_73).className="hide";
}
}
}
function readCookie(_74){
var _75=_74+"=";
var _76=document.cookie.split(";");
for(var i=0;i<_76.length;i++){
var c=_76[i];
while(c.charAt(0)==" "){
c=c.substring(1,c.length);
}
if(c.indexOf(_75)==0){
var _79=c.substring(_75.length,c.length);
return _79;
}
}
return null;
}
function removeCookie(_7a){
if(readCookie(_7a)){
document.cookie=_7a+"="+"; expires=Thu, 01-Jan-70 00:00:01 GMT";
return true;
}
return false;
}
function setCookie(_7b,_7c){
document.cookie=_7b+"="+_7c+"; path=/; ";
}
function searchFormSelect(_7d){
var _7e="searchForm";
if(typeof (_7d)=="number"){
document[_7e].source.selectedIndex=_7d;
}else{
for(var i=0;i<document[_7e].source.options.length;i++){
if(document[_7e].source.options[i].value==_7d||document[_7e].source.options[i].text.indexOf(_7d)>-1){
document[_7e].source.selectedIndex=i;
}
}
}
document[_7e].source.onchange();
}
function string2dom(_80,_81){
if(!_81){
_81="text/xml";
}
if(isDefined("window","DOMParser")){
var _82=new DOMParser();
return _82.parseFromString(_80,_81);
}else{
if(isDefined("window","ActiveXObject")){
var _83=new ActiveXObject("MSXML.DOMDocument");
if(_83){
_83.async=false;
_83.loadXML(_80);
if(_83.parseError!=0){
alert("Document parse error\nCode:"+_83.parseError.errorCode+"\nLine:"+_83.parseError.line+"\nReason:"+_83.parseError.reason);
}
return _83;
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
var _85=getMetaContent(document,"lw_searchParameters","source");
var _86=new Date();
var url=GLOBALS.basePath+"/content/search.html?id="+id+"&source="+_85+"&secs="+_86.getSeconds();
var _88=new IOClient();
_88.init("incremental",url);
_88.get();
}
function toggleIncrementalZeros(_89){
var _8a=document.getElementById("incrementalSearchResults").getElementsByTagName("h3");
var _8b=document.getElementById("incrementalSearchResults").getElementsByTagName("li");
var _8c=document.getElementById("zerotoggle");
if(_89=="true"){
_8c.href="javascript:toggleIncrementalZeros('false');";
_8c.innerHTML="Hide Details";
for(var i=0;i<_8a.length;i++){
if(_8a[i].className=="hide"){
_8a[i].className="unhide";
}
}
for(var i=0;i<_8b.length;i++){
if(_8b[i].className=="hide"){
_8b[i].className="unhide";
}
}
}else{
if(_89=="false"){
_8c.href="javascript:toggleIncrementalZeros('true');";
_8c.innerHTML="Show Details";
for(var i=0;i<_8a.length;i++){
if(_8a[i].className=="unhide"){
_8a[i].className="hide";
}
}
for(var i=0;i<_8b.length;i++){
if(_8b[i].className=="unhide"){
_8b[i].className="hide";
}
}
}
}
}
var searching=false;
function submitSearch(){
var _91="searchForm";
var _92=document[_91].source.options[document[_91].source.selectedIndex].value;
var _93=document[_91].keywords.value;
var _94="Please enter one or more search terms.";
if(eLibraryTabIDs.contains(_92)){
setCookie("LWeLibSource",_92);
}
if(_93==""){
alert(_94);
return false;
}else{
if(_92.match(/(research|clinical|peds)/)){
var _95=GLOBALS.searchPath+"?source="+_92+"&keywords="+_93+"&w="+GLOBALS.incrementalSearchWait;
window.location=_95;
return false;
}else{
if(_92=="biomedsem"){
var _96="http://med.stanford.edu/seminars/searchresults.jsp?searchString="+_93+"&Submit=Go";
window.location=_96;
return false;
}else{
if(_92=="catalog"){
var _97="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&SL=none&SAB1="+_93+"&BOOL1=all+of+these&FLD1=Keyword+Anywhere++%5BLKEY%5D+%28LKEY%29&GRP1=AND+with+next+set&SAB2=&BOOL2=all+of+these&FLD2=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&GRP2=AND+with+next+set&SAB3=&BOOL3=all+of+these&FLD3=ISSN+%5Bwith+hyphen%5D+%28ISSN%29&CNT=50";
window.location=_97;
return false;
}else{
if(_92=="google"){
var _98="http://www.google.com/search?hl=en&q="+_93;
if(GLOBALS.needsProxy=="true"){
_98=GLOBALS.proxyPrefix+_98;
}
window.location=_98;
return false;
}else{
if(_92=="pubmed"){
var _99="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?otool=stanford&CMD=search&DB=PubMed&term="+_93;
if(GLOBALS.needsProxy=="true"){
_99=GLOBALS.proxyPrefix+_99;
}
window.location=_99;
return false;
}else{
if(_92=="stanford_who"){
var _9a="https://stanfordwho.stanford.edu/lookup?search="+_93;
window.location=_9a;
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
function lastSelectValue(_9b){
var val=_9b.options[_9b.selectedIndex].value;
if((val=="----------------")||(val=="")){
if(lastIndex){
_9b.selectedIndex=lastIndex;
}else{
_9b.selectedIndex=0;
}
}else{
if(arguments.length>1&&arguments[1]=="portals"){
window.location=GLOBALS.basePath+_9b.options[_9b.selectedIndex].value;
}
}
}

