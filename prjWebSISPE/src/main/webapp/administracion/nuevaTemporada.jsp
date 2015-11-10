<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>
<%-- ---------- INICIO - Formulario para la creaci&oacute;n de temporadas ---------- --%>
<html:form action="nuevaTemporada" method="post">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%" class="textos">
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%" colspan="2">
                <html:hidden property="ayuda" value=""/>
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="./images/nuevaTemporada48.gif" border="0"></td>
                        <td height="35" valign="middle">Nueva Temporada</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">	
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('nuevaTemporada')">Guardar</html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="atras" value="ok"/>
                                        <div id="botonA">
                                            <html:link action="nuevaTemporada" paramId="volver" paramName="atras" styleClass="cancelarA">
                                                Cancelar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="ok"/>
                                        <div id="botonA">
                                            <html:link action="menuPrincipal" paramId="exit" paramName="exit" styleClass="inicioA" title="ir al men&uacute; principal">Inicio</html:link>
                                        </div>
                                    </td>                                    
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr><td colspan="8" height="8"></td></tr>
        <tr>
            <td align="center" valign="top">
                <table border="0" width="98%" cellpadding="2" cellspacing="0" class="tabla_informacion">
                    <tr>
                        <td class="textoAzul11N" width="11%" align="right">Descripci&oacute;n:*</td>
                        <td align="left">
                            <smx:text name="temporadasForm" property="descripcionTemporada" styleClass="textObligatorio" styleError="campoError" size="80" maxlength="176"/>
                        </td>
						<td>
						</td>
					</tr>
                    <tr>
                        <td class="textoAzul11N" align="right">Fecha Inicial:*</td>
                        <td align="left">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="11%">
                                        <smx:text name="temporadasForm" property="fechaInicialTemporada" styleClass="textObligatorio" styleError="campoError" size="15" maxlength="10"/>
                                    </td>
                                    <td>
                                        <smx:calendario property="fechaInicialTemporada" key="formatos.fecha"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
						<td></td>
                    </tr>
                    <tr>
                        <td class="textoAzul11N" align="right">Fecha Final:*</td>
                        <td align="left">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="11%">
                                        <smx:text name="temporadasForm" property="fechaFinalTemporada" styleClass="textObligatorio" styleError="campoError" size="15" maxlength="10"/>
                                    </td>
                                    <td>
                                        <smx:calendario property="fechaFinalTemporada" key="formatos.fecha"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
						<td>
							<table>
								<tr>
									<td class="textoAzul11N" width="11%" align="right">
										<input type="checkbox" checked="checked" name="incluirArtTemAnt" value="si">Incluir art&iacuteculos de la temporada anterior</input>&nbsp;&nbsp;
									</td>
								</tr>
							</table>
						</td>
                    </tr>
                </table>
            </td>
		</tr>
    </TABLE>
</html:form>
<%-- ---------- FIN - Formulario para la creaci&oacute;n de temporadas ---------- --%>
<tiles:insert page="../include/bottom.jsp"/>