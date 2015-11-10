<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@page contentType="application/ms-excel" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
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
            
            .tituloTablas3{
				border: thin solid #999999;
				background-color: #FFFFB3;
				color: #000000;
				font-size: 10px;
				font-style: normal;
				font-family: Verdana;
			}
			.borde1{
            	border: thin solid #999999;
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
        <table border="0" cellspacing="0" cellpadding="0" align="center">
            <%--Cabecera--%>
            <tr>
                <td class="nombreCompania" colspan="3">
                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>
                </td>
            </tr>
            <tr>
                <td class="titulo" colspan="3">Reporte de Ventas</td>
            </tr>
            <%--Local--%>
            <tr>
                <td colspan="3">
                    <logic:present name="ec.com.smx.sic.sispe.nombreLocalResponsable" scope="request">
                        <span class="titulo">Local Responsable:</span>&nbsp;
                        <span class="textoNegro10"><bean:write name="ec.com.smx.sic.sispe.nombreLocalResponsableVentas" scope="request"/></span>
                    </logic:present>
                </td>
            </tr>
            
            <%--Datos de Ventas--%>
            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColExcel">
                <bean:define id="totalVentas" value="${0}"/>
                <bean:define id="totalVentasPorContacto" value="${0}"/>
                <tr><td height="6"></td></tr>
                <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta1">
	                <tr class="tituloTablas">
	                    <td class="borde" align="center">Persona Responsable</td>
	                    <td class="borde" align="center">Local</td>
	                    <td class="borde" align="center">Empresa</td>
	                    <td class="borde" align="center">rucEmpresa</td>
	                    <td class="borde" align="center">Pago del Pedido</td>
	                    <td class="borde" align="center">Fecha del Pedido</td>
	                </tr>
                </logic:notEmpty>
                <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta2">
	                <tr class="tituloTablas">
	                    <td class="borde" align="center">Local</td>
	                    <td class="borde" align="center">N&uacute;mero pedido</td>
	                    <td class="borde" align="center">N&uacute;mero reserva</td>
	                    <td class="borde" align="center">Persona responsable</td>
	                    <td class="borde" align="center">Pago del pedido</td>
	                    <td class="borde" align="center">Fecha del pedido</td>
	                </tr>
                </logic:notEmpty>
                <logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColExcel" id="vistaReporteGeneralDTO">
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta1">
	                    <tr class="grisClaro"  valign="top">
	                        <c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto != 'RUC' || (vistaReporteGeneralDTO.tipoDocumentoContacto=='RUC' && vistaReporteGeneralDTO.tipoDocumento=='R')}">																
								<td align="left">
									<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
									<bean:write name="vistaReporteGeneralDTO" property="numeroDocumentoPersona"/> -
									<bean:write name="vistaReporteGeneralDTO" property="nombrePersona"/> - 
									<bean:write name="vistaReporteGeneralDTO" property="telefonoPersona"/> 
								</td>
							</c:if>
							<c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto == 'RUC' && vistaReporteGeneralDTO.tipoDocumento!='R'}">
								<td align="left">
									<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
									<bean:write name="vistaReporteGeneralDTO" property="rucEmpresa"/> -
									<bean:write name="vistaReporteGeneralDTO" property="nombreEmpresa"/> - 	
									<bean:write name="vistaReporteGeneralDTO" property="telefonoEmpresa"/>															
								</td>
							</c:if>
							<td align="left">
								<bean:write name="vistaReporteGeneralDTO" property="id.codigoAreaTrabajo"/> - <bean:write name="vistaReporteGeneralDTO" property="nombreLocal"/>
							</td>
							<td align="left">
								<bean:write name="vistaReporteGeneralDTO" property="nombreEmpresa"/>
							</td>
							<td align="left">
								'<bean:write name="vistaReporteGeneralDTO" property="rucEmpresa"/>
							</td>
	                        <td align="left">
	                            <smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
	                                <bean:message key="label.codigoEstadoPSP"/>
	                            </smx:equal>
	                            <smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoParcial">
	                                <bean:message key="label.codigoEstadoPPA"/>
	                            </smx:equal>
	                            <smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoTotal">
	                                <bean:message key="label.codigoEstadoPTO"/>
	                            </smx:equal>
	                            <smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoLiquidado">
									<bean:message key="label.codigoEstadoLQD"/>
								</smx:equal>
	                        </td>
	                        <td align="center"><bean:write name="vistaReporteGeneralDTO" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
	                    </tr>
                    </logic:notEmpty>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta2">
	                    <tr class="grisClaro"  valign="top">
	                        <td align="center"><bean:write name="vistaReporteGeneralDTO" property="id.codigoAreaTrabajo"/> - <bean:write name="vistaReporteGeneralDTO" property="nombreLocal"/></td>	
	                        <td align="center">
	                            &nbsp;<bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/>
	                        </td>
	                        <td align="center">
	                            &nbsp;<bean:write name="vistaReporteGeneralDTO" property="llaveContratoPOS"/>
	                        </td>
	                        <c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto != 'RUC' || (vistaReporteGeneralDTO.tipoDocumentoContacto=='RUC' && vistaReporteGeneralDTO.tipoDocumento=='R')}">
								<td align="left">
									<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
									<bean:write name="vistaReporteGeneralDTO" property="numeroDocumentoPersona"/> -
									<bean:write name="vistaReporteGeneralDTO" property="nombrePersona"/> - 
									<bean:write name="vistaReporteGeneralDTO" property="telefonoPersona"/> 
								</td>
							</c:if>
							<c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto == 'RUC' && vistaReporteGeneralDTO.tipoDocumento!='R'}">
								<td align="left">
									<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
									<bean:write name="vistaReporteGeneralDTO" property="rucEmpresa"/> -
									<bean:write name="vistaReporteGeneralDTO" property="nombreEmpresa"/> - 	
									<bean:write name="vistaReporteGeneralDTO" property="telefonoEmpresa"/>															
								</td>
							</c:if>
	                        <td align="left">
	                            <smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
	                                <bean:message key="label.codigoEstadoPSP"/>
	                            </smx:equal>
	                            <smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoParcial">
	                                <bean:message key="label.codigoEstadoPPA"/>
	                            </smx:equal>
	                            <smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoTotal">
	                                <bean:message key="label.codigoEstadoPTO"/>
	                            </smx:equal>
	                            <smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoLiquidado">
									<bean:message key="label.codigoEstadoLQD"/>
								</smx:equal>
	                        </td>
	                        <td align="center"><bean:write name="vistaReporteGeneralDTO" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
	                    </tr>
                    </logic:notEmpty>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta1">
	                    <tr class="tituloTablasCeleste">
	                        <td align="center" class="borde">N&uacute;mero pedido</td>
	                        <td align="center" class="borde">Estado</td>
	                        <td align="center" class="borde">N&uacute;mero reserva</td>
	                        <td align="center" class="borde">Valor venta</td>
	                        <td align="center" class="borde">Fecha del pedido</td>
	                        <td align="center" class="borde">Ganancia otro local</td>
	                    </tr>
	                </logic:notEmpty>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta2">
	                    <tr class="tituloTablasCeleste">
	                        <td align="center" class="borde">Local entrega / Direcci&oacute;n entrega</td>
	                        <td align="center" class="borde">Art&iacute;culo</td>
	                        <td align="center" class="borde">Cantidad</td>
	                        <td align="center" class="borde">Fecha Despacho</td>
	                        <td align="center" class="borde">Fecha Entrega</td>
	                        <td align="center" class="borde">Ganancia Otro Local</td>
	                    </tr>
	                </logic:notEmpty>
                    <c:set var="total" value="${0}"/>
                    <c:set var="totalPedidoContacto" value="${0}"/>
                    <logic:iterate name="vistaReporteGeneralDTO" property="detallesReporte" id="vistaReporteGeneralDTO2">
                       <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta1">
	                         <tr class="tituloTablas3">
	                            <td class="borde" align="center">
		                            &nbsp;<bean:write name="vistaReporteGeneralDTO2" property="id.codigoPedido"/>
		                        </td>
	                            <td class="borde" align="center">
	                                <bean:write name="vistaReporteGeneralDTO2" property="descripcionEstado"/>
	                            </td>
	                            <td class="borde" align="center">
		                            &nbsp;<bean:write name="vistaReporteGeneralDTO2" property="llaveContratoPOS"/>
		                        </td>
	                            <td class="borde" align="right">
	                                <bean:write name="vistaReporteGeneralDTO2" property="totalPedido"/>
	                            </td>
	                            <td class="borde" align="center">
	                                <bean:write name="vistaReporteGeneralDTO2" property="fechaInicialEstado" formatKey="formatos.fecha"/>
	                            </td>
	                            <td align="right" class="borde">
	                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaReporteGeneralDTO2.gananciaVentasLocalEntrega}"/>
	                            </td>
	                        </tr>
	                        <tr>
		                        <td>
		                        	<table>
		                        		<tr>
		                        			<td></td>
		                        			<td align="left" class="borde1" align="center">Local Entrega / Direcci&oacute;n Entrega</td>
		                        		</tr>
		                        	</table>
		                        </td>
		                        <td class="borde1" align="center">C&oacute;digo Barras</td>
		                        <td class="borde1" align="center">Descripci&oacute;n</td>
		                        <td class="borde1" align="center">Cantidad</td>
		                        <td class="borde1" align="center">Fecha Despacho</td>
		                        <td class="borde1" align="center">Fecha Entrega</td>
		                    </tr>
		                    <logic:iterate name="vistaReporteGeneralDTO2" property="detallesReporte" id="vistaReporteGeneralDTO3">
		                    	<tr class="blanco10">
		                            <td>
		                            	<table>
			                            	<tr class="blanco10">
			                            		<td></td>
			                            		<td class="borde" align="center">
					                              <logic:notEmpty name="vistaReporteGeneralDTO3" property="direccionEntrega">
														<bean:write name="vistaReporteGeneralDTO3" property="direccionEntrega"/>
												  </logic:notEmpty>
												  <logic:empty name="vistaReporteGeneralDTO3" property="direccionEntrega">-</logic:empty>
					                            </td>  
			                            	</tr>
		                            	</table>
		                            </td>
		                            <td class="borde" align="center">
			                            &nbsp;<bean:write name="vistaReporteGeneralDTO3" property="codigoBarras"/>
			                        </td>
		                            <td class="borde">
		                                <bean:write name="vistaReporteGeneralDTO3" property="descripcionArticulo"/>
		                            </td>
			                        <td class="borde" align="right">
				                        <bean:define id="cantidad" value="${vistaReporteGeneralDTO3.cantidadEntrega}"/>
										<c:if test="${cantidad == '0'}"> 
	           			        		   <bean:write name="vistaReporteGeneralDTO3" property="cantidadEstado"/>
	           			        	    </c:if>
	           			        	    <c:if test="${cantidad != '0'}"> 
	           			        		   <bean:write name="vistaReporteGeneralDTO3" property="cantidadEntrega"/>
	           			        	    </c:if>
	           			        	</td>
		                            <td class="borde" align="center">
		                                <logic:notEmpty name="vistaReporteGeneralDTO3" property="fechaDespachoBodega">
											<bean:write name="vistaReporteGeneralDTO3" property="fechaDespachoBodega" formatKey="formatos.fecha"/>	
										</logic:notEmpty>
										<logic:empty name="vistaReporteGeneralDTO3" property="fechaDespachoBodega">-</logic:empty>
		                            </td>
		                            <td align="center" class="borde">
		                                <logic:notEmpty name="vistaReporteGeneralDTO3" property="fechaEntregaCliente">
											<bean:write name="vistaReporteGeneralDTO3" property="fechaEntregaCliente" formatKey="formatos.fecha"/>
										</logic:notEmpty>
										<logic:empty name="vistaReporteGeneralDTO3" property="fechaEntregaCliente">-</logic:empty>
		                            </td>    
		                        </tr>
		                    </logic:iterate>   
                    	</logic:notEmpty> 
                        <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta2">
	                        <tr class="blanco10">
	                            <td class="borde">
	                                <logic:notEmpty name="vistaReporteGeneralDTO2" property="direccionEntrega">
									  <bean:write name="vistaReporteGeneralDTO2" property="direccionEntrega"/>
									</logic:notEmpty>
									<logic:empty name="vistaReporteGeneralDTO2" property="direccionEntrega">-</logic:empty>
	                            </td>
	                            <td class="borde" align="left">
	                                <bean:write name="vistaReporteGeneralDTO2" property="descripcionArticulo"/>
	                            </td>
	                            <td class="borde" align="right">
		                            <bean:define id="cantidad" value="${vistaReporteGeneralDTO2.cantidadEntrega}"/>
									<c:if test="${cantidad == '0'}"> 
	       			        		   <bean:write name="vistaReporteGeneralDTO2" property="cantidadEstado"/>
	       			        	    </c:if>
	       			        	    <c:if test="${cantidad != '0'}"> 
	       			        		   <bean:write name="vistaReporteGeneralDTO2" property="cantidadEntrega"/>
	       			        	    </c:if>
	       			        	</td>
	                            <td class="borde" align="center">
	                                <logic:notEmpty name="vistaReporteGeneralDTO2" property="fechaDespachoBodega">
										<bean:write name="vistaReporteGeneralDTO2" property="fechaDespachoBodega" formatKey="formatos.fecha"/>	
									</logic:notEmpty>
									<logic:empty name="vistaReporteGeneralDTO2" property="fechaDespachoBodega">-</logic:empty>
	                            </td>
	                            <td class="borde" align="center">
	                                <logic:notEmpty name="vistaReporteGeneralDTO2" property="fechaEntregaCliente">
										<bean:write name="vistaReporteGeneralDTO2" property="fechaEntregaCliente" formatKey="formatos.fecha"/>
									</logic:notEmpty>
									<logic:empty name="vistaReporteGeneralDTO2" property="fechaEntregaCliente">-</logic:empty>
	                            </td>
	                            <td align="right" class="borde">
	                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaReporteGeneralDTO2.gananciaVentasLocalEntrega}"/>
	                            </td>
	                        </tr>
                    	</logic:notEmpty>
                        <c:set var="total" value="${total + vistaReporteGeneralDTO2.gananciaVentasLocalEntrega}"/>
                        <c:set var="totalPedidoContacto" value="${totalPedidoContacto + vistaReporteGeneralDTO2.totalPedido}"/>
                    </logic:iterate>
                    <tr>
                        <td colspan="6">
                            <table>
                                <tr class="textoNegro10">
                                    <td colspan="5">&nbsp;</td>
                                    <td>Total Ganancia Otros Locales:</td>
                                    <td align="right"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${total}"/></b></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta1">
	                    <tr>
	                        <td colspan="6">
	                            <table>
	                                <tr class="textoNegro10">
	                                    <td colspan="5">&nbsp;</td>
	                                    <td>Sub-total Ventas Contacto:</td>
	                                    <td align="right">
	                                        <c:set var="totVentaContacto" value="${totalPedidoContacto - total}"/>
	                                        <b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totVentaContacto}"/></b>
	                                    </td>
	                                </tr>
	                            </table>
	                        </td>
	                    </tr>
                    </logic:notEmpty>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta2">
	                    <tr>
	                        <td colspan="6">
	                            <table>
	                                <tr class="textoNegro10">
	                                    <td colspan="5">&nbsp;</td>
	                                    <td>Total Venta Local:</td>
	                                    <td align="right">
	                                        <c:set var="totVentaLocal" value="${vistaReporteGeneralDTO.totalPedido - total}"/>
	                                        <b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totVentaLocal}"/></b>
	                                    </td>
	                                </tr>
	                            </table>
	                        </td>
	                    </tr>
                    </logic:notEmpty>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta1">
	                    <tr>
	                        <td colspan="6">
	                            <table>
	                                <tr class="verdeClaro10">
	                                    <td colspan="5">&nbsp;</td>
	                                    <td>Total Ventas por Contacto:</td>
	                                    <td align="right">	      
	                                    	<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalPedidoContacto}"/></b>                             	
	                                    </td>																												
	                                </tr>
	                            </table>
	                        </td>
	                    </tr>
                    </logic:notEmpty>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta2">
	                    <tr>
	                        <td colspan="6">
	                            <table>
	                                <tr class="verdeClaro10">
	                                    <td colspan="5">&nbsp;</td>
	                                    <td>Total Ventas Pedido:</td>
	                                    <td align="right"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaReporteGeneralDTO.totalPedido}"/></b></td>
	                                </tr>
	                            </table>
	                        </td>
	                    </tr>
                    </logic:notEmpty>
                    <c:set var="totalVentas" value="${totalVentas + vistaReporteGeneralDTO2.totalPedido}"/>
                    <c:set var="totalVentasPorContacto" value="${totalVentasPorContacto + totalPedidoContacto}"/>
                </logic:iterate>
                <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta1">
	                <tr class="textoCeleste10">
	                    <bean:size id="cantidadTotal" name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColExcel"/>
	                    <td align="left"><b>CANTIDAD VENTAS POR CONTACTO: <bean:write name="cantidadTotal"/></b></td>
	                    <td align="right" colspan="4"><b>TOTAL VENTAS CONTACTO:</b></td>
	                    <td align="right"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalVentasPorContacto}"/></b></td>
	                </tr>
                 </logic:notEmpty>
                <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteVenta2">
	                <tr class="textoCeleste10">
	                    <bean:size id="cantidadTotal" name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColExcel"/>
	                    <td align="left"><b>CANTIDAD VENTAS: <bean:write name="cantidadTotal"/></b></td>
	                    <td align="right" colspan="4"><b>TOTAL VENTAS:</b></td>
	                    <td align="right"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalVentas}"/></b></td>
	                </tr>
                 </logic:notEmpty>
                <tr><td height="6"></td></tr>
            </logic:notEmpty>
        </table>
        <%--Fin datos de ventas--%>
    </body>
</html>