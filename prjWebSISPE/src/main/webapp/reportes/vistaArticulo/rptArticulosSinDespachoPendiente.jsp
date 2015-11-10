<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<html>
    <head>
        <style type="text/css">
            .textoNegro10{
            font-size: 10px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            }
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
        </style>
    </head>
    <body>
        <table border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
                <td class="nombreCompania" colspan="3">
                    CORPORACI&Oacute;N FAVORITA C.A.
                </td>
            </tr>
            <tr>
                <td class="titulo" colspan="3">Reporte de art&iacute;culos</td>
            </tr>
            <tr><td height="6"></td></tr>
            <tr>
                <td class="tituloTablas"  align="center">C&oacute;digo</td>
                <td class="tituloTablas"  align="center">Art&iacute;culo</td>
                <td class="tituloTablas" align="center">Solicitado</td>
                <td  class="tituloTablas"  align="center">Despachado</td>
                <td  class="tituloTablas" align="center">Pendiente</td>
            </tr>
            
            <logic:iterate  name="controlProduccionForm" property="datos" id="vistaArticuloDTO" >
                <tr class="textoNegro10">
                    <td class="borde" align="left"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
                    <td class="borde"   align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
                    <td class="borde"  align="right"><bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/></td>
                    <td class="borde"   align="right"><bean:write name="vistaArticuloDTO" property="cantidadParcialEstado"/></td>
                    <td class="borde"   align="right"><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></td>
                </tr>
                
            </logic:iterate>
        </table>
    </body>
</html>
