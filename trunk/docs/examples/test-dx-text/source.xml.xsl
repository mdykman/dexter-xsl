<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
<!-- generated by dexter-0.3.0-beta (copyright (c) 2007-2009 Michael Dykman) from `source.xml'  -->
</xsl:output>
<xsl:template match="/">
		<xsl:element name="tests">			<xsl:text>
	</xsl:text>
		<xsl:apply-templates mode="ade0a0dbe95822886" select="data/fake"/>
			<xsl:text>
	</xsl:text>
		<xsl:apply-templates mode="a3f08b2f6b74ffda9" select="data"/>
			<xsl:text>

</xsl:text>
		</xsl:element>

	</xsl:template>
<xsl:template match="data/fake" mode="ade0a0dbe95822886">				<xsl:element name="div"><xsl:attribute name="id"><xsl:text>group-1</xsl:text><xsl:if test="last() &gt; 1">-<xsl:value-of select="generate-id()"/></xsl:if></xsl:attribute>					<xsl:text>
		</xsl:text>
									<xsl:element name="div"><xsl:attribute name="id"><xsl:text>test-1</xsl:text><xsl:if test="last() &gt; 1">-<xsl:value-of select="generate-id()"/></xsl:if></xsl:attribute><xsl:attribute name="name">found</xsl:attribute><xsl:value-of select="."/></xsl:element>

									<xsl:text>
		</xsl:text>
									<xsl:element name="div"><xsl:attribute name="id"><xsl:text>test-2</xsl:text><xsl:if test="last() &gt; 1">-<xsl:value-of select="generate-id()"/></xsl:if></xsl:attribute><xsl:attribute name="name">found</xsl:attribute><xsl:choose><xsl:when test="."><xsl:text>the value is `</xsl:text><xsl:value-of select="."/><xsl:text>'</xsl:text></xsl:when><xsl:otherwise><xsl:text/></xsl:otherwise></xsl:choose></xsl:element>

									<xsl:text>
	</xsl:text>
				</xsl:element>

			</xsl:template>
<xsl:template match="data" mode="a3f08b2f6b74ffda9">				<xsl:element name="div"><xsl:attribute name="id"><xsl:text>group-2</xsl:text><xsl:if test="last() &gt; 1">-<xsl:value-of select="generate-id()"/></xsl:if></xsl:attribute>					<xsl:text>
		</xsl:text>
									<xsl:element name="div"><xsl:attribute name="id"><xsl:text>test-3</xsl:text><xsl:if test="last() &gt; 1">-<xsl:value-of select="generate-id()"/></xsl:if></xsl:attribute><xsl:attribute name="name">valueoffound</xsl:attribute><xsl:value-of select="test/@value"/></xsl:element>

									<xsl:text>
	</xsl:text>
				</xsl:element>

			</xsl:template>
</xsl:stylesheet>