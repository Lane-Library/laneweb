<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0">
    
    <xsl:template match="/">
        <rss version="2.0">
            <channel>
                <title>FindIt@Stanford Highly Accessed Articles</title>
                <link>http://lane.stanford.edu</link>
                <description>Highly Accessed Articles from Lane Medical Library's FindIt@Stanford Service</description>
                <language>en-us</language>
                <ttl>1440</ttl>
                <image>
                    <title>LaneConnex</title>
                    <url>http://lane.stanford.edu/favicon.ico</url>
                    <link>http://lane.stanford.edu</link>
                </image>
                <xsl:apply-templates select="RESULTS/ITEM"/>
            </channel>
        </rss>
    </xsl:template>

    <xsl:template match="ITEM">
        <xsl:variable name="link">
            <xsl:choose>
                <xsl:when test="PMID">
                    <xsl:value-of select="concat('http://www.ncbi.nlm.nih.gov/pubmed/',PMID,'?otool=stanford&amp;holding=F1000,F1000M')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="OPEN_URL"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <item>
            <title><xsl:value-of select="ATITLE"/></title>
            <link><xsl:value-of select="$link"/></link>
            <category><xsl:value-of select="TITLE"/>, <xsl:value-of select="substring-before(DATE_OF_PUBLICATION,'-')"/></category>
            <xsl:apply-templates select="PMID"/>
        </item>
    </xsl:template>

    <xsl:template match="PMID">
        <guid isPermaLink="false">PubMed:<xsl:value-of select="."/></guid>        
    </xsl:template>
        

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
