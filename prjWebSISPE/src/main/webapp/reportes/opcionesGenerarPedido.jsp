<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<bean:define name="sispe.estado.activo" id="estadoActivo"/>
<table width="100%" cellpadding="1" cellspacing="0" class="tabla_informacion">
	<tr><td class="textoCafe10"><input type="checkbox" name="opEntregas" value="${estadoActivo}">&nbsp;<b>Detalle de las entregas</b></td></tr>
	<tr><td class="textoCafe10"><input type="checkbox" name="opCanastos" value="${estadoActivo}">&nbsp;<b>Detalle de los canastos</b></td></tr>
	<tr>
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
	</tr>
</table>