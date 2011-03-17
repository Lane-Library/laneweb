<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
    xmlns:lc="http://lane.stanford.edu/laneclasses" exclude-result-prefixes="lc h" xmlns="http://lane.stanford.edu/laneclasses"
    version="2.0">

    <xsl:template match="/lc:classes">
        <xsl:variable name="time"><xsl:value-of select="current-time()" /></xsl:variable>
        <xsl:variable name="date"><xsl:value-of select="current-date()" /></xsl:variable>
        <xsl:copy>
            <lastmodified>
                <date>
                    <xsl:value-of select="year-from-date($date)" /><xsl:text>-</xsl:text><xsl:value-of select="month-from-date($date)" /><xsl:text>-</xsl:text><xsl:value-of select="day-from-date($date)" />
                </date>
                <time>
                    <xsl:value-of select="hours-from-time($time)" /><xsl:text>:</xsl:text><xsl:value-of select="minutes-from-time($time)" /><xsl:text>:00</xsl:text>
                </time>
            </lastmodified>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>


    <xsl:template match="lc:custom_fields_set_1"></xsl:template>
    <xsl:template match="lc:standart_fields_set"></xsl:template>
    <xsl:template match="lc:optional_fees"></xsl:template>
    <xsl:template match="lc:terms_conditions"></xsl:template>
    <xsl:template match="lc:custom_fields_set_2"></xsl:template>
    <xsl:template match="lc:custom_fields_set_3"></xsl:template>
    <xsl:template match="lc:payment_options"></xsl:template>
    <xsl:template match="lc:early_bird_date"></xsl:template>
    <xsl:template match="lc:early_bird_fee"></xsl:template>
    <xsl:template match="lc:event_fee"></xsl:template>
    <xsl:template match="lc:cancellation_information"></xsl:template>
    <xsl:template match="lc:lodging_information"></xsl:template>
    <xsl:template match="lc:contact_info"></xsl:template>



    <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:copy-of select="." />
    </xsl:template>

</xsl:stylesheet>