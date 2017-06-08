<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"  
     version="2.0">

    <xsl:import href="archives.xsl" />

    <xsl:param name="class-id" />

    <xsl:template match="/classes">
        <html>
            <body>
                <xsl:apply-templates select="class[id = $class-id]" />
            </body>
        </html>
    </xsl:template>

     <xsl:template match="description">
         <div class="class-description">
             <xsl:copy-of select="node()"/>
         </div>
         <xsl:apply-templates select="../handout"/>
    </xsl:template>

    <xsl:template match="handout">
    	<div class="handout-description" >
            <xsl:copy-of select="node()"/>
		</div>
    </xsl:template>

</xsl:stylesheet>
