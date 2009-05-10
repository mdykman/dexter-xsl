<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
<!-- generated by dexter-0.3.0-beta (copyright (c) 2007-2009 Michael Dykman) from `source.xml'  -->
</xsl:output>
<xsl:template match="/">
<xsl:apply-templates mode="affcc2b27b7bafbac" select="data"/>
</xsl:template>
<xsl:template match="data" mode="affcc2b27b7bafbac">			<xsl:element name="tests">				<xsl:text>
	</xsl:text>
							<xsl:element name="div"><xsl:attribute name="src"><xsl:value-of select="uri"/></xsl:attribute><xsl:attribute name="class"><xsl:value-of select="'general'"/></xsl:attribute><xsl:attribute name="id"><xsl:text>test-1</xsl:text><xsl:if test="last() &gt; 1">-<xsl:value-of select="generate-id()"/></xsl:if></xsl:attribute><xsl:attribute name="name">found</xsl:attribute></xsl:element>

							<xsl:text>
	</xsl:text>
							<xsl:element name="div"><xsl:attribute name="fakeattribute"><xsl:choose><xsl:when test="fake"><xsl:text>the value is `</xsl:text><xsl:value-of select="fake"/><xsl:text>'</xsl:text></xsl:when><xsl:otherwise><xsl:text/></xsl:otherwise></xsl:choose></xsl:attribute><xsl:attribute name="id"><xsl:text>test-2</xsl:text><xsl:if test="last() &gt; 1">-<xsl:value-of select="generate-id()"/></xsl:if></xsl:attribute><xsl:attribute name="name">found</xsl:attribute></xsl:element>

							<xsl:text>
	</xsl:text>
							<xsl:element name="option"><xsl:if test="test/@value='n'"><xsl:attribute name="checked"/></xsl:if><xsl:attribute name="id"><xsl:text>test-3</xsl:text><xsl:if test="last() &gt; 1">-<xsl:value-of select="generate-id()"/></xsl:if></xsl:attribute><xsl:attribute name="name">thing</xsl:attribute><xsl:attribute name="value">C</xsl:attribute></xsl:element>

							<xsl:text>
	</xsl:text>
							<xsl:element name="option"><xsl:if test="test/@value='y'"><xsl:attribute name="checked"/></xsl:if><xsl:attribute name="id"><xsl:text>test-4</xsl:text><xsl:if test="last() &gt; 1">-<xsl:value-of select="generate-id()"/></xsl:if></xsl:attribute><xsl:attribute name="name">thing</xsl:attribute><xsl:attribute name="value">D</xsl:attribute></xsl:element>

							<xsl:text>
</xsl:text>
			</xsl:element>

		</xsl:template>
</xsl:stylesheet>