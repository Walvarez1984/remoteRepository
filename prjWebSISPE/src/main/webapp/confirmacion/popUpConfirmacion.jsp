<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<c:set var="tope" value="100"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="tope">
	<c:set var="tope"><bean:write name="ec.com.smx.sic.sispe.popUpConfirmacion" property="tope"/></c:set>
</logic:notEmpty>

<%-- jsp genérica para los popups de confirmación --%>
<div id="popupConfirmar" class="popup" style="visibility:visible;top:${tope}px">
	<div id="center" class="popupcontenido">
		<bean:define id="ancho" name="ec.com.smx.sic.sispe.popUpConfirmacion" property="ancho"/>
		<table border="0" align="center" cellspacing="0" cellpadding="0" bgcolor="#F4F5EB"  width="${ancho}%">
			<tr>
				<td background="images/barralogin.gif" height="22px">
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td class="textoBlanco11">
								<b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.popUpConfirmacion" property="tituloVentana"/></b>
							</td>
							<td align="right">
								<div id="botonWin">
									<logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="noMostrarBotonCerrar">
										<logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="accionEnvioCerrar">
											<a href="#" class="linkBlanco8" onclick="hide(['popupConfirmar']);ocultarModal();">X</a>
										</logic:empty>
										<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="accionEnvioCerrar">
											<bean:define id="valorEnvioCerrar" name="ec.com.smx.sic.sispe.popUpConfirmacion" property="accionEnvioCerrar"/>
											<a href="#" class="linkBlanco8" onclick="${valorEnvioCerrar}">X</a>
										</logic:notEmpty>
									</logic:empty>
								</div>
							</td>
							<td width="3px"></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td >
				<table border="0" align="center" cellspacing="0" cellpadding="5" bgcolor="#F4F5EB" class="tabla_informacion" width="100%">
				<tr><td>
					<table class="fondoBlanco textoNegro11" border="0" width="100%" align="center">
						<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="mensajeErrorVentana">
							<bean:define name="ec.com.smx.sic.sispe.popUpConfirmacion" property="mensajeErrorVentana" id="mensajeError"/>
							<tr>
								<td align="left">
									<img src="images/error.gif">
								</td>
								<td class="textoRojo11" align="left"><b>${mensajeError}</b></td>
							</tr>
						</logic:notEmpty>
						<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="mensajeVentana">
							<tr>
								<td align="left" valign="top" colspan="2">
									<bean:define name="ec.com.smx.sic.sispe.popUpConfirmacion" property="mensajeVentana" id="mensaje"/>
									${mensaje}
								</td>
							</tr>
						</logic:notEmpty>
						<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="contenidoVentana">
							<tr>
								<td align="left" valign="top" colspan="2">
									<bean:define id="paginaContenido" name="ec.com.smx.sic.sispe.popUpConfirmacion" property="contenidoVentana"/>
									<tiles:insert page="/${paginaContenido}">
										<tiles:put name="vtformName" beanName="vformName"/>
										<tiles:put name="vtformAction" beanName="vformAction"/>
									</tiles:insert>
								</td>
							</tr>
						</logic:notEmpty>
						 <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="preguntaVentana">
							<tr>
								<td valign="top" width="5%"><img src="images/pregunta24.gif" border="0"></td>
								<td align="left">
									<bean:define name="ec.com.smx.sic.sispe.popUpConfirmacion" property="preguntaVentana" id="pregunta"/>
									${pregunta}
								</td>
							</tr>
						</logic:notEmpty>
					</table>
					</td><tr>
					<tr><td>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="formaBotones">
					<table cellpadding="0" cellspacing="0" align="center" width="100%">
						<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorOK">
							<!-- se definen los valores de los botones -->
							<bean:define id="valorSI" name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorOK"/>
						</logic:notEmpty>
						<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorCANCEL">
							<bean:define id="valorNO" name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorCANCEL"/>
						</logic:notEmpty>
						<!-- se verifica la forma de los botones -->
						<!-- solo OK -->
						<logic:equal name="ec.com.smx.sic.sispe.popUpConfirmacion" property="formaBotones" value="1">
							<td align="center">
								<div id="botonD">
									<logic:notEmpty name="valorSI">
										<logic:equal name="ec.com.smx.sic.sispe.popUpConfirmacion" property="formaEnvioDatos" value="1">
											<html:link styleClass="aceptarD" href="#" onclick="realizarEnvio('${valorSI}');">Aceptar</html:link>
										</logic:equal>
										<logic:equal name="ec.com.smx.sic.sispe.popUpConfirmacion" property="formaEnvioDatos" value="2">
											<html:link styleClass="aceptarD" href="#" onclick="${valorSI}">Aceptar</html:link>
										</logic:equal>
									</logic:notEmpty>
									<logic:empty name="valorSI">
										<html:link styleClass="aceptarD" href="#" onclick="hide(['popupConfirmar']);ocultarModal();">Aceptar</html:link>
									</logic:empty>
								</div>
							</td>
						</logic:equal>
						<!-- OK + CANCEL (SI + NO) -->
						<logic:equal name="ec.com.smx.sic.sispe.popUpConfirmacion" property="formaBotones" value="2">
							<c:set var="etiquetaOk" value="Aceptar"/>
							<c:set var="etiquetaCancel" value="Cancelar"/>
							<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="etiquetaBotonOK">
								<c:set var="etiquetaOk"><bean:write name="ec.com.smx.sic.sispe.popUpConfirmacion" property="etiquetaBotonOK"/></c:set>
							</logic:notEmpty>
							<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="etiquetaBotonCANCEL">
								<c:set var="etiquetaCancel"><bean:write name="ec.com.smx.sic.sispe.popUpConfirmacion" property="etiquetaBotonCANCEL"/></c:set>
							</logic:notEmpty>
							<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="estiloOK">
								<c:set var="estiloOK"><bean:write name="ec.com.smx.sic.sispe.popUpConfirmacion" property="estiloOK"/></c:set>
							</logic:notEmpty>
							<logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="estiloOK">
								<c:set var="estiloOK" value="aceptarD"></c:set>
							</logic:empty>
							<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="estiloCANCEL">
								<c:set var="estiloCANCEL"><bean:write name="ec.com.smx.sic.sispe.popUpConfirmacion" property="estiloCANCEL"/></c:set>
							</logic:notEmpty>
							<logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="estiloCANCEL">
								<c:set var="estiloCANCEL" value="cancelarD"></c:set>
							</logic:empty>
							<td align="center">
								<div id="botonD">
									<logic:equal name="ec.com.smx.sic.sispe.popUpConfirmacion" property="formaEnvioDatos" value="1">
										<html:link styleClass="${estiloOK}" href="#" onclick="realizarEnvio('${valorSI}'); popWait('div_wait');">${etiquetaOk}</html:link>
									</logic:equal>
									<logic:equal name="ec.com.smx.sic.sispe.popUpConfirmacion" property="formaEnvioDatos" value="2">
									
										<html:link styleClass="${estiloOK}" href="#" onclick="${valorSI}">${etiquetaOk}</html:link>
									</logic:equal>
								</div>
							</td>
							<td align="center">
								<div id="botonD">
									<logic:notEmpty name="valorNO">
									
										<html:link styleClass="${estiloCANCEL}" href="#" onclick="${valorNO}">${etiquetaCancel}</html:link>
									</logic:notEmpty>
									<logic:empty name="valorNO">
										<html:link styleClass="${estiloCANCEL}" href="#" onclick="hide(['popupConfirmar']);ocultarModal();">${etiquetaCancel}</html:link>
									</logic:empty>
								</div>
							</td>
						</logic:equal>
					</table>
				</logic:notEmpty>
				</td><tr>
				</table>
				</td>
			</tr>
		</table>
	</div>
</div>