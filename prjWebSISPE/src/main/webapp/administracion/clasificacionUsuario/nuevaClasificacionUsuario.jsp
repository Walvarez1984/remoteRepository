<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../../include/top.jsp"/>

<%------------ INICIO - Fomulario de permisos sobre clasificaciones ------------%>
<bean:define id="estadoActivo" name="sispe.estado.activo"/>

<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" class="textoNegro11">
    <html:form action="nuevaClasificacionUsuario" method="post">
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <html:hidden property="ayuda" value=""/>
                    <tr>
                        <td  width="3%" align="center"><img src="./images/nuevoPermisoClasificacion.gif" border="0"></td>
                        <td height="35" valign="middle">Nueva Configuraci&oacute;n de Permisos</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('guardarN');">Guardar</html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <div id="botonA">	
                                            <html:link styleClass="cancelarA" action="clasificacionUsuario.do?accionCancelada=ok">Cancelar</html:link>
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
                    <tr><td class="fila_titulo" colspan="2">Datos del Usuario</td></tr>
                    <tr>
                        <td align="right" class="textoAzul11" width="7%">Usuario:</td>
                        <td align="left">
                            <smx:select property="idUsuario" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>
                                <logic:notEmpty	name="ec.com.smx.sic.sispe.permisosClasificaciones.colEntidadResponsableDTO">
                                    <logic:iterate name="ec.com.smx.sic.sispe.permisosClasificaciones.colEntidadResponsableDTO"	id="vistaEntidadResponsableDTO" indexId="indiceUsuario">
                                        <html:option value="${vistaEntidadResponsableDTO.id.idUsuario}">${vistaEntidadResponsableDTO.nombreCompletoUsuario}</html:option>
                                    </logic:iterate>
                                </logic:notEmpty>
                            </smx:select>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr><td height="10px"></td></tr>
        <tr>
            <td>
                <table cellpadding="0" cellspacing="0" width="98%" align="center">
                    <%--Titulo de los datos--%>
                    <tr>
                        <td class="fila_titulo">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center" class="tabla_informacion">
                                <tr>
                                    <td width="25%">&nbsp;<b>Lista de Clasificaciones permitidas</b></td>
                                    <td width="75%" align="right">
                                        <table cellpadding="1" cellspacing="0" align="right" width="100%">
                                            <tr>
                                                <td width="60%">&nbsp;</td>
                                                <td align="left" id="text_clasificacion">
                                                    <label class="textoAzul11">Clasificaci&oacute;n:&nbsp;</label><smx:text property="clasificacion" styleClass="textNormal" styleError="campoError" size="20" maxlength="15" onkeyup="requestAjaxEnter('actualizarClasificacionUsuario.do', ['mensajes','text_clasificacion','listado_general'], {parameters: 'agregarClasificacion=ok', evalScripts:true});"/>
                                                    <%-- esta campo oculto es para el correcto funcionamiento de ajax --%>
                                                    <span style="display:none"><input type="text" name="prueba"></span>
                                                </td>
                                                <td align="right" width="10%">
                                                    <div id="botonD">
                                                        <html:link styleClass="agregarD" href="#" onclick="requestAjax('nuevaClasificacionUsuario.do', ['mensajes','text_clasificacion','listado_general'], {parameters: 'agregarClasificacion=ok', evalScripts:true});">Agregar</html:link>
                                                    </div>
                                                </td>
                                                <td align="right" width="10%">
                                                    <div id="botonD">
                                                        <html:link styleClass="buscarD" href="#" title="buscar clasificaciones" onclick="dialogoWeb('catalogoArticulos.do','WIN_RBUS','dialogHeight:650px;dialogWidth:900px;help:no;scroll:no');requestAjax('nuevaClasificacionUsuario.do', ['mensajes','listado_general'], {parameters: 'agregarClasificaciones=ok', evalScripts:true});">Buscar</html:link>
                                                    </div>
                                                </td>
                                                <td align="right" width="10%">
                                                    <div id="botonD">
                                                        <html:link styleClass="eliminarD" href="#" title="eliminar clasificacion" onclick="requestAjax('nuevaClasificacionUsuario.do', ['mensajes','listado_general'], {parameters: 'eliminarClasificacion=ok'});">Eliminar</html:link>
                                                    </div>
                                                </td>
                                                <td align="right" width="10%">
                                                    <div id="botonD">
                                                        <html:link styleClass="actualizarD" href="#" title="activar/desactivar clasificacion" onclick="requestAjax('nuevaClasificacionUsuario.do', ['mensajes','listado_general'], {parameters: 'activarDesactivarClasificacion=ok'});">Act/Des</html:link>
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
                        <td class="columna_contenido fila_contenido columna_contenido_der" height="390px" valign="top">
                            <div id="listado_general">
                                <table cellpadding="0" cellspacing="0" width="100%">
                                    <logic:notEmpty name="ec.com.smx.sic.sispe.clasificacionesPorUsuario">
                                        <tr><td height="5px"></td></tr>
                                        <tr>
                                            <td>
                                                <table border="0" cellpadding="1" cellspacing="0" width="98%" align="center">
                                                    <tr class="tituloTablas" align="center">
                                                    	<td width="3%" class="columna_contenido" align="center"><input type="checkbox" name="todo" onclick="activarDesactivarTodo(this, clasificacionUsuarioForm.checksSeleccion);"></td>
                                                        <td width="10%" class="columna_contenido" align="center">CODIGO</td>
                                                        <td width="75%" class="columna_contenido" align="center">DESCRIPCION</td>
                                                        <td width="10%" class="columna_contenido" align="center">ESTADO</td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="center">
                                                <div id="detalle_clasificaciones" style="width:98%;height:360px;overflow:auto;border: 1px solid #cccccc">
                                                    <table border="0" cellpadding="1" cellspacing="0" width="100%">
                                                        <c:set var="indiceReal" value="0"/>
                                                        <logic:iterate name="ec.com.smx.sic.sispe.clasificacionesPorUsuario" id="clasificacionUsuarioDTO" indexId="indiceRegistro">
                                                            <bean:define id="residuo" value="${indiceReal % 2}"/>
                                                            <logic:equal name="residuo" value="0">
                                                                <bean:define id="clase" value="blanco10"/>
                                                            </logic:equal>
                                                            <logic:notEqual name="residuo" value="0">
                                                                <bean:define id="clase" value="grisClaro10"/>
                                                            </logic:notEqual>
                                                            <logic:notEmpty name="clasificacionUsuarioDTO" property="estadoClasificacionUsuario">
                                                                <tr class="${clase}">
                                                                    <td align="center" width="3%" class="columna_contenido fila_contenido">
                                                                        <html:multibox property="checksSeleccion" value="${indiceRegistro}"/>
                                                                    </td>
                                                                    <td align="left" width="10%" class="columna_contenido fila_contenido"><bean:write name="clasificacionUsuarioDTO" property="id.codigoClasificacion"/></td>
                                                                    <td align="left" width="75%" class="columna_contenido fila_contenido"><bean:write name="clasificacionUsuarioDTO" property="clasificacion.descripcionClasificacion"/></td>
                                                                    <td align="center" width="10%" class="columna_contenido fila_contenido" id="col_est_${indiceRegistro}">
                                                                        <logic:equal name="clasificacionUsuarioDTO" property="estadoClasificacionUsuario" value="${estadoActivo}">
                                                                            <img src="./images/exito_16.gif" alt="estado activo">
                                                                        </logic:equal>
                                                                        <logic:notEqual name="clasificacionUsuarioDTO" property="estadoClasificacionUsuario" value="${estadoActivo}">
                                                                            <img src="./images/parado.gif" alt="estado inactivo">
                                                                        </logic:notEqual>
                                                                    </td>
                                                                    <%--<td align="center" width="20%" class="columna_contenido fila_contenido columna_contenido_der" id="col_accion_${indiceRegistro}">
                                                                        <logic:equal name="clasificacionUsuarioDTO" property="estadoClasificacionUsuario" value="${estadoActivo}"><bean:define id="accion" value="Desactivar"/></logic:equal>
                                                                        <logic:notEqual name="clasificacionUsuarioDTO" property="estadoClasificacionUsuario" value="${estadoActivo}"><bean:define id="accion" value="Activar"/></logic:notEqual>
                                                                        <html:link href="#" onclick="requestAjax('nuevaClasificacionUsuario.do',['detalle_clasificaciones'],{parameters: 'indiceE=${indiceRegistro}'});">Eliminar</html:link>&nbsp;/&nbsp;<html:link href="#" onclick="requestAjax('nuevaClasificacionUsuario.do',['col_est_${indiceRegistro}','col_accion_${indiceRegistro}'],{parameters: 'indiceAD=${indiceRegistro}'});">${accion}</html:link>
                                                                    </td>--%>
                                                                </tr>
                                                                <c:set var="indiceReal" value="${indiceReal + 1}"/>
                                                            </logic:notEmpty>
                                                        </logic:iterate>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </logic:notEmpty>
                                    <logic:empty name="ec.com.smx.sic.sispe.clasificacionesPorUsuario">
                                        <tr><td height="5px"></td></tr>
                                        <tr>
                                            <td width="3%" align="center"><img src="images/info_16.gif"></td>
                                            <td align="left">Ingrese las clasificaciones que estar&aacute;n permitidas para el usuario</td>
                                        </tr>
                                    </logic:empty>
                                </table>
                                <logic:notEmpty name="clasificacionAgregada">
                                    <script language="JavaScript" type="text/javascript">document.getElementById("detalle_clasificaciones").scrollTop=document.getElementById("detalle_clasificaciones").scrollHeight;</script>
                                </logic:notEmpty>
                            </div>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </html:form>        
</table>
<tiles:insert page="../../include/bottom.jsp" />