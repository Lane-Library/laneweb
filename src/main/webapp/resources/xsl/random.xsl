<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="h" version="2.0">

    <!--
     return a random div element from a node set of divs
     Use:
	    <xi:include xmlns:xi="http://www.w3.org/2001/XInclude" 
	        href="cocoon:/random/cocoon:/random-data.html">
	      <xi:fallback/>
	    </xi:include>
     where random-data.html is a simple html doc with n child divs of the body tag
    -->
    
	<xsl:variable name="not-so-random-int" select="number(concat('0.',format-time(current-time(),'[f]')))"/>

	<xsl:template match="/">
		<xsl:copy-of
			select="/h:html/h:body/h:div[floor(number($not-so-random-int) * count(/h:html/h:body/h:div) + 1 )]" />
	</xsl:template>


</xsl:stylesheet>
