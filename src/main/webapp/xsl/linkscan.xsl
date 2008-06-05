<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:sql="http://apache.org/cocoon/SQL/2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                exclude-result-prefixes="sql">
    
    <xsl:template match="sql:rowset">
        <ul>
            <xsl:apply-templates/>
        </ul>
    </xsl:template>
    
    
  <xsl:template match="sql:row">
    <li> #<xsl:value-of select="position()"/>
        <ul>
            <li>
                <a href="{sql:url}"> id: <xsl:value-of select="concat(sql:record_type,'-',sql:record_id)"/> title: <xsl:value-of select="sql:title"/></a>
            </li>
        </ul>
    </li>
  </xsl:template>

</xsl:stylesheet>
