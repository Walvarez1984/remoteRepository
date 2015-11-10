<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">

<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="GENERATOR" content="IBM Software Development Platform">
		<script language="JavaScript" src="js/util/util.js" type="text/javascript"></script>
    	<script language="JavaScript" src="js/prototype.js" type="text/javascript"></script>
    	<script language="JavaScript" src="js/ajax.js" type="text/javascript"></script>
		<meta http-equiv="Content-Style-Type" content="text/css">
		<link href="css/textos.css" rel="stylesheet" type="text/css">
		<link href="css/componentes.css" rel="stylesheet" type="text/css">
		<link href="css/estilosBotones.css" rel="stylesheet" type="text/css">
		<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
		<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<meta HTTP-EQUIV="max-age" CONTENT="0">
		<meta HTTP-EQUIV="Expires" CONTENT="0">
		<title>
			SISPE - <bean:write name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO" property="descripcionArticulo"/>
		</title>
	</head>
	<body>
		<%-- se obtiene la acción actual --%>
		<%--<bean:define id="accionActual" name="ec.com.smx.sic.sispe.accion"/>
		<bean:define id="accionEstadoPedido"><bean:message key="ec.com.smx.sic.sispe.accion.estadoPedido"/></bean:define>--%>
		
		<html:form action="estadoDetalleCanasto" method="post">
		<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
		<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
			<tr>
				<td width="50%" colspan="2" valign="top" style="background:url(./images/rojos.gif)">
					<!-- TEXTO EMPRESA Y SISTEMA -->
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td height="35px"><img src="./images/super.gif" ></td>
						</tr>
						<tr>
							<td class="WhiteSystem">
								<bean:define id="currentSystem" name="multicompany.currentSystem"/>
								<b>&nbsp;<bean:write name="currentSystem" property="systemName"/></b>
							</td>
							<td class="WhiteMiddle" align="right" valign="top">
								<b>
									Bienvenido: &nbsp;
									<logic:notEmpty name="security.currenLogonUser">
										<bean:define id="currenLogonUser" name="security.currenLogonUser"/>
										<bean:write name="currenLogonUser" property="userCompleteName"/>
									</logic:notEmpty>
									<logic:empty name="security.currenLogonUser">Visitante.</logic:empty>
								</b>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#fe7279" height="3"></td>
			</tr>
			<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
			<tr>
				<td align="left" valign="top" width="100%">
					<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
						<tr>
							<td  width="3%" align="center"><img src="images/detalle_canasto.gif" border="0"></img></td>
							<td height="35" valign="middle">Detalle del canasto</td>
							<td align="right">
								<table border="0">
									<tr>
										<td>
											<div id="botonA">
												<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO">
													<bean:define id ="canasto" name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO"/>
													<logic:equal name="canasto" property="articuloDTO.codigoClasificacion" value="${articuloEspecial}">
														<html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 0, false);">Crear XLS</html:link>
													</logic:equal>
												</logic:notEmpty>
											</div>
										</td>
										<td>
											<bean:define id="bandera" value="${ORIGEN_DETALLE_CANASTA}"/>
											<div id="botonA">
												<html:link action="estadoDetalleCanasto" paramId="atras" paramName="bandera" styleClass="atrasA">
													Atras
												</html:link>
											</div>
										</td>
										
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="center" valign="top">
					<tiles:insert page="/servicioCliente/estadoPedido/detalleCanastaComun.jsp"/>
				</td>
			</tr>
		</table>
		</html:form>
	</body>
</html>