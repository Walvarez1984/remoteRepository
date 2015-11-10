<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<bean:define id="localOrigen"><bean:message key="opcion.localOrigen"/></bean:define>
<bean:define id="localCercano"><bean:message key="opcion.localCercano"/></bean:define>

<logic:notEmpty name="ventanaLocalEntrega">
	<bean:define id="visible" value="visible"/>
	<script language="javascript">
		mostrarModal();
	</script>	
</logic:notEmpty>	
<logic:empty name="ventanaLocalEntrega">
	<bean:define id="visible" value="hidden"/>
	<script language="javascript">
		ocultarModal();
	</script>
</logic:empty>	
<div id="popup" class="popup" style="visibility:${visible};"> 
	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="30%">&nbsp;</td>
			<td align="left" width="70%">
				<%--<iframe name="ZONE1" height="180px" width="470px" class="popupContenidoFrame">
					Este explorador no soporta IFRAME por favor utilice alg&uacute;n explorador recomendado...
				</iframe>--%>
				<div id="center" class="popupcontenido">
					<table border="1" width="300px" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion">
						<tr>
							<td background="images/barralogin.gif" height="22px" class="textoBlanco11"><b>&nbsp;&nbsp;Confirmaci&oacute;n</b></td>
						</tr>
						<tr>
							<td>
								<div id="mensajes" style="font-size:0px;position:relative;">
									<tiles:insert page="/include/mensajes.jsp"/>
								</div>
							</td>
						</tr>
						<tr>
							<td bgcolor="#F4F5EB">
								<table class="tabla_informacion textoNegro11" border="0" width="100%" align="center" height="50px">
									<logic:notEmpty name="ec.com.smx.sic.sispe.entregas.vistaLocalDTO.origen">
										<bean:define id="vistaLocalOrigen" name="ec.com.smx.sic.sispe.entregas.vistaLocalDTO.origen"/>
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.entregas.vistaLocalDTO.cercano">
	                                        <bean:define id="vistaLocalCercano" name="ec.com.smx.sic.sispe.entregas.vistaLocalDTO.cercano"/>
                                        </logic:notEmpty>	    
										<%--tr>
											<td align="left" colspan="2"><b>Local cercano:&nbsp;</b>
												<logic:notEmpty name="ec.com.smx.sic.sispe.entregas.vistaLocalDTO.cercano">
													<bean:define id="vistaLocalCercano" name="ec.com.smx.sic.sispe.entregas.vistaLocalDTO.cercano"/>
													<bean:write name="vistaLocalCercano" property="id.codigoLocal"/>-<bean:write name="vistaLocalCercano" property="nombreLocal"/>
												</logic:notEmpty>	
											</td>
										</tr>
										<tr><td width="2px"></td></tr--%>
										<tr>
											<td  width="2%"></td>
											<td><b>Local de Recepci&oacute;n:</b></td>
										</tr>
										<tr>
											<td>
												<html:radio property="opLocalEntrega" value="LOR"/>
											</td>
											<td>
													<bean:write name="vistaLocalOrigen" property="id.codigoLocal"/>-<bean:write name="vistaLocalOrigen" property="nombreLocal"/>
											</td>
										</tr>
										<logic:notEmpty name="ec.com.smx.sic.sispe.entregas.vistaLocalDTO.cercano">
											<tr>
												<td>
													<html:radio property="opLocalEntrega" value="LCE"/>
												</td>
												<td>
													<bean:write name="vistaLocalCercano" property="id.codigoLocal"/>-<bean:write name="vistaLocalCercano" property="nombreLocal"/>
												</td>
											</tr>
										</logic:notEmpty>	
										<tr>
											<td></td>
											<td align="center"  colspan="2">
												<div id="botonD">
													<html:link styleClass="aceptarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['confirmarLocalEntrega','mensajes','calendario','locales'], {parameters: 'condicionAceptarLocalEntrega=ok',popWait:true,evalScripts:true});ocultarModal();">Aceptar</html:link>
												</div>	
											</td>
										</tr>
									</logic:notEmpty>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>			
</div>