<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana insertada desde entregaArticuloLocal.jsp -->
<logic:notEmpty name="ec.com.smx.sic.pedido.numeroAutorizacion" scope="request">
	<bean:define id="visible" value="visible"/>
</logic:notEmpty>	
<logic:empty name="ec.com.smx.sic.pedido.numeroAutorizacion" scope="request">
	<bean:define id="visible" value="hidden"/>	
</logic:empty>
<script language="javascript">mostrarModalZ('frameModal2',120);</script>


<div id="popup" class="popup" style="visibility:${visible};top:-50px;z-index:121;"> 
	<div id="center" class="popupcontenido">
		<table border="0" width="300px" align="center" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion">
			<tr>
				<td background="images/barralogin.gif" height="22px"></td>
			</tr>
			<tr>
				<td bgcolor="#F4F5EB">
					<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						<tr>
						  <td class="textoNegro11" align="center">
								<table border="0" width="99%" >
									<tr>
										<td height="5"><img src="images/info48.gif"/></td>
										<%--<tr><td height="5"><%@ include file="/include/mensajes.jsp"%></td></tr>--%>
										<td align="center"  colspan="2">
											<logic:notEmpty name="ec.com.smx.sic.pedido.numeroAutorizacion" scope="request">
												<bean:write name="ec.com.smx.sic.pedido.numeroAutorizacion"/>
											</logic:notEmpty>
										</td>
									</tr>
									<tr>
										<td></td>
										<td align="center" colspan="2">
											<input style="display:none" type="text" name="">
											<html:text property="autorizacion" styleClass="textObligatorio" size="20" onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['confirmarAut','mensajesPopUp','center'], {parameters: 'condicionAceptar=ok',popWait:true});ocultarModalID('frameModal2');"></html:text>
										</td>
									</tr>
									<tr><td height="5" colspan="3"></td></tr>
									<tr>
										<td></td>
										<td align="center">
											<div id="botonD">
												<html:link styleClass="aceptarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['confirmarAut','mensajesPopUp','center'], {parameters: 'condicionAceptar=ok',popWait:true});ocultarModalID('frameModal2');">Aceptar</html:link>
											</div>	
										</td>
										<td align="center">
											<div id="botonD">
		                                        <html:link styleClass="cancelarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['confirmarAut'], {parameters: 'condicionCancelar=ok',popWait:false});ocultarModalID('frameModal2');">Cancelar</html:link>
											</div>	
										</td>
									</tr>
									<tr><td height="5"></td></tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
<div style="visibility:hidden"><input type="text" value=""/></div>