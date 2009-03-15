<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
		<!-- generated by dexter from `nodeCreation.html'  -->
	</xsl:output>
	<xsl:template match="/">
		<xsl:element name="form">
			<xsl:attribute name="id"><xsl:text><![CDATA[nodeForm]]></xsl:text></xsl:attribute>
			<xsl:element name="div">
				<xsl:attribute name="class">floatleft</xsl:attribute>
				<xsl:element name="div">
					<xsl:attribute name="id"><xsl:text><![CDATA[divNodeUser]]></xsl:text></xsl:attribute>

					<xsl:value-of select="result/member/username" />
					[
					<xsl:element name="a">
						<xsl:attribute name="onclick"><xsl:choose><xsl:when test="result/member/lid/text()"><xsl:text><![CDATA[return editField('user', ]]></xsl:text><xsl:value-of
							select="result/member/lid" /><xsl:text><![CDATA[);]]></xsl:text></xsl:when><xsl:otherwise><xsl:text /></xsl:otherwise></xsl:choose></xsl:attribute>
						<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
						edit
					</xsl:element>
					]
				</xsl:element>

				<xsl:choose>
					<xsl:when test="result/posTypeVal">
						<xsl:element name="div">
							<xsl:attribute name="id"><xsl:text><![CDATA[divPosType]]></xsl:text></xsl:attribute>
							<xsl:value-of select="result/posTypeVal" />
							[
							<xsl:element name="a">
								<xsl:attribute name="onclick"><xsl:choose><xsl:when test="result/member/name/text()"><xsl:text><![CDATA[return editField('posType', ']]></xsl:text><xsl:value-of
									select="result/member/name" /><xsl:text><![CDATA[')]]></xsl:text></xsl:when><xsl:otherwise><xsl:text /></xsl:otherwise></xsl:choose></xsl:attribute>
								<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
								edit
							</xsl:element>
							]
						</xsl:element>
					</xsl:when>
					<xsl:otherwise>
						<xsl:element name="div">
							<xsl:attribute name="id"><xsl:text><![CDATA[divPosType]]></xsl:text></xsl:attribute>
							<xsl:element name="a">
								<xsl:attribute name="onclick"><xsl:choose><xsl:when test="result/member/posType/text()"><xsl:text><![CDATA[return buildNodeSelectorPage(']]></xsl:text><xsl:value-of
									select="result/member/posType" /><xsl:text><![CDATA[')]]></xsl:text></xsl:when><xsl:otherwise><xsl:text /></xsl:otherwise></xsl:choose></xsl:attribute>
								<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
								Choose Position Type
							</xsl:element>
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>

				<xsl:element name="div">
					<xsl:element name="input">
						<xsl:attribute name="id"><xsl:text><![CDATA[nodePosType]]></xsl:text></xsl:attribute>
						<xsl:attribute name="type">text</xsl:attribute>
						<xsl:attribute name="value">posType</xsl:attribute>
					</xsl:element>
				</xsl:element>
				<xsl:element name="div">
					<xsl:element name="input">
						<xsl:attribute name="id"><xsl:text><![CDATA[nodePosition]]></xsl:text></xsl:attribute>
						<xsl:attribute name="type">text</xsl:attribute>
						<xsl:attribute name="value">position</xsl:attribute>
					</xsl:element>
				</xsl:element>
				<xsl:element name="div">
					<xsl:element name="input">
						<xsl:attribute name="id"><xsl:text><![CDATA[nodeCompany]]></xsl:text></xsl:attribute>
						<xsl:attribute name="type">text</xsl:attribute>
						<xsl:attribute name="value">company</xsl:attribute>
					</xsl:element>
				</xsl:element>
				<xsl:element name="div">
					<xsl:element name="input">
						<xsl:attribute name="id"><xsl:text><![CDATA[nodeFranchise]]></xsl:text></xsl:attribute>
						<xsl:attribute name="type">text</xsl:attribute>
						<xsl:attribute name="value">franchise</xsl:attribute>
					</xsl:element>
				</xsl:element>
				<xsl:element name="div">
					<xsl:element name="input">
						<xsl:attribute name="id"><xsl:text><![CDATA[nodeLocation]]></xsl:text></xsl:attribute>
						<xsl:attribute name="type">text</xsl:attribute>
						<xsl:attribute name="value">location</xsl:attribute>
					</xsl:element>
				</xsl:element>
				<xsl:element name="div">
					<xsl:element name="input">
						<xsl:attribute name="id"><xsl:text><![CDATA[nodeSalary]]></xsl:text></xsl:attribute>
						<xsl:attribute name="type">text</xsl:attribute>
						<xsl:attribute name="value">salary</xsl:attribute>
					</xsl:element>
				</xsl:element>
			</xsl:element>
			<xsl:element name="div">
				<xsl:attribute name="class">floatleft</xsl:attribute>
				<xsl:attribute name="id"><xsl:text><![CDATA[nodeWorkSpace]]></xsl:text></xsl:attribute>
			</xsl:element>
			<xsl:element name="div">
				<xsl:attribute name="class">clearboth</xsl:attribute>
			</xsl:element>
			<xsl:element name="div">
				<xsl:attribute name="id"><xsl:text><![CDATA[nodeStatement]]></xsl:text></xsl:attribute>
			</xsl:element>
			<xsl:element name="div">
				<xsl:element name="button">
					Add Node
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>