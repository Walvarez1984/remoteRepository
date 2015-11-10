<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/mensajeria.tld" prefix="mensajeria"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<logic:notEmpty name="sispe.estado.activo">
    <bean:define name="sispe.estado.activo" id="estadoActivo"/>
</logic:notEmpty>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="estadoDespachado" name="ec.com.smx.sic.sispe.estado.despachadoEspecial"></bean:define>
<bean:define id="estadoDespachoPrevio" name="ec.com.smx.sic.sispe.estado.despachoPrevio"></bean:define>
<bean:define id="estadoSolicitado" name="ec.com.smx.sic.sispe.estado.solicitadoEspecial"></bean:define>
<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedido"/>

<c:set var="mostrarCamposDespacho" value="0"/>
<c:if test="${vistaPedidoDTO.id.codigoEstado == estadoDespachado || vistaPedidoDTO.id.codigoEstado == estadoDespachoPrevio}">
    <c:set var="mostrarCamposDespacho" value="1"/>
</c:if>
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
    <logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
        <bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
        <bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
        <bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
        <tr>
            <td>
                <div id="informacionOrdenCompra">    
                    <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabla_informacion">
                        <html:form action="detalleEstadoPedidoEspecial" method="post">
                            <input type="hidden" name="ayuda" value="">
                            <tr class="fila_titulo">
                                <td class="textoNegro11 fila_contenido" align="left" height="20px"><b>Datos de la cabecera:</b></td>
                            </tr>
                            <tr>
                                <td class="textoAzul11">
                                    <!-- INICIO CABECERA --> 
                                    <table cellspacing="0" width="100%" align="left" cellpadding="1">
                                        <tr>
                                            <td class="textoAzul11" colspan="2" align="right">No. Pedido:
                                            </td>
                                            <td align="left" class="textoNegro11" colspan="2">
                                                <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
                                            </td>
                                            <%--
                                            <td class="textoAzul11" width="20%" align="right">Entidad Responsable:</td>
                                            <td align="left" class="textoNegro11">
                                                <logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadLocal}">
                                                    <bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local.descripcion"/>
                                                </logic:equal>
                                                <logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadBodega}">
                                                    <bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega.descripcion"/>
                                                </logic:equal>
                                            </td>
                                            --%>
                                        </tr>
                                       <logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa">
											<tr>
												<td class="textoAzul11" align="right" colspan="2">Empresa:</td>
												<td class="textoNegro11" align="left" colspan="9">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
												</td>
											</tr>																												
										</logic:notEmpty>
										
										<!-- PARA EL CASO DE DATOS DE PERSONA -->
										<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombrePersona">
											<tr>
												<td class="textoAzul11" align="right" colspan="2">Cliente:</td>
												<td class="textoNegro11" align="left" colspan="9">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
												</td>
											</tr>
										</logic:notEmpty>	
																				
										<!-- PARA EL CASO DE DATOS DEL CONTACTO PRINCIPAL DE LA EMPRESA -->					
										<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto">	
											<tr>
												<td class="textoAzul11" align="right" colspan="2">Contacto:</td>																																
												<td class="textoNegro11" align="left" colspan="9">
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoCliente"/>																			
												</td>																							
											</tr>
										</logic:notEmpty>	
										
							           <tr>  
                                            <td class="textoAzul11" align="right" colspan="2">Elaborado en:</td>
                                            <td class="textoNegro11" align="left">
                                                <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
                                            </td>
                                            <logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoSolicitado}">
                                                <td class="textoAzul11" align="right">Entrega en:&nbsp;</td><td class="textoNegro11"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="codigoLocalEntrega"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocalEntrega"/></td>
                                            </logic:notEqual>
                                        </tr>
                                        <tr>
                                            <td class="textoAzul11" align="right" colspan="2">Fecha de Estado:</td>
                                            <td class="textoNegro11" align="left">
                                                <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
                                            </td>
                                            <logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoSolicitado}">
                                                <td  class="textoAzul11" align="right" >Fecha Despacho:&nbsp;</td>
                                                 <td class="textoNegro11" align="left"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaDespacho" formatKey="formatos.fecha"/></td>
                                                 <td class="textoAzul11" align="right" >Fecha Entrega Cliente:&nbsp;</td>
												 <td class="textoNegro11" align="left"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaEntrega" formatKey="formatos.fecha"/></td>
                                               
                                            </logic:notEqual>
                                        </tr>
                                        <tr>
                                            <td class="textoAzul11" align="right">Estado:
                                            </td>
                                            <td class="textoNegro11" align="left" colspan="2"> 
                                                <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/>
                                            </td>
                                        </tr>
                                    </table>
                                    <!-- FIN CABECERA -->
                                </td>
                            </tr>        
                        </html:form>
                    </table>
                </div>
            </td>
        </tr>
        <tr height="10"><td></td></tr>
        <tr>
            <td align="center">
                <table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
                    <logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
                        <tr align="left" class="fila_titulo">
                            <td colspan="2" class="textoNegro11" height="20px"><b>Detalle del pedido:</b></td>
                        </tr>
                        <tr><td height="5px"></td></tr>
                        <tr>
                            <td colspan="2">
                                <c:set var="columnas" value="7"/>
                                <logic:equal name="mostrarCamposDespacho" value="1">      
                                    <c:set var="columnas" value="12"/>
                                </logic:equal>
                                <table width="100%" border="0" cellpadding="1" cellspacing="0">	
                                    <tr class="tituloTablas">
                                        <td class="columna_contenido" align="center" width="3%">No</td>
                                        <td class="columna_contenido" align="center" width="12%">C&oacute;digo barras</td>
                                        <td class="columna_contenido" align="center" width="20%">Art&iacute;culo</td>
                                        <td class="columna_contenido" align="center" width="5%">Cant.</td>
                                        <td class="columna_contenido" align="center" width="6%">Peso Kg.</td>
                                        <td class="columna_contenido" align="center" width="20%">Observaci&oacute;n</td>
                                        <td class="columna_contenido" align="center" width="6%">V. Unit.</td>
                                        <logic:equal name="mostrarCamposDespacho" value="1">
                                            <td class="columna_contenido" align="center" width="6%">Peso Reg.</td>
                                            <td class="columna_contenido" align="center" width="8%">Tot. Iva</td>
                                            <td class="columna_contenido" align="center" width="2%">Iva</td>
                                            <td class="columna_contenido" align="center" width="6%">V.Unit Neto</td>
                                            <td class="columna_contenido" align="center" width="8%">Tot. Neto</td>
                                        </logic:equal>
                                    </tr>
                                    <tr>
                                        <c:set var="condicion" value="0.00"/>
                                        <td colspan="${columnas}">
                                            <div style="width:100%;height:340px;overflow:auto;">
                                                <table width="100%" border="0" cellpadding="1" cellspacing="0" >	
                                                    <tr>
                                                        <td>
                                                            <logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
                                                                <%--------- control del estilo para el color de las filas --------------%>
                                                                <bean:define id="residuo" value="${indiceDetalle % 2}"/>
                                                                <logic:equal name="residuo" value="0">
                                                                    <bean:define id="colorBack" value="blanco10"/>
                                                                </logic:equal>	
                                                                <logic:notEqual name="residuo" value="0">
                                                                    <bean:define id="colorBack" value="grisClaro10"/>
                                                                </logic:notEqual>
                                                                <%--------------------------------------------------------------------%>
                                                                <bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
                                                                <tr class="${colorBack}">
                                                                    <td align="center" class="columna_contenido fila_contenido" width="3%"><bean:write name="numRegistro"/></td>
                                                                    <td class="columna_contenido fila_contenido" align="left" width="12%">
                                                                        <logic:notEmpty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
                                                                            <html:link href="#" onclick="requestAjax('detalleEstadoPedidoEspecial.do',['informacionOrdenCompra'],{parameters: 'detallePedido=${indiceDetalle}'});" title="detalle de la orden de compra"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></html:link>
                                                                        </logic:notEmpty>
                                                                        <logic:empty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
                                                                            <bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/>
                                                                        </logic:empty> 
                                                                    </td>
                                                                    <td align="left" class="columna_contenido fila_contenido" width="20%"><bean:write  name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
                                                                    <td class="columna_contenido fila_contenido" align="ridht" width="5%" title="cantidad total a entregar">
                                                                        <logic:equal name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                            <bean:write  name="vistaDetallePedidoDTO" property="cantidadEstado"/>
                                                                        </logic:equal>
                                                                        <logic:notEqual name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                            <label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
                                                                        </logic:notEqual>
                                                                    </td>
                                                                    <td class="columna_contenido fila_contenido" align="right" width="6%">
                                                                        <logic:notEqual name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoArticuloEstado}"/>
                                                                        </logic:notEqual>
                                                                        <logic:equal name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                            <label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
                                                                        </logic:equal>
                                                                    </td>
                                                                    <td class="columna_contenido fila_contenido" align="left" width="20%">&nbsp;<bean:write name="vistaDetallePedidoDTO" property ="observacionArticulo"/></td>
                                                                    <td class="columna_contenido fila_contenido" align="right" width="6%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVAEstado}"/></td>
                                                                    <logic:equal name="mostrarCamposDespacho" value="1">
                                                                        <td class="columna_contenido fila_contenido" align="right" width="6%">
                                                                            <logic:notEmpty name="vistaDetallePedidoDTO" property="pesoRegistradoBodega">
                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoRegistradoBodega}"/>
                                                                            </logic:notEmpty>
                                                                            <logic:empty name="vistaDetallePedidoDTO" property="pesoRegistradoBodega">---</logic:empty>
                                                                        </td>
                                                                        <td class="columna_contenido fila_contenido" align="right" width="8%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalEstadoIVA}"/></td>
                                                                        <td align="center" class="columna_contenido fila_contenido" width="2%">
                                                                            <logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
                                                                                <html:img page="/images/tick.png" border="0"/>
                                                                            </logic:equal>
                                                                            <logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
                                                                                <html:img page="/images/x.png" border="0"/>
                                                                            </logic:notEqual>
                                                                        </td>
                                                                        <td align="right" class="columna_contenido fila_contenido" width="6%">
                                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioPOS}"/>
                                                                        </td>
                                                                        <td align="right" class="columna_contenido fila_contenido" width="8%">
                                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
                                                                        </td>
                                                                    </logic:equal>
                                                                </tr>	
                                                            </logic:iterate>
                                                        </td>
                                                    </tr>
                                                    <tr><td height="15px"></td></tr>
                                                    <logic:equal name="mostrarCamposDespacho" value="1">
                                                        <tr align="right">
                                                            <td align="right" colspan="${columnas}">
                                                                <table border="0" cellpadding="0" cellspacing="0" align="right">
                                                                    <tr height="14">
                                                                        <td class="textoAzul10" align="right">SUBTOTAL (Aproximado):&nbsp;</td>
                                                                        <td class="textoNegro10" align="right">
                                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalPedido}"/>
                                                                        </td>	
                                                                    </tr>
                                                                    <tr><td colspan="2" align="right">------------------------</td></tr>
                                                                    <tr height="14">
                                                                        <td class="textoAzul10" align="right">TARIFA 0%:&nbsp;</td>
                                                                        <td class="textoNegro10" align="right">
                                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalNoAplicaIVA}"/>
                                                                        </td>
                                                                    </tr>
                                                                    <tr height="14">
                                                                        <td class="textoAzul10" align="right">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
                                                                        <td class="textoNegro10" align="right">
                                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalAplicaIVA}"/>
                                                                        </td>
                                                                    </tr>
                                                                    <tr height="14">
                                                                        <td class="textoAzul10" align="right"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
                                                                        <td class="textoNegro10" align="right">
                                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.ivaPedido}"/>
                                                                        </td>
                                                                    </tr>
                                                                    <tr><td colspan="2" align="right">------------------------</td></tr>
                                                                    <tr height="14">
                                                                        <td class="textoRojo11" align="right"><b>TOTAL (Aproximado):&nbsp;</b></td>
                                                                        <td class="textoNegro11" align="right">
                                                                        	<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.totalPedido}"/></b>
                                                                        </td>
                                                                    </tr>	                	                    	
                                                                </table>
                                                            </td>
                                                        </tr>	
                                                    </logic:equal>
                                                </table>
                                            </div>	
                                        </td>
                                    </tr>	
                                </table>
                            </td>
                        </tr>
                    </logic:notEmpty>
                </table>
            </td>
        </tr>
    </logic:notEmpty>
    <tr>
        <td id="seccionImpresion">
            <logic:notEmpty name="ec.com.smx.sic.sispe.funcionImprimir">
				<script language="JavaScript">
					imprimeSeleccion('rptDetalleEstadoPedidoEspecialTxt');
				</script>
                <!--<script language="JavaScript">window.print();</script> -->
            </logic:notEmpty>
        </td>
    </tr>
</TABLE>
<logic:notEmpty name="ec.com.smx.sic.sispe.envioMail">
	<bean:define id="valoresMail" name="ec.com.smx.sic.sispe.envioMail"/>
	<logic:empty name="valoresMail" property="cc">
		<mensajeria:enviarMail asunto="${valoresMail.asunto}" de="${valoresMail.de}" para="${valoresMail.para[0]}" host="${valoresMail.host}" puerto="${valoresMail.puerto}" 
		codigoCompania="${valoresMail.eventoDTO.id.companyId}" codigoSistema="${valoresMail.eventoDTO.id.systemId}"
		codigoEvento="${valoresMail.eventoDTO.id.codigoEvento}" reemplazarRemitente="${valoresMail.reemplazarRemitente}">
			<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro10">
				<tr>
					<td>
						<bean:write name="ec.com.smx.sic.sispe.textoMail" filter="false"/>
					</td>
				</tr>
			</table>
			<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/envioCotizacionMail.jsp" flush="false"/>
		</mensajeria:enviarMail>
	</logic:empty>
	<logic:notEmpty name="valoresMail" property="cc">
		<mensajeria:enviarMail asunto="${valoresMail.asunto}" de="${valoresMail.de}" para="${valoresMail.para[0]}" host="${valoresMail.host}" puerto="${valoresMail.puerto}" 
		codigoCompania="${valoresMail.eventoDTO.id.companyId}" codigoSistema="${valoresMail.eventoDTO.id.systemId}" cc="${valoresMail.cc[0]}"
		codigoEvento="${valoresMail.eventoDTO.id.codigoEvento}" reemplazarRemitente="${valoresMail.reemplazarRemitente}">
			<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro10">
				<tr>
					<td>
						<bean:write name="ec.com.smx.sic.sispe.textoMail" filter="false"/>
					</td>
				</tr>
			</table>
			<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/envioCotizacionMail.jsp" flush="false"/>
		</mensajeria:enviarMail>
	</logic:notEmpty>
</logic:notEmpty>