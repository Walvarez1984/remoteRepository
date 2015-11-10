<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>

<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>

<bean:define id="cedula"><bean:message key="ec.com.smx.sic.sispe.documento.cedula"/></bean:define>
<bean:define id="ruc"><bean:message key="ec.com.smx.sic.sispe.documento.ruc"/></bean:define>
<bean:define id="pasaporte"><bean:message key="ec.com.smx.sic.sispe.documento.pasaporte"/></bean:define>
<bean:define id="doc"><bean:message key="ec.com.smx.sic.sispe.documento.internacional"/></bean:define>

 <c:if test="${vformName == 'crearPedidoEspecialForm'}">
 	<c:set var="accion" value="crearPedidoEspecial.do"/>
 </c:if>
  <c:if test="${vformName == 'cotizarRecotizarReservarForm'}">
 	<c:set var="accion" value="crearCotizacion.do"/>
 </c:if>

 <logic:notEmpty name="ec.com.smx.sic.token.corp">
	<bean:define id="tokenCorp" name="ec.com.smx.sic.token.corp" />
 </logic:notEmpty>
 
 <logic:empty name="ec.com.smx.sic.token.corp">
	<bean:define id="tokenCorp" value=""/>
 </logic:empty>
 

<div id="div_datosCliente">
	<table border="0" cellpadding="0" cellpadding="0" align="left" width="100%">
		<tr>
			<td width="80%" height="35px">
				<table  border="0" cellspacing="0" cellpadding="0" align="left" class="textoAzul11">
					<tr>
						<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">																	
							<td align="left">N&uacute;mero documento: *</td>
							<td align="left" >
							&nbsp;<smx:text property="numeroDocumento" styleClass="textObligatorio" styleError="campoError" size="19" onkeypress="return validarInputNumeric(event);"
								onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"
								onkeyup="requestAjaxEnter('${accion}',['divTabs','mensajes','div_datosCliente','pregunta'],{parameters:'consultarCliente=ok',evalScripts:true});"/>		

							</td>
							<td align="left" width="110px">
							<!-- 
								<a href="#" onclick="requestAjax('${accion}', ['divTabs','mensajes','div_datosCliente','pregunta'] , {parameters: 'buscarPersonaEmpresa=ok',evalScripts: true});executeButtonIframe2('#frameB','#formOpcionesBusqueda','opcionCargarBusqueda')">
									<img alt="Buscar" src="./images/buscar.gif" border="0">
								</a>
							 -->
								<a href="#" onclick="requestAjax('${accion}',['divTabs','mensajes','pregunta','popUpCorporativo','div_datosCliente'],{parameters:'buscarPersonaEmpresa=ok',evalScripts:true});">
									<img alt="Buscar" src="./images/buscar.gif" border="0">
								</a>
								
								<iframe id="frameA" src="jsf/contacto/opcionesGenerales.jsf${tokenCorp}" align="middle" name="con"
													width="0%" height="0%" frameborder="0" onload="killWait('div_wait');"> 
									<script type="text/javascript">
									 	popWait('div_wait');
									</script>
								</iframe>
								
								<iframe id="frameB" src="jsf/contacto/opcionesBusqueda.jsf${tokenCorp}" align="middle" name="bus"
													width="0%" height="0%" frameborder="0" onload="killWait('div_wait');"> 
									<script type="text/javascript">
									 	popWait('div_wait');
									</script>
								</iframe>
								
							</td>
							<td align="left">&nbsp;</td>
						</logic:empty>
						
					</tr>
							
					<logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
						<tr>
							<td align="left">Tipo documento:&nbsp;</td>
							<td align="left" class="textoNegro11">
								<bean:write  name="${vformName}" property="tipoDocumento"/>
							</td>	
						</tr>	
						<tr>
							<td align="right">N&uacute;mero documento:&nbsp;</td>
							<td align="left"  class="textoNegro11"><bean:write name="${vformName}" property="numeroDocumento"/></td>							
						</tr>
						<tr>
							<logic:equal name="${vformName}" property="tipoDocumento"  value="${ruc}">
								<logic:empty name="ec.com.smx.sic.ruc.Persona">	
									<td align="left" >Raz&oacute;n social:&nbsp;</td>	
									<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="nombreEmpresa" /></td>
								</logic:empty>	
								<logic:notEmpty  name="ec.com.smx.sic.ruc.Persona">
									<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="nombrePersona" /></td>
								</logic:notEmpty>	
							</logic:equal>							
							<logic:equal name="${vformName}" property="tipoDocumento"  value="${cedula}">	
								<td align="left" >Nombre persona:&nbsp; </td>	
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="nombrePersona" /></td>
							</logic:equal>								
							<logic:equal name="${vformName}" property="tipoDocumento"  value="${pasaporte}">	
								<td align="left" >Nombre persona:&nbsp; </td>	
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="nombrePersona" /></td>
							</logic:equal>							
						</tr>	
					</logic:notEmpty>							
					
					<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
						<tr>
							<logic:equal name="${vformName}" property="tipoDocumento"  value="${cedula}">
								<td align="left">Tipo documento:&nbsp; </td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="tipoDocumento" /></td>
								<td align="left">Nombre persona:&nbsp; </td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="nombrePersona" /></td>
							</logic:equal>
							<logic:equal name="${vformName}" property="tipoDocumento"  value="${pasaporte}">
								<td align="left">Tipo documento:&nbsp; </td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="tipoDocumento" /></td>
								<td align="left">Nombre persona:&nbsp; </td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="nombrePersona" /></td>
							</logic:equal>
							<logic:equal name="${vformName}" property="tipoDocumento"  value="${ruc}">
							<logic:empty name="ec.com.smx.sic.ruc.Persona">	
								<td align="left">Tipo documento:&nbsp; </td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="tipoDocumento" /></td>
								<td align="left">&nbsp;</td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="nombreEmpresa" /></td>
							</logic:empty>	
							<logic:notEmpty  name="ec.com.smx.sic.ruc.Persona">
								<td align="left">Tipo documento:&nbsp; </td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="tipoDocumento" /></td>
								<td align="left">&nbsp;</td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="nombrePersona" /></td>
							</logic:notEmpty>	
							</logic:equal>
							<logic:equal name="${vformName}" property="tipoDocumento"  value="${doc}">
							<td align="left">Tipo documento:&nbsp; </td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="tipoDocumento" /></td>
								<td align="left">Raz&oacute;n social:&nbsp; </td>
								<td align="left"  class="textoNegro11" ><bean:write name="${vformName}" property="nombreEmpresa" /></td>
							</logic:equal>
						</tr>	
					</logic:empty>					
				</table>					
			</td>
			<td width="20%">
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir">
					
						<bean:define id="numeroBonos" name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir"/>
						<label class="textoGris11"><b>Nro bonos a recibir:	<bean:write name="numeroBonos" formatKey="formatos.enteros"/></b></label>
					
				</logic:notEmpty>
			</td>
		</tr>
	</table>	
</div>