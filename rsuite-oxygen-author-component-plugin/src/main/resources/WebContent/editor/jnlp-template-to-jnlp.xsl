<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:param name="rsuite.baseUri" select="'{undefined}'" />
	<xsl:param name="rsuite.startArguments" select="'{undefined}'" />

	<xsl:output encoding="UTF-8" indent="yes" />
	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>


	<xsl:template match="*" priority="2">
		<xsl:copy>
			 <xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

    <xsl:template match="@* | node()">
        <xsl:copy-of select="."/>
    </xsl:template>


	<xsl:template match="@codebase" priority="10">
        <xsl:attribute name="codebase">
            <xsl:value-of select="$rsuite.baseUri" />
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="param/@value" priority="10">
        <xsl:attribute name="value">
            <xsl:value-of select="$rsuite.startArguments" />
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="argument" priority="10">
    	<xsl:copy>    	
    		<xsl:value-of select="$rsuite.startArguments" />
    	</xsl:copy>
    </xsl:template>

</xsl:stylesheet>