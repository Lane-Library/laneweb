<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    version="2.0">
    
    <xsl:param name="format"/>
    
	<!-- ncbi returns just a link to a PubMed search when more than the configured RSS limit citations are returned. -->
	<xsl:variable name="linkToCitations" select="count(rss/channel/item) = 1 and matches(rss/channel/item/title,'.*; \+\d+ new citations')"/>
        
    <xsl:template match="h:*">
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
                <ul xmlns="http://www.w3.org/1999/xhtml">
                    <xsl:apply-templates select="channel/item"/>
                </ul>
            </xsl:when>
            <xsl:otherwise>
                <span xmlns="http://www.w3.org/1999/xhtml">
                    <ol class="citationList" xmlns="http://www.w3.org/1999/xhtml">
                        <xsl:apply-templates select="channel/item"/>
                    </ol>
                    <div class="tooltips" style="display:none" xmlns="http://www.w3.org/1999/xhtml">
                        <xsl:apply-templates select="channel/item" mode="tooltips"/>
                    </div>
                </span>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="item">
        <li xmlns="http://www.w3.org/1999/xhtml">
            <xsl:choose>
                <xsl:when test="$linkToCitations">
	                <a href="{link}">
	                    <xsl:value-of select="replace(title,'.*; \+','')"/>
	                </a>
                </xsl:when>
                <xsl:when test="starts-with(guid,'PubMed:')">
                    <a xmlns="http://www.w3.org/1999/xhtml" id="pubmed_{substring-after(guid,':')}" href="{concat('http://www.ncbi.nlm.nih.gov/pubmed/',substring-after(guid,':'),'?otool=stanford&amp;holding=F1000,F1000M')}" title="feed link---{../../channel/title}---{title}"><xsl:value-of select="title"/></a>
                </xsl:when>
                <xsl:otherwise>
                    <a xmlns="http://www.w3.org/1999/xhtml" href="{link}" title="feed link---{../../channel/title}---{title}"><xsl:value-of select="title"/></a>
                </xsl:otherwise>
            </xsl:choose>
            
            <xsl:choose>
                <xsl:when test="$linkToCitations"/>
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
        <div xmlns="http://www.w3.org/1999/xhtml"><xsl:value-of select="."/></div>
    </xsl:template>
    
    <xsl:template match="item" mode="tooltips">
            <xsl:choose>
                <xsl:when test="starts-with(guid,'PubMed:') and description">
                    <span xmlns="http://www.w3.org/1999/xhtml" id="pubmed_{substring-after(guid,':')}Tooltip" style="width:60%">
                        <xsl:apply-templates select="description/*[not(name()='title') and not(name()='table')]"/>
                    </span>
                </xsl:when>
                <xsl:when test="starts-with(guid,'PubMed:')">
                    <span xmlns="http://www.w3.org/1999/xhtml" id="pubmed_{substring-after(guid,':')}Tooltip">
                        <xsl:value-of select="title"/>
                    </span>
                </xsl:when>
            </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
