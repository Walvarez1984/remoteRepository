<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
	<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasta">
	<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasta"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>
<bean:define id="preciosActualizables"><bean:message key="ec.com.smx.sic.sispe.detallePedido.actualizar.precio"/></bean:define>
<bean:define id="cotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.cotizacion"/></bean:define>
<bean:define id="recotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.recotizacion"/></bean:define>
<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>
<bean:define id="modificarReservacion"><bean:message key="ec.com.smx.sic.sispe.accion.modificarReservacion"/></bean:define>
<bean:define id="articuloDeBaja"><bean:message key="ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"/></bean:define>
<bean:define id="articuloObsoleto"><bean:message key="ec.com.smx.sic.sispe.claseArticulo.obsoleto"/></bean:define>
<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
<bean:define id="codigoDescuentoVariable"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.descuentoVariable"/></bean:define>
<bean:define id="codigoAutorizacionStock"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.autorizacion.stock"/></bean:define>

<c:set var="imagen" value="cotizacion.gif"/>
<c:set var="estilo_btnCotRes" value="reservacionA"/>
<c:set var="texto_btnCotRes" value="Reservar"/>
<c:set var="title_btnCotRes" value="pasar a la Reservaci&oacute;n"/>
<c:set var="accion" value="crearCotizacion.do"/>
<c:set var="altura_div" value="215"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.entidadBodega">
	<c:set var="altura_div" value="218"/>
</logic:notEmpty>

<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
	<c:set var="imagen" value="reservacion.gif"/>
	<c:set var="estilo_btnCotRes" value="cotizacionA"/>
	<c:set var="texto_btnCotRes" value="Cotizar"/>
	<c:set var="title_btnCotRes" value="pasar a la Cotizaci&oacute;n"/>
</logic:equal>

<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<bean:define id="tipoArticuloPiezaPesoUnidadManejo"><bean:message key="ec.com.smx.sic.sispe.articulo.tipoControlCosto"/></bean:define>

<div id="detallePedido">
	<table border="0" width="100%" cellpadding="0" cellspacing="0">
                                    <logic:notEmpty name="ec.com.smx.sic.sispe.detallePedido">
                                        <tr>
                                            <td>
                                                <c:set var="anchoFilaIngreso" value="31%"/>
                                                <table cellspacing="0" cellpadding="0" width="98%" align="left">
                                                    <tr class="tituloTablas">
                                                        <td width="2%" align="center" class="columna_contenido">&nbsp;</td>
                                                        <logic:empty name="ec.com.smx.sic.sispe.accion.consolidar"><!-- Cambios oscar -->													
                                                        	<td width="3%" align="center" class="columna_contenido"><html:checkbox property="checkSeleccionarTodo" value="t" onclick="activarDesactivarTodo(this, cotizarRecotizarReservarForm.checksSeleccionar);"/></td>														
                                                        </logic:empty>	
                                                        <td class="columna_contenido" align="center">&nbsp;</td>
                                                        <td width="2%" align="center" class="columna_contenido">No</td>                                                        
                                                        <td width="3%" align="center" class="columna_contenido" title="AUTORIZACION">Aut</td>
                                                        <td width="13%" align="center" class="columna_contenido">C&oacute;digo barras</td>
                                                        <td width="27%" align="center" class="columna_contenido">Art&iacute;culo</td>
                                                        <td width="5%" align="center" class="columna_contenido">Cant.</td>
                                                        <td width="6%" align="center"  class="columna_contenido">Stock</td>
                                                        <td width="7%" align="center" class="columna_contenido">Peso KG</td>
                                                        <td width="6%" align="center" class="columna_contenido">V.unit.</td>
                                                        <td width="6%" align="center" class="columna_contenido">V.unit. IVA</td>
                                                         <%--   <table cellpadding="0" cellspacing="0" border="0">
                                                                <tr>
                                                                    <td>
                                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.establecimientoHabilitadoCambioPrecios">
                                                                            <logic:equal name="modificarDetallePedido" value="${estadoActivo}">
                                                                                <html:link styleClass="linkBlanco9" href="#" onclick="requestAjax('crearCotizacion.do',['seccion_detalle'],{parameters: 'actualizarPreciosUnitarios=ok'})">V.Unit</html:link>
                                                                            </logic:equal>
                                                                            <logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">V.Unit.</logic:notEqual>
                                                                        </logic:notEmpty>
                                                                        <logic:empty name="ec.com.smx.sic.sispe.establecimientoHabilitadoCambioPrecios">V.Unit.</logic:empty>
                                                                    </td>
                                                                </tr>
                                                            </table>--%>
                                                        </td>
                                                        <td width="7%" align="center" class="columna_contenido">Tot. IVA</td>
                                                        <td width="2%" align="center" class="columna_contenido">IVA</td>
                                                        <td width="3%" align="center" class="columna_contenido" title="descuento total: por caja + descuento aplicado">Desc</td>
                                                        <%--<td width="5%" align="center" class="columna_contenido" title="valor unitario incluido el descuento">V.Unit. Neto</td>--%>
                                                        <td width="7%" align="center" class="columna_contenido columna_contenido_der" title="valor total incluido el descuento">Tot. neto</td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div id="div_listado" style="width:100%;height:${altura_div}px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
                                                    <table cellspacing="0" cellpadding="0" width="98%" align="left">
                                                        <logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detalle" indexId="numeroRegistro">
                                                            <bean:define id="residuo" value="${numeroRegistro % 2}"/>
                                                            <c:set var="titulo_fila" value=""/>
                                                            <c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejo}"/>
                                                            <c:set var="aplicaImpuesto" value="${detalle.articuloDTO.aplicaImpuestoVenta}"/>
                                                            <logic:equal name="detalle" property="articuloDTO.npHabilitarPrecioCaja" value="${estadoActivo}">
                                                                <c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejoCaja}"/>
                                                            </logic:equal>
                                                            <c:set var="clase" value="blanco10"/>
                                                            <logic:notEqual name="residuo" value="0">
                                                                <c:set var="clase" value="grisClaro10"/>
                                                            </logic:notEqual>
                                                            <%-- control de estilos para indicar el status completo del artículo en el SIC --%>
                                                            <c:choose>
                                                                <%-- DE BAJA EN EL SIC --%>
                                                                <c:when test="${detalle.articuloDTO.npEstadoArticuloSIC == articuloDeBaja || detalle.articuloDTO.npEstadoArticuloSICReceta == articuloDeBaja}">
                                                                    <c:set var="clase" value="verdeClaro10"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <%-- OBSOLETO EN EL SIC --%>
                                                                    <c:choose>
                                                                        <c:when test="${detalle.articuloDTO.claseArticulo != null && detalle.articuloDTO.claseArticulo == articuloObsoleto}">
                                                                            <c:set var="clase" value="amarilloClaro10"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:choose>
                                                                                <%-- SIN ALCANCE --%>
                                                                                <c:when test="${detalle.articuloDTO.npAlcance == estadoInactivo || detalle.articuloDTO.npAlcanceReceta == estadoInactivo}">
                                                                                    <c:set var="clase" value="rojoClaro10"/>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <c:choose>
                                                                                        <%-- PROBLEMAS DE STOCK --%>
                                                                                        <c:when test="${detalle.articuloDTO.npEstadoStockArticulo == estadoInactivo || detalle.articuloDTO.npEstadoStockArticuloReceta == estadoInactivo}">
                                                                                            <c:set var="clase" value="naranjaClaro10"/>
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                            <c:choose>
                                                                                                <%-- INACTIVO EN EL SIC --%>
                                                                                                <c:when test="${detalle.articuloDTO.npEstadoArticuloSIC == estadoInactivo || detalle.articuloDTO.npEstadoArticuloSICReceta == estadoInactivo}">
                                                                                                    <c:set var="clase" value="celesteClaro10"/>
                                                                                                </c:when>
                                                                                            </c:choose>
                                                                                        </c:otherwise>
                                                                                    </c:choose>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:otherwise>
                                                            </c:choose>											
                                                            <bean:define id="fila" value="${numeroRegistro + 1}"/>
                                                            <c:set var="pesoVariable" value=""/>
                                                            <c:set var="imagen" value=""/>
                                                             <logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloPavo}">
                                                                <c:set var="pesoVariable" value="${estadoActivo}"/>
                                                                <logic:equal name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                                                	<c:set var="imagen" value="balanza.gif"/>
                                                                </logic:equal>
                                                                <logic:notEqual name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                                                	<c:set var="imagen" value="pavo.gif"/>
                                                                </logic:notEqual>		
                                                            </logic:equal>
                                                            <logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                <c:set var="pesoVariable" value="${estadoActivo}"/>
                                                                <c:set var="imagen" value="balanza.gif"/>
                                                            </logic:equal>
                                                            
                                                            
                                                            
                                                            <%-- Definici&oacute;n de la propiedad articuloDTO --%>
                                                            <bean:define name="detalle" property="articuloDTO" id ="articulo"/>
                                                            <tr class="${clase}">
                                                                <td width="2%" class="fila_contenido columna_contenido" align="center">
                                                                    <logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.npDetalleUnidades">
                                                                        <div id="plegar5${numeroRegistro}" class="displayNo">
                                                                            <a title="Ver Detalle de Entregas" href="#" onClick="ocultar(${numeroRegistro},'marco5','plegar5','desplegar5');">
                                                                                <html:img src="images/plegar.gif" border="0"/>
                                                                            </a>
                                                                        </div>
                                                                        <div id="desplegar5${numeroRegistro}">
                                                                            <a title="Ver Detalle de Entregas" href="#" onClick="mostrar(${numeroRegistro},'marco5','plegar5','desplegar5');">
                                                                                <html:img src="images/desplegar.gif" border="0"/>
                                                                            </a>
                                                                        </div>
                                                                    </logic:notEmpty>
                                                                    <logic:empty name="detalle" property="estadoDetallePedidoDTO.npDetalleUnidades">&nbsp;</logic:empty>
                                                                </td>                                                            
                                                                <logic:empty name="ec.com.smx.sic.sispe.accion.consolidar"><!-- Cambios oscar -->
	                                                                <td width="3%" class="fila_contenido" align="center">
	                                                                    <html:multibox property="checksSeleccionar" value="${numeroRegistro}"/>
	                                                                </td>
                                                                </logic:empty>
                                                                <td align="left" class="columna_contenido fila_contenido">
																	<logic:notEmpty name="detalle" property="articuloDTO.articuloRelacionCol">
																	<logic:notEmpty name="articulo" property="npNuevoCodigoClasificacion">
                                                                        <div id="desplegar1${numeroRegistro}" >
																			<a title="Ver detalle art&iacute;culo" href="#" onClick="mostrar(${numeroRegistro},'marco2','plegar2','desplegar2');mostrar(${numeroRegistro},'marco3','plegar1','desplegar1');">
																				<html:img src="images/desplegar.gif" border="0"/>
																			</a>
																		</div>
																		<div id="plegar1${numeroRegistro}" class="displayNo">
																			<a title="ocultar detalle art&iacute;culo" href="#" onClick="ocultar(${numeroRegistro},'marco2','plegar2','desplegar2');ocultar(${numeroRegistro},'marco3','plegar1','desplegar1');">
																				<html:img src="images/plegar.gif" border="0"/>
																			</a>
																		</div>           
                                                                    </logic:notEmpty>
                                                                    
                                                                    <logic:empty name="articulo" property="npNuevoCodigoClasificacion">
		                                                                <logic:notEmpty name="detalle" property="npCodigoClasificacion">
		                                                                    <c:if test="${detalle.npCodigoClasificacion==articuloEspecial}">
		                                                                    	<div id="desplegar1${numeroRegistro}" >
																					<a title="Ver detalle art&iacute;culo" href="#" onClick="mostrar(${numeroRegistro},'marco2','plegar2','desplegar2');mostrar(${numeroRegistro},'marco3','plegar1','desplegar1');">
																						<html:img src="images/desplegar.gif" border="0"/>
																					</a>
																				</div>
																				<div id="plegar1${numeroRegistro}" class="displayNo">
																					<a title="ocultar detalle art&iacute;culo" href="#" onClick="ocultar(${numeroRegistro},'marco2','plegar2','desplegar2');ocultar(${numeroRegistro},'marco3','plegar1','desplegar1');">
																						<html:img src="images/plegar.gif" border="0"/>
																					</a>
																				</div>      
		                                                                    </c:if>
		                                                                    <c:if test="${detalle.npCodigoClasificacion!=articuloEspecial}">&nbsp;</c:if>
                                                                    	</logic:notEmpty>                                                                     	
	                                                                </logic:empty>											
																	</logic:notEmpty>
																	<logic:empty name="detalle" property="articuloDTO.articuloRelacionCol">&nbsp;</logic:empty>	
																</td>
                                                                <td width="2%" class="columna_contenido fila_contenido" align="center"><bean:write name="fila"/></td>
                                                                
                                                                <!-- COLUMNA DE AUTORIZACION DE DESCUENTO VARIABLE -->                                                                
                                                                <td width="3%" class="columna_contenido fila_contenido" align="center" valign="middle">
                                                                	<logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol"> 
                                                                		<c:set var="autorizacion" value="0"/>
                                                                		<c:set var="nombreDepartamento" value=""/>                                                                       
                                                                        <logic:iterate name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutDTO" indexId="numeroAut">                                                                            
                                                                            <logic:notEmpty name="detalleAutDTO" property="estadoPedidoAutorizacionDTO">                                                                            	
                                                                            	<logic:notEmpty name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.npNombreDepartamento">
                                                                            		<c:set var="nombreDepartamento" value="-${detalleAutDTO.estadoPedidoAutorizacionDTO.npNombreDepartamento}- "/>
                                                                            	</logic:notEmpty>
	                                                                     		<c:if test="${codigoDescuentoVariable == detalleAutDTO.estadoPedidoAutorizacionDTO.npTipoAutorizacion}">	                                                                                
	                                                                                <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.pendiente">
	                                                                                    <c:set var="autorizacion" value="1"/>
	                                                                                </smx:equal>                                                                            
	                                                                                <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.solicitada">	
	                                                                                    <c:set var="autorizacion" value="2"/>
	                                                                                </smx:equal>
	                                                                                <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.aprobada">	
	                                                                                    <c:set var="autorizacion" value="3"/>
	                                                                                </smx:equal>
	                                                                                <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.rechazada">	
	                                                                                    <c:set var="autorizacion" value="4"/>
	                                                                                </smx:equal>
	                                                                                <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.utilizada">	
	                                                                                    <c:set var="autorizacion" value="5"/>
	                                                                                </smx:equal>
	                                                                                <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.caducada">	
	                                                                                    <c:set var="autorizacion" value="6"/>
	                                                                                </smx:equal>
	                                                                            </c:if>
                                                                            </logic:notEmpty>
                                                                        </logic:iterate>
                                                                        <logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
	                                                                        <c:if test="${autorizacion == 0}">
	                                                                           &nbsp;
	                                                                        </c:if>                                                                        
	                                                                        <c:if test="${autorizacion == 1}">
	                                                                           <img src="images/autPendiente.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}${fn:contains(detalleAutDTO.estadoPedidoAutorizacionDTO.valorReferencial,'MARCA PROPIA')?'(MARCA PROPIA) ':''}pendiente">
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 2}">
	                                                                           	<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
	                                                                           		<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}${fn:contains(detalleAutDTO.estadoPedidoAutorizacionDTO.valorReferencial,'MARCA PROPIA')?'(MARCA PROPIA) ':''}solicitada">
	                                                                           	</html:link>
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 3}">
	                                                                        	<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}${fn:contains(detalleAutDTO.estadoPedidoAutorizacionDTO.valorReferencial,'MARCA PROPIA')?'(MARCA PROPIA) ':''}aprobada">
	                                                                          	<!--   	<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
	                                                                           		<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n Aprobada">
	                                                                           	</html:link> -->
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 4}">
	                                                                           	<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
	                                                                           		<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}${fn:contains(detalleAutDTO.estadoPedidoAutorizacionDTO.valorReferencial,'MARCA PROPIA')?'(MARCA PROPIA) ':''}rechazada">
	                                                                           	</html:link>
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 5}">
	                                                                           	<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
	                                                                           		<img src="images/autUtilizada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}${fn:contains(detalleAutDTO.estadoPedidoAutorizacionDTO.valorReferencial,'MARCA PROPIA')?'(MARCA PROPIA) ':''}utilizada en otro momento">
	                                                                           	</html:link>
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 6}">
	                                                                           	<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
	                                                                           		<img src="images/autCaducada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}${fn:contains(detalleAutDTO.estadoPedidoAutorizacionDTO.valorReferencial,'MARCA PROPIA')?'(MARCA PROPIA) ':''}caducada">
	                                                                           	</html:link>
	                                                                        </c:if>
                                                                        </logic:empty>
                                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
                                                                        	<c:if test="${autorizacion == 0}">
	                                                                           &nbsp;
	                                                                        </c:if>                                                                        
	                                                                        <c:if test="${autorizacion == 1}">
	                                                                           <img src="images/autPendiente.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}pendiente">
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 2}">
																			<logic:empty name="ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion">
	                                                                           		<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}solicitada">
																			</logic:empty>
																			
																			<logic:notEmpty name="ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion">
	                                                                           	<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
	                                                                           		<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de sescuento variable ${nombreDepartamento}solicitada">
	                                                                           	</html:link>
																			</logic:notEmpty>
																			
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 3}">
	                                                                        	<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de sescuento variable ${nombreDepartamento}aprobada">
	                                                                          	<!--   	<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
	                                                                           		<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n Aprobada">
	                                                                           	</html:link> -->
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 4}">
	                                                                           <logic:notEmpty name="ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion">
																					<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
																						<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}rechazada">
																					</html:link>
																				</logic:notEmpty>
																				
																				<logic:empty name="ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion">
																						<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}rechazada">
																				</logic:empty>
																				
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 5}">
																				<logic:notEmpty name="ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion">
																					<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
																						<img src="images/autUtilizada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}utilizada en otro momento">
																					</html:link>
																				</logic:notEmpty>
																				<logic:empty name="ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion">
																							<img src="images/autUtilizada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}utilizada en otro momento">
																				</logic:empty>
	                                                                        </c:if>
	                                                                        <c:if test="${autorizacion == 6}">
																				<logic:notEmpty name="ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion">
																					<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoDescuentoVariable}', evalScripts: true})">
																						<img src="images/autCaducada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}caducada">
																					</html:link>
																				</logic:notEmpty>
																				<logic:empty name="ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion">
																						<img src="images/autCaducada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}caducada">
																				</logic:empty>
				                                                            </c:if>
                                                                        </logic:notEmpty>
                                                                    </logic:notEmpty>
                                                                    <logic:empty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                                                    	
                                                                    	&nbsp;
                                                                    </logic:empty>
                                                                </td>
                                                                
                                                                <!-- COLUMNA CODIGO DE BARRAS -->
                                                                <td width="13%" class="columna_contenido fila_contenido" align="center" title="${titulo_fila}"><bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/></td>															
																
																<!-- COLUMNA DESCRIPCION ARTICULO -->
                                                                <td width="27%" class="columna_contenido fila_contenido" align="left">
                                                                    <table cellpadding="0" cellspacing="0" width="100%" class="textoNegro9">
                                                                        <tr>
                                                                            <c:if test="${articulo.codigoClasificacion == articuloEspecial|| articulo.codigoClasificacion == canastoCatalogo}">
                                                                                <%-- secci&oacute;n para obtener el c&oacute;digo de las canastas y crear el link --%>
                                                                                <td valign="middle" align="left">
                                                                                    <logic:notEmpty name="articulo" property="npNuevoCodigoClasificacion">
                                                                                        <img title="canasto modificado" src="images/estrella.gif" border="0">
                                                                                    </logic:notEmpty>
                                                                                    <logic:empty name="articulo" property="npNuevoCodigoClasificacion">
	                                                                                    <logic:notEmpty name="detalle" property="npCodigoClasificacion">
		                                                                    				<c:if test="${detalle.npCodigoClasificacion==articuloEspecial}">
		                                                                    					<img title="canasto modificado" src="images/estrella.gif" border="0">
		                                                                    				</c:if>
		                                                                    			</logic:notEmpty>
	                                                                    			</logic:empty>
	                                                                    			
	                                                                    			<bean:define id="codigoSubClasificacion" name="articulo" property="codigoSubClasificacion"/>
	                                                                    			
	                                                                    			<c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
																					  	<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
                                                                                        <bean:define id="descripcion" value="detalle de la despensa"/>
																					</c:if>
																					<c:if test="${fn:contains(tipoCanasto, codigoSubClasificacion)}">
																					  	<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
                                                                                        <bean:define id="descripcion" value="detalle del canasto"/>
																					</c:if>
                                                                                   <%-- <logic:equal name="articulo" property="codigoSubClasificacion" value="${canastoCatalogo}">
                                                                                        <bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
                                                                                        <bean:define id="descripcion" value="detalle de la despensa o del canasto"/>
                                                                                    </logic:equal>--%>
                                                                                    <%--<logic:equal name="articulo" property="codigoTipoArticulo" value="${tipoCanasto}">
                                                                                        <bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
                                                                                        <bean:define id="descripcion" value="detalle del canasto"/>
                                                                                    </logic:equal>--%>
                                                                                    <html:link href="javascript:requestAjax('detalleCanasta.do',['mensajes','div_pagina'],{parameters: 'indiceCanasta=${numeroRegistro}'})" title="${descripcion}"><bean:write name="articulo" property="descripcionArticulo"/>,&nbsp;<bean:write name="articulo" property="articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</html:link>
                                                                                </td>
                                                                                <td align="right">
                                                                                    <html:link href="javascript:requestAjax('detalleCanasta.do',['mensajes','div_pagina'],{parameters: 'indiceCanasta=${numeroRegistro}'})" title="${descripcion}" onclick=""><img title="${descripcion}" src="${pathImagenArticulo}" border="0"/></html:link>
                                                                                </td>
                                                                            </c:if>
                                                                            
                                                                            <c:if test="${articulo.codigoClasificacion != articuloEspecial && articulo.codigoClasificacion != canastoCatalogo}">
                                                                                
                                                                                <td align="left"><bean:write name="articulo" property="descripcionArticulo"/>,&nbsp;<bean:write name="articulo" property="articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
                                                                                <logic:equal name="pesoVariable" value="${estadoActivo}">
                                                                                    <td align="right">
                                                                                    <logic:equal name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                                                                     	<img title="peso variable/unidad Manejo" src="images/${imagen}" border="0">
                                                                                    </logic:equal>
                                                                                    <logic:notEqual name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                                                                        <img title="peso variable" src="images/${imagen}" border="0">
                                                                                    </logic:notEqual>    
                                                                                    </td>
                                                                                </logic:equal>
                                                                            </c:if>
                                                                            <td width="2px">&nbsp;</td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                                <td width="5%" class="columna_contenido fila_contenido" align="center">
                                                                    <logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
                                                                        <logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                            <logic:equal name="modificarDetallePedido" value="${estadoActivo}">
                                                                                <html:text property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}" styleClass="textNormal" style="text-align:right" size="4" maxlength="5" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok&indiceDetalle=${numeroRegistro}',evalScripts:true});return validarInputNumeric(event);"
                                                                                onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>
                                                                            </logic:equal>
                                                                            <logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
                                                                                <html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}" write="true"/>
                                                                            </logic:notEqual>
                                                                        </logic:notEqual>
                                                                        <logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                            <center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
                                                                            <html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}"/>
                                                                        </logic:equal>
                                                                    </logic:empty>
                                                                    <logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
                                                                        <logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                            <logic:equal name="modificarDetallePedido" value="${estadoActivo}">
                                                                                <bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/>
                                                                                <html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}"/>
                                                                            </logic:equal>
                                                                            <logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
                                                                                <html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}" write="true"/>
                                                                            </logic:notEqual>
                                                                        </logic:notEqual>
                                                                        <logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                            <center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
                                                                            <html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}"/>
                                                                        </logic:equal>
                                                                    </logic:notEmpty>
                                                                </td>
                                                               
                                                                <!-- STOCK -->
                                                      		    <!-- columna de stock -->
                                                                <td width="6%" class="columna_contenido fila_contenido" align="center">
                                                                	<table>
                                                                		<tr>
                                                                			<td  width="50%">
                                                                    <logic:notEmpty name="detalle" property="articuloDTO.npStockArticulo">                                                                    	
                                                                    	<c:if test="${detalle.articuloDTO.claseArticulo==articuloObsoleto}">
                                                                    		<html:link href="#" title="Stock Obsoleto" style="text-decoration:none; cursor: default;">
                                                                    			<bean:write name="detalle" property="articuloDTO.npStockArticulo"/>
                                                                    		</html:link>
                                                                    	</c:if>  
                                                                    	<c:if test="${detalle.articuloDTO.claseArticulo!=articuloObsoleto}">
                                                                    		<bean:write name="detalle" property="articuloDTO.npStockArticulo"/>
                                                                    	</c:if>                                                       
                                                                    </logic:notEmpty>
                                                                    <logic:empty name="detalle" property="articuloDTO.npStockArticulo">&nbsp;</logic:empty>
																			</td>
                                                                			<!-- columna de la imagen de la autorizacion de stock -->
                                                                			<td width="50%">
			                                                                	<logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol"> 
			                                                                		<c:set var="autorizacionStock" value="0"/>
			                                                                		<c:set var="estadoAutStockDetalle" value="0"/>
			                                                                        <logic:iterate name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutStockDTO" indexId="numeroAut">
			                                                                            <logic:notEmpty name="detalleAutStockDTO" property="estadoPedidoAutorizacionDTO">
				                                                                     		<c:if test="${codigoAutorizacionStock == detalleAutStockDTO.estadoPedidoAutorizacionDTO.npTipoAutorizacion}">	                                                                                
				                                                                                <smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.pendiente">
				                                                                                    <c:set var="autorizacionStock" value="1"/>
				                                                                                </smx:equal>                                                                            
				                                                                                <smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.solicitada">	
				                                                                                    <c:set var="autorizacionStock" value="2"/>
				                                                                                </smx:equal>
				                                                                                <smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.aprobada">	
				                                                                                    <c:set var="autorizacionStock" value="3"/>
				                                                                                </smx:equal>
				                                                                                <smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.rechazada">	
				                                                                                    <c:set var="autorizacionStock" value="4"/>
				                                                                                </smx:equal>
				                                                                                <smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.utilizada">	
				                                                                                    <c:set var="autorizacionStock" value="5"/>
				                                                                                </smx:equal>
				                                                                                <smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.caducada">	
				                                                                                    <c:set var="autorizacionStock" value="6"/>
				                                                                                </smx:equal>
				                                                                                <smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.gestionada">	
				                                                                                    <c:set var="autorizacionStock" value="7"/>
				                                                                                </smx:equal>
				                                                                                <c:if test="${autorizacionStock != 3}">
					                                                                        		<c:set var="estadoAutStockDetalle" value="${autorizacionStock}"/>
					                                                                        	</c:if>  
				                                                                            </c:if>
			                                                                            </logic:notEmpty>
			                                                                        </logic:iterate>
		                                                                         	 <c:if test="${estadoAutStockDetalle != 0}">
		                                                                         	 	<c:set var="autorizacionStock" value="${estadoAutStockDetalle}"/>
		                                                                         	 </c:if>
				                                                                        <c:if test="${autorizacionStock == 0}">
				                                                                           &nbsp;
				                                                                        </c:if>                                                                        
				                                                                        <c:if test="${autorizacionStock == 1}">
				                                                                           <img src="images/autPendiente.png" border="0" title="Autorizaci&oacute;n de stock pendiente">
				                                                                        </c:if>
				                                                                        <c:if test="${autorizacionStock == 2}">
				                                                                           	<html:link href="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'consultarAutorizacion=ok&indiceArticulo=${numeroRegistro}&tipoAutorizacion=${codigoAutorizacionStock}', evalScripts: true})">
				                                                                           		<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de stock solicitada">
				                                                                           	</html:link>
				                                                                        </c:if>
				                                                                        <c:if test="${autorizacionStock == 3}">
				                                                                        	<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de stock aprobada">			                 
				                                                                        </c:if>
				                                                                        <c:if test="${autorizacionStock == 4}">
				                                                                           	<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de stock rechazada">
				                                                                        </c:if>
				                                                                        <c:if test="${autorizacionStock == 5}">
				                                                                           	<img src="images/autUtilizada.png" border="0" title="Autorizaci&oacute;n de stock utilizada en otro momento">
				                                                                        </c:if>
				                                                                        <c:if test="${autorizacionStock == 6}">
				                                                                           	<img src="images/autCaducada.png" border="0" title="Autorizaci&oacute;n de stock caducada">
				                                                                        </c:if>
				                                                                        <c:if test="${autorizacionStock == 7}">
				                                                                           	<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de stock gestionada">
				                                                                        </c:if>
			                                                                        
			                                                                    </logic:notEmpty>
			                                                                    <logic:empty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                                                    	
			                                                                    	&nbsp;
			                                                                    </logic:empty>
                                                                			</td>
                                                                		</tr>
                                                                	</table>	
                                                                </td>
                                                                <!-- PESO -->
                                                                <td width="7%" class="columna_contenido fila_contenido" align="right">
                                                                <c:set var="habilitarCambioPesos" value=""/>
                                                                <bean:define id="codigoClasificacion" name="detalle" property="articuloDTO.codigoClasificacion"/>
                                                                <logic:iterate name="ec.com.smx.sic.sispe.parametro.clasificacionesArticulos.cambioPesosAux" id="codigo"> 
                                                                    <c:if test="${codigo == codigoClasificacion}"> 
                                                                      <c:set var="habilitarCambioPesos" value="${estadoActivo}"/>
                                                                    </c:if>	
                                                                </logic:iterate>
                                                                <logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">												
                                                                    <logic:equal name="pesoVariable" value="${estadoActivo}">
                                                                        <logic:equal name="habilitarCambioPesos" value="${estadoActivo}">
                                                                            <logic:empty name="ec.com.smx.sic.sispe.modificarPeso.inactivo">
                                                                                <logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                                    <center><html:text property="vectorPesoActual" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="7" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok'});return validarInputNumericDecimal(event);"
                                                                                    onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, false, '99999999999999999999', '2'):true"/></center>
                                                                                    <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
                                                                                </logic:notEqual>
                                                                                <logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                                    <logic:equal name="modificarDetallePedido" value="${estadoActivo}">
                                                                                        <center><html:text property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="7" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok'});return validarInputNumericDecimal(event);"
                                                                                        onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, false, '99999999999999999999', '2'):true"/></center>
                                                                                    </logic:equal>
                                                                                    <logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
                                                                                        <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" write="true"/>
                                                                                    </logic:notEqual>
                                                                                </logic:equal>
                                                                            </logic:empty>
                                                                            <logic:notEmpty name="ec.com.smx.sic.sispe.modificarPeso.inactivo">
                                                                                <logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                                    <center>
                                                                                        <!--<html:text property="vectorPesoActual" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="7" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'}); return validarInputNumeric(event);"/>-->
                                                                                        <bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
                                                                                    </center>
                                                                                    <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
																				<!--	<html:hidden property="vectorPesoActual" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/> Cambios Oscar -->
                                                                                </logic:notEqual>
                                                                                <logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                                    <logic:equal name="modificarDetallePedido" value="${estadoActivo}">
                                                                                        <center>
                                                                                        <!--<html:text property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="7" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});"/>-->
                                                                                        <bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
                                                                                        <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
                                                                                        </center>
                                                                                    </logic:equal>
                                                                                    <logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
                                                                                        <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" write="true"/>
                                                                                    </logic:notEqual>
                                                                                </logic:equal>
                                                                            </logic:notEmpty>
                                                                        </logic:equal>
                                                                        <logic:notEqual name="habilitarCambioPesos" value="${estadoActivo}">
                                                                            <logic:empty name="ec.com.smx.sic.sispe.modificarPeso.inactivo">
                                                                                <logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                                    <logic:equal name="modificarDetallePedido" value="${estadoActivo}">
                                                                                        <center><html:text property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="7" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok'});return validarInputNumericDecimal(event);"
                                                                                        onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, false, '99999999999999999999', '2'):true"/></center>
                                                                                    </logic:equal>
                                                                                    <logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
                                                                                        <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" write="true"/>
                                                                                    </logic:notEqual>
                                                                                </logic:equal>
                                                                            </logic:empty>
                                                                            <logic:notEmpty name="ec.com.smx.sic.sispe.modificarPeso.inactivo">
                                                                                <logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                                    <logic:equal name="modificarDetallePedido" value="${estadoActivo}">
                                                                                        <center>
                                                                                        <!--<html:text property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="7" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});"/>-->
                                                                                        <bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
                                                                                        <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/><!-- Cambios Oscar -->
                                                                                        </center>
                                                                                    </logic:equal>
                                                                                    <logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
                                                                                        <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" write="true"/>
                                                                                    </logic:notEqual>
                                                                                </logic:equal>
                                                                            </logic:notEmpty>
                                                                            <logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                                <center>
                                                                                	<bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado" formatKey="formatos.numeros.decimales"/>
                                                                                	<html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
                                                                                </center>
                                                                            </logic:notEqual>
                                                                        </logic:notEqual>
                                                                    </logic:equal>
                                                                    <logic:notEqual name="pesoVariable" value="${estadoActivo}">
                                                                        <center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
                                                                        <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
                                                                    </logic:notEqual>
                                                                </logic:empty>
                                                                <logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">												
                                                                    <logic:equal name="pesoVariable" value="${estadoActivo}">
                                                                        <logic:equal name="habilitarCambioPesos" value="${estadoActivo}">
                                                                            <logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                                <center>
                                                                                    <bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
                                                                                    <html:hidden property="vectorPesoActual" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
                                                                                </center>
                                                                                <center><html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/></center>
                                                                            </logic:notEqual>
                                                                            <logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                                <center><html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" write="true"/></center>
                                                                            </logic:equal>
                                                                        </logic:equal>
                                                                        <logic:notEqual name="habilitarCambioPesos" value="${estadoActivo}">
                                                                            <center><html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" write="true"/></center>
                                                                        </logic:notEqual>
                                                                    </logic:equal>
                                                                    <logic:notEqual name="pesoVariable" value="${estadoActivo}">
                                                                        <center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
                                                                        <html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
                                                                    </logic:notEqual>
                                                                </logic:notEmpty>
                                                                </td>
                                                                <%--PRECIO UNITARIO SIN IVa --%>
                                                                <td width="6%" class="columna_contenido fila_contenido" align="right">
                                                                	<table cellpadding="0" cellspacing="0" width="100%" class="textoNegro9">
                                                                        <tr>
	                                                                        <td valign="middle" align="left">
		                                                                    	<logic:notEmpty name="articulo" property="npNuevoCodigoClasificacion">
		                                                                         	<c:if test="${(detalle.estadoDetallePedidoDTO.valorUnitarioEstado==detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado)}">
		                                                                    			<img title="Sumatoria de valores de receta sin IVA." src="images/totalRojo.png" border="0">
			                                                                    	</c:if>  
			                                                                    	<c:if test="${(detalle.estadoDetallePedidoDTO.valorUnitarioEstado!=detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado)}">
		                                                                    			<img title="Sumatoria de valores de Receta sin IVA" src="images/totalVerde.png" border="0">
			                                                                    	</c:if>                                                             
					                                                         	</logic:notEmpty>
					                                                         	<logic:empty name="articulo" property="npNuevoCodigoClasificacion">
					                                                                <logic:notEmpty name="detalle" property="npCodigoClasificacion">
					                                                                <c:if test="${detalle.npCodigoClasificacion==articuloEspecial}">
					                                                                    <c:if test="${(detalle.estadoDetallePedidoDTO.valorUnitarioEstado==detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado)}">
			                                                                    			<img title="Sumatoria de valores de receta sin IVA." src="images/totalRojo.png" border="0">
				                                                                    	</c:if>  
				                                                                    	<c:if test="${(detalle.estadoDetallePedidoDTO.valorUnitarioEstado!=detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado)}">
			                                                                    			<img title="Sumatoria de valores de Receta sin IVA." src="images/totalVerde.png" border="0">
				                                                                    	</c:if>  
				                                                                    </c:if>
			                                                                    	</logic:notEmpty> 
	                                                                			</logic:empty>   
	                                                                        </td>
	                                                                        <td align="right">
		                                                                        <logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
			                                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
			                                                                            <logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
			                                                                            </logic:empty>                                                                            
			                                                                        </logic:notEmpty>
			                                                                        <logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
			                                                                            <logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
			                                                                            </logic:empty>
			                                                                        </logic:empty>
			                                                                    </logic:equal>
			                                                                    <logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
			                                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
			                                                                            <logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
			                                                                            </logic:empty>
			                                                                        </logic:notEmpty>
			                                                                        <logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
			                                                                            <logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
			                                                                            </logic:empty>
			                                                                        </logic:empty>
			                                                                    </logic:notEqual>
	                                                                        </td>
                                                                        </tr>
                                                                 	</table> 
                                                                </td>
                                                                <%--PRECIO UNITARIO IVa --%>                                                                
                                                                <td width="6%" class="columna_contenido fila_contenido" align="right">                                                                 
                                                                	<table cellpadding="0" cellspacing="0" width="100%" class="textoNegro9">
                                                                        <tr>
	                                                                        <td valign="middle" align="left">
		                                                                         <logic:notEmpty name="articulo" property="npNuevoCodigoClasificacion">
					                                                                <c:if test="${(detalle.estadoDetallePedidoDTO.valorUnitarioEstado==detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado)}">			                                                                    		
		                                                                    			<img title="Sumatoria de valores de receta con IVA." src="images/totalRojo.png" border="0">
			                                                                    	</c:if>  
			                                                                    	<c:if test="${(detalle.estadoDetallePedidoDTO.valorUnitarioEstado!=detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado)}">			                                                                    		
		                                                                    			<img title="Sumatoria de valores de Receta con IVA." src="images/totalVerde.png" border="0">
			                                                                    	</c:if> 	
					                                                              </logic:notEmpty>
					                                                              <logic:empty name="articulo" property="npNuevoCodigoClasificacion">
					                                                                <logic:notEmpty name="detalle" property="npCodigoClasificacion">
					                                                                <c:if test="${detalle.npCodigoClasificacion==articuloEspecial}">
					                                                                    <c:if test="${(detalle.estadoDetallePedidoDTO.valorUnitarioEstado==detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado)}">
			                                                                    			<img title="Sumatoria de valores de receta con IVA." src="images/totalRojo.png" border="0">
				                                                                    	</c:if>  
				                                                                    	<c:if test="${(detalle.estadoDetallePedidoDTO.valorUnitarioEstado!=detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado)}">
			                                                                    			<img title="Sumatoria de valores de Receta con IVA." src="images/totalVerde.png" border="0">
				                                                                    	</c:if>  
				                                                                    </c:if>
			                                                                    	</logic:notEmpty> 
	                                                                			</logic:empty>   
	                                                                        </td>
	                                                                        <td align="right">
		                                                                        <logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
			                                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
			                                                                            <logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
			                                                                                <html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
			                                                                            </logic:empty>
			                                                                            <logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                                <center><html:text property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}" styleClass="textNormal" size="7" maxlength="7" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok'});"/></center>
			                                                                            </logic:notEmpty>
			                                                                            <html:hidden property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
			                                                                        </logic:notEmpty>
			                                                                        <logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
			                                                                            <logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
			                                                                                <html:hidden property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
			                                                                            </logic:empty>
			                                                                            <logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                                <center><html:text property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}" styleClass="textNormal" size="7" maxlength="7" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok'});"/></center>
			                                                                            </logic:notEmpty>
			                                                                            <html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
			                                                                        </logic:empty>
			                                                                    </logic:equal>
			                                                                    <logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
			                                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
			                                                                            <logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
			                                                                                <html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
			                                                                            </logic:empty>
			                                                                            <logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                                <center><html:text property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}" styleClass="textNormal" size="10" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok'});"/></center>
			                                                                            </logic:notEmpty>
			                                                                            <html:hidden property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
			                                                                        </logic:notEmpty>
			                                                                        <logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
			                                                                            <logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
			                                                                                <html:hidden property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
			                                                                            </logic:empty>
			                                                                            <logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
			                                                                                <center><html:text property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}" styleClass="textNormal" size="10" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok'});"/></center>
			                                                                            </logic:notEmpty>
			                                                                            <html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
			                                                                        </logic:empty>
			                                                                    </logic:notEqual>
	                                                                        </td>
                                                                        </tr>
                                                                 	</table> 
                                                                </td>
                                                                <!-- COLUMNA VALOR UNITARIO CON IVA -->
                                                                <td width="7%" class="fila_contenido columna_contenido" align="right">
                                                                	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorPrevioVenta}"/>
                                                                </td>
                                                                <td class="columna_contenido fila_contenido" width="2%" align="center">
                                                                	<logic:notEmpty name="articulo" property="npNuevoCodigoClasificacion">
	                                                                    <c:if test="${(detalle.npAplicaImpuestoCanastoEspecial)}">
	                                                                		<img src="images/tick.png" border="0">
				                                                        </c:if>  
				                                                        <c:if test="${(!detalle.npAplicaImpuestoCanastoEspecial)}">
	                                                                		<img src="images/x.png" border="0">				                                                        	
				                                                        </c:if> 	       
                                                                    </logic:notEmpty>
                                                                	<logic:empty name="articulo" property="npNuevoCodigoClasificacion">
	                                                                    <c:if test="${aplicaImpuesto}">
																			<img src="images/tick.png" border="0">
																		</c:if>
																		<c:if test="${!aplicaImpuesto}">
																			<img src="images/x.png" border="0">
																		</c:if>
                                                                    </logic:empty>																	
                                                                </td>
                                                                <!-- COLUMNA DESCUENTOS -->
                                                                <td width="3%" class="fila_contenido columna_contenido" align="center">&nbsp;
                                                                	<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
                                                                	<!--  <logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0"><bean:write name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" /></logic:greaterThan> --> 
                                                                    <logic:lessThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0"><label class="textoRojo10">E</label></logic:lessThan>
                                                                </td>
                                                                <%--<td width="5%" class="fila_contenido columna_contenido" align="right">
                                                                    <bean:write name="detalle" property="estadoDetallePedidoDTO.valorUnitarioPOS" formatKey="formatos.numeros"/>
                                                                </td>--%>
                                                                
                                                                <!-- COLUMNA VALOR TOTAL -->
                                                                <td width="7%" class="fila_contenido columna_contenido columna_contenido_der" align="right">
                                                                	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorTotalVenta}"/>
                                                                </td>
                                                                <%-- prueba de datos
                                                                <td width="7%" class="fila_contenido columna_contenido columna_contenido_der" align="right">
                                                                    <bean:write name="detalle" property="estadoDetallePedidoDTO.valorTotalEstado" formatKey="formatos.numeros"/> (<bean:write name="detalle" property="estadoDetallePedidoDTO.valorTotalEstadoIVA" formatKey="formatos.numeros"/>)
                                                                </td>
                                                                --%>
                                                                </tr>
                                                                <%-- sección de detalle del artículo --%>																
																<tr>
																		<td colspan="4" align="right" valign="bottom" class="fila_contenido columna_contenido">
																		<div id="marco2${numeroRegistro}" class="displayNo">                                                                       
																				<div id="plegar2${numeroRegistro}">
																					<a title="Contraer" href="#" onClick="ocultar(${numeroRegistro},'marco2','plegar2','desplegar2');ocultar(${numeroRegistro},'marco3','plegar1','desplegar1');">
																						<html:img src="images/flechaVerdeArr.gif" border="0"/>
																					</a>
																				</div>   
																				<div id="desplegar2${numeroRegistro}" class="displayNo">
																					<a title="Ver detalle art&iacute;culo" onClick="mostrar(${numeroRegistro},'marco3','plegar1','desplegar1');">
																						<html:img src="images/desplegar.gif" border="0"/>
																					</a>
																				</div>	
																		</div>
																		</td>
																		<td colspan="12" class="columna_contenido" align="left">																																			
																			<div id="marco3${numeroRegistro}" class="displayNo">
																			 <logic:notEmpty name="detalle" property="articuloDTO.articuloRelacionCol">
																				<table width="100%" border="0" cellpadding="0" cellspacing="0">
																						<tr>
																							<td>
																								<table cellpadding="0" cellspacing="0" width="100%" align="left" class="tabla_informacion">
																									<tr class="tituloTablas">																										
																										<td align="center" class="columna_contenido" width="3%">No</td>
																										<td align="center" class="columna_contenido" width="14%">C&oacute;digo barras</td>
																										<td align="center" class="columna_contenido" width="30%">Art&iacute;culo</td>
																										<td align="center" class="columna_contenido" width="5%" >Cant.</td>
																										<td align="center" class="columna_contenido" width="7%">Stock</td>
																										<td align="center" class="columna_contenido" width="7%">&nbsp;</td>
																										<td align="center" class="columna_contenido" width="7%">V.Unit.</td>
																										<td align="center" class="columna_contenido" width="6%" >V.Unit.Iva</td>
																										<td align="center" class="columna_contenido" width="8%">Tot. Iva</td>
																										<td align="center" class="columna_contenido" width="2%">Iva</td>
																										<td align="center" class="columna_contenido" width="5%">V.Caj.</td>
																										<td align="center" class="columna_contenido" width="6%">V.May.</td>
																									</tr>
																								</table>
																							</td>
																						</tr>
																						<tr>
																							<td><%--
																								<div id="div_listado2" style="width:100%;height:auto;overflow-y:hidden;overflow-x:hidden;">--%>
																									<table cellpadding="0" cellspacing="0" width="100%" align="left" class="tabla_informacion">																										
																											<logic:iterate name="detalle" property="articuloDTO.articuloRelacionCol" id="receta" indexId="indiceRegistro">
																												<!-- control del estilo para el color de las filas -->
																												<bean:define id="residuo" value="${indiceRegistro % 2}"/>
																												<c:set var="clase" value="blanco10"/>
																												<logic:notEqual name="residuo" value="0">
																													<c:set var="clase" value="grisClaro10"/>
																												</logic:notEqual>
																												<c:set var="unidadManejo" value="${receta.articuloRelacion.unidadManejo}"/>
																												<logic:equal name="receta" property="articuloRelacion.habilitadoPrecioCaja" value="${true}">
																													<c:set var="unidadManejo" value="${receta.articuloRelacion.unidadManejoPrecioCaja}"/>
																												</logic:equal>
																												<%-- control de estilos para indicar el status completo del artículo en el SIC --%>
																												<c:choose>
																													<%-- DE BAJA EN EL SIC --%>
																													<c:when test="${receta.articuloRelacion.npEstadoArticuloSIC == articuloDeBaja}">
																														<c:set var="clase" value="verdeClaro10"/>
																														<c:set var="title_fila" value="art&iacute;culo dado de baja en el SIC"/>
																													</c:when>
																													<c:otherwise>
																														<c:choose>
																															<%-- SIN ALCANCE --%>
																															<c:when test="${receta.articuloRelacion.npAlcance == estadoInactivo}">
																																<c:set var="clase" value="rojoClaro10"/>
																																<c:set var="title_fila" value="art&iacute;culo sin ALCANCE"/>
																															</c:when>
																															<c:otherwise>
																																<c:choose>
																																	<%-- PROBLEMAS DE STOCK --%>
																																	<c:when test="${receta.articuloRelacion.npEstadoStockArticulo == estadoInactivo}">
																																		<c:set var="clase" value="naranjaClaro10"/>
																																		<c:set var="title_fila" value="art&iacute;culo sin STOCK"/>
																																	</c:when>
																																	<c:otherwise>
																																		<c:choose>
																																			<%-- INACTIVO EN EL SIC --%>
																																			<c:when test="${receta.articuloRelacion.npEstadoArticuloSIC == estadoInactivo}">
																																				<c:set var="clase" value="celesteClaro10"/>
																																				<c:set var="title_fila" value="art&iacute;culo inactivo en el SIC"/>
																																			</c:when>
																																		</c:choose>
																																	</c:otherwise>
																																</c:choose>
																															</c:otherwise>
																														</c:choose>
																													</c:otherwise>
																												</c:choose>
																												<%-- control del color para los implementos --%>
																												<logic:equal name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																													<c:set var="clase" value="violetaClaro10"/>
																												</logic:equal>
																												<bean:define id="fila" value="${indiceRegistro + 1}"/>
																												<c:set var="pesoVariable" value=""/>
																												<logic:equal name="receta" property="articuloRelacion.tipoCalculoPrecio" value="${tipoArticuloPavo}">
																													<c:set var="pesoVariable" value="${estadoActivo}"/>
																												</logic:equal>
																												<logic:equal name="receta" property="articuloRelacion.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
																													<c:set var="pesoVariable" value="${estadoActivo}"/>
																												</logic:equal>
																												<tr class="${clase}">																													
																													<td align="center" class="fila_contenido columna_contenido" width="3%"><bean:write name="fila"/></td>
																													<td align="center" class="fila_contenido columna_contenido" width="14%"><bean:write name="receta" property="articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
																													<td align="left" class="fila_contenido columna_contenido" width="30%">
																														<table cellpadding="0" cellspacing="0" width="100%">
																															<tr>
																																<td><bean:write name="receta" property="articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="receta" property="articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
																																<logic:equal name="pesoVariable" value="${estadoActivo}">
																																	<td align="right"><img title="peso variable" src="images/balanza.gif" border="0"></td>
																																</logic:equal>
																															</tr>
																														</table>
																													</td>
																													<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
																														<td align="center" class="fila_contenido columna_contenido" width="5%">
																															<logic:equal name="modificarDetallePedido" value="${estadoActivo}">																																
																																<bean:write name="receta" property="cantidad"/>
																															</logic:equal>
																															<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																																<html:hidden property="vectorcantidadCanasta" value="${receta.cantidadArticulo}" write="true"/>
																															</logic:notEqual>
																														</td>
																													</logic:empty>
																													<logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
																														<td align="center" class="fila_contenido columna_contenido" width="5%">
																															<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																																<bean:write name="receta" property="cantidad"/>
																																<html:hidden property="vectorcantidadCanasta" value="${receta.cantidad}"/>
																															</logic:equal>
																															<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																																<html:hidden property="vectorcantidadCanasta" value="${receta.cantidad}" write="true"/>
																															</logic:notEqual>
																														</td>
																													</logic:notEmpty>
																													<!--COLUMNA STOCK DE LA RECETA -->
																													<td align="right" class="fila_contenido columna_contenido" width="7%">
																														<logic:notEmpty name="receta" property="articuloRelacion.npStockArticulo">
																															<bean:write name="receta" property="articuloRelacion.npStockArticulo"/>
																														</logic:notEmpty>
																														<!-- ICONO ESTADO DE AUTORIZACION DE STOCK -->
																														<logic:notEmpty name="receta" property="articuloRelacion.npEstadoAutorizacion">																					
																															<c:set var="autorizacion" value="0"/>
																															<smx:equal name="receta" property="articuloRelacion.npEstadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.solicitada">
																																<c:set var="autorizacion" value="1"/>
																															</smx:equal>																																
																															<smx:equal name="receta" property="articuloRelacion.npEstadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.aprobada">
																																<c:set var="autorizacion" value="2"/>
																															</smx:equal>
																															<smx:equal name="receta" property="articuloRelacion.npEstadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.rechazada">
																																<c:set var="autorizacion" value="3"/>
																															</smx:equal>	
																															 <c:if test="${autorizacion == 0}">
																																&nbsp;
																															 </c:if>                                                                        
																															 <c:if test="${autorizacion == 1}">
																																<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de stock pendiente">
																															 </c:if>
																															 <c:if test="${autorizacion == 2}">								            	
																																<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de stock aprobada">
																															 </c:if>
																															 <c:if test="${autorizacion == 3}">
																																<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de stock rechazada">
																															 </c:if>																					
																														</logic:notEmpty>
																														<logic:empty name="receta" property="articuloRelacion.npStockArticulo">&nbsp;</logic:empty>
																													</td>
																													<td align="right" class="fila_contenido columna_contenido" width="7%">
																														&nbsp;
																													</td>
																													<td class="fila_contenido columna_contenido" width="7%" align="right">
																														<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitario">
																															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBase}"/>
																														</logic:empty>
																														<logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitario">																															
																															<logic:equal name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBase}"/>
																															</logic:equal>
																														</logic:notEmpty>
																													</td>
																													<td class="fila_contenido columna_contenido" width="6%" align="right">
																														<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitario">
																															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBaseImp}"/>
																															<html:hidden property="vectorPrecioItem" value="${receta.articuloRelacion.precioBaseImp}"/>
																														</logic:empty>
																														<logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitario">
																															<logic:notEqual name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																																<html:text property="vectorPrecioItem" value="${receta.articuloRelacion.precioBaseImp}" styleClass="textNormal" size="10" maxlength="8" onkeypress="requestAjaxEnter('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'actualizarCanasto=ok'});"/>
																															</logic:notEqual>
																															<logic:equal name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBaseImp}"/>
																																<html:hidden property="vectorPrecioItem" value="${receta.precioBaseImp}"/>
																															</logic:equal>
																														</logic:notEmpty>
																													</td>
																													<td align="right" class="fila_contenido columna_contenido" width="8%">
																														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.precioTotalIMP}"/>
																													</td>
																													<td align="center" class="fila_contenido columna_contenido" width="2%">
																														
																														<c:if test="${receta.articuloRelacion.aplicaImpuestoVenta }">
																															<img src="images/tick.png" border="0">
																														</c:if>
																														<c:if test="${!receta.articuloRelacion.aplicaImpuestoVenta }">
																															<img src="images/x.png" border="0">
																														</c:if>
																													</td>
																													<logic:notEmpty name="ec.com.smx.sic.sispe.local.activo.precioMayorista">
																														<td class="fila_contenido columna_contenido textoAzul10" width="5%" align="center">
																															<bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
																														</td>
																														<c:if test="${receta.articuloRelacion.npHabilitarPrecioMayorista == estadoActivo && receta.articuloRelacion.precioMayorista > 0}">
																															<td class="fila_contenido columna_contenido" width="6%" align="right">
																																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioMayoristaImp}"/>
																															</td>
																														</c:if>
																														<c:if test="${receta.articuloRelacion.npHabilitarPrecioMayorista != estadoActivo}">
																															<td class="fila_contenido columna_contenido textoAzul10" width="6%" align="center">
																																<bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
																															</td>
																														</c:if>
																													</logic:notEmpty>
																													<logic:empty name="ec.com.smx.sic.sispe.local.activo.precioMayorista">
																														<c:if test="${receta.articuloRelacion.habilitadoPrecioCaja == true && receta.articuloRelacion.precioCaja > 0}">
																															<td class="fila_contenido columna_contenido" width="5%" align="right">
																																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioCajaImp}"/>
																															</td>
																														</c:if>
																														<c:if test="${receta.articuloRelacion.habilitadoPrecioCaja == false}">
																															<td class="fila_contenido columna_contenido textoAzul10" width="5%" align="center">
																																<bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
																															</td>
																														</c:if>
																														<td class="fila_contenido columna_contenido textoAzul10" width="6%" align="center">
																															<bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
																														</td>
																													</logic:empty>																												
																												</tr>
																											</logic:iterate>
																									</table>
																								<%--</div> --%>
																							</td>
																						</tr>																						
																				</table>
																				</logic:notEmpty>
																			</div>																			
																	</td>
																</tr>
                                                            <logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.npDetalleUnidades">
                                                                <tr>
                                                                    <td></td>
                                                                    <td colspan="11">
                                                                        <div id="marco5${numeroRegistro}" class="displayNo">
                                                                            <table border="0" class="tabla_informacion_negro" width="50%" align="left" cellspacing="0" cellpadding="0">
                                                                                <tr>
                                                                                    <td align="center">
                                                                                        <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                                                            <tr class="tituloTablasCeleste"  align="left">
                                                                                                <td class="fila_contenido_negro" width="100%" align="center">DESCRIPCION</td>
                                                                                            </tr>
                                                                                        </table>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>
                                                                                        <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                                                            <logic:iterate name="detalle" property="estadoDetallePedidoDTO.npDetalleUnidades" id="detalleUnidades" indexId="indiceDetalleCajas">
                                                                                                <bean:define id="fila" value="${indiceDetalleCajas + 1}"/>
                                                                                                <%-- control del estilo para el color de las filas --%>
                                                                                                <bean:define id="residuo" value="${indiceDetalleCajas % 2}"/>
                                                                                                <logic:equal name="residuo" value="0">
                                                                                                    <bean:define id="clase" value="blanco10"/>
                                                                                                </logic:equal>
                                                                                                <logic:notEqual name="residuo" value="0">
                                                                                                    <bean:define id="clase" value="grisClaro10"/>
                                                                                                </logic:notEqual>
                                                                                                <bean:define id="contador" value="${contador+1}" toScope="session"/>
                                                                                                <tr class="${clase}" id="fila_${indiceDetalleCajas}">
                                                                                                    <td class="columna_contenido fila_contenido" width="100%" align="left"><bean:write name="detalleUnidades"/></td>
                                                                                                </tr>
                                                                                            </logic:iterate>
                                                                                        </table>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </logic:notEmpty>
                                                        </logic:iterate>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </logic:notEmpty>
                                    <logic:empty name="ec.com.smx.sic.sispe.detallePedido">
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
                                    <logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
                                    <logic:equal name="modificarDetallePedido" value="${estadoActivo}">
                                        <tr>
                                            <td class="celesteFosforescente" width="100%">
                                                
                                                    <c:set var="descripcionArticulo" value=""/>
                                                    <c:set var="stockArticulo" value=""/>
                                                    <c:set var="descripcionProblema" value=""/>
                                                    <logic:notEmpty name="ec.com.smx.sic.sispe.articulo.problemasStock">
                                                        <c:set var="descripcionArticulo"><bean:write name="ec.com.smx.sic.sispe.articuloDTO" property="descripcionArticulo"/></c:set>
                                                        <c:set var="stockArticulo"><bean:write name="ec.com.smx.sic.sispe.articuloDTO" property="npStockArticulo"/></c:set>
                                                        <c:set var="descripcionProblema" value="Problema de stock"/>
                                                    </logic:notEmpty>
                                                    <logic:notEmpty name="ec.com.smx.sic.sispe.articulo.problemasAlcance">
                                                        <c:set var="descripcionArticulo"><bean:write name="ec.com.smx.sic.sispe.articuloDTO" property="descripcionArticulo"/></c:set>
                                                        <c:set var="descripcionProblema" value="Problema de alcance"/>
                                                    </logic:notEmpty>
                                                    <table cellpadding="0" cellspacing="0" width="98%" align="left">
                                                    <tr>
                                                        <td align="center" width="3%"><img src="images/flechader.gif" border="0"></td>
                                                        <td align="right" width="28%">
                                                            <label class="textoAzul11">&nbsp;C&oacute;digo barras:&nbsp;</label><html:text property="codigoArticulo" size="17" styleClass="textNormal" 
                                                            onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"
                                                            onkeypress="return validarInputNumeric(event);" onkeyup="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'agregarArticulo=ok&ingresoManual=ok&articuloNuevo=ok',evalScripts:true});"/>
                                                        </td>
                                                        <td align="right" width="23%">
                                                            <label class="textoAzul11">Cantidad/Peso KG:&nbsp;</label><html:text property="cantidadArticulo" size="6" maxlength="5" styleClass="textNormal" value="1" onkeypress="return validarInputNumeric(event);" 
                                                            onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"
                                                            onkeyup="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'agregarArticulo=ok&ingresoManual=ok&articuloNuevo=ok',evalScripts:true});"/>
                                                        </td>
                                                        <td align="center" width="30%" class="textoRojo10">
                                                            <logic:notEmpty name="descripcionArticulo">
                                                                <bean:write name="descripcionArticulo"/>
                                                            </logic:notEmpty>
                                                            <logic:empty name="descripcionArticulo">&nbsp;</logic:empty>
                                                        </td>
                                                        <td colspan="6" align="left">
                                                            <logic:notEmpty name="descripcionProblema">
                                                                <table cellpadding="0" cellspacing="0" width="100%">
                                                                    <tr>
                                                                        <td><img src="images/advertencia_16.gif" border="0">&nbsp;</td>
                                                                        <td>
                                                                            <bean:write name="descripcionProblema"/>&nbsp;
                                                                            <logic:notEmpty name="stockArticulo">
                                                                                (Stock: <bean:write name="stockArticulo"/>)
                                                                            </logic:notEmpty>
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
                                        <logic:empty name="ec.com.smx.sic.sispe.cotizar.primeraVez.bodega">
	                                        <logic:empty name="ec.com.smx.sic.sispe.pedido.popupDescuento">
		                                        <logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacion">
		                                            <script language="JavaScript" type="text/javascript">Field.activate('codigoArticulo');</script>
		                                        </logic:empty>
	                                        </logic:empty>
                                        </logic:empty>
                                        
                                    </logic:equal>
                                    </logic:empty>
                                </table>
																			
</div>