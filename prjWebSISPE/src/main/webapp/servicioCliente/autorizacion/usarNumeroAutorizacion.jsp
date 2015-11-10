<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<script language="javascript">mostrarModalZ('frameModal2',120);</script>

<logic:empty name="usarAutorizacion">
	<script language="javascript">ocultarModal();</script>
</logic:empty>

<div id="usarNumeroAutorizacionEnt" class="popup" style="top:-50px;z-index:121;">
	<table border="0" cellpadding="0" cellspacing="0" align="center" width="50%">
		<tr>
			<td valign="top" align="center">				
				<div id="center" class="popupcontenido">
					<table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="62%">
						 <tr>
			                <td background="images/barralogin.gif" align="center">
			                    <table cellpadding="0" cellspacing="0" width="100%" border="0">			                        
                                    <tr>
			                            <td class="textoBlanco11" align="left" height="20px">&nbsp;
			                                <b>Usar Autorizaci&oacute;n Externa</b>
			                            </td>
			                            <td align="right">											
                                            <html:link title="Cerrar" href="#" onclick="requestAjax('entregaLocalCalendario.do',['pregunta'], {parameters: 'cancelarUsoAut=ok', evalScripts: true});ocultarModalID('frameModal2');ocultarModal();">
                                                <img src="./images/close.gif" border="0" style="padding-top:3px;"/>
                                            </html:link>&nbsp;                                            
			                            </td>		                           
			                        </tr>
			                    </table>								
			                </td>
		            	</tr>
		            	<tr>
		            		<td bgcolor="#F4F5EB" valign="top">	
								<div id="mensajesPopUpAutorizacion">
									<logic:notEmpty name="ec.com.smx.sic.sispe.numeroautorizacionexternaerror">
										<table>
											<tr>
												<td align="left">
													<img src="images/error.gif">
												</td>
												<td class="textoRojo11" align="left">
													<b>El n&uacute;mero de autorizaci&oacute;n es incorrecto para el tipo de autorizaci&oacute;n.</b>
												</td>
											</tr>
										</table>
									</logic:notEmpty>
									
								</div>
			                    <table class="tabla_informacion textoNegro11" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
			                    	<tr>
			                    		<td>
			                    			<table class="tabla_informacion fondoBlanco" border="0" width="500px" align="center" cellpadding="0" cellspacing="5">
			                    				<tr>
													<td width="10%">Tipo:&nbsp;</td>
													<td>
														<html:select styleClass="combos" property="tipoAutorizacion">
															<html:option value=""></html:option>
															<logic:notEmpty name="ec.com.smx.sic.sispe.tiposAutorizaciones">
																<html:options collection="ec.com.smx.sic.sispe.tiposAutorizaciones" property="codigoInterno" labelProperty="nombre"/>
															</logic:notEmpty>
														</html:select>
													</td>
												</tr>
												<tr><td height="10px"></td></tr>
												<tr>
													<td>N&uacute;mero:</td>
													<td><html:text styleClass="textNormal" property="numeroAutorizacion" size="20"/></td>
												</tr>
												<tr>
													<td valign="top">Observaci&oacute;n:</td>
													<td><html:textarea styleClass="textNormal" property="observacionAutorizacion" cols="50" rows="4"/></td>
												</tr>
			                    			</table>
			                    		</td>
			                    	</tr>
			                    	<tr>
										<td align="center">
											<table cellpadding="0" cellspacing="0" border="0">
												<tr>                                                                                                   
													<td><div id="botonD"><html:link href="#" styleClass="aceptarD" onclick="requestAjax('entregaLocalCalendario.do',['pregunta','usarNumeroAutorizacionEnt'],{parameters: 'aceptarUsoAut=ok', evalScripts: true});ocultarModalID('frameModal2');ocultarModal();">Aceptar</html:link></div></td>
													<td>&nbsp;</td>
													<td><div id="botonD"><html:link href="#" styleClass="cancelarD" onclick="requestAjax('entregaLocalCalendario.do',['pregunta'], {parameters: 'cancelarUsoAut=ok', evalScripts: true});ocultarModalID('frameModal2');ocultarModal();">Cancelar</html:link></div></td>
												</tr>
											</table>
										</td>
									</tr>
			                    </table>
			                </td>
		            	</tr>																						
					</table>
				</div>
			</td>
		</tr>		
	</table>
</div>