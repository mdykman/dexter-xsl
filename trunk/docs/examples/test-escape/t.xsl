<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html" />

	<xsl:template match="/">
		<xsl:apply-templates select="data/names/name" />
	</xsl:template>

	<xsl:template match="data/names/name">
		<xsl:text>'</xsl:text><xsl:apply-templates select="./text()" mode="inner-quote" /><xsl:text>'</xsl:text>,
</xsl:template>

	<xsl:template match="*" name="inner-quote" mode="inner-quote">
		<xsl:param name="param1" select="."/> <!-- <xsl:value-of select="." /></xsl:param> -->
		<xsl:param name="param2" >'</xsl:param>
		<xsl:choose>
			<xsl:when test="contains($param1,$param2)" >
				<xsl:value-of select="substring-before($param1,$param2)" />\'<xsl:call-template name="inner-quote">
					<xsl:with-param name="param1" select="substring-after($param1,$param2)" />
					<xsl:with-param name="param2" select="$param2" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$param1" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
