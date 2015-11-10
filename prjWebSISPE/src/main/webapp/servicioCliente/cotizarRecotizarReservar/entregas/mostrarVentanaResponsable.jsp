<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<table class="fondoBlanco textoNegro11" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
	<tr>
		<td height="7px"></td> 
	</tr>
	<tr>
		<td  width="10%" align="left" height="20px">
			<b>&nbsp;C&oacute;digo:</b> 
		</td>
		<td  width="10%" align="left" height="20px">
			<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="detallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/>
		</td>
	</tr>
	<tr>
		<td  width="10%" align="left" height="20px">
			<b>&nbsp;Art&iacute;culo:</b>
		</td>
		<td  width="10%" align="left" height="20px">
			<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="detallePedidoDTO.articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="detallePedidoDTO.articuloDTO.articuloMedidaDTO.referenciaMedida"/>
		</td>
	</tr>
	<tr>	
		<td  width="10%" align="left" height="20px">
			<b>&nbsp;Cantidad entrega:</b>
		</td>
		<td  width="10%" align="left" height="20px">
			<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="npCantidadEntrega"/>
		</td>
	</tr>
	<tr>
		<td  width="10%" align="left" height="20px">
			<b>&nbsp;Responsable pedido:</b> 
		</td>
		<td  width="10%" align="left" height="20px">
			<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="responsablePedido"/> 
		</td>
	</tr>
	<tr>
		<td  width="10%" align="left" height="20px">
			<b>&nbsp;Responsable producci&oacute;n:</b>
		</td>
		<td  width="10%" align="left" height="20px">
			<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="responsableProduccion"/> 
		</td>
	</tr>
	<tr>
		<td  width="10%" align="left" height="20px">
			<b>&nbsp;Responsable despacho:</b>
		</td>
		<td  width="10%" align="left" height="20px">
			<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="responsableDespacho"/> 
		</td>
	</tr>
	<tr>
		<td  width="10%" align="left" height="20px">
			<b>&nbsp;Responsable entrega:</b>
		</td>
		<td  width="10%" align="left" height="20px">
			<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="responsableEntrega"/> 
		</td>
	</tr>
	<tr>
		<td  width="10%" align="left" height="20px">
			<b>&nbsp;Fecha despacho:</b>
		</td>
		<td  width="10%" align="left" height="20px">
			<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="entregaPedidoDTO.fechaDespachoBodega" formatKey="formatos.fecha"/>
		</td>
	</tr>
	<tr>
		<td  width="10%" align="left" height="20px">
			<b>&nbsp;Fecha entrega:</b>
		</td>
		<td  width="10%" align="left" height="20px">
			<bean:write name="ec.com.smx.sic.sispe.estructuraResponsable" property="entregaPedidoDTO.fechaEntregaCliente" formatKey="formatos.fecha"/>
		</td>
	</tr>	
</table>