<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana insertada desde entregaArticuloLocal.jsp -->
<logic:notEmpty name="ec.com.smx.sic.sispe.mensajeSeleccionCiudad" scope="request">
	<bean:define id="visible" value="visible"/>
	<script language="javascript">mostrarModalZ('frameModal2',120);</script>
</logic:notEmpty>	
<logic:empty name="ec.com.smx.sic.sispe.mensajeSeleccionCiudad" scope="request">
	<bean:define id="visible" value="hidden"/>	
</logic:empty>	
<div id="popup" class="popup" style="visibility:${visible};" style="top:-50px;z-index:121;"> 
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
										<td align="center" colspan="3">
                                        	<table border="1">
                                            	<tr>
                                                	<td>
	       												Usted seleccion&oacute; una ciudad diferente a las recomendadas. Desea Reconfigurar las opciones para que el lugar de entrega sea en "OTRO LOCAL"?
                                                    </td>
                                                </tr>
                                            	<tr>
                                                	<td>
														Nota: Si selecciona NO debe ingresar la distancia aproximada desde un local de referencia en la ciudad destino al domicilio del cliente.
                                                    </td>
                                                </tr>
                                        	</table>        
										</td>
									</tr>
									<tr><td height="5" colspan="3"></td></tr>
									<tr>
										<td></td>
										<td align="center">
											<div id="botonDmin">
												<html:link styleClass="siDmin" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['popupConfirmar','localEntrega','calendario','pregunta1','datos'], {parameters: 'condicionSi=ok',popWait:true});ocultarModalID('frameModal2');">Si</html:link>
											</div>	
										</td>
										<td align="center">
											<div id="botonDmin">
		                                        <html:link styleClass="noDmin" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['pregunta1','datos'], {parameters: 'condicionNo=ok',popWait:false});ocultarModalID('frameModal2');">No</html:link>
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