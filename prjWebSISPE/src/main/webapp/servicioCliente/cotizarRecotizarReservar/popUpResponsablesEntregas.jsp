<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<table border="0" cellspacing="0" cellpadding="0" width="100%"  align="right">
	<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
	<tr>
		<td valign="top">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion" align="center">
				<tr>
					<td>
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr class="tituloTablas" height="18px">
								<td align="center" width="4%" >No</td>
								<td align="center" width="14%" >C&oacute;digo barras</td>
								<td align="center" width="18%">Art&iacute;culo</td>
								<td align="center" width="10%">Cantidad despacho</td>
								<td align="center" width="12%">Resp. pedido</td>
								<td align="center" width="12%">Resp. despacho</td>
								<td align="center" width="12%">Resp. entrega</td>
								<td align="center" width="8%">F.despacho</td>
								<td align="center" width="8%">F.entrega</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<div id="detalleRes" style="width:100%;height:200px;overflow:auto;border-bottom:1px solid #cccccc">
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
								<logic:iterate name="ec.com.smx.sic.sispe.responsables.entregas" id="estructuraResEnt" indexId="indiceRes">
									<bean:define id="fila" value="${indiceRes + 1}"/>
									<bean:define id="residuo" value="${indiceRes % 2}"/>
									<logic:equal name="residuo" value="0">
										<bean:define id="colorBack" value="blanco10"/>
									</logic:equal>
									<logic:notEqual name="residuo" value="0">
										<bean:define id="colorBack" value="grisClaro10"/>
									</logic:notEqual>
									<tr  class="${colorBack}" valign="middle" height="30px">
										<td  class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="fila"/></td>
										<td  class="columna_contenido fila_contenido" width="14%" align="left"><bean:write name="estructuraResEnt" property="detallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
										<td  class="columna_contenido fila_contenido" width="18%" align="left"><bean:write name="estructuraResEnt" property="detallePedidoDTO.articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="estructuraResEnt" property="detallePedidoDTO.articuloDTO.articuloMedidaDTO.referenciaMedida"/></td>
										<td  class="columna_contenido fila_contenido" width="10%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${estructuraResEnt.entregaDTO.cantidadDespacho}"/></td>
										<td  class="columna_contenido fila_contenido" width="12%" align="right"><bean:write name="estructuraResEnt" property="responsablePedido" /></td>
										<td  class="columna_contenido fila_contenido" width="12%" align="right"><bean:write name="estructuraResEnt" property="responsableDespacho" /></td>
										<td  class="columna_contenido fila_contenido" width="12%" align="center"><bean:write name="estructuraResEnt" property="responsableEntrega"/></td>
										<td  class="columna_contenido fila_contenido" width="8%" align="center"><bean:write name="estructuraResEnt" property="entregaDTO.fechaDespachoBodega" formatKey="formatos.fecha" /></td>
										<td  class="columna_contenido fila_contenido" width="8%" align="center"><bean:write name="estructuraResEnt" property="entregaDTO.fechaEntregaCliente" formatKey="formatos.fecha" /></td>
									</tr>						
								</logic:iterate>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td height="15px"></td></tr>
	<tr>
		<td align="center">
			<div id="botonD">
				<html:link styleClass="cancelarD" action="crearCotizacion.do?cancelarResponsables=ok">Cancelar</html:link>
			</div>
		</td>
	</tr>
</table>			