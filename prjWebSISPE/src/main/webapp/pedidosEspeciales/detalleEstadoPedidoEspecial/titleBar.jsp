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

<table cellspacing="3" cellpadding="0">
<html:form action="detalleEstadoPedidoEspecial" method="post">
 <input type="hidden" name="ayuda" value="">  
    <tr>		
        <logic:equal name="vformAction" value="detalleEstadoPedidoEspecial">
            <%--<td><div id="botonA"><html:link styleClass="pdfA" action="detalleEstadoPedidoEspecial.do?confirmarPDF=ok'});">Crear PDF</html:link></div></td>--%>
            <!-- <td><div id="botonA"><html:link href="#" onclick="requestAjax('detalleEstadoPedidoEspecial.do',{parameters: 'confirmarImpresionTexto=ok'});" styleClass="imprimirA" >Imprimir</html:link></div></td>-->
            <bean:define id="vistaPedido" name="ec.com.smx.sic.sispe.vistaPedido"/>
            <bean:define id="rolesMail" name="ec.com.smx.sic.sispe.rolesMail"/>
			<bean:define id="rolActual" name="sispe.vistaEntidadResponsableDTO"/>
			<c:if test="${vistaPedido.id.codigoEstado!=anulado && vistaPedido.id.codigoEstado!=cotCaducada && vistaPedido.id.codigoEstado!=devolucion && fn:contains(rolesMail,rolActual.id.idRol)}">
            <td><div id="botonA"><html:link href="#" styleClass="enviar_mailA" title="Envía un mail con el pedido al cliente" onclick="requestAjax('detalleEstadoPedidoEspecial.do',['div_pagina','pregunta'],{parameters: 'redactarMail=ok', evalScripts: true});">Env&iacute;o mail</html:link></div></td>
            </c:if>
            <td><div id="botonA"><html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 1, false);">Crear XLS</html:link></div></td>
            <td><div id="botonA"><html:link action="detalleEstadoPedidoEspecial.do?confirmarImpresionTexto=ok'});" styleClass="imprimirA" >Imprimir</html:link></div></td>
            <td><div id="botonA"><html:link action="detalleEstadoPedidoEspecial.do?atras=ok" styleClass="atrasA">Atras</html:link></div></td>
        </logic:equal>       
    </tr>
    <tr>
	    <td>
		    <div id="pregunta">
				<!-- PopUp de Redactar mail-->
				<logic:notEmpty name="ec.com.smx.sic.sispe.redactarMail">
					<tiles:insert page="/servicioCliente/redactarMail.jsp">
						<tiles:put name="vtformAction" value="detalleEstadoPedidoEspecial" />
						<tiles:put name="vtformName" value="listadoPedidosForm" />
					</tiles:insert>
				</logic:notEmpty>
			</div>
    	</td>
    </tr>
</html:form>
</table>
