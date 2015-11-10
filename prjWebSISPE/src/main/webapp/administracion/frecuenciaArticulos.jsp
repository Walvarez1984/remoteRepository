<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp" />
<%-- ---------- INICIO - Lista de Frecuencia de Art&iacute;culos ---------- --%>
<logic:notEmpty name="sispe.estado.activo">
    <bean:define id="estadoActivo" name="sispe.estado.activo" />
</logic:notEmpty>
<html:form action="frecuenciaArticulos">
    <table border="0" width="100%" align="center" cellspacing="0"
    cellpadding="0">
        <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td width="3%" align="center"><img src="./images/frecuenciaArticulos.gif" border="0"></img></td>
                        <td height="35" valign="middle">Frecuencia de Art&iacute;culos</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td><!-- Par&aacute;metro oculto--> 
                                        <input type="hidden" name="ayuda" value="">
                                        <div id="botonA">
                                            <html:link href="#" styleClass="nuevoA" onclick="realizarEnvio('nuevaFrecuenciaArticulos')">Nuevo</html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA">Inicio</html:link>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td height="10px"></td>
        </tr>
        <tr>
            <td>
                <table cellpadding="0" cellspacing="0" border="0" width="98%" align="center" class="tabla_informacion">
                    <tr>
                        <td>
                            <table border="0" cellpadding="3" cellspacing="0" width="100%">
                                <tr class="tituloTablas">
                                    <td align="center" class="columna_contenido" width="5%">N&Uacute;MERO</td>
                                    <td align="center" class="columna_contenido" width="58%">DESCRIPCI&Oacute;N</td>
                                    <td align="center" class="columna_contenido" width="7%">ESTADO</td>
                                    <td align="center" class="columna_contenido" width="12%">EDICI&Oacute;N</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                    <logic:iterate name="ec.com.smx.sic.sispe.administracion.frecuencias" id="frecuenciaCollection" indexId="indiceRegistro">
                                        <bean:define id="residuo" value="${indiceRegistro % 2}" />
                                        <logic:equal name="residuo" value="0">
                                            <bean:define id="colorBack" value="blanco10" />
                                        </logic:equal>
                                        <logic:notEqual name="residuo" value="0">
                                            <bean:define id="colorBack" value="grisClaro10" />
                                        </logic:notEqual>
                                        <tr class="${colorBack}">
                                            <td align="center" width="5%" class="columna_contenido fila_contenido">${indiceRegistro + 1}</td>
                                            <td align="center" width="58%" class="columna_contenido fila_contenido">
                                                <bean:write name="frecuenciaCollection" property="descripcionFrecuencia" />
                                            </td>
                                            <td align="center" width="7%" class="columna_contenido fila_contenido">
                                                <logic:equal name="frecuenciaCollection" property="estadoFrecuencia" value="${estadoActivo}">
                                                    <img src="./images/exito_16.gif" alt="estado activo">
                                                </logic:equal>
                                                <logic:notEqual name="frecuenciaCollection" property="estadoFrecuencia" value="${estadoActivo}">
                                                    <img src="./images/parado.gif" alt="estado inactivo">
                                                </logic:notEqual>
                                            </td>
                                            <td align="center" width="12%" class="columna_contenido fila_contenido columna_contenido_der">
                                                <html:link action="frecuenciaArticulos" paramId="indiceRegistro" paramName="indiceRegistro">
                                                    <img src="./images/editar16.gif" border="0" alt="Editar Frecuencia Articulo">
                                                </html:link>
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
    </table>
</html:form>

<%-- ---------- FIN - Lista de Frecuencia de Art&iacute;culos ---------- --%>
<tiles:insert page="../include/bottom.jsp" />