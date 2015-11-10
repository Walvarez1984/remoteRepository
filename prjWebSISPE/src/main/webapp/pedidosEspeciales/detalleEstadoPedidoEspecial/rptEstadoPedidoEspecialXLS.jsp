<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<% long id = (new java.util.Date()).getTime(); %>
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
            color: #ffffff;
            }
            
			.blanco10 {	
				background-color: #FFFFFF;
				font-family: Verdana;
				font-size: 10px;
				color: #000000;
				border: thin solid #999999;
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
				border: thin solid #999999;
			}			
        </style>
    </head>
    <body>
        <table border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
                <td class="nombreCompania" >
                    CORPORACI&Oacute;N FAVORITA C.A.
                </td>                
            </tr>
            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
		        <bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
		        <bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
		        <bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
		        <tr>
		            <td>    
	                    <table  border="0" align="center" class="tabla_informacion">
                       		<tr class="fila_titulo">
                                <td class="textoNegro11" align="left" height="20px"><b>Datos de la cabecera:</b></td>
                            </tr>
                            <tr>
                                <td class="textoAzul11">
                                    <!-- INICIO CABECERA --> 
                                    <table align="left">
                                        <tr>
                                            <td class="textoAzul11" align="right" colspan="2">No. pedido:</td>
                                            <td align="left" class="textoNegro11">
                                                &nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
                                            </td>  
                                        </tr>   
                                        <!-- PARA EL CASO DE DATOS DE EMPRESA -->                                                             
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
									
										<!-- PARA EL CASO DE DATOS DEL CONTACTO PRINCIPAL DE LA PERSONA O EMPRESA -->					
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
                                            <td class="textoAzul11" align="right" colspan="2">Fecha de estado:</td>
                                            <td class="textoNegro11" align="left">
                                                <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
                                            </td>
                                            <logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoSolicitado}">
                                                <td  class="textoAzul11" align="right" >Fecha despacho:&nbsp;</td>
                                                 <td class="textoNegro11" align="left"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaDespacho" formatKey="formatos.fecha"/></td>
                                                 <td class="textoAzul11" align="right" >Fecha entrega Cliente:&nbsp;</td>
												 <td class="textoNegro11" align="left"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaEntrega" formatKey="formatos.fecha"/></td>                                               
                                            </logic:notEqual>
                                        </tr>
                                        <tr>
                                            <td class="textoAzul11" align="right" colspan="2">Estado:
                                            </td>
                                            <td class="textoNegro11" align="left" colspan="2"> 
                                                <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/>
                                            </td>
                                        </tr>
                                    </table>
                                    <!-- FIN CABECERA -->
                                </td>
                            </tr>   
	                    </table>
		            </td>
		        </tr>
		        <tr height="10"><td></td></tr>
		        <tr>
		            <td align="center">
		                <table border="0" class="tabla_informacion">
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
		                                <table border="0">	
		                                    <tr class="tituloTablas">
		                                        <td align="center" width="3%">No</td>
		                                        <td align="center" width="12%">C&oacute;digo barras</td>
		                                        <td align="center" width="20%">Art&iacute;culo</td>
		                                        <td align="center" width="5%">Cant.</td>
		                                        <td align="center" width="6%">Peso KG.</td>
		                                        <td align="center" width="20%">Observaci&oacute;n</td>
		                                        <td align="center" width="6%">V. unit.</td>
		                                        <logic:equal name="mostrarCamposDespacho" value="1">
		                                            <td align="center" width="6%">Peso reg.</td>
		                                            <td align="center" width="8%">Tot. IVA</td>
		                                            <td align="center" width="2%">IVA</td>
		                                            <td align="center" width="6%">V. unit. neto</td>
		                                            <td align="center" width="8%">Tot. neto</td>
		                                        </logic:equal>
		                                    </tr>
		                                    <tr>
		                                        <c:set var="condicion" value="0.00"/>
		                                        <td colspan="${columnas}">
		                                        	<table border="0">	
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
                                                               <td align="center" width="3%"><bean:write name="numRegistro"/></td>
                                                               <td align="left" width="12%">
                                                                 <bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/>  
                                                               </td>
                                                               <td align="left" width="20%"><bean:write  name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
                                                               <td align="ridht" width="5%">
                                                                   <logic:equal name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                       <bean:write  name="vistaDetallePedidoDTO" property="cantidadEstado"/>
                                                                   </logic:equal>
                                                                   <logic:notEqual name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                       <label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
                                                                   </logic:notEqual>
                                                               </td>
                                                               <td align="right" width="6%">
                                                                   <logic:notEqual name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                   		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoArticuloEstado}"/>
                                                                   </logic:notEqual>
                                                                   <logic:equal name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                       <label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
                                                                   </logic:equal>
                                                               </td>
                                                               <td align="left" width="20%"><bean:write name="vistaDetallePedidoDTO" property ="observacionArticulo"/></td>
                                                               <td align="right" width="6%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVAEstado}"/></td>
                                                               <logic:equal name="mostrarCamposDespacho" value="1">
                                                                   <td align="right" width="6%">
                                                                       <logic:notEmpty name="vistaDetallePedidoDTO" property="pesoRegistradoBodega">
                                                                       		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoRegistradoBodega}"/>
                                                                       </logic:notEmpty>
                                                                       <logic:empty name="vistaDetallePedidoDTO" property="pesoRegistradoBodega">---</logic:empty>
                                                                   </td>
                                                                   <td align="right" width="8%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalEstadoIVA}"/></td>
                                                                   <td align="center" width="2%">
                                                                       <logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
                                                                           SI
                                                                       </logic:equal>
                                                                       <logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
                                                                           NO
                                                                       </logic:notEqual>
                                                                   </td>
                                                                   <td align="right" width="6%">
                                                                   		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioPOS}"/>
                                                                   </td>
                                                                   <td align="right" width="8%">
                                                                   		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
                                                                   </td>
                                                               </logic:equal>
                                                           </tr>	
                                                       </logic:iterate>
	                                                    <tr><td height="15px"></td></tr>
	                                                </table>   
		                                        </td>
		                                    </tr>
		                                    <logic:equal name="mostrarCamposDespacho" value="1">
                                             <tr height="14">
                                                 <td colspan="11" class="textoAzul10" align="right">SUBTOTAL (Aproximado):&nbsp;</td>
                                                 <td class="textoNegro10" align="right">
                                                 	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalPedido}"/>
                                                 </td>	
                                             </tr>
                                             <tr><td colspan="11" align="right">------------------------</td></tr>
                                             <tr height="14">
                                                 <td colspan="11" class="textoAzul10" align="right">TARIFA 0%:&nbsp;</td>
                                                 <td class="textoNegro10" align="right">
                                                 	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalNoAplicaIVA}"/>
                                                 </td>
                                             </tr>
                                             <tr height="14">
                                                 <td colspan="11" class="textoAzul10" align="right">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
                                                 <td class="textoNegro10" align="right">
                                                 	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalAplicaIVA}"/>
                                                 </td>
                                             </tr>
                                             <tr height="14">
                                                 <td colspan="11" class="textoAzul10" align="right"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
                                                 <td class="textoNegro10" align="right">
                                                 	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.ivaPedido}"/>
                                                 </td>
                                             </tr>
                                             <tr><td colspan="11" align="right">------------------------</td></tr>
                                             <tr height="14">
                                                 <td colspan="11"class="textoRojo11" align="right"><b>TOTAL (Aproximado):&nbsp;</b></td>
                                                 <td class="textoNegro11" align="right">
                                                 	<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.totalPedido}"/></b>
                                                 </td>
                                             </tr>   	
                                            </logic:equal>	
		                                </table>
		                            </td>
		                        </tr>
		                    </logic:notEmpty>
		                </table>
		            </td>
		        </tr>
    		</logic:notEmpty>    
        </table>
    </body>
</html>