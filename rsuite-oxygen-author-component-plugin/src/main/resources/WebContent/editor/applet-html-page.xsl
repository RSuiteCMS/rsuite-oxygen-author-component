<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:r="http://www.rsuitecms.com/rsuite/ns/metadata"
    exclude-result-prefixes="r">
    <xsl:param name="rsuite.managedObjectId" select="'{undefined}'" />
    <xsl:param name="rsuite.pluginPath" select="'{undefined}'" />
    <xsl:param name="rsuite.appletContext" select="'{undefined}'" />
    <xsl:param name="rsuite.sessionkey" select="'{undefined}'" />
    <xsl:param name="rsuite.uuid" select="'{undefined}'" />

    <xsl:param name="rsuite.applet.tag" select="''" />


    <xsl:output method="html" doctype-public="XSLT-compat" omit-xml-declaration="yes" encoding="UTF-8" indent="yes" />
    <xsl:template match="/">
        <html>
            <head>
                <meta content-type="UTF-8" />
                <meta data-session-key="{$rsuite.sessionkey}" />
                <title>RSuite Oxygen Integration</title>
                <link rel="stylesheet" href="{$rsuite.pluginPath}/editor/editor.css" />
                <link rel="icon" href="{$rsuite.pluginPath}/editor/rsuite-oxygen.png" />
                <link rel="shortcut icon" href="{$rsuite.pluginPath}/editor/rsuite-oxygen.png" />
                <script src="{$rsuite.pluginPath}/lib/jquery-1.11.1.min.js"></script>
                <script src="{$rsuite.pluginPath}/lib/handlebars-v1.3.0.js"></script>
                <script src="{$rsuite.pluginPath}/lib/ember-1.7.0.min.js"></script>
                <script src="{$rsuite.pluginPath}/editor/oxygen-editor.js"></script>
            </head>
            <body>
            	<xsl:value-of select="$rsuite.applet.tag" disable-output-escaping="yes" />                
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>