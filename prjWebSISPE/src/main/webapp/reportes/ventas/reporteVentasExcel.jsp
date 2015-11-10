<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
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
                <tr><td height="6"></td></tr>
                <tr class="tituloTablas">
                    <td class="borde" align="center">Persona Responsable</td>
                    <td class="borde" align="center">Cédula- Contacto</td>
                </tr>
                <logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColExcel" id="vistaReporteGeneralDTO">
                    <tr class="grisClaro">                        
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
                    </tr>
                    <tr class="tituloTablasCeleste">
                        <td align="center" class="borde">N&uacute;mero pedido</td>  
                        <td align="center" class="borde">Cantidad</td>
                    </tr>
                    <logic:iterate id="vistaReporteGeneralDTO2" name="vistaReporteGeneralDTO" property="detallesReporte" >
                        <tr class="blanco10">
                            <td class="borde">
                                <bean:write name="vistaReporteGeneralDTO2" property="id.codigoPedido"/>
                            </td>
                            <td class="borde" align="right">
                                <bean:write name="vistaReporteGeneralDTO2" property="cantidadEntrega"/>
                            </td>
                        </tr>
                        <c:set var="total" value="${total + vistaReporteGeneralDTO2.gananciaVentasLocalEntrega}"/>
                    </logic:iterate>  
                </logic:iterate>
                <tr class="textoCeleste10">
                    <bean:size id="cantidadTotal" name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColExcel"/>
                    <td align="left"><b>CANTIDAD VENTAS: <bean:write name="cantidadTotal"/></b></td>
                </tr>
                <tr><td height="6"></td></tr>
            </logic:notEmpty>
        </table>
        <%--Fin datos de ventas--%>
    </body>
</html>