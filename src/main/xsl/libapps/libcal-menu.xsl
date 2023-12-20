<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="2.0">
    <xsl:template match="/classes">
        <html>
            <body>
                <div class="menu-container mobile hoverline no-bookmarking">
                    <h2>Categories
                       <i class="fa-regular fa-angle-down fa-lg"></i>
                       <i class="fa-regular fa-angle-up fa-lg"></i>
                    </h2>
                    <ul>
                        <li>
                            <a href="/calendar/classes/?cid=12663&amp;t=d">By Date</a>
                        </li>
                        <xsl:for-each-group select="./class/categories//category" group-by="name">
                            <xsl:sort select="." />
                            <xsl:apply-templates select="name" />
                        </xsl:for-each-group>
                    </ul>
                </div>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="name">
        <li>
            <a>
                <xsl:attribute name="href">
                     <xsl:value-of select="concat('/calendar/classes/?cid=12663&amp;t=d&amp;ct=', ../id )" />                               
                  </xsl:attribute>
                <xsl:value-of select="." />
            </a>
        </li>
    </xsl:template>
</xsl:stylesheet>