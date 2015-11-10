<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<tiles:useAttribute id="codigoEstablecimiento" name="codEst" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="codigoEstablecimientoAki" name="codEstAki" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute name="estadoAfilacion" id="preciosAfiliado" />


							
<%-- mensaje para los establecimientos que no son AKI --%>
<logic:notEqual name="codigoEstablecimiento" value="${codigoEstablecimientoAki}">
	<tr>
		<td width="25px" valign="top">1.- </td>
		<td>Para obtener el precio de afiliado, debe presentar la tarjeta de afiliaci&oacute;n (aplican restricciones).</td>
	</tr>
	<tr>
		<td width="25px" valign="top">2.- </td>
		<td>Los precios anotados son de afiliado 
		<logic:notEmpty name="preciosAfiliado">
			__X__ No afiliado ___</td>
		</logic:notEmpty>
		<logic:empty name="preciosAfiliado">
			___ No afiliado __X__</td>
		</logic:empty>
	</tr>
	<tr>
		<td width="25px" valign="top">3.- </td>
		<td>El pago podra ser: efectivo, tarjeta de cr&eacute;dito, cheque, etc.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">4.- </td>
		<td>
			Para el pago con cheque presentar la tarjeta de afiliaci&oacute;n que deber&aacute; ser del titular de la cuenta<br/>
			corriente y el cheque a nombre de Supermaxi, Megamaxi o Corporaci&oacute;n Favorita C.A. (aplican restricciones).
		</td>
	</tr>
	<tr>
		<td width="25px" valign="top">5.- </td>
		<td>En caso de efectuarse retenci&oacute;n a la fuente deber&aacute; registrarse a nombre de Corporaci&oacute;n Favorita C.A.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">6.- </td>
		<td>Si se va a requerir la factura debe canjearse la nota de venta, acercandose a servicios al cliente y presentar<br/>
			el RUC y la raz&oacute;n social.
		</td>
	</tr>
	<tr>
		<td width="25px" valign="top">7.- </td>
		<td>La confirmaci&oacute;n del cliente para surtir esta proforma, debe ser con un m&iacute;nimo de 72 horas de anticipaci&oacute;n.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">8.- </td>
		<td>Al confirmar debe ser cancelado el valor total de la proforma.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">9.- </td>
		<td>La mercader&iacute;a est&aacute; sujeta a disponibilidad.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">10.- </td>
		<td>Al confirmar el valor total de la proforma puede variar debido a un posible costo de flete si existen entregas a domicilio.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">11.- </td>
		<td>Al confirmar el valor total de la proforma puede variar si existen art&iacute;culos de peso variable ya que estos se reservan con el peso medio y en el momento de ser pesados puede variar el costo.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">12.- </td>
		<td>Los precios ser&aacute;n ajustados a favor del cliente.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">13.- </td>
		<td>Los valores autorizados para los art&iacute;culos que aplican a diferidos son:</td>
	</tr>
	<logic:notEmpty name="ec.com.smx.sic.sispe.diferidoCuotas">
		<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO1" indexId="indiceCuota">
			<tr>
			  <td align="right" colspan="1">&nbsp;&nbsp;&nbsp;<bean:write name="cuotaDTO1" property="numeroCuotas"/></td>
			  <td align="left">&nbsp;&nbsp;MESES&nbsp;$&nbsp;<bean:write name="cuotaDTO1" property="valorMinimo"/></td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
	<tr>
		<td width="25px" valign="top">14.- </td>
		<td>Por la restricci&oacute;n de la ley, EST&Aacute; PROHIBIDA LA VENTA Y ENTREGA DE LICORES LOS D&Iacute;AS DOMINGOS</td>
	</tr>
</logic:notEqual>
<%-- mensaje para los establecimientos AKI --%>
<logic:equal name="codigoEstablecimiento" value="${codigoEstablecimientoAki}">
	<tr>
		<td width="25px" valign="top">1.- </td>
		<td>El pago podra ser: efectivo, Ttrjeta de cr&eacute;dito, cheque, etc.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">2.- </td>
		<td>En caso de efectuarse retenci&oacute;n a la fuente deber&aacute; registrarse a nombre de Corporaci&oacute;n Favorita C.A.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">3.- </td>
		<td>Si se va a requerir la factura debe canjearse la nota de venta, acercandose a servicios al cliente y presentar<br/>
			el RUC y la raz&oacute;n social.
		</td>
	</tr>
	<tr>
		<td width="25px" valign="top">4.- </td>
		<td>La confirmaci&oacute;n del cliente para surtir esta proforma, debe ser con un m&iacute;nimo de 72 horas de anticipaci&oacute;n.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">5.- </td>
		<td>Al confirmar debe ser cancelado el valor total de la proforma.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">6.- </td>
		<td>La mercader&iacute;a est&aacute; sujeta a disponibilidad.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">7.- </td>
		<td>Al confirmar, el valor total de la proforma puede variar debido a un posible costo de flete si existen entregas a domicilio.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">8.- </td>
		<td>Al confirmar, el valor total de la proforma puede variar si existen art&iacute;culos de peso variable ya que estos se reservan con el peso medio y en el momento de ser pesados puede variar el costo.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">9.- </td>
		<td>Los precios ser&aacute;n ajustados a favor del cliente.</td>
	</tr>
	<tr>
		<td width="25px" valign="top">10.- </td>
		<td>Por la restricci&oacute;n de la ley, EST&Aacute; PROHIBIDA LA VENTA Y ENTREGA DE LICORES LOS D&Iacute;AS DOMINGOS</td>
	</tr>
</logic:equal>