<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>
<%-- ---------- INICIO - Formulario de Nuevo tipo descuento ---------- --%>
<html:form action="nuevoTipoDescuento" method="post">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
        <logic:notEmpty name="sispe.estado.activo">	
            <bean:define id="estadoActivo"><bean:write name="sispe.estado.activo"/></bean:define>
        </logic:notEmpty>
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <html:hidden property="ayuda" value=""/>
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="./images/nuevoTipoDescuento.gif" border="0"></td>
                        <td height="35" valign="middle">Nuevo Tipo de Descuento</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">	
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('registrarTipoDescuento')">
                                                Guardar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="atras" value="ok"/>
                                        <div id="botonA">
                                            <html:link action="nuevoTipoDescuento" paramId="volver" paramName="atras" styleClass="cancelarA">
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
                
                <table border="0" width="98%" cellpadding="2" cellspacing="0" class="tabla_informacion">
                    <tr>
                        <td class="textoAzul11N" align="left" width="10%">Descripci&oacute;n:*</td>
                        <td align="left"><smx:text name="tipoDescuentoForm" property="descripcionTipoDescuento" styleClass="textObligatorio" styleError="campoError" maxlength="100" size="60"/></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr><td height="10"></td></tr>
        <tr>
            <td id="seccion_Listado">
                <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
                    <%--Titulo de los datos--%>
                    <tr>
                        <td class="fila_titulo" colspan="3">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center" class="textoNegro11">
                                <tr>
                                    <td><img src="./images/detalle_pedidos24.gif" border="0"></td>
                                    <td height="23" width="63%"><b>&nbsp;Lista de Clasificaciones</b></td>
                                    <td>
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                            <tr>
                                                <td align="left" class="textoAzul11N">
                                                    C&oacute;digo de Clasificaci&oacute;n:&nbsp;
                                                </td>
                                                <td>
                                                    <html:text property="codigoClasificacionDescuento" size="10" styleClass="combos" onkeypress="requestAjaxEnter('nuevoTipoDescuento.do',['mensajes','div_listado'],{parameters: 'botonAgregarClasificacion=ok'});"/>
                                                </td>
                                                <td>&nbsp;</td>
                                                <td>
                                                    <div id="botonD">
                                                        <html:link styleClass="agregarD" href="#" onclick="requestAjax('nuevoTipoDescuento.do',['mensajes','div_listado','seccion_Listado'],{parameters: 'botonAgregarClasificacion=ok'});">Agregar</html:link>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td colspan="3" height="8"></td></tr>
                    <tr>
                        <td>
                            <table border="0" cellspacing="0" width="98%" align="center">
                                <tr class="tituloTablas">
                                    <td align="center" class="columna_contenido" width="10%">C&Oacute;DIGO</td>	
                                    <td align="center" class="columna_contenido" width="60%">DESCRIPCI&Oacute;N</td>
                                    <td align="center" class="columna_contenido" width="10%">ESTADO</td>
                                    <td align="center" class="columna_contenido" width="20%">ACCI&Oacute;N</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="div_listado" style="width:100%;height:365px;overflow:auto">
                                <logic:notEmpty name="ec.com.smx.sic.sispe.tiposDescuentosClasificaciones">
                                    <table border="0" cellspacing="0" width="98%" align="center">  
                                        <logic:iterate name="ec.com.smx.sic.sispe.tiposDescuentosClasificaciones" id="tipoDescuentoClasificacionDTO" indexId="numeroRegistro">	
                                            <%--------- control del estilo para el color de las filas --------------%>
                                            <bean:define id="residuo" value="${numeroRegistro % 2}"/>
                                            <logic:equal name="residuo" value="0">
                                                <bean:define id="colorBack" value="blanco10"/>
                                            </logic:equal>
                                            <logic:notEqual name="residuo" value="0">
                                                <bean:define id="colorBack" value="grisClaro10"/>
                                            </logic:notEqual>
                                            
                                            <tr class="${colorBack}">
                                                <td align="center" class="columna_contenido fila_contenido" width="10%"><bean:write name="tipoDescuentoClasificacionDTO" property="clasificacionDTO.id.codigoClasificacion"/></td>	
                                                <td align="left" class="columna_contenido fila_contenido" width="60%"><bean:write name="tipoDescuentoClasificacionDTO" property="clasificacionDTO.descripcionClasificacion"/></td>
                                                <td align="center" class="columna_contenido fila_contenido" width="10%">
                                                    <logic:equal name="tipoDescuentoClasificacionDTO" property="estadoTipoDescuentoClasificacion" value="${estadoActivo}">
                                                        <img src="./images/exito_16.gif" alt="estado activo">
                                                    </logic:equal> 
                                                    <logic:notEqual name="tipoDescuentoClasificacionDTO" property="estadoTipoDescuentoClasificacion" value="${estadoActivo}">
                                                        <img src="./images/parado.gif" alt="estado inactivo">
                                                    </logic:notEqual>
                                                </td>
                                                <td align="center" class="columna_contenido fila_contenido columna_contenido_der" width="20%">
                                                    <logic:equal name="tipoDescuentoClasificacionDTO" property="estadoTipoDescuentoClasificacion" value="${estadoActivo}">
                                                        <%--<html:link action="nuevoTipoDescuento" paramId="indiceDesactivacionActivacion" paramName="numeroRegistro">
                                                            Desactivar
                                                        </html:link>--%>
                                                        <html:link  href="#" onclick="requestAjax('nuevoTipoDescuento.do',['mensajes','div_listado','seccion_Listado'],{parameters: 'indiceDesactivacionActivacion=${numeroRegistro}'});">Desactivar</html:link>
                                                    </logic:equal>
                                                    <logic:notEqual name="tipoDescuentoClasificacionDTO" property="estadoTipoDescuentoClasificacion" value="${estadoActivo}">
                                                        <%--<html:link action="nuevoTipoDescuento" paramId="indiceDesactivacionActivacion" paramName="numeroRegistro">
                                                            Activar
                                                        </html:link>--%>
                                                        <html:link  href="#" onclick="requestAjax('nuevoTipoDescuento.do',['mensajes','div_listado','seccion_Listado'],{parameters: 'indiceDesactivacionActivacion=${numeroRegistro}'});">Activar</html:link>
                                                    </logic:notEqual>&nbsp;/&nbsp;
                                                     <%--<html:link action="nuevoTipoDescuento" paramId="indiceEliminacion" paramName="numeroRegistro">
                                                        Eliminar
                                                    </html:link>--%>
                                                    <html:link  href="#" onclick="requestAjax('nuevoTipoDescuento.do',['mensajes','div_listado','seccion_Listado'],{parameters: 'indiceEliminacion=${numeroRegistro}'});">Eliminar</html:link>
                                                </td>
                                             </tr>	
                                        </logic:iterate>
                                    </table>
                                </logic:notEmpty>   
                            </div>
                        </td>
                    </tr>
                    <tr><td height="8"></td></tr>
                </table>	
            </td>
        </tr>
    </TABLE>
</html:form>
<%-- ---------- FIN - Formulario de Actualizar tipo descuento ------------%>
<tiles:insert page="../include/bottom.jsp"/>