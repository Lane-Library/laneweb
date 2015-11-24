<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml" xmlns:b="http://lane.stanford.edu/bassett/ns" exclude-result-prefixes="h b"
    version="2.0">

    <xsl:param name="query" />


    <xsl:template match="doc">
        <xsl:apply-templates select="h:html" />
    </xsl:template>

	<xsl:template match="@href">
        <xsl:choose>
            <xsl:when test="$query and not(ends-with(., 'index.html'))">
                <xsl:attribute name="href">
                 <xsl:value-of select="." />
                 <xsl:text>&amp;q=</xsl:text>
                 <xsl:value-of select="$query"/> 
           </xsl:attribute>
         </xsl:when>
        <xsl:otherwise>
             <xsl:copy/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>


	<xsl:template match="h:span">
		<xsl:variable name="sub-region-id" select="@id" />
		<xsl:variable name="region-id" select="ancestor::h:ul/@id" />
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()|child::node()" />
			<xsl:value-of select="/doc/b:bassett_count/b:region[@b:name=$region-id]/b:sub_region[@b:name=$sub-region-id]" />
		</xsl:copy>
	</xsl:template>

    <xsl:template match="h:a">
        <xsl:variable name="sub-region-id" select="./../h:span[1]/@id"/>
        <xsl:variable name="region-id" select="ancestor::h:ul/@id"/>
        <xsl:variable name="class" select="@class"/>
        <xsl:choose>
        	<xsl:when test="@class != ''">
	        	<xsl:copy>
	        		<xsl:apply-templates select="attribute::node()|child::node()" />
	        	</xsl:copy>
        	</xsl:when>
            <xsl:when test="/doc/b:bassett_count/b:region[@b:name=$region-id]/b:sub_region[@b:name=$sub-region-id] != 0 or /doc/b:bassett_count/b:region[@b:name=$sub-region-id]/@b:total != 0">
                <xsl:copy>
                    <xsl:apply-templates select="attribute::node()|child::node()"/> 
                </xsl:copy>
            </xsl:when>
         </xsl:choose>
   </xsl:template>
 
  <xsl:template match="h:a[@class = 'see-all']">
        <xsl:variable name="region-id" select="ancestor::h:ul/@id"/>
         <xsl:if test="count(/doc/b:bassett_count/b:region[@b:name=$region-id]/b:sub_region) &gt; 4"> 
                <xsl:copy>
                	<xsl:apply-templates select="attribute::node()|child::node()"/> 
                 </xsl:copy> 
     	 </xsl:if>
   </xsl:template>
 	
 
 
 <xsl:template match="h:ul">
        <xsl:variable name="region-id" select="@id"/>
        	<xsl:choose>
            <xsl:when test="count(/doc/b:bassett_count/b:region[@b:name=$region-id]/b:sub_region) != 0 ">
                 <xsl:copy>
                    <xsl:apply-templates select="attribute::node()|child::node()"/> 
                </xsl:copy>
             </xsl:when>
         </xsl:choose> 
   </xsl:template>
   
 	<xsl:template match="h:li">
 		 <xsl:variable name="sub-region-id" select="./h:span/@id"/>
        <xsl:variable name="region-id" select="ancestor::h:ul/@id"/>
        	<xsl:choose>
            <xsl:when test="count(/doc/b:bassett_count/b:region[@b:name=$region-id]/b:sub_region[@b:name=$sub-region-id]) != 0 ">
                 <xsl:copy>
                    <xsl:apply-templates select="attribute::node()|child::node()"/> 
                </xsl:copy>
             </xsl:when>
             <xsl:when test="./@class != ''">
              <xsl:copy>
             	 <xsl:apply-templates select="attribute::node()|child::node()"/>
             	</xsl:copy> 
             </xsl:when> 
         </xsl:choose> 
   </xsl:template>
   
 	 
    <xsl:template match="node()">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="@*">
    <xsl:copy-of select="."/>
  </xsl:template>
        
</xsl:stylesheet>