<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dTD">

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html>
<div id="imprConvenioMatriz">
<head>	
		<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <meta HTTP-EQUIV="max-age" CONTENT="0">
        <meta HTTP-EQUIV="Expires" CONTENT="0">
		
		<style type="text/css">
			.textoNegro11{
				font-family: Verdana, Arial, Helvetica;
				font-size: 11px;
				color: #000000;
			}
			.textoNegro10{
				font-family: Verdana, Arial, Helvetica;
				font-size: 10px;
				color: #000000;
			}
			.textoNegro12{
				font-family: Verdana, Arial, Helvetica;
				font-size: 12px;
				color: #000000;
			}
			.textoNegro14{
				font-family: Verdana, Arial, Helvetica;
				font-size: 14px;
				color: #000000;
			}
			.textoNegro16{
				font-family: Verdana, Arial, Helvetica;
				font-size: 16px;
				color: #000000;
			}
			.tabla_informacion_encabezado
			{
				border: 1px solid #000000;		
			}
			.tabla_datos
			{
				border: 1px solid #000000;		
				border-bottom:none;
				font-weight:normal;
			}
			.tituloTablasCel
			{
				background-color:#B1C2D6;
				color: #000000;
				font-size: 10px;
				font-style: normal;
				line-height: normal;
				font-family: Verdana, Arial, Helvetica;
				font-weight: bold;
			}
			.fila_contenido
			{
				border-bottom-width: 1px;
				border-bottom-style: solid;
				border-bottom-color: #000000;
			}			
        </style>
	</head>	
<body>
	<logic:iterate id="convenioEntregaDomicilioDTO" name="ec.com.smx.sic.sispe.convenioEntregaCol" indexId="numArticulo">
	<bean:define id="convenioEntregaDetalleCol" name="convenioEntregaDomicilioDTO" property="convenioEntregaDomicilioDetalleSet"/>
   <table border="0" class="textoNegro11" width="100%" align="center" cellpadding="0" cellspacing="0">
	  <!-- ============================================================================== -->
	  <!-- Inicio Cabecera															    -->
	  <!-- ============================================================================== -->
  		<c:if test="${numArticulo!=0}">
	  		<tr><td><p style="page-break-before:always"></p></td></tr>
  		</c:if>
  		<tr>
			<td>
				<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left" width="10%"></td>
						<td align="right" width="89%" class="textoNegro14">
							<table border="0" width="100%" cellpadding="0" cellspacing="0">
	                            <tr>
	                                <td width="80%" align="right"><b>Convenio de entrega a domicilio N°:</b>&nbsp;</td>
	                                <td width="20%" align="left">&nbsp;<bean:write name="convenioEntregaDomicilioDTO" property="id.secuencialConvenio"/></td>
	                            </tr>
	                            <tr>
	                                <td align="right">Fecha:&nbsp;</td>
	                                <td align="left"><bean:write name="convenioEntregaDomicilioDTO" property="fechaConvenio" formatKey="formatos.fecha"/></td>
	                            </tr>
	                        </table>
						</td>
					</tr>
			    </table>
			</td>
		</tr>
	  <!-- ============================================================================== -->
	  <!-- Fin Cabecera															    	-->
	  <!-- ============================================================================== -->
      <tr><td height="10px"></td></tr>
      <tr>
   	    <td>
        <table border="0" width="100%" cellpadding="3" cellspacing="0">
          <tr>
            <td width="7%" align="left" class="textoNegro11">Local:</td>
            <td width="93%" align="left" class="textoNegro11">
            	<bean:write name="convenioEntregaDomicilioDTO" property="codigoLocalDestino"/>&nbsp;-&nbsp;
                <bean:write name="convenioEntregaDomicilioDTO" property="localDestinoDTO.nombreLocal"/>&nbsp;
                (N° Tel.: <bean:write name="convenioEntregaDomicilioDTO" property="localDestinoDTO.telefonoLocal"/>; 
                E-mail: <bean:write name="convenioEntregaDomicilioDTO" property="localDestinoDTO.emailLocal"/>)
            </td>
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
                  <td align="left" class="textoNegro11" width="12%">Vendedor:</td>
            	  <td align="left" class="textoNegro11" width="88%">
            		<logic:notEmpty name="convenioEntregaDomicilioDTO" property="vendedorDTO">
                      <bean:write name="convenioEntregaDomicilioDTO" property="vendedorDTO.nombreCompleto"/>
                	</logic:notEmpty>
            	  </td>
                </tr>
          	  </table>
          	</td>            
          </tr>                    
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="12%">Factura N°:</td>
          	  	  <td align="left" class="textoNegro11" width="20%"><bean:write name="convenioEntregaDomicilioDTO" property="npNumeroDocumentoFiscal"/></td>
          	  	  <td align="left" class="textoNegro11" width="18%">Titular convenio:</td>
          	  	  <td align="left" class="textoNegro11" width="50%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="titularConvenio">
                        <bean:write name="convenioEntregaDomicilioDTO" property="titularConvenio"/>
                    </logic:notEmpty>
                    <logic:empty name="convenioEntregaDomicilioDTO" property="titularConvenio">
                        <logic:notEmpty name="convenioEntregaDomicilioDTO" property="personaDTO">
                             <bean:write name="convenioEntregaDomicilioDTO" property="personaDTO.nombreCompleto" />
                        </logic:notEmpty>
                        <logic:notEmpty name="convenioEntregaDomicilioDTO" property="empresaDTO">															
                            <bean:write name="convenioEntregaDomicilioDTO" property="empresaDTO.razonSocialEmpresa" />
                        </logic:notEmpty>
                    </logic:empty>
          	  	  </td>
          	  	</tr>
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="19%">Direcci&oacute;n entrega:</td>
            	  <td align="left" class="textoNegro11" width="81%"><bean:write name="convenioEntregaDomicilioDTO" property="direccionEntrega"/></td>
          	  	</tr>
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="8%">Sector:</td>
          	  	  <td align="left" class="textoNegro11" width="30%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="sectorDireccionEntrega">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="sectorDireccionEntrega"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="sectorDireccionEntrega">
	            		NA
	            	</logic:empty>
          	  	  	
          	  	  </td>
          	  	  <td align="left" class="textoNegro11" width="12%">Referencia:</td>
          	  	  <td align="left" class="textoNegro11" width="50%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="referenciaDireccionEntrega">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="referenciaDireccionEntrega"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="referenciaDireccionEntrega">
	            		NA
	            	</logic:empty>          	  	  	
          	  	  </td>
          	  	</tr>                
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="20%">Tel&eacute;fono domicilio:</td>
          	  	  <td align="left" class="textoNegro11" width="18%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="npTelefonoDomicilio">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="npTelefonoDomicilio"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="npTelefonoDomicilio">
	            		N/D
	            	</logic:empty>          	  	  	
          	  	  </td>
          	  	  <td align="left" class="textoNegro11" width="12%">Celular:</td>
          	  	  <td align="left" class="textoNegro11" width="25%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="npTelefonoCelular">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="npTelefonoCelular"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="npTelefonoCelular">
	            		N/D
	            	</logic:empty>          	  	  	
          	  	  </td>
                  <td align="left" class="textoNegro11" width="10%">Oficina:</td>
          	  	  <td align="left" class="textoNegro11" width="15%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="npTelefonoOficina">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="npTelefonoOficina"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="npTelefonoOficina">
	            		N/D
	            	</logic:empty>          	  	  	
          	  	  </td>
          	  	</tr>
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="25%">Fecha tentativa de entrega:</td>
	              <td align="left" class="textoNegro11" width="75%">
	            	<bean:write name="convenioEntregaDomicilioDTO" property="fechaEntrega" formatKey="formatos.fecha"/>
	                &nbsp;entre las&nbsp;<bean:write name="convenioEntregaDomicilioDTO" property="horasEntrega"/>&nbsp;HORAS
	              </td>
          	  	</tr>
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="15%">Qui&eacute;n recibir&aacute;:</td>
	              <td align="left" class="textoNegro11" width="85%">
	            	<bean:write name="convenioEntregaDomicilioDTO" property="responsableRecepcion"/>
	              </td>
          	  	</tr>
          	  </table>
          	</td>
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="15%">Pedido generado desde SISPE, pedido #<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>, reserva #<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/></td>
          	  	</tr>
          	  </table>
          	</td>
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
		            <td align="left" class="textoNegro11" width="15%">
                    	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="description">
	                        Observaci&oacute;n:
                        </logic:notEmpty>
                        <logic:empty name="convenioEntregaDomicilioDTO" property="description">
		            		&nbsp;
		            	</logic:empty>
                    </td>
		            <td align="left" class="textoNegro11" width="85%">
		            	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="description">
		            		<bean:write name="convenioEntregaDomicilioDTO" property="description"/>
		            	</logic:notEmpty>
		            	<logic:empty name="convenioEntregaDomicilioDTO" property="description">
		            		&nbsp;
		            	</logic:empty>
		            </td>
		          </tr>
          	  </table>
          	</td>            
          </tr>          
         </table>
        </td>
      </tr>
      <tr><td height="10px"></td></tr>
      <tr>
        <td>
            <table border="0" width="100%" cellpadding="3" cellspacing="0">
                <tr>
                    <td> 
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <td >                                    
                                    <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" height="20px" style="border-bottom:none;">                                        
                                        <tr>
                                            <td width="5%" align="center"  class="fila_contenido textoNegro11"><B>Cant.</B></td>
                                            <td width="16%" align="center"  class="fila_contenido textoNegro11"><B>C&oacute;digo barras</B></td>
                                            <td width="24%" align="center"  class="fila_contenido textoNegro11"><B>Item</B></td>
                                            <td width="12%" align="center"  class="fila_contenido textoNegro11"><B>Marca</B></td>
                                            <td width="12%" align="center"  class="fila_contenido textoNegro11"><B>Modelo</B></td>
                                            <td width="8%" align="center"  class="fila_contenido textoNegro11"><B>Tipo</B></td>
                                        	<td width="8%" align="center"  class="fila_contenido textoNegro11"><B>Entregar a domi.</B></td>
                                        	<td width="15%" align="center"  class="fila_contenido textoNegro11"><B>Observaci&oacute;n</B></td>
                                        </tr>	
                                    </table>		
                                </td>
                            </tr>                           
                            <tr>
                                <td>                                    
                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">                                                                            
                                        <logic:notEmpty name="convenioEntregaDetalleCol">
                                        	<bean:size id="numeroArticulosConvenio" name="convenioEntregaDetalleCol"/>
                                        	<logic:iterate id="convenioEntregaDetalleDTO" name="convenioEntregaDetalleCol" indexId="numArticulo">                                                                                                                                                       
                                                <tr>
                                                    <td align="right" width="5%" class="textosNegroC10" style="padding-right:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="cantidad"/>
                                                    </td>
                                                    <td align="right" width="16%" class="textosNegroC10" style="padding-right:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/>
                                                    </td>
                                                    <td align="left" width="24%" class="textosNegroC10" style="padding-left:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="description"/>                                                        
                                                    </td>                                                    
                                                    <td align="left" width="12%" class="textosNegroC10" style="padding-left:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="marcaArticulo"/>
                                                    </td>
                                                    <td align="left" width="12%" class="textosNegroC10" style="padding-left:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="modeloArticulo"/>
                                                    </td>
                                                    <td align="left" width="8%" class="textosNegroC10" valign="middle" style="padding-left:3px;">
                                                        <smx:equal name="convenioEntregaDetalleDTO" property="tipoArticulo" valueKey="tipo.articulo.venta">
                                                           <bean:message key="etiqueta.venta"/> 
                                                        </smx:equal>
                                                        <smx:equal name="convenioEntregaDetalleDTO" property="tipoArticulo" valueKey="tipo.articulo.premio">
                                                            <bean:message key="etiqueta.premio"/>
                                                        </smx:equal>
                                                    </td>
                                                    <td align="center" width="8%" class="textosNegroC10" style="padding-left:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="estadoDetalle"/>
                                                    </td>
                                                    <td align="left" width="15%" class="textosNegroC10" style="padding-left:3px;">
                                                        <logic:notEmpty name="convenioEntregaDetalleDTO" property="observacion">
                                                        	<bean:write name="convenioEntregaDetalleDTO" property="observacion"/>
                                                        </logic:notEmpty>
                                                        <logic:empty name="convenioEntregaDetalleDTO" property="observacion">
                                                        	------------------
                                                        </logic:empty>
                                                    </td>
                                                </tr>
                                            </logic:iterate>
                                        </logic:notEmpty>                                
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
      </tr>
      <!-- ============================================================================== -->
	  <!-- Texto del Convenio
	  <!-- ============================================================================== -->      
      <tr><td height="5px"></td></tr>
      <tr>
      	<td align="center">
			<table border="0" width="100%" cellpadding="0" cellspacing="0"> 
				<c:if test="${numeroArticulosConvenio>8}">
                    <tr><td><p style="page-break-before:always"></p></td></tr>
                </c:if>
                <tr>
                    <td>                        
                        <table border="0" width="100%" align="left" cellspacing="0" cellpadding="3">
                            <tr>
                            	<td align="left" colspan="2"><br><bean:message key="convenio.domicilio.nota.titulo"/>:<br></td>                                
                            </tr>
                            <tr>
                                <td align="left" valign="top" colspan="2"><bean:message key="convenio.domicilio.nota.parrafo.cero"/></td>
                            </tr>
                            <tr>
                            	<td align="left" width="25px">1.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.uno"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">2.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.dos"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">3.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.tres"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">4.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.cuatro"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">5.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.cinco"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">6.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.seis"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">7.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.siete"/></td>
                            </tr>
                        </table>                        
                    </td>
                </tr>
            </table>
         </td>           
      </tr> 
      <!-- ============================================================================== -->
	  <!--  Fin Texto del Contrato
	  <!-- ============================================================================== --> 	    	        
	  <!-- ============================================================================== -->
	  <!-- Firma
	  <!-- ============================================================================== -->      
      <tr><td height="15px"></td></tr>
	  <tr>
        <td>                        
            <table border="0" width="100%" align="left" cellspacing="0" cellpadding="0">
            	<tr>
                	<td align="center">Conforme con las condiciones</td>
                </tr>
            	<tr><td height="50px" colspan="2"></td></tr>
                <tr>
                	<td align="center">___________________________________</td>
                </tr>                
                <tr>
                	<td align="center">
                		<logic:notEmpty name="convenioEntregaDomicilioDTO" property="personaDTO">		                	
		                    <bean:write name="convenioEntregaDomicilioDTO" property="personaDTO.nombreCompleto"/>
		                </logic:notEmpty>
		                <logic:notEmpty name="convenioEntregaDomicilioDTO" property="empresaDTO">
		                    <bean:write name="convenioEntregaDomicilioDTO" property="empresaDTO.razonSocialEmpresa"/>
		                </logic:notEmpty>
                	</td>
                </tr>                
                <tr>
                	<td align="center">CLIENTE</td>
                </tr>
                
			</table>
        </td>
      </tr>      
      <!-- ============================================================================== -->
	  <!--  Fin Firma
	  <!-- ============================================================================== -->
   </table>
   <table border="0" class="textoNegro11" width="100%" align="center" cellpadding="0" cellspacing="0">
	  <!-- ============================================================================== -->
	  <!-- Inicio Cabecera															    -->
	  <!-- ============================================================================== -->
  		<tr><td><p style="page-break-before:always"></p></td></tr>
        <tr>
			<td>
				<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left" width="10%"></td>
						<td align="right" width="89%" class="textoNegro14">
							<table border="0" width="100%" cellpadding="0" cellspacing="0">
	                            <tr>
	                                <td width="80%" align="right"><b>Convenio de entrega a domicilio N°:</b>&nbsp;</td>
	                                <td width="20%" align="left">&nbsp;<bean:write name="convenioEntregaDomicilioDTO" property="id.secuencialConvenio"/></td>
	                            </tr>
	                            <tr>
	                                <td align="right">Fecha:&nbsp;</td>
	                                <td align="left"><bean:write name="convenioEntregaDomicilioDTO" property="fechaConvenio" formatKey="formatos.fecha"/></td>
	                            </tr>
	                        </table>
						</td>
					</tr>
			    </table>
			</td>
		</tr>
	  <!-- ============================================================================== -->
	  <!-- Fin Cabecera															    	-->
	  <!-- ============================================================================== -->
      <tr><td height="10px"></td></tr>
      <tr>
   	    <td>
        <table border="0" width="100%" cellpadding="3" cellspacing="0">
          <tr>
            <td width="7%" align="left" class="textoNegro11">Local:</td>
            <td width="93%" align="left" class="textoNegro11">
            	<bean:write name="convenioEntregaDomicilioDTO" property="codigoLocalDestino"/>&nbsp;-&nbsp;
                <bean:write name="convenioEntregaDomicilioDTO" property="localDestinoDTO.nombreLocal"/>&nbsp;
                (N° tel.: <bean:write name="convenioEntregaDomicilioDTO" property="localDestinoDTO.telefonoLocal"/>; 
                E-mail: <bean:write name="convenioEntregaDomicilioDTO" property="localDestinoDTO.emailLocal"/>)
            </td>
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
                  <td align="left" class="textoNegro11" width="12%">Vendedor:</td>
            	  <td align="left" class="textoNegro11" width="88%">
            		<logic:notEmpty name="convenioEntregaDomicilioDTO" property="vendedorDTO">
                      <bean:write name="convenioEntregaDomicilioDTO" property="vendedorDTO.nombreCompleto"/>
                	</logic:notEmpty>
            	  </td>
                </tr>
          	  </table>
          	</td>            
          </tr>                     
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="12%">Factura N°:</td>
          	  	  <td align="left" class="textoNegro11" width="20%"><bean:write name="convenioEntregaDomicilioDTO" property="npNumeroDocumentoFiscal"/></td>
          	  	  <td align="left" class="textoNegro11" width="18%">Titular convenio:</td>
          	  	  <td align="left" class="textoNegro11" width="50%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="titularConvenio">
                        <bean:write name="convenioEntregaDomicilioDTO" property="titularConvenio"/>
                    </logic:notEmpty>
                    <logic:empty name="convenioEntregaDomicilioDTO" property="titularConvenio">
                        <logic:notEmpty name="convenioEntregaDomicilioDTO" property="personaDTO">
                             <bean:write name="convenioEntregaDomicilioDTO" property="personaDTO.nombreCompleto" />
                        </logic:notEmpty>
                        <logic:notEmpty name="convenioEntregaDomicilioDTO" property="empresaDTO">															
                            <bean:write name="convenioEntregaDomicilioDTO" property="empresaDTO.razonSocialEmpresa" />
                        </logic:notEmpty>
                    </logic:empty>
          	  	  </td>
          	  	</tr>
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="19%">Direcci&oacute;n entrega:</td>
            	  <td align="left" class="textoNegro11" width="81%"><bean:write name="convenioEntregaDomicilioDTO" property="direccionEntrega"/></td>
          	  	</tr>
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="8%">Sector:</td>
          	  	  <td align="left" class="textoNegro11" width="30%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="sectorDireccionEntrega">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="sectorDireccionEntrega"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="sectorDireccionEntrega">
	            		NA
	            	</logic:empty>
          	  	  	
          	  	  </td>
          	  	  <td align="left" class="textoNegro11" width="12%">Referencia:</td>
          	  	  <td align="left" class="textoNegro11" width="50%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="referenciaDireccionEntrega">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="referenciaDireccionEntrega"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="referenciaDireccionEntrega">
	            		NA
	            	</logic:empty>          	  	  	
          	  	  </td>
          	  	</tr>
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="20%">Tel&eacute;fono domicilio:</td>
          	  	  <td align="left" class="textoNegro11" width="18%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="npTelefonoDomicilio">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="npTelefonoDomicilio"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="npTelefonoDomicilio">
	            		N/D
	            	</logic:empty>          	  	  	
          	  	  </td>
          	  	  <td align="left" class="textoNegro11" width="12%">Celular:</td>
          	  	  <td align="left" class="textoNegro11" width="25%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="npTelefonoCelular">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="npTelefonoCelular"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="npTelefonoCelular">
	            		N/D
	            	</logic:empty>          	  	  	
          	  	  </td>
                  <td align="left" class="textoNegro11" width="10%">Oficina:</td>
          	  	  <td align="left" class="textoNegro11" width="15%">
          	  	  	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="npTelefonoOficina">
	            		<bean:write name="convenioEntregaDomicilioDTO" property="npTelefonoOficina"/>
	            	</logic:notEmpty>
	            	<logic:empty name="convenioEntregaDomicilioDTO" property="npTelefonoOficina">
	            		N/D
	            	</logic:empty>          	  	  	
          	  	  </td>
          	  	</tr>
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="25%">Fecha tentativa de entrega:</td>
	              <td align="left" class="textoNegro11" width="75%">
	            	<bean:write name="convenioEntregaDomicilioDTO" property="fechaEntrega" formatKey="formatos.fecha"/>
	                &nbsp;entre las&nbsp;<bean:write name="convenioEntregaDomicilioDTO" property="horasEntrega"/>&nbsp;HORAS
	              </td>
          	  	</tr>
          	  </table>
          	</td>            
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="15%">Qui&eacute;n recibir&aacute;:</td>
	              <td align="left" class="textoNegro11" width="85%">
	            	<bean:write name="convenioEntregaDomicilioDTO" property="responsableRecepcion"/>
	              </td>
          	  	</tr>
          	  </table>
          	</td>
          </tr>
           <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
          	  	  <td align="left" class="textoNegro11" width="15%">Pedido generado desde SISPE, pedido #<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>, reserva #<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/></td>
          	  	</tr>
          	  </table>
          	</td>
          </tr>
          <tr>
          	<td colspan="2">
          	  <table border="0" width="100%" cellpadding="0" cellspacing="0">
          	  	<tr>
		            <td align="left" class="textoNegro11" width="15%">
                    	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="description">
	                        Observaci&oacute;n:
                        </logic:notEmpty>
                        <logic:empty name="convenioEntregaDomicilioDTO" property="description">
		            		&nbsp;
		            	</logic:empty>
                    </td>
		            <td align="left" class="textoNegro11" width="85%">
		            	<logic:notEmpty name="convenioEntregaDomicilioDTO" property="description">
		            		<bean:write name="convenioEntregaDomicilioDTO" property="description"/>
		            	</logic:notEmpty>
		            	<logic:empty name="convenioEntregaDomicilioDTO" property="description">
		            		&nbsp;
		            	</logic:empty>
		            </td>
		          </tr>
          	  </table>
          	</td>            
          </tr>          
         </table>
        </td>
      </tr>
      <tr><td height="10px"></td></tr>
      <tr>
        <td>
            <table border="0" width="100%" cellpadding="3" cellspacing="0">
                <tr>
                    <td> 
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <td >                                    
                                    <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" height="20px" style="border-bottom:none;">                                        
                                        <tr>
                                            <td width="5%" align="center"  class="fila_contenido textoNegro11"><B>Cant.</B></td>
                                            <td width="16%" align="center"  class="fila_contenido textoNegro11"><B>C&oacute;digo barras</B></td>
                                            <td width="24%" align="center"  class="fila_contenido textoNegro11"><B>Item</B></td>
                                            <td width="12%" align="center"  class="fila_contenido textoNegro11"><B>Marca</B></td>
                                            <td width="12%" align="center"  class="fila_contenido textoNegro11"><B>Modelo</B></td>
                                            <td width="8%" align="center"  class="fila_contenido textoNegro11"><B>Tipo</B></td>
                                        	<td width="8%" align="center"  class="fila_contenido textoNegro11"><B>Entregar a  domi.</B></td>
                                        	<td width="15%" align="center"  class="fila_contenido textoNegro11"><B>Observaci&oacute;n</B></td>
                                        </tr>	
                                    </table>		
                                </td>
                            </tr>                           
                            <tr>
                                <td>                                    
                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">                                                                            
                                        <logic:notEmpty name="convenioEntregaDetalleCol">
                                        	<bean:size id="numeroArticulosConvenio" name="convenioEntregaDetalleCol"/>
                                        	<logic:iterate id="convenioEntregaDetalleDTO" name="convenioEntregaDetalleCol" indexId="numArticulo">                                                                                                                                                       
                                                <tr>
                                                	<td align="right" width="5%" class="textosNegroC10" style="padding-right:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="cantidad"/>
                                                    </td>
                                                    <td align="right" width="16%" class="textosNegroC10" style="padding-right:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/>
                                                    </td>
                                                    <td align="left" width="24%" class="textosNegroC10" style="padding-left:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="description"/>                                                        
                                                    </td>                                                    
                                                    <td align="left" width="12%" class="textosNegroC10" style="padding-left:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="marcaArticulo"/>
                                                    </td>
                                                    <td align="left" width="12%" class="textosNegroC10" style="padding-left:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="modeloArticulo"/>
                                                    </td>
                                                    <td align="left" width="8%" class="textosNegroC10" valign="middle" style="padding-left:3px;">
                                                        <smx:equal name="convenioEntregaDetalleDTO" property="tipoArticulo" valueKey="tipo.articulo.venta">
                                                           <bean:message key="etiqueta.venta"/> 
                                                        </smx:equal>
                                                        <smx:equal name="convenioEntregaDetalleDTO" property="tipoArticulo" valueKey="tipo.articulo.premio">
                                                            <bean:message key="etiqueta.premio"/>
                                                        </smx:equal>
                                                    </td>
                                                    <td align="center" width="8%" class="textosNegroC10" style="padding-left:3px;">
                                                        <bean:write name="convenioEntregaDetalleDTO" property="estadoDetalle"/>
                                                    </td>
                                                    <td align="left" width="15%" class="textosNegroC10" style="padding-left:3px;">
                                                        <logic:notEmpty name="convenioEntregaDetalleDTO" property="observacion">
                                                        	<bean:write name="convenioEntregaDetalleDTO" property="observacion"/>
                                                        </logic:notEmpty>
                                                        <logic:empty name="convenioEntregaDetalleDTO" property="observacion">
                                                        	------------------
                                                        </logic:empty>
                                                    </td>
                                                </tr>
                                            </logic:iterate>
                                        </logic:notEmpty>                                
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
      </tr>
      <!-- ============================================================================== -->
	  <!-- Texto del Convenio
	  <!-- ============================================================================== -->      
      <tr><td height="5px"></td></tr>
      <tr>
      	<td align="center">
			<table border="0" width="100%" cellpadding="0" cellspacing="0"> 
				<c:if test="${numeroArticulosConvenio>8}">
                    <tr><td><p style="page-break-before:always"></p></td></tr>
                </c:if>
                <tr>
                    <td>                        
                        <table border="0" width="100%" align="left" cellspacing="0" cellpadding="3">
                            <tr>
                            	<td align="left" colspan="2"><br><bean:message key="convenio.domicilio.nota.titulo"/>:<br></td>                                
                            </tr>
                            <tr>
                                <td align="left" valign="top" colspan="2"><bean:message key="convenio.domicilio.nota.parrafo.cero"/>
                            </tr>
                            <tr>
                            	<td align="left" width="25px">1.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.uno"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">2.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.dos"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">3.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.tres"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">4.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.cuatro"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">5.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.cinco"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">6.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.seis"/></td>
                            </tr>
                            <tr>
                            	<td align="left" valign="top">7.-&nbsp;</td>
                                <td align="left" valign="top"><bean:message key="convenio.domicilio.nota.parrafo.siete"/></td>
                            </tr>
                        </table>                        
                    </td>
                </tr>
            </table>
         </td>           
      </tr> 
      <!-- ============================================================================== -->
	  <!--  Fin Texto del Contrato
	  <!-- ============================================================================== --> 	    	        
	  <!-- ============================================================================== -->
	  <!-- Firma
	  <!-- ============================================================================== -->      
      <tr><td height="15px"></td></tr>
	  <tr>
        <td>                        
            <table border="0" width="100%" align="left" cellspacing="0" cellpadding="0">
            	<tr>
                	<td align="center">Conforme con las condiciones</td>
                </tr>
            	<tr><td height="50px" colspan="2"></td></tr>
                <tr>
                	<td align="center">___________________________________</td>
                </tr>                
                <tr>
                	<td align="center">
                		<logic:notEmpty name="convenioEntregaDomicilioDTO" property="personaDTO">		                	
		                    <bean:write name="convenioEntregaDomicilioDTO" property="personaDTO.nombreCompleto"/>
		                </logic:notEmpty>
		                <logic:notEmpty name="convenioEntregaDomicilioDTO" property="empresaDTO">
		                    <bean:write name="convenioEntregaDomicilioDTO" property="empresaDTO.razonSocialEmpresa"/>
		                </logic:notEmpty>
                	</td>
                </tr>                
                <tr>
                	<td align="center">CLIENTE</td>
                </tr>
                
			</table>
        </td>
      </tr>
      <!-- ============================================================================== -->
	  <!--  Fin Firma
	  <!-- ============================================================================== -->
   </table>
   <table border="0" class="textoNegro11" width="100%" align="center" cellpadding="0" cellspacing="0">
	  <tr><td height="20px"></td></tr>
      <tr>
        <td>
            <table border="0" width="100%" cellpadding="3" cellspacing="0">
                <tr>
                    <td> 
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <td >                                    
                                    <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion_encabezado" height="20px" style="border-bottom:none;">                                        
                                        <td align="left" valign="middle" class="fila_contenido" height="25px">
                                            &nbsp;<B>NOTA: POR FAVOR DIBUJAR EL CROQUIS DE LA DIRECCI&Oacute;N ENTREGA AL REVERSO DE ESTA HOJA</B>
                                        </td>
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
   </logic:iterate>
</body>
</div>
</html>