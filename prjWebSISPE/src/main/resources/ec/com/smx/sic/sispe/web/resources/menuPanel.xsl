<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:xalan="http://xml.apache.org/xslt">

<xsl:template match="/node" >
	<xsl:variable name="nodos_panel" select="//node[@panelStyle!='']"/>

<table border="0" cellpadding="0" cellspacing="0" width="575px">
	<tr><td height="30px"></td></tr>
	<tr>
		<td>
			<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" class="tabla_informacion">
				<tr>
					<td align="center" class="tituloTablasCeleste2" height="20px">
						<B>PANEL DE OPCIONES</B>
					</td>
				</tr>
            </table>
		</td>
    </tr>
    <tr>
        <td>
			<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" class="textoNegro11">
				<tr>
					<td background="images/tabla_izq.gif" height="12px" width="12px"></td>
					<td align="left" valign="top" bgcolor="#F1F3F5">
						<table cellpadding="2" cellspacing="2">
								<xsl:for-each select="$nodos_panel">
									<xsl:sort select="format-number(number(@panelOrder),'000000')" />
										<xsl:variable name="posicion" select="position()" />
										<xsl:if test="($posicion mod 5) = 1">
											<tr>
												<td>
													<xsl:call-template name="enlacePanel"></xsl:call-template>
												</td>
												<td>
													<xsl:for-each select="$nodos_panel">
														<xsl:sort select="format-number(number(@panelOrder),'000000')"/>
														<xsl:if test="position()=$posicion+1">
															<xsl:call-template name="enlacePanel"></xsl:call-template>
														</xsl:if>
													</xsl:for-each>
												</td>
												<td>
													<xsl:for-each select="$nodos_panel">
														<xsl:sort select="format-number(number(@panelOrder),'000000')"/>
														<xsl:if test="position()=$posicion+2">
															<xsl:call-template name="enlacePanel"></xsl:call-template>
														</xsl:if>
													</xsl:for-each>
												</td>
												<td>
													<xsl:for-each select="$nodos_panel">
														<xsl:sort select="format-number(number(@panelOrder),'000000')"/>
														<xsl:if test="position()=$posicion+3">
															<xsl:call-template name="enlacePanel"></xsl:call-template>
														</xsl:if>
													</xsl:for-each>
												</td>
												<td>
													<xsl:for-each select="$nodos_panel">
														<xsl:sort select="format-number(number(@panelOrder),'000000')"/>
														<xsl:if test="position()=$posicion+4">
															<xsl:call-template name="enlacePanel"></xsl:call-template>
														</xsl:if>
													</xsl:for-each>
												</td>
											</tr>
										</xsl:if>
								</xsl:for-each>
						</table>
					</td>
					<td background="images/tabla_der.gif" height="12px" width="12px"></td>
				</tr>
				<tr>
					<td background="images/tabla_inf_der.gif" height="12" width="12px"></td>
					<td background="images/tabla_inf.gif"></td>
					<td background="images/tabla_inf_izq.gif" height="12" width="12px"></td>
				</tr>
			</table>
		</td>
    </tr>
</table>

</xsl:template>

<xsl:template name="enlacePanel">
	<div id="botonP">
		<xsl:variable name="winId" select="@windowid"/>
		<xsl:if test="$winId != '_self'">
			<script language="JavaScript" type="text/javascript">
				var parWinOpen<xsl:value-of select="position()"/> = [&apos;<xsl:value-of select="@url"/>&apos;,&apos;<xsl:value-of select="@windowid"/>&apos;];
			</script>
		</xsl:if>
		<a title="Administra las opciones del usuario">
			<xsl:if test="$winId != '_self'">
				<xsl:attribute name="href">javascript:openWindow(parWinOpen<xsl:value-of select="position()"/>)</xsl:attribute>
			</xsl:if>
			<xsl:if test="$winId = '_self'">
				<xsl:attribute name="href"><xsl:value-of select="@url"/></xsl:attribute>
				<xsl:attribute name="onclick">popWait()</xsl:attribute>
			</xsl:if>
			<xsl:attribute name="class"><xsl:value-of select="@panelStyle"/></xsl:attribute>
			<xsl:attribute name="title"><xsl:value-of select="@description"/></xsl:attribute>
			<xsl:value-of select="@label"/>
		</a>
	</div>
</xsl:template>

</xsl:stylesheet>
