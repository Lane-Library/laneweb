<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:s="http://lane.stanford.edu/spellcheck/ns"
    version="2.0">
    
    
    
<xsl:template match="/">{
	"query": "<xsl:value-of select="encode-for-uri(//s:query)"/>",
	"suggestion": "<xsl:value-of select="//s:suggestion"/>"
}
</xsl:template>

</xsl:stylesheet>