<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rss="http://purl.org/rss/1.0/"
   xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:h="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml"
   exclude-result-prefixes="rss content" version="2.0"
>
   <xsl:variable name="max-word-number">
      50
   </xsl:variable>
   <xsl:template match="node()">
      <xsl:copy>
         <xsl:apply-templates select="node()|@*"/>
      </xsl:copy>
   </xsl:template>
   
   <xsl:template match="@*">
      <xsl:copy-of select="."/>
   </xsl:template>
   
   <xsl:template match="rss">
      <xsl:apply-templates
         select="channel/item[category[ . = 'Highlighted Resource']  and contains(./content:encoded, '&lt;article&gt;')][position() &lt; 2]"/>
   </xsl:template>
   
   
   <xsl:template match="item">
      <div class="pure-u-1-3">
         <div class="newsfeed">
            <header>Resources of the Months</header>
            <figure>
            <content>
               <xsl:value-of select="content:encoded"/>
            </content>
            </figure>
            <div class="newsfeed-title">
               <a href="{link}" title="feed link---{../../channel/title}">
                  <xsl:value-of select="title"/>
               </a>
            </div>
            <hr/>   
            <section>
            <content>
               <xsl:choose>
                  <xsl:when test="count(tokenize(content:encoded, '\W+')[. != ''])  &gt; $max-word-number">
                     <xsl:variable name="words" select="tokenize(content:encoded, ' ')"/>
                     <xsl:for-each select="$words[position() &lt;= $max-word-number]">
                        <xsl:value-of select="."/>
                        <xsl:text> </xsl:text>
                     </xsl:for-each>
                     <xsl:text>...</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:value-of select="content:encoded"/>
                  </xsl:otherwise>
               </xsl:choose>
            </content>
            </section>
         </div>
      </div>
   </xsl:template>
</xsl:stylesheet>