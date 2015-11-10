<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">
<html>
	<div id="reporteEtiquetaEntregaCDTxt">
		<logic:notEmpty name="ec.com.smx.sic.sispe.imprimirEtiquetas">
			<logic:notEmpty name="ec.com.sic.sispe.imprimir.etiquetas">
				<c:set var="numEntregas" value="0"/>			
				<logic:iterate name="ec.com.sic.sispe.imprimir.etiquetas" id="entregas" indexId="numActual">
					<c:set var="numEntregas" value="${numActual + 1}"/>
				</logic:iterate>			
				<logic:iterate name="ec.com.sic.sispe.imprimir.etiquetas" id="etiquetasEntregas">
					<head>
						<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
						<meta http-equiv="Content-Style-Type" content="text/css">
						<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
						<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
						<meta HTTP-EQUIV="max-age" CONTENT="0">
						<meta HTTP-EQUIV="Expires" CONTENT="0">
						<title>CONSTANCIA DE ENTREGA / RECEPCI&Oacute;N</title>
						<style type="text/css">
							body {
								font-family: Verdana, Arial, Helvetica;
								font-size: 11px;
							}
							.fila_cabecera{
								border-bottom-width: 1px;
								border-bottom-style: solid;
								
								border-top-width: 1px;
								border-top-style: solid;
								
								border-left-width: 1px;
								border-left-style: solid;
							}
							.fila_cabecera_final{
								border-width: 1px;
								border-style: solid;
							}
							
							.fila_detalle{
								border-bottom-width: 1px;
								border-bottom-style: solid;
								
								border-left-width: 1px;
								border-left-style: solid;
							}
							.fila_detalle_final{
								border-bottom-width: 1px;
								border-bottom-style: solid;
								
								border-left-width: 1px;
								border-left-style: solid;
								
								border-right-width: 1px;
								border-right-style: solid;
							}
							.borde_inferior{
								border-bottom-width: 1px;
								border-bottom-style: solid;
							}
							.saltoDePagina{
								PAGE-BREAK-AFTER: always
							}
							.textoNegro10{
								font-family: Verdana, Arial, Helvetica;
								font-size: 10px;
								color: #000000;
							}
						</style>
					</head>
					
					<body>							
						<table border="1" cellspacing="0" cellpadding="10" width="100%" class="textoNegro10">
							<tr>					
								<td colspan="6" align="center"><b>DETALLE DE LA ENTREGA</b></td>
							</tr>				
							<tr>
								<td align="left" width="35%"><b>Fecha actual:&nbsp;<bean:write name="etiquetasEntregas" property="fechaRegistro" formatKey="formatos.fecha"/></b></td>
								<td align="left" width="35%">No. de pedido:&nbsp;<bean:write name="etiquetasEntregas" property="codigoPedido"/></td>								
								<td align="left" width="30%">No. de reservaci&oacute;n:&nbsp;<bean:write name="etiquetasEntregas" property="estadoPedidoDTO.llaveContratoPOS"/></td>
							</tr>
							<c:if test="${etiquetasEntregas.estadoPedidoDTO.pedidoDTO.npNombreEmpresa != null}">
								<tr>							
									<td>EMPRESA:&nbsp;</td>				
									<td colspan="5"><b><bean:write name="etiquetasEntregas" property="estadoPedidoDTO.pedidoDTO.npNombreEmpresa"/></b></td>							
								</tr>
								<c:if test="${etiquetasEntregas.estadoPedidoDTO.pedidoDTO.npNombreContacto != null}">
									<tr>
										<td>CONTACTO:&nbsp;</td>
										<td colspan="5"><b><bean:write name="etiquetasEntregas" property="estadoPedidoDTO.pedidoDTO.npNombreContacto"/></b></td>
									</tr>
								</c:if>
							</c:if>
							<logic:empty name="etiquetasEntregas.estadoPedidoDTO.pedidoDTO.npNombreEmpresa">
								<c:if test="${etiquetasEntregas.estadoPedidoDTO.pedidoDTO.npNombreContacto != null}">
									<tr>
										<td>CLIENTE:&nbsp;</td>
										<td colspan="5"><b><bean:write name="etiquetasEntregas" property="estadoPedidoDTO.pedidoDTO.npNombreContacto"/></b></td>
									</tr>
								</c:if>
							</logic:empty>
							<tr>
								<td>TELEFONO:&nbsp;</td>
								<td colspan="5"><bean:write name="etiquetasEntregas" property="estadoPedidoDTO.pedidoDTO.npTelefonoContacto"/></td>
							</tr>
							<tr>
								<td>DIRECCION:&nbsp;</td>
								<td colspan="5"><b><bean:write name="etiquetasEntregas" property="direccionEntrega"/></b></td>
							</tr>
							<tr>
								<td>MERCADERIA ENTREGADA:</td>
								<td colspan="5">
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<logic:notEmpty name="etiquetasEntregas" property="entregaDetallePedidoCol">
											<logic:iterate name="etiquetasEntregas" property="entregaDetallePedidoCol" id="entregas">
												<tr>
													<td><b><bean:write name="entregas" property="cantidadDespachoBodega"/>&nbsp;-&nbsp;<bean:write name="entregas" property="codigoArticulo"/></b></td>
												</tr>
											</logic:iterate>
										</logic:notEmpty>
									</table>
								</td>
							</tr>
							<tr>
								<td>DESPACHADO POR:&nbsp;</td>
								<td colspan="5"></td>
							</tr>
							<tr>
								<td>HORA DE SALIDA DEL CAMION:&nbsp;</td>
								<td colspan="5"></td>
							</tr>
							<tr>
								<td>FECHA DESPACHO:&nbsp;</td>
								<td colspan="5"><bean:write name="etiquetasEntregas" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
							</tr>						
							<tr>
								<td>NOMBRE Y FIRMA DE QUIEN RECIBE:&nbsp;</td>
								<td colspan="5"></td>
							</tr>
							<tr>
								<td>SELLO DE LA EMPRESA:&nbsp;</td>
								<td colspan="5"></td>
							</tr>
						</table>
						<logic:notEqual name="numEntregas" value="1">
							<c:set var="numEntregas" value="${numEntregas - 1}"/>
							<table class="saltoDePagina"/>
						</logic:notEqual>
					</body>
				</logic:iterate>
			</logic:notEmpty>
		</logic:notEmpty>
	</div>
</html>