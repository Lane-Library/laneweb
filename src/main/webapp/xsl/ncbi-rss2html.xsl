<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://www.w3.org/1999/xhtml"
    version="2.0">
	
	<xsl:param name="format"/>
	
    <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="rss">
    	<xsl:choose>
	    	<xsl:when test="$format = 'brief'">
		        <ul>
		            <xsl:apply-templates select="channel/item"/>
		        </ul>
	    	</xsl:when>
	    	<xsl:otherwise>
		        <ol class="citationList">
		            <xsl:apply-templates select="channel/item"/>
		        </ol>
	    	</xsl:otherwise>
    	</xsl:choose>
    </xsl:template>
    
    <xsl:template match="item">
        <li>
            <xsl:choose>
                <xsl:when test="starts-with(guid,'PubMed:')">
                    <a href="{concat('http://sfx.stanford.edu/local?sid=Entrez:PubMed&amp;id=pmid:',substring-after(guid,':'))}" title="feed link---{../../channel/title}---{title}"><xsl:value-of select="title"/></a>
                </xsl:when>
                <xsl:otherwise>
                    <a href="{link}" title="feed link---{../../channel/title}---{title}"><xsl:value-of select="title"/></a>
                </xsl:otherwise>
            </xsl:choose>
            
            <xsl:choose>
	            <xsl:when test="$format = 'brief'">
					<xsl:text> </xsl:text>
					<xsl:value-of select="category"/>
	            </xsl:when>
	            <xsl:otherwise>
		            <xsl:apply-templates select="category|author"/>
	            </xsl:otherwise>
            </xsl:choose>
        </li>
    </xsl:template>

    <xsl:template match="category|author">
        <div><xsl:value-of select="."/></div>
    </xsl:template>
</xsl:stylesheet>
