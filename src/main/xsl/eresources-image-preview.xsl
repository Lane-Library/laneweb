<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:h="http://www.w3.org/1999/xhtml"
   xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="h"
>
   <xsl:param name="facets"/>
   <xsl:param name="url-encoded-query"/>
   <xsl:param name="token"/>
   
   <!-- placement of image search results -->
   <xsl:variable name="images-promo-position">3</xsl:variable>
   
   <!-- number of images to show in search results -->
   <xsl:variable name="images-promo-show">5</xsl:variable>
   <xsl:variable name="images-promo-enabled" select="'true'"/>
   <xsl:variable name="images-url">
      <xsl:value-of select="concat('/search.html?source=images-all&amp;q=',$url-encoded-query)"/>
   </xsl:variable>
   
   
   
   <xsl:template match="doc">
      <xsl:apply-templates select="h:html"/>
   </xsl:template>
   <xsl:template match="h:ul[@class='lwSearchResults']/h:li">
      <xsl:copy>
         <xsl:apply-templates select="node() | @*"/>
         <xsl:if test="$images-promo-position = position()">
            <xsl:call-template name="images-promotion"/>
         </xsl:if>
      </xsl:copy>
   </xsl:template>
   
   
   <xsl:template name="images-promotion">
      <xsl:if test="$images-promo-enabled and $facets = ''">
         <xsl:variable name="imageUrl" select="concat('cocoon://apps/search/image/preview?q=',$url-encoded-query)"/>
         <xsl:if test="count(/doc/list/string) >= $images-promo-show">
            <li class="no-bookmarking">
               <span class="primaryType" style="display:none;">Image Search Promo</span>
               <a class="primaryLink" href="{$images-url}" title="More images from Lane's Bio-Image Search">Results from Lane's Bio-Image Search</a>
               <span class="imageSurvey">
                  <span class="surveyLinks">
                  alain <xsl:value-of select="$token"/>     Useful?
                     <a href="#">
                        <i class="fa fa-smile-o fa-lg" aria-hidden="true"></i>
                        Yes
                     </a>
                     <a href="#">
                        <i class="fa fa-frown-o fa-lg" aria-hidden="true"></i>
                        No
                     </a>
                  </span>
                  <span class="surveySent">
                     Thank you for your feedback! Please send further suggestions to
                     <a href="/help/feedback.html#askus" rel="lightbox">
                        <i class="fa fa-envelope fa-fw"></i>
                        Ask Us
                     </a>
                     .
                  </span>
               </span>
               <div id="imageList" class="searchPromo">
                  <div class="pure-g">
                     <xsl:apply-templates select="/doc/list/string[position() &lt;= $images-promo-show]"/>
                  </div>
               </div>
            </li>
         </xsl:if>
      </xsl:if>
   </xsl:template>
   
   
   <xsl:template match="string">
      <div class="pure-u-1-5">
         <a href="{$images-url}" title="More images from Bio-Image Search">
            <img>
               <xsl:attribute name="src" select="text()"/>
            </img>
         </a>
      </div>
   </xsl:template>
   
   <xsl:template match="node()">
      <xsl:copy>
         <xsl:apply-templates select="node() | @*"/>
      </xsl:copy>
   </xsl:template>
   
   <xsl:template match="@*">
      <xsl:copy-of select="."/>
   </xsl:template>
</xsl:stylesheet>