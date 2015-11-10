<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="100%">
	<logic:notEmpty name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloDTO">
	  <bean:define id="calendarioArticuloDTO" name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloDTO"/>
	</logic:notEmpty>
	<logic:notEmpty name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloHijoDTO">
	  <bean:define id="calendarioArticuloDTOHijo" name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloHijoDTO"/>
	</logic:notEmpty>
	<logic:empty name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloDTO">
	  <tr>
	  	<td align="left">
	  	  <table  border="0" cellspacing="0" cellpadding="0" class="tabla_informacion" width="69%">
	  	  	<tr>
			   <td align="left" class="textoAzul11">&nbsp;Fecha:
				<html:text property="fechaCalendario" styleClass="textNormal" size="15" maxlength="15"/>			
			   </td>
			   <td align="left">
				 &nbsp;<smx:calendario property="fechaCalendario" key="formatos.fecha"/>
			   </td>
			   <td width="15px"></td>
			   <td class="textoAzul11" align="left" id="articulos">  
				<logic:notEmpty name="ec.com.smx.sic.sispe.lineaProduccion.colArticulos">Artículo:&nbsp;
				 <smx:select property="articuloCombo" styleClass="textObligatorio" styleError="campoError">
				  <html:option value="vacio">Seleccione</html:option>
					<logic:iterate name="ec.com.smx.sic.sispe.lineaProduccion.colArticulos" id="articuloDTO" indexId="indiceArticulo">
						<html:option value="${articuloDTO.id.codigoArticulo}" styleClass="comboDescripcionCiudad">&nbsp;${articuloDTO.codigoBarrasActivo.id.codigoBarras}&nbsp;&nbsp;-&nbsp;&nbsp;${articuloDTO.descripcionArticulo}</html:option>
					</logic:iterate>								
				 </smx:select>
			   </logic:notEmpty>   		
			   </td>
			   <td width="18px"></td>
			   <td align="center">
				 <div id="botonD">
					<html:link styleClass="buscarD" href="#" onclick="requestAjax('lineaProduccion.do',['mensajes','botones','detalleComun'],{parameters: 'aceptarBusqueda=ok'});">Buscar</html:link>
				 </div>
			   </td>
			</tr>
	  	  </table>
	  	</td>    
      </tr>
	</logic:empty>
	<logic:notEmpty name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloDTO">
	   <tr>
		 <td align="center" id="seccionTotales">
		    <table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
				<tr class="fila_titulo">
					<td colspan="4" class="textoNegro11 fila_contenido" align="left" height="20px"><b>Datos de la cabecera:</b></td>
				</tr>
				<tr>
					<td colspan="2" width="100%" class="textoAzul11" bgcolor="#F4F5EB">
						<table border="0" cellspacing="0" width="100%" align="left">
							<tr>
								<td colspan="2">
									<table cellpadding="1" cellspacing="0" width="100%">
										<tr>
										  <td class="textoNegro11" width="20%" align="left">
											<label class="textoAzul11">C&oacute;digo barras:&nbsp;</label>
											<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionArtFec"> 
											 <bean:write name="calendarioArticuloDTO" property="codigoBarras"/>
											</logic:notEmpty>
											<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionFecArt">
											 <bean:write name="calendarioArticuloDTOHijo" property="codigoBarras"/>		
											</logic:notEmpty>
										  </td>
										  <td align="left" class="textoNegro11" width="20%">
											<label class="textoAzul11">Total Ingresos:&nbsp;</label>
											<bean:write name="calendarioArticuloDTOHijo" property="npTotalIngresos"/> 
										  </td> 
										</tr>
										<tr>
										  <td class="textoNegro11" width="20%" align="left">
											<label class="textoAzul11">Descripci&oacute;n:&nbsp;</label>
											<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionArtFec"> 
											 <bean:write name="calendarioArticuloDTO" property="descripcionArticulo"/>
											</logic:notEmpty>
											<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionFecArt"> 
											 <bean:write name="calendarioArticuloDTOHijo" property="descripcionArticulo"/>
											</logic:notEmpty>	
										  </td>
										  <td align="left" class="textoNegro11" width="20%">
											<label class="textoAzul11">Total Egresos:&nbsp;</label>
											<bean:write name="calendarioArticuloDTOHijo" property="npTotalEgresos"/>
										  </td>
										</tr>
										<tr>
										  <td class="textoNegro11" width="20%" align="left">
											<label class="textoAzul11">Fecha Producción:&nbsp;</label>
											<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionArtFec"> 
											 <bean:write name="calendarioArticuloDTOHijo" property="fechaCalendario" formatKey="formatos.fecha"/>
											</logic:notEmpty>
											<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionFecArt"> 
											 <bean:write name="calendarioArticuloDTO" property="fechaCalendario" formatKey="formatos.fecha"/>
											</logic:notEmpty>	
										  </td>
										  <c:set var="saldo" value="${calendarioArticuloDTOHijo.npTotalIngresos - calendarioArticuloDTOHijo.npTotalEgresos}"/>
										  <c:set var="valor" value="${0}"/>
										   <c:if test="${saldo < valor}">
										   	<c:set var="saldoEncabezado" value="${(calendarioArticuloDTOHijo.npTotalIngresos - calendarioArticuloDTOHijo.npTotalEgresos)*-1}"/>
										   	<bean:define id="color" value="textoRojoE11"/>
										   </c:if>
										   <c:if test="${saldo > valor}">
										   	<c:set var="saldoEncabezado" value="${(calendarioArticuloDTOHijo.npTotalIngresos - calendarioArticuloDTOHijo.npTotalEgresos)}"/>
										   	<bean:define id="color" value="textoAzul10"/>
										   </c:if>
										   <c:if test="${saldo == valor}">
										   	<c:set var="saldoEncabezado" value="${(calendarioArticuloDTOHijo.npTotalIngresos - calendarioArticuloDTOHijo.npTotalEgresos)}"/>
										   	<bean:define id="color" value="textoNegro10"/>
										   </c:if>
										   <td align="left" class="textoAzul11" width="20%">
											 Saldo:&nbsp;<label class="${color}"><b><bean:write name="saldoEncabezado"/></b></label>  	 
										   </td> 
										</tr>
										<tr>
										 <td class="textoNegro11" width="20%" align="left">
											<label class="textoAzul11">Tipo Art&iacute;culo:&nbsp;</label>
											<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionArtFec"> 
											 <bean:write name="calendarioArticuloDTO" property="descripcionTipArt"/>
											</logic:notEmpty>
											<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionFecArt"> 
											 <bean:write name="calendarioArticuloDTOHijo" property="descripcionTipArt"/>
											</logic:notEmpty>
										 </td>								
									    </tr> 	
									</table>
								</td>
							</tr>	
						</table>
					</td>
				</tr>
			 </table>
		  </td>
		</tr>
		<tr height="10"><td></td></tr>
		<tr>
		   <td height="25px" id="seccionDetalle">	
			<table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">				
			 <tr height="25px" class="fila_titulo">
				<td align="left" class="textoNegro11"><b>Detalle de la Producci&oacute;n:</b></td>
				<td align="right">
				  <table cellspacing="0" cellpadding="1" align="right">
				    <tr>
						<td>
						  <div id="botonD">
							<html:link styleClass="agregarD" href="#" title="nueva línea producción" onclick="javascript:requestAjax('lineaProduccion.do',['mensajes','pregunta','especiales','seccion_detalle'],{parameters: 'agregarLinPro=ok', evalScripts: true});">Agregar</html:link>
						  </div>
						</td>
						<%--<td align="right">
						  <div id="botonD">
							<html:link styleClass="eliminarD" href="#" title="eliminar línea producción" onclick="javascript:requestAjax('lineaProduccion.do',['mensajes','pregunta','especiales','seccion_detalle','botones','detalleComun'],{parameters: 'eliminarLinPro=ok', evalScripts: true});">Eliminar</html:link>
						  </div>
					  	</td>
				   </tr>--%>
				 </table>
				</td>	  	
			</tr>
			<tr>
				<td colspan="8" bgcolor="#F4F5EB">
					<table width="100%" border="0" cellpadding="1" cellspacing="0">
						<tr class="tituloTablas" height="15px">
							<%-- <td width="3%" align="center" class="columna_contenido">
                     			<html:checkbox name="lineaProduccionForm" title="seleccionar todo los registros" property="checkTodo" value="t" onclick="activarDesactivarTodo(this,lineaProduccionForm.checksSeleccionar);"></html:checkbox>
                   			</td> --%>
							<td class="columna_contenido" align="center">No</td>
							<td class="columna_contenido" align="center">Motivo Mov.</td>
							<td class="columna_contenido" align="center">Cantidad</td>
							<td class="columna_contenido" align="center">Observaci&oacute;n</td>	
						</tr>									
						<logic:notEmpty name="calendarioArticuloDTOHijo" property="colCalendarioArticulo">
						 <logic:iterate name="calendarioArticuloDTOHijo" property="colCalendarioArticulo" id="calendarioArticuloDTO2" indexId="indiceDetalle">
							<bean:define id="residuo" value="${indiceDetalle % 2}"/>
							<logic:equal name="residuo" value="0">
								<bean:define id="colorBack" value="blanco10"/>
							</logic:equal>
							<logic:notEqual name="residuo" value="0">
								<bean:define id="colorBack" value="grisClaro10"/>
							</logic:notEqual>
							<tr class="${colorBack}">
								<%-- <td width="3%" class="fila_contenido" align="center">
									<html:multibox property="checksSeleccionar" value="${indiceDetalle}"/>
								</td>  --%>
								<bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
								<td align="center" class="columna_contenido fila_contenido"><bean:write name="numRegistro"/></td>
								<td align="left" class="columna_contenido fila_contenido"><bean:write name="calendarioArticuloDTO2" property="descripcionMotMov"/></td>
								<td align="center" class="columna_contenido fila_contenido"><bean:write name="calendarioArticuloDTO2" property="cantidad"/></td>
								<td align="left" class="columna_contenido fila_contenido">&nbsp;<bean:write name="calendarioArticuloDTO2" property="observacion"/></td>	
							</tr>	
						 </logic:iterate>
						</logic:notEmpty>
					 </table>
				 </td>
			  </tr>	
		    </table>	
		  </td>
	   </tr>
	   <tr height="5"><td></td></tr>		  
    </logic:notEmpty>
</table>