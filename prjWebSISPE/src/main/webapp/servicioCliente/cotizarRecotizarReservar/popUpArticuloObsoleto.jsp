<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<div id="mensajeObsoleto" style="position:relative;">
	<tiles:insert  page="/include/mensajes.jsp" />
</div>
<tiles:insert page="/include/metas.jsp"/>
<div id="seccion_obsoletos">
<table cellpadding="0" cellspacing="0" width="100%" align="center" >
	<tr>
		<table border="0" cellpadding="0" cellspacing="0">
	          	<tr>
	           		<td valign="top" width="3%"><img src="images/pregunta24.gif" border="0">&nbsp;&nbsp;</td> 
					<td align="left" class="textoRojo11"><b>
		                Estos art&iacute;culos son obsoletos. Podr&aacute; reservar solo si tiene stock en su local o en otro local. Es su responsabilidad obtener estos productos.
		            </b>
		            </td>		            
	          	</tr>
	   	</table>
	</tr>
	<tr>
		<td width="10%" valign="top">
            <div id="lista">
            	<table class="textoNegro11" border="0" width="100%"  cellpadding="0" cellspacing="0" class="fondoBlanco textoNegro11" align="top">
              		<logic:notEmpty  name="ec.com.smx.sic.sispe.detallePedidoObsoleto"  >
                                        <tr>
                                            <td>
                                                <table cellspacing="0" cellpadding="0" width="98%" align="left" class="tabla_informacion_encabezado" bgcolor="#ffffff">
                                                    <tr class="tituloTablas">
                                                        <td width="2%" align="center" class="tituloTablasCel">No</td>
                                                        <td width="13%" align="center" class="tituloTablasCel">C&oacute;digo barras</td>
                                                        <td width="27%" align="center" class="tituloTablasCel">Descripci&oacute;n</td>
                                                        <td width="8%" align="center" class="tituloTablasCel">Cantidad</td>
                                                        <td width="8%" align="center" class="tituloTablasCel">Stock obsoleto</td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div id="div_listado" style="width:100%;height:300px;overflow-y:auto;overflow-x:hidden;">
                                                    <table cellspacing="0" cellpadding="0" width="98%" align="left" bgcolor="#ffffff" class="tabla_datos">
                                                        <logic:iterate name="ec.com.smx.sic.sispe.detallePedidoObsoleto"  id="detalle" indexId="numeroRegistro">                                                                                                                       								
                                                            <bean:define id="fila" value="${numeroRegistro + 1}"/>  
                                                            <bean:define id="numFila" value="${numeroRegistro % 2}"/>                                                     
                                                            <smx:equal name="numFila" value="0">	
                                                              	<bean:define id="color" value="blanco"/>
                                                            </smx:equal>
                                                            <smx:equal name="numFila" value="1">	
                                                             	<bean:define id="color" value="grisClaro"/>
                                                            </smx:equal>  
                                                            <bean:define name="detalle" property="articuloDTO" id ="articulo"/>
                                                            <bean:define name="detalle" property="estadoDetallePedidoDTO" id ="estadoDetalle"/>                                
                                                            <tr class="${clase}">    
                                                            	<td class="${color} fila_contenido" width="2%" align="center">
                                                            		<bean:write name="fila"/>
                                                            	</td>                                 
                                                                <td class="${color} columna_contenido fila_contenido" width="13%" align="center">
                                                                	<bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/>
                                                                </td> 
                                                                <td class="${color} columna_contenido fila_contenido" align="left" width="27%" >
                                                                	<bean:write name="articulo" property="descripcionArticulo"/>
                                                               	</td>   
                                                               	<td class="${color} columna_contenido fila_contenido" align="center" width="8%" >
                                                                	<bean:write name="estadoDetalle" property="cantidadEstado"/>
                                                               	</td>                                                            
                                                                <td class="${color} columna_contenido fila_contenido" width="8%" align="center">                                                     	
                                                                    <html:text property="vectorObsoleto" value="${detalle.estadoDetallePedidoDTO.stoLocArtObs}" styleClass="textNormal" style="text-align:right" size="4" maxlength="5" 
                                                                    onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','especiales','seccion_detalle','div_datosCliente'],{parameters: 'confirmarStockObsoleto=ok&actualizarDetalle=ok',evalScripts:true});mantenerModalOnkeyEnter(event);return validarInputNumeric(event);"/>                 
                                                                </td>
                                                            </tr>                                                             
                                                        </logic:iterate>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
             		</logic:notEmpty> 
      			</table>                    
            </div>
    	</td>
    </tr>
</table>
</div>