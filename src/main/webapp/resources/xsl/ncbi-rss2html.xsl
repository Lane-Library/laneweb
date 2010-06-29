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
                <span>
                    <ol class="citationList">
                        <xsl:apply-templates select="channel/item"/>
                    </ol>
                    <div class="tooltips" style="display:none;">
                        <xsl:apply-templates select="channel/item" mode="tooltips"/>
                    </div>
                </span>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="item">
        <li>
            <xsl:choose>
                <xsl:when test="starts-with(guid,'PubMed:')">
                    <a id="pubmed_{substring-after(guid,':')}" href="{concat('http://www.ncbi.nlm.nih.gov/pubmed/',substring-after(guid,':'),'?otool=stanford&amp;holding=F1000,F1000M')}" title="feed link---{../../channel/title}---{title}"><xsl:value-of select="title"/></a>
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
    
    <xsl:template match="item" mode="tooltips">
            <xsl:choose>
                <xsl:when test="starts-with(guid,'PubMed:') and description">
                    <span id="pubmed_{substring-after(guid,':')}Tooltip" style="width:60%">
                        <!-- WARNING: brittle replacement of escaped HTML markup ... if PubMed feed markup changes, this will break -->
                        <xsl:value-of select="replace(normalize-space(description), '(.*&lt;p&gt;&lt;b&gt;|&lt;/?[pb]&gt;)','','m')"/>
                    </span>
                </xsl:when>
                <xsl:when test="starts-with(guid,'PubMed:')">
                    <span id="pubmed_{substring-after(guid,':')}Tooltip">
                        <xsl:value-of select="title"/>
                    </span>
                </xsl:when>
            </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
