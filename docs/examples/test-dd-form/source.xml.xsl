<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
<!-- generated by dexter-0.3.0-beta (copyright (c) 2007-2009 Michael Dykman) from `source.xml'  -->
</xsl:output>
<xsl:template match="/">
		<xsl:element name="form"><xsl:attribute name="name">userform</xsl:attribute>			<xsl:text>
	</xsl:text>
					<xsl:element name="input"><xsl:attribute name="value"><xsl:value-of select="*/formdata/first"/></xsl:attribute><xsl:attribute name="name">first</xsl:attribute></xsl:element>

					<xsl:text> </xsl:text>
					<xsl:element name="br"/>

					<xsl:text>
	</xsl:text>
					<xsl:element name="input"><xsl:attribute name="value"><xsl:value-of select="*/formdata/last"/></xsl:attribute><xsl:attribute name="name">last</xsl:attribute></xsl:element>

					<xsl:text> </xsl:text>
					<xsl:element name="br"/>

					<xsl:text>
	</xsl:text>
					<xsl:element name="input"><xsl:if test="string(*/formdata/size)='tall'"><xsl:attribute name="checked"><xsl:value-of select="true()"/></xsl:attribute></xsl:if><xsl:attribute name="name">size</xsl:attribute><xsl:attribute name="type">radio</xsl:attribute><xsl:attribute name="value">tall</xsl:attribute></xsl:element>

					<xsl:text>tall
	</xsl:text>
					<xsl:element name="input"><xsl:if test="string(*/formdata/size)='short'"><xsl:attribute name="checked"><xsl:value-of select="true()"/></xsl:attribute></xsl:if><xsl:attribute name="name">size</xsl:attribute><xsl:attribute name="type">radio</xsl:attribute><xsl:attribute name="value">short</xsl:attribute></xsl:element>

					<xsl:text>short </xsl:text>
					<xsl:element name="br"/>

					<xsl:text>
	</xsl:text>
					<xsl:element name="input"><xsl:if test="string(*/formdata/adult)='yes'"><xsl:attribute name="checked"><xsl:value-of select="true()"/></xsl:attribute></xsl:if><xsl:attribute name="name">adult</xsl:attribute><xsl:attribute name="type">checkbox</xsl:attribute><xsl:attribute name="value">yes</xsl:attribute></xsl:element>

					<xsl:text>adult </xsl:text>
					<xsl:element name="br"/>

					<xsl:text>
	</xsl:text>
					<xsl:element name="input"><xsl:if test="string(*/formdata/senior)='yes'"><xsl:attribute name="checked"><xsl:value-of select="true()"/></xsl:attribute></xsl:if><xsl:attribute name="name">senior</xsl:attribute><xsl:attribute name="type">checkbox</xsl:attribute><xsl:attribute name="value">yes</xsl:attribute></xsl:element>

					<xsl:text>senior </xsl:text>
					<xsl:element name="br"/>

					<xsl:text>
	</xsl:text>
					<xsl:element name="textarea"><xsl:attribute name="name">story</xsl:attribute><xsl:value-of select="*/formdata/story"/></xsl:element>

					<xsl:element name="br"/>

					<xsl:text>
	</xsl:text>
					<xsl:element name="input"><xsl:attribute name="value"><xsl:value-of select="*/formdata/clicker"/></xsl:attribute><xsl:attribute name="name">clicker</xsl:attribute><xsl:attribute name="type">submit</xsl:attribute></xsl:element>

					<xsl:text>
</xsl:text>
		</xsl:element>

	</xsl:template>
</xsl:stylesheet>