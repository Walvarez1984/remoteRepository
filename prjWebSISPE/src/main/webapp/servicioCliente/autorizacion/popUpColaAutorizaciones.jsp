<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="estadoActivo" name="estadoActivo" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="token" name="token" classname="java.lang.String" ignore="true"/>

<bean:define id="codigoAutFechaMin" type="java.lang.String"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.disminuir.fecha.minima.entrega"/></bean:define>
<bean:define id="codigoAutResBodLocal" type="java.lang.String"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.reservar.bodega.local"/></bean:define>
<bean:define id="codigoAutCDElabCanastos" type="java.lang.String"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.cd.elabora.canastos"/></bean:define>
<bean:define id="codigoDescuentoVariable"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.descuentoVariable"/></bean:define>
<bean:define id="codigoAutorizacionStock"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.autorizacion.stock"/></bean:define>
<bean:define id="tituloAutorizacionDescVariable"><bean:message key="ec.com.smx.sic.sispe.titulo.autorizacion.descuento.variable"/></bean:define>
<bean:define id="tituloAutorizacionStock"><bean:message key="ec.com.smx.sic.sispe.titulo.autorizacion.stock"/></bean:define>


<c:set var="tamPopup" value="565px"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.alto.popup.autorizacion">
	<bean:define id="tam" name="ec.com.smx.sic.sispe.alto.popup.autorizacion"/>
	<c:set var="tamPopup"  value="${tam}"/>
</logic:notEmpty>

<bean:define id="mostrarModal" value="mostrarModal();"/>
<bean:define id="ocultarModal" value="ocultarModal();"/>
<bean:define id="zindex" value=""/>

<!-- para las autorizaciones de entregas se carga diferente en mostrar y ocultarModal  porque son popUp dentro de otro popUp y toca hacer asi para 
que se bloqueen los botones del primer popUp -->

<bean:define id="tituloAutorizacion" value=""/>
<logic:notEmpty name="ec.com.smx.sic.sispe.codigo.tipo.autorizacion">
	<bean:define id="codigoTipoAut" name="ec.com.smx.sic.sispe.codigo.tipo.autorizacion"/>
	<logic:equal name="codigoTipoAut" value="${codigoAutFechaMin}">
		<bean:define id="mostrarModal"  value="mostrarModalZ('frameModal2',120);"/>
		<bean:define id="ocultarModal" value="ocultarModalID('frameModal2');"/>
		<bean:define id="zindex" value="z-index:121;"/>
	</logic:equal>
	<logic:equal name="codigoTipoAut" value="${codigoAutResBodLocal}">
		<bean:define id="mostrarModal"  value="mostrarModalZ('frameModal2',120);"/>
		<bean:define id="ocultarModal" value="ocultarModalID('frameModal2');"/>
		<bean:define id="zindex" value="z-index:121;"/>
	</logic:equal>
	
	<!-- Autorizacion para que la bodega Elabore los canastos en pedidos consolidados -->
	<logic:equal name="codigoTipoAut" value="${codigoAutCDElabCanastos}">
		<bean:define id="mostrarModal"  value="mostrarModalZ('frameModal2',120);"/>
		<bean:define id="ocultarModal" value="ocultarModalID('frameModal2');"/>
		<bean:define id="zindex" value="z-index:121;"/>
	</logic:equal>
	
	<!-- Para el caso de autorizacion de descuento variable -->
	<logic:equal name="codigoTipoAut" value="${codigoDescuentoVariable}">
		<bean:define id="tituloAutorizacion" value="${tituloAutorizacionDescVariable} - "/>
	</logic:equal>
		<!-- Para el caso de autorizacion de descuento variable -->
	<logic:equal name="codigoTipoAut" value="${codigoAutorizacionStock}">
		<bean:define id="tituloAutorizacion" value="${tituloAutorizacionStock} - "/>
	</logic:equal>
</logic:notEmpty>

<script language="javascript">${mostrarModal}</script>

<div id="descuentos" class="popup" style="top:-100px;${zindex}">
	<table border="0" cellpadding="0" cellspacing="0" align="center" width="50%">
		<tr>
			<td valign="top" align="center">
			    <div id="center" class="popupcontenido">
					<%-- Contenido de la ventana de confirmacion --%>					
			        <table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="62%">
			            <tr>
			                <td background="images/barralogin.gif" align="center">
			                    <table cellpadding="0" cellspacing="0" width="100%" border="0">			                        
                                    <tr>
			                            <td class="textoBlanco11" align="left" height="20px">&nbsp;
			                                <b>${tituloAutorizacion} Autorizaci&oacute;n requerida</b>
			                            </td>
			                            <td align="right">											
                                            <html:link title="Cerrar" href="#" onclick="requestAjax('${vformAction}.do',['div_pagina'], {parameters: 'cancelar=ok', evalScripts: true});${ocultarModal}">
                                                <img src="./images/close.gif" border="0" style="padding-top:3px;"/>
                                            </html:link>&nbsp;                                            
			                            </td>		                           
			                        </tr>
			                    </table>
			                </td>
			            </tr>
			            <tr>
			                <td bgcolor="#F4F5EB" valign="top" >	                            
			                	<div style="width: 630px; height:${tamPopup};border-bottom-width:5px;border-bottom-style:solid;border-bottom-color:#cccccc">
			                		<bean:define id="urlJSF" name="ec.com.smx.sic.sispe.pedido.popupAutorizaciones" />
									<iframe src="${urlJSF}" align="middle" name="componenteAutorizaciones" width="100%" height="100%" frameborder="0" class="tabla_informacion" onload="killWait('div_wait');"> 
													<script type="text/javascript">
													 popWait('div_wait');
													</script>
									</iframe>
								</div>
			                </td>
			            </tr>
			        </table>
			    </div>
			</td>
		</tr>
	</table>
</div>