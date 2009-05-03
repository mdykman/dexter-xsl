<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
<!-- generated by dexter-0.3.0-beta (copyright (c) 2007-2009 Michael Dykman) from `source.xml'  -->
</xsl:output>
<xsl:template match="/">
		<xsl:element name="div">			<xsl:text>
	</xsl:text>
		<xsl:for-each select="data/names/name"><xsl:variable name="DexterDepthLevel1"><xsl:value-of select="position()"/></xsl:variable>				<xsl:element name="div"><xsl:attribute name="onlick"><xsl:choose><xsl:when test="."><xsl:text>return collect(</xsl:text><xsl:call-template name="json"><xsl:with-param name="param1" select="."/></xsl:call-template><xsl:text>);</xsl:text></xsl:when><xsl:otherwise><xsl:text/></xsl:otherwise></xsl:choose></xsl:attribute></xsl:element>

			</xsl:for-each>			<xsl:text>
	---------------------------------------
	</xsl:text>
		<xsl:for-each select="data/person"><xsl:variable name="DexterDepthLevel1"><xsl:value-of select="position()"/></xsl:variable>				<xsl:element name="div">					<xsl:text>
		first </xsl:text>
									<xsl:element name="span"><xsl:value-of select="first"/></xsl:element>

									<xsl:text>
		middle </xsl:text>
									<xsl:element name="span"><xsl:value-of select="middle"/></xsl:element>

									<xsl:text>
		last </xsl:text>
									<xsl:element name="span"><xsl:value-of select="last"/></xsl:element>

									<xsl:text>

	</xsl:text>
									<xsl:element name="a"><xsl:attribute name="href"><xsl:choose><xsl:when test="."><xsl:text>javascript:dosomething(</xsl:text><xsl:call-template name="json"><xsl:with-param name="param1" select="."/></xsl:call-template><xsl:text>)</xsl:text></xsl:when><xsl:otherwise><xsl:text/></xsl:otherwise></xsl:choose></xsl:attribute>						<xsl:text>do something</xsl:text>
					</xsl:element>

									<xsl:text>
</xsl:text>
				</xsl:element>

			</xsl:for-each>			<xsl:text>
</xsl:text>
		</xsl:element>

	</xsl:template>
<xsl:template name="json">
	<xsl:param name="param1" select="."/>
	<xsl:apply-templates mode="js.data" select="$param1"/>
</xsl:template><xsl:template match="*" mode="js.data" name="js.inner-data">
	<xsl:choose>
		<xsl:when test="last() = 1 and item">
			<xsl:call-template name="js.list">
				<xsl:with-param name="param1" select="."/>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="local-name(*[1]) and local-name(*[1]) = local-name(*[2])">
			<xsl:call-template name="js.list">
				<xsl:with-param name="param1" select="."/>
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="*">
			<xsl:call-template name="js.hash">
				<xsl:with-param name="param1" select="."/>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:call-template name="js.scalar"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template><xsl:template name="js.list">
	<xsl:param name="param1" select="."/>
	<xsl:text>[ </xsl:text>
	<xsl:for-each select="*">
		<xsl:call-template name="js.inner-data"/>
 		<xsl:if test="position()!=last()">
 			<xsl:text>, </xsl:text>
 		</xsl:if>
	</xsl:for-each>
	<xsl:text>] </xsl:text>
</xsl:template><xsl:template name="js.hash">
	<xsl:param name="param1" select="."/>
	<xsl:text>{ </xsl:text>
	<xsl:for-each select="*">
		<xsl:value-of select="local-name(.)"/>
		<xsl:text>:</xsl:text>
		<xsl:call-template name="js.inner-data"/>
 		<xsl:if test="position()!=last()">
 			<xsl:text>, </xsl:text>
 		</xsl:if>
	</xsl:for-each>
	<xsl:text> }</xsl:text>
</xsl:template><xsl:template name="js.scalar">
	<xsl:choose>
		<xsl:when test="string(number(.)) = 'NaN'">
			<xsl:call-template name="js.str"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="."/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template><xsl:template name="js.str">
	<xsl:param name="param1" select="."/>
	<xsl:text>'</xsl:text>
	<xsl:call-template name="escape-quotes">
		<xsl:with-param name="param1">
			<xsl:call-template name="escape-quotes">
				<xsl:with-param name="param1" select="$param1"/>
				<xsl:with-param name="param2">\</xsl:with-param>
				<xsl:with-param name="param3">\</xsl:with-param>
			</xsl:call-template>
		</xsl:with-param>
		<xsl:with-param name="param2">'</xsl:with-param>
		<xsl:with-param name="param3">\</xsl:with-param>
	</xsl:call-template>
	<xsl:text>'</xsl:text>
</xsl:template><xsl:template name="escape-quotes">
	<xsl:param name="param1" select="."/>
	<xsl:param name="param2">'</xsl:param>
	<xsl:param name="param3">\</xsl:param>
	<xsl:choose>
		<xsl:when test="contains($param1,$param2)">
			<xsl:value-of select="substring-before($param1,$param2)"/>
			<xsl:value-of select="$param3"/>
			<xsl:value-of select="$param2"/>
			<xsl:call-template name="escape-quotes">
				<xsl:with-param name="param1" select="substring-after($param1,$param2)"/>
				<xsl:with-param name="param2" select="$param2"/>
				<xsl:with-param name="param3" select="$param3"/>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$param1"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template></xsl:stylesheet>