<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/mensajeria.tld" prefix="mensajeria"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<!-- Pantalla principal para el resumen del pedido -->
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<div id="pregunta">
	<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
		<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
		<tiles:put name="vtformAction" value="crearPedidoEspecial"/>
		</tiles:insert>
		<script language="javascript">mostrarModal();</script>
	</logic:notEmpty>
	<!-- PopUp de Redactar mail-->
	<logic:notEmpty name="ec.com.smx.sic.sispe.redactarMail">
		<tiles:insert page="/servicioCliente/redactarMail.jsp">
			<tiles:put name="vtformAction" value="envioMailEsp"/>
			<tiles:put name="vtformName" value="envioMailForm"/>
		</tiles:insert>
	</logic:notEmpty>
</div>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
    <tr>
        <td>
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                <tr>
                    <td align="center" valign="top">
                        <table border="0" class="textoNegro11" width="98%" align="center" cellpadding="0" cellspacing="0">
                            <%--------------------------- secci&oacute;n del resumen del pedido ---------------------%>
                            <tr><td height="5px"></td></tr>
                            <tr>
                                <td colspan="2" width="100%">	
                                    <%----------------------------- para que el reporte se despliegue en un nueva ventana ---------------%>
                                    <table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11 tabla_informacion">
                                        <tr><td height="5px"></td></tr>
                                        <tr>
                                            <logic:notEmpty name="ec.com.smx.sic.sispe.reservacion.responsable">
                                                <td align="left">
                                                    <label class="textoRojo11"><b>Entidad Responsable:</b></label>&nbsp;
                                                    <bean:write name="ec.com.smx.sic.sispe.pedido.responsable"/>
                                                </td>
                                            </logic:notEmpty>
                                            <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.pedidoDTO">
                                                <td align="right">
                                                    <b><label class="textoRojo11">N&uacute;mero de Pedido:&nbsp;</label></b><bean:write name="ec.com.smx.sic.sispe.pedido.pedidoDTO" property="id.codigoPedido"/>&nbsp;&nbsp;&nbsp;
                                                </td>
                                            </logic:notEmpty>
                                        </tr>
                                        <tr><td height="5px"></td></tr>
                                    </table>
                                    <%---------------------------------------------------------------------------------------------------%>
                                </td>
                            </tr>	
                            <tr>
								<td>
									<table border="0" width="100%" cellspacing="0" cellpadding="0" align="left" class="textoNegro11 tabla_informacion">											
										<!--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp -->
										<tiles:insert page="/contacto/cabeceraContactoFormulario.jsp">	
											<tiles:put name="tformName" value="crearPedidoEspecialForm"/>
											<tiles:put name="textoNegro" value="textoNegro11"/>
											<tiles:put name="textoAzul" value="textoAzul11"/>
										</tiles:insert>																																										
									</table>										
								</td>
							</tr>
    
                            <tr>
                                <td colspan="2" align="right">
                                    <table border="0" width="100%" class="textoNegro11 tabla_informacion" cellpadding="0" cellspacing="0">
                                        <tr><td height="5px"></td></tr>
                                        <tr>
                                            <td align="left" width="40%">
                                                <b>Elaborado en:&nbsp;</b><html:hidden name="${vformName}" property="localResponsable" write="true"/>
                                            </td>
                                        </tr>
                                        <tr><td height="5px"></td></tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td height="5px"></td></tr>
                            <tr>
                                <td colspan="2">                                	
									<!-- 
										@author Wladimir López	
								 		Todo el table de detalles, fué trasladado a 
										/pedidosEspeciales/crearcion/detalleResumenPedido.jsp
							 		-->
									<tiles:insert page="/controlesUsuario/controlTab.jsp"/>													
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td id="seccionImpresion">
                        <logic:equal name="ec.com.smx.sic.sispe.funcionImprimir" value="ok">
							<script language="JavaScript">
								imprimeSeleccion('rptEstadoPedidoEspTxt');
							</script>
                        </logic:equal>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<logic:notEmpty name="ec.com.smx.sic.sispe.envioMail">
	<bean:define id="valoresMail" name="ec.com.smx.sic.sispe.envioMail"/>
	<logic:empty name="valoresMail" property="cc">
		<mensajeria:enviarMail asunto="${valoresMail.asunto}" de="${valoresMail.de}" para="${valoresMail.para[0]}" host="${valoresMail.host}" puerto="${valoresMail.puerto}" 
		codigoCompania="${valoresMail.eventoDTO.id.companyId}" codigoSistema="${valoresMail.eventoDTO.id.systemId}"
		codigoEvento="${valoresMail.eventoDTO.id.codigoEvento}" reemplazarRemitente="${valoresMail.reemplazarRemitente}">
			<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro10">
				<tr>
					<td>
						<bean:write name="ec.com.smx.sic.sispe.textoMail" filter="false"/>
					</td>
				</tr>
			</table>
			<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/envioCotizacionMail.jsp" flush="false"/>
		</mensajeria:enviarMail>
	</logic:empty>
	<logic:notEmpty name="valoresMail" property="cc">
		<mensajeria:enviarMail asunto="${valoresMail.asunto}" de="${valoresMail.de}" para="${valoresMail.para[0]}" host="${valoresMail.host}" puerto="${valoresMail.puerto}" 
		codigoCompania="${valoresMail.eventoDTO.id.companyId}" codigoSistema="${valoresMail.eventoDTO.id.systemId}" cc="${valoresMail.cc[0]}"
		codigoEvento="${valoresMail.eventoDTO.id.codigoEvento}" reemplazarRemitente="${valoresMail.reemplazarRemitente}">
			<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro10">
				<tr>
					<td>
						<bean:write name="ec.com.smx.sic.sispe.textoMail" filter="false"/>
					</td>
				</tr>
			</table>
			<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/envioCotizacionMail.jsp" flush="false"/>
		</mensajeria:enviarMail>
	</logic:notEmpty>
</logic:notEmpty>
