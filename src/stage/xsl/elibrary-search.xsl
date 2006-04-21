<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:h="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="h">
        
    <xsl:template match="/aggregate">
        <xsl:apply-templates select="h:html"/>
    </xsl:template>
     
    <xsl:template match="h:html//h:div[@id='lw_metaEngines']">
		<xsl:element name="script">
			<xsl:attribute name="type">text/javascript</xsl:attribute>
			<xsl:value-of select="/aggregate/h:javascript-array[1]"/>
			<xsl:value-of select="/aggregate/h:javascript-array[2]"/>
			<xsl:value-of select="/aggregate/h:javascript-array[3]"/>
		</xsl:element>
    </xsl:template>

    <xsl:template match="*">
         <xsl:copy>
             <xsl:apply-templates select="@*|node()"/>
         </xsl:copy>
    </xsl:template>
      
    <xsl:template match="@*">
       <xsl:copy-of select="."/>
     </xsl:template>
     
 </xsl:stylesheet>
