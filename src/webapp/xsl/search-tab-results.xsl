<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:res="http://irt.stanford.edu/search/2.0"
                xmlns:st="http://lane.stanford.edu/search-templates/ns"
                xmlns:str="http://lane.stanford.edu/search-tab-results/ns"
                xmlns="http://lane.stanford.edu/search-tab-result/ns"
                exclude-result-prefixes="h st str res"
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
	"hits" : "<xsl:if test="$clinicalHits != 0"><xsl:value-of  select="$clinicalHits" /></xsl:if>",
	"url" :"/search.html?source=clinical&amp;id=<xsl:value-of select="@id"/>"
	}
	,	
	{
	"resource":"history",
	"hits" : "<xsl:if test="$historyHits != 0"><xsl:value-of  select="$historyHits" /></xsl:if>",
	"url":"/search.html?source=history&amp;id=<xsl:value-of select="@id"/>"
	}
	,	
	{
	"resource":"peds",
	"hits" : "<xsl:if test="$pedsHits != 0"><xsl:value-of  select="$pedsHits" /></xsl:if>",
	"url":"/search.html?source=peds&amp;id=<xsl:value-of select="@id"/>"
	}
	,	
	{
	"resource":"research",
	"hits" : "<xsl:if test="$researchHits != 0"><xsl:value-of  select="$researchHits" /></xsl:if>",
	"url":"/search.html?source=research&amp;id=<xsl:value-of select="@id"/>"
	}
	,	
	{
	"resource":"google",
	"hits" : "<xsl:value-of  select="res:engine[@id='google']/res:hits" />",
	"url":"<xsl:value-of select="res:engine[@id='google']/res:resource/res:url"/>"
	}
	,	
	{
	"resource":"lois",
	"hits" : "<xsl:value-of  select="res:engine[@id='lois']/res:hits" />",
	"url":"<xsl:value-of select="res:engine[@id='lois']/res:resource/res:url"/>"
	}
	,	
	{
	"resource":"pubmed",
	"hits" : "<xsl:value-of  select="res:engine[@id='crossdb']/res:hits" />",
	"url":"<xsl:value-of select="res:engine[@id='crossdb']/res:resource[@id='pubmed']/res:url"/>"
	}
  ]
 }
}  
    </xsl:template>
 
   
   
   
   
</xsl:stylesheet>