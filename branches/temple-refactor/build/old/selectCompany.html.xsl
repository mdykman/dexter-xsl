<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
<!-- generated by dexter from `xsl/default/xhtml-1.0/selectCompany.html'  -->
</xsl:output><xsl:template match="/"><xsl:element name="div">
	<xsl:element name="form">
		<xsl:for-each select=".//alphabet/*"><xsl:element name="div"><xsl:attribute name="onclick"><xsl:choose><xsl:when test="./text()"><xsl:text><![CDATA[return getCompanyByLetterNode(']]></xsl:text><xsl:value-of select="."/><xsl:text><![CDATA[');]]></xsl:text></xsl:when><xsl:otherwise><xsl:text/></xsl:otherwise></xsl:choose></xsl:attribute><xsl:attribute name="class">floatleft click</xsl:attribute><xsl:value-of select="."/></xsl:element></xsl:for-each>	
		<!--<div class="clearboth"></div>--><xsl:element name="br"/>
	<xsl:element name="div"><xsl:attribute name="class"/><xsl:attribute name="id"><xsl:text><![CDATA[companylist]]></xsl:text></xsl:attribute>
		<xsl:for-each select=".//Company"><xsl:element name="div"><xsl:element name="span"><xsl:attribute name="onclick"><xsl:choose><xsl:when test="cid/text()"><xsl:text><![CDATA[return addValToNode('cid', ]]></xsl:text><xsl:value-of select="cid"/><xsl:text><![CDATA[);]]></xsl:text></xsl:when><xsl:otherwise><xsl:text/></xsl:otherwise></xsl:choose></xsl:attribute><xsl:attribute name="class">skillTitle</xsl:attribute><xsl:value-of select="name"/></xsl:element></xsl:element></xsl:for-each>
	</xsl:element>
</xsl:element>
</xsl:element></xsl:template></xsl:stylesheet>