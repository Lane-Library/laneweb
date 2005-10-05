<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0">
    
    <xsl:template match="sql:rowset">
        <counts>
            <xsl:apply-templates/>
        </counts>
    </xsl:template>
    
    <xsl:template match="sql:row">
        <sql:execute-query>
            <sql:query>select '<xsl:value-of select="sql:day"/>' as day, count(*) as count from km_prod.links where to_char(create_date,'YYYY-MM-DD') = '<xsl:value-of select="sql:day"/>' and referer like 'http://lane-beta%'</sql:query>
        </sql:execute-query>
    </xsl:template>

</xsl:stylesheet>
