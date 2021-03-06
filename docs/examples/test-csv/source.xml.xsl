<!DOCTYPE xsl:stylesheet>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html"><!--  generated by dexter-0.9-beta (copyright (c) 2007-2010 Michael Dykman, Lauren Enterprises) from `source.xml'   --></xsl:output>
<xsl:preserve-space elements="*"></xsl:preserve-space>

<xsl:template match="/">

<div><xsl:text>
	</xsl:text><xsl:if test="string-length(data/names)"><xsl:call-template name="csv"><xsl:with-param name="param1" select="data/names"></xsl:with-param></xsl:call-template></xsl:if><xsl:text>
	</xsl:text><xsl:if test="string-length(data/names)"><xsl:call-template name="section"><xsl:with-param name="param1" select="data/names"></xsl:with-param></xsl:call-template></xsl:if><xsl:text>
</xsl:text></div>

</xsl:template>

<xsl:template name="csv">
	<xsl:param name="param1"><xsl:copy-of select="."></xsl:copy-of></xsl:param> 
	<xsl:param name="param2">,</xsl:param> 
	<xsl:apply-templates mode="csv" select="$param1">
		<xsl:with-param name="param1" select="$param2"></xsl:with-param>
	</xsl:apply-templates>
</xsl:template>

<xsl:template match="*" mode="csv">
	<xsl:param name="param1">,</xsl:param>
	<xsl:for-each select="descendant-or-self::*[string-length(normalize-space(text()))> 0]">
 		<xsl:value-of select="."></xsl:value-of>
 		<xsl:if test="position() != last()">
 			<xsl:value-of select="$param1"></xsl:value-of>
 		</xsl:if> 
	</xsl:for-each>
</xsl:template>

<xsl:template name="section">
	<xsl:param name="param1"></xsl:param> 
	<xsl:call-template name="csv">
		<xsl:with-param name="param1" select="$param1"></xsl:with-param> 
		<xsl:with-param name="param2"> :: </xsl:with-param>
	</xsl:call-template>
</xsl:template>

</xsl:stylesheet>