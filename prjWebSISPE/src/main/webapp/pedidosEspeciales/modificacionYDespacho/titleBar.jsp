<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>

<table cellspacing="0" cellpadding="0">
    <tr>
        <td id="barraBotones">
            <table cellspacing="3" cellpadding="0">
                <tr>
                    <logic:notEmpty name="ec.com.smx.sic.sispe.pedidoEspecial.tipoPedidoUsuario">
	                    <logic:equal name="vformAction" value="controlProduccionPedEsp">
		                    <td>
								<div id="botonA">
									<html:link href="#" styleClass="excelA" 
										onclick="requestAjax('controlProduccionPedEsp.do',['mensajes','pregunta','mensajeDespacho'],{parameters: 'reporteDespachos=ok', evalScripts: true});">
										Crear XLS
									</html:link>
								</div>
							</td>
	                        <logic:empty name="ec.com.smx.sic.sispe.habilitaDespacho">
	                            <td><div id="botonA"><html:link styleClass="cierreDiaA" href="#" title="Cerrar d&iacute;a" onclick="requestAjax('controlProduccionPedEsp.do',['mensajes','barraBotones','pregunta','mensajeDespacho','resultadosBusqueda'],{parameters: 'cerrarDia=ok', evalScripts: true});">Cerrar día</html:link></div></td>
	                           <%--<td><div id="botonAlock"><html:link styleClass="despachoAlock" href="#" title="despachar pedidos" onclick="">Despachar</html:link></div></td>--%>
	                        </logic:empty>
	                        <logic:notEmpty name="ec.com.smx.sic.sispe.habilitaDespacho">
	                            <td><div id="botonAlock"><html:link styleClass="cierreDiaAlock" href="#" title="Cerrar d&iacute;a" onclick="">Cerrar Día</html:link></div></td>
	                         <%--   <td><div id="botonA"><html:link styleClass="despachoA" href="#" title="despachar pedidos" onclick="requestAjax('controlProduccionPedEsp.do',['resultadosBusqueda','mensajes','pregunta','mostrarInfoCerrarDia' ],{parameters: 'botonProducir=ok', evalScripts: true});">Despachar</html:link></div></td>--%>
	                        </logic:notEmpty>
	                        <td><div id="botonA"><html:link styleClass="imprimirA" href="#" title="imprimir reporte" onclick="requestAjax('controlProduccionPedEsp.do',['resultadosBusqueda','mensajes','pregunta' ],{parameters: 'crearReporte=ok', evalScripts: true});">Imprimir</html:link></div></td>
	                    </logic:equal>
                    </logic:notEmpty>
                    <td><div id="botonA"><html:link styleClass="inicioA" action="menuPrincipal.do">Inicio</html:link></div></td>
                </tr>
            </table>
        </td>
    </tr>
</table>