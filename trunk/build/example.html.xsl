<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
<!-- generated by dexter from `example.html'  -->
</xsl:output><xsl:template match="/"><xsl:element name="select"><xsl:attribute name="name">thing</xsl:attribute><xsl:text>
	</xsl:text><xsl:for-each select="result/pets/pet"><xsl:variable name="DexterDepthLevel1"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="option"><xsl:attribute name="value"><xsl:value-of select="id"/></xsl:attribute><xsl:if test="string(/result/default) = string(id)"><xsl:attribute name="selected"><xsl:value-of select="&quot;true&quot;"/></xsl:attribute></xsl:if><xsl:value-of select="name"/></xsl:element></xsl:for-each><xsl:text>
</xsl:text></xsl:element></xsl:template></xsl:stylesheet>