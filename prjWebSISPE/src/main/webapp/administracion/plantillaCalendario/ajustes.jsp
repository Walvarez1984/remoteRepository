<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vtformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>
<div id="ajustes">
	<table cellpadding="0" cellspacing="0" border="0">
		<tr>
			<!-- Sección de ingreso de datos para los ajustes -->
			<td valign="top" width="35%">	
				<table border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
					<tr>
						<td class="fila_titulo" height="29px" colspan="3">
	                       <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
	                           <tr>
	                               <td width="34"><img src="./images/ajustesCapacidad24.gif" border="0"></td>
                                   <td width="164" align="left">Ingreso de Datos&nbsp;</td>
                                   <td align="right" width="132">
                    				   <div id="botonD">	
	                                       <html:link styleClass="guardarD" href="#" onclick="requestAjax('adminPlantillas.do', ['ajustes','mensajes'], {parameters: 'botonGrabarAjuste=ok',popWait:true});">Grabar</html:link>
	                                   </div>	
	                               </td>	
                                   <td width="4">&nbsp;</td>
                             </tr>
	                       </table>
	                   </td>
	                </tr>
	                <tr><td height="2px">&nbsp;</td></tr>
	                <!-- IMPORTANTE: Se presenta un solo campo para ingreso de CPE en lugar de CN y CA -->
	                <%-- %><tr>
                    	<td width="2px"></td>
	                    <td class="textoAzul11">
							Capacidad Normal:							
						</td>
					    <td class="textoNegro11">
		               		<smx:text property="ajusteCapacidadNormal" size="5" styleClass="combos"/>
		                </td>
		            </tr>--%>		          
		            <%-- %><tr>
                    	<td width="2px"></td>
	                    <td class="textoAzul11">
							Capacidad Adicional:							
						</td>
					    <td class="textoNegro11">
		               		<smx:text property="ajusteCapacidadAdicional" size="5" styleClass="combos"/>
		                </td>
		            </tr>--%>
		            <tr>
                    	<td width="2px"></td>
	                    <td class="textoAzul11">
							Capacidad Pedidos Especiales:							
						</td>
					    <td class="textoNegro11">
		               		<smx:text property="ajusteCapacidadNormal" size="5" styleClass="combos"/>
		                </td>
		            </tr>
		            <tr>
                    	<td width="2px"></td>
                    	<td class="textoAzul11">
							Concepto:							
						</td>	
			           <td width="60%" align="left" class="textoNegro9">
    	                 <smx:textarea rows="3" cols="30" property="conceptoMovimiento" styleClass="textObligatorio" styleError="campoError"/>
	                   </td>	 
                    </tr>   
		            <tr>
                    	<td width="2px"></td>
	                    <td class="textoAzul11" align="left" width="40%">Fecha Inicial:</td>
                        <td align="left">
	                        <table border="0" cellspacing="0">
	                            <tr>
	                                <td class="textoNegro11">
	                                    <smx:text property="fechaInicial" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="10"/>
	                                </td>
	                                <td>
	                                    <smx:calendario property="fechaInicial" key="formatos.fecha"/>
	                                </td>
	                                <td width="2px"></td>
	                            </tr>
	                        </table>
	                    </td>
	                </tr>
	                <tr>
                    	<td width="2px"></td>
	                    <td class="textoAzul11" align="left">Fecha Final:&nbsp;</td>
	                    <td>
	                        <table border="0" cellspacing="0">
	                            <tr>
	                                <td class="textoNegro11">
	                                    <smx:text property="fechaFinal" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="10"/>
	                                </td>
	                                <td>
	                                    <smx:calendario property="fechaFinal" key="formatos.fecha"/>
	                                </td>
	                                <td width="2px"></td>
	                            </tr>
	                        </table>
	                    </td>
	                </tr>  
	            </table>  
			</td>
            <td width="2px">&nbsp;</td>
			<td valign="top">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white" class="tabla_informacion">
                    <tr>
                        <td class="fila_titulo" height="29px" colspan="3">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                <tr>
                                    <td width="33" align="right"><img src="./images/locales24.gif" border="0"></td>
                                  	<td width="373">&nbsp;Locales</td>
                                  	<td width="80" class="textoAzul10" align="right">Ciudad:</td>
                              		<td width="117" align="right">
										<logic:notEmpty name="ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol">
                                            <smx:select property="ciudadesA" styleClass="combos" onchange="requestAjax('adminPlantillas.do', ['listadoLocales','mensajes'], {parameters: 'buscarLocalesAjustes=LOC',popWait:true});">
                                                <html:option value="">Seleccione</html:option>
                                                <html:options collection="ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol" labelProperty="nombreCiudad" property="id.codigoCiudad"/>
                                            </smx:select>
                                        </logic:notEmpty>	
                                  </td>	
                                  <td width="10"></td>
                              </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td height="2px"></td></tr>
                    <tr class="tituloTablas" align="left">
                        <td class="columna_contenido" width="15%" align="center">TODOS&nbsp;
                            <html:checkbox property="todoA" title="seleccionar todos los art&iacute;culos" onclick="activarDesactivarTodo(this,calendarizacionForm.seleccionadosA)"/>
                        </td>
                        <td class="columna_contenido" align="center" width="15%">C&Oacute;DIGO</td>
                        <td class="columna_contenido" align="center" width="70%">NOMBRE LOCAL</td>
                    </tr>
                    <logic:notEmpty name="ec.com.smx.sic.sispe.vistaLocalDTOColA">
                        <tr>
                            <td colspan="3">
                                <div id="listadoLocales" style="width:100%;height:380px;overflow-y:auto;overflow-x:hidden">
                                    <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                        <logic:iterate name="ec.com.smx.sic.sispe.vistaLocalDTOColA" id="vistaLocalDTO" indexId="numLocal">
                                            <bean:define id="numFila" value="${numLocal + 1}"/>
                                            <%--------- control del estilo para el color de las filas --------------%>
                                            <bean:define id="residuo" value="${numLocal % 2}"/>
                                            <c:set var="clase" value="blanco10"/>
                                            <logic:notEqual name="residuo" value="0">
                                                <c:set var="clase" value="grisClaro10"/>
                                            </logic:notEqual>
                                            <tr>
                                                <td class="${clase} textoNegro10" width="15%" align="center">
                                                    <html:multibox property="seleccionadosA" value="${vistaLocalDTO.id.codigoLocal}"></html:multibox>
                                                </td>
                                                <td class="${clase} textoNegro10" align="center" width="15%"><bean:write name="vistaLocalDTO" property="id.codigoLocal"/></td>
                                                <td class="${clase} textoNegro10" align="left" width="70%">&nbsp;<bean:write name="vistaLocalDTO" property="nombreLocal"/></td>
                                            </tr>
                                        </logic:iterate>
                                    </table>
                                </div>		
                            </td>
                        </tr>	   	   
                    </logic:notEmpty>       	
				</table>           
			</td>
		</tr>
	</table>
</div>	             
             