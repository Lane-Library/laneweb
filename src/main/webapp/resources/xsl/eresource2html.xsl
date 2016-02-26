<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://lane.stanford.edu/resources/1.0"
    exclude-result-prefixes="h s" version="2.0">
    
    <xsl:param name="type"/>
    
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node()|child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="."/>
    </xsl:template>

    <!-- transforms eresource result node into displayable -->
    <xsl:template match="s:result[@type='eresource']">
        <xsl:variable name="available" select="number(s:available)"/>
        <li>
            <xsl:if test="contains(s:primaryType, 'Book')">
                <img class="bookcover" data-bibid="{s:recordId}"/>
            </xsl:if>
            <xsl:apply-templates select="s:link[not(starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=')) or position() = 1]"/>
            <xsl:apply-templates select="s:pub-author"/>
            <xsl:apply-templates select="s:pub-text"/>
            <div class="resultInfo">
                <xsl:apply-templates select="s:primaryType"/>
                <xsl:if test="contains(s:primaryType,'Print') and $available &gt; 0">
                    <span>Status: Not Checked Out</span>
                </xsl:if>
                <xsl:if test="s:description">
                    <span class="descriptionTrigger eresource"/>
                </xsl:if>
                
                
                <xsl:if test="s:recordType = 'bib'">
                    <span>
                        <a href="http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">Lane Catalog Record</a>
                    </span>
                </xsl:if>
                
                <xsl:if test="s:recordType = 'auth'">
                    <span>
                        <a href="http://cifdb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID={s:recordId}">Lane Community Info Record</a>
                    </span>
                </xsl:if>
                
            </div>
            <xsl:apply-templates select="s:description"/>
            <xsl:apply-templates select="s:link[position() > 1 and starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=')]"/>
        </li>
    </xsl:template>
    
    <xsl:template match="s:description">
        <div class="description">
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="s:link[1]">
        <div>
            <a class="primaryLink" href="{s:url}" title="{../s:title}">
                <xsl:value-of select="../s:title" />
            </a>
        </div>
        <xsl:if test="s:holdings-dates">
            <a href="{s:url}" title="{../s:title}">
                <xsl:value-of select="s:holdings-dates" />
            </a>
        </xsl:if>
        <xsl:if test="@type = 'getPassword'">
            <a href="/secure/ejpw.html" title="Get Password"> Get Password</a>
        </xsl:if>
        <xsl:if test="s:additional-text">
            <xsl:text> </xsl:text>
            <xsl:value-of select="s:additional-text" />
        </xsl:if>
    </xsl:template>

    <xsl:template match="s:link">
        <xsl:variable name="print" select="starts-with(s:url,'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=')"/>
        <div>
            <xsl:if test="$print">Also available: </xsl:if>
            <a href="{s:url}" title="{s:label}">
                <xsl:if test="$print">Print &#8211; </xsl:if>
                <xsl:value-of select="s:link-text"/>
            </a>
            <xsl:if test="s:additional-text">
                <xsl:text> </xsl:text>
                <xsl:value-of select="s:additional-text"/>
            </xsl:if>
            <xsl:if test="@type = 'getPassword'">
                <xsl:text> </xsl:text>
                <a href="/secure/ejpw.html" title="Get Password">Get Password</a>
            </xsl:if>
        </div>
    </xsl:template>

    <xsl:template match="s:link[@type = 'impactFactor']">
        <div>
            <a href="{s:url}">Impact Factor</a>
        </div>
    </xsl:template>

    <xsl:template match="s:primaryType">
        <xsl:if test="contains('ejbook',$type)">
            <span><strong>
                <xsl:choose>
                    <xsl:when test="contains(., 'Print')">Print</xsl:when>
                    <xsl:otherwise>Digital</xsl:otherwise>
                </xsl:choose>
            </strong></span>
        </xsl:if>
    </xsl:template>

    <xsl:template match="s:pub-author">
        <div>
            <xsl:value-of select="."/>
        </div>
    </xsl:template>
    
    <xsl:template match="s:pub-text">
        <xsl:apply-templates/>
    </xsl:template>

</xsl:stylesheet>
