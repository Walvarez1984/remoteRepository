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
				font-size: 11px;
				color: #000000;
			}		
        </style>
    </head>
    <bean:define id="despacho"><bean:message key="ec.com.smx.sic.sispe.accion.despacho"/></bean:define>
	<bean:define id="entrega"><bean:message key="ec.com.smx.sic.sispe.accion.entrega"/></bean:define>
    <body>
        <table border="0" align="center">
            <tr>
                <td class="nombreCompania" colspan="3">
                    CORPORACI&Oacute;N FAVORITA C.A.
                </td>                
            </tr>
           <tr> 
              <td class="titulo" colspan="2">Reporte del despacho de pedidos</td>
           </tr> 
           <tr>
              <td>
                  <logic:notEmpty name="ec.com.smx.sic.sispe.reporte.despachado">
                      <logic:iterate name="ec.com.smx.sic.sispe.reporte.despachado" id="vistaReporteGeneralDTO1" indexId="indexOrdenEntrega">
                          <table border="0" class="textoNegro11">
                              <tr><td height="10px"></td></tr>
                              <tr>
                                  <td align="left"><b><label class="textoAzul11">Local or&iacute;gen:&nbsp;</label></b><bean:write name="vistaReporteGeneralDTO1" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaReporteGeneralDTO1" property="nombreLocal"/></td>
                              </tr>
                              <tr>
                                  <td>
                                      <table border="0" class="tabla_informacion" bgcolor="#B9FFFF">
                                          <tr>
                                              <td>&nbsp;</td>
                                              <td>
                                                  <logic:notEmpty name="vistaReporteGeneralDTO1" property="detallesReporte">
                                                      <logic:iterate name="vistaReporteGeneralDTO1" property="detallesReporte" id="vistaReporteGeneralDTO2" indexId="indexPedido">
                                                          <table border="0">
                                                              <tr><td height="5px"></td></tr>
                                                              <tr>
                                                                  <td align="left"><b><label class="textoRojo11">No de pedido:&nbsp;</label></b><bean:write name="vistaReporteGeneralDTO2" property="id.codigoPedido"/></td>
                                                              </tr>
                                                              <tr>
                                                                  <td>
                                                                      <table bgcolor="#FFE7CE" class="tabla_informacion">
                                                                          <tr>
                                                                              <td>
                                                                                  <table>    
        																			<tr>
		                                                                                <td colspan="2">
		                                                                                    <table cellpadding="0" cellspacing="0" width="100%">    
		                                                                                    	<logic:notEmpty name="vistaReporteGeneralDTO2" property="nombreEmpresa">                                                                                    	                                                                                   	
																									<tr>
																										<td class="textoAzul11" align="right" width="12%">Empresa:</td>
																										<td class="textoNegro11" align="left" width="80%">RUC:
																											<bean:write name="vistaReporteGeneralDTO2" property="rucEmpresa"/>&nbsp;- NE:
																											<bean:write name="vistaReporteGeneralDTO2" property="nombreEmpresa"/>&nbsp;-
																											<bean:write name="vistaReporteGeneralDTO2" property="telefonoEmpresa"/>
																										</td>
																									</tr>																			
																							</logic:notEmpty>
																								
																								<!-- PARA EL CASO DE DATOS DE PERSONA -->
																								<logic:notEmpty name="vistaReporteGeneralDTO2" property="nombrePersona">
																									<tr>
																										<td class="textoAzul11" align="right" >Cliente:</td>
																										<td class="textoNegro11" align="left" colspan="2">
																											<bean:write name="vistaReporteGeneralDTO2" property="tipoDocumentoContacto"/>:
																											<bean:write name="vistaReporteGeneralDTO2" property="numeroDocumentoPersona"/>&nbsp;- NC:
																											<bean:write name="vistaReporteGeneralDTO2" property="nombrePersona"/>&nbsp;-
																											<bean:write name="vistaReporteGeneralDTO2" property="telefonoPersona"/>																		
																										</td>
																									</tr>
																								</logic:notEmpty>	
																								
																								<!-- PARA EL CASO DE DATOS DEL CONTACTO PRINCIPAL DE LA PERSONA O EMPRESA -->																						
																								<logic:notEmpty name="vistaReporteGeneralDTO2" property="nombreContacto">																																		
																										<tr>
																											<td class="textoAzul11" align="right" >Contacto:</td>																					
																											<td class="textoNegro11" align="left" colspan="2">		
																												<bean:write name="vistaReporteGeneralDTO2" property="tipoDocumentoContacto"/>:
																												<bean:write name="vistaReporteGeneralDTO2" property="cedulaContacto"/>&nbsp;- NC:
																												<bean:write name="vistaReporteGeneralDTO2" property="nombreContacto"/>&nbsp;-
																												<bean:write name="vistaReporteGeneralDTO2" property="telefonoContacto"/>																													
																											</td>	
																										</tr>										
																								</logic:notEmpty>
		                                                                                    </table>
		                                                                                </td>
		                                                                            </tr>
                                                                                  </table>
                                                                              </td>
                                                                          </tr>
                                                                          <tr>
                                                                              <td>
                                                                                 <table border="0" align="left">
                                                                                     <logic:notEmpty name="vistaReporteGeneralDTO2" property="detallesReporte">
                                                                                         <logic:iterate name="vistaReporteGeneralDTO2" property="detallesReporte" id="vistaReporteGeneralDTO3" indexId="indexEntrega">
                                                                                             <tr><td height="5px"></td></tr>
                                                                                             <tr>                                                                                               
                                                                                                 <td align="left" class="textoNegro10"><b><label class="textoVerde11">Lugar entrega:&nbsp;</label></b><bean:write name="vistaReporteGeneralDTO3" property="direccionEntrega"/></td>
                                                                                             </tr>
                                                                                             <tr>
                                                                                                 <td>
                                                                                                     <table border="0" class="tabla_informacion" bgcolor="#D7FDD9">
                                                                                                         <tr>
                                                                                                             <td>&nbsp;</td>
                                                                                                             <td>
                                                                                                                 <table border="0">
                                                                                                                     <logic:notEmpty name="vistaReporteGeneralDTO3" property="detallesReporte">
                                                                                                                         <logic:iterate name="vistaReporteGeneralDTO3" property="detallesReporte" id="vistaReporteGeneralDTO4" indexId="indexEntregaFecha">
                                                                                                                             <tr><td height="5px"></td></tr>
                                                                                                                             <tr>                                                                                                
                                                                                                                                 <td align="left" class="textoNegro10"><b><label class="textoCafe11">Fecha de despacho&nbsp;</label></b> <bean:write name="vistaReporteGeneralDTO4" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
                                                                                                                             </tr>
                                                                                                                             <tr>
                                                                                                                               <td>
                                                                                                                                   <table border="0" class="tabla_informacion" bgcolor="#E8E8E8">
                                                                                                                                       <tr><td colspan="2"></td></tr>
                                                                                                                                       <tr>
                                                                                                                                           <td><b>Listado de art&iacute;culos:</b></td>
                                                                                                                                       </tr>
                                                                                                                                       <tr>
                                                                                                                                           <td>&nbsp;</td>
                                                                                                                                           <td>
                                                                                                                                               <table border="0" class="tabla_informacion">
                                                                                                                                                   <logic:notEmpty name="vistaReporteGeneralDTO4" property="detallesReporte">
                                                                                                                                                       <tr class="tituloTablas" align="left">
                                                                                                                                                           <td align="left">C&Oacute;DIGO BARRAS</td>
                                                                                                                                                           <td align="left">DESCRIPCI&Oacute;N</td>
                                                                                                                                                           <td align="right">CANTIDAD</td>
                                                                                                                                                       </tr>
                                                                                                                                                       <logic:iterate name="vistaReporteGeneralDTO4" property="detallesReporte" id="vistaReporteGeneralDTO5">
                                                                                                                                                           <tr>
                                                                                                                                                               <td class="borde" align="left">&nbsp;<bean:write name="vistaReporteGeneralDTO5" property="codigoBarras"/></td>
                                                                                                                                                               <td class="borde" align="left"><bean:write name="vistaReporteGeneralDTO5" property="descripcionArticulo"/></td>
                                                                                                                                                               <td class="borde" align="right"><bean:write name="vistaReporteGeneralDTO5" property="cantidadDespacharTotal"/></td>
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