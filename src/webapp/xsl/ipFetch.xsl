<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  version="1.0"
xmlns="http://www.w3.org/1999/xhtml"  xmlns:r="http://apache.org/cocoon/request/2.0"
>

<xsl:param name="sunetid"/>
<xsl:param name="ticket"/>
<xsl:param name="proxy-links"/>   
<xsl:param name="remoteAddr"/>   
<xsl:param name="path"/>
   
<xsl:variable name="proxy-url">http://laneproxy.stanford.edu/login?</xsl:variable> 
<xsl:variable name="url">
 <xsl:choose>
	 <xsl:when test="$proxy-links = 'true' and $sunetid != '' and $ticket != ''">
         <xsl:value-of select="$proxy-url"/>
         <xsl:text>user=</xsl:text>
         <xsl:value-of select="$sunetid"/>
         <xsl:text>&amp;ticket=</xsl:text>
         <xsl:value-of select="$ticket"/>
         <xsl:text>&amp;url</xsl:text>
         <xsl:value-of select="$path"/>
         <xsl:value-of select="$remoteAddr"/>
	</xsl:when>
	 <xsl:otherwise>
	   <xsl:value-of select="$path"/>
       <xsl:value-of select="$remoteAddr"/>
      </xsl:otherwise>
  </xsl:choose>
</xsl:variable>  
  
<xsl:template match="/">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<title> IP Fetch </title>
<head>

<script language="javascript">

var url =    "<xsl:value-of select="$url"/> ";
<xsl:text disable-output-escaping="yes">

function redirect()
{	
	window.location =  url.replace(/&amp;amp;/g,"&amp;");
	
}
</xsl:text>
</script>


</head>
<body onload="redirect()">
<table>

<tr><td>Please, wait while we redirect you. </td></tr>
</table>
</body>
</html>
</xsl:template>


</xsl:stylesheet>