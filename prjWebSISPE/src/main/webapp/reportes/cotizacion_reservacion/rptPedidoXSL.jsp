<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<%-- lista de definiciones para las acciones --%>
<bean:define id="cotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.cotizacion"/></bean:define>
<bean:define id="recotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.recotizacion"/></bean:define>
<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
</logic:notEmpty>
<c:set var="imagen" value="cotizacion.gif"/>
<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
	<c:set var="imagen" value="reservacion.gif"/>
</logic:equal>
<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo">
	<bean:define id="clasificacionCanastosCatalogo" name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas">
	<bean:define id="clasificacionCanastoEspecial" name="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/>
</logic:notEmpty>
<html>
    <head>
        <meta http-equiv="Content-Style-Type" content="text/css"/>
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="max-age" CONTENT="0"/>
        <meta HTTP-EQUIV="Expires" CONTENT="0"/> 
        <style type="text/css">
            .nombreCompania{
            font-size: 20px;
            font-family: Verdana, Arial, Helvetica;
            color: #FF0033;
            font-weight: bolder
            }
            
            .borde{
            border: thin solid #999999;
            }
            .titulo{
            font-size: 15px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            font-weight: bolder
            }
            
            .tituloTablas{
            background-color:#333333;
            color: #FFFFFF;
            font-size: 9px;
            font-family: Verdana, Arial, Helvetica;
            font-weight: bold;
            border: thin solid #999999;
            }
            
            .textoNegro10{
            font-size: 10px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            }
            
            .tituloTablasCeleste{
            background-color:#DDEEFF;
            color: #000000;
            font-size: 9px;
            font-style: normal;
            line-height: normal;
            font-family: Verdana;
            font-weight: bolder;
            border: thin solid #999999;
            }
            
            .tituloTablasCeleste1{
            background-color:#DDEEFF;
            color: #000000;
            font-size: 9px;
            font-style: normal;
            line-height: normal;
            font-family: Verdana;
            font-weight: bolder;
            }
            
            .blanco {
            font-family: Verdana;
            font-style: normal;
            font-size: 9px;
            background-color:#ffffff;
            font-weight: bolder;
            color: #000000;
            }
            
            .amarilloClaro10 {
            background-color: #ffffca;
            font-family: Verdana;
            font-size: 11px;
            color: #000000;
            }
			.blanco10 {	
				background-color: #FFFFFF;
				font-family: Verdana;
				font-size: 10px;
				color: #000000;
			}
			.textoRojo11{
				font-family: Verdana, Arial, Helvetica;
				font-size: 11px;
				color: #990000;
			}	
			.textoAzul11{
				font-family: Verdana, Arial, Helvetica;
				font-size: 11px;
				color: #0000aa;
			}
			.grisClaro10{
				background-color: #f2f2f2;
				font-family: Verdana, Arial, Helvetica, sans-serif;
				font-size: 10px;
				color: #000000;
			}		
        </style>
    </head>
    <body>
        <table align="center">
            <tr>
                <td class="nombreCompania" colspan="5">
                    CORPORACI&Oacute;N FAVORITA C.A.
                </td>                
            </tr>
            <tr>
				<td colspan="3"  class="titulo" align="left"><b>Resumen del pedido:</b></td>
			</tr>
            <tr>
				<logic:notEmpty name="ec.com.smx.sic.sispe.reservacion.responsable">
					<td colspan="3" align="left">
						<label><b>Entidad responsable:</b></label>
						<bean:write name="ec.com.smx.sic.sispe.reservacion.responsable"/>
					</td>
				</logic:notEmpty>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO">
					<td colspan="2" align="right">
						<b><label>N&uacute;mero de pedido:&nbsp;</label></b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="id.codigoPedido"/>
					</td>
					<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
						<logic:empty name="ec.com.smx.sic.sispe.pedido.reservacionTemporal">
							<td colspan="2" align="right">
								<b><label>N&uacute;mero reservaci&oacute;n:&nbsp;</label></b><bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOS"/>
							</td>
						</logic:empty>
					</logic:equal>
				</logic:notEmpty>
			</tr>
			<tr><td height="8"></td></tr>
			<!--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp -->
			<tiles:insert page="/contacto/cabeceraContactoFormulario.jsp">	
				<tiles:put name="tformName" value="cotizarRecotizarReservarForm"/>
				<tiles:put name="textoNegro" value="textoNegro11"/>
				<tiles:put name="textoAzul" value="textoAzul11"/>
			</tiles:insert>					
			<tr><td height="8"></td></tr>
			<tr>	
				<td  colspan="3" class="textoNegro10"  align="left">
					<b>Elaborado en:</b>&nbsp;<html:hidden name="cotizarRecotizarReservarForm" property="localResponsable" write="true"/>
				</td>
				<%--logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSAnterior">
					<td  colspan="3" class="textoNegro10" align="right">
						<label>Reservaci&oacute;n anterior:&nbsp;</label><bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSAnterior"/>&nbsp;&nbsp;
					</td>
					<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSDevolucion">
						<td  colspan="3" class="textoNegro10" align="right">
							<label>Devoluci&oacute;n&nbsp;</label>(N&uacute;mero:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSDevolucion"/>, Valor:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npValorDevolucionPOS" formatKey="formatos.numeros"/>)&nbsp;&nbsp;
						</td>
					</logic:notEmpty>
				</logic:notEmpty--%>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSDevolucion">
					<td  colspan="3" class="textoNegro10" align="right">
						<label>Devoluci&oacute;n&nbsp;</label>(N&uacute;mero:&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.pedidoDTO.npLlaveContratoPOSDevolucion}"/>, Valor:&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.pedidoDTO.npValorDevolucionPOS}"/>)&nbsp;&nbsp;
					</td>
				</logic:notEmpty>		
			</tr>
			<tr><td height="6"></td></tr>
		</table>
		<table align="center">
			<tr>
				<td class="titulo" align="left" colspan="3"><b>Lista de los art&iacute;culos del pedido</b></td>
			</tr>
			<tr>
				<td class="tituloTablas" align="center">No</td>
				<td class="tituloTablas" align="center">C&oacute;digo barras</td>
				<td class="tituloTablas" align="center">Art&iacute;culo</td>
				<td class="tituloTablas" align="center">Cant.</td>
				<td class="tituloTablas" align="center">Peso KG.</td>
				<td class="tituloTablas" align="center">V. Unit.</td>
				<td class="tituloTablas" align="center">Tot. IVA</td>
				<td class="tituloTablas" align="center">IVA</td>
				<td class="tituloTablas" align="center">Dscto.</td>
				<td class="tituloTablas" align="center">Tot. neto</td>
			</tr>
			<logic:notEmpty name="ec.com.smx.sic.sispe.detallePedido">
				<logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detalle" indexId="indice">
					<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejo}"/>
					<logic:equal name="detalle" property="articuloDTO.npHabilitarPrecioCaja" value="${estadoActivo}">
						<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejoCaja}"/>
					</logic:equal>
					<bean:define id="numFila" value="${indice + 1}"/>
					<%-- control del estilo para el color de las filas --%>
					<bean:define id="residuo" value="${indice % 2}"/>
					<logic:equal name="residuo" value="0">
						<bean:define id="colorBack" value="blanco10"/>
					</logic:equal>
					<logic:notEqual name="residuo" value="0">
						<bean:define id="colorBack" value="grisClaro10"/>
					</logic:notEqual>
					<c:set var="pesoVariable" value=""/>
					<c:set var="imagen" value=""/>
					<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloPavo}">
						<c:set var="pesoVariable" value="${estadoActivo}"/>
					</logic:equal>
					<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
						<c:set var="pesoVariable" value="${estadoActivo}"/>
					</logic:equal>
					<tr>
						<td class="borde" align="center"><bean:write name="numFila"/></td>
						<td class="borde" align="center">&nbsp;<bean:write name="detalle" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
						<td class="borde" valign="middle" align="left">
							<bean:write name="detalle" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="detalle" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}
						</td>
						<td class="borde" align="center">
							<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
								<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
							</logic:equal>
							<logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
								<bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/>
							</logic:notEqual>
						</td>
						<td class="borde" align="right">
							<logic:equal name="pesoVariable" value="${estadoActivo}">
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
							</logic:equal>
							<logic:notEqual name="pesoVariable" value="${estadoActivo}">
								<center class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
							</logic:notEqual>
						</td>
						<td align="right" class="borde">
							<table cellpadding="0" cellspacing="0" width="100%">
								<tr>															
									<td align="left" >
										<logic:equal name="detalle" property="articuloDTO.codigoClasificacion" value="1606">
											T
										</logic:equal>
									</td>															
									<td align="right" >
										<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
										</logic:notEmpty>
										<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
										</logic:empty>
									</td>
								</tr>
							</table>
						</td
						<td class="borde" align="right">
							<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorPrevioVenta}"/>
						</td>
						<td class="borde" align="center">
							<c:set var="aplicaImpuesto" value="${detalle.articuloDTO.aplicaImpuestoVenta}"/>
							
							<c:if test="${aplicaImpuesto}">
								SI
							</c:if>
							<c:if test="${!aplicaImpuesto}">
								NO
							</c:if>
						</td>
						<td class="borde" align="center">&nbsp;
							<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
							<logic:lessThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0"><label>E</label></logic:lessThan>
						</td>
						<td class="borde" align="right">
							<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorTotalVenta}"/>
						</td>
					</tr>
					<%-- sección de detalle del pedido --%>
					<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
					<bean:define name="detalle" id="configuracionDTO"/>		
					 <logic:notEmpty name="detalle"  property="entregaDetallePedidoCol">
						<tr>
							<td align="center">
								<table>
									<tr>
										<td>&nbsp;&nbsp;</td>
										<td class="tituloTablasCeleste" align="center">No.</td>
									</tr>
								</table>
							</td>
							<td class="tituloTablasCeleste" align="center">Cant. entrega</td>
							<td class="tituloTablasCeleste" align="center">F. entrega</td>
							<td class="tituloTablasCeleste" align="center">Cant. despacho</td>
							<td class="tituloTablasCeleste" align="center">F. despacho</td>
							<td class="tituloTablasCeleste" align="center">Destino entrega</td>
						</tr>
							<logic:iterate name="detalle" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO" indexId="numEntrega">
								<%--------- control del estilo para el color de las filas -------------%>
								<bean:define id="residuoE" value="${numEntrega % 2}"/>
								<logic:equal name="residuoE" value="0">
									<bean:define id="colorBack" value="blanco10"/>
								</logic:equal>
								<logic:notEqual name="residuoE" value="0">
									<bean:define id="colorBack" value="amarilloClaro10"/>
								</logic:notEqual>
								<logic:notEmpty name="entregaDetallePedidoDTO" property="entregaPedidoDTO">
									<bean:define id="entregaPedidoDTO" property="entregaPedidoDTO" name="entregaDetallePedidoDTO"></bean:define>
								</logic:notEmpty>
								<tr class="${colorBack}">
									<td>
										<table>
											<tr>
												<td class="blanco">&nbsp;&nbsp;</td>
												<td class="borde" align="center">
													${numEntrega + 1}
												</td>
											</tr>
										</table>
									</td>
									<td class="borde" align="right">
										<bean:write name="entregaDetallePedidoDTO" property="cantidadEntrega"/>
									</td>
									<td class="borde" align="center">
										<bean:write name="entregaPedidoDTO" property="fechaEntregaCliente" formatKey="formatos.fechahora"/>
									</td>
									<td class="borde" align="right">
										<bean:write name="entregaDetallePedidoDTO" property="cantidadDespacho"/>
									</td>
									<td class="borde" align="center">
										<bean:write name="entregaPedidoDTO" property="fechaDespachoBodega" formatKey="formatos.fecha"/>
									</td>
									<td class="borde" align="left">
										 <table border="0" width="100%" cellpadding="1" cellspacing="0">
										  <tr>
										  	<td>
											   <logic:notEmpty name="entregaPedidoDTO" property="divisionGeoPoliticaDTO">
											  	 <bean:write name="entregaPedidoDTO" property="divisionGeoPoliticaDTO.nombreDivGeoPol"/>
											  	  - 
											   </logic:notEmpty>
											   <!-- Mostrar Convenio vinculado a la entrega a domicilio desde local -->
											   <logic:notEmpty name="entregaPedidoDTO" property="entregaPedidoConvenioCol">
											  		 E.DOM 
													   <logic:iterate name="entregaPedidoDTO" property="entregaPedidoConvenioCol" id="entregaPedidoConvenio">
													 		<bean:write name="entregaPedidoConvenio" property="secuencialConvenio"/>
													   </logic:iterate>
												   
												    - 
											   </logic:notEmpty>
											   <bean:write name="entregaPedidoDTO" property="direccionEntrega"/>
											   <logic:equal name="entregaPedidoDTO" property="codigoBodega" value="97">
													<table border="0" cellpadding="0" cellspacing="0">
													  <tr>
													   <td title="Bodega de tránsito">-(Tráns)</td>
													  </tr>
													</table> 
											   </logic:equal>
										   </td>
										  </tr>
										</table>
									</td>
							  </tr>
						   </logic:iterate>		
					 </logic:notEmpty>	
				  </logic:equal>
				</logic:iterate>
			</logic:notEmpty>
			<%-- se muestra el detalle de los descuentos--%>  		
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentos">
				<tr>
					<td align="left" class="fila_titulo"><b>Descuentos</b></td>
				</tr>
				<tr class="tituloTablas">
					<td align="center">DESCRIPCION</td>
					<td align="center">PORCENT.</td>
					<td align="center">DESC.</td>
				</tr>
				<c:set var="totalDescuento" value="0"/>
				<logic:iterate name="ec.com.smx.sic.sispe.pedido.descuentos" id="descuento" indexId="numDescuento">
					<%-- control del estilo para el color de las filas --%>
					<bean:define id="residuo" value="${numDescuento % 2}"/>
					<logic:equal name="residuo" value="0">
						<bean:define id="colorBack" value="blanco10"/>
					</logic:equal>
					<logic:notEqual name="residuo" value="0">
						<bean:define id="colorBack" value="grisClaro10"/>
					</logic:notEqual>
					<tr class="${colorBack}">
						<td align="left" class="borde"><bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/></td>
						<td align="right" class="borde">
							<logic:greaterThan name="descuento" property="porcentajeDescuento" value="0">
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>%
							</logic:greaterThan>
							<logic:equal name="descuento" property="porcentajeDescuento" value="0">---</logic:equal>
						</td>
						<td align="right" class="borde"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorDescuento}"/></td>
					</tr>
					<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
				</logic:iterate>
				<tr>
					<td align="right" class="textoRojo11" colspan="2"><b>TOTAL:</b></td>
					<%--<td align="right" class="textoNegro10"><b><bean:write name="ec.com.smx.sic.sispe.pedido.descuento.porcentajeTotal" formatKey="formatos.numeros"/>%</b></td> --%>
					<td align="right" class="textoNegro10"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/></b></td>
				</tr>
				<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
							<tr><td height="5px" colspan="2" class=""></td></tr>
							<tr><td colspan="4" align="center" class="tituloTablas">NRO DE BONOS A RECIBIR:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp"/></td></tr>
						</logic:notEmpty>
			</logic:notEmpty>
		 	<tr>
				<td colspan="9" align="right" class="textoAzul11">SUBTOTAL BRUTO SIN IVA :&nbsp;</td>
				<td align="right" class="textoNegro11">
					<html:hidden name="cotizarRecotizarReservarForm" property="subTotalBrutoNoAplicaIva"/>
					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalBrutoNoAplicaIva}"/>
				</td>
			</tr>
			<tr>
				<td colspan="9" align="right" class="textoAzul11">(-)DESCUENTO :&nbsp;</td>
				<td align="right" class="textoNegro11">
					<html:hidden name="cotizarRecotizarReservarForm" property="totalDescuentoIva"/>
					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.totalDescuentoIva}"/>
				</td>
			</tr>
			<tr><td colspan="9" align="right" height="5px">-------------------------</td></tr>
			<tr>
				<td colspan="9" align="right" class="textoAzul11">SUBTOTAL NETO:&nbsp;</td>
				<td align="right" class="textoNegro11">
					<html:hidden name="cotizarRecotizarReservarForm" property="subTotalNetoBruto"/>
					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNetoBruto}"/>
				</td>
			</tr>
			<tr>
				<td colspan="9" align="right" class="textoAzul11">Tarifa 0%:&nbsp;</td>
				<td align="right" class="textoNegro11">
					<html:hidden name="cotizarRecotizarReservarForm" property="subTotalNoAplicaIVA"/>
					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNoAplicaIVA}"/>
				</td>
			</tr>
			<tr>
				<td colspan="9" align="right" class="textoAzul11">Tarifa&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
				<td align="right" class="textoNegro11">
					<html:hidden name="cotizarRecotizarReservarForm" property="subTotalAplicaIVA"/>
					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalAplicaIVA}"/>
				</td>
			</tr>
			<tr>
				<td colspan="9" align="right" class="textoAzul11"><bean:message key="porcentaje.iva"/>%&nbsp;Iva:&nbsp;</td>
				<td align="right" class="textoNegro11">
					<html:hidden name="cotizarRecotizarReservarForm" property="ivaTotal"/>
					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.ivaTotal}"/>
				</td>
			</tr>
			<tr>
				<td colspan="9" align="right" class="textoAzul11">Costo Flete:&nbsp;</td>
				<td align="right" class="textoNegro11">
					<html:hidden name="cotizarRecotizarReservarForm" property="costoFlete"/>
					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.costoFlete}"/>
				</td>
			</tr>
			<tr>
				<td colspan="9" align="right" class="textoRojo11"><b>TOTAL :&nbsp;</b></td>
				<td align="right" class="textoNegro11">
					<html:hidden name="cotizarRecotizarReservarForm" property="total"/>
					<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.total}"/></b>
				</td>
			</tr>   	
    	</table>
    </body>
</html>