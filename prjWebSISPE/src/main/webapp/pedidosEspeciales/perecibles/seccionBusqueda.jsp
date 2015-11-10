<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName" name="vtformName"  classname="java.lang.String" ignore="true"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
				<tr>
					<td  class="fila_titulo" height="25px" colspan="3">
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
							<tr>
								<td width="30px" align="right"><img src="./images/especiales.gif" border="0"></td>
								<td width="100%" class="textoNegro11">&nbsp;Tipo de pedido</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr><td height="5px"></td></tr>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedidos.especiales">
					<tr>
						<td>
							<table cellspacing="0" cellpadding="0" align="left" width="100%">
								<logic:iterate name="ec.com.smx.sic.sispe.pedidos.especiales" id="especialDTO" indexId="indicePedido">
									<tr>
										<td align="left" class="textoAzul11">
											<logic:empty name="ec.com.smx.sic.sispe.ingresoDesdeBusqueda">
												<html:radio property="opTipoPedido" value="${especialDTO.tipoPedidoDTO.id.codigoTipoPedido}" onclick="requestAjax('crearPedidoEspecial.do',['resultadosBusqueda','mensajes','div_tipoPedido','divTabs','div_pagina'],{parameters:'indicePedido=${indicePedido}',evalScripts: true});"><b><bean:write name="especialDTO" property="descripcionEspecial"/></b></html:radio>
											</logic:empty>
											<logic:notEmpty name="ec.com.smx.sic.sispe.ingresoDesdeBusqueda">
												<li><b><bean:write name="especialDTO" property="descripcionEspecial"/></b></li>
											</logic:notEmpty>
										</td>
									</tr>
								</logic:iterate>
							</table>
						</td>
					</tr>
				</logic:notEmpty>
				<tr><td height="5px"></td></tr>
			</table>
		</td>
	</tr>
		<tr><td height="10px"></td></tr>
		<tr>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
					<tr>
						<td class="fila_titulo" height="25px" colspan="2">
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<tr>
									<td width="30px" align="right"><img src="./images/buscar24.gif" border="0"></td>
									<td width="166" class="textoNegro11">&nbsp;B&uacute;squeda</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="6px"></td></tr>
					<tr>
						<td>
							<table cellspacing="0" cellpadding="0" align="left" width="100%">
								<tr>
									<td align="left" class="textoAzul9" onclick="chequear(${vformName}.opTipoBusqueda[0]);${vformName}.textoBusqueda.focus();">
										<html:radio property="opTipoBusqueda" value="${opCodigoClasificacion}" onclick="${vformName}.textoBusqueda.focus();">Cod. clasificaci&oacute;n</html:radio>
									</td>
								</tr>
								<tr>
									<td align="left" class="textoAzul9" onclick="chequear(${vformName}.opTipoBusqueda[1]);${vformName}.textoBusqueda.focus();">
										<html:radio property="opTipoBusqueda" value="${opNombreClasificacion}" onclick="document.forms[0].textoBusqueda.focus()">Desc. clasificaci&oacute;n</html:radio>
									</td>
								</tr>
								<tr>
									<td align="left" class="textoAzul9" onclick="chequear(${vformName}.opTipoBusqueda[2]);${vformName}.textoBusqueda.focus();">
										<html:radio property="opTipoBusqueda" value="${opNombreArticulo}" onclick="document.forms[0].textoBusqueda.focus()">Art&iacute;culo</html:radio>
									</td>
								</tr>
								<tr>
									<td align="left"><html:text property="textoBusqueda" size="24" styleClass="textNormal" onkeypress="resultadosBusquedaENTER('buscarArticulo.do','WIN_RBUS',['opTipoBusqueda','textoBusqueda'], 'buscarArtPed');requestAjaxEnter('crearPedidoEspecial.do',['mensajes','detallePedido','divTabs','tabPedidoEspecial'],{parameters: 'actualizarDetalle=ok', evalScripts: true});"/></td>
								</tr>
							</table>
						</td>
						<td width="4px">&nbsp;</td>
					</tr>
					<tr><td height="10px" colspan="2"></td></tr>
					<tr>
						<td align="right"><div id="botonD"><html:link href="#" styleClass="buscarD" onclick="resultadosBusqueda('buscarArticulo.do', 'WIN_RBUS', ['opTipoBusqueda','textoBusqueda'], 'buscarArtPed');requestAjax('crearPedidoEspecial.do',['mensajes','detallePedido','divTabs','tabPedidoEspecial'],{parameters: 'actualizarDetalle=ok', evalScripts: true});">Buscar</html:link></div></td>
						<td width="2px"></td>
					</tr>
					<tr><td height="5px" colspan="2"></td></tr>
				</table>
			</td>
		</tr>
		<tr><td height="10px"></td></tr>
		<tr>
			<td>
				<table width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion">
					<tr class="fila_titulo" height="25px">
						<td>
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<tr>
									<td width="20%"><img src="./images/datos_informacion24.gif" border="0"></td>
									<td align="left" class="textoNegro11">Informaci&oacute;n</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="6px"></td></tr>
					<tr>
						<td>
							<table cellspacing="0" cellpadding="1" align="left" width="100%">
								<tr>
									<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoClaro10" width="100%" height="12px"></td></tr></table></td>
									<td align="left" class="textoAzul10">&nbsp;Sin alcance</td>
								</tr>
								<tr>
									<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoObsuro10" width="100%" height="12px"></td></tr></table></td>
									<td align="left" class="textoAzul10">&nbsp;Error de datos</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="5px" colspan="2"></td></tr>
				</table>
			</td>
		</tr>
	</table>
	<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.autorizacion.existe">
		<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
			<tr>
				<td class="fila_titulo" height="25px" colspan="2">
					<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
						<tr>
							<td width="30px" align="right"><img src="./images/autorizacion.gif" border="0"></td>
							<td width="166" class="textoNegro12">&nbsp;Autorizaci&oacute;n</td>
							<td width="5"></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr><td height="6px"></td></tr>
			<tr>
				<td>
					<tiles:insert page="/servicioCliente/autorizacion/ingresoAutorizacion.jsp"/>
				</td>
			</tr>
		</table>
	</logic:notEmpty>
	<table>
		<tr><td height="4px"/></tr>
	</table>
	
</table>