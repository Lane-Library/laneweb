<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

    <xsl:import href="laneclasses-common.xsl" />

    <xsl:template match="/classes">
        <html>
            <body>
            	<xsl:apply-templates select="class" />
            </body>
        </html>
    </xsl:template>
	
    <xsl:template match="description">
        <xsl:choose>
            <xsl:when test="count(tokenize(., '\W+')[. != ''])  &gt; number(50)">
                 <xsl:call-template name="firstWords">
                    <xsl:with-param name="value" select="."/>
                    <xsl:with-param name="count" select="number(50)"/>
                 </xsl:call-template>
                <xsl:text>...</xsl:text>
                    <a href="/classes-consult/archive.html?class-id={../id}"> More <i class="fa fa-arrow-right"/></a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="class">
    	<div class="archive">
            <div class="yui3-g">
                <div class="yui3-u-1-8">
                    <div class="date">
                       <div class="month-day"><xsl:value-of select="./dates/date/month"/></div>
                       <div class="year"><xsl:value-of select="./dates/date/year"/></div>
                    </div>
                </div>
                <div class="yui3-u-7-12">
                    <div class="details">
                        <h4>
                            <a href="/classes-consult/archive.html?class-id={id}">
                                <xsl:value-of select="name" />
                            </a>
                        </h4>
                        <div>
                            <xsl:apply-templates select="description" />
                        </div>
                    </div>
                </div>
               <xsl:apply-templates select="moreInfoUrl[string-length() &gt; 0]"/> 
            </div>
        </div>
    </xsl:template>
    
     <xsl:template match="moreInfoUrl">
        <xsl:variable name="schemeless-url">
            <xsl:choose>
                <xsl:when test="contains(.,'://')">
                    <xsl:value-of select="substring-after(., ':')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <div class="yui3-u-7-24">
            <div class="youtube-class">
                <xsl:choose>
                    <xsl:when test="contains($schemeless-url, 'youtube')">
                        <iframe src="{$schemeless-url}" width="200" height="120" frameborder="0" allowfullscreen="true"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <a class="button" href="{$schemeless-url}">
                            <span><i class="icon fa fa-video-camera"/> Watch Video</span>
                            <i class="icon fa fa-arrow-right"/>
                        </a>
                        <div>SUNet ID required</div>
                    </xsl:otherwise>
                </xsl:choose>
            </div>
        </div>
    </xsl:template>


   </xsl:stylesheet>
