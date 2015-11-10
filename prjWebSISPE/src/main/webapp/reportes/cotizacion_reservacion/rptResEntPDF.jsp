<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@page contentType="application/pdf" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<kreport:pdfout>
	<html page-orientation="H">
		<head>
			<meta http-equiv="Content-Style-Type" content="text/css"/>
			<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
			<meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
			<meta HTTP-EQUIV="max-age" CONTENT="0"/>
			<meta HTTP-EQUIV="Expires" CONTENT="0"/>
			<style type="text/css">
			  .textoRojoC13{
		 	   font-family: Verdana, Arial, Helvetica;
			   font-size: 10px;
			   color: #E30207;
			  }
			  .saltoDePagina{
				PAGE-BREAK-AFTER: always;
			  }
    		</style>
		</head>
		<body>
			<table border="0" cellpadding="0" cellspacing="0" width="100%" class="textoNegro10">
				<tr>
					<td valign="top" font-size="7pt">
						<table border="0" cellspacing="0" cellpadding="1" width="100%">
							<tr>
								<td align="left">
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>&#32;-&#32;
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="codigoLocal"/>&#32;
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAreaTrabajo"/>
								</td>
								<td align="right">Ruc:&#32;<bean:message key="security.CURRENT_COMPANY_RUC"/></td>
							</tr>
							<tr><td height="10px" colspan="2"></td></tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="center" valign="top" font-size="7pt">
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr><td><hr/></td></tr>
							<tr>
								<td width="100%">
									<table border="0" align="left" cellspacing="0" cellpadding="1" width="100%">
										<tr>
											<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO">
												<td align="left">
													No de pedido:&#32;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="id.codigoPedido"/>
												</td>
												<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
													<td align="left">
														No de reservaci&oacute;n:&#32;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="llaveContratoPOS"/>
													</td>
												</logic:equal>
											</logic:notEmpty>
											<logic:empty name="ec.com.smx.sic.sispe.pedidoDTO">
												<td>&#32;</td>
											</logic:empty>
										</tr>
										<tr><td height="5px"></td></tr>
									</table>
								</td>
							</tr>
							<tr><td height="7px"></td></tr>
							<tr>
								<td align="right">
									<table border="0" width="100%" cellpadding="1" cellspacing="0">
										<tr>
											<td align="left">
												Lugar y fecha:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCiudad"/>,&#32;<bean:write name="ec.com.smx.sic.sispe.pedido.fechaPedido" formatKey="formatos.fecha"/>
											</td>
											<td align="left">
												Elaborado para:&#32;<bean:write name="cotizarRecotizarReservarForm" property="localResponsable"/>
											</td>
										</tr>
										<tr>
											<logic:empty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
												<td align="left">
													Tel&eacute;fono local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal"/>
												</td>
												<td align="left">
													Administrador local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador"/>
												</td>
											</logic:empty>
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
												<td align="left">
													Tel&eacute;fono local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal"/>
												</td>
												<td align="left">
													Administrador local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.administradorLocal"/>
												</td>
											</logic:notEmpty>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>		
				<tr>
					<td>
						<table width="98%" border="0" cellpadding="0" cellspacing="0">
							<logic:notEmpty name="ec.com.smx.sic.sispe.responsables.entregas">
								<tr><td><hr/></td></tr>
								<tr><td font-size="7pt">Detalle de los art&iacute;culos</td></tr>
								<tr><td><hr/></td></tr>
								<tr>
									<td font-size="7pt">
										<c:set var="totalPrevio" value="0"/>
										<table width="100%" border="0" cellpadding="1" cellspacing="0">
											<tr>
												<td align="center" width="20px" valign="top">No</td>
												<td align="center" width="60px">C&oacute;digo barras</td>
												<td align="center" width="140px">Art&iacute;culo</td>
												<td align="center" width="60px">Cant.</td>
												<td align="left" width="60px">Resp. pedido</td>
												<td align="left" width="60px">Resp. despacho</td>
												<td align="left" width="60px">Resp. entrega</td>
												<td align="center" width="50px">F.despacho</td>
												<td align="center" width="50px">F.entrega</td>
											</tr>
											<logic:iterate name="ec.com.smx.sic.sispe.responsables.entregas" id="estructuraResEnt" indexId="indiceRes">
												<bean:define id="fila" value="${indiceRes + 1}"/>
												<tr>
													<td align="left" width="20px"><bean:write name="fila"/></td>
													<td align="left" width="60px"><bean:write name="estructuraResEnt" property="detallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
													<td align="left" width="140px"><bean:write name="estructuraResEnt" property="detallePedidoDTO.articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="estructuraResEnt" property="detallePedidoDTO.articuloDTO.articuloMedidaDTO.referenciaMedida"/></td>
													<td align="center" width="60px">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${estructuraResEnt.entregaDTO.cantidadEntrega}"/>
													</td>
													<td align="left" width="60px">
														<bean:write name="estructuraResEnt" property="responsablePedido" />
													</td>
													<td align="left" width="60px">
														<bean:write name="estructuraResEnt" property="responsableDespacho" />
													</td>
													<td align="left" width="60px">
														<bean:write name="estructuraResEnt" property="responsableEntrega"/>
													</td>
													<td align="center" width="50px">
														<bean:write name="estructuraResEnt" property="entregaDTO.fechaDespachoBodega" formatKey="formatos.fecha" />
													</td>
													<td align="center" width="50px">
														<bean:write name="estructuraResEnt" property="entregaDTO.fechaEntregaCliente" formatKey="formatos.fecha" />
													</td>
												</tr>
											</logic:iterate>
										</table>
									</td>
								</tr>
							</logic:notEmpty>
						</table>
					</td>
				</tr>	
			</table>
			<%--Fin datos--%>
		</body>
	</html>
</kreport:pdfout>