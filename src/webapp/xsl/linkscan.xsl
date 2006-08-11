<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns:sql="http://apache.org/cocoon/SQL/2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                exclude-result-prefixes="sql h">
    
  <xsl:template match="sql:row">
	<li> #<xsl:value-of select="position()"/>
		<ul>
			<li>
				<a href="{sql:url}"> id: <xsl:value-of select="sql:version_id"/> title: <xsl:value-of select="sql:title"/></a>
			</li>
		</ul>
	</li>
  </xsl:template>

</xsl:stylesheet>
