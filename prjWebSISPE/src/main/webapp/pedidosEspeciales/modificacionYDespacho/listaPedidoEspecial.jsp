<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:useAttribute id="vformName"  name="vtformName"  classname="java.lang.String" ignore="true"/>

<table  border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="98%" align="center">
    <tr><td height="5px"></td></tr>
    <tr>
        <%--Barra Izquierda--%>
        <td class="datos"  width="25%" id="izquierda" align="center">
            <%-- <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/> --%>
            <div style="width:100%;height:486px;">
				<tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
			</div>
        </td>
        <td class="datos" width="80%" id="derecha">
            <div style="height:486px;width:100%;" >
                <table class="tabla_informacion textoNegro12" border="0"  cellpadding="0" cellspacing="0" width="98%" align="center">
                    <%--Titulo de los datos--%>
                    <tr>
                        <td class="fila_titulo" colspan="7">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                <tr>
                                    <td><img src="./images/detalle_pedidos24.gif" border="0"/></td>
                                    <td height="23" width="100%">&nbsp;
                                    	<logic:notEmpty name="sispe.pedido.pavos">
		                                	Detalle de pedidos de pavos
		                                </logic:notEmpty>
		                                
                                    	<logic:empty name="sispe.pedido.pavos">
		                                	Detalle de pedidos de carnes
		                                </logic:empty>
                                    	
                                    	
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td height="5px"></td></tr>
                    <tr>
                        <td>
                            <div id="resultadosBusqueda">
                                <table border="0" class="textoNegro11" width="99%" align="center" cellspacing="0" cellpadding="0">
                                    <logic:notEmpty name="ec.com.smx.sic.sispe.pedidos.subPagina">
                                        <tr>
                                            <td colspan="5" align="right">
                                                <smx:paginacion start="${listadoPedidosForm.start}" range="${listadoPedidosForm.range}" results="${listadoPedidosForm.size}" styleClass="textoNegro11" url="confirmarPedidoEspecial.do" campos="false"/>
                                            </td>
                                        </tr> 
                                        <tr class="tituloTablas">
                                            <td colspan="5">
                                                <table border="0" cellspacing="0" align="left" width="100%" class="tabla_informacion" cellpadding="0">	
                                                    <tr >
                                                        <td class="columna_contenido" width="4%" align="center">No</td>
                                                        <td class="columna_contenido" width="20%" align="center">No PEDIDO</td>
                                                        <td class="columna_contenido" width="50%" align="center">CONTACTO-EMPRESA</td>
                                                        <td class="columna_contenido" width="12%" align="center">FECHA PEDIDO</td>
                                                        <td class="columna_contenido" width="12%" align="center">ACCI&Oacute;N</td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                                                    <table border="0" cellspacing="0" width="100%" class="tabla_informacion" cellpadding="3">
                                                        <logic:iterate name="ec.com.smx.sic.sispe.pedidos.subPagina" id="vistaPedidoDTO" indexId="numeroRegistro">
                                                            <bean:define id="indice" value="${numeroRegistro + listadoPedidosForm.start}"/>
                                                            <%--------- control del estilo para el color de las filas -------------%>
                                                            <bean:define id="residuo" value="${numeroRegistro % 2}"/>
                                                            <bean:define id="fila" value="${indice + 1}"/>
                                                            <logic:equal name="residuo" value="0">
                                                                <bean:define id="colorBack" value="blanco10"/>
                                                            </logic:equal>
                                                            <logic:notEqual name="residuo" value="0">
                                                                <bean:define id="colorBack" value="grisClaro10"/>
                                                            </logic:notEqual>
                                                            <%-------------------------------------------------------------------%>
                                                            <tr class="${colorBack}"> 
                                                                <td class="fila_contenido" align="center" width="4%">${fila}</td>
                                                                <td class="columna_contenido fila_contenido" align="center" width="20%" >
                                                                    <html:link title="Detalle del pedido" action="detalleEstadoPedidoEspecial.do?detallePedido=${indice}" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                                </td>
                                                                <td class="columna_contenido fila_contenido" width="50%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>	
                                                                <td class="columna_contenido fila_contenido" width="12%" align="center"><bean:write name="vistaPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
                                                                <td class="columna_contenido fila_contenido" width="12%" align="center">
                                                                    <html:link action="crearPedidoEspecial" paramId="indice" paramName="indice" onclick="popWait();">Modificar</html:link>
                                                                </td>
                                                            </tr>
                                                        </logic:iterate> 
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </logic:notEmpty>
                                    <logic:empty name="ec.com.smx.sic.sispe.pedidos.subPagina">
                                        <tr>
                                            <td colspan="5">
                                                Seleccione un criterio de b&uacute;squeda
                                            </td>
                                        </tr>
                                    </logic:empty>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
</table>