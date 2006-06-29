<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                       xmlns:h="http://www.w3.org/1999/xhtml"
                       xmlns:java="http://xml.apache.org/xalan/java/edu.stanford.laneweb.xslt.LanewebXSLTExtensions"
                       exclude-result-prefixes="h java"
                       version="1.0">

	<xsl:output method='xml' omit-xml-declaration='no' indent="yes"/>

	<xsl:param name="q" />

	<xsl:template match="h:html">
		<response>
		<xsl:for-each select="h:body/h:table/h:tr/h:td/h:table/h:tr/h:td/h:a">
			<xsl:variable name="matches">
				<xsl:apply-templates select="h:b[java:contains(self::h:b,$q)]"/>
			</xsl:variable>
			<xsl:if test="string-length($matches) &gt; 0 and string-length($q) &gt; 2">
				<result>
					<name><xsl:value-of select="h:b"/></name>
					<link>
						<xsl:choose>
							<xsl:when test="contains(substring(@href,1,1),'/')">
								<xsl:value-of select="concat('http://library.stanford.edu',@href)"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="@href"/>
							</xsl:otherwise>
						</xsl:choose>
					</link>
				</result>
			</xsl:if>
		</xsl:for-each>
		</response>
	</xsl:template>
    
</xsl:stylesheet>
