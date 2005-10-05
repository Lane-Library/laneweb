<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:sql="http://apache.org/cocoon/SQL/2.0"
                exclude-result-prefixes="sql">

  
  <xsl:template match="@*|node()">
      <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
  </xsl:template>
  
  <xsl:template match="sql:rowset">
     <ul class="eresources">
      <xsl:apply-templates/>
    </ul>
  </xsl:template>

  <xsl:template match="sql:row">
       <li class="eresource" id="{concat('bib:',sql:bib_id,';mfhd:',sql:mfhd_id)}"/>
  </xsl:template>

</xsl:stylesheet>
