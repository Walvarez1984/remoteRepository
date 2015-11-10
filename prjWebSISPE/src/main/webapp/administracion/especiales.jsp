<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%-- ---------- INICIO - Listado Especiales ---------- --%>
<logic:notEmpty name="administracionListadoForm" property="datos">
    <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" class="tabla_informacion">
        <tr>
            <td>
                <table border="0" cellpadding="1" cellspacing="0" width="100%">
                    <tr class="tituloTablas">
                        <td width="10%" align="center" class="columna_contenido">C&oacute;digo</td>
                        <td width="30%" align="center" class="columna_contenido">Descripci&oacute;n</td>
                        <td width="8%" align="center" class="columna_contenido">Estado</td>
                        <td width="8%" align="center" class="columna_contenido">Publicado</td>
                        <td width="8%" align="center" class="columna_contenido">Usado en</td>
                        <td width="8%" align="center" class="columna_contenido">Edici&oacute;n</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                    <table border="0" cellpadding="1" cellspacing="0" width="100%">
                        <logic:iterate name="administracionListadoForm" property="datos" id="especial" indexId="indiceRegistro">
                            <bean:define id="indice" value="${indiceRegistro + administracionListadoForm.start}"/>
                            <bean:define id="residuo" value="${indiceRegistro % 2}"/>
                            <logic:equal name="residuo" value="0">
                                <bean:define id="colorBack" value="blanco10"/>
                            </logic:equal>
                            <logic:notEqual name="residuo" value="0">
                                <bean:define id="colorBack" value="grisClaro10"/>
                            </logic:notEqual>
                            <tr class="${colorBack}"> 
                                <td align="center" width="10%" class="columna_contenido fila_contenido"><bean:write name="especial" property="id.codigoEspecial"/></td>
                                <td align="center" width="30%" class="columna_contenido fila_contenido"><bean:write name="especial" property="descripcionEspecial"/></td>
                                <td align="center" width="8%" class="columna_contenido fila_contenido">
                                    <logic:notEmpty name="sispe.estado.activo">
                                        <bean:define name="sispe.estado.activo" id="activo"/>
                                    </logic:notEmpty>
                                    <logic:equal name="especial" property="estadoEspecial" value="${activo}">
                                        <img src="./images/exito_16.gif" alt="estado activo">
                                    </logic:equal>
                                    <logic:notEqual name="especial" property="estadoEspecial" value="${activo}">
                                        <img src="./images/parado.gif" alt="estado inactivo">
                                    </logic:notEqual>
                                </td>
                                <td align="center" width="8%" class="columna_contenido fila_contenido"><bean:write name="especial" property="publicado"/></td>
                                <td align="center" width="8%" class="columna_contenido fila_contenido"><bean:write name="especial" property="tipoPedidoDTO.descripcionTipoPedido"/></td>
                                <td align="center" width="8%" class="columna_contenido fila_contenido columna_contenido_der">
                                    <html:link action="actualizarEspecial" paramId="indice" paramName="indice">
                                        <img src="./images/editar16.gif" border="0" alt="Editar Especial">
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

<%-- ---------- FIN - Listado Especiales ---------- --%>