<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:pom="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    
    <xsl:template match="attribute::node()">
        <xsl:copy-of select="self::node()"/>
    </xsl:template>
    
    <xsl:template match="child::node()">
        <xsl:copy>
            <xsl:apply-templates select="attribute::node() | child::node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!--<xsl:template match="pom:dependencies">
        <xsl:copy>
            <xsl:apply-templates select="pom:dependency[not(pom:scope)] | pom:dependency[pom:scope[.='compile']]">
                <xsl:sort select="pom:artifactId"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="pom:dependency[pom:scope[.='runtime']]">
                <xsl:sort select="pom:artifactId"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="pom:dependency[pom:scope[.='provided']]">
                <xsl:sort select="pom:artifactId"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="pom:dependency[pom:scope[.='system']]">
                <xsl:sort select="pom:artifactId"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="pom:dependency[pom:scope[.='test']]">
                <xsl:sort select="pom:artifactId"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>-->
    
    <xsl:template match="pom:dependencies | pom:exclusions">
        <xsl:copy>
            <xsl:apply-templates>
                <xsl:sort select="pom:artifactId"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="pom:dependency | pom:exclusion">
        <xsl:copy>
            <xsl:apply-templates select="child::node()[not(self::pom:exclusions)]">
                <xsl:sort select="name()"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="child::pom:exclusions"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="pom:dependency/pom:scope[.='compile']"/>
    
    <xsl:template match="pom:dependency/pom:type[.='jar']"/>
    
</xsl:stylesheet>
