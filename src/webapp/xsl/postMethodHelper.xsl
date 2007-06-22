<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  version="1.0"
xmlns="http://www.w3.org/1999/xhtml"  xmlns:r="http://apache.org/cocoon/request/2.0"
>

<xsl:param name="sunetid"/>
<xsl:param name="ticket"/>
<xsl:param name="proxy-links"/>   
   
<xsl:variable name="proxy-url">http://laneproxy.stanford.edu/login?</xsl:variable> 
<xsl:variable name="post-url">
 <xsl:choose>
 <xsl:when test="$proxy-links = 'true' and $sunetid != '' and $ticket != ''">
                       <xsl:value-of select="$proxy-url"/>
                       <xsl:text>user=</xsl:text>
                       <xsl:value-of select="$sunetid"/>
                       <xsl:text>&amp;ticket=</xsl:text>
                       <xsl:value-of select="$ticket"/>
                       <xsl:text>&amp;url=</xsl:text>
                       <xsl:value-of select="r:request/r:requestParameters/r:parameter[@name='postAction']"/>
			</xsl:when>
                   <xsl:otherwise>
                       <xsl:value-of select="r:request/r:requestParameters/r:parameter[@name='postAction']"/>
                   </xsl:otherwise>
  </xsl:choose>
</xsl:variable>  
  
<xsl:template match="/">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<title>Post Method Helper</title>
<head>
</head>



<body onload="document.defaultForm.submit()" >


<form   action="{r:request/r:requestParameters/r:parameter[@name='postAction']}"  name="defaultForm" method="post">
<xsl:for-each select="r:request/r:requestParameters/r:parameter">
	<xsl:if  test="@name !='postAction'">
		<input type="hidden" name="{@name}" value="{r:value}"/>
	</xsl:if>
</xsl:for-each>	
</form>

<table>
<tr><td>Please, wait while we redirect you.</td></tr>
</table>
</body>
</html>
</xsl:template>


</xsl:stylesheet>