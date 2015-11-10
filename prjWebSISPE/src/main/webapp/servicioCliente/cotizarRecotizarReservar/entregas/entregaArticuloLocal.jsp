<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:insert page="/include/top.jsp"/>
<logic:notEmpty name="sispe.vistaEntidadResponsableDTO">
	<bean:define id="tipoEntidadResponsable"><bean:write name="sispe.vistaEntidadResponsableDTO" property="tipoEntidadResponsable"/></bean:define>
</logic:notEmpty>
<bean:define id="entidadResponsableLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
<html:form action="entregaLocalCalendario" method="post">
<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	
		<html:hidden property="numeroDocumento"/>
		<html:hidden property="nombreContacto"/>
		<html:hidden property="telefonoContacto"/>
		<html:hidden property="fechaEntrega"/>
		<html:hidden property="rucEmpresa"/>
		<html:hidden property="nombreEmpresa"/>
		<html:hidden property="opTipoBusqueda"/>
		<html:hidden property="checkCalculosPreciosMejorados"/>
		<html:hidden property="checkCalculosPreciosAfiliado"/>
		<%--T&iacute;tulos, boton: salir --%>
		<tr>
			<td>
				<div id="confirmarSalir" style="font-size:0px;">
					<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/confirmacionCerrarEntrega.jsp"/>
				</div>
				<div id="confirmarLocalEntrega" style="font-size:0px;">
					<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/confirmacionLocalEntrega.jsp"/>
				</div>
				<div id="pregunta" style="font-size:0px;">
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp"/>
						<script language="javascript">mostrarModalAux();</script>
					</logic:notEmpty>
					
					<!-- IOnofre. PopUp para subir archivo croquis de la direccion de entrega, pendiente hasta realizar el cambio en el estado del pedido. -->
					<logic:notEmpty name="ec.com.smx.sic.sispe.popupArchivoEntrega">
						<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/nuevaConfiguracionV2/popUpCargarArchivoCroquis.jsp"/>
					</logic:notEmpty>
										
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpAutorizacionEntregas">
						<tiles:insert page="/servicioCliente/autorizacion/usarNumeroAutorizacion.jsp"/>				
					</logic:notEmpty>
					
					<!-- PopUp de Calendario SICMER para entrega a domicilio desde local-->
					<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.popupCalendario">
						<tiles:insert page="../entregas/popupCalendarioEntregaSicmer.jsp"/>
					</logic:notEmpty>
					
					<!-- PopUp de autorizaciones-->
					<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.popupAutorizaciones">
						<tiles:insert page="/servicioCliente/autorizacion/popUpColaAutorizaciones.jsp">
							<tiles:put name="vtformAction" value="entregaLocalCalendario"/>                            
						</tiles:insert>
					</logic:notEmpty>	
				</div>
				<div id="pregunta1" style="font-size:0px;">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mensajeSeleccionCiudad">
						<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/mensajeSeleccionCiudad.jsp"/>
					</logic:notEmpty>
				</div>
				<div id="confirmarAut" style="font-size:0px;">
					<logic:notEmpty name="ec.com.smx.sic.pedido.numeroAutorizacion">
						<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/mensajes.jsp"/>
					</logic:notEmpty>
				</div>
				<div id="pregunta2" style="font-size:0px;">
					<logic:notEmpty name="ec.com.smx.sic.confirmacionanteriorentregas">
						<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/confirmacionAnteriorConfiguracionEntrega.jsp"/>
					</logic:notEmpty>
				</div>
			</td>
		</tr>
		<tr>
			<td align="left" valign="top" width="100%">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
					<tr>
						<td width="3%" align="center"><img src="images/entregaPedido.gif" border="0"></img></td>
						<td height="35" valign="middle">Entregas</td>
						<td width="4%" valign="middle"><table class="tabla_informacion_leyendas" cellpadding="0" cellspacing="0" width="100%"><tr><td class="cafeClaro10" width="100%" height="12px"></td></tr></table></td>
						<td width="8%" align="left" class="textoAzul9">Entrega despachada</td>
						
						<!-- leyenda entregas con fecha despacho vencida -->
						<td width="4%" valign="middle"><table class="tabla_informacion_leyendas_amarillo" cellpadding="0" cellspacing="0" width="100%"><tr><td class="amarillo10" width="100%" height="12px"></td></tr></table></td>
						<td width="12%" align="left" class="textoAzul9">Entrega con fecha despacho vencida</td>
						
						<td align="right" valign="top">
							<input type="hidden" name="botonGuardarCambios" value="">
							<input type="hidden" name="botonCerrarEntregas" value="">
							<table border="0" cellpadding="1" cellspacing="0">
								<tr>
									<td>
										<div id="botonA">
											<html:link styleClass="ayudaA" href="ayuda/ConfiguracionEntregaArticulos.pdf" target="blank">Ayuda</html:link>
										</div>
									</td>
									<%-- >td class="columna_contenido_der" align="center">&nbsp;</td>
									<td>&nbsp;</td>
									<td>
										<div id="botonA">
											<html:link styleClass="autorizarA" href="#" title="usar autorizaci&oacute;n externa" onclick="requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'usarAutorizacion=2',evalScripts:true});">Usar Aut.</html:link>
										</div>
									</td>
									<td>
										<div id="botonAp">
											<html:link styleClass="autCapCalA" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['confirmar','mensajes'], {parameters: 'botonAutorizar=ok',popWait:true,evalScripts:true});mostrarModal();" title="permite reservar el espacio de otro local en un día donde la capacidad no sea adecuada">Autoriza. Calendario</html:link>
										</div>
									</td--%>
									<td class="columna_contenido_der" align="center">&nbsp;</td>
									<td>&nbsp;</td>
									<td>
										<div id="botonA">
											<html:link styleClass="guardarA" href="#" onclick="cotizarRecotizarReservarForm.botonGuardarCambios.value='ok';cotizarRecotizarReservarForm.submit();">Guardar</html:link>
										</div>
									</td>
									<td>
										<div id="botonA">
											<html:link styleClass="cancelarA" href="#" onclick="cotizarRecotizarReservarForm.botonCerrarEntregas.value='ok';cotizarRecotizarReservarForm.submit();">Cancelar</html:link>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td height="0px"></td></tr>
		<logic:notEqual name="tipoEntidadResponsable" value="${entidadResponsableLocal}">
			<html:hidden property="indiceLocalResponsable"/>
			<tr><td align="left"><label class="textoAzul11">&nbsp;&nbsp;&nbsp;Local:&nbsp;</label><label class="textoNegro10"><html:hidden name="cotizarRecotizarReservarForm" property="localResponsable" write="true"/></label></td></tr>
		</logic:notEqual>
		<tr>
			<td>
			<div id="entregas" style="width:100%;height:500px;overflow-y:hidden;overflow-x:hidden;">
				<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="99%">
					<%--Mensajes de confirmacion--%>
					<tr>
						<%--Barra Izquierda--%>
						
						<%--<td valign="top" width="40%" align="left">
							<table cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td>
										<div id="localesCalendario">
											<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/localesCalendario.jsp"/>
										</div>
									</td>
								</tr>
							</table>
						</td><%--
						<%--Fin Barra Izquierda--%>
						<%--<td width="1%"  align="left">&nbsp;</td>--%>
						<%-- Calendario --%>
						<%-- <td valign="top" width="59%"  align="left">
							<div id="reservacion">
								<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/reservacion.jsp"/>
							</div>
						</td>--%>
						<!-- @author jmena -->
						 <td valign="top" width="100%" align="center">
							<tiles:insert page="/controlesUsuario/controlTab.jsp"/>
						 </td>	
					</tr>
				</table>
			</div>
			</td>
		</tr>

</table>
</html:form>
<tiles:insert page="/include/bottom.jsp"/>