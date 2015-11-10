<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<logic:notEmpty name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloDTO">
  <bean:define id="calendarioArticuloDTO" name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloDTO"/>
</logic:notEmpty>
<tiles:useAttribute id="vformName" name="vtformName"  classname="java.lang.String" ignore="true"/>
    <div id="zonaMensajes" style="font-size:1px">
		<div id="zonaMensajes1" style="display:block">
			<tiles:insert page="/include/mensajes.jsp"/>
		</div>
	</div>	
	<table border="0" cellspacing="0" cellpadding="0" width="100%">	
  		<tr>
  			<td>
  			  <logic:notEmpty name="ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol">
  			  	<table  border="0" cellspacing="0" cellpadding="5" width="100%">
  					<tr>
			      		<td class="textoNegro11" align="left">Tipo Movimiento:
					   	 <smx:select property="tipoMotMov" styleClass="textObligatorio" styleError="campoError">
					      <html:option value="seleccione">Seleccione</html:option>
					      <html:options collection="ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol" labelProperty="descripcionMotivoMovimiento" property="id.codigoMotivoMovimiento"/>
					   	 </smx:select>   
			      		</td>
  					</tr>
  				</table> 
  			  </logic:notEmpty>	
  			</td>
 	    </tr>
 		<tr>	
			<td>
  				<table  border="0" cellspacing="0" cellpadding="5" width="100%">
  					<tr>
  						<td width="15%" align="left" class="textoNegro11">Cantidad:</td>
		 				<td align="left" class="textoNegro11">
						 <smx:text property="cantidad" size="15" styleClass="textObligatorio" styleError="campoError" />
						</td>
  					</tr>
  				</table>
  			</td>	
 		</tr>	
 		<tr>
 			<td>
  				<table  border="0" cellspacing="0" cellpadding="5" width="100%">
  					<tr>
  						<td width="15%" align="left" class="textoNegro11">Observación:</td>
 						<td align="left" class="textoNegro11">
							<smx:textarea rows="2" cols="40" property="observacion" styleClass="textObligatorio" styleError="campoError"/>
						</td>
  					</tr>
  				</table>
  			</td>	
 		</tr>
	</table>