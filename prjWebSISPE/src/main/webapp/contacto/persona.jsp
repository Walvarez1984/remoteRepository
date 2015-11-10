<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>


<bean:define id="accionAux" name="ec.com.smx.sic.sispe.accion"/>

<bean:define id="crearPedidosEspeciales">
	<bean:message key="ec.com.smx.sic.sispe.accion.crearPedidoEspecial" />
</bean:define>

 <c:if test="${accionAux == crearPedidosEspeciales}">
 	<c:set var="formulario" value="crearPedidoEspecialForm" />
 	<c:set var="accion" value="crearPedidoEspecial.do"/>
 	<c:set var="altoDiv" value="460" />
 </c:if>
 
 <c:if test="${accionAux != crearPedidosEspeciales}">
 	<c:set var="formulario" value="cotizarRecotizarReservarForm" />
 	<c:set var="accion" value="crearCotizacion.do"/>
 	<c:set var="altoDiv" value="510" />
 </c:if>
 
 

<%--- variables de session para persona o empresa ---%>
<%--- @author wlopez --------------------------------%>
<logic:notEmpty name="ec.com.smx.sic.sispe.btnCrearPersona">
	<bean:define id="btnCrearPersona"
		name="ec.com.smx.sic.sispe.btnCrearPersona" />
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.btnCrearEmpresa">
	<bean:define id="btnCrearEmpresa"
		name="ec.com.smx.sic.sispe.btnCrearEmpresa" />
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.btnAgregarLocalizacion">
	<bean:define id="btnAgregarLocalizacion"
		name="ec.com.smx.sic.sispe.btnAgregarLocalizacion" />
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.reporte">
	<bean:define id="reporte" name="ec.com.smx.sic.sispe.reporte" />
</logic:notEmpty>
<logic:notEmpty name="system.corp.visualizar.contactos">
	<bean:define id="urlJSF" name="system.corp.visualizar.contactos" />
</logic:notEmpty>

<logic:notEmpty name="ec.com.smx.sic.token.corp">
	<bean:define id="tokenCorp" name="ec.com.smx.sic.token.corp" />
</logic:notEmpty>

<logic:empty name="ec.com.smx.sic.token.corp">
	<bean:define id="tokenCorp" value="" />
</logic:empty>

 <logic:notEmpty name="ec.com.smx.sic.sispe.url.opciones">
	<bean:define id="urlOpciones" name="ec.com.smx.sic.sispe.url.opciones" />
 </logic:notEmpty>

<div id="div_datosPersona">
														
	<table class="textoNegro11" cellpadding="0" cellspacing="0"
		align="left" width="100%">
		<tr>
			<td height="3px"></td>
		</tr>
		<tr>
			<td width="100%">
				<table border="0" cellspacing="0" cellpadding="0" width="100%"
					class="tabla_informacion">					
					<logic:notEmpty name="ec.com.smx.sic.sispe.persona">	
						<logic:empty name="reporte">	
							<tr class="fila_titulo">
								<td width="0px" class="fila_contenido" align="center"><img
									src="./images/persona.gif" border="0"></td>
								<td height="25px" align="left"
									class="fila_contenido textoNegro10" width="100%">
									&nbsp;Datos de persona</td>
								<td class="fila_contenido">
									<table cellspacing="0" cellpadding="1" align="right">
										<tr>
											<td>
												<div id="botonD" style="height:25px">													
													<html:link styleClass="editarD" href="#"
														onclick="requestAjax('${accion}',['mensajes','pregunta','popUpCorporativo'],{parameters:'editarPersona=ok',evalScripts:true})"
														title="Editar persona">Editar</html:link>	
													
													<iframe id="frameOpPer" src="${urlOpciones}" align="middle" name="opcionesPer"
														width="0%" height="0%" frameborder="0" onload="killWait('div_wait');"> 
															<script type="text/javascript">
															 	popWait('div_wait');
															</script>
													</iframe>										
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</logic:empty>
						<tr>
							<td colspan="3">
								<div id="detallePersona" style="overflow-x: hidden;">
									<table border="0" width="100%" cellpadding="0" cellspacing="0">
										<tr>
											<td align="center" height="${altoDiv}" valign="top">
											
												<iframe id="frame" src="jsf/contacto/adminContacto.jsf${tokenCorp}" align="middle" name="Contactos" onload="killWait('div_wait');"
													width="100%" height="100%" frameborder="0">
													<script type="text/javascript">
													 	popWait('div_wait');
													</script>
												</iframe>
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</logic:notEmpty>
					<logic:empty name="ec.com.smx.sic.sispe.persona">
					<logic:notEmpty name="ec.com.smx.sic.sispe.localizacion">
						<logic:notEmpty name="btnAgregarLocalizacion">
							<tr class="fila_titulo">
								<td width="25px" class="fila_contenido" align="center"><img
									src="./images/empresa.gif" border="0"></td>
								<td height="25px" align="left"
									class="fila_contenido textoNegro10">&nbsp;Datos de empresa
								</td>
								<td class="fila_contenido">
									<table cellspacing="0" cellpadding="1" align="right">
										<tr>
											<td>
												<div id="botonD">
													<logic:empty name="reporte">
														<html:link styleClass="editarD" href="#"
															onclick="requestAjax('${accion}', ['mensajes','pregunta'] , {parameters: 'editarEmpresa=ok',evalScripts: true});"
															title="Editar empresa">Editar</html:link>
													</logic:empty>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									<div>
										<table border="0" width="100%" cellpadding="0" cellspacing="0">
											<tr>
												<td align="center" height="380px">Agrege una
													localizaci&oacute;n</td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</logic:notEmpty>
						<logic:empty name="btnAgregarLocalizacion">
							<logic:empty name="reporte">
								<tr class="fila_titulo">
									<td width="0px" class="fila_contenido" align="center"><img
										src="./images/empresa.gif" border="0"></td>
									<td height="25px" align="left"
										class="fila_contenido textoNegro10" width="100%">
										&nbsp;Datos de empresa</td>
									<td class="fila_contenido">
										<table cellspacing="0" cellpadding="1" align="right">
											<tr>
												<td>
													<div id="botonD" style="height:25px">														
															<html:link styleClass="editarD" href="#"
																onclick="requestAjax('${accion}',['mensajes','pregunta','popUpCorporativo'],{parameters:'editarEmpresa=ok',evalScripts:true});"
																title="Editar empresa">Editar</html:link>
																
															<iframe id="frameOpEmp" src="${urlOpciones}" align="middle" name="empresa"
																width="0%" height="0%" frameborder="0" onload="killWait('div_wait');">
																<script type="text/javascript">
																 	popWait('div_wait');
																</script>
															</iframe>													
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								</logic:empty>
							<tr>
								<td colspan="3">
									<div id="divDetalleEmpresa" style="overflow-x: hidden;overflow-y: hidden;">
										
										<script type="text/javascript">
											jQuery().ready(function(){ 
												$j('#divDetalleEmpresa').load('contacto/persona.jsp #divDetalleEmpresa');		
											});
										</script>
										
										<table border="0" width="100%" cellpadding="0" cellspacing="0">
											<tr>
												<td align="center" height="380px" valign="top">
													<!-- componente empresa -->
													<iframe id="frameEmpresa" src="jsf/contacto/adminEmpresa.jsf${tokenCorp}" align="middle" name="Empresa"
														width="100%" height="100%" frameborder="0"
														onload="killWait('div_wait');"> 
														<script type="text/javascript">
														 	popWait('div_wait');
														</script>
													</iframe>
													
												</td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</logic:empty>
						</logic:notEmpty>
					</logic:empty>
					<logic:notEmpty name="btnCrearPersona">
						<tr class="fila_titulo">
							<td width="25px" class="fila_contenido" align="center"><img
								src="./images/persona.gif" border="0"></td>
							<td height="25px" align="left"
								class="fila_contenido textoNegro10">&nbsp;Datos de personas
							</td>
							<td class="fila_contenido">
								<table cellspacing="0" cellpadding="1" align="right">
									<tr>
										<td>
											<div id="botonD">
												<html:link styleClass="nuevoD" href="#"
													onclick="requestAjax('${accion}', ['mensajes','pregunta'] , {parameters: 'registrarPersona=ok',evalScripts: true});"
													title="Registrar persona">Nuevo</html:link>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div id="detallePersona">
									<table border="0" width="100%" cellpadding="0" cellspacing="0">
										<tr>
											<td align="center" height="380px">Persona no registrada
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</logic:notEmpty>
					<logic:notEmpty name="btnCrearEmpresa">
						<tr class="fila_titulo">
							<td width="25px" class="fila_contenido" align="center"><img
								src="./images/empresa.gif" border="0"></td>
							<td height="25px" align="left"
								class="fila_contenido textoNegro10">&nbsp;Datos de empresa
							</td>
							<td class="fila_contenido">
								<table cellspacing="0" cellpadding="1" align="right">
									<tr>
										<td>
											<div id="botonD">
												<html:link styleClass="nuevoD" href="#"
													onclick="requestAjax('${accion}', ['mensajes','pregunta'] , {parameters: 'registrarEmpresa=ok',evalScripts: true});"
													title="Nueva empresa">Nuevo</html:link>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div id="detallePersona">
									<table border="0" width="100%" cellpadding="0" cellspacing="0">
										<tr>
											<td align="center" height="380px">Empresa no registrada
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</logic:notEmpty>
					<logic:empty name="ec.com.smx.sic.sispe.persona">	
						<logic:empty name="ec.com.smx.sic.sispe.localizacion">
							<tr class="fila_titulo">
								<td width="25px" class="fila_contenido" align="center"><img
									src="./images/persona.gif" border="0"></td>
								<td height="25px" align="left"
									class="fila_contenido textoNegro10">&nbsp;Datos de personas
								</td>
								<td class="fila_contenido">
									<table cellspacing="0" cellpadding="1" align="right" border="0" width="100px">
										<tr>
											<td>
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									<div id="detallePersona">
										<table border="0" width="100%" cellpadding="0" cellspacing="0">
											<tr>
												<td align="center" height="380px">Seleccione un criterio de
									b&uacute;squeda
												</td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</logic:empty>
					</logic:empty>
				</table>
			</td>
		</tr>
	</table>
</div>
