<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<logic:notEmpty name="ec.com.smx.sic.sispe.mensajes">
	<script language="JavaScript" type="text/javascript">
		
		var d = document.getElementById('div_pagina');
		d.style.overflow="hidden";
	</script>
</logic:notEmpty>

<div id="divConfEntregas" style="top:-150px">
<table  border="0" cellpadding="0" cellspacing="0" width="100%" align="center">	
	<tr>
		<td align="left" valign="top" width="100%">
			<div id="mensajesPopUp" style="font-size:0px;position:relative;">
				<!-- variable para que no muestre error en pantalla de atras en pop up de calendario sicmer al aceptar sin rango de horas -->
				<logic:empty name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.horasInvalidas">
					<tiles:insert  page="/include/mensajes.jsp" />
				</logic:empty>
		  	</div>
			<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
				<tr>
					<td height="35" valign="middle">Configuraci&oacute;n entregas</td>
					<td align="right" valign="top">
						<table border="0" cellpadding="1" cellspacing="0">
							<tr>
								<!-- validacion para que no muestre links de autorizaciones en pantalla de entrega a domicilio desde local-->
								<bean:define id="localsicmer"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.domicilio.local"/></bean:define>
								<bean:define id="responsableBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
								<c:if test="${cotizarRecotizarReservarForm.opLugarEntrega!=localsicmer || (cotizarRecotizarReservarForm.opLugarEntrega==localsicmer && cotizarRecotizarReservarForm.opElaCanEsp==responsableBodega)}">
									<td class="columna_contenido_der" align="center">&nbsp;</td>
									<td>&nbsp;</td>
									<logic:notEmpty name="ec.com.smx.sic.sispe.mostrar.autorizacion.cd.elabora.canastos">
										<td>
											<div id="botonA">
												<html:link styleClass="autorizarA" href="#" title="Permite ingresar una autorizaci&oacute;n para que el CD elabore los canastos." onclick="requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'usarAutorizacion=3',evalScripts:true});ocultarModal();">CD elab. canastos</html:link>										
											</div>
										</td>
									</logic:notEmpty>
									<td>
										<div id="botonA">
											<html:link styleClass="autorizarA" href="#" title="Permite ingresar una autorizaci&oacute;n para disminuir la fecha m&iacute;nima de entrega al cliente." onclick="requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'usarAutorizacion=2',evalScripts:true});ocultarModal();">Usar aut.</html:link>										
										</div>
									</td>								
									<logic:empty name="ec.com.smx.sic.sispe.mostrar.calendario.bodega.por.horas">
										<td  valign="top" >
											<div id="botonAp">												
												<html:link styleClass="autCapCalA" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'botonAutorizar=ok',popWait:true,evalScripts:true});ocultarModal();" title="Permite reservar el espacio de otro local en un d&iacute;a donde la capacidad no sea adecuada.">Autoriza. calendario</html:link>										
											</div>
										</td>			
									</logic:empty>
								</c:if>										
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td valign="top" width="100%" align="center">
				 
			
            <table border="0" cellpadding="1" cellspacing="0" width="100%">
				
								
             	<tr>
             		<td>
             			<div id="tabConfEntregas">
             				<tiles:insert page="/controlesUsuario/controlTabPopUp.jsp"/>
             			</div>							
					</td>
				</tr>
			</table>
		 </td>
	</tr>
</table>
</div>