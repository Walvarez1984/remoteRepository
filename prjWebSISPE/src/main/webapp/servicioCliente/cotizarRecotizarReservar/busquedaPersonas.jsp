<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<!-- Recibo el valor de variables de paginación desde sesión -->
<logic:notEmpty name="ec.com.smx.sic.sispe.personaDTOCol">
	<bean:define id="start" name="ec.com.smx.sic.sispe.startPag"></bean:define>
	<bean:define id="range" name="ec.com.smx.sic.sispe.rangePag"></bean:define>
	<bean:define id="size" name="ec.com.smx.sic.sispe.sizePag"></bean:define>
</logic:notEmpty>

<table cellpadding="0" cellspacing="0" width="100%" border="0">
	<tr>
		<td width="25%" valign="top">
			<table cellpadding="1" cellspacing="0" width="100%" class="tabla_informacion">
				<tr class="fila_titulo">
					<td width="15%" class="fila_contenido"><img src="images/buscar24.gif" border="0"/></td>
					<td width="85%" class="fila_contenido">Filtros</td>
				</tr>
				<!-- Sección de presentación de campos de ingreso de texto, en donde se actualizan las secciones de listado y paginación -->
				<tr><td height="5px"></td></tr>
				<tr><td width="36%" align="left">CI/PAS:</td><td><input type="text" class="textNormal" name="filtroDocumento" size="20" onkeyup="requestAjaxEnter('${vformAction}.do', ['listaPersonas','paginacion'] , {parameters: 'buscarPersona=ok'});"/></td></tr>
				<tr><td height="10px"></td></tr>
				<tr><td width="36%" align="left">P. Nombre:</td><td><input type="text" class="textNormal" name="filtroNombre1" size="25" onkeyup="requestAjaxEnter('${vformAction}.do', ['listaPersonas','paginacion'], {parameters: 'buscarPersona=ok'});"/></td></tr>
				<tr><td width="36%" align="left">S. Nombre:</td><td><input type="text" class="textNormal" name="filtroNombre2" size="25" onkeyup="requestAjaxEnter('${vformAction}.do', ['listaPersonas','paginacion'], {parameters: 'buscarPersona=ok'});"/></td></tr>
				<tr><td width="36%" align="left">P. Apellido:</td><td><input type="text" class="textNormal" name="filtroApellido1" size="25" onkeyup="requestAjaxEnter('${vformAction}.do', ['listaPersonas','paginacion'], {parameters: 'buscarPersona=ok'});"/></td></tr>
				<tr><td width="36%" align="left">S. Apellido:</td><td><input type="text" class="textNormal" name="filtroApellido2" size="25" onkeyup="requestAjaxEnter('${vformAction}.do', ['listaPersonas','paginacion'], {parameters: 'buscarPersona=ok'});"/></td></tr>
				<tr><td height="10px"></td></tr>
				<tr><td width="36%" align="left">R. Empresa:</td><td><input type="text" class="textNormal" name="filtroRucEmpresa" size="20" onkeyup="requestAjaxEnter('${vformAction}.do', ['listaPersonas','paginacion'], {parameters: 'buscarPersona=ok'});"/></td></tr>
				<tr><td width="36%" align="left">N. Empresa:</td><td><input type="text" class="textNormal" name="filtroNombreEmpresa" size="25" onkeyup="requestAjaxEnter('${vformAction}.do', ['listaPersonas','paginacion'], {parameters: 'buscarPersona=ok'});"/></td></tr>
				<tr><td height="10px"></td></tr>
				<tr>
					<td align="center" colspan="3">
						<!-- Sección de proceso de búsqueda de personas, en el cual se actualizan las secciones de resultados y paginación -->
						<div id="botonD">
							<a href="#" class="buscarD" onclick="requestAjax('${vformAction}.do', ['listaPersonas','paginacion'] , {parameters: 'buscarPersona=ok'});">Buscar</a>
						</div>
					</td>
				</td>
				</tr>
			</table>
		</td>
		<td width="1%"></td>
		<td width="74%" valign="top">
			<table cellpadding="1" cellspacing="0" width="100%">
				<!-- Inicio: Sección Paginación -->
				<tr>
					<td align="right">
						<div id="paginacion">
							<logic:notEmpty name="${vformAction}">
								<smx:paginacion start="${start}" range="${range}" results="${size}" styleClass="textoNegro10" url="${vformAction}.do"  requestAjax="'listaPersonas','paginacion'"/>
							</logic:notEmpty>
							<logic:empty name="${vformAction}">
								<smx:paginacion start="${start}" range="${range}" results="${size}" styleClass="textoNegro10" url="crearPedidoEspecial.do"  requestAjax="'listaPersonas','paginacion'"/>
							</logic:empty>
						</div>
					</td>
				</tr>
				<!-- Fin: Sección Paginación -->
				<tr>
					<td>
						<table cellpadding="1" cellspacing="0" width="100%">
							<tr class="tituloTablas">
								<td width="5%" class="columna_contenido">&nbsp;</td>
								<td width="15%" class="columna_contenido" align="center">Documento</td>
								<td width="25%" class="columna_contenido" align="center">Nombres Completos</td>
								<td width="15%" class="columna_contenido" align="center">Tel&eacute;fono</td>
								<td width="18%" class="columna_contenido" align="center">Ruc Empresa</td>
								<td width="20%" class="columna_contenido" align="center">Nombre Empresa</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<div id="listaPersonas" style="width:100%;height:400px;overflow-y:auto;overflow-x:hidden">
							<logic:notEmpty name="ec.com.smx.sic.sispe.personaDTOCol">
								<table cellpadding="1" cellspacing="0" width="100%" class="tabla_informacion">
									<logic:iterate name="ec.com.smx.sic.sispe.personaDTOCol" id="clienteDTO" indexId="numPersona">
										<bean:define id="residuo" value="${numeroRegistro % 2}"/>
										<logic:equal name="residuo" value="0">
											<bean:define id="clase" value="blanco10"/>
										</logic:equal>
										<logic:notEqual name="residuo" value="0">
											<bean:define id="clase" value="grisClaro10"/>
										</logic:notEqual>
										<tr class="${clase}">
											<td width="5%" class="fila_contenido columna_contenido"><input type="radio" name="radioSeleccionPersona" value="${numPersona}"></td>
											<td width="15%" class="fila_contenido columna_contenido"><bean:write name="clienteDTO" property="personaDTO.numeroDocumento"/></td>
											<td width="25%" class="fila_contenido columna_contenido"><bean:write name="clienteDTO" property="personaDTO.nombreCompleto"/></td>
											<td width="15%" class="fila_contenido columna_contenido"><bean:write name="clienteDTO" property="personaDTO.telefonoDomicilio"/>&nbsp;</td>
											<td width="18%" class="fila_contenido columna_contenido">&nbsp;
												<logic:notEmpty name="clienteDTO" property="empresaDTO">
													<bean:write name="clienteDTO" property="empresaDTO.numeroRuc"/>
												</logic:notEmpty>
											</td>
											<td width="20%" class="fila_contenido columna_contenido columna_contenido_der">&nbsp;
												<logic:notEmpty name="clienteDTO" property="empresaDTO">
													<bean:write name="clienteDTO" property="empresaDTO.nombreComercialEmpresa"/>
												</logic:notEmpty>
											</td>
										</tr>
									</logic:iterate>
								</table>
							</logic:notEmpty>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>