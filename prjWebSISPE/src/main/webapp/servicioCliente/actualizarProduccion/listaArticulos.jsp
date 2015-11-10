<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<table border="0" class="tabla_informacion_negro" width="98%" align="center" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="6">
			<table border="0" cellspacing="0" cellpadding="1" width="100%" class="tabla_informacion_negro">
				<tr class="tituloTablas"  align="left">
					<td class="tituloTablasCeleste" width="15%" align="center">C&Oacute;DIGO BARRAS</td>
					<td class="tituloTablasCeleste" width="67%" align="center">ART&Iacute;CULO</td>
					<td class="tituloTablasCeleste" width="18%" align="center">CANT. PRODUCIR</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" cellspacing="0" cellpadding="1" width="100%">
				<logic:iterate name="articulos" id="articuloDTO" indexId="indiceDetalleArticulos">
					<%-- control del estilo para el color de las filas--%>
					<bean:define id="residuo" value="${indiceDetalleArticulos % 2}"/>
					<logic:equal name="residuo" value="0">
						<bean:define id="clase" value="blanco10"/>
					</logic:equal>
					<logic:notEqual name="residuo" value="0">
						<bean:define id="clase" value="naranjaClaro10"/>
					</logic:notEqual>
					<bean:define id="contador" value="${contador+1}" toScope="session"/>
					<tr class="${clase}" id="fila_${indiceDetalleArticulos}"> 
						<td class="columna_contenido fila_contenido" width="15%" align="left"><bean:write name="articuloDTO" property="codigoBarrasActivo.id.codigoBarras"/></td>
						<td class="columna_contenido fila_contenido" width="63%" align="left"><bean:write name="articuloDTO" property="descripcionArticulo"/></td>
						<td class="columna_contenido fila_contenido columna_contenido_der" width="18%" align="center">
							<html:text property="cantidadProducida" value="${articuloRelacion.npCantidadAProducir}" styleClass="textNormal" size="7" maxlength="5"/>
						</td>
					</tr>
			   </logic:iterate>
			</table>
		</td>
	</tr>
</table>