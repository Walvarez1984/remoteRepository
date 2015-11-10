<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana llamada desde ingresoKardex.jsp -->
<bean:define id="calendarioDiaLocalDTO" name="ec.com.smx.calendarizacion.calendarioDiaLocalDTO"/>
<bean:define id="colorReservado"><bean:message key="color.colorReservado"/></bean:define>
<bean:define id="colorIngresos"><bean:message key="color.colorIngreso"/></bean:define>
<bean:define id="colorEgresos"><bean:message key="color.colorEgreso"/></bean:define>
<bean:define id="colorAnulado"><bean:message key="color.colorAnulado"/></bean:define>
<bean:define id="colorAjuste"><bean:message key="color.colorAjustes"/></bean:define>

<table width="200px" border="0" cellpadding="0" cellspacing="0">
 	<tr><td height="5px"></td></tr>
    <tr>	
        <td>
            <table width="200px" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
            
                <tr>
                    <td class="fila_titulo" height="29px">
                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                            <tr>
                                <td width="11%" align="right"><img src="./images/configuracion24.gif" border="0"></td>
                                <td width="89%">&nbsp;Configuraci&oacute;n</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td height="6px"></td></tr>
                <tr>
                    <td id="tblCantidades">
                        <table border="0" class="textos" width="98%" align="center" cellspacing="0" cellpadding="0">
                            <!-- IMPORTANTE: Se presenta CPE en lugar de Cn y CA -->
                            <%-- %><tr>
                                <td width="2px"></td>
                                <td class="textoAzul11" width="75%">
                                    Capacidad Normal:                        
                                </td>
                                <td class="textoNegro11" align="right">
                                    <b><bean:write name="calendarioDiaLocalDTO" property="capacidadNormal" formatKey="formatos.enteros"/></b>	
                                </td>	
                            </tr> 
                            <tr><td height="4px"></td></tr>
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11">
                                    Capacidad Adicional:                        
                                </td>
                                <td class="textoNegro11" align="right">
                                    <b><bean:write name="calendarioDiaLocalDTO" property="capacidadAdicional" formatKey="formatos.enteros"/></b>
                                </td>	
                            </tr> --%>
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11" width="75%">
                                    Capacidad Pedidos Especiales:                        
                                </td>
                                <td class="textoNegro11" align="right">
                                   
                                    <c:set var="cn" value="${calendarioDiaLocalDTO.capacidadNormal}"/>
									<c:set var="ca" value="${calendarioDiaLocalDTO.capacidadAdicional}"/>
									<c:set var="sum" value="${cn+ca}" />
																																												
									<b><bean:write name="sum" formatKey="formatos.enteros"/></b>
									
                                </td>	
                            </tr> 
                            <tr><td height="4px"></td></tr>
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11">
                                    Cantidad Reservada:                        
                                </td>
                                <td class="textoNegro11" align="right">
                                    <b><bean:write name="calendarioDiaLocalDTO" property="cantidadReservada" formatKey="formatos.enteros"/></b>
                                </td>	
                            </tr> 
                            <tr><td height="4px"></td></tr>
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11">
                                    Cantidad Acumulada:                        
                                </td>
                                <td class="textoNegro11" align="right">
                                    <b><bean:write name="calendarioDiaLocalDTO" property="cantidadAcumulada" formatKey="formatos.enteros"/></b>
                                </td>	
                            </tr> 
                            <tr><td height="4px"></td></tr>
                            <!-- IMPORTANTE: Se muestra cantidad excedida en lugar de cantidad disponible respectivamente -->
                            <tr>	     
	                           	<c:if test="${calendarioDiaLocalDTO.cantidadExcedida==0}">                      
	                               	<td width="2px"></td>                                	
	                                <td class="textoAzul11">
	                                    Capacidad Disponible:                        
	                                </td>                                                                 		                                 		 
	                                <td class="textoNegro11" align="right">
	                                   	<b><bean:write name="calendarioDiaLocalDTO" property="cantidadDisponible" formatKey="formatos.enteros"/></b>	
	                                </td>	    
	                           	</c:if>  
	                           	<c:if test="${calendarioDiaLocalDTO.cantidadExcedida>0}">
	                           		<td width="2px"><img src = "images/error12.gif" border="0" align="top"/></td>                               	
	                                <td class="textoAzul11" bgcolor="#FF7171">
	                                    Capacidad Disponible:                        
	                                </td>                                                                 		                                 		 
	                                <td class="textoNegro11" align="right" bgcolor="#FF7171">
	                                   	<b>-<bean:write name="calendarioDiaLocalDTO" property="cantidadExcedida" formatKey="formatos.enteros"/></b>	
	                                </td>	                      
	                           	</c:if>                                                    	                               		                            		                            
                            </tr> 
                            <%--<c:if test="${calendarioDiaLocalDTO.cantidadExcedida>0}">
                                <tr><td height="4px"></td></tr>
                                <tr>
                                    <td width="2px"><img src = "images/error12.gif" border="0" align="top"/></td>
                                    <td class="textoBlanco11" bgcolor="#FF7171">
                                        Capacidad Excedida:
                                    </td>
                                    <td class="textoBlanco11" align="right" bgcolor="#FF7171">
                                        <b><bean:write name="calendarioDiaLocalDTO" property="cantidadExcedida" formatKey="formatos.enteros"/></b>	
                                    </td>	
                                </tr> 
                            </c:if>--%>
                            <tr><td colspan="4" class="fila_contenido">&nbsp;</td></tr>
                            
                            <tr>
                                <td class="textoAzul11" colspan="4">
                                	<!-- IMPORTANTE: Se modifica fórmula -->
                                    <%-- %>CD = CN + CA - CR - CS --5--%>
                                    CD = CPE - CR - CA
                                </td>
                            </tr> 
                            
                            <tr><td height="4px"></td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td height="4px"></td></tr>
    <tr>	
        <td>
            <table width="200px" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
                <tr>
                    <td class="fila_titulo" height="29px">
                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                            <tr>
                                <td width="11%" align="right"><img src="./images/totales.gif" border="0"></td>
                                <td width="89%">&nbsp;Totales</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td height="6px"></td></tr>
                <tr>
                    <td>
                        <table border="0" class="textos" width="98%" align="center" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11" width="187">
                                    Ingresos:								
                                </td>
                                <td align="right" class="textoNegro11">
                                    <b><bean:write name="calendarioDiaLocalDTO" property="npTotalIngresos" formatKey="formatos.enteros"/></b>	
                                </td>	
                            </tr> 
                            <tr><td height="6px"></td></tr>
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11">
                                    Egresos:                        
                                </td>
                                <td class="textoNegro11" align="right">
                                    <b><bean:write name="calendarioDiaLocalDTO" property="npTotalEgresos" formatKey="formatos.enteros"/></b>	
                                </td>	
                            </tr> 
                            <tr><td height="6px"></td></tr>
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11">
                                    Total:                        
                                </td>
                                <td class="textoNegro11" align="right">
                                    <b><bean:write name="calendarioDiaLocalDTO" property="npTotalKardex" formatKey="formatos.enteros"/></b>
                                </td>	
                            </tr> 
                            <tr><td height="6px" colspan="3" class="fila_contenido">&nbsp;</td></tr>
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11" colspan="3">
                                    Unidad: Cantidad en Bultos                        
                                </td>
                            </tr> 
                            <tr><td colspan="3" class="fila_contenido">&nbsp;</td></tr>
                            <tr bgcolor="#E0F2FE">
                                <td valign="top"><img src="./images/info_16.gif" border="0"></td>
                                <td class="textoNegro10" colspan="3" bgcolor="#E0F2FE">
                                    Los Ingresos y Egresos de Capacidad de Pedidos Especiales no se toman en cuenta para el Total del K&aacute;rdex
                                </td>
                            </tr> 
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td height="4px"></td></tr>
    <tr>
        <td height="29px">
            <table width="200px" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
                <tr>
                    <td class="fila_titulo" height="29px">
                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                            <tr>
                                <td width="11%" align="right"><img src="./images/datos_informacion24.gif" border="0"></td>
                                <td width="89%">&nbsp;Informaci&oacute;n</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td height="6px"></td></tr>
                <tr>
                    <td>
                        <table border="0" class="textos" width="98%" align="center" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11" width="55px">
                                    <table bgcolor="#${colorIngresos}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td height="13px" width="50px"></td>
                                        </tr>
                                    </table>                        
                                </td>
                                <td class="textoNegro11">
                                    Ingreso
                                </td>	
                            </tr> 
                            <tr><td height="4px"></td></tr>
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11" width="55px">
                                    <table bgcolor="#${colorEgresos}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td height="13px" width="50px"></td>
                                        </tr>
                                    </table>                        
                                </td>
                                <td class="textoNegro11">
                                    Egreso
                                </td>	
                            </tr>
                            <tr><td height="4px"></td></tr> 
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11" width="55px">
                                    <table bgcolor="#${colorReservado}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td height="13px" width="50px"></td>
                                        </tr>
                                    </table>                        
                                </td>
                                <td class="textoNegro11">
                                    Reservado
                                </td>	
                            </tr> 
                            <tr><td height="4px"></td></tr> 
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11" width="55px">
                                    <table bgcolor="#${colorAnulado}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td height="13px" width="50px"></td>
                                        </tr>
                                    </table>                        
                                </td>
                                <td class="textoNegro11">
                                    Anulado
                                </td>	
                            </tr> 
                            <tr><td height="4px"></td></tr> 
                            <tr>
                                <td width="2px"></td>
                                <td class="textoAzul11" width="55px">
                                    <table bgcolor="#${colorAjuste}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td height="13px" width="50px"></td>
                                        </tr>
                                    </table>                        
                                </td>
                                <td class="textoNegro11">
                                    Ajustes
                                </td>	
                            </tr> 
                        </table>
                    </td>
                </tr>
                <tr><td height="4px"></td></tr>
            </table>
        </td>
    </tr>
</table>