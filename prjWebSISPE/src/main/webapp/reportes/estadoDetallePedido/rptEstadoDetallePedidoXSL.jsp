<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<% long id = (new java.util.Date()).getTime(); %>
<tiles:insert page="/include/topSinMenu.jsp"/>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
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
	<bean:define name="sispe.estado.activo" id="estadoActivo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasto">
	<bean:define name="ec.com.smx.sic.sispe.tipoArticulo.canasto" id="tipoCanasto"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo">
	<bean:define id="clasificacionCanastosCatalogo" name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/>
</logic:notEmpty>

<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
<bean:define id="afiliado" name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado"/>

<%------ se obtiene la acción actual ---------%>
<%--<bean:define id="accionActual" name="ec.com.smx.sic.sispe.accion"/>
	<bean:define id="accionEstadoPedido"><bean:message key="ec.com.smx.sic.sispe.accion.estadoPedido"/></bean:define>--%>

<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<%-- se definen los c&oacute;digos de las posibles entidades responsables --%>
<bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
<bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
<%--- variables de session para persona o empresa ---%>
<%--- @author wlopez --------------------------------%>
<logic:notEmpty name="ec.com.smx.sic.sispe.persona">
	<bean:define id="persona" name="ec.com.smx.sic.sispe.persona" />
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.localizacion">
	<bean:define id="empresa" name="ec.com.smx.sic.sispe.localizacion" />
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
            
            .textoNegro9{
            font-family: Verdana, Arial, Helvetica;
            font-size: 9px;
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
			.grisClaro10 {
				background-color: #f2f2f2;
				font-family: Verdana, Arial, Helvetica, sans-serif;
				font-size: 10px;
				color: #000000;
			}
						
        </style>
    </head>
    <body>
        <table border="0" cellspacing="0" cellpadding="0" align="center" >
            <tr>
                <td class="nombreCompania" colspan="5">
                    CORPORACI&Oacute;N FAVORITA C.A.
                </td>                
            </tr>
            <tr>
                <td class="titulo" colspan="5"><b>Datos de la cabecera:</b></td>
            </tr>
            <tr>	
				<td class="textoNegro10"  align="left" colspan="2">
					<label class="textoNegro10">No. pedido:&nbsp;</label>
					<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
				</td>
				<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS">
					<td align="left" class="textoNegro10">
						<label class="textoNegro10">No. reservaci&oacute;n:&nbsp;</label>
						<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/>
					</td>
				</logic:notEmpty>
				<td align="left" class="textoNegro10">
					<label class="textoNegro10">Entidad responsable:&nbsp;</label>
					<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadLocal}">
						<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local.descripcion"/>
					</logic:equal>
					<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadBodega}">
						<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega.descripcion"/>
					</logic:equal>
				</td>
				<td align="left" class="textoNegro10"  colspan="2">
					<label class="textoNegro10">Precios afiliado:&nbsp;</label>
					<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">Si</logic:equal>
					<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">No</logic:notEqual>
				</td>			
			</tr>
			<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado">
				<tr>
					<td class="textoNegro10" align="right"  colspan="2" >Pedido relacionado:</td>
					<td align="left" class="textoNegro10">&nbsp;
						<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado"/>
					</td>
				</tr>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa">
				<tr>
					<td class="textoNegro10" align="right"  colspan="2" >Empresa:</td>
					<td class="textoNegro10" align="left" colspan="9">
						<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
					</td>
				</tr>							
			</logic:notEmpty>
			
			<!-- PARA EL CASO DE DATOS DE PERSONA -->
			<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombrePersona">
				<tr>
					<td class="textoNegro10" align="right"  colspan="2" >Cliente:</td>
					<td class="textoNegro10" align="left" colspan="9">
						<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
					</td>
				</tr>
			</logic:notEmpty>	
			
			<!-- PARA EL CASO DE DATOS DEL CONTACTO PRINCIPAL DE LA EMPRESA -->					
			<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto">
				<tr>
					<td class="textoNegro10" align="right"  colspan="2" >Contacto:</td>
					<td class="textoNegro10" align="left" colspan="9">
						<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoCliente"/>																			
					</td>
				</tr>
			</logic:notEmpty>							
	
			<tr>
				<td class="textoNegro10" align="right"  colspan="2">Elaborado en:</td>
				<td class="textoNegro10" align="left">
					<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
				</td>
			</tr>
			<tr>
				<td class="textoNegro10" align="right"  colspan="2">Fecha de estado:</td>
				<td class="textoNegro10" align="left">
					<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
				</td>
			</tr>
			<tr>
				<bean:define id="codigoEstadoReserva"><bean:message key="ec.com.smx.sic.sispe.estado.reservado"/></bean:define>
				<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
					<td class="textoNegro10" align="right" colspan="2">Estado:</td>
					<td class="textoNegro10" align="left">
						<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/>
					</td>
					<td class="textoNegro10" align="left"  colspan="2">Etapa:&nbsp;
						<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="etapaEstadoActual"/>
					</td>
				</logic:notEqual>
				<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
					<td class="textoNegro10" align="right" colspan="2" >Estado:</td>
					<td class="textoNegro10" align="left">
						<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/>
					</td>
					<td class="textoNegro10" align="left">Etapa:&nbsp;
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
			<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedido"/>
			<c:choose>
				<c:when test="${vistaPedidoDTO.id.codigoEstado != estadoCotizado && vistaPedidoDTO.id.codigoEstado != estadoRecotizado}">
					<tr>
						<td class="textoNegro10" align="right" colspan="2">Valor abono inicial:</td>
						<td>
							<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.valorAbonoInicialManual}"/>
						</td>
						<td align="left">
							<label class="textoNegro10">&nbsp;Valor abonado:</label>&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.abonoPedido}"/>
						</td>
						<td align="left">
							<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="reservarBodegaSIC" value="${estadoActivo}">
								<label class="textoNegro10">(Enviado al CD)</label>
							</logic:equal>
							<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="reservarBodegaSIC" value="${estadoActivo}">
								<label class="textoNegro10">(No enviado al CD)</label>
							</logic:notEqual>
						</td>
					</tr>
				</c:when>
			</c:choose>
			<tr><td height="6"></td></tr>
			<logic:empty name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO">
				<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
				
					<tr align="left" class="fila_titulo">
						<td colspan="6" class="titulo"><b>Detalle del pedido:</b></td>
					</tr>
					<tr>
						<td class="tituloTablas" align="center">No</td>
						<td class="tituloTablas" align="center">C&oacute;digo barras</td>
						<td class="tituloTablas" align="center">Art&iacute;culo</td>
						<td class="tituloTablas" align="center">Cant.</td>
						<td class="tituloTablas" align="center">Cant. res.</td>
						<td class="tituloTablas" align="center">Peso KG.</td>
						<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoProduccion}">
							<c:set value="${columnas + 1}" var="columnas"/>
							<td class="tituloTablas" align="center" title="cantidad producida hasta el momento">Cant. prod.</td>
						</logic:equal>
						<td class="tituloTablas" align="center" >V. unit.</td>
						<td class="tituloTablas" align="center" >V. unit. IVA</td>
						<td class="tituloTablas" align="center" >Tot. IVA</td>
						<td class="tituloTablas" align="center" >IVA</td>
						<td class="tituloTablas" align="center" >Dscto.</td>
						<td class="tituloTablas" align="center" >Tot. neto</td>
					</tr>
					<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
						<bean:define id="residuo" value="${indiceDetalle % 2}"/>
						<logic:equal name="residuo" value="0">
							<bean:define id="colorBack" value="blanco10"/>
						</logic:equal>
						<logic:notEqual name="residuo" value="0">
							<bean:define id="colorBack" value="grisClaro10"/>
						</logic:notEqual>
						<tr class="${colorBack}">
							<bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
							<c:set var="pesoVariable" value=""/>
							<c:set var="imagen" value=""/>
							<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloPavo}">
								<c:set var="pesoVariable" value="${estadoActivo}"/>
							</logic:equal>
							<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
								<c:set var="pesoVariable" value="${estadoActivo}"/>
							</logic:equal>
							<td class="borde" align="left"><bean:write name="numRegistro"/></td>
							<td class="borde" align="left">&nbsp;
								<logic:notEmpty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
									<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/>
								</logic:notEmpty>
								<logic:empty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
									<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/>
								</logic:empty>
							</td>
							
							<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo == tipoCanasto || vistaDetallePedidoDTO.codigoTipoArticulo == tipoDespensa}"> --%>
							
							<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == articuloEspecial|| vistaDetallePedidoDTO.codigoClasificacion == canastoCatalogo}">
								<td class="borde" align="left">
									<%-- secci&oacute;n para obtener el c&oacute;digo de las canastas y crear el link --%>
									<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}
									<c:set var="existenCanastos" value="${estadoActivo}"/>
								</td>
							</c:if>
							<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo != tipoCanasto && vistaDetallePedidoDTO.codigoTipoArticulo != tipoDespensa}"> --%>
							<c:if test="${vistaDetallePedidoDTO.codigoClasificacion != articuloEspecial && vistaDetallePedidoDTO.codigoClasificacion != canastoCatalogo}">
								<td class="borde" align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>						
							</c:if>
							<td class="borde" align="center">
								<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
									<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
								</logic:notEqual>
								<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
									<label class="textoNegro10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
								</logic:equal>
							</td>
							<td class="borde" align="center">
								<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
									<logic:notEmpty name="vistaDetallePedidoDTO" property="cantidadReservarSIC">
										<bean:write name="vistaDetallePedidoDTO" property="cantidadReservarSIC"/>
									</logic:notEmpty>
									<logic:empty name="vistaDetallePedidoDTO" property="cantidadReservarSIC">&nbsp;</logic:empty>
								</logic:notEqual>
								<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
									<label class="textoNegro10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
								</logic:equal>
							</td>
							<td class="borde" align="right">
								<logic:equal name="pesoVariable" value="${estadoActivo}">
									<logic:notEmpty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoRegistradoLocal}"/>
									</logic:notEmpty>
									<logic:empty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoArticuloEstado}"/>
									</logic:empty>
								</logic:equal>
								<logic:notEqual name="pesoVariable" value="${estadoActivo}">
									<center class="textoNegro10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
								</logic:notEqual>
							</td>
							<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoProduccion}">
								<td class="borde" align="center">
									<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
										<bean:write name="vistaDetallePedidoDTO" property="cantidadParcialEstado"/>
									</logic:notEqual>
									<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
										<label class="textoNegro10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
									</logic:equal>
								</td>
							</logic:equal>
							<td align="right" class="borde" valign="top">
								<table cellpadding="0" cellspacing="0" width="100%">
									<tr>															
										<td align="left" >
											<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
												T
											</logic:equal>
										</td>															
										<td align="right" >
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
										</tr>
								</table>
							</td>		
							<td align="right" class="borde" valign="top">
								<table cellpadding="0" cellspacing="0" width="100%">
									<tr>															
										<td align="left" >
											<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
												T
											</logic:equal>
										</td>															
										<td align="right" >
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
									</tr>
								</table>
							</td>		
							<td class="borde" align="right">
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorPrevioVenta}"/>
							</td>
							<td class="borde" align="center">
								<logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
									SI
								</logic:equal>
								<logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
									NO
								</logic:notEqual>
							</td>
							<td class="borde" align="center">&nbsp;
								<logic:greaterThan name="vistaDetallePedidoDTO" property="valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
								<logic:lessThan name="vistaDetallePedidoDTO" property="valorFinalEstadoDescuento" value="0"><label class="textoNegro10">E</label></logic:lessThan>
							</td>
							<td class="borde" align="right">
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
							</td>
						</tr>
						
						<%-- MUESTRA EL DETALLE DE LAS ENTREGAS --%>
						<logic:notEmpty name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol">
							<tr>
								<td>
									<table>
										<tr>
											<td>&nbsp;&nbsp;</td>
											<td class="tituloTablasCeleste1">&nbsp;&nbsp;</td>
										</tr>
										<tr>
											<td>&nbsp;&nbsp;</td>
											<td class="tituloTablasCeleste1" align="center" rowspan="1">No.</td>
										</tr>
									</table>
								</td>
								<td class="tituloTablasCeleste" align="center" rowspan="1">Cant. entrega</td>
								<td class="tituloTablasCeleste" align="center" rowspan="1">F. entrega</td>
								<td rowspan="1">
									<table>
										<tr class="tituloTablasCeleste">
											<td colspan="2" align="center" class="borde">Producci&oacute;n</td>
										</tr>
										<tr class="tituloTablasCeleste">
											<td align="center" class="borde">Tot.</td>
											<td align="center" class="borde">Pend.</td>
										</tr>
									</table>
								</td>
								<td class="tituloTablasCeleste" align="center"  rowspan="1">F. despacho</td>
								<td class="tituloTablasCeleste" align="center"  rowspan="1">Destino entrega</td>
								<td class="tituloTablasCeleste" align="center"  rowspan="1">Contexto entrega</td>
								<td class="tituloTablasCeleste" align="center"  rowspan="1">Tipo entrega</td>
								<td class="tituloTablasCeleste" align="center"  rowspan="1">Stock entrega</td>
							</tr>
							<logic:iterate name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO" indexId="numEntrega">
								<%-- control del estilo para el color de las filas --%>
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
									<td align="right" class="borde">
										<bean:write name="entregaDetallePedidoDTO" property="cantidadEntrega"/>
									</td>
									<td align="center" class="borde">
										<bean:write name="entregaPedidoDTO" property="fechaEntregaCliente" formatKey="formatos.fechahora"/>
									</td>
									<td>
										<table>
											<tr>
												<td align="right" class="borde">
													<bean:write name="entregaDetallePedidoDTO" property="cantidadDespacho"/>
												</td>
												<td align="right" class="borde">
													${entregaDetallePedidoDTO.cantidadDespacho - entregaDetallePedidoDTO.cantidadParcialDespacho}
												</td>
											</tr>
										</table>
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
									<td class="borde" align="left">
										<logic:notEmpty name="entregaPedidoDTO" property="configuracionContextoDTO">
											<bean:write name="entregaPedidoDTO" property="configuracionContextoDTO.nombreConfiguracion"/>
										</logic:notEmpty>
										<logic:empty name="entregaPedidoDTO" property="configuracionContextoDTO">&nbsp;</logic:empty>
										<logic:equal name="entregaPedidoDTO" property="codigoBodega" value="97">
											<table border="0" cellpadding="0" cellspacing="0">
											  <tr>
											   <td title="Bodega de tránsito">(Tráns)</td>
											  </tr>
											 </table> 
										</logic:equal>
									</td>
									<td class="borde" align="left">
										<logic:notEmpty name="entregaPedidoDTO" property="configuracionAlcanceDTO">
											<bean:write name="entregaPedidoDTO" property="configuracionAlcanceDTO.nombreConfiguracion"/>
										</logic:notEmpty>
										<logic:empty name="entregaPedidoDTO" property="configuracionAlcanceDTO">&nbsp;</logic:empty>
									</td>
									<td class="borde" align="left">
										<logic:notEmpty name="entregaPedidoDTO" property="configuracionStockDTO">
											<bean:write name="entregaPedidoDTO" property="configuracionStockDTO.nombreConfiguracion"/>
										</logic:notEmpty>
										<logic:empty name="entregaPedidoDTO" property="configuracionStockDTO">&nbsp;</logic:empty>
									</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
					<tr>
						<td colspan="11" class="textoRojo11" align="right">TOTAL:&nbsp;</td>
						<td class="borde,textoNegro10" align="right">
							<bean:define id="totalPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="totalPedido"/>
							<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalPedido}"/>
						</td>
					</tr>
					<logic:empty name="ec.com.smx.sic.sispe.NoMostrarTotales">
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos">
							<tr><td height="20px"> </td> </tr>
							<tr>
								<td>&nbsp;</td>
								<td align="left" class="textoAzul11" colspan="2"><b>Detalle de los descuentos</b></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td class="tituloTablas" align="center" colspan="2">DESCRIPCI&Oacute;N</td>
								<td class="tituloTablas" align="center">PORCENTAJE</td>
								<td class="tituloTablas" align="center">DESCUENTO</td>
							</tr>			
							<c:set var="totalDescuento" value="0"/>
							<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos" id="descuento" indexId="numDescuento">
								<%-- control del estilo para el color de las filas --%>
								<bean:define id="residuo" value="${numDescuento % 2}"/>
								<tr class="textoNegro10" >
								<td>&nbsp;</td>
									<td align="left" colspan="2" class="borde"><b><bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/></b></td>
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
								<td class="textoNegro10" align="right" colspan="4"><b>TOTAL:</b></td>
								<%-- <td class="textoNegro10" align="right"><b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="porcentajeTotalDescuento" formatKey="formatos.numeros"/>%</b></td> --%>
								<td class="textoNegro10" align="right"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/></b></td>
							</tr>
							<tr><td colspan="2" height="5px"></td></tr>
							<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
								<tr><td height="5px" colspan="2" class=""></td></tr>
								<tr><td colspan="4" align="center" class="tituloTablas">NRO DE BONOS A RECIBIR:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp"/></td></tr>
							</logic:notEmpty>
							
						</logic:notEmpty>
						<tr>
							<td colspan="10" class="textoAzul11" align="right">SUBTOTAL BRUTO SIN IVA:&nbsp;</td>
							<td colspan="2" class="textoNegro10" align="right">
								<bean:define id="valorTotalBrutoSinIva" name="ec.com.smx.sic.sispe.vistaPedido" property="valorTotalBrutoSinIva"/>
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorTotalBrutoSinIva}"/>
							</td>
						</tr>
						<tr>
							<td colspan="10" class="textoAzul11" align="right">(-)DESCUENTO:&nbsp;</td>
							<td colspan="2" class="textoNegro10" align="right">
								<bean:define id="totalDescuentoIva" name="ec.com.smx.sic.sispe.vistaPedido" property="totalDescuentoIva"/>
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuentoIva}"/>
							</td>
						</tr>
						<!-- datos del resumen de cantidades -->					
						<tr>
							<td colspan="10" class="textoAzul11" align="right">SUBTOTAL NETO:&nbsp;</td>
							<td colspan="2" class="textoNegro10" align="right">
								<bean:define id="subTotalNetoBruto" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNetoBruto"/>
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNetoBruto}"/>
							</td>
						</tr>	
						<tr>
							<td colspan="10" class="textoAzul11" align="right">TARIFA 0%:&nbsp;</td>
							<td colspan="2" class="textoNegro10" align="right">
								<bean:define id="subTotalNoAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNoAplicaIVA"/>
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNoAplicaIVA}"/>
							</td>
						</tr>
						<tr>
							<td colspan="10" class="textoAzul11" align="right">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
							<td colspan="2" class="textoNegro10" align="right">
								<bean:define id="subTotalAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalAplicaIVA"/>
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalAplicaIVA}"/>
							</td>
						</tr>
						<tr>
							<td colspan="10" class="textoAzul11" align="right"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
							<td colspan="2" class="textoNegro10" align="right">
								<bean:define id="ivaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="ivaPedido"/>
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ivaPedido}"/>
							</td>
						</tr>
						<tr>
							<td colspan="10" class="textoAzul11" align="right">COSTO FLETE:&nbsp;</td>
							<td colspan="2" class="textoNegro10" align="right">
								<bean:define id="valorCostoEntregaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="valorCostoEntregaPedido"/>
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorCostoEntregaPedido}"/>
							</td>
						</tr>
						<tr>
							<td colspan="10" class="textoRojo11" align="right">TOTAL:&nbsp;</td>
							<td colspan="2" class="textoNegro11" align="right">
								<bean:define id="totalPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="totalPedido"/>
								<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalPedido}"/></b>
							</td>
						</tr>
						
					</logic:empty>
				</logic:notEmpty>
			</logic:empty>
			</table>
			<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO">
				<bean:define id ="canasto" name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO"/>
					<table border="0" cellspacing="0" cellpadding="0" align="center" >
						<tr><td height="20px"></td></tr>
						<tr><td><hr/></td></tr>
						<tr><td class="titulo" align="left"><b>Detalle de los canastos</b></td></tr>
						<tr>
							<td >
								Detalle del pedido (I: valor aplicado el IVA)
							</td>
						</tr>
						<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO">
							<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo == tipoCanasto || vistaDetallePedidoDTO.codigoTipoArticulo == tipoDespensa}"> --%>
							<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == articuloEspecial}">
								<c:if test="${canasto!=null && vistaDetallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras==canasto.articuloDTO.codigoBarrasActivo.id.codigoBarras}">
									<tr>
										<td>
											<table cellpadding="1" cellspacing="0" align="left">
												<tr><td height="10px" colspan="2"></td></tr>
												<tr>
													<td align="left"><b>Descripci&oacute;n:&nbsp;</b></td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
												</tr>
												<tr>
													<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
														<td align="left"><b>Precio:&nbsp;</b></td>
														<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
													</logic:notEqual>												
													<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
														<td align="left"><b>Precio tot. sin IVA.:&nbsp;</b></td>
														<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioEstado"/></td>
														<td align="left"><b>Precio total:&nbsp;</b></td>
														<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
													</logic:equal>
													<td align="left" width="60px">&nbsp;</td>
													<td align="left"><b>Cantidad:&nbsp;</b></td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr><td height="10px"></td></tr>
									<logic:notEmpty name="vistaDetallePedidoDTO" property="articuloDTO">
										
												<table cellpadding="1" cellspacing="0" width="100%">
													<tr>
														<td align="left" class="tituloTablas">No</td>
														<td align="left" class="tituloTablas">No CAJAS</td>
														<td align="left" class="tituloTablas">C&Oacute;DIGO BARRAS</td>
														<td align="left" class="tituloTablas">ART&Iacute;CULO</td>
														<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
															<td align="right" class="tituloTablas">CANT.</td>
														</logic:notEqual>
														<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">													
															<td align="right" class="tituloTablas">CANT.</td>
															<td align="right" class="tituloTablas">V.UNIT.</td>
															<td align="right" class="tituloTablas">V.UNIT.IVA</td>
															<td align="right" class="tituloTablas">T.NETO.INC.IVA</td>														
															<td align="right" class="tituloTablas">IVA</td>
														</logic:equal>
													</tr>
													<logic:iterate name="vistaDetallePedidoDTO" property="articuloDTO.articuloRelacionCol" id="recetaArticuloDTO" indexId="registroReceta">
														<tr class="textoNegro10">
															<c:set var="unidadManejo" value="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
															<logic:equal name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.npHabilitarPrecioCaja" value="${estadoActivo}">
																<c:set var="unidadManejo" value="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
															</logic:equal>
															<bean:define id="fila" value="${registroReceta + 1}"/>
															<td class="borde" align="left"><bean:write name="fila"/></td>
															<c:if test="${unidadManejo!=null}">
															<td class="borde" align="left"><fmt:formatNumber value="${((vistaDetallePedidoDTO.cantidadEstado*recetaArticuloDTO.cantidadArticulo)/unidadManejo)+(1-(((vistaDetallePedidoDTO.cantidadEstado*recetaArticuloDTO.cantidadArticulo)/unidadManejo)%1))%1}"  maxFractionDigits="0"/></td>
															</c:if>
															<c:if test="${unidadManejo==null}">
															<td class="borde" align="left">-</td>
															</c:if>
															<td class="borde" align="left"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.codigoBarrasActivo.id.codigoBarras"/>&nbsp;</td>
															<td class="borde" align="left"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
															<td class="borde" align="right"><bean:write name="recetaArticuloDTO" property="cantidadArticulo"/></td>
															<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
																<td class="borde" align="right">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.precioUnitario}"/>
																</td>
																<td class="borde" align="right">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.precioUnitarioIVA}"/>
																</td>
																<td class="borde" align="right">
																	<c:if test="${!recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																		<c:set var="totalValorIva" value="${recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento}"/>
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																	</c:if>
																	<c:if test="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																		<c:set var="totalValorIva" value="${(recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento)  * 1.12}"/>
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																	</c:if>
																</td>
																<td class="borde" align="right">
																	<c:if test="${!recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">&nbsp;</c:if>
																	<c:if test="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">I</c:if>
																</td>	
															</logic:equal>
														</tr>
													</logic:iterate>
													<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
													<tr>
														<td colspan="4" />
														<td class="textoRojo11"align="right">TOTAL:</td >
														<td class="textoNegro11" align="right">		
															<c:if test="${afiliado != null}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
															</c:if>
															<c:if test="${afiliado == null}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</c:if>
														</td>
														<td class="textoNegro11" align="right" >
															<c:if test="${afiliado != null}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
															</c:if>
															<c:if test="${afiliado == null}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
															</c:if>
														</td>
														<td class="textoNegro11" align="right" >
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorTotalVenta}"/>
														</td>
														<td align="right">&nbsp;</td> 
													</tr>
													</logic:equal>
												</table>
											
									</logic:notEmpty>
								</c:if>	
							</c:if>
						</logic:iterate>
       			 </table>	
			</logic:notEmpty>
			<logic:equal name="existenCanastos" value="${estadoActivo}">
			<table border="0" cellspacing="0" cellpadding="0" align="center" >
						<tr><td height="20px"></td></tr>
						<tr><td><hr/></td></tr>
						<tr><td class="titulo" align="left"><b>Detalle de los canastos</b></td></tr>
						<tr>
							<td >
								Detalle del pedido (I: valor aplicado el IVA)
							</td>
						</tr>
						<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO">
							<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo == tipoCanasto || vistaDetallePedidoDTO.codigoTipoArticulo == tipoDespensa}"> --%>
							<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == articuloEspecial}">
								<tr>
									<td>
										<table cellpadding="1" cellspacing="0" align="left">
											<tr><td height="10px" colspan="2"></td></tr>
											<tr>
												<td align="left"><b>Descripci&oacute;n:&nbsp;</b></td>
												<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
											</tr>
											<tr>
												<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
													<td align="left"><b>Precio:&nbsp;</b></td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
												</logic:notEqual>												
												<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
													<td align="left"><b>Precio tot. sin IVA.:&nbsp;</b></td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioEstado"/></td>
													<td align="left"><b>Precio total:&nbsp;</b></td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
												</logic:equal>
												<td align="left" width="60px">&nbsp;</td>
												<td align="left"><b>Cantidad:&nbsp;</b></td>
												<td align="left"><bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="10px"></td></tr>
								<logic:notEmpty name="vistaDetallePedidoDTO" property="articuloDTO">
									
											<table cellpadding="1" cellspacing="0" width="100%">
												<tr>
													<td align="left" class="tituloTablas">No</td>
													<td align="left" class="tituloTablas">No CAJAS</td>
													<td align="left" class="tituloTablas">C&Oacute;DIGO BARRAS</td>
													<td align="left" class="tituloTablas">ART&Iacute;CULO</td>
													<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
														<td align="right" class="tituloTablas">CANT.</td>
													</logic:notEqual>
													<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">													
														<td align="right" class="tituloTablas">CANT.</td>
														<td align="right" class="tituloTablas">V.UNIT.</td>
														<td align="right" class="tituloTablas">V.UNIT.IVA</td>
														<td align="right" class="tituloTablas">T.NETO.INC.IVA</td>														
														<td align="right" class="tituloTablas">IVA</td>
													</logic:equal>
												</tr>
												<logic:iterate name="vistaDetallePedidoDTO" property="articuloDTO.articuloRelacionCol" id="recetaArticuloDTO" indexId="registroReceta">
													<tr class="textoNegro10">
														<c:set var="unidadManejo" value="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
														<logic:equal name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.npHabilitarPrecioCaja" value="${estadoActivo}">
															<c:set var="unidadManejo" value="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
														</logic:equal>
														<bean:define id="fila" value="${registroReceta + 1}"/>
														<td class="borde" align="left"><bean:write name="fila"/></td>
														<c:if test="${unidadManejo!=null}">
														<td class="borde" align="left"><fmt:formatNumber value="${((vistaDetallePedidoDTO.cantidadEstado*recetaArticuloDTO.cantidadArticulo)/unidadManejo)+(1-(((vistaDetallePedidoDTO.cantidadEstado*recetaArticuloDTO.cantidadArticulo)/unidadManejo)%1))%1}"  maxFractionDigits="0"/></td>
														</c:if>
														<c:if test="${unidadManejo==null}">
														<td class="borde" align="left">-</td>
														</c:if>
														<td class="borde" align="left"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.codigoBarrasActivo.id.codigoBarras"/>&nbsp;</td>
														<td class="borde" align="left"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
														<td class="borde" align="right"><bean:write name="recetaArticuloDTO" property="cantidadArticulo"/></td>
														<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
															<td class="borde" align="right">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.precioUnitario}"/>
															</td>
															<td class="borde" align="right">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.precioUnitarioIVA}"/>
															</td>
															<td class="borde" align="right">
																<c:if test="${!recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																	<c:set var="totalValorIva" value="${recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento}"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																</c:if>
																<c:if test="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																	<c:set var="totalValorIva" value="${(recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento)  * 1.12}"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																</c:if>
															</td>
															<td class="borde" align="right">
																<c:if test="${!recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">&nbsp;</c:if>
																<c:if test="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">I</c:if>
															</td>	
														</logic:equal>
													</tr>
												</logic:iterate>
												<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
												<tr>
													<td colspan="3" />
													<td class="textoRojo11"align="right">TOTAL:</td >
													<td class="textoNegro11" align="right">		
														<c:if test="${afiliado != null}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
														</c:if>
														<c:if test="${afiliado == null}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
														</c:if>
													</td>
													<td class="textoNegro11" align="right" >
														<c:if test="${afiliado != null}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
														</c:if>
														<c:if test="${afiliado == null}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
														</c:if>
													</td>
													<td class="textoNegro11" align="right" >
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorTotalVenta}"/>
													</td>
													<td align="right">&nbsp;</td> 
												</tr>
												</logic:equal>
											</table>
										
								</logic:notEmpty>
								
							</c:if>
						</logic:iterate>
        			</table>
        	</logic:equal>
    </body>
</html>