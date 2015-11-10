<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<fmt:setLocale value="en_US"/>
<%-- ---------- INICIO - Listado de Descuentos ---------- --%>
<logic:notEmpty name="administracionListadoForm" property="datos">
	<logic:notEmpty name="sispe.estado.activo">
    	<bean:define name="sispe.estado.activo" id="activo"/>
		<bean:define name="sispe.estado.inactivo" id="inactivo"/>
	</logic:notEmpty>
    <html:form action="descuentos" method="post">
        <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" class="tabla_informacion">
            <tr>
                <td>
                    <table border="0" cellpadding="1" cellspacing="0" width="100%">
                        <tr class="tituloTablas"  align="left">
                            <td align="center" class="columna_contenido" width="15%">TIPO</td>
                            <td align="center" class="columna_contenido" width="15%">MOTIVO</td>
                            <td align="center" class="columna_contenido" width="12%">RANGO INICIAL</td>
                            <td align="center" class="columna_contenido" width="12%">RANGO FINAL</td>
                            <td align="center" class="columna_contenido" width="12%">PORCENTAJE</td>
                            <td align="center" class="columna_contenido" width="13%">FECHA CREACI&Oacute;N</td>
                            <td align="center" class="columna_contenido" width="13%">ESTADO</td>
                            <td align="center" class="columna_contenido" width="8%">EDICI&Oacute;N</td>
                        </tr>
                    </table>
                </td>
            </tr>
            
            <tr>
                <td>
                    <div id="div_listado" style="width:100%;height:430px;overflow:auto">
                        <table border="0" cellpadding="1" cellspacing="0" width="100%">
                            <logic:iterate name="administracionListadoForm" property="datos" id="descuento" indexId="indiceRegistro">
                                <bean:define id="indice" value="${indiceRegistro + administracionListadoForm.start}"/>
                                <bean:define id="residuo" value="${indiceRegistro % 2}"/>
                                <logic:equal name="residuo" value="0">
                                    <bean:define id="colorBack" value="blanco10"/>
                                </logic:equal>
                                <logic:notEqual name="residuo" value="0">
                                    <bean:define id="colorBack" value="grisClaro10"/>
                                </logic:notEqual>
                                <tr class="${colorBack}">
                                    <td align="center" class="columna_contenido fila_contenido" width="15%"><bean:write name="descuento" property="tipoDescuentoDTO.descripcionTipoDescuento"/></td>
                                    <td align="center" class="columna_contenido fila_contenido" width="15%"><bean:write name="descuento" property="motivoDescuentoDTO.descripcionMotivoDescuento"/></td>
                                    <td align="right" class="columna_contenido fila_contenido" width="12%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.rangoInicialDescuento}"/></td>
                                    <td align="right" class="columna_contenido fila_contenido" width="12%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.rangoFinalDescuento}"/></td>
                                    <td align="right" class="columna_contenido fila_contenido" width="12%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>%</td>
                                    <td align="right" class="columna_contenido fila_contenido" width="13%"><bean:write name="descuento" property="fechaCreacionDescuento" formatKey="formatos.fechahora"/></td>
                                    
                                    <td align="center" class="columna_contenido fila_contenido" width="13%">
                                        <logic:equal name="descuento" property="estadoDescuento" value="${activo}">
                                            <img src="./images/exito_16.gif" alt="estado activo">
                                        </logic:equal>
                                        <logic:equal name="descuento" property="estadoDescuento" value="${inactivo}">
                                            <img src="./images/parado.gif" alt="estado inactivo">
                                        </logic:equal>
                                    </td>
                                    <td align="center" class="columna_contenido fila_contenido columna_contenido_der" width="8%">
                                        <html:link action="actualizarDescuento" paramName="indice" paramId="indice"><img src="./images/editar16.gif" border="0" alt="Actualizar Registro"></html:link>
                                    </td>				
                                </tr>	
                            </logic:iterate>
                        </table>
                    </div>
                </td>
            </tr>    
        </table>
    </html:form>
</logic:notEmpty> 
<%-- ---------- FIN - Listado de Descuentos ---------- --%>