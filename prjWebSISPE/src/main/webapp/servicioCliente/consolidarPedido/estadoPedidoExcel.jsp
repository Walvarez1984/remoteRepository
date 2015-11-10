<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@page contentType="application/ms-excel" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
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
            
            .titulo{
            font-size: 15px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            font-weight: bolder
            }
            
            .borde{
            border: thin solid #999999;
            }
            
            .tituloTablas{
            background-color:#333333;
            color: #FFFFFF;
            font-size: 9px;
            font-family: Verdana, Arial, Helvetica;
            font-weight: bold;
            }
            
            .blanco10 {	
            background-color: #FFFFFF;
            font-family: Verdana;
            font-size: 10px;
            color: #000000;
            }
            
            .textoGris10 {	
            background-color: #CCCCCC;
            color: #000000;
            font-family: Verdana;
            font-size: 10px;	    
            }
            
            .grisClaro {
            background-color: #CCCCCC;
            font-family: Verdana;
            font-size: 11px;
            color: #000000;
            }
            
            .textoCeleste10{
            background-color: #CCFFFF;
            color: #000000;
            font-family: Verdana;
            font-size: 10px;
            }
            
            .textoNegro10{
            font-size: 10px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            }
            
            .verdeClaro10 {
            background-color: #dcf3d1;
            font-family: Verdana;
            font-size: 10px;
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
            }
            
        </style>
        
    </head>
    
    <body>
        <table border="0" width="98%" align="center" cellspacing="0" cellpadding="0">
			<%--Cabecera--%>
            <tr>
                <td class="nombreCompania" colspan="3">
                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>
                </td>
            </tr>
            <tr>
                <td class="titulo" colspan="3">ESTADO PEDIDO</td>
            </tr>
			
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedidosXLS">
				<tr>
					<td>
						<table border="0" cellspacing="0" width="98%" cellpadding="1">
							<tr class="tituloTablas">
								<td class="borde" width="4%" align="center">No</td>
								<td class="borde" width="14%" align="center">No PEDIDO</td>
								<td class="borde" width="7%" align="center">No RES.</td>
								<td class="borde" width="10%" align="center">FECHA ESTADO</td>
								<td class="borde" width="19%" align="center">CONTACTO EMPRESA</td>
								<td class="borde" width="8%" align="center">VALOR TOTAL</td>
								<td class="borde" width="8%" align="center">VALOR ABONADO</td>
								<td class="borde" width="8%" align="center">ESTADO</td>
								<td class="borde" width="6%" align="center">ACTUAL</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<logic:notEmpty name="ec.com.smx.sic.sispe.tipoPedido.especial">
							<bean:define id="pedidoEspecial" name="ec.com.smx.sic.sispe.tipoPedido.especial"/>
						</logic:notEmpty>
						<table border="0" cellspacing="0" width="98%" cellpadding="1">
							<logic:iterate name="ec.com.smx.sic.sispe.pedidosXLS" id="vistaPedidoDTO" indexId="indicePedido">
								<bean:define id="indiceGlobal" value="${indicePedido + listadoPedidosForm.start}"/>
								<bean:define id="fila" value="${indiceGlobal + 1}"/>
								<%-- control del estilo para el color de las filas --%>
								<bean:define id="residuo" value="${indicePedido % 2}"/>
								<tr>
									<td class="borde" width="4%" align="center"><bean:write name="fila"/></td>
									<td width="14%" class="borde" align="center">
										<bean:write name="vistaPedidoDTO" property="id.codigoPedido"/>
									</td>
									<td class="borde" width="7%" align="center">
										<logic:notEmpty name="vistaPedidoDTO" property="llaveContratoPOS">
											<bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/>
										</logic:notEmpty>
										<logic:empty name="llaveContratoPOS">&nbsp;</logic:empty>
									</td>
									<td class="borde" width="10%" align="center"><bean:write name="vistaPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fechahora"/></td>
									<td class="borde" width="19%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
									<td class="borde" width="8%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.totalPedido}"/></td>
									<td class="borde" width="8%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.abonoPedido}"/></td>
									<td class="borde" width="8%" align="center"><bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>
									<td class="borde" width="6%" align="center"><bean:write name="vistaPedidoDTO" property="estadoActual"/></td>
								</tr>
								<logic:notEmpty name="vistaPedidoDTO" property="vistaPedidoDTOCol">
									<tr>
										<td>&nbsp;</td>
										<td colspan="8" align="center">
											<b align="center" class="textoAzul10">Historial de modificaciones de la reserva ${vistaPedidoDTO.llaveContratoPOS}</b>
											<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
												<tr class="tituloTablasCeleste">
													<td class="borde" align="center">No Pedido</td>
													<td class="borde" align="center">No Reserva</td>
													<td class="borde" align="center">Fecha Estado</td>
													<td class="borde" align="center">V. Total</td>
													<td class="borde" align="center">V. Abono</td>
													<td class="borde" align="center">Estado</td>
												</tr>
												<logic:iterate name="vistaPedidoDTO" property="vistaPedidoDTOCol" id="vistaPedidoDTO2" indexId="indicePedido2">
													<tr class="textoNegro10">
														<td align="center" class="borde"><bean:write name="vistaPedidoDTO2" property="id.codigoPedido"/></td>
														<td align="center" class="borde"><bean:write name="vistaPedidoDTO2" property="llaveContratoPOS"/></td>
														<td align="center" class="borde"><bean:write name="vistaPedidoDTO2" property="fechaInicialEstado" formatKey="formatos.fechahora"/></td>
														<td align="right" class="borde"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO2.totalPedido}"/></td>
														<td align="right" class="borde"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO2.abonoPedido}"/></td>
														<td align="center" class="borde"><bean:write name="vistaPedidoDTO2" property="descripcionEstado"/></td>
													</tr>
												</logic:iterate>
												<tr>
													<td colspan="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</logic:notEmpty>
							</logic:iterate>
						</table>
					</td>
				</tr>
				<tr>
					<td  height="5px"></td>
				</tr>
			</logic:notEmpty>
			<logic:empty name="ec.com.smx.sic.sispe.pedidosXLS">
				<tr>
					<td colspan="9" class="textoNegro11">Seleccione un criterio de b&uacute;squeda</td>
				</tr>
			</logic:empty>
		<%--Fin datos de pedidos--%>
		</table>
    </body>
</html>