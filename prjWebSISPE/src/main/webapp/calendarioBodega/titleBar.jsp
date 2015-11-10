<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<tiles:useAttribute id="vformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>

<table cellspacing="3" cellpadding="0">
	<tr>
		<td><div id="div_mensajes_inactividad" style="font-size:15px;"></div></td>
		<logic:notEmpty name="ec.com.smx.sic.sispe.usuarioAccesoCalendario">
			<td><div id="botonA"><a href="#" class="imprimirDiaHoyA" onclick="realizarEnvio('imprimirDiaHoy');">Imprimir</a></div></td>
			<td><div id="botonA"><a href="#" class="imprimirDiaSelA" onclick="realizarEnvio('imprimirDiaSel');">Imprimir</a></div></td>
			<td><div id="botonA"><a href="#" class="actualizarA" onclick="actualizarPantalla();">Actualizar</a></div></td>
		</logic:notEmpty>
        <td><div id="botonA"><html:link styleClass="inicioA" action="menuPrincipal.do">Inicio</html:link></div></td>
	</tr>
</table>