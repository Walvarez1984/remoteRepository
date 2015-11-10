<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../../include/top.jsp"/>

<%------------ INICIO - Fomulario de permisos sobre clasificaciones ------------%>
<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>

<logic:notEmpty name="ec.com.smx.sic.sispe.articulo.normal">
	<bean:define id="codigoTipoArtNormal" name="ec.com.smx.sic.sispe.articulo.normal"/>
	<bean:define id="codigoTipoArtPavo" name="ec.com.smx.sic.sispe.articulo.pavo"/>
	<bean:define id="codigoTipoArtOtroPeso" name="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/>
	
	<bean:define id="descripcionTipoArtNormal" name="ec.com.smx.sic.sispe.articulo.normal.descripcion"/>
	<bean:define id="descripcionTipoArtPavo" name="ec.com.smx.sic.sispe.articulo.pavo.descripcion"/>
	<bean:define id="descripcionTipoArtOtroPeso" name="ec.com.smx.sic.sispe.articulo.otroPesoVariable.descripcion"/>
</logic:notEmpty>

<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" class="textoNegro11">
    <html:form action="actualizarArticulo" method="post">
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <html:hidden property="ayuda" value=""/>
                    <tr>
                        <td  width="3%" align="center"><img src="./images/articulos.gif" border="0"></td>
                        <td height="35" valign="middle">Actualizar Datos del Art&iacute;culo</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('actualizarArticulo');">Guardar</html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <div id="botonA">	
                                            <html:link styleClass="cancelarA" action="actualizarArticulo.do?accionCancelada=ok">Cancelar</html:link>
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
        <tr><td height="5px"></td></tr>
        <tr>
            <td>
                <table cellpadding="2" cellspacing="0" width="98%" class="tabla_informacion" align="center">
                    <tr><td class="fila_titulo" colspan="2">Datos del Art&iacute;culo</td></tr>
                    <tr>
                        <td align="right" class="textoAzul11" width="20%">C&oacute;digo:</td>
                        <td align="left"><bean:write name="ec.com.smx.sic.sispe.articuloSeleccionado" property="id.codigoArticulo"/></td>
                    </tr>
                    <tr>
                        <td align="right" class="textoAzul11">Clasificaci&oacute;n:</td>
                        <td align="left"><bean:write name="ec.com.smx.sic.sispe.articuloSeleccionado" property="codigoClasificacion"/></td>
                    </tr>
                    <tr>
                        <td align="right" class="textoAzul11">Descripci&oacute;n:</td>
                        <td align="left"><smx:text property="descripcionArticulo" size="50" maxlength="100" styleClass="textObligatorio" styleError="campoError"/></td>
                    </tr>
                    <tr>
                        <td align="right" class="textoAzul11">Precio:</td>
                        <td align="left"><smx:text property="precioArticulo" size="12" maxlength="8" styleClass="textObligatorio" styleError="campoError"/></td>
                    </tr>
                    <tr>
                        <td align="right" class="textoAzul11">Precio Caja:</td>
                        <td align="left"><smx:text property="precioCajaArticulo" size="12" maxlength="8" styleClass="textObligatorio" styleError="campoError"/></td>
                    </tr>
                    <tr>
                        <td align="right" class="textoAzul11">Peso Aproximado:</td>
                        <td align="left"><smx:text property="pesoAproximado" size="12" maxlength="8" styleClass="textObligatorio" styleError="campoError"/></td>
                    </tr>
                    <tr>
                        <td align="right" class="textoAzul11">Unidad Manejo:</td>
                        <td align="left"><smx:text property="unidadManejo" size="7" maxlength="5" styleClass="textObligatorio" styleError="campoError"/></td>
                    </tr>
                    <tr>
                        <td align="right" class="textoAzul11">Unidad Manejo Caja:</td>
                        <td align="left"><smx:text property="unidadManejoCaja" size="7" maxlength="5" styleClass="textObligatorio" styleError="campoError"/></td>
                    </tr>
                    <tr>
                        <td align="right" class="textoAzul11">Tipo de Art&iacute;culo (para calculos):</td>
                        <td align="left">
                            <smx:select property="tipoArticuloCalculoPrecio" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>
                                <html:option value="${codigoTipoArtNormal}">${codigoTipoArtNormal} - ${descripcionTipoArtNormal}</html:option>
                                <html:option value="${codigoTipoArtPavo}">${codigoTipoArtPavo} - ${descripcionTipoArtPavo}</html:option>
                                <html:option value="${codigoTipoArtOtroPeso}">${codigoTipoArtOtroPeso} - ${descripcionTipoArtOtroPeso}</html:option>
                            </smx:select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" class="textoAzul11">Estado:</td>
                        <td align="left">
                            <smx:select property="estadoArticulo" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>										
                                <html:option value="${estadoActivo}">Activo</html:option>
                                <html:option value="${estadoInactivo}">Inactivo</html:option>
                            </smx:select>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
       
    </html:form>        
</table>
<tiles:insert page="../../include/bottom.jsp" />