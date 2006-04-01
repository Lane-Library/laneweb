<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Micromedex Search</title>
<body>
<script language="JavaScript">

var userAgent = navigator.userAgent;
var path="<%=request.getParameter("path")%>";
var entryUrl = "<%=request.getParameter("entryUrl")%>";

if(userAgent.indexOf('Firefox')>-1)
{
	document.write("<IFRAME SRC='"+entryUrl+"' onload='redirect()' name='session' frameborder='0' height='0' width='0'></IFRAME>");	
}
else
{
	var host=document.location.hostname 
	var sessionWindows=window.open(entryUrl,'sessionWindows','directories=no,menubar=no,toolbar=no,scrollbars=no,height=1,width=1,location=yes,left=85,top=120');
	if (sessionWindows){
		 	window.focus() ;
			setTimeout("redirect()",4000);
			setTimeout("sessionWindows.close()",4000);}
 	else {
			document.write("<p>In order for this automated script to work you must enable pop-up</p>");
		}
}

function redirect(){
	document.location.href=path;
}
</script>

</body>
</html>