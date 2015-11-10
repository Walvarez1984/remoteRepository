<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>

<script language="javascript">mostrarModal();</script>

<div id="reservarStockEntrega">
	<table cellpadding="0" cellspacing="0" align="center" width="100%" >	
		<tr>																
			<td colspan="4">
				<div id="mensaje_popUp" style="position:relative;">
					<tiles:insert  page="/include/mensajes.jsp" /> 
				</div> 
			</td>                                                                                                                                                 															
		</tr>
		<tr><td height="10px" colspan="4"></td></tr>
		<tr>
			<td valign="top">
			    <div style="height:110px;">
			        <table border="0" cellspacing="0" cellpadding="0" >				        	
			         	<tr>
			            	<td width="30%" class="textoAzul11" align="left">Fecha M&iacute;n. Entrega:&nbsp;</td>
							<td align="left">								
								<label class="textoNegro11"><html:hidden property="fechaEntrega" write="true"/></label>
							</td>
			            </tr>		            
			            <tr>
			            	<td width="30%" class="textoAzul11" align="left">Fecha M&aacute;x. Entrega:&nbsp;</td>
							<td align="left">
								<table border="0" cellspacing="0">
									<tr>
										<td class="textoAzul11">
											<html:text property="fechaEntregaCliente" styleClass="textNormal" size="12" maxlength="10" onfocus="document.forms[0].opcionFecha[0].checked=true;" onkeypress="requestAjaxEnter('${accion}' );"/>
										</td>
										<td >
											<smx:calendario property="fechaEntregaCliente" key="formatos.fecha"/>
										</td>
										<td width="2px"></td>
									</tr>
								</table>
							</td>
			            </tr>
			            <tr>
			                <td width="30%" class="textoAzul11" align="left">Hora:&nbsp;</td>
			                <td>
			                	<smx:select property="horas" styleClass="comboObligatorio" styleError="campoError" onchange="requestAjax('crearCotizacion.do',['mensajes','div_pagina'],{parameters: 'cambiarHora=ok', evalScripts: true});">
										<html:option value="">Seleccione</html:option>										
												<logic:notEmpty name="ec.com.smx.sic.sispe.horaCol" >
													<logic:iterate name="ec.com.smx.sic.sispe.horaCol"  id="hora" indexId="indice">
														<html:option value="${hora}"></html:option>
													</logic:iterate>
												</logic:notEmpty>
								</smx:select>&nbsp;:
								<smx:select property="minutos" styleClass="comboObligatorio" styleError="campoError" onchange="requestAjax('crearCotizacion.do',['mensajes','div_pagina'],{parameters: 'cambiarMinuto=ok', evalScripts: true});">
										<html:option value="">Seleccione</html:option>										
												<logic:notEmpty name="ec.com.smx.sic.sispe.minutoCol" >
													<logic:iterate name="ec.com.smx.sic.sispe.minutoCol" id="minuto" indexId="indice">
														<html:option value="${minuto}"></html:option>
													</logic:iterate>
												</logic:notEmpty>
								</smx:select>&nbsp;HH:mm	
							</td>									                
			            </tr>	
			        </table>
			    </div>
			</td>
		</tr>		        	
	</table>
</div>
