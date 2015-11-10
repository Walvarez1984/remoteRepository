<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@page contentType="application/ms-excel" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<tiles:useAttribute id="numeroBonos" name="nBonos" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="montoCompraMinima" name="mCompra" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="valorBono" name="mBono" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="valorCalculado" name="vCalculado" classname="java.lang.String" ignore="true"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo">
	<bean:define name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo" id="tipoDescuentoMaxiNavidad"/>
</logic:notEmpty>
<tr><td height="20px"></td></tr>
<tr>
	<td align="left">
		<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOS">
			COMPROBANTE BONO NAVIDAD EMPRESARIAL
		</logic:notEmpty>	
		<logic:empty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOS">
			INFORMACION BONO NAVIDAD EMPRESARIAL
		</logic:empty>
	</td>
</tr>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentos">
	<logic:iterate name="ec.com.smx.sic.sispe.pedido.descuentos" id="descuento" indexId="numDescuento">
			<bean:define id="idMaxi" name="descuento" property="descuentoDTO.tipoDescuentoDTO.id.codigoTipoDescuento"/>
			<c:if test="${idMaxi == tipoDescuentoMaxiNavidad}"> 
			   <bean:define id="vDes" name="descuento" property="valorDescuento"/>
			   <bean:define id="porcentaje" name="descuento" property="porcentajeDescuento"/>
			</c:if>
	</logic:iterate>
</logic:notEmpty>
<tr><td><hr/></td></tr>
<tr>
	<td width="100%">
		<table border="0" align="left" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO">
					<td align="left">
						No de Pedido:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="id.codigoPedido"/>
					</td>					
				</logic:notEmpty>
			</tr>
			<tr><td height="5px"></td></tr>
			<tr>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOS">
					<td align="left">
						No de Reservaci&oacute;n:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOS"/>
					</td>
				</logic:notEmpty>
			</tr>
			<tr><td height="5px"></td></tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<table cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td align="right">					
					<table border="0" width="100%" cellspacing="0" cellpadding="1" align="left" class="textoNegro10" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 11px;text-align:justify">											
						<%--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp --%>
						<tiles:insert page="/contacto/cabeceraContactoFormulario.jsp">	
							<tiles:put name="tformName" value="cotizarRecotizarReservarForm"/>
							<tiles:put name="textoNegro" value="textoNegro10"/>
							<tiles:put name="textoAzul" value="textoAzul10"/>
						</tiles:insert>																																										
					</table>
				</td>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.empresarial">
					<td valign="top">
						<table border="0" width="100%" cellspacing="0">
							<tr align="center">
								<td colspan="2" align="left">Datos de la Empresa:</td>
							</tr>
							<tr><td height="3px" colspan="2"></td></tr>
							<tr>
								<td width="10%" align="right">Ruc:</td>
								<td align="left"><bean:write name="cotizarRecotizarReservarForm" property="rucEmpresa"/></td>
							</tr>
							<tr>
								<td align="right">Empresa:</td>
								<td align="left"><bean:write name="cotizarRecotizarReservarForm" property="nombreEmpresa"/></td>
							</tr>
						</table>
					</td>
				</logic:notEmpty>
			</tr>
		</table>
	</td>
</tr>
<tr><td height="7px"></td></tr>
<tr>
	<td align="left">
		<table border="0" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left" width="20%">
					Fecha de Elaboraci&oacute;n:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedido.fechaPedido" formatKey="formatos.fecha"/>
				</td>
				<td align="left" width="40%">
					Elaborado en:&nbsp;<bean:write name="cotizarRecotizarReservarForm" property="localResponsable"/>
				</td>
			</tr>
			<tr><td height="7px"></td></tr>
			<tr>
				<logic:empty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
					<td align="left" width="50%">
						Tel&eacute;fono Local:&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal"/>
					</td>
					<td align="left" width="50%">
						Administrador Local:&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador"/>
					</td>
				</logic:empty>
				<logic:notEmpty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
					<td align="left" width="50%">
						Tel&eacute;fono Local:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal"/>
					</td>
					<td align="left" width="50%">
						Administrador Local:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.administradorLocal"/>
					</td>
				</logic:notEmpty>
			</tr>
		</table>
	</td>
</tr>
<tr><td><hr/></td></tr>
<tr>
	<td colspan="2" align="right">
		<table border="0" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left" colspan="2">
					En sus pagos de CONTADO Y CREDITO CORRIENTE por cada $<bean:write name="montoCompraMinima"/> dólares de compra, reciba un bono de $<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorBono}"/> dólares.
				</td>
			</tr>
			<tr><td height="30px">&nbsp;</td></tr>
<!--		<tr>
				<logic:empty name="ec.com.smx.sic.sispe.navEmpCre.seleccionado">
					<td align="left" width="30%">VALOR CALCULADO:&nbsp;&nbsp;&nbsp;</td>
					<td align="left">
						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorCalculado}"/>
					</td>
				</logic:empty>
			</tr>-->
			<!-- validacion solo se muestro numero de bonos a recibir soloo cuando exista una reserva-->
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOS"><!--Cambios Oscar --><!--Cambios Oscar -->
			<tr><td align="left" colspan="2">--------------------------</td></tr>
			<tr>
				
				<logic:notEmpty name="ec.com.smx.sic.sispe.navEmpCre.seleccionado">
						<td align="left" width="30%">NAVIDAD EMPRESARIAL CREDITO:&nbsp;&nbsp;&nbsp;</td>
				</logic:notEmpty>
				
				<logic:empty name="ec.com.smx.sic.sispe.navEmpCre.seleccionado">
					<td align="left" width="30%">NAVIDAD EMPRESARIAL:&nbsp;&nbsp;&nbsp;</td>
					<td align="left"><bean:write name="vDes"/>&nbsp;&nbsp;&nbsp;
						<bean:write name="porcentaje"/>%
					</td>
				</logic:empty>
			</tr>
			<tr>
				<td align="left" width="30%">No BONOS A RECIBIR:&nbsp;&nbsp;&nbsp;</td>
				<td align="left">
					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${numeroBonos}"/>
				</td>
			</tr>
			<tr><td align="left" colspan="2">--------------------------</td></tr>
			<tr><td height="60px">&nbsp;</td></tr>
			<tr>
				<td colspan="2" align="right">
					<table border="0" width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" colspan="2">INFORMACIÓN DE ENTREGA DE BONOS</td>
						</tr>
						<tr><td colspan="2"><hr/></td></tr>
						<tr><td height="20px">&nbsp;</td></tr>
						<tr>
							<td align="left" width="25%"># FACTURA:</td>
							<td align="left">______________________________________________</td>
						</tr>
						<tr><td height="18px"></td></tr>
						<tr>
							<td align="left" width="25%">FORMA PAGO:</td>
							<td align="left">______________________________________________</td>
						</tr>
						<tr><td height="18px"></td></tr>
						<tr>
							<td align="left" width="25%">FECHA ENTREGA:</td>
							<td align="left">______________________________________________</td>
						</tr>
					</table>
				</td>
			</tr>
			</logic:notEmpty>			
		</table>
	</td>
</tr>
<tr><td height="220px">&nbsp;</td></tr>
<tr>
	<td colspan="2" align="right">
		<table border="0" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" width="30%">_________________________________</td>
				<td align="center" width="30%">_________________________________</td>
			</tr>
			<tr>
				<td align="center" width="30%">Firma del Cliente</td>
				<td align="center" width="30%">Firma Persona Responsable del Local</td>
			</tr>
			<tr><td height="28px">&nbsp;</td></tr>
			<tr>
				<td align="center" width="30%">NOMBRE:____________________________</td>
				<td align="center" width="30%">NOMBRE:____________________________</td>
			</tr>
		</table>
	</td>
</tr>