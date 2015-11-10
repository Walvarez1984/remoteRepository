<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">
<html>
	<head>
		<title>Detalle de pedido</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="GENERATOR" content="IBM Software Development Platform">
		<meta http-equiv="Content-Style-Type" content="text/css">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="max-age" content="0"> 
		<meta http-equiv="Expires" content="0">
		<link href="css/textos.css" rel="stylesheet" type="text/css">
		<link href="css/componentes.css" rel="stylesheet" type="text/css">
		<link href="css/estilosBotones.css" rel="stylesheet" type="text/css">
		<script language="JavaScript" src="js/util/util.js" type="text/javascript"></script>
		<script language="JavaScript" src="js/ajax.js" type="text/javascript"></script>
		<script language="JavaScript" src="js/prototype.js" type="text/javascript"></script>
		<base target="_self">
	</head>
	<body>
	<bean:define id="articuloDeBaja"><bean:message key="ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"/></bean:define>
	<bean:define id="codigoDescuentoVariable"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.descuentoVariable"/></bean:define>
	<bean:define id="articuloObsoleto"><bean:message key="ec.com.smx.sic.sispe.claseArticulo.obsoleto"/></bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoCotizado">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoCotizado" id="estadoCotizado"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoRecotizado">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoRecotizado" id="estadoRecotizado"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoReservado">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoReservado" id="estadoReservado"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoProduccion">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoProduccion" id="estadoProduccion"/>
</logic:notEmpty>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
	<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasto">
	<bean:define name="ec.com.smx.sic.sispe.tipoArticulo.canasto" id="tipoCanasto"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>

	<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
	<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
	<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
	<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloPiezaPesoUnidadManejo"><bean:message key="ec.com.smx.sic.sispe.articulo.tipoControlCosto"/></bean:define>

	<div id="articulosPedAntpopUp" class="popup"  style="top:40px;z-index:120;">
	
		<table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="90%" align="center">
                        <tr>
                            <td background="images/barralogin.gif" align="center">
                                <table cellpadding="0" cellspacing="0" width="100%" border="0">			
                                	                     
                                    <tr>
                                        <td class="textoBlanco11" align="left">
                                        	&nbsp;<b>Detalle pedido</b>
                                        </td>
                                        <!-- Boton Cerrar -->
                                        <td align="right">
                                        	<html:link title="Cerrar" href="#" onclick="requestAjax('crearCotizacion.do', ['pregunta','articulosPedAntpopUp'], {parameters: 'pedidosAnteriores=ok&accionesPedAnt=cerrarpopup', evalScripts: true});ocultarModalID('frameModal2');" >
                                        		<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
                                        	</html:link>&nbsp;
                                        </td>		                           
                                    </tr>
                                </table>
                            </td>
                        </tr>    
                        <tr>
                            <td bgcolor="#F4F5EB" valign="top">                                
                            	<table class="tabla_informacion" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
                                    <tr>								   		                                                 
                                          <td>                                  	
											<div id="mensajesPopup" style="font-size:1px;position:relative;">
														<jsp:include page="/include/mensajes.jsp"/>	
											</div>
		<div id="div_pagina" style="height:100%">
			<%-- lista de definiciones para las acciones --%>
			<bean:define id="crePedEsp"><bean:message key="ec.com.smx.sic.sispe.accion.crearPedidoEspecial"/></bean:define>
			
			<logic:notEmpty name="sispe.estado.activo">
				<bean:define name="sispe.estado.activo" id="estadoActivo"/>
			</logic:notEmpty>
			<logic:notEmpty name="sispe.estado.inactivo">
				<bean:define name="sispe.estado.inactivo" id="estadoInactivo"/>
			</logic:notEmpty>
			<bean:define id="estadoObsoleto"><bean:message key="ec.com.smx.sic.sispe.claseArticulo.obsoleto"/></bean:define>
		
				<TABLE border="0" cellspacing="0" cellpadding="2" width="100%" align="left" CLASS="textoNegro11">
					<tr>
						<td height="35px">
							<table class="titulosAccion" border="0" width="100%" cellspacing="0" cellpadding="0">
								<tr>
									<td  width="3%" align="center"><img src="images/detalle_pedidos.gif" border="0"></img></td>
                            		<td height="35" valign="middle">Detalle del pedido seleccionado</td>
									<td width="40%">
										<table align="left" cellpadding="0">
											<tr>
												<td width="12%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="naranjaClaro10" width="100%" height="12px"></td></tr></table></td>
												<td width="12%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoClaro10" width="100%" height="12px"></td></tr></table></td>
												<td width="12%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="verdeClaro10" width="100%" height="12px"></td></tr></table></td>
												<td width="12%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="celesteClaro10" width="100%" height="12px"></td></tr></table></td>
												<td width="12%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="amarilloClaro10" width="100%" height="12px"></td></tr></table></td>
											</tr>
											<tr>
												<td align="left" class="textoAzul9">Sin stock</td>
												<td align="left" class="textoAzul9">Sin alcance</td>
												<td align="left" class="textoAzul9">De baja en el SIC</td>
												<td align="left" class="textoAzul9">Inactivo en el SIC</td>
												<td align="left" class="textoAzul9">Obsoleto en el SIC</td>
											</tr>
											
										</table>
									</td>											
									<td align="right">
										<table cellspacing="0">
											<tr>
												
												<!-- esta condicion se aplica cunado se desea modificar una reserva que está pagada completamente -->
												<logic:empty name="ec.com.smx.sic.sispe.modificarReserva.pagadoTotalmente">
													<td>
														<div id="botonA">
															<a class="atrasA" href="#" onclick="requestAjax('crearCotizacion.do', ['pregunta','articulosPedAntpopUp'], {parameters: 'pedidosAnteriores=ok&accionesPedAnt=cerrarpopup', evalScripts: true});ocultarModalID('frameModal2');">Atr&aacute;s</a>
														</div>
													</td>
													<td>
														<div id="botonA">
															<a id="agregar" class="aceptarA" href="#" onclick="requestAjax('crearCotizacion.do',['mensajesPopup'], {parameters: 'pedidosAnteriores=ok&accionesPedAnt=agregarArticulos',popWait:true});">Agregar</a>
														</div>
													</td>
												</logic:empty>
												
											</tr>
							
										</table>		
									</td>
								</tr>
											
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
								<tr class="fila_titulo">
									<td colspan="4" class="textoNegro11 fila_contenido" align="left" height="20px"><b>Datos de la cabecera:</b></td>
								</tr>
								<tr>
									<td colspan="2" width="100%" class="textoAzul11" bgcolor="#F4F5EB">
										<table border="0" cellspacing="0" width="100%" align="left">
											<tr>
												<td colspan="2">
													<table cellpadding="1" cellspacing="0" width="100%">
														<tr>
															<td></td>
															<td class="textoNegro11" width="30%" align="left">
																<label class="textoAzul11">No pedido:&nbsp;</label>
																<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
																<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="codigoConsolidado">
																<b><label class="textoRojo11">
																	(CONSOLIDADO)
																</label></b>
																</logic:notEmpty>
															</td>
															<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS">
																<td align="left" class="textoNegro11" width="20%">
																	<label class="textoAzul11">No reservaci&oacute;n:&nbsp;</label>
																	<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/>
																</td>
															</logic:notEmpty>
															<td align="left" class="textoNegro11" width="20%">
																<label class="textoAzul11">Entidad responsable:&nbsp;</label>
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadLocal}">
																	<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local.descripcion"/>
																</logic:equal>
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadBodega}">
																	<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega.descripcion"/>
																</logic:equal>
															</td>
															<td align="left" class="textoNegro11" width="15%">
																<label class="textoAzul11">Precios afiliado:&nbsp;</label>
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">Si</logic:equal>
																<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">No</logic:notEqual>
															</td>
															<td align="left" class="textoNegro11" width="45%">
																<label class="textoAzul11">Pago efectivo:&nbsp;</label>
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="pagoEfectivo" value="${estadoActivo}">Si</logic:equal>
																<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="pagoEfectivo" value="${estadoActivo}">No</logic:notEqual>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado">
												<tr>
													<td class="textoAzul11" width="12%" align="right">Pedido relacionado:</td>
													<td align="left" class="textoNegro11" width="80%">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado"/>
													</td>
												</tr>
											</logic:notEmpty>
											
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa">
												<tr>
													<td colspan="2">
													<table cellpadding="1" cellspacing="0" width="100%">
														<tr>
															<td class="textoAzul11" align="right" width="12%">Empresa:</td>
															<td class="textoNegro11" align="left" width="65%">
																	<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
															</td>
															<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="fechaFinalEstado">
																<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
																	<td class="textoAzul11" align="right" width="15%">Nro bonos a recibir: </td>
																	<td class="textoNegro11" align="left" width="5%">
																			<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp"/>																			
																	</td>
																</logic:notEmpty>
															</logic:empty>
															<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
																<td class="textoAzul11" align="left" width="20%">&nbsp;</td>
															</logic:empty>
															</td>
														</tr>
													</table>
												</tr>																			
											</logic:notEmpty>
											
											<!-- PARA EL CASO DE DATOS DE PERSONA -->
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombrePersona">
												<tr>
													<td colspan="2">
													<table cellpadding="1" cellspacing="0" width="100%">
														<tr>
															<td class="textoAzul11" align="right" width="10%">Cliente:</td>
															<td class="textoNegro11" align="left" width="50%">
																	<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
															</td>
															<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="fechaFinalEstado">
																<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
																	<td class="textoAzul11" align="right" width="15%">Nro bonos a recibir: </td>
																	<td class="textoNegro11" align="left" width="5%">
																			<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp"/>																			
																	</td>
																</logic:notEmpty>
															</logic:empty>
															<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
																<td class="textoAzul11" align="left" width="20%">&nbsp;</td>
															</logic:empty>
														</tr>
													</table>
													</td>										
												</tr>
											</logic:notEmpty>	
											
											<!-- PARA EL CASO DE DATOS DEL CONTACTO PRINCIPAL DE LA PERSONA O EMPRESA -->														
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto">	
													<tr>
														<td class="textoAzul11" align="right" width="12%">Contacto:</td>																					
														<td class="textoNegro11" align="left" width="80%">		
																<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoCliente"/>&nbsp;																													
														</td>	
													</tr>										
											</logic:notEmpty>	
																										
											<tr>
												<td class="textoAzul11" align="right">Elaborado en:</td>
												<td class="textoNegro11" align="left">
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
												</td>
											</tr>
											<tr>
												<td class="textoAzul11" align="right">Fecha de estado:</td>
												<td class="textoNegro11" align="left">
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<table cellpadding="1" cellspacing="0" width="100%">
														<tr>
															<bean:define id="codigoEstadoReserva"><bean:message key="ec.com.smx.sic.sispe.estado.reservado"/></bean:define>
															<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
																<td class="textoAzul11" align="right" width="13%">Estado:</td>
																<td class="textoRojo11" align="left" width="18%">
																	<b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/></b>
																</td>
																<td class="textoAzul11" align="right" width="5%">Etapa:</td>
																<td class="textoNegro11" align="left">
																	<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="etapaEstadoActual"/>
																</td>
															</logic:notEqual>
															<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
																<td class="textoAzul11" align="right" width="13%">Estado:</td>
																<td class="textoRojo11" align="left" width="18%">
																	<b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/></b>
																</td>
																<td class="textoAzul11" align="right" width="5%">Etapa:</td>
																<td class="textoNegro11" align="left">
																	<bean:define id="pedidoReservadoPCO"><bean:message key="estado.pedidoReservado.pendienteConfirmar"/></bean:define>
																	<bean:define id="pedidoReservadoCON"><bean:message key="estado.pedidoReservado.confirmado"/></bean:define>
																	<bean:define id="pedidoReservadoEXP"><bean:message key="estado.pedidoReservado.expirado"/></bean:define>
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoPCO}">
																		<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.PCO"/></bean:define>
																	</logic:equal>
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoCON}">
																		<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.CON"/></bean:define>
																	</logic:equal>
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoEXP}">
																		<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.EXP"/></bean:define>
																	</logic:equal>
																	<bean:write name="descripcionEstadoReserva"/>
																</td>
															</logic:equal>
														</tr>
													</table>
												</td>
											</tr>
											
											
										</table>
									</td>
								</tr>
							</TABLE>
						</td>
					</tr>
					<tr>
						<td align="center" valign="top">
							<table border=0 cellspacing="0" cellpadding="0" width="100%">
								<logic:notEmpty name="cotizarRecotizarReservarForm" property="datos">
									<tr><td height="3px"></td></tr>
									
									<tr><td height="3px"></td></tr>
									<tr>	
										<td>
											
												
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
							<tr align="left" class="fila_titulo">
								<td class="textoNegro11" height="20px"><b>Detalle del pedido:</b></td>
							</tr>
							<tr align="left">
								<td  bgcolor="#F4F5EB">
									<table width="98%" border="0" cellpadding="0" cellspacing="0">
										<tr class="tituloTablas" height="15px" align="left">
											<td width="3%" class="columna_contenido">
												<html:checkbox name="cotizarRecotizarReservarForm" title="a&ntilde;adir todos los productos" property="opEscogerTodos" value="t" onclick="activarDesactivarTodo(this,cotizarRecotizarReservarForm.opEscogerProdBuscados);ocultarModales(cotizarRecotizarReservarForm.opEscogerProdBuscados);"></html:checkbox>
															
											</td>
											<td width="2%"  align="center" class="columna_contenido">No</td>  
											<td width="13%" align="center" class="columna_contenido" >C&oacute;digo barras</td>
											<td width="27%" align="center" class="columna_contenido">Art&iacute;culo</td>
											<td width="5%" align="center" class="columna_contenido"  title="cantidad total a entregar">Cant.</td>
											<td width="7%" align="center" class="columna_contenido"  >Peso Kg.</td>
											<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoProduccion}">
												<c:set value="${columnas + 1}" var="columnas"/>
												<td width="6%" align="center" class="columna_contenido"  title="cantidad producida hasta el momento">Cant. prod.</td>
											</logic:equal>
											<td width="6%" align="center" class="columna_contenido"  >V. unit.</td>
											<td width="6%" align="center" class="columna_contenido"  >V. unit. Iva</td>
											<td width="7%" align="center" class="columna_contenido" >Tot. Iva</td>
											<td width="2%" align="center" class="columna_contenido"  >Iva</td>
											<td width="3%" align="center" class="columna_contenido"  >Dscto.</td>
											<%--<td class="columna_contenido" align="center" >V.Unit Neto</td>--%>
											<td  width="7%" align="center" class="columna_contenido"  >Tot. neto</td>
										
										</tr>	
									</table>
                                </td>
                             </tr>
                             <tr>
                                <td>
									<div style="width:100%;height:350px;overflow:auto;">	
										<table cellspacing="0" cellpadding="0" width="98%" align="left">										
										<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
											<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejo}"/>
											<logic:equal name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
												<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejo}"/>
											</logic:equal>
											<c:set var="clase" value="blanco10"/> 
                                                            <logic:notEqual name="residuo" value="0">
                                                                <c:set var="clase" value="grisClaro10"/>
                                                            </logic:notEqual>
                                                            <%-- control de estilos para indicar el status completo del artículo en el SIC --%>
                                                            <c:choose>
                                                                <%-- DE BAJA EN EL SIC --%>
                                                                <c:when test="${vistaDetallePedidoDTO.articuloDTO.npEstadoArticuloSIC == articuloDeBaja || vistaDetallePedidoDTO.articuloDTO.npEstadoArticuloSICReceta == articuloDeBaja}">
                                                                    <c:set var="clase" value="verdeClaro10"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <%-- OBSOLETO EN EL SIC --%>
                                                                    <c:choose>
                                                                        <c:when test="${vistaDetallePedidoDTO.articuloDTO.claseArticulo != null && vistaDetallePedidoDTO.articuloDTO.claseArticulo == articuloObsoleto}">
                                                                            <c:set var="clase" value="amarilloClaro10"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:choose>
                                                                                <%-- SIN ALCANCE --%>
                                                                                <c:when test="${vistaDetallePedidoDTO.articuloDTO.npAlcance == estadoInactivo || vistaDetallePedidoDTO.articuloDTO.npAlcanceReceta == estadoInactivo}">
                                                                                    <c:set var="clase" value="rojoClaro10"/>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <c:choose>
                                                                                        <%-- PROBLEMAS DE STOCK --%>
                                                                                        <c:when test="${vistaDetallePedidoDTO.articuloDTO.npEstadoStockArticulo == estadoInactivo || vistaDetallePedidoDTO.articuloDTO.npEstadoStockArticuloReceta == estadoInactivo}">
                                                                                            <c:set var="clase" value="naranjaClaro10"/>
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                            <c:choose>
                                                                                                <%-- INACTIVO EN EL SIC --%>
                                                                                                <c:when test="${vistaDetallePedidoDTO.articuloDTO.npEstadoArticuloSIC == estadoInactivo || vistaDetallePedidoDTO.articuloDTO.npEstadoArticuloSICReceta == estadoInactivo}">
                                                                                                    <c:set var="clase" value="celesteClaro10"/>
                                                                                                </c:when>
                                                                                            </c:choose>
                                                                                        </c:otherwise>
                                                                                    </c:choose>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:otherwise>
                                                            </c:choose>					
											<tr class="${clase}">
												<bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
												<c:set var="pesoVariable" value=""/>
												<c:set var="imagen" value=""/>
												<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloPavo}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
													<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                                          	<c:set var="imagen" value="balanza.gif"/>
                                                    </logic:equal>
                                                    <logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                                          	<c:set var="imagen" value="pavo.gif"/>
                                                    </logic:notEqual>
												</logic:equal>
												<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
													<c:set var="imagen" value="balanza.gif"/>
												</logic:equal>	
												<td width="3%" class="columna_contenido fila_contenido" align="center">
													<c:if test="${vistaDetallePedidoDTO.articuloDTO.npEstadoArticuloSIC == articuloDeBaja || vistaDetallePedidoDTO.articuloDTO.npEstadoArticuloSICReceta == articuloDeBaja}">
														<html:multibox disabled="true" name="cotizarRecotizarReservarForm" property="opEscogerProdBuscados" onclick="ocultarModales(cotizarRecotizarReservarForm.opEscogerProdBuscados);" >
															<bean:write name="indiceDetalle"/>
														</html:multibox>
													</c:if>
													<c:if test="${vistaDetallePedidoDTO.articuloDTO.npEstadoArticuloSIC != articuloDeBaja}">
														<html:multibox name="cotizarRecotizarReservarForm" property="opEscogerProdBuscados" onclick="ocultarModales(cotizarRecotizarReservarForm.opEscogerProdBuscados);" >
															<bean:write name="indiceDetalle"/>
														</html:multibox>
													</c:if>
												</td>
												
												<td width="2%" class="columna_contenido fila_contenido" align="center"><bean:write name="numRegistro"/></td>  
																							
												<td width="13%" class="columna_contenido fila_contenido" align="center">
													<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
														<logic:notEmpty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
															<html:link href="#" onclick="requestAjax('detalleEstadoPedido.do',['informacionOrdenCompra'],{parameters: 'indiceInfoOrdenCompra=${indiceDetalle}'});mostrarModal();" title="detalle de la orden de compra"><bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/></html:link>
														</logic:notEmpty>
														<logic:empty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
															<bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/>
														</logic:empty>
													</logic:empty>
													<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
														<bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/>
													</logic:notEmpty>
												</td>
												<td width="27%" class="columna_contenido fila_contenido">
													<table border="0" width="100%" cellpadding="0" cellspacing="0">
														<tr>
															<c:if test="${vistaDetallePedidoDTO.articuloDTO.claseArticulo==articuloObsoleto}">
																<td>
		                                                            	<img src="images/obsoleto.png" border="0" title="Articulo Obsoleto"  >
																</td>
															</c:if>
															<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo == tipoCanasto || vistaDetallePedidoDTO.codigoTipoArticulo == tipoDespensa}"> --%>
															<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == articuloEspecial || vistaDetallePedidoDTO.codigoClasificacion == canastoCatalogo}">
																<td align="left">
																	<%-- secci&oacute;n para obtener el c&oacute;digo de las canastas y crear el link --%>
																	<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}
																	</logic:empty>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}
																	</logic:notEmpty>
																</td>
																<td valign="middle" align="right">
																	<bean:define id="codigoSubClasificacion" name="vistaDetallePedidoDTO" property="codigoSubClasificacion"/>
																	<bean:define id="imgSrc" value="canasto_lleno"/>
																	<c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
																		<bean:define id="imgSrc" value="despensa_llena"/>
																	</c:if>
																	<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<img src="./images/${imgSrc}.gif" border="0">
																	</logic:empty>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<img src="./images/${imgSrc}.gif" border="0">
																	</logic:notEmpty>
																	
																	<%--<bean:define id="imgSrc" value="canasto_lleno"/>
																	<logic:equal name="vistaDetallePedidoDTO" property="codigoTipoArticulo" value="${tipoDespensa}"><bean:define id="imgSrc" value="despensa_llena"/></logic:equal>
																	<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<img src="./images/${imgSrc}.gif" border="0">
																	</logic:empty>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<img src="./images/${imgSrc}.gif" border="0">
																	</logic:notEmpty> --%>
																</td>
															</c:if>
															<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo != tipoCanasto && vistaDetallePedidoDTO.codigoTipoArticulo != tipoDespensa}"> --%>
															<c:if test="${vistaDetallePedidoDTO.codigoClasificacion != articuloEspecial && vistaDetallePedidoDTO.codigoClasificacion != canastoCatalogo}">
																<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
																<logic:equal name="pesoVariable" value="${estadoActivo}">
																	<td align="right">
																	<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
				                                                       	<img title="peso variable/unidad Manejo" src="images/balanza.gif" border="0">
				                                                    </logic:equal>
				                                                    <logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
				                                                       	<img title="peso variable" src="./images/pavo.gif" border="0">
				                                                    </logic:notEqual>
																
																</td>
																</logic:equal>
															</c:if>
															<td width="2px">&nbsp;</td>
														</tr>
													</table>
												</td>
												<td  width="5%" class="columna_contenido fila_contenido" align="center" title="cantidad total a entregar">
													<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">														
														<c:if test="${vistaDetallePedidoDTO.articuloDTO.claseArticulo==articuloObsoleto}">
	                                                        <html:link href="#" title="Stock Obsoleto" style="text-decoration:none; cursor: default;">
	                                                        	<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
	                                                        </html:link>
                                                        </c:if>  
                                                        <c:if test="${vistaDetallePedidoDTO.articuloDTO.claseArticulo!=articuloObsoleto}">
                                                        	<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
                                                        </c:if>  
													</logic:notEqual>
													<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
													</logic:equal>
												</td>
												
												<td width="7%" class="columna_contenido fila_contenido" align="center">
													<logic:equal name="pesoVariable" value="${estadoActivo}">
														<logic:notEmpty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoRegistradoLocal}"/>
														</logic:notEmpty>
														<logic:empty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoArticuloEstado}"/>
														</logic:empty>
													</logic:equal>
													<logic:notEqual name="pesoVariable" value="${estadoActivo}">
														<center class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
													</logic:notEqual>
												</td>
												<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoProduccion}">
													<td width="6%" class="columna_contenido fila_contenido" align="center" title="cantidad producida hasta el momento">
														<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<bean:write name="vistaDetallePedidoDTO" property="cantidadParcialEstado"/>
														</logic:notEqual>
														<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
														</logic:equal>
													</td>
												</logic:equal>
												
												<td width="6%" class="columna_contenido fila_contenido" align="right">
													<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/>
														</logic:equal>
														<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/>
														</logic:notEqual>
													</logic:equal>
													<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
														</logic:equal>
														<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
														</logic:notEqual>
													</logic:notEqual>
												</td>
												
												<td width="6%" class="columna_contenido fila_contenido" align="right">
													<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVAEstado}"/>
														</logic:equal>
														<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/>
														</logic:notEqual>
													</logic:equal>
													<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
														</logic:equal>
														<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
														</logic:notEqual>
													</logic:notEqual>
												</td>
												<%--
												<td  class="columna_contenido fila_contenido" align="right">
													<logic:equal name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
														<logic:greaterThan name="vistaDetallePedidoDTO" property="valorCajaIVAEstado" value="0">
															(<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.unidadManejo"/>)
															<bean:write name="vistaDetallePedidoDTO" property="valorCajaIVAEstado" formatKey="formatos.numeros"/>
														</logic:greaterThan>
														<logic:lessEqual name="vistaDetallePedidoDTO" property="valorCajaIVAEstado" value="0">
															<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
														</logic:lessEqual>
													</logic:equal>
													<logic:notEqual name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
														<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
													</logic:notEqual>
												</td>
												--%>
												<td width="7%" align="right" class="columna_contenido fila_contenido">
													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorPrevioVenta}"/>
												</td>
												<td width="2%" align="center" class="columna_contenido fila_contenido">
													<logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
														<html:img page="/images/tick.png" border="0"/>
													</logic:equal>
													<logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
														<html:img page="/images/x.png" border="0"/>
													</logic:notEqual>
												</td>
												<td width="3%" align="center" class="columna_contenido fila_contenido">&nbsp;
													<logic:greaterThan name="vistaDetallePedidoDTO" property="valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
													<logic:lessThan name="vistaDetallePedidoDTO" property="valorFinalEstadoDescuento" value="0"><label class="textoRojo10">E</label></logic:lessThan>
												</td>
												<%--<td align="right" class="columna_contenido fila_contenido">
													<bean:write name="vistaDetallePedidoDTO" property="valorUnitarioPOS" formatKey="formatos.numeros"/>
												</td>--%>
												<td width="7%" align="right" class="columna_contenido fila_contenido">
													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
												</td>
											</tr>
											
										</logic:iterate>
										</table>
									</div>
								</td>
							</tr>
					</table>
								
						</logic:notEmpty>
					
										</td>
									</tr>
									<tr><td height="10px"></td></tr>
								</logic:notEmpty>
							</table>
						</td>
					</tr>
				</TABLE>
			
			</div>
		</td>
	</tr>	
										
									
</table>
</td>							
</tr>	
</table>
</div>
</body>
</html>