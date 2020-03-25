<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rss="http://purl.org/rss/1.0/"
   xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
   exclude-result-prefixes="rss h content" version="2.0"
>
  
   <xsl:template match="node()">
      <xsl:copy>
         <xsl:apply-templates select="node()|@*"/>
      </xsl:copy>
      
   </xsl:template>
   <xsl:template match="@*">
      <xsl:copy-of select="."/>
   </xsl:template>
   
   <xsl:template match="rss">
      <xsl:apply-templates select="./channel/item[category[ . = 'Highlighted Resource'] and  count(./content:encoded//h:article) &gt; 0 ] [position() &lt; 2]"/>
   </xsl:template>
   
   <xsl:template match="item">
      <div class="pure-u-1-3">
         <div class="newsfeed">
             <header>Resource of the Month</header>
            <figure>
               <xsl:copy-of select="./content:encoded/h:article//h:img[1]"/>
               </figure>
            <div class="newsfeed-title">
               <a href="{link}" title="feed link---{../../channel/title}">
                  <xsl:value-of select="title"/>
               </a>
            </div>
            <hr/>   
            <section>
               <xsl:for-each select="./content:encoded//h:article[position() &gt; 1]">
                  <xsl:apply-templates/>
               </xsl:for-each>
            </section>
         </div>
      </div>        
   </xsl:template>
 
</xsl:stylesheet>