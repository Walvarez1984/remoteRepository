<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
	
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true" />
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true" />

<div>
	<table class="fondoBlanco textoNegro11" border="0" width="100%" cellpadding="0" cellspacing="5">
		<tr>
			<td align="left">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td colspan="2"><tiles:insert page="/include/mensajes.jsp" /></td>
					</tr>					
					<tr>
						<table cellspacing="0" cellpadding="0" width="98%" align="left">
							<tr class="tituloTablas">
								<td width="10%" align="center" class="columna_contenido" rowspan="2">No</td>
								<td width="30%" align="center" class="columna_contenido" rowspan="2">C&oacute;digo barras</td>
								<td width="40%" align="center" class="columna_contenido" rowspan="2">Descripci&oacute;n</td>
								<td width="10%" align="center" class="columna_contenido" rowspan="2">Cantidad anterior</td>
								<td width="10%" align="center" class="columna_contenido" rowspan="2">Cantidad actual</td>
							</tr>
						</table>
					</tr>
					<tr>
						<td>
							<div id="div_listado" style="width: 100%; height: 200px; overflow: auto">
								<table cellspacing="0" cellpadding="1" width="98%" align="left" border="0"><%-- comienza la iteracion del detalle del pedido --%>									
									<logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detalle" indexId="numeroRegistro">
										<bean:define name="detalle" property="articuloDTO" id="articulo" />
										<bean:define name="detalle" property="estadoDetallePedidoDTO" id="estadoDetallePedido" />
										<tr>
											<td width="10%" class="columna_contenido fila_contenido" align="center">${numeroRegistro + 1}</td>
											<td width="30%" class="columna_contenido fila_contenido" align="center">
												<bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras" />
											</td>
											<td width="40%" class="columna_contenido fila_contenido" align="center">
												<bean:write name="articulo" property="descripcionArticulo" />
											</td>
											<td width="10%" class="columna_contenido fila_contenido" align="center">
												&nbsp;<bean:write name="detalle" property="npCantidadAnterior" />
											</td>
											<td width="10%" class="columna_contenido fila_contenido" align="center">
												<bean:write name="estadoDetallePedido" property="cantidadEstado" />
											</td>											
										</tr>
									</logic:iterate><%-- termina la iteracion del detalle del pedido --%>									
								</table>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>