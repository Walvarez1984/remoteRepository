<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<table border="0" class="textoNegro11" width="99%" align="center" cellpadding="0" cellspacing="0">
    <tr>                               
        <TD class="datos" width="82%">
            <div id="resultadosBusqueda">
                <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" bgcolor="white">
                    <!--Titulo de los datos-->
                    <tr>
                        <td class="fila_titulo" colspan="7" height="26px">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                <tr>
                                    <td width="3%">
                                        &nbsp;<img src="images/Kardex24.gif" border="0"/>
                                    </td>
                                    <td width="85%" align="left">	
                                        <b>&nbsp;Reporte de art&iacute;culos y pedidos</b>
                                    </td>                                    
                                    <td width="2%"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td height="4px" bgcolor="#F4F5EB"></td></tr>
                    <tr>
                        <td bgcolor="#F4F5EB">
                            <table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
                                <logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol">                                                                                          	
	                                    <%-- %><tr>
	                                        <td colspan="6">
	                                            <table cellpadding="0" cellspacing="0" width="100%">
	                                                <tr>
	                                                    <td align="left" class="textoRojo10" id="ordenarPor1">
	                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.ordenamiento.datosColumna">
	                                                            <bean:define id="datosColumnaOrdenada" name="ec.com.smx.sic.sispe.ordenamiento.datosColumna"/>
	                                                            <b>Ordenado Por:</b>&nbsp;<label class="textoAzul10">${datosColumnaOrdenada[0]}&nbsp;(Orden:&nbsp;${datosColumnaOrdenada[1]})</label>
	                                                        </logic:notEmpty>
	                                                    </td>
	                                                    <td align="right" id="pag">
	                                                        <smx:paginacion start="${controlProduccionForm.start}" range="${controlProduccionForm.range}" results="${controlProduccionForm.size}" styleClass="textoNegro10" url="actualizarDespacho.do" requestAjax="'mensajes','pag','div_listado'"/>
	                                                    </td>
	                                                </tr>
	                                            </table>
	                                        </td>
	                                    </tr>--%>
                                   
                                    	<tr>
                                            <td>
                                                <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                    <tr class="tituloTablas"  align="left">
                                                        <td class="columna_contenido" width="4%" align="center"></td>
                                                        <td class="columna_contenido" width="4%" align="center">No.</td>
                                                        <td class="columna_contenido" width="20%" align="center">C&oacute;digo barras</td>
                                                        <td class="columna_contenido" width="24%" align="center">Ref. art.</td>
                                                        <td class="columna_contenido" width="24%" align="center">Cantidad</td>
                                                        <td class="columna_contenido" width="24%" align="center">Bultos</td>                                                        
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div id="div_listado" style="width:100%;height:370px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
                                                    <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                        <logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol" id="vistaArticuloDTO" indexId="indiceArticulo">
                                                            <bean:define id="indiceGlobal" value="${indiceArticulo + controlProduccionForm.start}"/>
                                                            <bean:define id="numFila" value="${indiceGlobal + 1}"/>
                                                            
                                                            <bean:define id="residuo" value="${indiceArticulo % 2}"/>
                                                            <logic:equal name="residuo" value="0">
                                                                <bean:define id="clase" value="blanco10"/>
                                                            </logic:equal>
                                                            <logic:notEqual name="residuo" value="0">
                                                                <bean:define id="clase" value="grisClaro10"/>
                                                            </logic:notEqual>
                                                           
                                                            
                                                            <tr class="${clase}"> 
                                                                <td class="columna_contenido fila_contenido" width="4%" align="center">
                                                                    <c:set var="despliegueA1" value="block"/>
                                                                    <c:set var="despliegueA2" value="none"/>
                                                                    
                                                                   <!--  Se contrala que se mantenga activo el índice elegido previo a la visualización de un detalle de pedido -->
                                                                   <logic:notEmpty name="ec.com.smx.sic.sispe.primerIndiceVRG">
                                                                   		<bean:define id="indiceActivoVRG" name="ec.com.smx.sic.sispe.primerIndiceVRG"></bean:define>
                                                                   		<logic:equal name="indiceActivoVRG" value="${indiceArticulo}">
                                                                   		    <c:set var="despliegueA1" value="none"/>
                                                                            <c:set var="despliegueA2" value="block"/>
                                                                        </logic:equal>
                                                                   </logic:notEmpty>
                                                                    
                                                                    <%--<logic:notEmpty name="indiceNivel1">
                                                                        <logic:equal name="indiceNivel1" value="${indiceArticulo}">
                                                                            <c:set var="despliegueA1" value="none"/>
                                                                            <c:set var="despliegueA2" value="block"/>
                                                                        </logic:equal>
                                                                    </logic:notEmpty>--%>
                                                                    
                                                                    <div style="display:${despliegueA1}" id="desplegar_${indiceArticulo}">
                                                                        <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indiceArticulo}']);show(['plegar_${indiceArticulo}','listado_${indiceArticulo}']);"></a>
                                                                    </div>
                                                                    <div style="display:${despliegueA2}" id="plegar_${indiceArticulo}">
                                                                        <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indiceArticulo}','listado_${indiceArticulo}']);show(['desplegar_${indiceArticulo}']);"></a>
                                                                    </div>
                                                                </td>
                                                                <td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila"/></td>
                                                                <td class="columna_contenido fila_contenido" width="20%" align="left"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>        
                                                              	<td class="columna_contenido fila_contenido" width="24%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>  
                                                              	<td class="columna_contenido fila_contenido" width="24%" align="left"><bean:write name="vistaArticuloDTO" property="cantidadDespacharTotal"/></td> 
                                                              	<td class="columna_contenido fila_contenido" width="24%" align="left"><bean:write name="vistaArticuloDTO" property="cantidadBultos"/></td>    
                                                            </tr>
                                                            <tr>
                                                                <td colspan="10" id="listado_${indiceArticulo}" align="center" style="display:${despliegueA2}">
                                                                    <!-- se muestra el detalle de los locales -->
                                                                    <table cellpadding="1" cellspacing="0" width="90%" class="tabla_informacion_negro">
                                                                        <tr class="tituloTablasCeleste">
                                                                           
                                                                           	<td class="columna_contenido" width="4%" align="center">No.</td>
                                                        					<td class="columna_contenido" width="20%" align="center">Código ped.</td>
                                                        					<td class="columna_contenido" width="24%" align="center">No. Res</td>
                                                        					<td class="columna_contenido" width="24%" align="center">Cantidad</td>
                                                        					<td class="columna_contenido" width="24%" align="center">Bultos</td>                                                                             
                                                                        </tr>
                                                                        <logic:notEmpty name="vistaArticuloDTO" property="detallesReporte">
                                                                            <!-- Inicio Iteración hijo ---------------------------------------------------------------------------------------- -->                                                                   
	                                                                        <logic:iterate name="vistaArticuloDTO" property="detallesReporte" id="vistaArticuloDTO2" indexId="indiceArticulo2">
	                                                                          
	                                                                            <bean:define id="residuo2" value="${indiceArticulo2 % 2}"/>
	                                                                            <bean:define id="numFila2" value="${indiceArticulo2 + 1}"/>
	                                                                            <logic:equal name="residuo2" value="0">
	                                                                                <bean:define id="clase2" value="blanco10"/>
	                                                                            </logic:equal>
	                                                                            <logic:notEqual name="residuo2" value="0">
	                                                                                <bean:define id="clase2" value="amarilloClaro10"/>
	                                                                            </logic:notEqual>
	                                                                                                                                                                                                                                  
	                                                                            <tr class="${clase2}">
	                                                                                <%--<c:set var="despliegueL1" value="block"/>
	                                                                                <c:set var="despliegueL2" value="none"/>
	                                                                                <logic:notEmpty name="indiceNivel2">
	                                                                                    <logic:equal name="indiceNivel1" value="${indiceGlobal}">
	                                                                                        <logic:equal name="indiceNivel2" value="${indiceArticulo2}">
	                                                                                            <c:set var="despliegueL1" value="none"/>
	                                                                                            <c:set var="despliegueL2" value="block"/>
	                                                                                        </logic:equal>
	                                                                                    </logic:equal>
	                                                                                </logic:notEmpty>--%>
	                                                                                
	                                                                               <td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila2"/></td>
	                                                                			   <%-- %><td class="columna_contenido fila_contenido" width="24%" align="left"><bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/></td>   --%>
	                                                                			   <td class="columna_contenido fila_contenido" width="24%" align="left">
	                                                                			   		<%--<html:link title="detalle del pedido" onclick="realizarEnvio('indiceArticulo2=ok');" href="#"><bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/></html:link>    --%>
	                                                                			   		<bean:define id="indices" value="${indiceArticulo}_${indiceArticulo2}"></bean:define>
	                                                                			   		<html:link title="Ver detalles" action="kardex" paramId="indiceVRG" paramName="indices"><bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/></html:link>												
	                                                                			   </td>
	                                                              	  			   <td class="columna_contenido fila_contenido" width="24%" align="left">&nbsp;<bean:write name="vistaArticuloDTO2" property="llaveContratoPOS"/></td>  
	                                                              				   <td class="columna_contenido fila_contenido" width="24%" align="left"><bean:write name="vistaArticuloDTO2" property="cantidadDespacharTotal"/></td> 
	                                                              			       <td class="columna_contenido fila_contenido" width="24%" align="left"><bean:write name="vistaArticuloDTO2" property="cantidadBultos"/></td>                                                                                                                                                                                                                                             
	                                                                            </tr>
	                                                                            
	                                                                        </logic:iterate>
	                                                                        <!-- Fin Iteración hijo ---------------------------------------------------------------------------------------- -->                                                                   
                                                                        </logic:notEmpty>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </logic:iterate>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>                                   
                                 </logic:notEmpty>   
                                 <logic:empty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol"> 
                                 	No hay datos
                                 </logic:empty>                                                       
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
        </TD>
      
    </tr>
</table>