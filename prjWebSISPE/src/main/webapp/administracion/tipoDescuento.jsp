<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%-- ---------- INICIO - Listado tipo descuento ---------- --%>
<logic:notEmpty name="administracionListadoForm" property="datos">
    <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" class="tabla_informacion">
        <tr>
            <td>
                <table border="0" cellpadding="1" cellspacing="0" width="100%">
                    <tr class="tituloTablas">
                        <td width="10%" align="center" class="columna_contenido">C&Oacute;DIGO</td>
                        <td width="65%" align="center" class="columna_contenido">DESCRIPCI&Oacute;N</td>
                        <td width="8%" align="center" class="columna_contenido">ESTADO</td>
                        <td width="10%" align="center" class="columna_contenido">DETALLE</td>
                        <td width="7%" align="center" class="columna_contenido">EDICI&Oacute;N</td>
                    </tr>
                </table>
        </td></tr>
        <tr>
            <td>
                <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                    <table border="0" cellpadding="1" cellspacing="0" width="100%">
                        <logic:iterate name="administracionListadoForm" property="datos" id="tipoDescuento" indexId="indiceRegistro">
                            <bean:define id="indice" value="${indiceRegistro + administracionListadoForm.start}"/>
                            <bean:define id="residuo" value="${indiceRegistro % 2}"/>
                            <logic:equal name="residuo" value="0">
                                <bean:define id="colorBack" value="blanco10"/>
                            </logic:equal>
                            <logic:notEqual name="residuo" value="0">
                                <bean:define id="colorBack" value="grisClaro10"/>
                            </logic:notEqual>
                            <tr class="${colorBack}"> 
                                <td align="center" width="10%" class="columna_contenido fila_contenido"><bean:write name="tipoDescuento" property="id.codigoTipoDescuento"/></td>
                                <td align="center" width="65%" class="columna_contenido fila_contenido"><bean:write name="tipoDescuento" property="descripcionTipoDescuento"/></td>
                                <td align="center" width="8%" class="columna_contenido fila_contenido">
                                    <logic:notEmpty name="sispe.estado.activo">
                                        <bean:define name="sispe.estado.activo" id="activo"/>
                                    </logic:notEmpty>
                                    <logic:equal name="tipoDescuento" property="estadoTipoDescuento" value="${activo}">
                                        <img src="./images/exito_16.gif" alt="estado activo">
                                    </logic:equal>
                                    <logic:notEqual name="tipoDescuento" property="estadoTipoDescuento" value="${activo}">
                                        <img src="./images/parado.gif" alt="estado inactivo">
                                    </logic:notEqual>
                                </td> 
                                <td align="center" width="10%" class="columna_contenido fila_contenido">
                                    <html:link action="descuentos" paramId="indiceTipoDescuento" paramName="indice">
                                        descuentos
                                    </html:link>
                                </td>
                                <td align="center" width="7%" class="columna_contenido fila_contenido columna_contenido_der">
                                    <html:link action="actualizarTipoDescuento" paramId="indice" paramName="indice">
                                        <img src="./images/editar16.gif" border="0" alt="Editar Descuento">
                                    </html:link>
                                </td>
                            </tr>
                        </logic:iterate>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</logic:notEmpty>

<%-- ---------- FIN - Listado tipo descuento ---------- --%>