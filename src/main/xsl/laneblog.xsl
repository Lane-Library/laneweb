<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" 
   xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:h="http://www.w3.org/1999/xhtml" 
    version="2.0"
>
   
   <xsl:param name="highlightedresources"/>
   
   
   <xsl:template match="node()">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*"/>
        </xsl:copy>
    </xsl:template>
   
   <xsl:template match="@*">
      <xsl:copy-of select="."/>
   </xsl:template>
   
    <xsl:template match="/">
      <xsl:apply-templates select="h:laneblog-rss/h:item"/>
    </xsl:template>
   
   
   
   <xsl:template match="h:item">
      <div class="pure-u-1-3">
         <div class="newsfeed">
            <xsl:if test="$highlightedresources = 'true'">
               <header>Resources of the Months</header>
            </xsl:if>
            <figure>
               <xsl:copy-of select=".//*:img[1]"/>
               </figure>
            <div class="newsfeed-title">
                <xsl:copy-of select="h:link/node()"/>
            </div>
            <hr/>   
            <section>
               <xsl:for-each select="./h:blog-content//h:article[position() &gt; 1]">
                  <xsl:apply-templates/>
               </xsl:for-each>
                 
            </section>
         </div>
      </div>
   </xsl:template>


    <xsl:template match="h:img">
        <xsl:copy>
             <xsl:attribute name="src" select="replace(@src,'http:','')" />
        </xsl:copy>
    </xsl:template>
   
</xsl:stylesheet>