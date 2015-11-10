<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction"  classname="java.lang.String" ignore="true"/>

<logic:notEmpty name="ec.com.smx.sic.sispe.usuarioAccesoCalendario">

<div id="div_imprimirDia" style="display:none;">
	<logic:notEmpty name="ec.com.smx.sic.sispe.funcionImprimir">
		<script language="JavaScript">window.print();</script>
	</logic:notEmpty>
</div>

<script type="text/javascript">
	jQuery().ready(function(){
		$j("#dialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 700,
			width: 900,
			modal: true,
			show: 'slide',
			hide: 'slide',
			zIndex: 98,
			beforeclose: function() {
				$j('#div_detalleCanasta').hide();
			}
		});
	});
	jQuery().ready(function(){
		$j("#dialogConfigEntr").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 700,
			width: 900,
			modal: true,
			show: 'slide',
			hide: 'slide',
			zIndex: 98,
			title: 'Configuración de entregas'
		});
	});
	
	jQuery().ready(function(){
		$j("#dialogGuardarEntr").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 90,
			width: 400,
			modal: true,
			show: 'slide',
			hide: 'slide',
			zIndex: 98,
			title: 'Confirmación',
			resizable: false
		});
	});
</script>

<div id="dialog">
	<div id="div_detallePedido">
		<tiles:insert page="/servicioCliente/estadoPedido/detalleComun.jsp"/>
	</div>
	<div id="div_detalleCanasta" class="displayNo">
		<table border="0" width="100%" cellspacing="0" cellpadding="0">
			<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
				<tr>
					<td align="left" valign="top" width="100%">
						<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
							<tr>
								<td  width="3%" align="center"><img src="images/detalle_canasto.gif" border="0"></img></td>
								<td height="35" valign="middle">Detalle del canasto</td>
								<td align="right">
									<table border="0">
										<tr>
											<td>
												<div id="botonA" onclick="$j('#div_detalleCanasta').hide();$j('#div_detallePedido').show();">
													<a href="#" class="atrasA">Atras</a>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>	
			</logic:notEmpty>
			<logic:empty name="ec.com.smx.sic.sispe.vistaPedido">
				<tr>
					<td align="left" valign="top" width="100%">
						<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
							<tr>
								<td  width="3%" align="center"><img src="images/detalle_canasto.gif" border="0"></img></td>
								<td height="35" valign="middle">Detalle del canasto</td>
								<td align="right">&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>
			</logic:empty>
			<tr>
				<td>
					<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanastaLP">
						<tiles:insert page="/servicioCliente/estadoPedido/detalleCanastaComunLP.jsp"/>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta">
						<tiles:insert page="/servicioCliente/estadoPedido/detalleCanastaComun.jsp"/>
					</logic:notEmpty>					
				</td>
			</tr>
		</table>
		<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta">
			<script type="text/javascript">
				//alert('asd');
				$j('#div_detallePedido').hide();
				$j('#div_detalleCanasta').show();
			</script>
		</logic:notEmpty>
	</div>
</div>
<div id="openDialog" style="display:none;visibility:hidden;">
	<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
		<script type="text/javascript">
			$j('#dialog').dialog('open');
		</script>
	</logic:notEmpty>
	<logic:empty name="ec.com.smx.sic.sispe.vistaPedido">
			<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta">
				<script type="text/javascript">
					$j('#dialog').dialog('open');
					mostrarDetCanDir = false;
				</script>
			</logic:notEmpty>
			<logic:empty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta">
				<script type="text/javascript">
					if(mostrarDetalleCanastaDirecto){
						$j('#div_detallePedido').hide();
						$j('#div_detalleCanasta').show();
						$j('#dialog').dialog('open');
					}
					mostrarDetCanDir = false;
				</script>
			</logic:empty>
	</logic:empty>
</div>

<div id="dialogConfigEntr">
	<div id="div_configEntregas">
	<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
			<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/entregaArticuloLocalComun.jsp"/>
	</logic:notEmpty>
	</div>
</div>

<div id='openDialogConfigEntr' style="display:none;visibility:hidden;">
	<logic:notEmpty name="ec.com.smx.sic.sispe.entregaArticuloLocal">
		<script type="text/javascript">
			$j('#dialogConfigEntr').dialog('open');
		</script>
	</logic:notEmpty>
</div>
<logic:empty name="ec.com.smx.sic.sispe.entregaArticuloLocal">
		<script type="text/javascript">
		$j('#dialogConfigEntr').dialog('close');
		</script>
</logic:empty>

<div id="dialogGuardarEntr">
		<div id="div_guardarEntregas" style="display:none;visibility:hidden;">
			<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/confirmacionLocalEntregaPerecibles.jsp"/>
		</div>
</div>
<div id="guardarEntregas">
		<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
			<script type="text/javascript">
				$j('#dialogConfigEntr').dialog('open');
				$j('#dialogGuardarEntr').dialog('close');
				$j('#div_guardarEntregas').hide();
			</script>
		</logic:notEmpty>			
		<logic:notEmpty name="ec.com.smx.sic.sispe.localSeleccionado">
			<logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacion">
				<script type="text/javascript">
					$j('#dialogGuardarEntr').dialog('close');
					$j('#div_guardarEntregas').hide();
					actualizarCalendarioEntregas();
				</script>
			</logic:empty>
		</logic:notEmpty>
</div>

<div style="position:relative;width:100%;height:100%;">
	<div id="div_calendario" class="seccionCalendario" style="position:absolute;left:0.5%;top:1%;width:49%;height:98%;">
		<tiles:insert page="/calendarioBodega/calendario.jsp">
			<tiles:put name="vtformName" beanName="vformName"/>
			<tiles:put name="vtformAction" beanName="vformAction"/>
			<tiles:put name="vtMes" value="sel"/>
		</tiles:insert>
	</div>
	<div id="div_desplazarCalendario" style="position:absolute;left:49.7%;top:1%;width:1%;height:98%;">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td valign="bottom">
					<a href="#" style="height:100%;width:100%;text-decoration:none;" title="Desplazar Calendario" onclick="desplazarSeccionCalendario();"><img id="img_desplazarCalendario" src="images/flechaVerdeIzq.gif" border="0"></a>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<a href="#" style="height:100%;width:100%;text-decoration:none;" onclick="desplazarSeccionDias();"><img id="img_desplazarDiaTodos" title="Mostrar Mes Siguiente" src="images/flechaRojaDer.gif" border="0"></a>
				</td>
			</tr>
		</table>
	</div>
	<div id="div_calendarioPosterior" class="seccionCalendario" style="position:absolute;left:50.5%;top:1%;width:49%;height:98%;">
		<tiles:insert page="/calendarioBodega/calendario.jsp">
			<tiles:put name="vtformName" beanName="vformName"/>
			<tiles:put name="vtformAction" beanName="vformAction"/>
			<tiles:put name="vtMes" value="pos"/>
		</tiles:insert>
	</div>
	<div id="div_diaHoy" class="seccionDiaHoy" style="position:absolute;left:50.5%;top:1%;width:49%;height:48%;">
		<tiles:insert page="/calendarioBodega/diaHoy.jsp">
			<tiles:put name="vtformName" beanName="vformName"/>
			<tiles:put name="vtformAction" beanName="vformAction"/>
			<tiles:put name="vtformAction" beanName="vformAction"/>
			<tiles:put name="vtDia" value="diaActual"/>
		</tiles:insert>
	</div>
	<div id="div_desplazarDia" style="position:absolute;left:50.5%;top:49.3%;width:49%;height:2%;">
		<a href="#" style="height:100%;width:100%;text-decoration: none;" title="Desplazar D&iacute;a Hoy" onclick="desplazarSeccionDia();"><img id="img_desplazarDiaH" src="images/flechaAmarillaAba.gif" border="0"></a>	
		<a href="#" style="height:100%;width:100%;text-decoration: none;" title="Desplazar D&iacute;a Seleccionado" onclick="desplazarSeccionDiaSel();"><img id="img_desplazarDiaS" src="images/flechaAzulArr.gif" border="0"></a>	
	</div>
	<div id="div_diaPosHoy" class="seccionDiaPosHoy" style="position:absolute;left:50.5%;top:51%;width:49%;height:48%;">
		<tiles:insert page="/calendarioBodega/diaHoy.jsp">
			<tiles:put name="vtformName" beanName="vformName"/>
			<tiles:put name="vtformAction" beanName="vformAction"/>
			<tiles:put name="vtformAction" beanName="vformAction"/>
			<tiles:put name="vtDia" value="diaSeleccionado"/>
		</tiles:insert>
		<script type="text/javascript">
			ajusteInicial(true);
		</script>
	</div>
</div>
</logic:notEmpty>
<logic:empty name="ec.com.smx.sic.sispe.usuarioAccesoCalendario">
	<table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="100%" height="10%">
		<tr>
			<td width="100%" height="18px"></td>
		</tr>
		<tr>
			<td width="100%" height="53px" class="tabla_informacion amarillo1" align="center">
				<b>NO TIENE LOS PERMISOS NECESARIOS PARA ACCEDER A ESTA OPCIÓN</b>
			</td>
		</tr>
	</table>
</logic:empty>
