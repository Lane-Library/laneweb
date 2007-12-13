<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:res="http://irt.stanford.edu/search/2.0"
                xmlns:st="http://lane.stanford.edu/search-templates/ns"
                xmlns="http://lane.stanford.edu/search-tab-result/ns"
                exclude-result-prefixes="st res"
                version="2.0">
    

   
   <xsl:template match="res:search">
         <xsl:variable name="successful" select="res:engine/res:resource[@status='successful']/res:hits[.!=0]" /> 
         <xsl:variable name="clinicalHits" select="count(//st:template[@id='clinical']/st:resource[@idref=$successful/../@id])"/> 
         <xsl:variable name="historyHits" select="count(//st:template[@id='history']/st:resource[@idref=$successful/../@id])"/> 
         <xsl:variable name="pedsHits" select="count(//st:template[@id='peds']/st:resource[@idref=$successful/../@id])"/> 
         <xsl:variable name="researchHits" select="count(//st:template[@id='research']/st:resource[@idref=$successful/../@id])"/>      
{"results":{
"status": "<xsl:value-of select="@status"></xsl:value-of>",
"tabs": [ 
	{
	"resource":"clinical", 
	"hits" : "<xsl:if test="$clinicalHits != 0"><xsl:value-of  select="format-number($clinicalHits, '###,###')" /></xsl:if>"
	},	
	{
	"resource":"history",
	"hits" : "<xsl:if test="$historyHits != 0"><xsl:value-of  select="format-number($historyHits, '###,###')" /></xsl:if>"
	},	
	{
	"resource":"peds",
	"hits" : "<xsl:if test="$pedsHits != 0"><xsl:value-of  select="format-number($pedsHits, '###,###')" /></xsl:if>"
	},	
	{
	"resource":"research",
	"hits" : "<xsl:if test="$researchHits != 0"><xsl:value-of  select="format-number($researchHits, '###,###')" /></xsl:if>"
	},	
	{
	"resource":"google",
	"hits" : "<xsl:if test="res:engine[@id='google']/res:hits"><xsl:value-of  select="format-number(res:engine[@id='google']/res:hits, '###,###')" /></xsl:if>"
	},	
	{
	"resource":"lois",
	"hits" : "<xsl:if test="res:engine[@id='lois']/res:hits"><xsl:value-of  select="format-number(res:engine[@id='lois']/res:hits, '###,###')" /></xsl:if>"
	},	
	{
	"resource":"pubmed",
	"hits" : "<xsl:if test="res:engine[@id='crossdb']/res:hits"><xsl:value-of  select="format-number(res:engine[@id='crossdb']/res:hits, '###,###')" /></xsl:if>"
	}
  ]
 }
}  
    </xsl:template>
 
   
   
   
   
</xsl:stylesheet>