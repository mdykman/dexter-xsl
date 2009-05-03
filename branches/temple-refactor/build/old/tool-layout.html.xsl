<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
<!-- generated by dexter from `xsl/default/xhtml-1.0/tool-layout.html'  -->
</xsl:output><xsl:template match="/"><xsl:element name="html">
	<xsl:element name="head">
		<xsl:element name="title">Workastica Tools</xsl:element>
		<xsl:element name="link"><xsl:attribute name="href">/css/main.css</xsl:attribute><xsl:attribute name="rel">stylesheet</xsl:attribute><xsl:attribute name="type">text/css</xsl:attribute></xsl:element>
		<xsl:element name="script"><xsl:attribute name="src">/js/jquery.js</xsl:attribute><xsl:attribute name="type">text/javascript</xsl:attribute></xsl:element>
				<xsl:element name="script"><xsl:attribute name="src">/js/didi.js</xsl:attribute><xsl:attribute name="type">text/javascript</xsl:attribute></xsl:element>
		<xsl:element name="script"><xsl:attribute name="src">/js/main.js</xsl:attribute><xsl:attribute name="type">text/javascript</xsl:attribute></xsl:element>
	</xsl:element>
	<xsl:element name="body">
		<xsl:element name="div"><xsl:attribute name="id"><xsl:text><![CDATA[mainContainer]]></xsl:text></xsl:attribute>
			<xsl:element name="div"><xsl:attribute name="id"><xsl:text><![CDATA[nodeMapContainer]]></xsl:text></xsl:attribute>
				<xsl:element name="div"><xsl:attribute name="id"><xsl:text><![CDATA[nodeMapNav]]></xsl:text></xsl:attribute>
					<xsl:element name="div"><xsl:attribute name="class">floatleft</xsl:attribute><xsl:attribute name="id"><xsl:text><![CDATA[worldButton]]></xsl:text></xsl:attribute><xsl:attribute name="onClick">return showWorldMap()</xsl:attribute><xsl:element name="button">View the World</xsl:element></xsl:element> 
					<xsl:element name="div"><xsl:attribute name="class">floatleft</xsl:attribute><xsl:attribute name="id"><xsl:text><![CDATA[searchContext]]></xsl:text></xsl:attribute><xsl:element name="form"><xsl:attribute name="name">forwardfrm</xsl:attribute> How do I become a <xsl:element name="select"><xsl:attribute name="id"><xsl:text><![CDATA[position]]></xsl:text></xsl:attribute><xsl:attribute name="name">choice</xsl:attribute><xsl:attribute name="onchange">return setBottomNode(this.value);</xsl:attribute>
						<xsl:for-each select=".//Position"><xsl:element name="option"><xsl:attribute name="value"><xsl:value-of select="pid"/></xsl:attribute><xsl:value-of select="title"/></xsl:element></xsl:for-each>
						</xsl:element> when I grow up?</xsl:element></xsl:element>
				</xsl:element>
				<xsl:element name="div"><xsl:attribute name="class">clearboth</xsl:attribute></xsl:element>
				<xsl:element name="div"><xsl:attribute name="id"><xsl:text><![CDATA[nodeMap]]></xsl:text></xsl:attribute>
					nodeMap
				</xsl:element>
			</xsl:element>
			<xsl:element name="div"><xsl:attribute name="id"><xsl:text><![CDATA[toolSetContainer]]></xsl:text></xsl:attribute>
				<xsl:element name="div"><xsl:attribute name="id"><xsl:text><![CDATA[toolSet]]></xsl:text></xsl:attribute>
				Tools in Here
				</xsl:element>
				<xsl:element name="div"><xsl:attribute name="id"><xsl:text><![CDATA[jobInfo]]></xsl:text></xsl:attribute>
					Job info
				</xsl:element>
			</xsl:element>
		</xsl:element>
		<xsl:element name="script">
			populateAreas();
			</xsl:element>
	</xsl:element>
</xsl:element></xsl:template></xsl:stylesheet>