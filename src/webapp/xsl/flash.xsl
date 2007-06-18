<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    version="1.0">
    
	<xsl:template match="h:object[h:param[@name='flash-version']]">
    	<xsl:variable name="flash-version">
    		<xsl:choose>
    			<xsl:when test="h:param[@name='flash-version']">
    				<xsl:value-of select="h:param[@name='flash-version']/@value"/>
    			</xsl:when>
    			<xsl:otherwise>6.0.65</xsl:otherwise>
    		</xsl:choose>
    	</xsl:variable>
        <script type="text/javascript">
<xsl:text disable-output-escaping="yes">
var hasProductInstall = DetectFlashVer(6, 0, 65);
var requiredVersion = '</xsl:text><xsl:value-of select="$flash-version"/><xsl:text disable-output-escaping="yes">'.split('.');
var hasRequestedVersion = DetectFlashVer(requiredVersion[0],requiredVersion[1],requiredVersion[2]);
if ( hasProductInstall &amp;&amp; !hasRequestedVersion ) {
<!--	// MMdoctitle is the stored document.title value used by the installation process to close the window that started the process
	// This is necessary in order to close browser windows that are still utilizing the older version of the player after installation has completed
	// DO NOT MODIFY THE FOLLOWING FOUR LINES
	// Location visited after installation is complete if installation is required-->
	var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
	var MMredirectURL = window.location;
	document.title = document.title.slice(0, 47) + " - Flash Player Installation";
	var MMdoctitle = document.title;

	AC_FL_RunContent(
		"src", "/flash/playerProductInstall.swf",
		"FlashVars", "MMredirectURL="+MMredirectURL+'&amp;MMplayerType='+MMPlayerType+'&amp;MMdoctitle='+MMdoctitle+"",
		"width", "550",
		"height", "300",
		"align", "middle",
		"id", "detectionExample",
		"quality", "high",
		"bgcolor", "#3A6EA5",
		"name", "detectionExample",
		"allowScriptAccess","sameDomain",
		"type", "application/x-shockwave-flash",
		"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
} else if (hasRequestedVersion) {
<!--	// if we've detected an acceptable version
	// embed the Flash Content SWF when all tests are passed-->
	AC_FL_RunContent(</xsl:text>
<xsl:for-each select="@*">
"<xsl:value-of select="name()"/>","<xsl:value-of select="." disable-output-escaping="yes"/>",
</xsl:for-each>
<xsl:for-each select="h:param[not(@name='flash-version')]">
"<xsl:value-of select="@name"/>","<xsl:value-of select="@value" disable-output-escaping="yes"/>"<xsl:if test="position() != last()">,</xsl:if>
</xsl:for-each>
<xsl:text disable-output-escaping="yes">	);
} else {<!--  // flash is too old or we can't detect the plugin-->
	var alternateContent = 'This content requires the Adobe Flash Player. '
	+ '&lt;a href=http://www.adobe.com/go/getflash/>Get Flash&lt;/a>';
	document.write(alternateContent);<!--  // insert non-flash content-->
}
</xsl:text>
       </script>
    	<noscript><p>This flash object requires javascript.</p></noscript>
    </xsl:template>
    
</xsl:stylesheet>
