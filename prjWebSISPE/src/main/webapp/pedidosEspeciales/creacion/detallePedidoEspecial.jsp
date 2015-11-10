<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<!-- Detalle del pedido es llamado desde crearPedidoEspecial.jsp -->

<tiles:useAttribute id="vtformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<!-- insertada desde entregaArticuloLocal.jsp -->
<c:set var="altura_div" value="270"/>

<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/> 

<div id="detallePedido">
    <table border="0" cellspacing="0" cellpadding="0" class="tabla_informacion" width="100%">
        <tr class="fila_titulo">
            <td align="left" class="fila_contenido textoAzul11">&nbsp;Detalle:
                <%--&nbsp;&nbsp;<html:checkbox property="checkActualizarStockAlcance" title="active esta opci&oacute;n para actualizar el stock y alcance mediante el bot&oacute;n ACTUALIZAR"/><label class="textoNegro10">Verificar Alcance</label>--%>
            </td>
            <td height="25px" class="fila_contenido">
                <table cellspacing="0" cellpadding="1" align="right">
                    <tr>
                        <td>
                            <div id="botonD">
                                <html:link styleClass="agregarD" href="#" title="Agregar art&iacute;culos al detalle" onclick="requestAjax('crearPedidoEspecial.do',['mensajes','detallePedido'],{parameters: 'agregarArticulo=ok', evalScripts: true});">Agregar</html:link>
                            </div>
                        </td>
                        <td>
                            <div id="botonD">
                                <html:link styleClass="eliminarD" href="#" title="Eliminar art&iacute;culos seleccionados" onclick="requestAjax('crearPedidoEspecial.do',['mensajes','detallePedido'],{parameters: 'eliminarArticulos=ok'});">Eliminar</html:link>
                            </div>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2">
            	
           	  <logic:empty name="sispe.pedido.pavos">
           	  	<c:set var="colEspanPavos" value="8"/>
           	  </logic:empty>
            	
           	  <logic:notEmpty name="sispe.pedido.pavos">
           	  	<c:set var="colEspanPavos" value="8"/>
           	  </logic:notEmpty>
            	
                <div id="detallePedido">    
                    <table border="0" width="100%" cellpadding="0" cellspacing="0">
                        <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol">
                            <tr class="tituloTablas" align="left">
                                <td class="columna_contenido" align="center" width="3%">
                                    <html:checkbox property="checkSeleccionarTodo" title="Seleccionar todos los art&iacute;culos" onclick="activarDesactivarTodo(this,crearPedidoEspecialForm.checksSeleccionar)"/>
                                </td>
                                <td class="columna_contenido" align="center" width="4%">No</td>
                                <td class="columna_contenido" width="15%" align="center">C&oacute;digo barras</td>
                                <td class="columna_contenido" align="center" width="30%">Art&iacute;culo</td>
                                <logic:notEmpty name="sispe.pedido.pavos">
                                	<td class="columna_contenido" align="center" width="7%">Stock D. L.</td>
                                </logic:notEmpty>
                                <td class="columna_contenido" align="center" width="7%">Cantidad</td>
                                
                                <logic:empty name="sispe.pedido.pavos">
                                	<td class="columna_contenido" align="center" width="7%">Peso</td>
                                </logic:empty>
                                
                                <td class="columna_contenido" width="7%" align="center">V. unit.</td>
                                <td class="columna_contenido" width="33%" align="center">Observaci&oacute;n</td>
                            </tr>
                            <tr>
                                <td colspan="${colEspanPavos}">
                                    <div id="seccion_detalle" style="width:100%;height:${altura_div}px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
                                        <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                            <logic:iterate name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol" id="detalle" indexId="numDetalle">
                                                <!----- control del estilo para el color de las filas ------>
                                                <bean:define id="residuo" value="${numDetalle % 2}"/>
                                                <c:set var="title_fila" value=""/>
                                                <c:set var="clase" value="blanco10"/>
                                                <logic:notEqual name="residuo" value="0">
                                                    <c:set var="clase" value="grisClaro10"/>
                                                </logic:notEqual>
                                                <%---- control del estilo para el alcance -----%>
                                                <c:choose>
                                                    <c:when test="${detalle.articuloDTO.npAlcance == estadoInactivo}">
                                                        <c:set var="clase" value="rojoClaro10"/>
                                                    </c:when>
                                                </c:choose>
                                                <logic:notEmpty name="detalle" property="npEstadoError">
                                                    <c:set var="clase" value="rojoObsuro10"/>
                                                </logic:notEmpty>
                                                <%----------------------------------------------------------%>
                                                <bean:define id="fila" value="${numDetalle + 1}"/>
                                                <c:set var="pesoVariable" value=""/>
                                                <c:set var="imagen" value=""/>
                                                <%-----Definici&oacute;n de la propiedad articuloDTO ------%>
                                                <bean:define name="detalle" property="articuloDTO" id ="articulo"/>
                                                <tr class="${clase}">
                                                    <td width="3%" class="fila_contenido" align="center">
                                                        <html:multibox property="checksSeleccionar" value="${numDetalle}"/>
                                                    </td>
                                                    <td width="4%" class="columna_contenido fila_contenido" align="center"><bean:write name="fila"/></td>
                                                          
                                                    <logic:notEmpty name="articulo" property="codigoBarrasActivo">
                                                    	<td width="15%" class="columna_contenido fila_contenido" align="center"><bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/></td>
                                                    </logic:notEmpty>
                                                    
													<logic:empty name="articulo" property="codigoBarrasActivo">
														<logic:notEmpty name="detalle" property="codigoBarras">
															<td width="15%" class="columna_contenido fila_contenido" align="center"><bean:write name="detalle" property="codigoBarras"/></td>
														</logic:notEmpty>
													</logic:empty>
                                                    <td width="30%" class="columna_contenido fila_contenido" align="left"><bean:write name="articulo" property="descripcionArticulo"/></td>
                                                    <logic:notEmpty name="sispe.pedido.pavos">
                                                    	 <td width="7%" class="columna_contenido fila_contenido" align="right"><bean:write name="articulo" property="npStockArticulo"/>&nbsp;</td>
                                                    </logic:notEmpty>
                                                    
                                                    <td width="7%" class="columna_contenido fila_contenido" align="center">
                                                        <smx:text property="cantidadArticulo" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}" styleClass="textNormal" size="6" maxlength="5" onkeypress="return validarInputNumeric(event);"/>	
                                                    </td>
                                                    
                                                    <logic:empty name="sispe.pedido.pavos">
	                                                    <td width="7%" class="columna_contenido fila_contenido" align="center">
	                                                        <smx:text property="pesoArticulo" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="6" maxlength="5" onkeypress="return validarInputNumericDecimal(event);"/>	
	                                                    </td>
                                                    </logic:empty>
                                                    
                                                    <td width="7%" class="columna_contenido fila_contenido" align="right">
                                                    	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>&nbsp;
                                                    </td>    
                                                    <td width="33%" class="columna_contenido fila_contenido" align="right">
                                                        <html:textarea style="width:95%" property="observacion" styleClass="textObligatorio" rows="2" cols="40" value="${detalle.estadoDetallePedidoDTO.observacionArticulo}"></html:textarea>
                                                    </td>
                                                </tr>        
                                            </logic:iterate>
                                        </table>
                                    </div>		
                                </td>
                            </tr>
                        </logic:notEmpty>
                        <logic:empty name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol">
                            <bean:define id="alturaDetalleVacio" value="${altura_div + 20}"/>
                            <tr>
                                <td>
                                    <div id="div_listado" style="width:100%;height:${alturaDetalleVacio}px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
                                        <table class="textoNegro11" align="left">
                                            <tr>
                                                <td width="3%" align="center"><img src="images/info_16.gif"></td>
                                                <td align="left">Ingrese los art&iacute;culos para el pedido</td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </logic:empty>
                        <tr>
                            <td class="celesteFosforescente" width="100%" colspan="9">
                                <c:set var="descripcionArticulo" value=""/>
                                <c:set var="stockArticulo" value=""/>
                                <c:set var="descripcionProblema" value=""/>
                                <logic:notEmpty name="ec.com.smx.sic.sispe.articuloDTO" scope="request">
                                    <c:set var="descripcionArticulo"><bean:write name="ec.com.smx.sic.sispe.articuloDTO" property="descripcionArticulo"/></c:set>
                                    <c:set var="descripcionProblema" value="Problema de alcance"/>
                                </logic:notEmpty>
                                <table cellpadding="0" cellspacing="0" width="98%" align="left">
                                    <tr>
                                        <td align="center" width="3%"><img src="images/flechader.gif" border="0"></td>
                                        <td align="right" width="30%">
                                            <label class="textoAzul11">&nbsp;&nbsp;C&oacute;digo barras:&nbsp;</label><html:text property="codigoArticulo" size="17" styleClass="textNormal" onkeypress="return validarInputNumeric(event);" onkeydown="requestAjaxEnter('crearPedidoEspecial.do',['mensajes','detallePedido'],{parameters: 'agregarArticulo=ok',evalScripts:true});"/>
                                        </td>
                                        <td align="right" width="23%">
                                            <label class="textoAzul11">Cantidad/Peso KG:&nbsp;</label><html:text property="cantidadArticuloI" size="6" maxlength="5" styleClass="textNormal" value="0" onkeypress="return validarInputNumericDecimal(event);" onkeydown="requestAjaxEnter('crearPedidoEspecial.do',['mensajes','detallePedido'],{parameters: 'agregarArticulo=ok',evalScripts:true});"/>
                                        </td>
                                        <td align="center" width="30%" class="textoRojo10">
                                            <logic:notEmpty name="descripcionArticulo">
                                                <bean:write name="descripcionArticulo"/>
                                            </logic:notEmpty>
                                            <logic:empty name="descripcionArticulo">&nbsp;</logic:empty>
                                        </td>
                                        <td colspan="4" align="left">
                                            <logic:notEmpty name="descripcionProblema">
                                                <table cellpadding="0" cellspacing="0" width="100%">
                                                    <tr>
                                                        <td><img src="images/advertencia_16.gif" border="0">&nbsp;</td>
                                                        <td>
                                                            <bean:write name="descripcionProblema"/>&nbsp;
                                                        </td>
                                                    </tr>
                                                </table>
                                            </logic:notEmpty>
                                            <logic:empty name="descripcionProblema">&nbsp;</logic:empty>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    <logic:empty name="ec.com.smx.sic.sispe.pedido.primeraVez.bodega">
                        <script language="JavaScript" type="text/javascript">Field.activate('codigoArticulo');</script>
                    </logic:empty>
                </div>
            </td>
        </tr>			
    </table>
</div>