<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">

<logic:notEmpty name="ec.com.smx.sic.sispe.reporte.diaCalendario">
	<logic:equal name="ec.com.smx.sic.sispe.reporte.diaCalendario" value="diaActual">
		<bean:define id="vDia" value="diaActual"></bean:define>	
		<bean:define id="fechaSeccion" name="ec.com.smx.sic.sispe.fechaActualNombre"/>	
	</logic:equal>
	<logic:notEqual name="ec.com.smx.sic.sispe.reporte.diaCalendario" value="diaActual">
		<bean:define id="vDia" value="diaSeleccionado"></bean:define>	
		<bean:define id="fechaSeccion" name="ec.com.smx.sic.sispe.fechaSeleccionadaNombre"/>	
	</logic:notEqual>
</logic:notEmpty>
<bean:define id="vTipo" value="canasta"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="Content-Style-Type" content="text/css">
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <meta HTTP-EQUIV="max-age" CONTENT="0">
        <meta HTTP-EQUIV="Expires" CONTENT="0">
        <link href="../../css/textos.css" rel="stylesheet" type="text/css">
        <title>Entregas a Domicilio y Despachos a Local del d&iacute;a <bean:write name="fechaSeccion" format="yyyy-MM-dd"/> </title>
        <style>
        	.textoNegro10{
				font-family: Verdana, Arial, Helvetica;
				font-size: 9px;
				color: #000000;
			}
        	
        	.amarillo1 {
				color: #000000;
				font-weight:bold;
			}
        	.tabla_informacion{
				border-width: 1px;
				border-style: solid;
			    border-color: #000000;
			}
        	.tituloTablasB{
				color: #000000;
				font-style: normal;
				line-height: normal;
				font-weight: bold;
			}
			.columna_contenido{
				border-left-width: 1px;
				border-left-style: solid;
				border-left-color: #000000;	
			}
			.fila_contenido{
				border-bottom-width: 1px;
				border-bottom-style: solid;
				border-bottom-color: #000000;
			}
			.fila_contenido_sup{
				border-top-width: 1px;
				border-top-style: solid;
				border-top-color: #000000;
			}
			.columna_contenido_der{
				border-right-width: 1px;
				border-right-style: solid;
				border-right-color: #000000;	
			}
			.tituloTablasCelesteB{
				border-style: solid;
				color: #000000;
				font-style: normal;
				line-height: normal;
				font-weight: bolder;
			}
			.tabla_informacion_negro{
				border-width: 1px;
				border-style: solid;
			    border-color: #000000;
			}
        </style>
    </head>
    <body>
        <table border="0" width="98%" align="center" cellpadding="0" cellspacing="0" class="textoNegro10">
        	<logic:notEmpty name="ec.com.smx.sic.sispe.articulosDespachoDomicilioImpresion.guayaquil.${vDia}.${vTipo}">
	        	<tr>
					<td align="center">
						<bean:define id="entregasDomicilioGuayaquil" name="ec.com.smx.sic.sispe.articulosDespachoDomicilioImpresion.guayaquil.${vDia}.${vTipo}"></bean:define>
						<bean:define id="entregaDomicilioGuayaquil">${entregasDomicilioGuayaquil[0].fechaEntregaCliente}</bean:define>
						<b style="font-size:12px;">ENTREGAS A DOMICILIO EN GUAYAQUIL PARA EL ${entregaDomicilioGuayaquil.substring(0,10)}</b>
					</td>        	
	        	</tr>
	        	<tr>
					<td align="center" height="10px">
						&nbsp;
					</td>        	
	        	</tr>
	        	<tr>
	        		<td>
	        			<table border="0" cellspacing="0" cellpadding="0" width="100%">
						    <tr>
					            <td colspan="2">
					                <table border="0" cellspacing="0" cellpadding="1" width="100%">
					                    <tr class="tituloTablasB"  align="left">
						                    <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">&nbsp;</td>
						                    <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">No</td>
						                    <td class="columna_contenido fila_contenido fila_contenido_sup" width="50%" align="center">HORA ENTREGA</td>
						                    <td class="columna_contenido fila_contenido fila_contenido_sup" width="20%" align="center">ENT</td>
						                    <td class="columna_contenido fila_contenido fila_contenido_sup columna_contenido_der" width="20%" align="center">ART</td>
					                    </tr>
					                </table>
					            </td>
					        </tr>
					        <tr>
					            <td colspan="2">
						            <table border="0" cellspacing="0" cellpadding="2" width="100%" class="textoNegro10">
						                <logic:iterate name="ec.com.smx.sic.sispe.articulosDespachoDomicilioImpresion.guayaquil.${vDia}.${vTipo}" id="vistaArticuloDTO" indexId="indiceArticulo">
						                    <bean:size id="sizeEntregas" name="vistaArticuloDTO" property="colVistaArticuloDTO"/>
						                    <bean:define id="indiceTotal" value="${indiceArticulo + calendarioBodegaForm.start}"/>
						                    <bean:define id="numFila" value="${indiceTotal + 1}"/>
						                    
						                    <logic:notEqual name="numFila" value="1">
						                    	<tr><td colspan="5" height="5px">&nbsp;</td></tr>
						                    </logic:notEqual>
						                    
						                    <tr class="textoNegro10">
						                        <td class="columna_contenido fila_contenido" width="5%" align="center">
						                        	&nbsp;
						                        </td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center"><bean:write name="numFila"/></td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="50%" align="center" style="font-size:11px;"><bean:write name="vistaArticuloDTO" property="fechaEntregaCliente" formatKey="formatos.hora"/></td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="20%" align="right"><b><bean:write name="vistaArticuloDTO" property="npNumeroEntregas"/><%--bean:write name="sizeEntregas"/--%></b></td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup columna_contenido_der" width="20%" align="right"><b><bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/></b></td>
						                    </tr>
						                    <tr>
						                        <td colspan="5" id="listado_entrega_${indiceArticulo}_${vDia}_${vTipo}" align="center">
						                            <!-- se muestra el detalle de los locales -->
						                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
						                                <tr class="tituloTablasCelesteB">
						                                    <td class="columna_contenido fila_contenido" width="5%" align="center">&nbsp;</td>
									                        <td class="columna_contenido fila_contenido" width="5%" align="center">No</td>
									                        <td class="columna_contenido fila_contenido" width="15%" align="center">No PEDIDO</td>
									                        <td class="columna_contenido fila_contenido" width="10%" align="center">No RES</td>
									                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="0">
										                        <td class="columna_contenido fila_contenido" width="30%" align="center">CONTACTO EMPRESA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">ENTREGADO</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">ENTREGA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">PENDIENTE</td>
									                        </logic:equal>
									                         <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="1">
										                        <td class="columna_contenido fila_contenido" width="45%" align="center">CONTACTO EMPRESA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">ENTREGA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">TOTAL</td>
									                        </logic:equal>
									                        <td class="columna_contenido fila_contenido columna_contenido_der" width="5%" align="center">ART</td>
						                                </tr>
						                                <logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indiceArticulo2">
						                                    <bean:define id="numFila2" value="${indiceArticulo2 + 1}"/>
						                                    
						                                    <logic:notEqual name="numFila2" value="1">
										                    	<tr><td colspan="9" height="5px">&nbsp;</td></tr>
										                    </logic:notEqual>
						                                    
						                                    <tr class="textoNegro10">
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">
						                                            &nbsp;
						                                        </td>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center"><bean:write name="numFila2"/></td>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="15%" align="center">
						                                        	<bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/>
						                                        </td>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="center"><bean:write name="vistaArticuloDTO2" property="llaveContratoPOS"/></td>
						                                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="0">
							                                           <logic:empty name="vistaArticuloDTO2" property="nombreEmpresa">
												                    		<td class="columna_contenido fila_contenido fila_contenido_sup" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoContacto}</td>
												                    	</logic:empty>
												                    	<logic:notEmpty name="vistaArticuloDTO2" property="nombreEmpresa">
												                    		<td class="columna_contenido fila_contenido fila_contenido_sup" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoEmpresa}</td>
												                    	</logic:notEmpty>
							                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="numeroEntregados"/></b></td>
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="npNumeroEntregas"/></b></td>
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b>${vistaArticuloDTO2.numeroTotalEntregas - vistaArticuloDTO2.numeroEntregados}/<bean:write name="vistaArticuloDTO2" property="numeroTotalEntregas"/></b></td>
											                    </logic:equal>
											                    <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="1">
							                                           <logic:empty name="vistaArticuloDTO2" property="nombreEmpresa">
												                    		<td class="columna_contenido fila_contenido fila_contenido_sup" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoContacto}</td>
												                    	</logic:empty>
												                    	<logic:notEmpty name="vistaArticuloDTO2" property="nombreEmpresa">
												                    		<td class="columna_contenido fila_contenido fila_contenido_sup" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoEmpresa}</td>
												                    	</logic:notEmpty>		                                        
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="npNumeroEntregas"/></b></td>
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="numeroTotalEntregas"/></b></td>
											                    </logic:equal>     
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup columna_contenido_der" width="5%" align="right"><b><bean:write name="vistaArticuloDTO2" property="cantidadReservadaEstado"/></b></td>
						                                    </tr>
						                                    <tr>
						                                        <td colspan="9" id="listado_entrega_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}" align="center">
						                                            <!-- se muestra el detalle de los locales -->
						                                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
						                                                <tr class="tituloTablasCelesteB">
						                                                    <td class="columna_contenido fila_contenido" width="4%" align="center">No</td>
						                                                    <td class="columna_contenido fila_contenido" width="15%" align="center">C&Oacute;DIGO BARRAS</td>
						                                                    <td class="columna_contenido fila_contenido" width="25%" align="center">ART&Iacute;CULO</td>
						                                                    <td class="columna_contenido fila_contenido" width="11%" align="center">FECHA ENTREGA</td>
						                                                    <td class="columna_contenido fila_contenido" width="40%" align="center">DOMICILIO</td>
						                                                    <td class="columna_contenido fila_contenido columna_contenido_der" width="5%" align="center">ART</td>
						                                                </tr>
						                                                <logic:iterate name="vistaArticuloDTO2" property="colVistaArticuloDTO" id="vistaArticuloDTO3" indexId="indiceArticulo3">
						                                                    <bean:define id="numFila3" value="${indiceArticulo3 + 1}"/>
						                                                    <tr class="textoNegro10">
						                                                        <td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila3"/></td>
						                                                        <td class="columna_contenido fila_contenido" width="15%" align="center">
						                                                        	<bean:write name="vistaArticuloDTO3" property="codigoBarras"/>
						                                                        </td>
						                                                        <td class="columna_contenido fila_contenido" width="25%" align="left"><bean:write name="vistaArticuloDTO3" property="descripcionArticulo"/></td>
						                                                        <td class="columna_contenido fila_contenido" width="11%" align="left"><bean:write name="vistaArticuloDTO3" property="fechaEntregaCliente" format="dd-MM-yyyy"/></td>
						                                                        <td class="columna_contenido fila_contenido" width="40%" align="left"><logic:notEmpty name="vistaArticuloDTO3" property="ciudadEntrega"><bean:write name="vistaArticuloDTO3" property="ciudadEntrega"/> - </logic:notEmpty><bean:write name="vistaArticuloDTO3" property="direccionEntrega"/></td>
						                                                        <td class="columna_contenido fila_contenido columna_contenido_der" width="5%" align="right"><b><bean:write name="vistaArticuloDTO3" property="cantidadReservadaEstado"/></b></td>
						                                                    </tr>
						                                                </logic:iterate>
						                                            </table>
						                                        </td>
						                                    </tr>
						                                </logic:iterate>
						                            </table>
						                        </td>
						                    </tr>
						                </logic:iterate>
						            </table>
					            </td>
					        </tr>
						</table>
	        		</td>
	        	</tr>
	        	<tr>
	        		<td width="100%" height="30px">
	        			<hr style="border-style:solid;">
	        		</td>
	        	</tr>
        	</logic:notEmpty>
        	<tr>
				<td align="center">
					<b style="font-size:12px;">ENTREGAS A DOMICILIO PARA EL <bean:write name="fechaSeccion" format="yyyy-MM-dd"/></b>
				</td>        	
        	</tr>
        	<tr>
				<td align="center" height="10px">
					&nbsp;
				</td>        	
        	</tr>
        	<tr>
        		<td>
        			<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<logic:notEmpty name="ec.com.smx.sic.sispe.articulosDespachoDomicilioImpresion.${vDia}.${vTipo}">
						    <tr>
					            <td colspan="2">
					                <table border="0" cellspacing="0" cellpadding="1" width="100%">
					                    <tr class="tituloTablasB"  align="left">
						                    <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">&nbsp;</td>
						                    <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">No</td>
						                    <td class="columna_contenido fila_contenido fila_contenido_sup" width="50%" align="center">HORA ENTREGA</td>
						                    <td class="columna_contenido fila_contenido fila_contenido_sup" width="20%" align="center">ENT</td>
						                    <td class="columna_contenido fila_contenido fila_contenido_sup columna_contenido_der" width="20%" align="center">ART</td>
					                    </tr>
					                </table>
					            </td>
					        </tr>
					        <tr>
					            <td colspan="2">
						            <table border="0" cellspacing="0" cellpadding="2" width="100%" class="textoNegro10">
						                <logic:iterate name="ec.com.smx.sic.sispe.articulosDespachoDomicilioImpresion.${vDia}.${vTipo}" id="vistaArticuloDTO" indexId="indiceArticulo">
						                    <bean:size id="sizeEntregas" name="vistaArticuloDTO" property="colVistaArticuloDTO"/>
						                    <bean:define id="indiceTotal" value="${indiceArticulo + calendarioBodegaForm.start}"/>
						                    <bean:define id="numFila" value="${indiceTotal + 1}"/>
						                    
						                    <logic:notEqual name="numFila" value="1">
						                    	<tr><td colspan="5" height="5px">&nbsp;</td></tr>
						                    </logic:notEqual>
						                    
						                    <tr class="textoNegro10">
						                        <td class="columna_contenido fila_contenido" width="5%" align="center">
						                        	&nbsp;
						                        </td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center"><bean:write name="numFila"/></td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="50%" align="center" style="font-size:11px;"><bean:write name="vistaArticuloDTO" property="fechaEntregaCliente" formatKey="formatos.hora"/></td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="20%" align="right"><b><bean:write name="vistaArticuloDTO" property="npNumeroEntregas"/><%--bean:write name="sizeEntregas"/--%></b></td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup columna_contenido_der" width="20%" align="right"><b><bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/></b></td>
						                    </tr>
						                    <tr>
						                        <td colspan="5" id="listado_entrega_${indiceArticulo}_${vDia}_${vTipo}" align="center">
						                            <!-- se muestra el detalle de los locales -->
						                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
						                                <tr class="tituloTablasCelesteB">
						                                    <td class="columna_contenido fila_contenido" width="5%" align="center">&nbsp;</td>
									                        <td class="columna_contenido fila_contenido" width="5%" align="center">No</td>
									                        <td class="columna_contenido fila_contenido" width="15%" align="center">No PEDIDO</td>
									                        <td class="columna_contenido fila_contenido" width="10%" align="center">No RES</td>
									                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="0">
										                        <td class="columna_contenido fila_contenido" width="30%" align="center">CONTACTO EMPRESA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">ENTREGADO</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">ENTREGA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">PENDIENTE</td>
									                        </logic:equal>
									                         <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="1">
										                        <td class="columna_contenido fila_contenido" width="45%" align="center">CONTACTO EMPRESA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">ENTREGA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">TOTAL</td>
									                        </logic:equal>
									                        <td class="columna_contenido fila_contenido columna_contenido_der" width="5%" align="center">ART</td>
						                                </tr>
						                                <logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indiceArticulo2">
						                                    <bean:define id="numFila2" value="${indiceArticulo2 + 1}"/>
						                                    
						                                    <logic:notEqual name="numFila2" value="1">
										                    	<tr><td colspan="9" height="5px">&nbsp;</td></tr>
										                    </logic:notEqual>
						                                    
						                                    <tr class="textoNegro10">
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">
						                                            &nbsp;
						                                        </td>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center"><bean:write name="numFila2"/></td>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="15%" align="center">
						                                        	<bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/>
						                                        </td>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="center"><bean:write name="vistaArticuloDTO2" property="llaveContratoPOS"/></td>
						                                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="0">
							                                           <logic:empty name="vistaArticuloDTO2" property="nombreEmpresa">
												                    		<td class="columna_contenido fila_contenido fila_contenido_sup" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoContacto}</td>
												                    	</logic:empty>
												                    	<logic:notEmpty name="vistaArticuloDTO2" property="nombreEmpresa">
												                    		<td class="columna_contenido fila_contenido fila_contenido_sup" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoEmpresa}</td>
												                    	</logic:notEmpty>
							                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="numeroEntregados"/></b></td>
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="npNumeroEntregas"/></b></td>
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b>${vistaArticuloDTO2.numeroTotalEntregas - vistaArticuloDTO2.numeroEntregados}/<bean:write name="vistaArticuloDTO2" property="numeroTotalEntregas"/></b></td>
											                    </logic:equal>
											                    <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="1">
							                                           <logic:empty name="vistaArticuloDTO2" property="nombreEmpresa">
												                    		<td class="columna_contenido fila_contenido fila_contenido_sup" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoContacto}</td>
												                    	</logic:empty>
												                    	<logic:notEmpty name="vistaArticuloDTO2" property="nombreEmpresa">
												                    		<td class="columna_contenido fila_contenido fila_contenido_sup" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoEmpresa}</td>
												                    	</logic:notEmpty>		                                        
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="npNumeroEntregas"/></b></td>
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="numeroTotalEntregas"/></b></td>
											                    </logic:equal>     
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup columna_contenido_der" width="5%" align="right"><b><bean:write name="vistaArticuloDTO2" property="cantidadReservadaEstado"/></b></td>
						                                    </tr>
						                                    <tr>
						                                        <td colspan="9" id="listado_entrega_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}" align="center">
						                                            <!-- se muestra el detalle de los locales -->
						                                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
						                                                <tr class="tituloTablasCelesteB">
						                                                    <td class="columna_contenido fila_contenido" width="5%" align="center">No</td>
						                                                    <td class="columna_contenido fila_contenido" width="15%" align="center">C&Oacute;DIGO BARRAS</td>
						                                                    <td class="columna_contenido fila_contenido" width="25%" align="center">ART&Iacute;CULO</td>
						                                                    <td class="columna_contenido fila_contenido" width="45%" align="center">DOMICILIO</td>
						                                                    <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="center">ART</td>
						                                                </tr>
						                                                <logic:iterate name="vistaArticuloDTO2" property="colVistaArticuloDTO" id="vistaArticuloDTO3" indexId="indiceArticulo3">
						                                                    <bean:define id="numFila3" value="${indiceArticulo3 + 1}"/>
						                                                    <tr class="textoNegro10">
						                                                        <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila3"/></td>
						                                                        <td class="columna_contenido fila_contenido" width="15%" align="center">
						                                                        	<bean:write name="vistaArticuloDTO3" property="codigoBarras"/>
						                                                        </td>
						                                                        <td class="columna_contenido fila_contenido" width="25%" align="left"><bean:write name="vistaArticuloDTO3" property="descripcionArticulo"/></td>
						                                                        <td class="columna_contenido fila_contenido" width="45%" align="left"><logic:notEmpty name="vistaArticuloDTO3" property="ciudadEntrega"><bean:write name="vistaArticuloDTO3" property="ciudadEntrega"/> - </logic:notEmpty><bean:write name="vistaArticuloDTO3" property="direccionEntrega"/></td>
						                                                        <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="right"><b><bean:write name="vistaArticuloDTO3" property="cantidadReservadaEstado"/></b></td>
						                                                    </tr>
						                                                </logic:iterate>
						                                            </table>
						                                        </td>
						                                    </tr>
						                                </logic:iterate>
						                            </table>
						                        </td>
						                    </tr>
						                </logic:iterate>
						            </table>
					            </td>
					        </tr>
						</logic:notEmpty>
						<logic:empty name="ec.com.smx.sic.sispe.articulosDespachoDomicilioImpresion.${vDia}.${vTipo}">
					    	<tr>
					    		<td width="80%" height="40px" align="center" style="vertical-align: middle;">
					    			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%" class="textoNegro10">
					    				<tr>
					    					<td width="100%" height="100%" class="tabla_informacion amarillo1">
					    						NO TIENE ENTREGAS A DOMICILIO PENDIENTE
					    					</td>
					    				</tr>
					    			</table>
					    		</td>
					    	</tr>
					    </logic:empty>
					</table>
        		</td>
        	</tr>
        	<tr>
        		<td width="100%" height="30px">
        			<hr style="border-style:solid;">
        		</td>
        	</tr>
        	<tr>
				<td align="center">
					<b style="font-size:12px;">DESPACHOS A LOCAL PARA EL <bean:write name="fechaSeccion" format="yyyy-MM-dd"/></b>
				</td>        	
        	</tr>
        	<tr>
				<td align="center" height="10px">
					&nbsp;
				</td>        	
        	</tr>
        	<tr>
        		<td>
        			<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
					    <logic:notEmpty name="ec.com.smx.sic.sispe.articulosDespachoLocalImpresion.${vDia}.${vTipo}">
					        <tr>
					            <td colspan="2">
					                <table border="0" cellspacing="0" cellpadding="1" width="100%">
					                   	<%--<tr class="tituloTablas"  align="left">--%>
					                    <tr class="tituloTablasB"  align="left">
					                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">&nbsp;</td>
					                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">No</td>
					                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="60%" align="center">LOCAL</td>
					                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="15%" align="center">DES</td>
					                        <td class="columna_contenido fila_contenido fila_contenido_sup columna_contenido_der" width="15%" align="center">ART</td>
					                    </tr>
					                </table>
					            </td>
					        </tr>
					        <tr>
					            <td colspan="2">
						            <table border="0" cellspacing="0" cellpadding="2" width="100%" class="textoNegro10">
						                <logic:iterate name="ec.com.smx.sic.sispe.articulosDespachoLocalImpresion.${vDia}.${vTipo}" id="vistaArticuloDTO" indexId="indiceArticulo">
						                    <bean:size id="sizeEntregas" name="vistaArticuloDTO" property="colVistaArticuloDTO"/>
						                    <bean:define id="indiceTotal" value="${indiceArticulo + calendarioBodegaForm.start}"/>
						                    <bean:define id="numFila" value="${indiceTotal + 1}"/>
						                    
						                    <logic:notEqual name="numFila" value="1">
						                    	<tr><td colspan="5" height="5px">&nbsp;</td></tr>
						                    </logic:notEqual>
						                    
						                    <tr class="textoNegro10">
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">
						                            &nbsp;
						                        </td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center"><bean:write name="numFila"/></td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="60%" align="left"><bean:write name="vistaArticuloDTO" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO" property="nombreLocalOrigen"/></td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="15%" align="right"><b><bean:write name="vistaArticuloDTO"  property="npNumeroEntregas"/><%--bean:write name="sizeEntregas"/--%></b></td>
						                        <td class="columna_contenido fila_contenido fila_contenido_sup columna_contenido_der" width="15%" align="right"><b><bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/></b></td>
						                    </tr>
						                    <tr>
						                        <td colspan="5" id="listado_despacho_${indiceArticulo}_${vDia}_${vTipo}" align="center">
						                            <!-- se muestra el detalle de los locales -->
						                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
						                                <%--<tr class="tituloTablasCeleste">--%>
						                                <tr class="tituloTablasCelesteB">
						                                    <td class="columna_contenido fila_contenido" width="5%" align="center">&nbsp;</td>
									                        <td class="columna_contenido fila_contenido" width="5%" align="center">No</td>
									                        <td class="columna_contenido fila_contenido" width="15%" align="center">No PEDIDO</td>
									                        <td class="columna_contenido fila_contenido" width="10%" align="center">No RES</td>
									                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="0">
										                        <td class="columna_contenido fila_contenido" width="35%" align="center">CONTACTO EMPRESA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">DESPACHADO</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">DESPACHO</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">PENDIENTE</td>
									                        </logic:equal>
									                         <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="1">
										                        <td class="columna_contenido fila_contenido" width="45%" align="center">CONTACTO EMPRESA</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">DESPACHO</td>
										                        <td class="columna_contenido fila_contenido" width="10%" align="center">TOTAL</td> 
									                        </logic:equal>
									                        <td class="columna_contenido fila_contenido columna_contenido_der" width="5%" align="center">ART</td>
						                                </tr>
						                                <logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indiceArticulo2">
						                                    <bean:define id="numFila2" value="${indiceArticulo2 + 1}"/>
						                                    
						                                    <logic:notEqual name="numFila2" value="1">
										                    	<tr><td colspan="9" height="5px">&nbsp;</td></tr>
										                    </logic:notEqual>
						                                    
						                                    <tr class="textoNegro10">
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center">
						                                        	&nbsp;
						                                        </td>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="5%" align="center"><bean:write name="numFila2"/></td>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="15%" align="center">
						                                        	<bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/>
						                                        </td>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="center"><bean:write name="vistaArticuloDTO2" property="llaveContratoPOS"/></td>
						                                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="0">
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="35%" align="left">CI:${vistaArticuloDTO2.cedulaContacto} - NC:${vistaArticuloDTO2.nombreContacto} - TC:${vistaArticuloDTO2.telefonoContacto} - NE:${vistaArticuloDTO2.nombreEmpresa}</td>
											                        <td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="numeroEntregados"/></b></td>
						                                        	<td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="npNumeroEntregas"/></b></td>
						                                       		<td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b>${vistaArticuloDTO2.numeroTotalEntregas - vistaArticuloDTO2.numeroEntregados}/<bean:write name="vistaArticuloDTO2" property="numeroTotalEntregas"/></b></td>
										                        </logic:equal>
										                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="1">
						                                        	<td class="columna_contenido fila_contenido fila_contenido_sup" width="45%" align="left">CI:${vistaArticuloDTO2.cedulaContacto} - NC:${vistaArticuloDTO2.nombreContacto} - TC:${vistaArticuloDTO2.telefonoContacto} - NE:${vistaArticuloDTO2.nombreEmpresa}</td>
						                                        	<td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="npNumeroEntregas"/></b></td>
						                                       		<td class="columna_contenido fila_contenido fila_contenido_sup" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="numeroTotalEntregas"/></b></td>
										                        </logic:equal>
						                                        <td class="columna_contenido fila_contenido fila_contenido_sup columna_contenido_der" width="5%" align="right"><b><bean:write name="vistaArticuloDTO2" property="cantidadReservadaEstado"/></b></td>
						                                    </tr>
						                                    <tr>
						                                        <td colspan="9" id="listado_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}" align="center">
						                                            <!-- se muestra el detalle de los locales -->
						                                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
						                                                <%--<tr class="tituloTablasCeleste">--%>
						                                                <tr class="tituloTablasCelesteB">
						                                                    <td class="columna_contenido fila_contenido" width="5%" align="center">No</td>
						                                                    <td class="columna_contenido fila_contenido" width="15%" align="center">C&Oacute;DIGO BARRAS</td>
						                                                    <td class="columna_contenido fila_contenido" width="20%" align="center">ART&Iacute;CULO</td>
						                                                    <td class="columna_contenido fila_contenido" width="20%" align="center">LUGAR ENTREGA</td>
						                                                    <td class="columna_contenido fila_contenido" width="15%" align="center">FECHA DESPACHO</td>
						                                                    <td class="columna_contenido fila_contenido" width="15%" align="center">FECHA ENTREGA</td>
						                                                    <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="center">ART</td>
						                                                </tr>
						                                                <logic:iterate name="vistaArticuloDTO2" property="colVistaArticuloDTO" id="vistaArticuloDTO3" indexId="indiceArticulo3">
						                                                    <bean:define id="numFila3" value="${indiceArticulo3 + 1}"/>
						                                                    <tr class="textoNegro10">
						                                                        <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila3"/></td>
						                                                        <td class="columna_contenido fila_contenido" width="15%" align="center">
						                                                        	<bean:write name="vistaArticuloDTO3" property="codigoBarras"/>
						                                                        </td>
						                                                        <td class="columna_contenido fila_contenido" width="20%" align="left"><bean:write name="vistaArticuloDTO3" property="descripcionArticulo"/></td>
						                                                        <td class="columna_contenido fila_contenido" width="20%" align="left"><bean:write name="vistaArticuloDTO3" property="codigoLocalReferencia"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO3" property="nombreLocalReferencia"/></td>
						                                                        <td class="columna_contenido fila_contenido" width="15%" align="center"><bean:write name="vistaArticuloDTO3" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
						                                                        <td class="columna_contenido fila_contenido" width="15%" align="center"><bean:write name="vistaArticuloDTO3" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
						                                                        <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="right"><b><bean:write name="vistaArticuloDTO3" property="cantidadReservadaEstado"/></b></td>
						                                                    </tr>
						                                                </logic:iterate>
						                                            </table>
						                                        </td>
						                                    </tr>
						                                </logic:iterate>
						                            </table>
						                        </td>
						                    </tr>
						                </logic:iterate>
						            </table>
					            </td>
					        </tr>
					        <tr><td height="5px" colspan="2"></td></tr>
					    </logic:notEmpty>
					    <logic:empty name="ec.com.smx.sic.sispe.articulosDespachoLocalImpresion.${vDia}.${vTipo}">
					    	<tr>
					    		<td width="80%" height="40px" align="center" style="vertical-align: middle;">
					    			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%" class="textoNegro10">
					    				<tr>
					    					<td width="100%" height="100%" class="tabla_informacion amarillo1">
					    						NO TIENE DESPAHO A LOCALES PENDIENTE
					    					</td>
					    				</tr>
					    			</table>
					    		</td>
					    	</tr>
					    </logic:empty>
					    <tr>
					    <td align="center" height="50px">
							&nbsp;
						</td> 
					    </tr>
					    <tr>
					    	<td height="40px" align="center" style="vertical-align: middle;">
					    			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
					    				<tr>
											<td width="50%" height="100%" align="center">
											<table border="0" cellpadding="0" cellspacing="0" width="80%" height="100%">
												<tr>
													<td>
														<hr style="border-style:solid;">
														<center>Firma</center>
													</td>
												</tr>
											</table>
											</td>
											<td width="50%" height="100%" align="center">
												<table border="0" cellpadding="0" cellspacing="0" width="80%" height="100%">
													<tr>
														<td>
															<hr style="border-style:solid;">
															<center>Sello</center>
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
        </table>
    </body>
</html>