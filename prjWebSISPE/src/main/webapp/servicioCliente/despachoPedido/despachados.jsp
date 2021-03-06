<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="parametroImpresion"><bean:write name="ec.com.smx.sic.sispe.despachoEntrega.imprimir"/></bean:define>
<bean:define id="parametroImpresion2"><bean:write name="ec.com.smx.sic.sispe.Despacho.imprimir"/></bean:define>

<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="99%" align="center" bgcolor="white">
    <%--Titulo de los datos--%>
    <tr>
        <td>
            <div id="resultadosBusqueda">
                <table cellspacing="0" cellpadding="0" width="100%">
                    <tr>
                        <td class="fila_titulo" colspan="6">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                <tr>
                                    <td width="3%">
                                        &nbsp;<img src="images/pedidos24.gif" border="0"/>
                                    </td>
                                    <td width="85%" align="left">	
                                        <b>&nbsp;Lista de pedidos despachados:</b>
                                    </td>
                                    <logic:notEmpty name="despachosEntregasForm" property="datos">
                                    	<td width="10%" align="left">
                                            <div id="botonD">	
                                                <html:link styleClass="imprimirD" href="#" onclick="requestAjax('despachoPedido.do',['mensajes','ventanaImpresion2'],{parameters: 'botonImprimir2=ok', evalScripts: true});">Ver reporte</html:link>
                                            </div>												
                                        </td>
                                    	<%--<td width="10%" align="left">
                                            <div id="botonD">	
                                                <html:link styleClass="imprimirD" href="#" onclick="requestAjax('despachoPedido.do',['mensajes','ventanaImpresion'],{parameters: 'botonImprimir=ok', evalScripts: true});">Ver Reporte</html:link>
                                            </div>												
                                        </td>
                                        --%>
                                    </logic:notEmpty>
                                    <td width="2%"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td colspan="6" height="8"></td></tr>
                    <tr>
                        <td>
                            <table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
                                <logic:notEmpty name="despachosEntregasForm" property="datos">
                                    <tr>
                                        <td align="right" colspan="7">
                                            <smx:paginacion start="${despachosEntregasForm.start}" range="${despachosEntregasForm.range}" results="${despachosEntregasForm.size}" styleClass="textoNegro10" url="despachoPedido.do"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="7">
                                            <table border="0" cellspacing="0" width="100%" cellpadding="1">
                                                <tr class="tituloTablas"  align="left">
                                                  <%--
                                                    <td class="columna_contenido" width="3%" align="center">
                                                        <html:checkbox property="opSeleccionTodos" title="selecionar todos los pedidos para imprimir" value="t" onclick="activarDesactivarTodo(this, despachosEntregasForm.opSeleccionAlgunos);"/>
                                                    </td>
                                                    --%>
                                                    <td class="columna_contenido" width="17%" align="center">No PEDIDO</td>
                                                    <td class="columna_contenido" width="10%" align="center">No RES.</td>
                                                    <td class="columna_contenido" width="43%" align="center">CONTACTO - EMPRESA</td>
                                                    <td class="columna_contenido" width="12%" align="center">% ABONADO</td>
                                                    <td class="columna_contenido" width="15%" align="center" title="primera fecha de despacho al local">PRIMERA FECHA DESPACHO</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div id="div_listado" style="width:100%;height:375px;overflow:auto;">
                                                <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                    <logic:iterate name="despachosEntregasForm" property="datos" id="vistaPedidoDTO" indexId="indicePedido">
                                                        <bean:define id="indice" value="${indicePedido + despachosEntregasForm.start}"/>
                                                        <%--------- control del estilo para el color de las filas --------------%>
                                                        <bean:define id="residuo" value="${indicePedido % 2}"/>
                                                        <%--------- control del estilo para el color de las filas --------------%>
                                                        <logic:equal name="residuo" value="0">
                                                            <bean:define id="clase" value="blanco10"/>
                                                        </logic:equal>
                                                        <logic:notEqual name="residuo" value="0">
                                                            <bean:define id="clase" value="grisClaro10"/>
                                                        </logic:notEqual>
                                                        <%--------------------------------------------------------------------%>
                                                        <tr class="${clase}">
                                                           <%-- <td class="columna_contenido fila_contenido" width="3%" align="center"><html:multibox property="opSeleccionAlgunos" value="${indicePedido}"/></td>--%>
                                                            <td class="columna_contenido fila_contenido" width="17%" align="center"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></td>
                                                            <td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/></td>
                                                            <td class="columna_contenido fila_contenido" width="43%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
                                                            <td class="columna_contenido fila_contenido" width="12%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.porcentajeAbonoPedido}"/>%</td>
                                                            <td class="columna_contenido fila_contenido columna_contenido_der" width="15%" align="center" title="primera fecha de despacho al local"><bean:write name="vistaPedidoDTO" property="primeraFechaDespacho" formatKey="formatos.fecha"/></td>			
                                                        </tr>
                                                    </logic:iterate>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                </logic:notEmpty>
                                <logic:empty name="despachosEntregasForm" property="datos">
                                    <tr><td>Seleccione una opci&oacute;n de b&uacute;squeda</td></tr>
                                </logic:empty>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
    <tr>
        <td id="ventanaImpresion">
            <script language="JavaScript" type="text/javascript">
                verificarAbrirVentana('despachoPedido.do?mostrarVentana=ok','WIN_DES_ENT','${parametroImpresion}');
            </script>
        </td>
    </tr>
     <tr>
        <td id="ventanaImpresion2">
            <script language="JavaScript" type="text/javascript">
                verificarAbrirVentana('despachoPedido.do?mostrarVentana2=ok','WIN_DES_ENT','${parametroImpresion2}');
            </script>
        </td>
    </tr>
  </table>

