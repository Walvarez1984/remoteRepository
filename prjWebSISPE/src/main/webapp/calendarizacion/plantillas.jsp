<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana llamada desde calendarizacion.jsp -->
<bean:define id="opFechaMesAno"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaMesAno"/></bean:define>
<bean:define id="opFechaEspecifica"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaEspecifica"/></bean:define>
<bean:define id="porDefecto" name="ec.com.smx.calendarizacion.porDefecto"/>
<div id="accordion1" style="width:100%">
    <div>
        <div class="panelHeader"><table width="100%" cellpadding="0" cellspacing="0"><tr><td width="20%"><img src="./images/plantilla24.gif" border="0"></td><td align="left">Plantillas</tr></table></div>
        <div class="panelContent" style="width:99%">
            <table width="100%" border="0">
                <tr>
                    <td>     
                        <table width="251px" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
                            <%-- informacion de la plantilla--%>
                            <tr>
                                <td class="fila_titulo" height="29px">
                                    <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                        <tr>
                                            <td width="50%"></td>
                                            <td align="right" width="49%">
                                                <div id="botonD">	
                                                    <html:link styleClass="agregarD" href="#" onclick="requestAjax('calendarizacion.do', ['plantillas','mensajes'], {parameters: 'botonNuevaPlantilla=ok',popWait:false});">Agregar</html:link>
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
                                        <logic:notEmpty name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTOCol">
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
                                                        <div style="width:100%;height:70px;overflow-y:auto;overflow-x:hidden">
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
                                                                            <html:link title="Ver" href="#" onclick="requestAjax('calendarizacion.do', ['plantillas','mensajes'], {parameters: 'verPlantillaLink=${indicePlantilla}',popWait:true});">
                                                                                <bean:write name="calendarioPlantillaLocalDTO" property="nombrePlantilla"/>
                                                                            </html:link>&nbsp;&nbsp;&nbsp;
                                                                        </td>
                                                                        <td width="15%" align="center">
                                                                            <html:link title="Editar" href="#" onclick="requestAjax('calendarizacion.do', ['plantillas','mensajes'], {parameters: 'modificarPlantillaLink=${indicePlantilla}',popWait:true});">
                                                                                <img src = "./images/editar16.gif" border="0" align="middle"/>
                                                                            </html:link>&nbsp;&nbsp;&nbsp;
                                                                        </td>
                                                                        <td width="15%" align="center">	
                                                                            <html:link title="Eliminar" href="#" onclick="requestAjax('calendarizacion.do', ['confirmar','plantillas','mensajes'], {parameters: 'eliminarPlantillaLink=${indicePlantilla}',popWait:true});">
                                                                                <html:img src = "./images/eliminar.gif" border="0"/>
                                                                            </html:link>&nbsp;&nbsp;&nbsp;
                                                                        </td>	
                                                                    </tr>	
                                                                </logic:iterate>			
                                                            </table>
                                                        </div> 
                                                    </td>
                                                </tr>
                                            </table>	
                                        </logic:notEmpty>	
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
                            <tiles:insert page="/calendarizacion/detallePlantilla.jsp"/>
                        </div>
                    </td>
                </tr>
            </table>            
        </div>
    </div>
    <div>
        <div class="panelHeader"><table width="100%" cellpadding="0" cellspacing="0"><tr><td width="20%"><img src="./images/buscar24.gif" border="0"></td><td align="left">B&uacute;squeda</tr></table></div>
        <div class="panelContent">
            <table width="258px" border="0">
                <logic:notEmpty name="ec.com.smx.sic.sispe.local">
                    <tr>
                        <td>
                            <table width="100%" border="0" class="tabla_informacion" cellpadding="1" cellspacing="0" bgcolor="white">
                                <%--detalles del local--%>
                                <tr>
                                    <td class="fila_contenido">
                                        <table border="0" cellspacing="0" width="100%">
                                            <tr>
                                                <td height="25" colspan="4">&nbsp;
                                                    <html:link href="#" onclick="requestAjax('calendarizacion.do', ['cambioMes','calendario','mensajes'], {parameters: 'botonBuscarHoy=OK',popWait:true});">
                                                        <html:img src = "images/diaxmes.gif" border="0"/>&nbsp;Hoy
                                                    </html:link>
                                                </td>
                                            </tr>    
                                        </table>
                                    </td>
                                </tr>
                                <tr>	
                                    <td class="fila_contenido">
                                        <table border="0" cellspacing="0" width="100%">
                                            <tr>
                                                <td width="2%">
                                                    <html:radio property="opcionBusqueda" value="${opFechaMesAno}" onclick="calendarizacionForm.buscaAnio.focus();"/>
                                                </td>
                                                <td width="28%" height="25" class="textoAzul11">&nbsp;A&ntilde;o:</td>
                                                <td width="70%" colspan="3"><html:text property="buscaAnio" styleClass="combos" size="12" maxlength="4" onclick="chequear(calendarizacionForm.opcionBusqueda[0]);" onkeypress="requestAjaxEnter('calendarizacion.do', ['cambioMes','calendario','mensajes'], {parameters: 'botonBuscarCalendar=OK',popWait:true});"/></td>
                                            </tr>
                                            <tr>	
                                                <td width="2%"></td>
                                                <td class="textoAzul11">&nbsp;Mes:</td>
                                                <td colspan="3">
                                                    <html:select property="buscaMes" styleClass="combos" onclick="chequear(calendarizacionForm.opcionBusqueda[0]);">
                                                        <html:option value="">Seleccione</html:option>
                                                        <html:options collection="ec.com.smx.calendarizacion.mesesDelAnioDTOCol" labelProperty="nombreMes" property="codigoMes"/>
                                                    </html:select>
                                                </td>
                                            </tr>
                                            <tr><td colspan="3" height="8"></td></tr>
                                        </table>	
                                    </td>	
                                </tr>	
                                <tr>	
                                    <td>	
                                        <table border="0" cellspacing="0" width="100%">
                                            <tr>
                                                <td width="2%">
                                                    <html:radio property="opcionBusqueda" value="${opFechaEspecifica}" onclick="calendarizacionForm.buscaFecha.focus();"/>
                                                </td>
                                                <td width="28%" height="25" class="textoAzul11">&nbsp;Fecha:</td>
                                                <td width="46%" class="textoAzul11">
                                                    <smx:fecha name="calendarizacionForm" property="buscaFecha" styleClass="combos" size="16" maxlength="10" formatKey="formatos.fecha" onclick="chequear(calendarizacionForm.opcionBusqueda[1]);" onkeypress="requestAjaxEnter('calendarizacion.do', ['cambioMes','calendario','mensajes'], {parameters: 'botonBuscarCalendar=OK',popWait:true});"/>
                                                </td>
                                                <td width="24%" align="left" colspan="2">	
                                                    <smx:calendario property="buscaFecha" key="formatos.fecha"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr><td colspan="4" height="8"></td></tr>
                                <tr>
                                    <td align="right" colspan="4">
                                        <html:link href="#" onclick="requestAjax('calendarizacion.do', ['cambioMes','calendario','mensajes'], {parameters: 'botonBuscarCalendar=OK',popWait:true});">
                                            Ir a fecha
                                        </html:link>&nbsp;&nbsp;
                                    </td>
                                </tr>
                                <tr><td colspan="3" height="4"></td></tr>												
                            </table>
                        </td>
                    </tr>
                </logic:notEmpty> 
                <%--Separaci&oacute;n--%>		
            </table>	
        </div>
    </div>
</div>			
<script language="JavaScript" type="text/javascript">acordeon('accordion1',342);</script>