<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp" />
<%---------------------- lista de definiciones para las acciones -----------------------------%>
<bean:define id="act" name="sispe.estado.activo" /> 
<bean:define id="ina" name="sispe.estado.inactivo" />

<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
       <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
       <tr>
        <td align="left" valign="top" width="100%" colspan="3">
            <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                <tr>
                    <td width="4%" align="center"><img src="images/admMovimientos.gif" border="0"></img></td>
                    <td height="35" valign="middle">Administraci&oacute;n de Motivos de Movimientos</td>
                    <td align="right" valign="top">
                        <table border="0">
                            <tr>
                                <td>
                                	<bean:define id="exit" value="exit" />
                                    <div id="botonA"><html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA">Inicio</html:link></div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td height="10"></td></tr>
    <tr>
        <td>
            <html:form action="administracionMotivoMovimiento">
                <div id="cuadroMensaje">
                    <logic:present name="ec.com.smx.sic.sispe.mensaje">
                        <bean:define id="visible" value="visible" />
                    </logic:present> 
                    <logic:notPresent name="ec.com.smx.sic.sispe.mensaje">
                        <bean:define id="visible" value="hidden" />
                    </logic:notPresent>
                    <div id="popup" class="popup" style="visibility:${visible};">
                        <div id="center" class="popupcontenido">
                            <logic:present name="ec.com.smx.sic.sispe.mensaje">
                                <table border="0" align="center" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion">
                                    <tr>
                                        <td background="images/barralogin.gif" height="22px"></td>
                                    </tr>
                                    <tr>
                                        <td bgcolor="#F4F5EB">
                                            <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                                <tr>
                                                    <td class="textoNegro11" align="center">
                                                        <table border="0" width="99%">
                                                            <tr>
                                                                <td height="20%"><img src="./images/pregunta24.gif"/></td>
                                                                <td align="left" colspan="2"><bean:write name="ec.com.smx.sic.sispe.mensaje" /></td>
                                                            </tr>
                                                            <tr>
                                                                <td height="5" colspan="2"></td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" width="100%">
                                                                    <table cellpadding="0" cellspacing="0" align="center" width="100%">
                                                                        <td align="center">
                                                                            <div id="botonDmin">
                                                                                <a href="#" class="siDmin" onclick="requestAjax('administracionMotivoMovimiento.do',['mensajes','listado_movimientos'], {parameters: 'btnAceptar=ok'});hide(['popup']);">Si</a>
                                                                            </div>
                                                                        </td>
                                                                        <td align="center">
                                                                            <div id="botonDmin">
                                                                                <a href="#" class="noDmin" onclick="hide(['popup']);">No</a>
                                                                            </div>
                                                                        </td>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td height="5" colspan="2"></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </logic:present>
                        </div>
                    </div>
                </div>
            </html:form>
        </td>
    </tr>
    <tr>
        <td valign="top" colspan="3">
            <TABLE border="0" cellspacing="0" cellpadding="0" width="98%" align="center" class="textoNegro11 tabla_informacion">
                <tr>
                    <td>
                        <table border="0" cellpadding="3" cellspacing="0" width="100%">
                            <tr class="tituloTablas">
                                <td align="center" class="columna_contenido" width="8%">C&Oacute;DIGO</td>
                                <td align="center" class="columna_contenido" width="60%">DESCRIPCI&Oacute;N</td>
                                <td align="center" class="columna_contenido" width="12%">TIPO DE STOCK AFECTADO</td>
                                <td align="center" class="columna_contenido" width="10%">ESTADO</td>
                                <td align="center" class="columna_contenido" width="10%">ACCI&Oacute;N</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="listado_movimientos" style="width:100%;height:440px;overflow-y:auto;overflow-x:hidden">
                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <logic:iterate id="motivo" name="ec.com.smx.sic.sispe.calendarioMotivoMovimientoDTOCol" indexId="nMotivo">
                                    <bean:define id="residuo" value="${nMotivo % 2}" />
                                    <logic:equal name="residuo" value="0">
                                        <bean:define id="colorBack" value="blanco10" />
                                    </logic:equal>
                                    <logic:notEqual name="residuo" value="0">
                                        <bean:define id="colorBack" value="grisClaro10" />
                                    </logic:notEqual>
                                    <tr class="${colorBack}">
                                        <td align="center" width="8%" class="columna_contenido fila_contenido">
                                        <bean:write name="motivo" property="id.codigoMotivoMovimiento" /></td>
                                        <td align="center" width="60%" class="columna_contenido fila_contenido">
                                        <bean:write name="motivo" property="descripcionMotivoMovimiento" /></td>
                                        <td align="center" width="12%" class="columna_contenido fila_contenido">
                                        <bean:write name="motivo"  property="tipoStockAfectado" /></td>
                                        <TD align="center" width="10%" class="columna_contenido fila_contenido">
                                            <logic:equal name="motivo" property="estadoMotivoMovimiento" value="${ina}">
                                                <img src="images/parado.gif" border="0" title="Inactivo">
                                            </logic:equal>
                                            <logic:equal name="motivo" property="estadoMotivoMovimiento" value="${act}">
                                                <img src="images/exito_16.gif" border="0" title="Activo">
                                            </logic:equal>
                                        </TD>
                                        <td align="center" width="10%" class="columna_contenido fila_contenido columna_contenido_der">
                                            <html:link action="administracionMotivoMovimiento" paramId="motivoActual" paramName="nMotivo" >
                                                <logic:equal name="motivo" property="estadoMotivoMovimiento" value="${ina}">
                                                    Activar
                                                </logic:equal>
                                            </html:link>
                                            <html:link action="administracionMotivoMovimiento" paramId="motivoActual" paramName="nMotivo" >
                                                <logic:equal name="motivo" property="estadoMotivoMovimiento" value="${act}">
                                                    Desactivar
                                                </logic:equal>
                                            </html:link> <%--administracionMotivoMovimiento.do?motivoActual=2--%>
                                        </td>
                                    </tr>
                                </logic:iterate>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</TABLE>
<tiles:insert page="/include/bottom.jsp"/>