<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
<!-- generated by dexter from `source.xml'  -->
</xsl:output><xsl:template match="/"><xsl:element name="form">
	<xsl:element name="input"><xsl:attribute name="name">first</xsl:attribute><xsl:value-of select="*/formdata/first"/></xsl:element>
</xsl:element></xsl:template></xsl:stylesheet>