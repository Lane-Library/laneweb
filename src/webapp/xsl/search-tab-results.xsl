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
         <search-tab-results id="{@id}" status="{@status}" >
            
            <resource id="clinical">
            	<xsl:variable name="clinicalHits" select="count(//st:template[@id='clinical']/st:resource[@idref=$successful/../@id])"/> 
            	<xsl:if test="$clinicalHits != 0">
            		<hits> <xsl:value-of  select="$clinicalHits" /></hits>
            	</xsl:if>
	        	<url><xsl:text>/search.html?source=clinical&amp;id=</xsl:text><xsl:value-of select="@id"/></url>
		   </resource>
			<resource id="history">
			<xsl:variable name="historyHits" select="count(//st:template[@id='history']/st:resource[@idref=$successful/../@id])"/> 
            	<xsl:if test="$historyHits != 0">
            		<hits> <xsl:value-of  select="$historyHits" /></hits>
            	</xsl:if>
	        <url><xsl:text>/search.html?source=history&amp;id=</xsl:text><xsl:value-of select="@id"/></url>
		   </resource>
			<resource id="peds">
	      		<xsl:variable name="pedsHits" select="count(//st:template[@id='peds']/st:resource[@idref=$successful/../@id])"/> 
            	<xsl:if test="$pedsHits != 0">
            		<hits> <xsl:value-of  select="$pedsHits" /></hits>
            	</xsl:if>
	        	<url><xsl:text>/search.html?source=peds&amp;id=</xsl:text><xsl:value-of select="@id"/></url>
		   </resource>	            
      		<resource id="research">
	        	<xsl:variable name="researchHits" select="count(//st:template[@id='research']/st:resource[@idref=$successful/../@id])"/> 
            	<xsl:if test="$researchHits != 0">
            		<hits> <xsl:value-of  select="$researchHits" /></hits>
            	</xsl:if>
	        	<url><xsl:text>/search.html?source=research&amp;id=</xsl:text><xsl:value-of select="@id"/></url>
		   </resource>
		   <xsl:apply-templates/>
        </search-tab-results>
   </xsl:template>   
   
   
      
     
    <xsl:template match="res:search/res:engine[@id='google']">
        <resource id="{@id}">
        	<hits><xsl:value-of select="res:hits"/></hits>
        	<url><xsl:value-of select="res:resource/res:url"/></url>
            <xsl:apply-templates/>
        </resource>
    </xsl:template>

 	<xsl:template match="res:search/res:engine[@id='lois']">
        <resource id="{@id}">
        	<hits><xsl:value-of select="res:hits"/></hits>
        	<url><xsl:value-of select="res:resource/res:url"/></url>
            <xsl:apply-templates/>
        </resource>
    </xsl:template>


    <xsl:template match="res:search/res:engine[@id='crossdb']/res:resource[@id='pubmed']">
        <resource id="{@id}">
        	<hits><xsl:value-of select="res:hits"/></hits>
        	<url><xsl:value-of select="res:url"/></url>
        	<xsl:apply-templates/>
        </resource>
    </xsl:template>
   
     <xsl:template match="res:search/res:engine[@id='lane']/res:resource[@id='lane_all']">
        <resource id="er">
        	<hits><xsl:value-of select="res:hits"/></hits>
        	<xsl:apply-templates/>
        </resource>
    </xsl:template>
   
     <xsl:template match="res:search/res:engine[@id='lane']/res:resource[@id='biotools']">
        <resource id="{@id}">
        	<hits><xsl:value-of select="res:hits"/></hits>
        	<xsl:apply-templates/>
        </resource>
    </xsl:template>
    
   
     <xsl:template match="res:search/res:engine[@id='lane']/res:resource[@id='lane_videos']">
        <resource id="video">
        	<hits><xsl:value-of select="res:hits"/></hits>
        	<xsl:apply-templates/>
        </resource>
    </xsl:template>
    
   
     <xsl:template match="res:search/res:engine[@id='lane']/res:resource[@id='calculators']">
        <resource id="cc">
        	<hits><xsl:value-of select="res:hits"/></hits>
           <xsl:apply-templates/>
        </resource>
    </xsl:template>
    
   
     <xsl:template match="res:search/res:engine[@id='lane']/res:resource[@id='lanedatabase']">
        <resource id="database">
        	<hits><xsl:value-of select="res:hits"/></hits>
            <xsl:apply-templates/>
        </resource>
    </xsl:template>
    
   
     <xsl:template match="res:search/res:engine[@id='lane']/res:resource[@id='lanefaq']">
        <resource id="lanefaq">
        	<hits><xsl:value-of select="res:hits"/></hits>
            <xsl:apply-templates/>
        </resource>
    </xsl:template>
       
     <xsl:template match="res:search/res:engine[@id='lane']/res:resource[@id='lanebook']">
        <resource id="book">
        	<hits><xsl:value-of select="res:hits"/></hits>
            <xsl:apply-templates/>
        </resource>
    </xsl:template>
    
    
     <xsl:template match="res:search/res:engine[@id='lane']/res:resource[@id='lane_ej']">
        <resource id="ej">
        	<hits><xsl:value-of select="res:hits"/></hits>
            <xsl:apply-templates/>
        </resource>
    </xsl:template>
    
    
    
     
      <xsl:template match="text()"/>
   
   
</xsl:stylesheet>