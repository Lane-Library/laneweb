<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                       xmlns:h="http://www.w3.org/1999/xhtml"
                       xmlns="http://www.w3.org/1999/xhtml"
                       xmlns:math="http://exslt.org/math"
                       exclude-result-prefixes="h math"
                       version="1.0">

    <!--
     return a random div element from a node set of divs
     Use:
        <xi:include xmlns:xi="http://www.w3.org/2001/XInclude"
           href="content:/random?source=content:/random-data.html"><xi:fallback/>
        </xi:include
        where random-data.html is a simple html doc with n child divs of the body tag
        
     Note: this is a version 1.0 stylesheet because it uses xalan, which only knows about 1.0
           and not saxon because later versions of saxon don't know about exslt extensions.
    -->    

    <xsl:template match="/">
        <xsl:copy-of select="/h:html/h:body/h:div[floor(math:random() * count(/h:html/h:body/h:div) + 1 )]"/>
    </xsl:template>


</xsl:stylesheet>
