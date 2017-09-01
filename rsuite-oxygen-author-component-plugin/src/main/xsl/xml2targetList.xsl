<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0" xmlns:rsicms="http://rsicms.com">

	<xsl:variable name="maxTitleLength" select="100" />



	<xsl:template match="/">
		<list>
			<xsl:apply-templates />
		</list>
	</xsl:template>




	<xsl:template match="*">

		<xsl:if test="@id">
			<xsl:call-template name="generate-link">
				<xsl:with-param name="id" select="@id" />
			</xsl:call-template>
		</xsl:if>



		<xsl:apply-templates />
	</xsl:template>




	<xsl:template match="text()" />

	<xsl:function name="rsicms:format-title">
		<xsl:param name="titleText" />

		<xsl:choose>
			<xsl:when test="string-length($titleText) > $maxTitleLength">
				<xsl:value-of
					select="concat(normalize-space(substring($titleText, 0, $maxTitleLength)),'...')" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="normalize-space($titleText)" />
			</xsl:otherwise>
		</xsl:choose>

	</xsl:function>

	<xsl:template name="generate-link">
		<xsl:param name="id" />

		<xsl:variable name="title">
			<xsl:choose>
				<xsl:when test="title">
					<xsl:value-of select="rsicms:format-title(string(title))" />
				</xsl:when>
				<xsl:when test="string-length(normalize-space(string(.))) > 0">
					<xsl:value-of select="rsicms:format-title(string(.))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="concat(local-name(), ' - ', @id)" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:element name="link">
			<xsl:attribute name="element" select="local-name()" />
			<xsl:attribute name="title" select="$title" />
			<xsl:attribute name="targetId" select="$id" />
			<xsl:attribute name="ID" select="''" />
		</xsl:element>
	</xsl:template>

</xsl:stylesheet>