<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:sql="http://apache.org/cocoon/SQL/2.0">
    
    <xsl:output method="text"/>
    
    <xsl:template match="sql:server">
        <xsl:value-of select="."/>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>
    
    <xsl:template match="text()"/>
    
</xsl:stylesheet>
