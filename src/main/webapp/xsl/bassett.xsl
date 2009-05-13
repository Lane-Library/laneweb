<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:h="http://www.w3.org/1999/xhtml"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:b="http://lane.stanford.edu/bassett/ns"
                exclude-result-prefixes="h b"
                version="2.0">

<xsl:param name="q"/>
<xsl:param name="type"/> 
<xsl:param name="region"/>
<xsl:param name="images-url"/>
<xsl:param name="images-per-page"/>
<xsl:param name="current-page"/>


<xsl:variable name="thumbnail-directory"><xsl:value-of select="$images-url"/>/small/</xsl:variable>
<xsl:variable name="large-image-directory"><xsl:value-of select="$images-url"/>/large/</xsl:variable>
<xsl:variable name="medium-image-directory"><xsl:value-of select="$images-url"/>/medium/</xsl:variable>
<xsl:variable name="image-directory"><xsl:if test="$type != 'largerView'"><xsl:value-of select="$medium-image-directory"/></xsl:if><xsl:if test="$type = 'largerView'"><xsl:value-of select="$large-image-directory"/></xsl:if></xsl:variable>
<xsl:variable name="page-number"><xsl:if test="$current-page != ''"><xsl:value-of select="number($current-page)"/></xsl:if><xsl:if test="$current-page = ''"><xsl:value-of select="number(0)"/></xsl:if></xsl:variable>
<xsl:variable name="imgs-per-page"><xsl:value-of select="number($images-per-page)"/></xsl:variable>

<xsl:variable name="query-string">
	<xsl:if test="$q != ''">q=<xsl:value-of select="$q"/></xsl:if>
	<xsl:if test="$q != '' and $region != ''"><xsl:text>&amp;</xsl:text></xsl:if>
	<xsl:if test="$region != ''">r=<xsl:value-of select="$region"/></xsl:if>
	<xsl:if test="$type != ''">&amp;t=<xsl:value-of select="$type"/></xsl:if>
</xsl:variable>


<xsl:variable name="query"><xsl:value-of select="replace($query-string,'&amp;t=diagram','')"/> </xsl:variable>
	<xsl:template match="*">
	     <xsl:copy>
	         <xsl:apply-templates select="attribute::node()|child::node()"/>
	     </xsl:copy>
	</xsl:template>
	    
	<xsl:template match="doc">
		<xsl:apply-templates select="h:html"/>
	</xsl:template>

	  <!-- To Give the content title -->  
	    
     <xsl:template match="h:span[@id='bassett-title']">
      	<xsl:copy>
      	   <xsl:apply-templates select="attribute::node()|child::node()"/>
      		<xsl:choose>
				<xsl:when test="$q">
					<xsl:text>Search Term: </xsl:text>
					<xsl:value-of select="$q"/>
				</xsl:when>
				<xsl:when test="$region">
					<xsl:value-of select="replace($region,'--',': ')"/>
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="/doc/b:bassetts/b:bassett/b:regions/b:region[1]/@b:name"/>:</xsl:otherwise>
			</xsl:choose>
		</xsl:copy>
		        	
     </xsl:template> 
     
	<xsl:template match="h:span[@id='bassett-subtitle']">
		 <xsl:variable name="sub-titles">
			<xsl:for-each select="/doc/b:bassetts/b:bassett/b:regions/b:region[1]/b:sub_region"><xsl:value-of select="."/>, </xsl:for-each>
		</xsl:variable>
     	<xsl:copy>
     			<xsl:apply-templates select="attribute::node()"/>
				<xsl:value-of select="substring($sub-titles ,1, string-length($sub-titles)-2) "/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="h:div[@id='second-region']">
		<xsl:if test="/doc/b:bassetts/b:bassett/b:regions/b:region[2]/@b:name">
			<xsl:copy>
				<xsl:apply-templates select="attribute::node()|child::node()"/>
    		    <xsl:value-of select="/doc/b:bassetts/b:bassett/b:regions/b:region[2]/@b:name"/>
			</xsl:copy>
		</xsl:if>
	</xsl:template>     
	
	<xsl:template match="h:h2[@id='bassett-number']">
		<xsl:copy>
			<xsl:apply-templates select="attribute::node()|child::node()"/>
			<xsl:value-of select="/doc/b:bassetts/b:bassett/@b:bassett_number"/>
		</xsl:copy>
	</xsl:template>     
	
	
	
    <!-- on multiple View to give link digrams or photos  -->
    
    
    <xsl:template match="h:span[@class='choice']">
		<xsl:choose>
  			<xsl:when test="count(/doc/b:bassetts/b:bassett) != 0">
  				<xsl:copy>
  				     <xsl:apply-templates select="attribute::node()|child::node()"/>
	    		</xsl:copy>
  			</xsl:when>
  			<xsl:otherwise>
  				<xsl:text>No images for search term </xsl:text>
  				<b><xsl:value-of select="$q"/></b>
  				<xsl:text> were found in this region.</xsl:text>
  			</xsl:otherwise>
  		</xsl:choose>
	</xsl:template> 
    
    
    <xsl:template match="h:a[@id='photo-choice']/@href">
		<xsl:attribute name="href">
      	    <xsl:value-of select="."/>
      	   	<xsl:value-of select="$query"/>    	
      	   	<xsl:text>&amp;pageNumber=</xsl:text>
      	   	<xsl:value-of select="$page-number"/>
      	</xsl:attribute> 
     </xsl:template> 
    
    <xsl:template match="h:a[@id='diagram-choice']/@href">
    	<xsl:attribute name="href">
      	    <xsl:value-of select="."/>
      	   	<xsl:value-of select="$query"/>
      	   	<xsl:text>&amp;t=diagram&amp;pageNumber=</xsl:text>
      	   	<xsl:value-of select="$page-number"/>
      	</xsl:attribute>
     </xsl:template> 
     
    <!-- Multiple View Generate div for the images  --> 
 
    <xsl:template match="h:div[@id='thumbnail']">
    	<xsl:if test="/doc/b:bassetts/b:bassett">
    	<xsl:copy>	
	    	<xsl:apply-templates select="attribute::node()"/>
		    	<xsl:for-each select="/doc/b:bassetts/b:bassett">
		     	<xsl:if test="position() &gt; $imgs-per-page * $page-number and position() &lt;= $imgs-per-page * $page-number + $imgs-per-page">
		     		<div>
		     			<xsl:attribute name="class">image-container</xsl:attribute>
			     		<div>
			     		<xsl:attribute name="class">hr</xsl:attribute>
						   	<a> 
					           	<xsl:attribute name="href"  >
					        		<xsl:text>/bassett/bassettView.html?bn=</xsl:text><xsl:value-of select="./@b:bassett_number"/>
					        	</xsl:attribute>
					     		<img>
					     		  	<xsl:attribute name="title"  >
						        		<xsl:value-of select="./b:title"/>
						        	</xsl:attribute>
						        	<xsl:attribute name="src"  >
						        		<xsl:value-of select="$thumbnail-directory"/>
						        		<xsl:choose>
						        			<xsl:when test="$type='diagram'">
						        				<xsl:value-of select="./b:diagram_image"/>
						        			</xsl:when>
						        			<xsl:otherwise>
						        				<xsl:value-of select="./b:bassett_image"/>
						        			</xsl:otherwise>
						        		</xsl:choose>
						        	</xsl:attribute>
						        	<xsl:attribute name="alt"  >
						        		<xsl:text>bassett Number </xsl:text><xsl:value-of select="./@b:bassett_number"/>
						        	</xsl:attribute>
						      	</img>
						      	</a>
				      		</div>
				      	<div>
				      	<xsl:attribute name="class">image-text</xsl:attribute>
				 		<xsl:text>#</xsl:text><xsl:value-of select="./@b:bassett_number"/>
						<br/>
						<a> 
			     		  	<xsl:attribute name="title"  >
				        		<xsl:value-of select="./b:title"/>
				        	</xsl:attribute>
				        	<xsl:attribute name="href"  >
				        		<xsl:text>/bassett/bassettView.html?bn=</xsl:text><xsl:value-of select="./@b:bassett_number"/>
				        	</xsl:attribute>
				        	<xsl:text> View Larger</xsl:text>
				       </a>
				       </div>
					</div>
					</xsl:if>
				</xsl:for-each>
		    </xsl:copy>
	    </xsl:if>
	</xsl:template>    
    
<!-- To get the Image source for bassettView.html and bassettLagerView.html -->

<xsl:template match="h:td[@id='image']/h:a/h:img/@src | h:td[@id='image']/h:img/@src">
	<xsl:attribute name="src">
		<xsl:value-of select="$image-directory"/>
		<xsl:value-of select="/doc/b:bassetts/b:bassett/b:bassett_image"/>
	</xsl:attribute>
</xsl:template>
 
  
<xsl:template match="h:td[@id='diagram-image']/h:a/h:img/@src | h:td[@id='diagram-image']/h:img/@src">		      
	<xsl:attribute name="src">
		<xsl:value-of select="$image-directory"/>
		<xsl:value-of select="/doc/b:bassetts/b:bassett/b:diagram_image"/>
	</xsl:attribute>
</xsl:template>

<xsl:template match="h:td[@id='legend-image']/h:a/h:img/@src | h:td[@id='legend-image']/h:img/@src">		      
	<xsl:attribute name="src">
		<xsl:value-of select="$image-directory"/>
		<xsl:value-of select="/doc/b:bassetts/b:bassett/b:legend_image"/>
	</xsl:attribute>
</xsl:template>
  
<!-- Legend table -->  
<xsl:template match="h:*[@class='legend-title']">
	<xsl:copy>
		<xsl:apply-templates select="attribute::node()"/>
		<xsl:value-of select="upper-case(substring-before(/doc/b:bassetts/b:bassett/b:title, '.'))"/>
	</xsl:copy>
	 
</xsl:template>
	
<xsl:template match="h:*[@class='legend-sub-title']">
   <xsl:copy>
   		<xsl:apply-templates select="attribute::node()"/>
   		<xsl:value-of select="substring-after(/doc/b:bassetts/b:bassett/b:title, '.')"/>
   </xsl:copy>
</xsl:template>
	
<xsl:template match="h:td[@class='legend-description']">
	<xsl:copy>
		<xsl:apply-templates select="attribute::node()"/>
		<xsl:value-of select="/doc/b:bassetts/b:bassett/b:description"/>
	</xsl:copy>
</xsl:template>
	 
	 
<xsl:template match="h:div[@id='english-legend']">
	<xsl:for-each select="tokenize(/doc/b:bassetts/b:bassett/b:legend, '--')">
		<xsl:if test="substring-before(.,'.') != ''">
			<tr><td><xsl:value-of select="substring-before(.,'.')"/>.</td><td><xsl:value-of select="substring-after(.,'.')"/></td></tr>
		</xsl:if> 	
	</xsl:for-each>
</xsl:template>


<!-- to get the Href for all links that will open a new window for the bassettLargerView.html -->
<xsl:template match="h:a[starts-with(@rel,'popup')]/@href">
	<xsl:attribute name="href">
		<xsl:text>/bassett/raw/bassettLargerView.html?t=largerView&amp;bn=</xsl:text>
		<xsl:value-of select="/doc/b:bassetts/b:bassett/@b:bassett_number"/>
	</xsl:attribute>
</xsl:template>

<!-- paging  -->

<xsl:variable name="total-images">
	<xsl:value-of select="count(/doc/b:bassetts/b:bassett)"/>
</xsl:variable>

<xsl:variable name="total-pages">
	<xsl:value-of select="substring-before( string($total-images div $imgs-per-page),'.')"/>
</xsl:variable>

<xsl:template match="h:div[@class='paging']">
	<xsl:if test="$total-pages != '0' and $total-pages != ''">
		<xsl:copy>
		    <xsl:apply-templates select="attribute::node()|child::node()"/>
		</xsl:copy>
	</xsl:if>
</xsl:template>

<xsl:template match="h:td[@id='page-number']">
	<xsl:copy>
		<xsl:apply-templates select="attribute::node()|child::node()"/>
		<xsl:text>Images </xsl:text>
		<xsl:value-of select="string(($imgs-per-page * $page-number)+1)"/>
		<xsl:text >-</xsl:text>
		<xsl:choose>
		<xsl:when test="$page-number != $total-pages">
			<xsl:value-of select="string($imgs-per-page * ($page-number+1))"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="string($total-images - (($total-pages - $page-number)* $imgs-per-page) )"/>
		</xsl:otherwise>
		</xsl:choose>
		<xsl:text > of </xsl:text>
		<xsl:value-of select="$total-images"></xsl:value-of>
	</xsl:copy>
</xsl:template>


<xsl:template match="h:a[@id='first-page']/@href">
	<xsl:attribute name="href">
		<xsl:text>/bassett/bassettsView.html?</xsl:text>
		<xsl:value-of select="$query-string"/>
	</xsl:attribute>
</xsl:template>

<xsl:template match="h:a[@id='previous-page']/@href">
	<xsl:attribute name="href">
		<xsl:text>/bassett/bassettsView.html?</xsl:text>
		<xsl:value-of select="$query-string"/>
		<xsl:if test="$page-number != 0">
			<xsl:text>&amp;pageNumber=</xsl:text><xsl:value-of select="number($page-number - 1)"></xsl:value-of>
		</xsl:if>
	</xsl:attribute>
</xsl:template>

<xsl:template match="h:a[@id='next-page']/@href">
	<xsl:attribute name="href">
		<xsl:text>/bassett/bassettsView.html?</xsl:text>
		<xsl:value-of select="$query-string"/>
		<xsl:if test="$total-pages != $page-number">
			<xsl:text>&amp;pageNumber=</xsl:text><xsl:value-of select="number($page-number + 1)"></xsl:value-of>
		</xsl:if>
		<xsl:if test="$total-pages = $page-number">
			<xsl:text>&amp;pageNumber=</xsl:text><xsl:value-of select="$total-pages"/>
		</xsl:if>
	</xsl:attribute>
</xsl:template>

<xsl:template match="h:a[@id='last-page']/@href">
	<xsl:attribute name="href">
		<xsl:text>/bassett/bassettsView.html?</xsl:text>
		<xsl:value-of select="$query-string"/>
		<xsl:text>&amp;pageNumber=</xsl:text><xsl:value-of select="$total-pages"/>
	</xsl:attribute>
</xsl:template>

<!-- End  paging -->


<xsl:template match="attribute::node()">
    <xsl:copy-of select="self::node()"/>
</xsl:template>
</xsl:stylesheet>