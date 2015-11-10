<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<tiles:useAttribute id="vformName" name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vDia" name="vtDia"  classname="java.lang.String" ignore="true"/>

<script type="text/javascript">
	jQuery().ready(function(){
		$j('#tabs_pro_${vDia}_canasta').tabs();
		$j('#tabs_pro_${vDia}_despensa').tabs();
	});
</script>

<logic:equal name="vDia" value="diaActual">
	<logic:notEmpty name="ec.com.smx.sic.sispe.fechaActualNombre">
		<bean:define id="fechaSeccion" name="ec.com.smx.sic.sispe.fechaActualNombre"/>
	</logic:notEmpty>
</logic:equal>
<logic:notEqual name="vDia" value="diaActual">
	<logic:notEmpty name="ec.com.smx.sic.sispe.fechaSeleccionadaNombre">
		<bean:define id="fechaSeccion" name="ec.com.smx.sic.sispe.fechaSeleccionadaNombre"/>
	</logic:notEmpty>
</logic:notEqual>

<div style="position:relative;height:100%;width:100%;">
	<div id="div_desDom_${vDia}" style="position:absolute;left:1%;top:1%;width:98%;height:32%;z-index:80;" esMax="false">
		<div style="position:absolute;height:15px;width:100%;" class="contenedorTitulo">
			<a href="#" onclick="maximizarContenedor('div_desDom_${vDia}','div_pro_${vDia}','div_desLoc_${vDia}','1%','98%',85,'1%','32%',80);">&nbsp;ENTREGA A DOMICILIO&nbsp;<bean:write name="fechaSeccion" format="dd-MM-yyyy"/></a>
		</div>
		<div id="div_desDom_${vDia}_datos" style="position:absolute;overflow-x:auto;overflow-y:auto;top:17px;height:100%;width:100%;" class="contenedorCuerpo cont1N">
			<tiles:insert page="/calendarioBodega/entregaArticulosPedido.jsp">
				<tiles:put name="vtformName" beanName="vformName"/>
				<tiles:put name="vtformAction" beanName="vformAction"/>
				<tiles:put name="vtDia" beanName="vDia"/>
				<tiles:put name="vtTipo" value="canasta"/>
			</tiles:insert>
			
			<%--div style="position:absolute;left:1%;top:4%;width:48.5%;height:92%;">
				<div style="position:absolute;height:15px;width:100%;" class="contenedorTitulo">
					&nbsp;CANASTOS
				</div>
				<div style="position:absolute;overflow-x:auto;overflow-y:auto;top:17px;height:100%;width:100%;" class="contenedorCuerpo cont2N">
					<tiles:insert page="/calendarioBodega/entregaArticulosPedido.jsp">
						<tiles:put name="vtformName" beanName="vformName"/>
						<tiles:put name="vtformAction" beanName="vformAction"/>
						<tiles:put name="vtDia" beanName="vDia"/>
						<tiles:put name="vtTipo" value="canasta"/>
					</tiles:insert>
				</div>
			</div>
			<div style="position:absolute;left:50.5%;top:4%;width:48.5%;height:92%;">
				<div style="position:absolute;height:15px;width:100%;" class="contenedorTitulo">
					&nbsp;DESPENSAS
				</div>
				<div style="position:absolute;overflow-x:auto;overflow-y:auto;top:17px;height:100%;width:100%;" class="contenedorCuerpo cont2N">
					<tiles:insert page="/calendarioBodega/entregaArticulosPedido.jsp">
						<tiles:put name="vtformName" beanName="vformName"/>
						<tiles:put name="vtformAction" beanName="vformAction"/>
						<tiles:put name="vtDia" beanName="vDia"/>
						<tiles:put name="vtTipo" value="despensa"/>
					</tiles:insert>
				</div>
			</div--%>
		</div>
	</div>
	<div id="div_desLoc_${vDia}" style="position:absolute;left:1%;top:34%;width:98%;height:32%;z-index:80;" esMax="false">
		<div style="position:absolute;height:15px;width:100%;" class="contenedorTitulo">
			<a href="#" onclick="maximizarContenedor('div_desLoc_${vDia}','div_pro_${vDia}','div_desDom_${vDia}','1%','98%',85,'34%','32%',80);">&nbsp;DESPACHO A LOCALES&nbsp;<bean:write name="fechaSeccion" format="dd-MM-yyyy"/></a>
		</div>
		<div id="div_desLoc_${vDia}_datos" style="position:absolute;overflow-x:auto;overflow-y:auto;top:17px;height:100%;width:100%;" class="contenedorCuerpo cont1N">
			<tiles:insert page="/calendarioBodega/despachoPedidosArticulos.jsp">
				<tiles:put name="vtformName" beanName="vformName"/>
				<tiles:put name="vtformAction" beanName="vformAction"/>
				<tiles:put name="vtDia" beanName="vDia"/>
				<tiles:put name="vtTipo" value="canasta"/>
			</tiles:insert>
					
			<%--div style="position:absolute;left:1%;top:4%;width:48.5%;height:92%;">
				<div style="position:absolute;height:15px;width:100%;" class="contenedorTitulo">
					&nbsp;CANASTAS
				</div>
				<div style="position:absolute;overflow-x:auto;overflow-y:auto;top:17px;height:100%;width:100%;" class="contenedorCuerpo cont2N">
					<tiles:insert page="/calendarioBodega/despachoPedidosArticulos.jsp">
						<tiles:put name="vtformName" beanName="vformName"/>
						<tiles:put name="vtformAction" beanName="vformAction"/>
						<tiles:put name="vtDia" beanName="vDia"/>
						<tiles:put name="vtTipo" value="canasta"/>
					</tiles:insert>
				</div>
			</div>
			<div style="position:absolute;left:50.5%;top:4%;width:48.5%;height:92%;">
				<div style="position:absolute;height:15px;width:100%;" class="contenedorTitulo">
					&nbsp;DESPENSAS
				</div>
				<div style="position:absolute;overflow-x:auto;overflow-y:auto;top:17px;height:100%;width:100%;" class="contenedorCuerpo cont2N">
					<tiles:insert page="/calendarioBodega/despachoPedidosArticulos.jsp">
						<tiles:put name="vtformName" beanName="vformName"/>
						<tiles:put name="vtformAction" beanName="vformAction"/>
						<tiles:put name="vtDia" beanName="vDia"/>
						<tiles:put name="vtTipo" value="despensa"/>
					</tiles:insert>
				</div>
			</div--%>
		</div>
	</div>
	<div id="div_pro_${vDia}" style="position:absolute;left:1%;top:67%;width:98%;height:32%;z-index:80;" esMax="false">
		<div style="position:absolute;height:15px;width:100%;" class="contenedorTitulo">
			<a href="#" onclick="maximizarContenedor('div_pro_${vDia}','div_desLoc_${vDia}','div_desDom_${vDia}','1%','98%',85,'67%','32%',80);">&nbsp;PRODUCCION&nbsp;<bean:write name="fechaSeccion" format="dd-MM-yyyy"/></a>
		</div>
		<div style="position:absolute;top:17px;height:100%;width:100%;" class="contenedorCuerpo cont1N">
			<div style="position:absolute;left:1%;top:4%;width:48.5%;height:92%;">
				<%--div style="position:absolute;height:15px;width:100%;" class="contenedorTitulo">
					&nbsp;CANASTAS ESPECIALES
				</div--%>
				<div style="position:absolute;overflow-x:auto;overflow-y:auto;top:0px;height:100%;width:100%;border-top-style:solid;border-top-width:1px;border-top-color:#A0A0A0;" class="contenedorCuerpo">
					<div style="position:absolute;top:1%;left:1%;width:98%;height:98%;">
						<div id="tabs_pro_${vDia}_canasta" style="position:absolute;">
							<ul style="height:15px">
								<li style="height:15px"><a href="#tabs_pro_${vDia}_canasta1"><label class="textoNegroB"><B>CAN. CATALOGO</B></label></a></li>
								<li style="height:15px"><a href="#tabs_pro_${vDia}_canasta2"><label class="textoNegroB"><B>CAN. ESPECIAL</B></label></a></li>
								<li style="height:15px"><a href="#tabs_pro_${vDia}_canasta3"><label class="textoNegroB"><B>CAN. LINEA</B></label></a></li>
							</ul>
							<div id="tabs_pro_${vDia}_canasta1">
								<tiles:insert page="/calendarioBodega/produccionArticulos.jsp">
									<tiles:put name="vtformName" beanName="vformName"/>
									<tiles:put name="vtformAction" beanName="vformAction"/>
									<tiles:put name="vtDia" beanName="vDia"/>
									<tiles:put name="vtTipo" value="canasta"/>
									<tiles:put name="vtClasificacion" value="catalogo"/>
								</tiles:insert>
							</div>
							<div id="tabs_pro_${vDia}_canasta2">
								<tiles:insert page="/calendarioBodega/produccionArticulos.jsp">
									<tiles:put name="vtformName" beanName="vformName"/>
									<tiles:put name="vtformAction" beanName="vformAction"/>
									<tiles:put name="vtDia" beanName="vDia"/>
									<tiles:put name="vtTipo" value="canasta"/>
									<tiles:put name="vtClasificacion" value="especial"/>
								</tiles:insert>
							</div>
							<div id="tabs_pro_${vDia}_canasta3">
								<tiles:insert page="/calendarioBodega/lineaProduccion.jsp">
									<tiles:put name="vtformName" beanName="vformName"/>
									<tiles:put name="vtformAction" beanName="vformAction"/>
									<tiles:put name="vtDia" beanName="vDia"/>
									<tiles:put name="vtTipo" value="canasta"/>
									<tiles:put name="vtClasificacion" value="linea"/>
								</tiles:insert>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div style="position:absolute;left:50.5%;top:4%;width:48.5%;height:92%;">
				<%--div style="position:absolute;height:15px;width:100%;" class="contenedorTitulo">
					&nbsp;DESPENSAS ESPECIALES
				</div--%>
				<div style="position:absolute;overflow-x:auto;overflow-y:auto;top:0px;height:100%;width:100%;border-top-style:solid;border-top-width:1px;border-top-color:#A0A0A0;" class="contenedorCuerpo">
					<div style="position:absolute;top:1%;left:1%;width:98%;height:98%;">
						<div id="tabs_pro_${vDia}_despensa" style="position:absolute;">
								<ul style="height:15px">
									<li style="height:15px"><a href="#tabs_pro_${vDia}_despensa1"><label class="textoNegroB"><B>DES. CATALOGO</B></label></a></li>
									<li style="height:15px"><a href="#tabs_pro_${vDia}_despensa2"><label class="textoNegroB"><B>DES. ESPECIAL</B></label></a></li>
									<li style="height:15px"><a href="#tabs_pro_${vDia}_despensa3"><label class="textoNegroB"><B>DES. LINEA</B></label></a></li>
								</ul>
							<div id="tabs_pro_${vDia}_despensa1">
								<tiles:insert page="/calendarioBodega/produccionArticulos.jsp">
									<tiles:put name="vtformName" beanName="vformName"/>
									<tiles:put name="vtformAction" beanName="vformAction"/>
									<tiles:put name="vtDia" beanName="vDia"/>
									<tiles:put name="vtTipo" value="despensa"/>
									<tiles:put name="vtClasificacion" value="catalogo"/>
								</tiles:insert>
							</div>
							<div id="tabs_pro_${vDia}_despensa2">
								<tiles:insert page="/calendarioBodega/produccionArticulos.jsp">
									<tiles:put name="vtformName" beanName="vformName"/>
									<tiles:put name="vtformAction" beanName="vformAction"/>
									<tiles:put name="vtDia" beanName="vDia"/>
									<tiles:put name="vtTipo" value="despensa"/>
									<tiles:put name="vtClasificacion" value="especial"/>
								</tiles:insert>
							</div>
							<div id="tabs_pro_${vDia}_despensa3">
								<tiles:insert page="/calendarioBodega/lineaProduccion.jsp">
									<tiles:put name="vtformName" beanName="vformName"/>
									<tiles:put name="vtformAction" beanName="vformAction"/>
									<tiles:put name="vtDia" beanName="vDia"/>
									<tiles:put name="vtTipo" value="despensa"/>
									<tiles:put name="vtClasificacion" value="linea"/>
								</tiles:insert>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>