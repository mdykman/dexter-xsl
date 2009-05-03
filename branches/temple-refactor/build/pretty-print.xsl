<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" indent="yes" media-type="text/html" method="html" />

	<xsl:template match="/">
		<xsl:element name="html">
			<xsl:element name="head">
				<xsl:element name="title">
					data console
				</xsl:element>
				<xsl:element name="style">
					<xsl:text>
body {
	background-color: #FFFFCC;
}
.tag {
	color:#6666FF; 
	font-weight:bold;
}

.attname {
	color:#6420BB;
}				
.attval {
	color:#BB3341; 
	font-style:italic;
}				
.punc {
	color:#7733cc;
}				
.text {
	font-style:italic;
	color:#350075;
}
.element {
	position:relative; left: 20px;
}
</xsl:text>
				</xsl:element>
			</xsl:element>
			<xsl:element name="body">
				<div>
					<xsl:element name="span">
						<xsl:attribute name="class">punc</xsl:attribute>
					<xsl:text>&lt;</xsl:text>
					</xsl:element>
					<xsl:element name="span">
						<xsl:attribute name="class">tag</xsl:attribute>
						<xsl:value-of select="local-name(*)" />
					</xsl:element>
					<xsl:element name="span">
						<xsl:attribute name="class">punc</xsl:attribute>
						<xsl:text>&gt;</xsl:text>
					</xsl:element>
					<xsl:for-each select="child::*">
						<xsl:apply-templates />
					</xsl:for-each>
					<xsl:element name="span">
						<xsl:attribute name="class">punc</xsl:attribute>
					<xsl:text>&lt;</xsl:text>
					<xsl:text>/</xsl:text>
					</xsl:element>
					<xsl:element name="span">
						<xsl:attribute name="class">tag</xsl:attribute>
						<xsl:value-of select="local-name(*)" />
					</xsl:element>
					<xsl:element name="span">
						<xsl:attribute name="class">punc</xsl:attribute>
					<xsl:text>&gt;</xsl:text>
					</xsl:element>
				</div>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<xsl:template match="text()">
	<xsl:element name="span">
	<xsl:attribute name="class">text</xsl:attribute>
		<xsl:value-of  select="." />
		</xsl:element>
	</xsl:template>

	<xsl:template match="*">
		<xsl:element name="div">
		<xsl:attribute name="class">element</xsl:attribute>
		<xsl:element name="span">
			<xsl:attribute name="class">punc</xsl:attribute>
					<xsl:text>&lt;</xsl:text>
		</xsl:element>
		<xsl:element name="span">
			<xsl:attribute name="class">tag</xsl:attribute>
			<xsl:value-of select="local-name()" />
		</xsl:element>
		<xsl:for-each select="attribute::*">
			<xsl:text> </xsl:text>
			<xsl:element name="span">
				<xsl:attribute name="class">attname</xsl:attribute>
				<xsl:value-of select="local-name()" />
			</xsl:element>
			<xsl:element name="span">
				<xsl:attribute name="class">punc</xsl:attribute>
				<xsl:text>=</xsl:text>
			</xsl:element>
			<xsl:text>"</xsl:text>
			<xsl:element name="span">
				<xsl:attribute name="class">attval</xsl:attribute>
				<xsl:value-of select="." />
			</xsl:element>
			<xsl:text>"</xsl:text>
		</xsl:for-each>
		<xsl:element name="span">
			<xsl:attribute name="class">punc</xsl:attribute>
					<xsl:text>&gt;</xsl:text>
		</xsl:element>
		<xsl:apply-templates select="*|text()"/>
		<xsl:element name="span">
			<xsl:attribute name="class">punc</xsl:attribute>
					<xsl:text>&lt;</xsl:text>
			<xsl:text>/</xsl:text>
		</xsl:element>
		<xsl:element name="span">
			<xsl:attribute name="class">tag</xsl:attribute>
			<xsl:value-of select="local-name()" />
			</xsl:element>
		<xsl:element name="span">
			<xsl:attribute name="class">punc</xsl:attribute>
					<xsl:text>&gt;</xsl:text>
			</xsl:element>
		</xsl:element>
	</xsl:template>

</xsl:stylesheet>

