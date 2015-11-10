<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>

<c:set var="formulario" value="cotizarRecotizarReservarForm" />
<c:set var="accion" value="crearCotizacion.do"/>
<logic:empty name="ec.com.smx.sic.sispe.existeLugarEntrega">
	<table class="textoNegro11" cellpadding="0" cellspacing="0"
		align="left" width="100%">
			<tr>
				<td width="100%" height="400px"> 
					<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" >
						<tr>						
							<td align="center" class="textoNegro11">
								No existe una configuraci&oacute;n para mostrar el calendario											
							</td>
						</tr>
					</table>

				</td>
			</tr>
	</table>
</logic:empty>
<logic:notEmpty name="ec.com.smx.sic.sispe.existeLugarEntrega">	

	<table class="textoNegro11" cellpadding="0" cellspacing="0"
		align="left" width="100%">
		<tr>
			<td height="3px"></td>
		</tr>
		
		
			<tr>
				<td width="100%">
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>						
							<td>
								<div id="localesCalendario">								
									<logic:notEmpty name="ec.com.smx.sic.sispe.mostrar.calendario.bodega.por.horas">										
										<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/calendarioEntregaBodega.jsp"/>											
									</logic:notEmpty>
									<logic:empty name="ec.com.smx.sic.sispe.mostrar.calendario.bodega.por.horas">	
										<bean:define id="localsicmer"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.domicilio.local"/></bean:define>
										<!-- Caso de entrega a domicilio desde local -->
										<logic:equal name="cotizarRecotizarReservarForm" property="opLugarEntrega" value="${localsicmer}">
											<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/calendarioEntregasSicmer.jsp"/>
										</logic:equal>
										<logic:notEqual name="cotizarRecotizarReservarForm" property="opLugarEntrega" value="${localsicmer}"> 
											<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/calendarioEntregas.jsp"/>	
										</logic:notEqual >											
									</logic:empty>								
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		
	</table>

</logic:notEmpty>
