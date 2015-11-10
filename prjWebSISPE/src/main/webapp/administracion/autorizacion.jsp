<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%-- ---------- INICIO - Fomulario de autorizaciones ------------%>

<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<fmt:setLocale value="en_US"/>
<logic:notEmpty name="administracionListadoForm" property="datos">
    <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" class="tabla_informacion">
        <html:form action="autorizaciones" method="post">
            <tr>
                <td>
                    <table border="0" cellpadding="1" cellspacing="0" width="100%">
                        <tr class="tituloTablas" align="center">
                            <td width="10%" class="columna_contenido" align="center">TIPO</td>
                            <td width="6%" class="columna_contenido" align="center">NUMERO</td>
                            <td width="11%" class="columna_contenido" align="center">LOCAL</td>
                            <td width="14%" class="columna_contenido" align="center">USR. CREACION</td>
                            <td width="8%" class="columna_contenido" align="center">FECHA CREACION</td>
                            <td width="15%" class="columna_contenido" align="center">OBSERVACION</td>
                            <td width="16%" class="columna_contenido" align="center">ACCION</td>
                            <td width="7%" class="columna_contenido" align="center">ESTADO</td>
                            <td width="13%" class="columna_contenido" align="center">USR.USO AUTORIZACION</td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                        <table border="0" cellpadding="2" cellspacing="0" width="100%">
                            <logic:iterate name="administracionListadoForm" property="datos" id="autorizacion" indexId="indiceRegistro">
                                <bean:define id="indice" value="${indiceRegistro + administracionListadoForm.start}"/>
                                <bean:define id="residuo" value="${indiceRegistro % 2}"/>
                                <logic:equal name="residuo" value="0">
                                    <bean:define id="color" value="blanco10"/>
                                </logic:equal>	
                                <logic:notEqual name="residuo" value="0">
                                    <bean:define id="color" value="grisClaro10"/>
                                </logic:notEqual>
                                <tr class="${color}">
                                    <td align="left" width="10%" class="columna_contenido fila_contenido"><bean:write name="autorizacion" property="tipoAutorizacionDTO.descripcionTipoAutorizacion"/></td>
                                    <td align="left" width="6%" class="columna_contenido fila_contenido"><bean:write name="autorizacion" property="id.codigoAutorizacion"/></td>
                                    <td align="center" width="11%" class="columna_contenido fila_contenido"><bean:write name="autorizacion" property="id.codigoLocal"/>&nbsp;-&nbsp;<bean:write name="autorizacion" property="nombreLocal"/></td>
                                    <td align="left" width="14%" class="columna_contenido fila_contenido"><bean:write name="autorizacion" property="nombreUsuarioCreacionAutorizacion"/></td>
                                    
                                    <td align="center" width="8%" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${autorizacion.fechaCreacionAutorizacion}"/></td>
                                    
                                    <td align="left" id="colObserv_${indiceRegistro}" width="15%" class="columna_contenido fila_contenido"><bean:write name="autorizacion" property="observacionAutorizacion"/></td>
                                    <td align="center" id="colAccion_${indiceRegistro}" width="16%" class="columna_contenido fila_contenido">
                                        <logic:equal name="autorizacion" property="estadoAutorizacion" value="${estadoActivo}">
                                            <logic:equal name="autorizacion" property="estadoAutorizacion" value="${estadoActivo}">
                                                <html:link href="#" onclick="requestAjax('autorizaciones.do', ['div_listado'], {parameters: 'indice=${indice}'});">Desactivar</html:link>
                                                <logic:notEmpty name="ec.com.smx.sic.sispe.indice">
                                                    <logic:equal name="ec.com.smx.sic.sispe.indice" value="${indice}">
                                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                            <tr>
                                                                <td colspan="3">
                                                                    <html:textarea property="observacionAutorizacion" styleClass="combos" rows="5" cols="25"/>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td align="right">
                                                                    <div id="boton20">
                                                                        <html:link href="#" styleClass="aceptar20" onclick="requestAjax('autorizaciones.do', ['mensajes', 'colObserv_${indiceRegistro}', 'colAccion_${indiceRegistro}', 'colEstado_${indiceRegistro}', 'colUsrUso_${indiceRegistro}'], {parameters: 'botonDesactivar=ok', indexForm: 1});" title="aceptar"/>
                                                                    </div>
                                                                </td>
                                                                <td>&nbsp;</td>
                                                                <td align="left">
                                                                    <div id="boton20">
                                                                        <html:link href="#" styleClass="cancelar20" onclick="requestAjax('autorizaciones.do', ['mensajes', 'colAccion_${indiceRegistro}'], {parameters: 'botonCancelar=ok'});" title="cancelar"/>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </logic:equal>
                                                </logic:notEmpty>
                                            </logic:equal>
                                        </logic:equal>
                                        <logic:notEqual name="autorizacion" property="estadoAutorizacion" value="${estadoActivo}">
                                            <b>Ninguna</b>
                                        </logic:notEqual>
                                    </td>
                                    <td align="center" width="7%" class="columna_contenido fila_contenido" id="colEstado_${indiceRegistro}">
                                        <logic:equal name="autorizacion" property="estadoAutorizacion" value="${estadoActivo}">
                                            <img src="./images/exito_16.gif" alt="estado activo">
                                        </logic:equal>
                                        <logic:notEqual name="autorizacion" property="estadoAutorizacion" value="${estadoActivo}">
                                            <img src="./images/parado.gif" alt="estado inactivo">
                                        </logic:notEqual>
                                    </td>
                                    <td align="center" id="colUsrUso_${indiceRegistro}" width="13%" class="columna_contenido fila_contenido columna_contenido_der">
                                        <logic:empty name="autorizacion" property="usuarioUsoAutorizacion">
                                            <b>Ninguno</b>
                                        </logic:empty>
                                        <logic:notEmpty name="autorizacion" property="usuarioUsoAutorizacion">
                                            <bean:write name="autorizacion"  property="nombreUsuarioUsoAutorizacion"/>
                                        </logic:notEmpty>
                                    </td>
                                </tr>	
                            </logic:iterate>
                        </table>
                    </div>
                </td>
            </tr>
        </html:form>
    </table>
</logic:notEmpty> 
<%-- ---------- FIN - Fomulario de autorizaciones ---------- --%>