<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="2.0" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema" 
                xmlns:r="http://www.rsuitecms.com/rsuite/ns/metadata"
                exclude-result-prefixes="r">

    <xsl:param name="rsuite.base.oxygen.url" select="'{undefined}'"/>
    <xsl:param name="rsuite.serverurl" select="'{undefined}'"/>
    <xsl:param name="rsuite.sessionkey" select="'{undefined}'"/>
    <xsl:param name="rsuite.username" select="'{undefined}'"/>
    <xsl:param name="rsuite.project" select="'{undefined}'"/>
    <xsl:param name="rsuite.mo.schemaId" select="'{undefined}'"/>
    <xsl:param name="rsuite.documenturi" select="'{undefined}'"/>
    <xsl:param name="rsuite.custom.jars" select="''"/>
    <xsl:param name="rsuite.jar.list" select="''"/>
    <xsl:param name="rsuite.mathflow.jar.list" select="''"/>

    <xsl:param name="uuid" select="''"/>

    <xsl:param name="applet.version" select="''"/>
    <xsl:param name="oxygen.version" select="''"/>

    <xsl:param name="rsuite.schemaId" select="'{undefined}'"/>
    <xsl:param name="rsuite.schemaPublicId" select="''"/>
    <xsl:param name="rsuite.schemaSystemId" select="''"/>

    <xsl:param name="rsuite.xpathStartLocation" select="''"/>
    <xsl:param name="rsuite.moReferenceId" select="''"/>

    <xsl:param name="rsuite.customization.class" select="'undefined'"/>
    <xsl:param name="rsuite.custom.arguments" select="'undefined'"/>
    <xsl:param name="rsuite.title" select="'undefined'"/>

	<xsl:output method="html"
	            omit-xml-declaration="yes"
				encoding="UTF-8"
				indent="yes" />

	<xsl:variable name="jar.list">
		<xsl:for-each select="tokenize(rsuite.jar.list,';')">
			<xsl:value-of select="concat(normalize-space(.), ',' )" />
		</xsl:for-each>
	</xsl:variable>

	<xsl:variable name="custom.jar.list">
		<xsl:for-each select="tokenize($rsuite.custom.jars,';')">
			<xsl:value-of select="concat(normalize-space(.),'?', $uuid, ',' )" />
		</xsl:for-each>
	</xsl:variable>

	<xsl:template match="/">

		<applet class="oxygen-editor" 
		        name="RSuite Oxygen Applet" 
				scriptable="true"
		        code="com.rsicms.rsuite.editors.oxygen.applet.OxygenApplet"
				codebase="{$rsuite.base.oxygen.url}"
				archive="{normalize-space($rsuite.jar.list)}">

			<param name="codebase_lookup" value="false" />
			<param name="documentUri" value="{$rsuite.documenturi}"/>
			<param name="baseUri" value="{$rsuite.base.oxygen.url}"/>
			<param name="title" value="{$rsuite.title}"/>
			<param name="projectName" value="{$rsuite.project}"/>
			<param name="userName" value="{$rsuite.username}"/>
			<param name="sessionKey" value="{$rsuite.sessionkey}"/>
			<param name="customizationClass" value="{$rsuite.customization.class}"/>
			<param name="schemaSystemId" value="{$rsuite.schemaSystemId}"/>
			<param name="schemaPublicId" value="{$rsuite.schemaPublicId}"/>
			<param name="schemaId" value="{$rsuite.schemaId}"/>
			<param name="xpathStartLocation" value="{$rsuite.xpathStartLocation}"/>
			<param name="moReferenceId" value="{$rsuite.moReferenceId}"/>

			<param name="classloader_cache" value="false"/>
			<param name="separate_jvm" value="true"/>
			<div class="java_missing_message">
				The oXygen Author Demo Applet requires the Java
				<sup>TM</sup>
				Plug-In version 1.7.0_10
				and above.
				<br />
				Try to install the last Java version in order to run the plugin.
				<br />
				If you already have the Java installed, you may need to enable it
				through your web browser.
			</div>
		</applet>
	</xsl:template>
</xsl:stylesheet>