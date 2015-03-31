<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="xml" />


<xsl:template name="lookup">
	<xsl:param name="key" />
	<xsl:param name="data" />
	<xsl:value-of select="$data/data/entry[@key=$key]/*|$data/data/entry[@key=$key]/text()" />
</xsl:template>


<xsl:template name="replace-chars">
	<xsl:param name="param1" select="."/>
	<xsl:param name="param2" />
	<xsl:param name="param3" />
	<xsl:choose>
		<xsl:when test="contains($param1,$param2)" >
			<xsl:value-of select="substring-before($param1,$param2)" />
			<xsl:value-of select="$param3" />
			<xsl:call-template name="replace-chars">
				<xsl:with-param name="param1" select="substring-after($param1,$param2)" />
				<xsl:with-param name="param2" select="$param2" />
				<xsl:with-param name="param3" select="$param3" />
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$param1" />
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="escape-quotes">
	<xsl:param name="param1" select="."/>
	<xsl:param name="param2" >'</xsl:param>
	<xsl:param name="param3" >\</xsl:param>
	<xsl:choose>
		<xsl:when test="contains($param1,$param2)" >
			<xsl:value-of select="substring-before($param1,$param2)" />
			<xsl:value-of select="$param3" />
			<xsl:value-of select="$param2" />
			<xsl:call-template name="escape-quotes">
				<xsl:with-param name="param1" select="substring-after($param1,$param2)" />
				<xsl:with-param name="param2" select="$param2" />
				<xsl:with-param name="param3" select="$param3" />
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$param1" />
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

	
<xsl:template name="csv">
	<xsl:param name="param1" ><xsl:copy-of select="." /></xsl:param> 
	<xsl:param name="param2" >,</xsl:param> 
	<xsl:apply-templates select="$param1" mode="csv" >
		<xsl:with-param name="param1" select="$param2" />
	</xsl:apply-templates>
</xsl:template>

<xsl:template match="*" mode="csv" >
	<xsl:param name="param1">,</xsl:param>
	<xsl:for-each select="descendant-or-self::*[string-length(normalize-space(text()))> 0]">
 		<xsl:value-of select="." />
 		<xsl:if test="position() != last()" >
 			<xsl:value-of select="$param1" />
 		</xsl:if> 
	</xsl:for-each>
</xsl:template>

<xsl:template name="json">
	<xsl:param name="param1" select="." />
	<xsl:apply-templates select="$param1" mode="js.data"/>
</xsl:template>

<xsl:template name="js.inner-data" mode="js.data" match="*">
	<xsl:choose>
		<xsl:when test="last() = 1 and item">
			<xsl:call-template name="js.list" >
				<xsl:with-param name="param1" select="." />
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="local-name(*[1]) and local-name(*[1]) = local-name(*[2])">
			<xsl:call-template name="js.list" >
				<xsl:with-param name="param1" select="." />
			</xsl:call-template>
		</xsl:when>
		<xsl:when test="*">
			<xsl:call-template name="js.hash" >
				<xsl:with-param name="param1" select="." />
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:call-template name="js.scalar" />
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="js.scalar" >
	<xsl:choose>
		<xsl:when test="string(number(.)) = 'NaN'">
			<xsl:call-template name="js.str" />
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="." />
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="find-extension">
	<xsl:param name="param1" />
	<xsl:call-template name="last-index-of">
		<xsl:with-param name="str" select="$param1"/>
		<xsl:with-param name="mc">.</xsl:with-param>
	</xsl:call-template>
</xsl:template>

<xsl:template name="gossamer-image">
	<xsl:param name="param1" />
	<xsl:param name="param2" />
	<xsl:variable name="n">
		<xsl:call-template name="find-extension">
			<xsl:param name="param1" select="$param1"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:choose>
		<xsl:when test="$n >= 0">
			<xsl:value-of select="substring($param1,0,$n)" />
			<xsl:value-of select="$param2" />
			<xsl:value-of select="substring($param1,$n)" />
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$param1" />
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="last-index-of">
	<xsl:param name="str"/>
	<xsl:param name="mc"/>
	<xsl:param name="index" select="string-length(str)" />
	<xsl:choose>
		<xsl:when test="$index == 0" >
			<xsl:value-of select= "number(-1)" />
		</xsl:when>
		<xsl:when test="starts-with(substring($str,$index),$mc)" >
			<xsl:value-of select="$index" />
		</xsl:when>
		<xsl:otherwise>
			<xsl:call-template name="last-index-of">
				<xsl:with-param name="str" select="$str" />
				<xsl:with-param name="mc" select="$mc" />
				<xsl:with-param name="index" select="$index-1" />
			</xsl:call-template>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="escape-all" >
	<xsl:param name="param1" select="." />
	<xsl:param name="param2" />
	<xsl:param name="param3" />

</xsl:template>

<xsl:template name="js.str" >
	<xsl:param name="param1" select="."/>
	<xsl:text>'</xsl:text>

<xsl:call-template name="replace-chars">
	<xsl:with-param name="param1">
		<xsl:call-template name="replace-chars">
			<xsl:with-param name="param1" >
				<xsl:call-template name="escape-quotes">
					<xsl:with-param name="param1" >
						<xsl:call-template name="escape-quotes">
							<xsl:with-param name="param1" select="$param1" />
							<xsl:with-param name="param2" >\</xsl:with-param>
							<xsl:with-param name="param3" >\</xsl:with-param>
						</xsl:call-template>
					</xsl:with-param>
					<xsl:with-param name="param2" >'</xsl:with-param>
					<xsl:with-param name="param3" >\</xsl:with-param>
				</xsl:call-template>
			</xsl:with-param>
			<xsl:with-param name="param2"><xsl:text>&#x0A;</xsl:text></xsl:with-param>
			<xsl:with-param name="param3" select="'\n'" />
		</xsl:call-template>
	</xsl:with-param>
	<xsl:with-param name="param2"><xsl:text>&#x0D;</xsl:text></xsl:with-param>
	<xsl:with-param name="param3" select="'\r'" />
</xsl:call-template>

	<xsl:text>'</xsl:text>
</xsl:template>

<xsl:template name="js.list">
	<xsl:param name="param1" select="."/>
	<xsl:text>[ </xsl:text>
	<xsl:for-each select="*" >
		<xsl:call-template name="js.inner-data"/>
 		<xsl:if test="position()!=last()" >
 			<xsl:text>, </xsl:text>
 		</xsl:if>
	</xsl:for-each>
	<xsl:text>] </xsl:text>
</xsl:template>

<xsl:template name="js.hash">
	<xsl:param name="param1"  select="." />
	<xsl:text>{ </xsl:text>
	<xsl:for-each select="*" >
		<xsl:value-of select="local-name(.)" />
		<xsl:text>:</xsl:text>
		<xsl:call-template name="js.inner-data" />
 		<xsl:if test="position()!=last()" >
 			<xsl:text>, </xsl:text>
 		</xsl:if>
	</xsl:for-each>
	<xsl:text> }</xsl:text>
</xsl:template>

</xsl:stylesheet>
