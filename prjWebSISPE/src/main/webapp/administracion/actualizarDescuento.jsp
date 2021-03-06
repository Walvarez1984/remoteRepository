<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>
<%-- ---------- INICIO - Formulario de Actualizar Descuento ---------- --%>
<html:form action="actualizarDescuento" method="post">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
        
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <html:hidden property="ayuda" value=""/>
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="images/editarDescuentos48.gif" border="0"></img></td>
                        <td height="35" valign="middle">Actualizar Descuento</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('actualizarDescuento')">
                                                Guardar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="atras" value="ok"/>
                                        <div id="botonA">
                                            <html:link action="actualizarDescuento" paramId="volver" paramName="atras" styleClass="cancelarA">
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
            <td align="center" valign="top">
                <table border="0" width="98%" cellspacing="0" cellpadding="2" class="tabla_informacion" align="center">	
                    <logic:notEmpty name="sispe.estado.activo">
                        <bean:define name="sispe.estado.activo" id="estadoActivo"/>
                        <bean:define name="sispe.estado.inactivo" id="estadoInactivo"/>
                    </logic:notEmpty>
                    <tr>
                        <td class="textoAzul11N" width="16%" align="right">Tipo de Descuento:*</td>
                        <td class="textoNegro11" align="left"><b><bean:write name="ec.com.smx.sic.sispe.TipoDescuentoDTO" property="descripcionTipoDescuento"/></b></td>
                    </tr>
                    <tr>
                        <td class="textoAzul11N" align="right">Estado:*</td>
                        <td align="left">
                            <smx:select property="estadoDescuento" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>
                                <html:option value="${estadoActivo}">Activo</html:option>
                                <html:option value="${estadoInactivo}">Inactivo</html:option>
                            </smx:select>
                        </td>
                    </tr>	
                    <tr>
                        <td class="textoAzul11N" align="right">Motivo de Descuento:*</td>
                        <td align="left">
                            <smx:select property="codigoMotivoDescuento" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>
                                <logic:notEmpty name="ec.com.smx.sic.sispe.motivoDescuento">	
                                    <html:options collection="ec.com.smx.sic.sispe.motivoDescuento" property="id.codigoMotivoDescuento" labelProperty="descripcionMotivoDescuento"/>
                                </logic:notEmpty>	
                            </smx:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="textoAzul11N" align="right">Rango Inicial:*</td>
                        <td align="left"><smx:text property="rangoInicialDescuento" styleClass="textObligatorio" styleError="campoError" size="16" maxlength="13"/></td>
                    </tr>
                    <tr>
                        <td class="textoAzul11N" align="right">Rango Final:*</td>
                        <td align="left"><smx:text property="rangoFinalDescuento" styleClass="textObligatorio" styleError="campoError" size="16" maxlength="13"/></td>
                    </tr>
                    <tr>
                        <td class="textoAzul11N" align="right">Porcentaje:*</td>
                        <td align="left"><smx:text property="porcentajeDescuento" styleClass="textObligatorio" styleError="campoError" size="9" maxlength="6"/><span class="textoAzul11N">%</span></td>
                    </tr>
                </table>
            </td>
        </tr>
    </TABLE>
</html:form>
<%-- ---------- FIN - Formulario de Actualizar Descuento ---------- --%>
<tiles:insert page="../include/bottom.jsp"/>