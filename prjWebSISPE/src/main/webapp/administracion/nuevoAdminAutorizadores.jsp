<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp" />
<%-- ---------- INICIO - Formulario de Actualizar Descuento ---------- --%>
<html:form action="nuevoAdminAutorizadores" method="post">
    <bean:define id="estadoActivo" name="sispe.estado.activo"/>
    <bean:define name="sispe.estado.inactivo" id="estadoInactivo" />
    <bean:define name="sispe.vistaEntidadResponsableDTO" id="vistaEntidadResponsableDTO"/>
    
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0"
                       align="center" class="titulosAccion">
                    <tr>
                        <td width="3%" align="center"><img src="images/nuevoComprador.gif" border="0"></td>
                        <td height="35" valign="middle">Nueva Configuraci&oacute;n del Autorizador</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <html:hidden property="ayuda" value=""/> 
                                        <logic:equal name="ec.com.smx.sic.sispe.vacioAutorizadores" value="${estadoActivo}">
                                            <div id="botonA">
                                                <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('guardarAdminAutorizadores')">
                                                    Guardar
                                                </html:link>
                                            </div>
                                        </logic:equal>
                                    </td>
                                    <td>
                                        <bean:define id="cancel" value="cancel" />
                                        <div id="botonA">
                                            <html:link action="administracionAutorizadores" styleClass="cancelarA">Cancelar</html:link>
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
        <tr>
            <td colspan="8" height="8"></td>
        </tr>        
        <tr>
            <td align="center" valign="top">
                <table border="0" width="98%" cellspacing="0" cellpadding="1" class="tabla_informacion" align="center">
                    <tr>
                        <td class="textoAzul11N" align="right" width="20%">Nombre del Usuario:</td>
                        <td align="left" class="textoNegro11">
                            <bean:write name="vistaEntidadResponsableDTO" property="nombreCompletoUsuario"/>
                            <html:hidden property="nombreUsuario" value="${vistaEntidadResponsableDTO.id.idUsuario}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="textoAzul11N" align="right" width="10%">Local:</td>
                        <td align="left" class="textoNegro11">
                            <%--<logic:equal name="ec.com.smx.sic.sispe.combo" value="${estadoActivo}">
                                <smx:select property="local" styleClass="comboObligatorio" styleError="campoError">
                                    <html:option value="">Seleccione</html:option>
                                    <logic:notEmpty name="sispe.vistaEstablecimientoCiudadLocalDTO">
                                        <logic:iterate name="sispe.vistaEstablecimientoCiudadLocalDTO" id="vistaEstablecimientoCiudadLocalDTO" indexId="indiceCiudad">
                                            <html:option value="ciudad" styleClass="combos">${vistaEstablecimientoCiudadLocalDTO.nombreCiudad}</html:option>
                                            <logic:notEmpty name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales">
                                                <logic:iterate name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales" id="vistaLocalDTO" indexId="indiceLocal">
                                                    <html:option value="${vistaLocalDTO.id.codigoLocal}">&nbsp;&nbsp;&nbsp;${vistaLocalDTO.id.codigoLocal}-${vistaLocalDTO.nombreLocal}</html:option>
                                                </logic:iterate>
                                            </logic:notEmpty>
                                        </logic:iterate>
                                    </logic:notEmpty>
                                </smx:select>
                            </logic:equal>
                            <logic:notEqual name="ec.com.smx.sic.sispe.combo" value="${estadoActivo}">--%>
                            <bean:write name="ec.com.smx.sic.sispe.localActual"/>
                            <html:hidden property="local" value="${vistaEntidadResponsableDTO.codigoLocal}"/>
                            <%--</logic:notEqual>--%>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>    
        <tr>
            <td colspan="8" height="8"></td>
        </tr>
        <tr>
            <td>
                <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="98%" align="center">                                    
                    <tr>
                        <td class="fila_titulo" colspan="3">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center" class="textoNegro11">
                                <tr>
                                    <td height="23" width="60%" class="textoNegro11">&nbsp;<b>Usuarios Autorizados</b></td>
                                    <td>
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                            <tr>
                                                <td align="left" class="textoAzul11N">Usuario:&nbsp;</td>
                                                <td>
                                                    <smx:select property="idUsuarioAutorizado" styleClass="combos" styleError="campoError">
                                                        <html:option value="sinValor">Seleccione</html:option>
                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.nombresUsuarioAutorizados">
                                                            <logic:iterate id="nombreUsuarioCol" name="ec.com.smx.sic.sispe.nombresUsuarioAutorizados">
                                                                <html:option value="${nombreUsuarioCol.id.idUsuario}">
                                                                    <bean:write name="nombreUsuarioCol" property="nombreCompletoUsuario" />
                                                                </html:option>
                                                            </logic:iterate>
                                                        </logic:notEmpty>
                                                    </smx:select>
                                                </td>
                                                <td>
                                                    <div id="botonD"><html:link styleClass="agregarD" href="#" onclick="realizarEnvio('agregarUsuario')">Agregar</html:link></div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr> 
                    <tr>
                        <td colspan="8" height="8"></td>
                    </tr>
                    <tr>
                        <td>
                            <table cellpadding="0" cellspacing="0" border="0" width="98%" align="center">
                                <tr>
                                    <td>
                                        <table border="0" cellpadding="1" cellspacing="0" width="100%" align="center" class="tabla_informacion">
                                            <tr class="tituloTablas">
                                                <td align="center" class="columna_contenido" width="5%">No</td>                                    
                                                <td align="center" class="columna_contenido" width="50%">NOMBRE COMPLETO</td>
                                                <td align="center" class="columna_contenido" width="5%">ESTADO</td>
                                                <td align="center" class="columna_contenido" width="5%">ACCION</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div id="div_listado" style="width:100%;height:300px;overflow:auto">
                                            <table border="0" cellpadding="1" cellspacing="0" width="100%" class="tabla_informacion">
                                                <logic:notEmpty name="ec.com.smx.sic.sispe.usuariosAutorizados">	
                                                    <logic:iterate name="ec.com.smx.sic.sispe.usuariosAutorizados" id="usuariosAut" indexId="indiceRegistro">
                                                        <bean:define id="residuo" value="${indiceRegistro % 2}" />                                            
                                                        <logic:equal name="residuo" value="0">
                                                            <bean:define id="colorBack" value="blanco10" />
                                                        </logic:equal>
                                                        <logic:notEqual name="residuo" value="0">
                                                            <bean:define id="colorBack" value="grisClaro10" />
                                                        </logic:notEqual>
                                                        <logic:notEmpty name="usuariosAut">
                                                            <tr class="${colorBack}" >
                                                                <td width="5%" align="center" class="columna_contenido fila_contenido">
                                                                    ${indiceRegistro + 1}
                                                                </td>
                                                                <td width="50%" class="columna_contenido fila_contenido">
                                                                    &nbsp;<bean:write name="usuariosAut" property="usuarioAutorizadoDTO.userCompleteName" />
                                                                </td>
                                                                <td width="5%" class="columna_contenido fila_contenido">
                                                                <table border="0" align="center">
                                                                    <tr>
                                                                        <td align="center">
                                                                            <logic:equal name="usuariosAut" property="estadoAutorizador" value="${estadoActivo}">
                                                                                <img src="./images/exito_16.gif" alt="estado activo">
                                                                            </logic:equal> 
                                                                            <logic:notEqual  name="usuariosAut" property="estadoAutorizador" value="${estadoActivo}">
                                                                                <img src="./images/parado.gif" alt="estado inactivo">
                                                                            </logic:notEqual>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                                <td width="5%" class="columna_contenido fila_contenido">
                                                                    <table border="0" align="center">
                                                                        <tr>
                                                                            <td align="center">
                                                                                <logic:equal name="usuariosAut" property="estadoAutorizador" value="${estadoActivo}">
                                                                                    <html:link href="#" onclick="realizarEnvio('activar${indiceRegistro}')">
                                                                                        Desactivar
                                                                                    </html:link>																	
                                                                                </logic:equal> 
                                                                                <logic:notEqual  name="usuariosAut" property="estadoAutorizador" value="${estadoActivo}">
                                                                                    <html:link href="#" onclick="realizarEnvio('desactivar${indiceRegistro}')">
                                                                                        Activar
                                                                                    </html:link>                                                                    
                                                                                </logic:notEqual>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </logic:notEmpty>
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
    </table>
</html:form>
<tiles:insert page="/include/bottom.jsp" />