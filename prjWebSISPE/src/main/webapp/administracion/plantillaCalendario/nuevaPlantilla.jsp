<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<fmt:setLocale value="en_US"/>
<table width="100%" border="0">
    <tr>
        <td>     
            <table border="0" width="400px" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
                <%-- informacion de la plantilla--%>               
                <tr>
                    <td class="fila_titulo" height="29px">
                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                            <tr>
                                <td width="50%"></td>
                                <td align="right" width="49%">
                                    <div id="botonD">	
                                        <html:link styleClass="agregarD" href="#" onclick="requestAjax('adminPlantillas.do', ['plantillas','mensajes'], {parameters: 'botonNuevaPlantilla=ok',popWait:false});">Agregar</html:link>
                                    </div>	
                                </td>	
                                <td width="2px"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td height="2px"></td></tr>
                <tr height="72px">
                    <td>
                        <div id="listadoPlantilla">
                            <table border="0" class="textos" width="95%" align="center" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td width="100%">
                                        <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion" height="19px">
                                            <tr>
                                                <td width="10%" align="center" class="tituloTablas columna_contenido"><B></B></td>
                                                <td width="60%" align="center" class="tituloTablas columna_contenido"><B>Nombre</B></td>																	
                                                <td width="30%" align="center" class="tituloTablas columna_contenido"><B></B></td>
                                            </tr>	
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">
                                        <div style="width:100%;height:150px;overflow-y:auto;overflow-x:hidden">
                                            <logic:notEmpty name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol">
                                                <table border="0" cellspacing="1" cellpadding="1" align="left" width="100%" class="tabla_informacion">
                                                    <logic:iterate name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol" id="calendarioPlantillaLocalDTO" indexId="indicePlantilla">
                                                        <bean:define id="numFila" value="${indicePlantilla % 2}"/>
                                                        <logic:equal name="numFila" value="0">	
                                                            <bean:define id="color" value="grisClaro"/>
                                                        </logic:equal>
                                                        <logic:equal name="numFila" value="1">	
                                                            <bean:define id="color" value="blanco"/>
                                                        </logic:equal>
                                                        <logic:equal name="calendarioPlantillaLocalDTO" property="estadoPlantillaLocal" value="${porDefecto}">
                                                            <bean:define id="color" value="verdeClaro10"/>
                                                        </logic:equal>
                                                        <tr class="${color}">
                                                            <td width="10%" align="center">
                                                                <bean:define id="colorPlantilla" name="calendarioPlantillaLocalDTO" property="codigoColorPrincipal"/>
                                                                <table bgcolor="${colorPlantilla}" border="1"><tr><td height="3px"></td></tr></table>
                                                            </td>
                                                            <td width="60%">
                                                                <html:link title="Ver" href="#" onclick="requestAjax('adminPlantillas.do', ['plantillas','mensajes'], {parameters: 'verPlantillaLink=${indicePlantilla}',popWait:true});">
                                                                    <bean:write name="calendarioPlantillaLocalDTO" property="nombrePlantilla"/>
                                                                </html:link>&nbsp;&nbsp;&nbsp;
                                                            </td>
                                                            <td width="15%" align="center">
                                                                <html:link title="Editar" href="#" onclick="requestAjax('adminPlantillas.do', ['plantillas','mensajes'], {parameters: 'modificarPlantillaLink=${indicePlantilla}',popWait:true});">
                                                                    <img src = "images/editar16.gif" border="0" align="middle"/>
                                                                </html:link>&nbsp;&nbsp;&nbsp;
                                                            </td>
                                                            <td width="15%" align="center">	
                                                                <html:link title="Eliminar" href="#" onclick="requestAjax('adminPlantillas.do', ['confirmar','plantillas','mensajes'], {parameters: 'eliminarPlantillaLink=${indicePlantilla}',popWait:true});mostrarModal();">
                                                                    <html:img src = "images/eliminar.gif" border="0"/>
                                                                </html:link>&nbsp;&nbsp;&nbsp;
                                                            </td>	
                                                        </tr>	
                                                    </logic:iterate>			
                                                </table>
                                            </logic:notEmpty>                      
                                        </div> 
                                    </td>
                                </tr>
                            </table>	
                        </div>	
                    </td>
                </tr>	
                <tr><td height="4px"></td></tr>	
            </table>
        </td>
    </tr>
</table>	
<table width="100%" border="0">
    <tr>
        <td>
            <div id="plantillas">
                <!-- ventana llamada desde plantillas.jsp -->
                
                <bean:define id="estDefecto" name="ec.com.smx.calendarizacion.porDefecto"/>
                <bean:define id="estAnuladoPlantilla" name="ec.com.smx.calendarizacion.estadoAnuladoPlantilla"/>
                
                <logic:notEmpty name="ec.com.smx.calendarizacion.detallePlantilla">
                    <bean:define id="opPlantilla" name="ec.com.smx.calendarizacion.detallePlantilla"/>
                    <table width="400px" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
                        <%-- informacion de la plantilla--%>
                        <tr>
                            <td class="fila_titulo" colspan="4" height="29px">
                                <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                    <tr>
                                        <td width="50%" class="textoNegro12">&nbsp;
                                            Detalle Plantilla
                                        </td>
                                        <td align="right" width="49%" valign="bottom">
                                            <c:if test="${opPlantilla == 'ver'}">
                                                <logic:empty name="ec.com.smx.calendarizacion.eliminarPlantilla">
                                                    <div id="botonD"><html:link href="#" styleClass="aceptarD" onclick="requestAjax('adminPlantillas.do',['mensajes','plantillas','listado_locales'],{parameters: 'aplicarPlantilla=ok'})">Aplicar</html:link></div>				
                                                </logic:empty> 
                                            </c:if>
                                            <c:if test="${opPlantilla == 'nuevo'}">
                                                <div id="botonD"><html:link href="#" styleClass="guardarD" onclick="requestAjax('adminPlantillas.do',['mensajes','plantillas','listadoPlantilla','seccionNuevaPlan','seccionLocalesPlan'],{parameters: 'guardarPlantilla=ok&adminPlantillas=ok',popWait:true})">Guardar</html:link></div>				
                                            </c:if>
                                        </td>	
                                        <td width="2px"></td>		
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <c:if test="${opPlantilla == 'ver'}">
                            <bean:define id="calendarioPlantillaLocalDTO" name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO"/>	
                            <tr>
                                <td align="left" colspan="2">
                                    <table border="0" cellspacing="0" align="left" width="100%">
                                        <tr>
                                            <td class="textoAzul11" width="40%" valign="top">
                                                &nbsp;Nombre:                        
                                            </td>
                                            <td class="textoNegro11">
                                                <bean:write name="calendarioPlantillaLocalDTO" property="nombrePlantilla"/>	
                                            </td>	
                                        </tr> 
                                        <tr>
                                            <td class="textoAzul11">
                                                &nbsp;Color:
                                            </td>
                                            <td class="textoNegro11">
                                                <bean:define id="colorPlantilla" name="calendarioPlantillaLocalDTO" property="codigoColorPrincipal"/>
                                                <bean:define id="nombreColorPrincipal" name="ec.com.smx.calendarizacion.calendarioColorDTO" property="nombreColorPrincipal"/>
                                                <table bgcolor="${colorPlantilla}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <td class="paleta" title="${nombreColorPrincipal}">
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>	
                                        </tr>
                                        <tr>
                                            <td class="textoNegro11" colspan="2"><b>
                                                    &nbsp;Configuraci&oacute;n:
                                            </b></td>
                                        </tr>
                                        <tr>	
                                            <td colspan="2">
                                                <table border="0" class="textos" width="100%" align="center" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td width="100%">
                                                            <table border="0" width="98%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion">
                                                                <tr>
                                                                	<!-- IMPORTANTE: Se muestra título CPE en lugar de CA y CA -->
                                                                    <%-- %><td width="34%" align="center" class="tituloTablas columna_contenido"><B>D&iacute;a</B></td>
                                                                    <td width="33%" align="center" class="tituloTablas columna_contenido"><B>CN</B></td>																	
                                                                    <td width="33%" align="center" class="tituloTablas columna_contenido"><B>CA</B></td>--%>
                                                                    
                                                                    <td width="52%" align="center" class="tituloTablas columna_contenido"><B>D&iacute;a</B></td>
                                                                    <td width="48%" align="center" class="tituloTablas columna_contenido"><B>CPE</B></td>
                                                                    
                                                                </tr>	
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td  width="100%" align="center">		
                                                            <div id="div_listado" style="width:98%;height:185px;overflow:auto">						
                                                                <table class="tabla_informacion" cellpadding="1" cellspacing="1" width="100%" align="center">
                                                                    <logic:notEmpty name="calendarioPlantillaLocalDTO" property="calendarioDetallesPlantillaLocal">
                                                                        <bean:define id="calendarioDetallePlantillaLocalDTOCol" name="calendarioPlantillaLocalDTO" property="calendarioDetallesPlantillaLocal"/>	
                                                                        <logic:iterate name="calendarioDetallePlantillaLocalDTOCol" id="calendarioDetallePlantillaLocalDTO" indexId="numeroDetalle">
                                                                            <bean:define id="numFila" value="${numeroDetalle % 2}"/>
                                                                            <logic:equal name="numFila" value="0">	
                                                                                <bean:define id="color" value="grisClaro"/>
                                                                            </logic:equal>
                                                                            <logic:equal name="numFila" value="1">	
                                                                                <bean:define id="color" value="blanco"/>
                                                                            </logic:equal>
                                                                            <logic:notEqual name="calendarioDetallePlantillaLocalDTO" property="npEstadoDetalle" value="${estAnuladoPlantilla}">
                                                                                <bean:define id="opcionesDiasOBJ" name="calendarizacionForm" property="dias[${calendarioDetallePlantillaLocalDTO.numeroDia}]"/>
                                                                                <tr class="${color}">
                                                                                    <td width="52%" class="textoNegro9" align="left">&nbsp;
                                                                                        <bean:write name="opcionesDiasOBJ" property="nombreDia"/>
                                                                                    </td>	
                                                                                    <!-- IMPORTANTE: Se muestra CPE en lugar de CN y CA -->
                                                                                    <%-- %><td width="33%" align="right" class="textoNegro10">&nbsp;<bean:write name="calendarioDetallePlantillaLocalDTO" property="capacidadNormal" formatKey="formatos.enteros"/>&nbsp;</td>																																																
                                                                                    <td width="33%" align="right" class="textoNegro10">&nbsp;<bean:write name="calendarioDetallePlantillaLocalDTO" property="capacidadAdicional" formatKey="formatos.enteros"/>&nbsp;</td>--%>
                                                                                    
                                                                                    <td width="48%" align="left" class="textoNegro10">
                                                                                    	
                                                                                    	<c:set var="cn" value="${calendarioDetallePlantillaLocalDTO.capacidadNormal}"/>
																						<c:set var="ca" value="${calendarioDetallePlantillaLocalDTO.capacidadAdicional}"/>
																						<c:set var="sum" value="${cn+ca}" />
																						
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${sum}"/>																					
																						
                                                                                    </td>
                                                                                    
                                                                                </tr>
                                                                            </logic:notEqual>
                                                                        </logic:iterate>
                                                                    </logic:notEmpty>
                                                                </table>
                                                            </div>		
                                                        </td>
                                                    </tr>
                                                </table>	    	                                    	
                                            </td>	
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </c:if>	
                        <c:if test="${opPlantilla == 'nuevo'}">
                            <tr height="168px">
                                <td align="left" colspan="2">
                                    <table border="0" cellspacing="3" align="left" width="100%">
                                        <tr>
                                            <td class="textoAzul11" valign="top">
                                                Nombre:							
                                            </td>
                                            <td class="textoNegro11">
                                                <smx:text property="nombrePlantilla" size="24" styleClass="textObligatorio" styleError="campoError"/>
                                            </td>
                                            <td width="150" align="left" class="textoNegro11" id="colorSeleccionado">
                                                <bean:define id="calendarioColorDTO" name="ec.com.smx.calendarizacion.calendarioColorDTO"/>
                                                <bean:define id="colorPlantilla" name="calendarioColorDTO" property="id.codigoColorPrincipal"/>
                                                <bean:define id="nombreColorPrincipal" name="calendarioColorDTO" property="nombreColorPrincipal"/>
                                                <table bgcolor="${colorPlantilla}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <td>
                                                            <html:link styleClass="paleta" title="${nombreColorPrincipal}" href="#" onclick="requestAjax('adminPlantillas.do', ['colores','mensajes'], {parameters: 'seleccionarColor=OK'});mostrarModal();">
                                                            </html:link>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>	
                                        </tr>
                                        <logic:empty name="ec.com.smx.calendarizacion.verDetalles">
                                            <tr>
                                                <td class="textoNegro11" colspan="2"><b>
                                                        Configuraci&oacute;n:
                                                </b></td>
                                            </tr>
                                            <tr>	
                                                <td colspan="3">
                                                    <table border="0" class="textos" width="97%" align="center" cellspacing="0" cellpadding="0">
                                                        <tr>
                                                            <td width="100%">
                                                                <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion" height="18px">
                                                                    <tr>
                                                                    	<!-- IMPORTANTE: Se muestra CPE en lugar de CN y CA -->
                                                                        <%-- %><td width="38%" align="center" class="tituloTablas columna_contenido"><B>D&iacute;a</B></td>
                                                                        <td width="26%" align="center" class="tituloTablas columna_contenido"><B>CN</B></td>																	
                                                                        <td width="26%" align="center" class="tituloTablas columna_contenido"><B>CA</B></td>
                                                                        <td width="10%" align="right" class="tituloTablas columna_contenido" colspan="2"></td>--%>
                                                                        
                                                                        <td width="51%" align="center" class="tituloTablas columna_contenido"><B>D&iacute;a</B></td>
                                                                        <td width="39%" align="center" class="tituloTablas columna_contenido"><B>CPE</B></td>
                                                                        <td width="10%" align="right" class="tituloTablas columna_contenido" colspan="2"></td>
                                                                    </tr>	
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td  width="100%" id="listado">
                                                                <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                                                    <tr>
                                                                        <td width="51%" class="textoNegro9" align="center" height="25px">
                                                                            <html:select property="diaSeleccionado" styleClass="textObligatorio">
                                                                                <html:option value=""></html:option>
                                                                                <html:options collection="ec.com.smx.calendarizacion.opcionesDiasCol" labelProperty="nombreDia" property="seleccion"/>
                                                                            </html:select>
                                                                        </td>
                                                                        <!-- IMPORTANTE: Se obtiene el valor de CN solamente -->
                                                                        <%-- %><td width="26%" align="center" class="textoNegro9 columna_contenido">
                                                                            <smx:text property="cn" size="5" styleClass="textObligatorio" styleError="campoError"/>
                                                                        </td>																																																
                                                                        <td width="26%" align="center" class="textoNegro9 columna_contenido">
                                                                            <smx:text property="ca" size="5" styleClass="textObligatorio" styleError="campoError" onkeypress="requestAjaxEnter('adminPlantillas.do.do', ['listado','mensajes','confirmar'], {parameters: 'agregarConfiguracion=OK',popWait:true});"/>
                                                                        </td>--%>
                                                                        <td width="39%" align="center" class="textoNegro9 columna_contenido">
                                                                            <smx:text property="cn" size="15" styleClass="textObligatorio" styleError="campoError"/>
                                                                        </td>	                                                                      
                                                                        <td width="10%" align="center" colspan="2" class="columna_contenido">
                                                                            <html:link title="Agregar Configuraci&oacute;n" href="#" onclick="requestAjax('adminPlantillas.do', ['listado','mensajes','confirmar'], {parameters: 'agregarConfiguracion=OK',popWait:false});"><img src="./images/exito_16.gif" border="0"></html:link>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                                <div id="divlistado" style="width:100%;height:155px;overflow:auto">						
                                                                    <table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%">
                                                                        <logic:notEmpty name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO">
                                                                            <bean:define id="calendarioPlantillaLocalDTO" name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO"/>	
                                                                            <bean:define id="calendarioDetallePlantillaLocalDTOCol" name="calendarioPlantillaLocalDTO" property="calendarioDetallesPlantillaLocal"/>	
                                                                            <bean:define id="numeroFila" value="${0}"/>
                                                                            <logic:iterate name="calendarioDetallePlantillaLocalDTOCol" id="calendarioDetallePlantillaLocalDTO" indexId="numeroDetalle">
                                                                                <bean:define id="numFila" value="${numeroFila % 2}"/>
                                                                                <logic:equal name="numFila" value="0">	
                                                                                    <bean:define id="color" value="grisClaro"/>
                                                                                </logic:equal>
                                                                                <logic:equal name="numFila" value="1">	
                                                                                    <bean:define id="color" value="blanco"/>
                                                                                </logic:equal>
                                                                                <logic:notEqual name="calendarioDetallePlantillaLocalDTO" property="npEstadoDetalle" value="${estAnuladoPlantilla}">
                                                                                    <bean:define id="opcionesDiasOBJ" name="calendarizacionForm" property="dias[${calendarioDetallePlantillaLocalDTO.numeroDia}]"/>
                                                                                    <tr class="${color}">
                                                                                        <td width="39%" class="textoNegro9" align="left">&nbsp;
                                                                                            <bean:write name="opcionesDiasOBJ" property="nombreDia"/>&nbsp;
                                                                                        </td>
                                                                                        <!-- IMPORTANTE: Se muestra CPE en lugar de CN y CA -->
                                                                                        <%-- %><td width="26%" align="right" class="textoNegro10">&nbsp;<bean:write name="calendarioDetallePlantillaLocalDTO" property="capacidadNormal" formatKey="formatos.enteros"/>&nbsp;</td>																																																
                                                                                        <td width="26%" align="right" class="textoNegro9">&nbsp;<bean:write name="calendarioDetallePlantillaLocalDTO" property="capacidadAdicional" formatKey="formatos.enteros"/>&nbsp;</td>--%>
                                                                                        
                                                                                        <td width="26%" align="right" class="textoNegro9">
                                                                                        
                                                                                        	<c:set var="cn" value="${calendarioDetallePlantillaLocalDTO.capacidadNormal}"/>
																							<c:set var="ca" value="${calendarioDetallePlantillaLocalDTO.capacidadAdicional}"/>
																							<c:set var="sum" value="${cn+ca}" />
																							
																							<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${sum}"/>																					
																							
                                                                                        </td>
                                                                                        
                                                                                        <td width="9%" align="center">		
                                                                                            <html:link title="Eliminar" href="#" onclick="requestAjax('adminPlantillas.do', ['confirmar','plantillas','mensajes'], {parameters: 'eliminarConfiguracionLink=${numeroDetalle}',popWait:true});">
                                                                                                <html:img src = "./images/eliminar.gif" border="0"/>
                                                                                            </html:link>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <bean:define id="numeroFila" value="${numeroFila + 1}"/>
                                                                                </logic:notEqual>	
                                                                            </logic:iterate>
                                                                        </logic:notEmpty>
                                                                    </table>
                                                                </div>		
                                                            </td>
                                                        </tr>
                                                    </table>	    	                                    	
                                                </td>	
                                            </tr>
                                        </logic:empty>	
                                    </table>
                                </td>
                            </tr>
                        </c:if>	               
                    </table>
                </logic:notEmpty>
            </div>
        </td>
    </tr>
</table>  