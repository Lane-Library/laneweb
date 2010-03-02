<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:dir="http://apache.org/cocoon/directory/2.0"
    version="2.0">
        
    <xsl:param name="file"/>
    
    <xsl:template match="//dir:file[@name=$file]">
        <xsl:copy>
            <xsl:copy-of select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>
