<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:b="http://lane.stanford.edu/bassett/ns"
                exclude-result-prefixes="h b"
                version="2.0">

	<xsl:param name="q"/>
	

	<xsl:template match="doc">
		<xsl:apply-templates select="h:html"/>
	</xsl:template>
	
    <xsl:template match="h:a[not(@rel)]/@href">
	  <xsl:choose>
		<xsl:when test="$q">
		   <xsl:attribute name="href">
		     	<xsl:value-of  select="."/>
		     	<xsl:text>&amp;q=</xsl:text>
		     	<xsl:value-of  select="$q"/>
		   </xsl:attribute>
		 </xsl:when>
		<xsl:otherwise>
			 <xsl:copy/>
		</xsl:otherwise>
	  </xsl:choose>
    </xsl:template>
    
 	
   <xsl:template match="h:span">
   		<xsl:copy>
		<xsl:variable name="sub-region-id" select="@id"/>
		<xsl:variable name="region-id" select="./../../../../h:h2/h:span/@id"/>
		<xsl:choose>
			<xsl:when test="/doc/b:bassett_count/b:region[@b:name=$region-id]/b:sub_region[@b:name=$sub-region-id]">
				(<xsl:value-of select="/doc/b:bassett_count/b:region[@b:name=$region-id]/b:sub_region[@b:name=$sub-region-id]"/>)
			</xsl:when>
			<xsl:when test="/doc/b:bassett_count/b:region[@b:name=$sub-region-id]/@b:total">
				(<xsl:value-of select="/doc/b:bassett_count/b:region[@b:name=$sub-region-id]//@b:total"/>)
			</xsl:when>
		</xsl:choose>
		</xsl:copy>
   </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>

	<xsl:template match="*">
	     <xsl:copy>
	         <xsl:apply-templates select="attribute::node()|child::node()"/>
	     </xsl:copy>
	</xsl:template>
	    
</xsl:stylesheet>