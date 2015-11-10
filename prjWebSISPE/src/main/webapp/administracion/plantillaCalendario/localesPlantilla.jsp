<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vtformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>

<table width="100%" border="0">
    <tr>
        <td>     
            <table border="0" width="570px" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
                <tr>
                    <td class="fila_titulo" height="29px" colspan="3">
                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                            <tr>
                                <td width="32" align="right"><img src="./images/locales24.gif" border="0"></td>
                                <td width="308">&nbsp;Locales</td>
                                <td width="60" class="textoAzul10" align="right">Ciudad:</td>
                                <td width="126" align="right">
                                    <logic:notEmpty name="ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol">
                                        <smx:select property="ciudades" styleClass="combos" onchange="requestAjax('adminPlantillas.do', ['listado_locales','mensajes'], {parameters: 'buscarLocales=LOC',popWait:true});">
                                            <html:option value="">Seleccione</html:option>
                                            <html:options collection="ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol" labelProperty="nombreCiudad" property="id.codigoCiudad"/>
                                        </smx:select>
                                    </logic:notEmpty>	
                                </td>	
                                <td width="4"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td height="2px"></td></tr>
                <tr class="tituloTablas" align="left">
                    <td class="columna_contenido" width="15%" align="center">TODOS&nbsp;
                        <html:checkbox property="todo" title="seleccionar todos los art&iacute;culos" onclick="activarDesactivarTodo(this,calendarizacionForm.seleccionados)"/>
                    </td>
                    <td class="columna_contenido" align="center" width="15%">C&Oacute;DIGO</td>
                    <td class="columna_contenido" align="center" width="70%">NOMBRE LOCAL</td>
                </tr>
                <logic:notEmpty name="ec.com.smx.sic.sispe.vistaLocalDTOColP">
                    <tr>
                        <td colspan="3">
                            <div id="listado_locales" style="width:100%;height:380px;overflow-y:auto;overflow-x:hidden">
                                <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                    <logic:iterate name="ec.com.smx.sic.sispe.vistaLocalDTOColP" id="vistaLocalDTO" indexId="numLocal">
                                        <bean:define id="numFila" value="${numLocal + 1}"/>
                                        <%--------- control del estilo para el color de las filas --------------%>
                                        <bean:define id="residuo" value="${numLocal % 2}"/>
                                        <c:set var="clase" value="blanco10"/>
                                        <c:set var="colorBack" value="#ffffff"/>
                                        <logic:notEqual name="residuo" value="0">
                                            <c:set var="clase" value="grisClaro10"/>
                                            <c:set var="colorBack" value="#EBEBEB"/>
                                        </logic:notEqual>
                                        <tr>
                                            <td class="${clase} textoNegro10" width="15%" align="center">
                                                <html:multibox property="seleccionados" value="${vistaLocalDTO.id.codigoLocal}"></html:multibox>
                                            </td>
                                            <td class="${clase} textoNegro10" align="center" width="15%"><bean:write name="vistaLocalDTO" property="id.codigoLocal"/></td>
                                            <td class="${clase} textoNegro10" align="left" width="70%">&nbsp;<bean:write name="vistaLocalDTO" property="nombreLocal"/></td>
                                        </tr>
                                    </logic:iterate>
                                </table>
                            </div>		
                        </td>
                    </tr>	   	   
                </logic:notEmpty>       	
            </table>
        </td>
    </tr>
</table>           
