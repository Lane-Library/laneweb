
var imagePath = '/././images/templates/default/';
var nokeywords = 'Please enter one or more search terms.';
var searching = false;

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(typeof d.MM_p == 'undefined') d.MM_p=new Array();
    var i,j=d.MM_p.length,a=arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf('#')!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=imagePath+a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf('?'))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(typeof x.oSrc == 'undefined') x.oSrc=x.src; x.src=a[i+2];}
}

function listSearchTagline(select) {
	var elementId = select.options[select.selectedIndex].value;
	var elementContainerForDisplayText = document.getElementById(elementId + "SearchTagline");
	document.getElementById("displaySearchTaglineText").innerHTML = (elementContainerForDisplayText != null) ? elementContainerForDisplayText.innerHTML : document.getElementById("eResourcesSearchTagline").innerHTML;
}

function startState(url) {
	var id, img;
	/***
		legacy syntax: /online/er.html?tab=ej
		new syntax: /search.html?source=clinical
	***/
	var legacyString = (url.indexOf('/online/er') != -1) ? url.substring(url.indexOf('/online') + "/online/er.html?".length, url.length) : "";
	var newString = (url.indexOf('/search') != -1) ? url.substring(url.indexOf('/search') + "/search.html?".length, url.length) : "";
	var paramString = legacyString + newString; /* mutually exclusive, OK to concatenate */
	
	if (url.indexOf('/online/') != -1 && (url.substring(url.indexOf('/online/') + "/online/".length, url.length)).indexOf('ej') != -1) /* accommodate ej.html, ejbrowse.html, ejsubject.html, etc. */{
		id='eJourn'; img='eJournOn.jpg';
	}
	else if (url.indexOf('/portals/clinical') != -1 || 
		url.indexOf('/portals/peds') != -1 || 
		paramString.indexOf('clinical') != -1 || 
		paramString.indexOf('peds') != -1) { 
		id='clin'; img='clinOn.jpg';
	}
	else if (url.indexOf('/portals/bioresearch') != -1 || 
		paramString.indexOf('research') != -1) {
		id='research'; img='bioresearchOn.jpg';
	}
	else if (url.indexOf('/services/') != -1 || 
		url.indexOf('/askus') != -1 ||
		paramString.indexOf('faq') != -1
		/*(url.indexOf('/search') != -1 && (url.substring(url.indexOf('/search') + "/search.html?".length, url.length)).indexOf('source=faq') != -1 ||
			url.indexOf('/online') != -1 && (url.substring(url.indexOf('/online') + "/online/er.html?".length, url.length)).indexOf('tab=faq') != -1)*/
		) {
		id='services'; img='laneServicesOn.jpg';
	}
	else {
		id='eLibrary'; img='eLibOn.jpg';
	}
		
	MM_swapImage(id,'',imagePath+img,1);
	listSearchTagline(document.getElementById("source"));
}

/*
function startState(str) {
	var id, img;
	if (str == 'home') { 
		return;
	}
	else {
		if (str == 'clinician') { clinician = 1; id='tab_clinician'; img='tab_clinician_f2.gif'; }
		if (str == 'researcher') { researcher = 1; id='tab_researcher'; img='tab_researcher_f2.gif'; }
		if (str == 'instructor') { instructor = 1; id='tab_instructor'; img='tab_instructor_f2.gif'; }
		if (str == 'student') { student = 1; id='tab_student'; img='tab_student_f2.gif'; }
		if (str == 'patient') { patient = 1; id='tab_patient'; img='tab_patient_f2.gif'; }
		
		MM_swapImage(id,'',imagePath+img,1);
	}
}
*/
function openLink(url) {
   openSearchResult(url);
/*
  w = 700;
  h = 500;
  features = 'width='+w+',height='+h+',toolbar=yes,scrollbars=yes,resizable=yes,status=yes';

  window.open(url, '', features);
  */
}

function openSearchResult(url) {
    window.open(url, '', 'width=700,height=650,directories=yes,menubar=yes,location=yes,left=75,toolbar=yes,scrollbars=yes,resizable=yes,status=yes,top=100');
}

/*
function submitSearch() {
  var source = document.searchForm.source.options[document.searchForm.source.selectedIndex].value;
  var keywords = document.searchForm.keywords.value;

  if (keywords == '') {
    alert(nokeywords);
    return false;
  }

  if (source == 'google') {
    openSearchResult('http://www.google.com/search?hl=en&q=' + keywords);
    return false;
  }
  else if (source == 'catalog') {
    openSearchResult('http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=locali&Search_Arg=' + keywords + '&SL=None&Search_Code=FT*&CNT=50');
    return false;
  }
  else if (source == 'pubmed') {
    openSearchResult('http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?otool=stanford&CMD=search&DB=PubMed&term=' + keywords);
    return false;
  }
  else if (source == 'stanford_med') {
    openSearchResult('http://find.stanford.edu/search?site=stanford&client=stanford&output=xml_no_dtd&restrict=stanford_medicine&proxystylesheet=http://www-med.stanford.edu/school/master/protomed.xslt&q=' + keywords);
    return false;
  }
  else if (source == 'stanford_who') {
    openSearchResult('https://stanfordwho.stanford.edu/lookup?search=' + keywords);
    return false;
  }
  else if (source == 'stanford') {
    openSearchResult('http://find.stanford.edu/search?q=' + keywords + '&site=stanford&client=stanford&output=xml_no_dtd&proxystylesheet=http://google.stanford.edu/stylesheet.xslt');
    return false;
  }
  else if (source == 'lane_site') {
    openSearchResult('http://find.stanford.edu/search?site=stanford&client=stanford&output=xml_no_dtd&restrict=stanford_medicine&proxystylesheet=http://www-med.stanford.edu/school/master/protomed.xslt&q=site:lane.stanford.edu ' + keywords);
    return false;
  }
  if (searching) {
    alert('a search is already in progress');
    return false;
  }
  searching = true;
  return true;
}
*/
