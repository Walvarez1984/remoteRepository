<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:xalan="http://xml.apache.org/xslt">

<xsl:template match="/node" >
<div>
<script language="JavaScript" type="text/javascript">
var myMenu =

[
	 <xsl:for-each select="node">
        _cmSplit,
        [&apos;&apos;,&apos;<xsl:value-of select="@label"/>&apos;,&apos;<xsl:value-of select="@url"/>&apos;,&apos;<xsl:value-of select="@windowid"/>&apos;,&apos;<xsl:value-of select="@label"/>&apos;,
			<xsl:apply-templates select="node"/>
        ],
     </xsl:for-each>
];
</script>
<table><tr><td id="div_MyMenu"></td></tr></table>

<script language="JavaScript" type="text/javascript">   
var menuActivo = true;
cmDraw (&apos;div_MyMenu&apos;, myMenu, &apos;hbr&apos;, cmThemeOffice, &apos;ThemeOffice&apos;, menuActivo);
</script>

</div>
</xsl:template>
<xsl:template match="node" >
        [&apos;&apos;,&apos;<xsl:value-of select="@label"/>&apos;,&apos;<xsl:value-of select="@url"/>&apos;,&apos;<xsl:value-of select="@windowid"/>&apos;,&apos;<xsl:value-of select="@label"/>&apos;
		<xsl:if test="count(node)&gt;0">,
				<xsl:apply-templates select="node"/>
		</xsl:if>
        ],
</xsl:template>
</xsl:stylesheet>
