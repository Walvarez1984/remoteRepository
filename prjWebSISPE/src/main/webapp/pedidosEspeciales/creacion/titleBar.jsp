<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>
<bean:define id="anulado"><bean:message key="ec.com.smx.sic.sispe.estado.anulado"/></bean:define>
<bean:define id="cotCaducada"><bean:message key="ec.com.smx.sic.sispe.estado.cotizacionCaducada"/></bean:define>
<bean:define id="devolucion"><bean:message key="ec.com.smx.sic.sispe.estado.devolucion"/></bean:define>
<table cellspacing="0" cellpadding="1">
    <tr>
        <logic:empty name="ec.com.smx.sic.resumen.visible">
            <td id="botones">
                <table border="0" cellpadding="1" cellspacing="0">
                    <tr>
                        <logic:notEmpty  name="ec.com.smx.sic.fechas.visible">
                            <td>
                                <div id="botonA">
                                    <%-- <html:link styleClass="guardarA" href="#" onclick="realizarEnvio('regPedido');" title="guardar formulario">Guardar</html:link> --%>
									<html:link href="#" styleClass="guardarA" onclick="requestAjax('crearPedidoEspecial.do',['divTabs','div_confirmarPedido','mensajes','seccion_detalle','div_datosCliente'],{parameters: 'grabarPedido=ok&ayuda=regPedido', evalScripts: true});" title="guardar formulario">Guardar</html:link>
                                </div>
                            </td>
                        </logic:notEmpty>
                        <logic:empty  name="ec.com.smx.sic.fechas.visible">
                            <td>
                                <div id="botonA">
                                    <%--<html:link styleClass="guardarA" href="#" onclick="realizarEnvio('regPedido');" title="guardar formulario">Guardar</html:link>--%>
                                    <html:link href="#" styleClass="guardarA" onclick="requestAjax('crearPedidoEspecial.do',['divTabs','div_confirmarPedido','mensajes','seccion_detalle','div_datosCliente'],{parameters: 'grabarPedido=ok&ayuda=regPedido', evalScripts: true});" title="guardar formulario">Guardar</html:link>
                                </div>
                            </td>
                        </logic:empty>
                        <%-- 
                        <td>
                          <div id="botonA">
                          	<html:link href="#" styleClass="guardarA" onclick="realizarEnvio('guardar');" title="guardar formulario">Guardar</html:link>
                          </div>
                        </td>
                        --%>
                    </tr>
                </table>
            </td>
            <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.sinConfirmacion">
                <td>
                    <div id="botonA">
                        <html:link href="#" styleClass="cancelarA" onclick="requestAjax('crearPedidoEspecial.do',['pregunta'],{parameters: 'volverBuscar=ok'});" title="volver a la pantalla de b&uacute;squeda">Cancelar</html:link>
                    </div>
                </td>
            </logic:notEmpty>
        </logic:empty>
        <logic:notEmpty name="ec.com.smx.sic.resumen.visible">
            <%--
	    	<td>
                <div id="botonA">
                    <html:link href="#" styleClass="pdfA" onclick="realizarEnvioPDF('confirmarPDF');">Crear PDF</html:link>
                </div>
            </td>
            --%>
            <bean:define id="vistaPedido" name="ec.com.smx.sic.sispe.vistaPedido"/>
            <bean:define id="rolesMail" name="ec.com.smx.sic.sispe.rolesMail"/>
			<bean:define id="rolActual" name="sispe.vistaEntidadResponsableDTO"/>
			<c:if test="${vistaPedido.id.codigoEstado!=anulado && vistaPedido.id.codigoEstado!=cotCaducada && vistaPedido.id.codigoEstado!=devolucion && fn:contains(rolesMail,rolActual.id.idRol)}">
	            <td>
					<div id="botonA">
						<html:link href="#" styleClass="enviar_mailA" title="Envía un mail con el pedido al cliente" onclick="requestAjax('crearPedidoEspecial.do',['div_pagina','pregunta'],{parameters: 'redactarMail=ok', evalScripts: true});">Env&iacute;o mail</html:link>
					</div>
				</td>
			</c:if>
            <td>
                <div id="botonA">
                    <html:link href="#" styleClass="imprimirA" onclick="requestAjax('crearPedidoEspecial.do',['mensajes','seccionImpresion'],{parameters: 'confirmarImpresionTexto=ok',evalScripts:true});">Imprimir</html:link>
                </div>
            </td>
        </logic:notEmpty>
        <td>
            <div id="botonA">
                <html:link styleClass="inicioA" action="menuPrincipal.do" title="ir al men&uacute; principal">Inicio</html:link>
            </div>
        </td>	
    </tr>
</table>