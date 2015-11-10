<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>
<%-- ---------- INICIO - Formulario de Nueva Autorizaci&oacute;n ---------- --%>
<html:form action="nuevaAutorizacion" method="post">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro11">
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <html:hidden property="ayuda" value=""/>
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="images/nueva_autorizacion48.gif" border="0"></img></td>
                        <td height="35" valign="middle">Nueva Autorizaci&oacute;n</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">	
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('registrarAutorizacion')">Guardar</html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="atras" value="ok"/>
                                        <div id="botonA">
                                            <html:link action="nuevaAutorizacion" paramId="volver" paramName="atras" styleClass="cancelarA">
                                                Cancelar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="ok"/>
                                        <div id="botonA">
                                            <html:link action="menuPrincipal" paramId="exit" paramName="exit" styleClass="inicioA" title="ir al men&uacute; principal">Inicio</html:link>
                                        </div>
                                    </td>                                    
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr><td colspan="8" height="8"></td></tr>
        <tr>
            <td valign="top">
                <table border="0" width="98%" cellpadding="0" cellspacing="2" class="tabla_informacion" align="center">	
                    <tr>
                        <td class="textoAzul11" align="right" width="15%"><b>Local:*</b></td>	
                        <td class="combos" align="left" height="20">
                            <logic:empty name="ec.com.smx.sic.sispe.entidadLocal">
                            <smx:select property="local" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>
                                <logic:notEmpty name="sispe.vistaEstablecimientoCiudadLocalDTO">
                                    <logic:iterate name="sispe.vistaEstablecimientoCiudadLocalDTO" id="vistaEstablecimientoCiudadLocalDTO" indexId="indiceCiudad">
                                        <html:option value="ciudad" styleClass="comboDescripcionCiudad">${vistaEstablecimientoCiudadLocalDTO.nombreCiudad}</html:option>
                                        <logic:notEmpty name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales">
                                            <logic:iterate name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales" id="vistaLocalDTO" indexId="indiceLocal">
                                                <html:option value="${vistaLocalDTO.id.codigoLocal}">&nbsp;&nbsp;&nbsp;${vistaLocalDTO.id.codigoLocal}-${vistaLocalDTO.nombreLocal}</html:option>
                                            </logic:iterate>
                                        </logic:notEmpty>
                                    </logic:iterate>
                                </logic:notEmpty>
                            </smx:select>
                            </logic:empty>
                            <logic:notEmpty name="ec.com.smx.sic.sispe.entidadLocal">
                                <bean:write name="sispe.vistaEntidadResponsableDTO" property="codigoLocal"/>&nbsp;-&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAreaTrabajo"/>	
                            </logic:notEmpty>
                        </td>
                    </tr>     		
                    <tr>
                        <td class="textoAzul11" align="right"><b>Tipo de Autorizaci&oacute;n:*</b></td>
                        <td align="left">
                            <smx:select property="tipoAutorizacion" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>
                                <logic:notEmpty name="ec.com.smx.sic.sispe.tipoAutorizacion">	
                                    <html:options collection="ec.com.smx.sic.sispe.tipoAutorizacion" property="id.codigoTipoAutorizacion"labelProperty="descripcionTipoAutorizacion"/>
                                </logic:notEmpty>
                            </smx:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="textoAzul11" align="right" valign="top"><b>Observaci&oacute;n:*</b></td>
                        <td align="left"><smx:textarea name="autorizacionesForm" property="observacionAutorizacion" styleClass="comboObligatorio" styleError="campoError" cols="100" rows="2"/></td>
                    </tr>
                </table>    
            </td>
        </tr>
    </TABLE>
</html:form>
<%-- ---------- FIN - Formulario de Nueva Autorizaci&oacute;n ---------- --%>
<tiles:insert page="../include/bottom.jsp"/>