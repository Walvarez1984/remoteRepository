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
            
            .tituloTablasVerde{
                background-color:#ccffcc;
                color: #000000;
                font-size: 9px;
                font-style: normal;
                line-height: normal;
                font-family: Verdana;
                font-weight: bolder;
                border: thin solid #999999;
            }

            .grisClaro {
            background-color: #eaeaea;
            font-family: Verdana;
            font-size: 11px;
            color: #000000;
            border: thin solid #999999;
            border: thin solid #999999;
            }
            
            .blanco {
            font-family: Verdana;
            font-size: 11px;
            color: #ffffff;
            }
            
            .amarilloClaro10 {
            background-color: #ffffca;
            font-family: Verdana;
            font-size: 9px;
            color: #000000;
            border: thin solid #999999;
            font-weight: bolder
            }
        </style>
    </head>
    <body>
        <table border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
                <td class="nombreCompania" colspan="5">
                    CORPORACI&Oacute;N FAVORITA C.A.
                </td>                
            </tr>
            <tr >
                <td class="titulo" colspan="5">Reporte de Entregas</td>
            </tr>
            <%--Datos de Despachos--%>
            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol">
                <tr><td height="6"></td></tr>                			         	     
		        </tr> 
		         	 <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega1">              
		                 <td align="left" class="tituloTablas" colspan="4">C&oacute;digo barras</td>
		                 <td align="left" class="tituloTablas" colspan="2">Ref. art.</td>
		                 <td align="right" class="tituloTablas" colspan="2">Cantidad</td>
		                 <td align="right" class="tituloTablas" colspan="2">Bultos</td>
		       		 </logic:notEmpty>		
		       		 <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega2">           		 
		       		  	 <td align="left" class="tituloTablas" colspan="4">Código ped.</td>
		                 <td align="left" class="tituloTablas" colspan="2">No. Res.</td>
		                 <td align="left" class="tituloTablas" colspan="2">Estado</td>
		                 <td align="center" class="tituloTablas" colspan="2">Cantidad</td>
		       		 </logic:notEmpty>
		       		 <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega3"> 
		       		 	<td align="left" class="tituloTablas" colspan="4">Fecha entrega</td>
		                <td align="left" class="tituloTablas" colspan="2">Cantidad entrega</td>		                
		       		 </logic:notEmpty>
               	</tr>                
                <logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol" id="vistaReporteGeneralDTO">
                    	<tr class="textoNegro10">   
	                    	<logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega1">                      
	                            <td class="grisClaro" align="left" colspan="4" >&nbsp;
	                            	<bean:write name="vistaReporteGeneralDTO" property="codigoBarras"/>
	                            </td>
	                            <td class="grisClaro" align="left" colspan="2">
	                            	<bean:write name="vistaReporteGeneralDTO" property="descripcionArticulo"/>
	                            </td>
	                            <td class="grisClaro" colspan="2">
	                                <b><bean:write name="vistaReporteGeneralDTO" property="cantidadDespacharTotal"/></b>
	                            </td>   
	                            <td class="grisClaro" colspan="2">
	                                <b><bean:write name="vistaReporteGeneralDTO" property="cantidadBultos"/></b>
	                            </td>  
	                        </logic:notEmpty>   
	                        <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega2">  
	                        	<td align="left" class="borde" colspan="4">&nbsp;
                                    <bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/>
                                </td>
                                <td align="left" class="borde" colspan="2">
                                    <bean:write name="vistaReporteGeneralDTO" property="llaveContratoPOS"/>
                                </td>
                                <td align="left" class="borde" colspan="2">                                	                                	                                
                                    <bean:write name="vistaReporteGeneralDTO" property="id.codigoEstado"/>
                                </td>
                                <td align="center" class="borde" colspan="2">
                                    <b> <bean:write name="vistaReporteGeneralDTO" property="cantidadDespacharTotal"/></b>
                                </td>   	                                     
	                        </logic:notEmpty>
	                        <logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega3">  
	                        	<td align="left" class="borde" colspan="4">
                                    <bean:write name="vistaReporteGeneralDTO" property="fechaEntregaCliente" formatKey="formatos.fecha"/>
                                </td>
                                <td align="left" class="borde" colspan="2">
                                    <bean:write name="vistaReporteGeneralDTO" property="cantidadDespacharTotal"/>
                                </td>
                           	</logic:notEmpty>
                    	</tr>
                    	<tr>
                    	    <td width="3px" class="blanco" >.</td>
                    		<logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega1"> 	                    	                     	                           
	                            <td align="left" class="tituloTablasCeleste" colspan="3">Código ped.</td>
	                            <td align="left" class="tituloTablasCeleste" colspan="2">No. Res.</td>
	                            <td align="left" class="tituloTablasCeleste" colspan="2">Estado</td>
	                            <td align="center" class="tituloTablasCeleste" colspan="2">Cantidad</td>                       	                    	
                    		</logic:notEmpty>
                    		<logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega2"> 	
                    		    <td align="left" class="tituloTablasCeleste" colspan="3">C&oacute;digo barras</td>
		                 		<td align="left" class="tituloTablasCeleste" colspan="2">Ref. art.</td>
		                 		<td align="right" class="tituloTablasCeleste" colspan="2">Cantidad</td>
			                 	<td align="right" class="tituloTablasCeleste" colspan="2">Bultos</td>               	                     
                    		</logic:notEmpty>
                    		<logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega3"> 
                    			<td align="left" class="tituloTablasCeleste" colspan="5">Dirección entrega</td>
                    		</logic:notEmpty>	
                    	</tr>
                    	<logic:iterate  name="vistaReporteGeneralDTO" property="detallesReporte" id="vistaReporteGeneralDTO1">
                       		<tr class="textoNegro10"> 
                       			<td width="3px" class="blanco">.</td>
                        		<logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega1">                            	                                
	                                <td align="left" class="borde" colspan="3">&nbsp;
	                                    <bean:write name="vistaReporteGeneralDTO1" property="id.codigoPedido"/>
	                                </td>
	                                <td align="left" class="borde" colspan="2">
	                                    <bean:write name="vistaReporteGeneralDTO1" property="llaveContratoPOS"/>
	                                </td>
	                                <td align="left" class="borde" colspan="2">
	                                    <bean:write name="vistaReporteGeneralDTO1" property="id.codigoEstado"/>
	                                </td>
	                                <td align="center" class="borde" colspan="2">
	                                    <b> <bean:write name="vistaReporteGeneralDTO1" property="cantidadDespacharTotal"/></b>
	                                </td>     
	                        	</logic:notEmpty>   
	                        	<logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega2">   
	                        		<td class="grisClaro" align="left" colspan="3" >&nbsp;
		                            	<bean:write name="vistaReporteGeneralDTO1" property="codigoBarras"/>
		                            </td>
		                            <td class="grisClaro" align="left" colspan="2">
		                            	<bean:write name="vistaReporteGeneralDTO1" property="descripcionArticulo"/>
		                            </td>
		                            <td class="grisClaro" colspan="2">
		                                <b><bean:write name="vistaReporteGeneralDTO1" property="cantidadDespacharTotal"/></b>
		                            </td>   
		                            <td class="grisClaro" colspan="2">
		                                <b><bean:write name="vistaReporteGeneralDTO1" property="cantidadBultos"/></b>
		                            </td>                                             
	                        	</logic:notEmpty>
	                        	<logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega3">   
	                        		<td class="grisClaro" align="left" colspan="5" >
		                            	<bean:write name="vistaReporteGeneralDTO1" property="direccionEntrega"/>
		                            </td>
		                       	</logic:notEmpty>
                       		</tr>
                       		<logic:empty name="ec.com.sic.sispe.reporte.reporteEntrega3">   
	                       		<tr>                           			
	                                <td width="3px" class="blanco">.</td>
	                                <td width="3px" class="blanco">.</td>
	                                <td align="left" class="amarilloClaro10" colspan="2">Dir. Entrega</td>
	                                <td align="left" class="amarilloClaro10" colspan="2">Fecha Entrega</td>
	                                <td align="center" class="amarilloClaro10" colspan="4">Cantidad Entrega</td>  	                        	
	                       		</tr>
	                       		<logic:iterate name="vistaReporteGeneralDTO1" property="detallesReporte" id="vistaReporteGeneralDTO2">
	                           		<tr class="textoNegro10">                              		
	                                    <td width="3px" class="blanco" >.</td>
	                                    <td width="3px" class="blanco">.</td>
	                                    <td class="borde" align="left" colspan="2"><bean:write name="vistaReporteGeneralDTO2" property="direccionEntrega"/></td>
	                                    <td class="borde" align="left" colspan="2"><bean:write name="vistaReporteGeneralDTO2" property="fechaEntregaCliente" formatKey="formatos.fecha"/></td>
	                                    <td class="borde" align="center" colspan="4"><b><bean:write name="vistaReporteGeneralDTO2" property="cantidadEntrega"/></b></td>                                    	                               
	                           		</tr>                                                       
	                       		</logic:iterate>
                       		</logic:empty>
                       		<logic:notEmpty name="ec.com.sic.sispe.reporte.reporteEntrega3">   
                       			<tr>                           			
	                                <td width="3px" class="blanco">.</td>
	                                <td width="3px" class="blanco">.</td>
	                                <td align="left" class="amarilloClaro10" colspan="1">C&oacute;digo barras</td>	   
	                             	<td align="left" class="amarilloClaro10" colspan="2">Desc. art&iacute;culo</td>	        
	                            	<td align="left" class="amarilloClaro10" colspan="1">Cantidad</td>	                                                   	
	                       		</tr>
	                       		<logic:iterate name="vistaReporteGeneralDTO1" property="detallesReporte" id="vistaReporteGeneralDTO2">
	                           		<tr class="textoNegro10">                              		
	                                    <td width="3px" class="blanco" >.</td>
	                                    <td width="3px" class="blanco">.</td>
	                                    <td class="borde" align="left" colspan="1">&nbsp;<bean:write name="vistaReporteGeneralDTO2" property="codigoBarras"/></td>	
	                                  	<td class="borde" align="left" colspan="2"><bean:write name="vistaReporteGeneralDTO2" property="descripcionArticulo"/></td>	   
	                                  	<td class="borde" align="left" colspan="1"><bean:write name="vistaReporteGeneralDTO2" property="cantidadDespacharTotal"/></td>	                                                                                                         
	                           		</tr>                                                       
	                       		</logic:iterate>
                       		</logic:notEmpty>
                       		<tr><td height="10"></td></tr>
                    	</logic:iterate>
                </logic:iterate>
            </logic:notEmpty>
        </table>
    </body>
</html>