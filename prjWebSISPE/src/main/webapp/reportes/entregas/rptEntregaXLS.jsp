<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
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
			
			.textoCafe11{
			   	font-family: Verdana, Arial, Helvetica;
				font-size: 11px;
				color: #996600;
			}	
			.textoNaranja11{
				font-family: Verdana, Arial, Helvetica;
				font-size: 11px;
				color: #e67300;
			}
			.tabla_informacion{
				border-width: 1px;
				border-style: solid;
			    border-color: #cccccc;
			}
			.textoNegro11{
				font-family: Verdana, Arial, Helvetica;
				font-size: 13px;
				color: #000000;
			}
			.grisClaro10{
				background-color: #f2f2f2;
				font-family: Verdana, Arial, Helvetica, sans-serif;
				font-size: 10px;
				color: #000000;
				border: thin solid #999999;
			}		
        </style>
    </head>
    <bean:define id="despacho"><bean:message key="ec.com.smx.sic.sispe.accion.despacho"/></bean:define>
    <bean:define id="entrega"><bean:message key="ec.com.smx.sic.sispe.accion.entrega"/></bean:define>
    <bean:define id="etiquetaFecha" value="Fecha de entrega:"/>
     <body>
        <table border="0" align="center">
            <tr>
                <td class="nombreCompania" >
                    CORPORACI&Oacute;N FAVORITA C.A.
                </td>                
            </tr>
   		    <tr>           
                <td class="titulo" >Reporte de la entrega de pedidos</td>
            </tr>	
            <tr>
                <td>
                    <logic:notEmpty name="ec.com.smx.sic.sispe.DespachosEntregas.reporte">
                        <logic:iterate name="ec.com.smx.sic.sispe.DespachosEntregas.reporte" id="ordenDespachoEntregaRDTO" indexId="indexOrdenEntrega">
                            <table border="0" class="textoNegro11">
                                <tr><td height="10px"></td></tr>
                                <tr>
                                    <td align="left" class="textoAzul11"><b>Local origen:&nbsp;</b><bean:write name="ordenDespachoEntregaRDTO" property="descripcionLocal"/></td>
                                </tr>
                                <tr>
                                    <td>
                                        <table  bgcolor="#B9FFFF" border="0" class="tabla_informacion">
                                            <tr>
                                                <td>&nbsp;</td>
                                                <td>
                                                    <logic:notEmpty name="ordenDespachoEntregaRDTO" property="pedidos">
                                                        <logic:iterate name="ordenDespachoEntregaRDTO"  property="pedidos" id="pedidoRDTO" indexId="indexPedido">
                                                            <table border="0">
                                                                <tr><td height="5px"></td></tr>
                                                                <tr>
                                                                    <td align="left"><b><label class="textoRojo11">No de pedido:&nbsp;</label></b><bean:write name="pedidoRDTO" property="numeroPedido"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <table bgcolor="#FFE7CE" class="tabla_informacion">
                                                                            <tr>
                                                                                <td colspan="2">
                                                                    				<table cellpadding="0" cellspacing="0" width="100%"  >    
                                                                                        <tr>
																							<logic:notEmpty name="pedidoRDTO" property="cedulaContacto">
																								<td align="left" width="50%" class="textoNaranja11"><b>Datos del cliente</b></td>
																							</logic:notEmpty>
																							<logic:notEmpty name="pedidoRDTO" property="rucEmpresa">
																								<td align="left" width="50%" class="textoNaranja11"><b>Datos de la empresa</b></td>
																							</logic:notEmpty>
                                                                                        </tr>                                                                                  
                                                                                        <tr>
																							<logic:notEmpty name="pedidoRDTO" property="cedulaContacto">
																								<td width="50%">
																									<table cellpadding="1" cellspacing="0" border="0" class="textoNegro10">																					
																										<tr>
																											<td align="right"><b>C&eacute;dula/Pasaporte:</b></td>
																											<td align="left"><bean:write name="pedidoRDTO" property="cedulaContacto"/></td>
																										</tr>
																										<tr>
																											<td align="right"><b>Nombre:</b></td>
																											<td align="left"><bean:write name="pedidoRDTO" property="nombreContacto"/></td>
																										</tr>
																										<tr>
																											<td align="right"><b>Tel&eacute;fono:</b></td>
																											<td align="left"><bean:write name="pedidoRDTO" property="telefonoContacto"/></td>
																										</tr>   																								
																									</table>
																								</td>
																							</logic:notEmpty>
																							<logic:notEmpty name="pedidoRDTO" property="rucEmpresa">
																								<td width="50%">
																									<table cellpadding="1" cellspacing="0" border="0" class="textoNegro10">
																									
																										<tr>
																											<td align="right"><b>Ruc:</b></td>
																											<td align="left"><bean:write name="pedidoRDTO" property="rucEmpresa"/></td>
																										</tr>
																										<tr>
																											<td align="right"><b>Empresa:</b></td>
																											<td align="left"><bean:write name="pedidoRDTO" property="nombreEmpresa"/></td>
																										</tr>																									
																									</table>
																								</td>
																							</logic:notEmpty>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>
                                                                                   <table border="0" align="left">
                                                                                       <logic:notEmpty name="pedidoRDTO" property="entregas">
                                                                                           <logic:iterate name="pedidoRDTO" property="entregas" id="entregaRDTO" indexId="indexEntrega">
                                                                                               <tr><td height="5px"></td></tr>
                                                                                               <tr>                                                                                               
                                                                                                   <td align="left" class="textoNegro10"><b><label class="textoVerde11">Lugar entrega:&nbsp;</label></b><bean:write name="entregaRDTO" property="lugarEntrega"/></td>
                                                                                               </tr>
                                                                                               <tr>
                                                                                                   <td>
                                                                                                       <table border="0" class="tabla_informacion" bgcolor="#D7FDD9">
                                                                                                           <tr>
                                                                                                               <td>&nbsp;</td>
                                                                                                               <td>
                                                                                                                   <table border="0">
                                                                                                                       <logic:notEmpty name="entregaRDTO" property="entregasFecha">
                                                                                                                           <logic:iterate name="entregaRDTO" property="entregasFecha" id="entregaFechaRDTO" indexId="indexEntregaFecha">
                                                                                                                               <tr><td height="5px"></td></tr>
                                                                                                                               <tr>                                                                                                
                                                                                                                                   <td align="left" class="textoNegro10"><b><label class="textoCafe11">${etiquetaFecha}&nbsp;</label></b><bean:write name="entregaFechaRDTO" property="fechaDespachoEntrega"/></td>
                                                                                                                               </tr>
                                                                                                                               <tr>
                                                                                                                                   <td>
                                                                                                                                       <table border="0" class="tabla_informacion" bgcolor="#E8E8E8">
                                                                                                                                           <tr>
                                                                                                                                               <td>&nbsp;</td>
                                                                                                                                               <td>
                                                                                                                                                   <table border="0">
                                                                                                                                                       <logic:notEmpty name="entregaFechaRDTO" property="beneficiarios">
                                                                                                                                                           <logic:iterate name="entregaFechaRDTO" property="beneficiarios" id="beneficiarioRDTO">
                                                                                                                                                               <tr><td height="5px"></td></tr>
                                                                                                                                                               <tr>
                                                                                                                                                                   <td valign="bottom"><b>Listado de art&iacute;culos:</b></td>
                                                                                                                                                               </tr>
                                                                                                                                                               <tr>
                                                                                                                                                                   <td>
                                                                                                                                                                       <table border="0" class="tabla_informacion">
                                                                                                                                                                           <tr class="tituloTablas" align="left">
                                                                                                                                                                               <td align="left">C&Oacute;DIGO BARRAS</td>
                                                                                                                                                                               <td colspan="3" align="left">DESCRIPCI&Oacute;N</td>
                                                                                                                                                                               <td align="right">CANTIDAD</td>
                                                                                                                                                                           </tr>
                                                                                                                                                                           <logic:notEmpty name="beneficiarioRDTO" property="articulos">	
                                                                                                                                                                               <logic:iterate name="beneficiarioRDTO" property="articulos" id="articuloRDTO" indexId="indiceArticulo">
                                                                                                                                                                                   <tr>
                                                                                                                                                                                       <td class="grisClaro10" align="left">&nbsp;<bean:write name="articuloRDTO" property="codigoBarras"/></td>
                                                                                                                                                                                       <td class="grisClaro10" colspan="3" align="left"><bean:write name="articuloRDTO" property="descripcionArticulo"/></td>
                                                                                                                                                                                       <td class="grisClaro10" align="right"><bean:write name="articuloRDTO" property="cantidadArticulo"/></td>
                                                                                                                                                                                   </tr>
                                                                                                                                                                               </logic:iterate>
                                                                                                                                                                           </logic:notEmpty>
                                                                                                                                                                       </table>	
                                                                                                                                                                   </td>
                                                                                                                                                               </tr>
                                                                                                                                                           </logic:iterate>
                                                                                                                                                       </logic:notEmpty>
                                                                                                                                                   </table>
                                                                                                                                               </td>
                                                                                                                                           </tr>
                                                                                                                                       </table>
                                                                                                                                   </td>
                                                                                                                               </tr>
                                                                                                                           </logic:iterate>
                                                                                                                       </logic:notEmpty>
                                                                                                                   </table>
                                                                                                               </td>
                                                                                                           </tr>
                                                                                                       </table>
                                                                                                   </td>
                                                                                               </tr>
                                                                                           </logic:iterate>
                                                                                       </logic:notEmpty>									
                                                                                   </table>
                                                                               </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </logic:iterate>
                                                    </logic:notEmpty>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>	
                        </logic:iterate>
                    </logic:notEmpty>
                </td>		
            </tr>
        </table>
    </body>
</html>