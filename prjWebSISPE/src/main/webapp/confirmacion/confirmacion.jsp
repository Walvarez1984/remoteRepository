<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.botonSI">
    <bean:define id="valor_SI" name="ec.com.smx.sic.sispe.pedido.botonSI"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.botonNO">
    <bean:define id="valor_NO" name="ec.com.smx.sic.sispe.pedido.botonNO"/>
</logic:notEmpty>

<bean:define name="sispe.estado.activo" id="estadoActivo"/>
<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>

<c:set var="accion" value="crearCotizacion.do"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.busqueda.porPedidosEspeciales">
    <c:set var="accion" value="crearPedidoEspecial.do"/>
</logic:notEmpty>

<c:set var="onclick_btn_SI" value="realizarEnvio('${valor_SI}');"/>

<logic:notEmpty name="ec.com.smx.sic.sispe.consolidar.cerrarPopUp">
 <bean:define id="onclick_btn_NO" name="ec.com.smx.sic.sispe.consolidar.cerrarPopUp"/>
</logic:notEmpty>
<logic:empty name="ec.com.smx.sic.sispe.consolidar.cerrarPopUp">	
	<c:set var="onclick_btn_NO" value="hide(['popup']);ocultarModal();"/>    
</logic:empty>

<c:set var="ancho" value="40%"/>
<c:set var="tope" value="100px"/>
<c:set var="titulo" value="Confirmaci&oacute;n"/>
<c:set var="ayudaBoton" value=""/>
<c:set var="mostrarOpcionesImpresion" value=""/>
<c:set var="confirmarReservacionBodega" value=""/>

<logic:notEmpty name="ec.com.smx.sic.sispe.titulo.popup.confirmacion">	
	<bean:define id="tituloPopUp" name="ec.com.smx.sic.sispe.titulo.popup.confirmacion"/>
	<c:set var="titulo" value="${tituloPopUp}"/> 
</logic:notEmpty> 

<logic:equal name="ec.com.smx.sic.sispe.pedido.botonSI" value="siRegReservacion">
    <c:set var="onclick_btn_NO" value="hide(['popup']);ocultarModal();"/>
    <logic:notEmpty name="ec.com.smx.sic.sispe.responsable.local">
        <c:set var="ancho" value="80%"/>
        <c:set var="tope" value="30px"/>
        <c:set var="titulo" value="CONFIRMAR RESERVACI&Oacute;N PARA BODEGA"/>
        <c:set var="confirmarReservacionBodega" value="${estadoActivo}"/>
        <c:set var="ayudaBoton" value="Guardar reservaci&oacute;n"/>
        <script language="javascript">
            mostrarModal();
        </script>
    </logic:notEmpty>
</logic:equal>

<logic:equal name="ec.com.smx.sic.sispe.pedido.botonSI" value="siCambiarLocal">
    <c:set var="onclick_btn_NO" value="requestAjax('${accion}',['pregunta','local'],{parameters: 'botonNo=${valor_NO}', popWait:false});"/>
</logic:equal>
<logic:equal name="ec.com.smx.sic.sispe.pedido.botonSI" value="siRegCanasto">
    <c:set var="onclick_btn_NO" value="realizarEnvio('noRegCanasto');"/>
</logic:equal>
<logic:equal name="ec.com.smx.sic.sispe.pedido.botonSI" value="siImprimirPedido">
    <c:set var="onclick_btn_SI" value="enviarFormulario('${valor_SI}');hide(['popup']);"/>
    <c:set var="mostrarOpcionesImpresion" value="${estadoActivo}"/>
    <c:set var="ayudaBoton" value="Imprimir documento"/>
</logic:equal>
<logic:equal name="ec.com.smx.sic.sispe.pedido.botonSI" value="siGenerarPDF">
    <c:set var="onclick_btn_SI" value="enviarFormulario('${valor_SI}',0,false);hide(['popup']);ocultarModal();"/>
    <c:set var="mostrarOpcionesImpresion" value="${estadoActivo}"/>
    <c:set var="ayudaBoton" value="Generar pdf"/>
</logic:equal>
<div id="popup" class="popup" style="display:block;top:${tope}">
    <%--<bean:write name="onclick_btn_NO"/>--%>
    <div id="center" class="popupcontenido">
        <table border="0" align="center" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion" width="${ancho}">
            <tr>
                <td background="images/barralogin.gif" height="22px">
                    <table cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td class="textoBlanco11">
                                <b>&nbsp;&nbsp;${titulo}</b>
                            </td>
                            <td align="right">
                                <c:set var="btnCerrarPopup" value="${onclick_btn_NO}"/>
                                <logic:equal name="ec.com.smx.sic.sispe.pedido.botonSI" value="siRegCanasto">
                                    <c:set var="btnCerrarPopup" value="hide(['popup']);"/>
                                </logic:equal>
                                <div id="botonWin">
                                    <a href="#" class="linkBlanco8" onclick="${btnCerrarPopup};ocultarModal();">X</a>
                                </div>
                            </td>
                            <td width="3px"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
				<table border="0" align="center" cellspacing="0" cellpadding="5" bgcolor="#F4F5EB" class="tabla_informacion" width="100%">
				<tr><td>
                    <table class="fondoBlanco textoNegro11" border="0" width="100%" align="center">
                        <tr>
                            <td align="left" valign="top" colspan="2">
                                <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.mensajeConfirmacion">
                                    <bean:define name="ec.com.smx.sic.sispe.pedido.mensajeConfirmacion" id="mensajeConfirmacion"/>
                                    <bean:message key="${mensajeConfirmacion}"/>
                                </logic:notEmpty>
                            </td>
                        </tr>
                        <logic:equal name="mostrarOpcionesImpresion" value="${estadoActivo}">
                            <tr>
                                <td colspan="2">
                                    <table width="100%" cellpadding="1" cellspacing="0" class="tabla_informacion">
                                        <tr><td class="textoCafe10"><input type="checkbox" name="opEntregas" value="${estadoActivo}">&nbsp;<b>Detalle de las entregas</b></td></tr>
                                        <tr><td class="textoCafe10"><input type="checkbox" name="opCanastos" value="${estadoActivo}">&nbsp;<b>Detalle de los canastos</b></td></tr>
                                        <tr>
											<logic:empty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
												<logic:empty name="ec.com.smx.sic.sispe.pedido.descuentos">
													<td class="textoCafe10">
														<input type="checkbox" name="opDescuentos" value="${estadoActivo}">&nbsp;<b>Detalle de los descuentos</b>
													</td>
												</logic:empty>
												<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentos">
													<td class="textoCafe10">
														<input type="checkbox" name="opDescuentos" value="${estadoActivo}" checked="true">&nbsp;<b>Detalle de los descuentos</b>
													</td>
												</logic:notEmpty>
											</logic:empty>
											
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
												<logic:empty name="ec.com.smx.sic.sispe.descuentos.aplicadosCol">
													<td class="textoCafe10">
														<input type="checkbox" name="opDescuentos" value="${estadoActivo}">&nbsp;<b>Detalle de los descuentos</b>
													</td>
												</logic:empty>
												<logic:notEmpty name="ec.com.smx.sic.sispe.descuentos.aplicadosCol">
													<td class="textoCafe10">
														<input type="checkbox" name="opDescuentos" value="${estadoActivo}" checked="true">&nbsp;<b>Detalle de los descuentos</b>
													</td>
												</logic:notEmpty>
											</logic:notEmpty>
										</tr>
                                        <tr><td class="textoCafe10"><input type="checkbox" name="opDiferidos" value="${estadoActivo}">&nbsp;<b>Detalle de los diferidos</b></td></tr>
                                    </table>
                                </td>
                            </tr>
                        </logic:equal>
                        <tr>
                            <td valign="top" width="3%"><img src="images/pregunta24.gif" border="0"></td>
                            <td align="left">
                                <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preguntaConfirmacion">
                                    <bean:write name="ec.com.smx.sic.sispe.pedido.preguntaConfirmacion"/>
                                </logic:notEmpty>
                            </td>
                        </tr>
                    </table>
					</td><tr>
					<tr><td>
					<table cellpadding="0" cellspacing="0" align="center" width="50%">
						<c:choose>
							<c:when test="${confirmarReservacionBodega == estadoActivo || mostrarOpcionesImpresion == estadoActivo}">
								<td align="center">
									<div id="botonD">
										<html:link styleClass="aceptarD" title="${ayudaBoton}" href="#" onclick="${onclick_btn_SI}">Aceptar</html:link>
									</div>
								</td>
							</c:when>
							<c:otherwise>
								<td align="center">	
									<div id="botonDmin">
										<html:link styleClass="siDmin" href="#" onclick="${onclick_btn_SI} popWait('div_wait');">Si</html:link>
									</div>
								</td>
								<td align="center">
									<div id="botonDmin">
										<html:link styleClass="noDmin" href="#" onclick="${onclick_btn_NO};ocultarModal();">No</html:link>
									</div>
								</td>
							</c:otherwise>
						</c:choose>
					</table>
					</td><tr>
				</table>
                </td>
            </tr>
        </table>
    </div>
</div>