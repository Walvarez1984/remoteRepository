<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>

<html>
	<head>
		<meta http-equiv="Content-Style-Type" content="text/css"/>
		<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
		<meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
		<meta HTTP-EQUIV="max-age" CONTENT="0"/>
		<meta HTTP-EQUIV="Expires" CONTENT="0"/>

		<style type="text/css">
			.nombreCompania{
			font-size: 20px;
			font-family: Verdana, Arial, Helvetica;
			color: #FF0033;
			font-weight: bolder
			}

			.borde{
			border: thin solid #999999;
			}

			.titulo{
			font-size: 15px;
			font-family: Verdana, Arial, Helvetica;
			color: #000000;
			font-weight: bolder
			}

			.tituloTablas{
			background-color:#333333;
			color: #FFFFFF;
			font-size: 9px;
			vertical-align:middle;
			font-family: Verdana, Arial, Helvetica;
			font-weight: bold;
			border: thin solid #999999;
			}

			.textoNegro10{
			font-size: 10px;
			font-family: Verdana, Arial, Helvetica;
			color: #000000;
			}

			.textoNegro9{
			font-family: Verdana, Arial, Helvetica;
			font-size: 9px;
			color: #000000;
			}

			.tituloTablasCeleste{
			background-color:#DDEEFF;
			color: #000000;
			font-size: 9px;
			font-style: normal;
			line-height: normal;
			font-family: Verdana;
			font-weight: bolder;
			border: thin solid #999999;
			}

			.tituloTablasVerde{
				background-color:#ccffcc;
				color: #000000;
				font-size: 9px;
				font-style: normal;
				line-height: normal;
				font-family: Verdana;
				font-weight: bolder;
				border: thin solid #999999;
			}

			.grisClaro {
			background-color: #eaeaea;
			font-family: Verdana;
			font-size: 11px;
			color: #000000;
			border: thin solid #999999;
			border: thin solid #999999;
			}

			.blanco {
			font-family: Verdana;
			font-size: 11px;
			color: #ffffff;
			}

			.amarilloClaro10 {
			background-color: #ffffca;
			font-family: Verdana;
			font-size: 9px;
			color: #000000;
			border: thin solid #999999;
			font-weight: bolder
			}
		</style>
	</head>
	<body>
		<table border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="nombreCompania" colspan="5">
					CORPORACI&Oacute;N FAVORITA C.A.
				</td>
			</tr>
			<tr >
				<td class="titulo" colspan="5">Reporte de Artículos Reservados</td>
			</tr>
			<%--Datos de Despachos--%>
			<logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol">
				<tr><td height="6"></td></tr>
				</tr>
					 <td align="center" class="tituloTablas" colspan="2">C&oacute;digo barras</td>
					 <td align="center" class="tituloTablas" colspan="2">Descripci&oacute;n art.</td>
					 <td align="left" class="tituloTablas" colspan="2">
					 	<table border="0" cellspacing="0" cellpadding="1" width="100%">
					 		<tr>
					 			<td align="center" colspan="3" class="tituloTablas">Cant. Reservada</td>
					 		</tr>
					 		<tr>
					 			<td colspan="2">
					 				<table border="0" cellspacing="0" cellpadding="1" width="100%">
					 					<tr>
					 						<td align="center" colspan="2" class="tituloTablas">Solicitada a CD</td>
					 					</tr>
					 					<tr>
					 						<td class="tituloTablas">
					 							Enviada
					 						</td>
					 						<td class="tituloTablas">
					 							No Enviada
					 						</td>
					 					</tr>
					 				</table>
					 			</td>
					 			<td class="tituloTablas">No Solicitada a CD</td>
					 		</tr>
					 	</table>
					 </td>
					 <td align="center" class="tituloTablas" colspan="1">Producido</td>
					 <td align="center" class="tituloTablas" colspan="1">Despachado</td>
					 <td align="center" class="tituloTablas" colspan="1">Stock</td>
			   	</tr>
				<logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol" id="vistaReporteGeneralDTO">
						<tr class="textoNegro10">
							<td class="grisClaro" align="left" colspan="2">&nbsp;
								<bean:write name="vistaReporteGeneralDTO" property="codigoBarras"/>
							</td>
							<td class="grisClaro" align="left" colspan="2">
								<bean:write name="vistaReporteGeneralDTO" property="descripcionArticulo"/>
							</td>
							<td class="grisClaro" align="left" colspan="1">
								<table>
									<tr>
										<td class="grisClaro">
											<bean:write name="vistaReporteGeneralDTO" property="cantidadEnviado"/>
										</td>
										<td class="grisClaro">
											<bean:write name="vistaReporteGeneralDTO" property="cantidadNoEnviado"/>
										</td>
									</tr>
								</table>
							</td>
							<td class="grisClaro" align="center" colspan="1">
								${vistaReporteGeneralDTO.cantidadEntregarTotal-vistaReporteGeneralDTO.cantidadDespacharTotal}
							</td>
							<td class="grisClaro" align="left" colspan="1">
								<bean:write name="vistaReporteGeneralDTO" property="cantidadParcialProTotal"/>/<bean:write name="vistaReporteGeneralDTO" property="cantidadEnviado"/>
							</td>
							<td class="grisClaro" align="left" colspan="1">
								<bean:write name="vistaReporteGeneralDTO" property="cantidadParcialDesTotal"/>/<bean:write name="vistaReporteGeneralDTO" property="cantidadEnviado"/>
							</td>
							<td class="grisClaro" align="left" colspan="1">
								<%--<logic:notEmpty name="vistaReporteGeneralDTO" property="npStockArticulo">--%>
									<bean:write name="vistaReporteGeneralDTO" property="npStockArticulo"/>
								<%--</logic:notEmpty>
								<logic:empty name="vistaReporteGeneralDTO" property="npStockArticulo">
								</logic:empty>--%>
							</td>
						</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
	</body>
</html>