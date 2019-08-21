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
      <laneblog-rss>
      <xsl:apply-templates
         select="channel/item[category[ . = 'Highlighted Resource'] ]"/>
      </laneblog-rss>
   </xsl:template>
   
   <xsl:template match="item">
      <item>
         <blog-content>
            <content>
               <xsl:value-of select="content:encoded"/>
            </content>
         </blog-content>
         <link>
            <a href="{link}" title="feed link---{../../channel/title}">
               <xsl:value-of select="title"/>
            </a>
         </link>
       </item>
   </xsl:template>
</xsl:stylesheet>