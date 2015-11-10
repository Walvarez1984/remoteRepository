<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>



<!-- Modal para el popUp del calendario del popUp de entregas -->
<script language="javascript">
	//mostrarModalZ('frameModal2',120);
</script>
<!-- Contiene el calendario SICMER -->
<bean:define name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.calendarioMesEST" id="calendarioResumenEST"/>
<div id="calendariopopup" class="popup"  style="top:40px;z-index:119;">
    <table border="0" cellpadding="0" cellspacing="0" align="center" width="100%">
        <tr>
            <td valign="top" align="center">
                <div id="center" style="position:relative;top:30px;">
                    <%-- Contenido de la ventana de confirmacion --%>					
                    <table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="35%">
                        <tr>
                            <td background="images/barralogin.gif" align="center">
                                <table cellpadding="0" cellspacing="0" width="100%" border="0">			
                                	                     
                                    <tr>
                                        <td class="textoBlanco11" align="left">
                                        	&nbsp;<b>Calendario de entregas a domicilio</b>
                                        </td>
                                        <!-- Boton Cerrar -->
                                        <td align="right">
                                        	<html:link title="Cerrar" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'calendariosicmer=ok&accioncalendario=cancelar', evalScripts: true});ocultarModalID('frameModal2');ocultarModal();" >
                                        		<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
                                        	</html:link>&nbsp;
                                        </td>		                           
                                    </tr>
                                </table>
                            </td>
                        </tr>    
                        <tr>
          					<td bgcolor="#F4F5EB" valign="top">                                
                            	<table class="tabla_informacion" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
                                    <tr>								   		                                                 
                                        <td>
                                        	<table class="tabla_informacion fondoBlanco" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
                                            	<tr>
                                                	<td class="textoAzul12">
                                                    	<table border="0" cellpadding="0" cellspacing="0" width="100%">     
                                                    		<tr>
									                            <td>
									                                <div id="mensajesPopUpSICMER" style="font-size:0px;position:relative;">
																		<tiles:insert  page="/include/mensajes.jsp" />
											  						</div>
											  					</td>
											  				</tr>                                                        
                                                            <tr>
                                                            	<td align="right">
                                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                       <!-- Fila de Año del calendario y navegacion entre meses -->
                                                                       
                                                                        <tr> 
                                                                        	<td class="textoAzulC15" align="left" width="90%"><b>Calendario&nbsp;<bean:write name="calendarioResumenEST" property="anio"/></b></td>                                                                           
                                                                            <td align="right"><a href="#" onclick="requestAjax('entregaLocalCalendario.do', ['calendariopopup','mensajesPopUpSICMER'], {parameters: 'calendariosicmer=ok&accioncalendario=mesanterior', evalScripts: true});">
                                                                            	<html:img src = "images/atrasAzul.gif" border="0"/>
                                                                            </a></td>
                                                                            <td>&nbsp;&nbsp;</td>
                                                                            <td class="textoAzulC15"><b><bean:write name="calendarioResumenEST" property="mesS"/></b></td>
                                                                            <td>&nbsp;&nbsp;</td>
                                                                            <td align="left"><a href="#" onclick="requestAjax('entregaLocalCalendario.do', ['calendariopopup'], {parameters: 'calendariosicmer=ok&accioncalendario=mesposterior', evalScripts: true});">
                                                                            	<html:img src = "images/adelanteAzul.gif" border="0"/>	
                                                                            </a></td>
                                                                        </tr>
                                                                    </table>                    
                                                                </td>
                                                            <tr><td height="3px"></td></tr>
                                                            <tr>
                                                                <td align="center">
                                                                    <table border="0" cellpadding="0" cellspacing="0" width="632px" class="tabla_calendario_encabezado">
                                                                        <tr>
                                                                            <logic:iterate id="diasSemana" name="calendarioResumenEST" property="diasSemana" indexId="numRegistro">
                                                                                <bean:define id="width" value="90px"/>
                                                                                <logic:equal name="diasSemana" value="Lunes">
                                                                                	<bean:define id="width" value="91px"/>
                                                                                </logic:equal>
                                                                                <td class="columna_calendario_encabezado" width="${width}"><bean:write name="diasSemana"/></td>
                                                                            </logic:iterate>
                                                                        </tr>                            	
                                                                    </table>
                                                                <td>
                                                            </tr>
                                                            <tr>
                                                                <td align="center">                                                                    
                                                                    <table border="0" cellpadding="0" cellspacing="0" width="632px" class="tabla_calendario">
                                                                        <logic:iterate id="resumenDiaDTO" name="calendarioResumenEST" property="resumenDiaEST" indexId="numRegistro">
                                                                            <bean:define id="menuIndex" value="${numRegistro}"/>
                                                                            <bean:define id="residuoI" value="${menuIndex % 7}"/>
                                                                            <bean:define id="residuoI1" value="${(menuIndex+1) % 7}"/>
                                                                            <bean:define id="estiloDia" name="resumenDiaDTO" property="estiloDia"></bean:define>
                                                                            <bean:define id="botonEstado" name="resumenDiaDTO" property="botonEstado"></bean:define>
                                                                            <logic:equal name="residuoI" value="0">
                                                                                <tr>
                                                                            </logic:equal>
                                                                            <td class="columna_calendario_dia" width="90px" valign="top" >
                                                                                <table border="0" cellpadding="0" cellspacing="1" width="100%" class="${estiloDia}">
                                                                                    <tr>                                        
                                                                                        <td>
                                                                                            <bean:define name="resumenDiaDTO" property="fechaS" id="dia"/>
                                                                                            <logic:equal name="botonEstado" value="anterior">
                                                                                            	<a href="#" class="${botonEstado}">
                                                                                                	<bean:write name="dia"/>
                                                                                                </a>
                                                                                            </logic:equal>
                                                                                            <logic:notEqual name="botonEstado" value="anterior">
                                                                                            <a href="#" class="${botonEstado}" onclick="requestAjax('entregaLocalCalendario.do', ['calendariopopup'], {parameters: 'calendariosicmer=ok&accioncalendario=verDia-${numRegistro}', evalScripts: true});">
                                                                                                <table border="0" cellpadding="0" cellspacing="1" width="100%" style="cursor:pointer">
                                                                                                	<tr>
                                                                                                    	<logic:equal name="estiloDia" value="diaActual">
                                                                                             	           <td align="left">Hoy</td>
                                                                                                        </logic:equal>
                                                                                                        <td><bean:write name="dia"/>&nbsp;</td>
                                                                                                    </tr>
                                                                                                </table>        
                                                                                            </a>
                                                                                            </logic:notEqual>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td>
                                                                                            <logic:notEqual name="resumenDiaDTO" property="estiloDia" value="diaAnterior">
                                                                                                <table border="0" cellpadding="0" cellspacing="1" width="100%" class="textoAzul10">                                                                                                        
                                                                                                    <tr><td height="2px"></td></tr>
                                                                                                   <!--<tr>                                        
                                                                                                        <td align="right">
                                                                                                            <logic:notEqual  name="resumenDiaDTO" property="total1" value="0">
                                                                                                            	<b><bean:write name="resumenDiaDTO" property="total1"/></b>
                                                                                                            </logic:notEqual>                                                                                                            
                                                                                                        </td>
                                                                                                        <td align="left">
                                                                                                        	<logic:notEqual  name="resumenDiaDTO" property="total1" value="0">
                                                                                                            	&nbsp;convenios
                                                                                                            </logic:notEqual>
                                                                                                        </td>
                                                                                                    </tr>-->
                                                                                                    <tr>                                        
                                                                                    					<td align="right">
                                                                                        					<logic:notEqual  name="resumenDiaDTO" property="totalSICMER" value="0">
                                                                                            					<b><bean:write name="resumenDiaDTO" property="totalSICMER"/></b>
                                                                                        					</logic:notEqual>                                                                                                            
                                                                                    					</td>
                                                                                    					<td align="left">
                                                                                        					<logic:notEqual  name="resumenDiaDTO" property="totalSICMER" value="0">
                                                                                            					&nbsp;conv. SICMER
                                                                                        					</logic:notEqual>
                                                                                    					</td>
                                                                                					</tr> 
                                                                                					<tr>
                                                                                 						<td align="right">
                                                                                        					<logic:notEqual  name="resumenDiaDTO" property="totalSISPE" value="0">
                                                                                            					<b><bean:write name="resumenDiaDTO" property="totalSISPE"/></b>
                                                                                        					</logic:notEqual>                                                                                                            
                                                                                    					</td>
                                                                                    					<td align="left">
                                                                                        					<logic:notEqual  name="resumenDiaDTO" property="totalSISPE" value="0">
                                                                                            					&nbsp;conv. SISPE
                                                                                        					</logic:notEqual>
                                                                                    					</td>
                                                                                					</tr>
                                                                                                    <tr><td height="2px"></td></tr>
                                                                                                </table>
                                                                                            </logic:notEqual>     
                                                                                            <logic:equal name="resumenDiaDTO" property="estiloDia" value="diaAnterior">
                                                                                                <table border="0" cellpadding="0" cellspacing="1" width="100%" class="textoAzul10">                                                                                                        
                                                                                                    <tr><td height="2px"></td></tr>
                                                                                                    <tr>                                        
                                                                                                        <td align="right" width="20%">&nbsp;</td>
                                                                                                        <td width="80%" align="left">&nbsp;</td>
                                                                                                    </tr>
                                                                                                    <tr><td height="2px"></td></tr>
                                                                                                </table>
                                                                                            </logic:equal>                                               
                                                                                        </td>
                                                                                    </tr>													
                                                                                </table>
                                                                            </td>
                                                                            <logic:equal name="residuoI1" value="0">
                                                                                </tr>
                                                                            </logic:equal>
                                                                        </logic:iterate>                               
                                                                    </table>
                                                                <td>
                                                            </tr>
                                                            <tr><td height="2px"></td></tr>
                                                            <tr>
                                                            	<td>
                                                                    <table class="tabla_informacion" border="0" cellspacing="0" cellpadding="0" width="100%" >    
                                                                        <tr>		
                                                                            <td valign="top">
                                                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="fila_titulo">
                                                                                    <tr>
                                                                                        <td align="left" width="32px">&nbsp;<img src="images/convenio_24.gif" align="middle" border="0"/></td>
                                                                                        <td class="textoNegroC12B">
                                                                                        	<bean:define id="resumenDiaEST" name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.resumenDiaEST"/>
                                                                                            Convenios de entrega a domicilio del: <b><bean:write name="resumenDiaEST" property="fecha.time" formatKey="formatos.fecha"/></b>
                                                                                        </td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>   
                                                                        <tr>    
                                                                            <td>   	
                                                                            	<table border="0" cellpadding="0" cellspacing="3" width="100%" >
                                                                                    <tr>
                                                                                        <td> 
                                                                                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                                                <tr>
                                                                                                    <td>
                                                                                                        <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion_encabezado" height="20px">                                        
                                                                                                            <tr>
                                                                                                                <td align="center" class="tituloTablasCel" width="13%">
                                                                                                                    <B>Rango Hora</B>
                                                                                                                </td>
                                                                                                                <td align="center" class="tituloTablasCel" width="12%">
                                                                                                                    <B>Convenio</B>
                                                                                                                </td>                                                                                                               
                                                                                                                <td align="center" class="tituloTablasCel" width="50%">
                                                                                                                    <B>Direcci&oacute;n</B>
                                                                                                                </td>                                                                                                                                                                                                                              
                                                                                                                <td align="center" class="tituloTablasCel" width="15%">
                                                                                                                    <B>Estado</B>
                                                                                                                </td>
                                                                                                                <td align="center" class="tituloTablasCel" width="10%">
                                                                                                                    <B>Sistema Origen</B>
                                                                                                                </td>
                                                                                                            </tr>	
                                                                                                        </table>
                                                                                                    </td>
                                                                                                </tr>                           
                                                                                                <tr>
                                                                                                    <td>
                                                                                                        <logic:notEmpty name="resumenDiaEST" property="datos1">
                                                                                                            <div id="div_listado" style="width:100%;height:140px;overflow-x:hidden;overflow-y:auto;">                                        
                                                                                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tabla_datos">                                                                            
                                                                                                                    <logic:iterate id="convenioDiaDTO" name="resumenDiaEST" property="datos1" indexId="numRegistro">
                                                                                                                        <bean:define id="numFila" value="${numRegistro % 2}"/>                                                     
                                                                                                                       <bean:define id="fechaActual" name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.fechaActual"/>
                                                                                                                        <smx:equal name="numFila" valueKey="valor.numero.false">	
                                                                                                                            <bean:define id="color" value="blanco"/>
                                                                                                                        </smx:equal>
                                                                                                                        <smx:equal name="numFila"  valueKey="valor.numero.true">	
                                                                                                                            <bean:define id="color" value="grisClaro"/>
                                                                                                                        </smx:equal>
                                                                                                                            
                                                                                                                          
                                                                                                                        <c:if test="${convenioDiaDTO.fechaEntrega<fechaActual}" > 
                                                                                                                        	<smx:equal name="convenioDiaDTO" property="estado" valueKey="valor.status.registrado">
                                                                                                                        		<bean:define id="color" value="rojo"/>
                                                                                                                            </smx:equal>                                                                                                                            
                                                                                                                            <smx:notEqual name="convenioDiaDTO" property="estado" valueKey="valor.status.registrado"> 
                                                                                                                                <smx:equal name="convenioDiaDTO" property="estado" valueKey="valor.status.entrega.parcial"> 
                                                                                                                                    <bean:define id="color" value="rojo"/>
                                                                                                                                </smx:equal>
                                                                                                                            </smx:notEqual>
                                                                                                                     	 </c:if>
                                                                                                                                                                                                                                                
                                                                                                                        <tr>                                                        
                                                                                                                            <td width="13%" align="center" class="${color} fila_contenido">                                                            
                                                                                                                            	<bean:write name="convenioDiaDTO" property="rangoHora" />
                                                                                                                            </td>
                                                                                                                            <td width="12%" align="right" class="${color} fila_contenido columna_contenido" height="22px" style="padding-right:3px;">                                                                                                                                
                                                                                                                                <bean:write name="convenioDiaDTO" property="convenioCaso" />
                                                                                                                            </td>
                                                                                                                            <td width="50%" align="left" class="${color} fila_contenido columna_contenido" style="padding-left:2px;">
                                                                                                                               <bean:write name="convenioDiaDTO" property="direccion" />
                                                                                                                            </td>
                                                                                                                            <td width="15%" align="left" class="${color} fila_contenido columna_contenido" style="padding-left:3px;">
                                                                                                                                <smx:equal name="convenioDiaDTO" property="estado" valueKey="valor.status.anulado"> 
                                                                                                                                    <bean:message key="etiqueta.status.anulado"/>
                                                                                                                                </smx:equal>
                                                                                                                                <smx:equal name="convenioDiaDTO" property="estado" valueKey="valor.status.registrado"> 
                                                                                                                                    <bean:message key="etiqueta.status.registrado"/>
                                                                                                                                </smx:equal>
                                                                                                                                <smx:equal name="convenioDiaDTO" property="estado" valueKey="valor.status.despachado"> 
                                                                                                                                    <bean:message key="etiqueta.status.despachado"/>
                                                                                                                                </smx:equal>
                                                                                                                                <smx:equal name="convenioDiaDTO" property="estado" valueKey="valor.status.entrega.parcial"> 
											                                                                                        <bean:message key="etiqueta.status.entrega.parcial"/>
											                                                                                    </smx:equal>
                                                                                                                                <smx:equal name="convenioDiaDTO" property="estado" valueKey="valor.status.entregado"> 
                                                                                                                                    <bean:message key="etiqueta.status.entregado"/>
                                                                                                                                </smx:equal> 
                                                                                                                                <smx:equal name="convenioDiaDTO" property="estado" valueKey="valor.status.pendiente"> 
                                                                                                                                    <bean:message key="etiqueta.status.pendiente"/>
                                                                                                                                </smx:equal>                                                         
                                                                                                                            </td>
                                                                                                                            <td width="10%" align="left" class="${color} fila_contenido columna_contenido" style="padding-left:2px;">
                                                                                   												<logic:notEqual name="convenioDiaDTO" property="codigoSistema" value="">
                                                                                   													<bean:write name="convenioDiaDTO" property="codigoSistema" />
                                                                                   												</logic:notEqual>
																															</td>                                                     
                                                                                                                        </tr>
                                                                                                                    </logic:iterate>
                                                                                                                </table>
                                                                                                            </div>
                                                                                                        </logic:notEmpty>
                                                                                                        <logic:empty name="resumenDiaEST" property="datos1">                                    
                                                                                                            <div id="div_listado" style="width:100%;height:110px;overflow-x:hidden;overflow-y:auto;">
                                                                                                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                                                                    <tr>
                                                                                                                        <td align="center" height="105px" class="tabla_informacion amarillo1">
                                                                                                                            No hay entregas programadas este d&iacute;a
                                                                                                                        </td>
                                                                                                                    </tr>
                                                                                                                </table>
                                                                                                            </div>
                                                                                                        </logic:empty>
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
                                                            <tr><td height="4px"></td></tr>
                                                            <tr>
                                                            	<td align="left">
                                                                    <table border="0" cellspacing="0" cellpadding="0" >    
                                                                        <tr>		
                                                                            <td class="textoNegroC12B">
                                                                            	Programar la Entrega para el d&iacute;a:&nbsp;<b><bean:write name="resumenDiaEST" property="fecha.time" formatKey="formatos.fecha"/></b>&nbsp;&nbsp;
                                                                            </td>
                                                                            <td>Entre las:&nbsp;</td>
                                                                            <td>
                                                                                <smx:select property="npHoraDesde" styleClass="comboObligatorio"  styleError="campoError" onchange="requestAjax('entregaLocalCalendario.do', ['calendariopopup'], {parameters: 'calendariosicmer=ok&accioncalendario=programarfechas',popWait:true,evalScripts:true});">
                                                                                    <logic:notEmpty name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.horasDiaDesde">
	                                                                                        <html:options name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.horasDiaDesde"/>
	                                                                                    </logic:notEmpty>
                                                                                </smx:select>
                                                                            </td>
                                                                            <td>&nbsp;horas.&nbsp;&nbsp;Y las&nbsp;</td>
                                                                            <td>
                                                                                <smx:select property="npHoraHasta" styleClass="comboObligatorio"  styleError="campoError">
                                                                                   <logic:notEmpty name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.horasDiaHasta">
	                                                                                        <html:options name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.horasDiaHasta"/>
                                                                                   </logic:notEmpty>
                                                                                </smx:select>
                                                                            </td>
                                                                            <td>&nbsp;horas.</td>
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
                                    <tr>
                                        <td align="center">
                                            <table cellpadding="0" cellspacing="0" border="0">
                                                <tr>                                                    
                                                    <td><div id="botonD"><html:link styleClass="aceptarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['pregunta','opcionesBusqueda'], {parameters: 'calendariosicmer=ok&accioncalendario=colocarfecha', popWait:true, evalScripts: true});ocultarModalID('frameModal2');ocultarModal();">Aceptar</html:link></div></td>
                                                    <td>&nbsp;</td>
                                                    <td><div id="botonD"><html:link styleClass="cancelarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'calendariosicmer=ok&accioncalendario=cancelar', evalScripts: true});ocultarModalID('frameModal2');ocultarModal();">Cancelar</html:link></div></td>
                                                </tr>    
                                            </table>
                                        </td>
                                    </tr>
                            	</table>
                            </td>
                        </tr>                        	
                    </table>
                </div>
            </td>
        </tr>
    </table>
</div>