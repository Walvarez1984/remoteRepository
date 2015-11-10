<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/mensajeria.tld" prefix="mensajeria"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="codigoDescuentoVariable"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.descuentoVariable"/></bean:define>
					
<table border="0" width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion">
    <tr>
        <td class="fila_titulo" height="20px" align="left" colspan="2"><b>Lista de los art&iacute;culos del pedido</b></td>
    </tr>
    <tr>
        <td colspan="2">
            <table border="0" align="left" cellspacing="0" cellpadding="1" width="100%">
                <tr class="tituloTablas">
                    <td class="columna_contenido" align="center" width="5%">No</td>
                    <td class="columna_contenido" width="12%" align="center">C&Oacute;DIGO BARRAS</td>
                    <td class="columna_contenido" align="center" width="30%">ART&Iacute;CULO</td>
                    <td class="columna_contenido" align="center" width="6%">CANT.</td>
                    <td class="columna_contenido" align="center" width="7%">PESO</td>
                    <td class="columna_contenido" width="7%" align="center">V. UNIT.</td>
                    <td class="columna_contenido" width="7%" align="center">V. UNIT.IVA</td>
                    <td class="columna_contenido" width="31%" align="center">OBSERVACI&Oacute;N</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="detallePedido" style="width:100%;height:240px;overflow:auto;">
              <table border="0" width="100%" cellpadding="1" cellspacing="0">
                  <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol">
                      <logic:iterate name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol" id="detalle" indexId="indice">
                          <bean:define id="numFila" value="${indice + 1}"/>
                          <%--------- control del estilo para el color de las filas --------------%>
                          <bean:define id="residuo" value="${indice % 2}"/>
                          <logic:equal name="residuo" value="0">
                              <bean:define id="colorBack" value="blanco10"/>
                          </logic:equal>
                          <logic:notEqual name="residuo" value="0">
                              <bean:define id="colorBack" value="grisClaro10"/>
                          </logic:notEqual>
                          <%--------------------------------------------------------------------%>
                          <tr class="${colorBack}"> 
                              <td width="3%" class="columna_contenido fila_contenido" align="center" valign="middle">
                                <logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                          
                                <c:set var="autorizacion" value="0"/>                              
                                    <logic:iterate name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutDTO" indexId="numeroAut">                                        
                                        <logic:notEmpty name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.codigoAutorizacion">	
	                                        <c:if test="${codigoDescuentoVariable == detalleAutDTO.estadoPedidoAutorizacionDTO.npTipoAutorizacion}">
	                                            <c:if test="${autorizacion != 2}"> 
	                                                <c:set var="autorizacion" value="1"/>
	                                            </c:if>    
                                            </c:if>
                                        </logic:notEmpty>
                                        <logic:empty name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.codigoAutorizacion">	
                                            <c:set var="autorizacion" value="2"/>
                                        </logic:empty>
                                    </logic:iterate>
                                    <c:if test="${autorizacion == 1}">
                                        <html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}'})" title="ver Autorizacion">
                                            <img src="images/autSolicitada.png" border="0">
                                        </html:link>
                                    </c:if>
                                    <c:if test="${autorizacion == 2}">
                                       <img src="images/autSolicitada.png" border="0" title="Autorizacion Solicitada">
                                    </c:if>
                                </logic:notEmpty>
                                <logic:empty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                                                    	
                                    &nbsp;
                                </logic:empty>
                              </td> 			
                              <td width="5%" align="center" class="fila_contenido columna_contenido">&nbsp;<bean:write name="numFila"/></td>
                              <td width="9%" align="center" class="fila_contenido columna_contenido">&nbsp;<bean:write name="detalle" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
                              <td width="30%" align="left" class="fila_contenido columna_contenido">&nbsp;<bean:write name="detalle" property="articuloDTO.descripcionArticulo"/></td>
                              <td width="6%" align="right" class="fila_contenido columna_contenido">&nbsp;<bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/></td>
                              
                              <td width="7%" align="right" class="fila_contenido columna_contenido">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/></td>
                              <td width="7%" align="right" class="fila_contenido columna_contenido">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/></td>
                              <td width="7%" align="right" class="fila_contenido columna_contenido">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/></td>
                              <td width="31%" align="left" class="fila_contenido columna_contenido">&nbsp;<bean:write name="detalle" property="estadoDetallePedidoDTO.observacionArticulo"/>
                          </tr>
                      </logic:iterate>
                  </logic:notEmpty>
                </table>
            </div>
        </td>
    </tr>
</table>