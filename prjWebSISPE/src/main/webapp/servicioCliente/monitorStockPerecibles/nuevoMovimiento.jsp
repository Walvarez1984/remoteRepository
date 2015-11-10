<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<table cellpadding="0" cellspacing="0" width="100%" border="0">
	 <tr>
  		<td valign="top" style="position:relative;">
             <tiles:insert  page="/include/mensajes.jsp" />
        </td>
     </tr>        
	<tr>
		<td width="25%" valign="top">
			<table width="100%" cellspacing="0" cellpadding="0" class="tabla_informacion">
				<tr>
					<td class="fila_titulo" height="20px">
						<table cellpadding="0" cellspacing="0" align="left" width="100%">
							<tr>
							<td align="left">&nbsp;Datos del Art&iacute;culo: </td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<logic:notEmpty name="ec.com.smx.sic.sispe.articulo.perecible.seleccionado">
							<table cellpadding="1" cellspacing="2" class="tabla_informacion" width="100%">
								
								<bean:define id="articuloSeleccionado"  name="ec.com.smx.sic.sispe.articulo.perecible.seleccionado"/>													
								<tr>
									<td class="textoAzul11" align="left" width="15%">
										C&oacute;digo barras:
									</td>
									<td align="left" width="40%">
										${articuloSeleccionado.articulo.codigoBarrasActivo.id.codigoBarras}
									</td>
									<td width="45%">
										&nbsp;
									</td>
								</tr>
								
								<tr>
									<td class="textoAzul11" align="left">
										Descripci&oacute;n:
									</td>
									<td align="left">
										${articuloSeleccionado.articulo.descripcionArticulo}
									</td>
								</tr>
								
								<tr>
									<td class="textoAzul11" align="left">
										Proveedor:
									</td>
									<td align="left">
										${articuloSeleccionado.nombreProveedor}
									</td>
								</tr>
								
								
								
							</table>
						</logic:notEmpty>
					</td>
				</tr>
				
				<tr height="1px"><td></td></tr>
				
			</table>
		</td>
	</tr>
	<tr height="2px">	
		<td></td>
	</tr>
	<tr>
		<td width="74%" valign="top">
			<table width="100%" cellspacing="0" cellpadding="0" class="tabla_informacion">
				<tr>
					<td class="fila_titulo" height="20px">
						<table cellpadding="0" cellspacing="0" align="left" width="100%">
							<tr>
							<td align="left">&nbsp;Creaci&oacute;n Movimiento </td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table cellpadding="1" cellspacing="2" class="tabla_informacion" width="100%">														
							<tr>
								<td class="textoAzul11" align="left" width="15%">
									Motivo Movimiento:*
								</td>
								<td align="left" width="30%">
									
									<bean:define id="listamot"  name="ec.com.smx.sic.sispe.lista.motivos.movimientos"/>
									<c:set var="longitud" value="${listamot.size()}"/>
									<c:set var="valor" value="${1}"/>
									<c:if test="${longitud == valor}">
										  
										<smx:select name="listadoPedidosForm" property="comboMotivoMovimiento" styleClass="comboFijoOpacity220px" styleError="campoError"  disabled="true">
											<logic:notEmpty name="ec.com.smx.sic.sispe.lista.motivos.movimientos">
												<logic:iterate name="ec.com.smx.sic.sispe.lista.motivos.movimientos" id="motivoMovimiento" indexId="indiceM">													
													<logic:notEmpty name="motivoMovimiento" property="listMotMov">
														<logic:iterate name="motivoMovimiento" property="listMotMov" id="cmm" indexId="indiceCal">
															<html:option value="${cmm.id.codigoMotivoMovimiento}">
																&nbsp;&nbsp;&nbsp;${cmm.descripcionMotivoMovimiento}
															</html:option>
														</logic:iterate>
													</logic:notEmpty>
													
												</logic:iterate>
											</logic:notEmpty>
										</smx:select>
									</c:if>
									
									<c:if test="${longitud > valor}">
										<smx:select name="listadoPedidosForm" property="comboMotivoMovimiento" styleClass="comboFijo220px" style="width:230px;" styleError="campoError">
											<html:option value="">Seleccione</html:option>
											<logic:notEmpty name="ec.com.smx.sic.sispe.lista.motivos.movimientos">
												<logic:iterate name="ec.com.smx.sic.sispe.lista.motivos.movimientos" id="motivoMovimiento" indexId="indiceM">
													<html:option value="" styleClass="comboDescripcionCiudad">
														${motivoMovimiento.tipoMovimiento}
													</html:option>
													
													<logic:notEmpty name="motivoMovimiento" property="listMotMov">
														<logic:iterate name="motivoMovimiento" property="listMotMov" id="cmm" indexId="indiceCal">
															<html:option value="${cmm.id.codigoMotivoMovimiento}">
																&nbsp;&nbsp;&nbsp;${cmm.descripcionMotivoMovimiento}
															</html:option>
														</logic:iterate>
													</logic:notEmpty>
													
												</logic:iterate>
											</logic:notEmpty>
										</smx:select>
									</c:if>
								</td>
								<td width="55%">
									&nbsp;
								</td>
							</tr>
							
							<tr>
								<td class="textoAzul11" align="left">
									Cantidad:*
								</td>
								<td align="left">
									<input style="display:none;" type="text" name="oculto" />
									<html:text name="listadoPedidosForm" property="cantidadMotivoMovimiento" size="15" styleClass="textNormal" maxlength="10" />
								</td>
							</tr>
							
						</table>
					</td>
				</tr>
				
				<tr height="1px"><td></td></tr>
				
			</table>
		</td>
	</tr>
</table>