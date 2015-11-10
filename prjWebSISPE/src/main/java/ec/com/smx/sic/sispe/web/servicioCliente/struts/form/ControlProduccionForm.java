/*
 * Clase ControlProduccionForm.java
 * Creado el 26/04/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaArticuloDTO;

/**
 * <p>
 * Contiene los campos y los demas controles del formulario de Control de Producci\u00F3n aqu\u00ED se realizan las 
 * validaciones de los datos ingresados por el usuario. Tambi\u00E9n se resetea el formulario cada vez que se realiza 
 * una petici\u00F3n.
 * </p>
 * @author 	fmunoz
 * @version	1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings({"serial","unchecked"})
public class ControlProduccionForm extends ListadoPedidosForm
{
	/**
	 * Controles principales para el formulario de Control de Producci\u00F3n:
	 * <ul>
	 * <li>String botonProducir: Permite enviar los pedidos reservados a producci\u00F3n</li>
	 * <li>String tagPedidos: Permite ver los pedidos que tienen una producci\u00F3n pendiente</li>
	 * <li>String tagDetArticulos: Permite ver los art\u00EDculos que est\u00E1n en producci\u00F3n</li>
	 * <li>String botonImprimir: Permite generar el informe de control de producci\u00F3n</li>
	 * <li>String [] opEscogerProducir: Arreglo de String que representan los checkbox en para la acci\u00F3n Control de Producci\u00F3n</li>
	 * </ul>
	 */
	private String botonProducir;
	private String tabPedidos;
	private String tabDetArticulos;
	private String botonImprimir;
	private String [] opEscogerProducir;

	/**
	 * Controles principales para el formulario de Actualizaci\u00F3n de Producci\u00F3n:
	 * <ul>
	 * <li>String botonActProduccion: Permite actualizar las cantidades producidas de cada art\u00EDculo</li>
	 * <li>String botonCanProduccion: Cancela la acci\u00F3n de actualizaci\u00F3n, volviendo al men\u00FA principal</li>
	 * <li>String tagProduccion: Muestra la lista de art\u00EDculos en producci\u00F3n, cuyas cantidades en producci\u00F3n
	 * 						   pueden ser actualizadas</li> 
	 * <li>String tagPedidosProd: Muetra la lista de pedidos que ser\u00E1n afectados en la actualizaci\u00F3n de producci\u00F3n</li>
	 * <li>String [] cantidadProducida: Campos donde se ingresa la cantidad a producir</li>
	 * <li>String [] opSeleccionPedidos: Checkboxs para selecionar los pedidos</li>
	 * <li> String opTipoAgrupacion: radioButtons para generar reportes</li>
	 * </ul>
	 */
	private String botonActProduccion;
	private String tabProduccion;
	private String tabPedidosProd;
	private String [] cantidadProducida;
	private String [] opSeleccionPedidos;
	private String opTipoAgrupacion;

	//OANDINO: Variables para control de intercambio de art\u00EDculos
	private String codigoArtIntercambio;
	private String [] observacionArticulo;
	
	/**
	 * Ejecuta la validacion en la p\u00E1gina JSP <code>controlProduccion.jsp</code>
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia	
	 * @param request		La petici\u00F3n que se est\u00E1 procesando
	 * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	 * <code>null</code>
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		//se crea una variable para almacenar la afirmaci\u00F3n
		request.setAttribute("ec.com.smx.sic.sispe.afirmacion",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

		//se obtiene el valor de variable ayuda
		String ayuda = request.getParameter(Globals.AYUDA);
		if (ayuda != null && !ayuda.equals("")) {
			if (ayuda.equals("reporteProduccion")) {
				this.botonImprimir = ayuda;
			} else if (ayuda.equals("ACT")) {
				this.botonActProduccion = ayuda;
			}
		}
		LogSISPE.getLog().info("ayuda: {}",ayuda);

		try
		{
			//se validan los campos del formulario padre
			errors = super.validate(mapping, request);
			/*-------------------- si se desea actualizar la producci\u00F3n -----------------------*/
			if(errors.isEmpty() && this.botonActProduccion!=null){
				
				boolean cantidadesCorrectas = true;
				//primero se verifica que las cantidades est\u00E9n correctamente ingresadas
				if(this.cantidadProducida!=null){
					int contadorCantidadesCorrectas = 0;
					for(int i=0; i<this.cantidadProducida.length; i++){
						try{
							Long cantidad = new Double(this.cantidadProducida[i]).longValue();
							LogSISPE.getLog().info("cantidad[{}]: {}",i, cantidad);
							if(cantidad.longValue() > 0){
								contadorCantidadesCorrectas ++;
							}
						}catch(NumberFormatException ex){
							this.cantidadProducida[i] = "0";
							//solo se captura la excepci\u00F3n
						}
					}
					LogSISPE.getLog().info("cantidadesCorrectas: {}" , cantidadesCorrectas);
					//se verifica el contador
					if(contadorCantidadesCorrectas == 0)
						cantidadesCorrectas = false;
					
				}

				//solo si las cantidades ingresadas son correctas
				if(cantidadesCorrectas){
					//se obtiene el detalle de la Producci\u00F3n 
					Collection articulosEnPro = super.getDatos();
					LogSISPE.getLog().info("ENTRO A LA VALIDACI\u00F3N: {}" , this.cantidadProducida.length);
					int indiceVector = 0;
					session.setAttribute("indiceVector1", new Integer(0));
					//IOnofre. Indice que se pasa para calcular la cantidad final
					session.setAttribute("indiceVector2", new Integer(0));
					int indiceVectorAux = 1;
					LogSISPE.getLog().info("indiceVector: {}",session.getAttribute("indiceVector1"));
					for(Iterator<VistaArticuloDTO> it = articulosEnPro.iterator(); it.hasNext(); ){
						indiceVector=((Integer)(session.getAttribute("indiceVector1"))).intValue();
						LogSISPE.getLog().info("indice: {}" , indiceVector);
						VistaArticuloDTO consultaVistaArticuloDTO = it.next();
						LogSISPE.getLog().info("codigo Articulo: {} - {}" , consultaVistaArticuloDTO.getId().getCodigoArticulo(), consultaVistaArticuloDTO.getDescripcionArticulo());
						try{
							long cantidadFinal=0;
							session.setAttribute("cantidad1", new Long(0));
							consultaVistaArticuloDTO.setNpErrorCantidadAProducir(null);
							//Si tiene articulos hijos
							if(consultaVistaArticuloDTO.getArticulos()!=null && consultaVistaArticuloDTO.getArticulos().size()>0){
								LogSISPE.getLog().info("tiene articulos hijos");
								session.setAttribute("indiceVector1", new Integer(indiceVector+1));
								//navega todos los hijos
								cantidadFinal = consultaRecursiva(consultaVistaArticuloDTO.getArticulos(),errors,session,consultaVistaArticuloDTO);
								LogSISPE.getLog().info("cantidad final: {}" , cantidadFinal);
								consultaVistaArticuloDTO.setNpCantidadParcialEstado(new Long(cantidadFinal));
								consultaVistaArticuloDTO.setNpCantidadParcialEstadoFija(consultaVistaArticuloDTO.getNpCantidadParcialEstado());
							}
							//Si tiene recetas que tengan articulos hijos
							else if(consultaVistaArticuloDTO.getNpDesplegarItemsReceta()!=null){
								try{
									LogSISPE.getLog().info("tiene recetas que tienen articulos hijos");
									//int indiceVista=indiceVector;//indice de la vistaArticuloDTO
									int indiceVista=indiceVectorAux - 1;
									//session.setAttribute("indiceVector1", new Integer(indiceVector+1));
									LogSISPE.getLog().info("indice Canasto: {}" , indiceVista);
									//session.setAttribute("indiceVector1", new Integer(indiceVector+1));
									session.setAttribute("indiceVector1", new Integer(indiceVector));
									LogSISPE.getLog().info("indice Vector***: {}" , session.getAttribute("indiceVector1"));
									//recorro todas las recetas
									List<ArticuloRelacionDTO> lista = (List<ArticuloRelacionDTO>) consultaVistaArticuloDTO.getRecetaArticulos();
									//lista.get(5);
									for(int i = 0; i<lista.size(); i++){
									//for (Iterator<ArticuloRelacionDTO> iter = consultaVistaArticuloDTO.getRecetaArticulos().iterator(); iter.hasNext();) {
										indiceVector=((Integer)(session.getAttribute("indiceVector1"))).intValue();
										//ArticuloRelacionDTO recetaArticuloDTO = iter.next();
										ArticuloRelacionDTO recetaArticuloDTO = lista.get(indiceVector);
										session.setAttribute("cantidad1", new Long(0));

										LogSISPE.getLog().info("Receta["+indiceVector+"]:"+recetaArticuloDTO.getArticuloRelacion().getId().getCodigoArticulo()+" - "+ recetaArticuloDTO.getArticuloRelacion().getDescripcionArticulo());
										
//										session.setAttribute("indiceVector1", new Integer((((Integer)(session.getAttribute("indiceVector1"))).intValue())+1));
										indiceVector++;
										session.setAttribute("indiceVector1", indiceVector);
										//si la receta tiene hijos
										if(recetaArticuloDTO.getArticuloRelacion()!=null && !CollectionUtils.isEmpty(recetaArticuloDTO.getNpArticulos()) && recetaArticuloDTO.getNpArticulos().size()>0){
//											int indiceVectorAux=((Integer)(session.getAttribute("indiceVector1"))).intValue();
											//int indiceVectorAux = indiceVector;
											//se sube el indiceVista para que tome el valor del canasto padre
											//session.setAttribute("indiceVector1", new Integer(indiceVista));
//											session.setAttribute("indiceVector1", new Integer(1));
											LogSISPE.getLog().info("numero hijos receta: {}" , recetaArticuloDTO.getNpArticulos().size());
											//LogSISPE.getLog().info("entro por la receta: {}" , indiceVectorAux);
											//navego por todos los articulos de cada receta
											LogSISPE.getLog().info("cantidadFinalInicial: {}" , cantidadFinal);
											session.setAttribute("indiceVector2", indiceVectorAux +1);
											//..session.setAttribute("indiceVector1", new Integer(indiceVector));
											cantidadFinal = consultaRecursiva(recetaArticuloDTO.getNpArticulos(),errors,session,consultaVistaArticuloDTO);
											//valido que la suma de las cantidades ingresadas en los articulos no sea mayor la ingresada en el canasto
											//LogSISPE.getLog().info("cantidadCanasto: {}" , this.cantidadProducida[indiceVista]);
											LogSISPE.getLog().info("numero de articulos: {}" , recetaArticuloDTO.getCantidad());
											LogSISPE.getLog().info("cantidad total articulos: {}" , cantidadFinal);
											recetaArticuloDTO.setNpErrorCantidadAProducir(null);
											if(cantidadFinal!=(recetaArticuloDTO.getCantidad().longValue()*(Long.parseLong(this.cantidadProducida[indiceVista]))) || cantidadFinal == 0){
												recetaArticuloDTO.setNpErrorCantidadAProducir("ok");
												consultaVistaArticuloDTO.setNpErrorCantidadAProducir("ok");
												errors.add("cantidadReceta",new ActionMessage("errors.cantidadHijosReceta",recetaArticuloDTO.getArticuloRelacion().getDescripcionArticulo(),consultaVistaArticuloDTO.getDescripcionArticulo(),(recetaArticuloDTO.getCantidad().longValue()*(Long.parseLong(this.cantidadProducida[indiceVista])))));
											}
											//se restablece el indice del vector
											session.setAttribute("indiceVector1", new Integer(indiceVector));
											indiceVectorAux = indiceVectorAux + recetaArticuloDTO.getNpArticulos().size();
										}
										indiceVectorAux = indiceVectorAux + 1;
									}
									session.setAttribute("indiceVector1", new Integer(0));
									indiceVectorAux = indiceVectorAux + 1;
									/*******************Valida la cantidad ingresada de la consultaVistaArticuloDTO***************/
									if(this.cantidadProducida[indiceVista]!=null && !this.cantidadProducida[indiceVista].equals("")){
										if(Long.parseLong(this.cantidadProducida[indiceVista])<0){
											errors.add("cantidadNegativa",new ActionMessage("errors.actualizarProduccion.long",consultaVistaArticuloDTO.getDescripcionArticulo()));
											//errors.add("cantidadNegativa",new ActionMessage("errors.actualizarProduccion.long"));
											consultaVistaArticuloDTO.setNpCantidadParcialEstado(new Long(0));
											consultaVistaArticuloDTO.setNpCantidadParcialEstadoFija(consultaVistaArticuloDTO.getNpCantidadParcialEstado());
										}else{
											consultaVistaArticuloDTO.setNpCantidadParcialEstado(new Long(this.cantidadProducida[indiceVista]));
											LogSISPE.getLog().info("cantidadParcialEstado {} del articulo {}",this.cantidadProducida[indiceVista], consultaVistaArticuloDTO.getId().getCodigoArticulo());
											consultaVistaArticuloDTO.setNpCantidadParcialEstadoFija(consultaVistaArticuloDTO.getNpCantidadParcialEstado());
											cantidadFinal=(new Long(this.cantidadProducida[indiceVista])).longValue();
										}
									}else{
										consultaVistaArticuloDTO.setNpCantidadParcialEstado(new Long(0));
										consultaVistaArticuloDTO.setNpCantidadParcialEstadoFija(consultaVistaArticuloDTO.getNpCantidadParcialEstado());
									}
									//se valida que no exeda la cantidad reservada
									long cantidadProducir = consultaVistaArticuloDTO.getCantidadParcialEstado().longValue() + cantidadFinal;
									if(cantidadProducir > consultaVistaArticuloDTO.getCantidadReservadaEstado().longValue()){
										long cantidadMaxima = consultaVistaArticuloDTO.getCantidadReservadaEstado().longValue() - consultaVistaArticuloDTO.getCantidadParcialEstado().longValue();
										consultaVistaArticuloDTO.setNpErrorCantidadAProducir("ok");
										//error en caso de que se supere la cantidad m\u00E1xima de art\u00EDculos a producir
										errors.add("CantidadExcedida",new ActionMessage("errors.cantidadAProducir.excedida",consultaVistaArticuloDTO.getDescripcionArticulo(),String.valueOf(cantidadMaxima)));
									}
								}catch(NumberFormatException e){
									LogSISPE.getLog().info("se produjo un error: {}" , e.getStackTrace());
									errors.add("cantidadFormato",new ActionMessage("errors.formatoCantidad"));
								}
							}else{
								LogSISPE.getLog().info("no tiene hijos");
								if(this.cantidadProducida[indiceVector]!=null && !this.cantidadProducida[indiceVector].equals("")){
									if(Long.parseLong(this.cantidadProducida[indiceVector])<0){
										errors.add("cantidadNegativa",new ActionMessage("errors.actualizarProduccion.long",consultaVistaArticuloDTO.getDescripcionArticulo()));
										//errors.add("cantidadNegativa",new ActionMessage("errors.actualizarProduccion.long"));
										consultaVistaArticuloDTO.setNpCantidadParcialEstado(new Long(0));
										consultaVistaArticuloDTO.setNpCantidadParcialEstadoFija(consultaVistaArticuloDTO.getNpCantidadParcialEstado());
									}else{
										consultaVistaArticuloDTO.setNpCantidadParcialEstado(new Long(this.cantidadProducida[indiceVector]));
										consultaVistaArticuloDTO.setNpCantidadParcialEstadoFija(consultaVistaArticuloDTO.getNpCantidadParcialEstado());
										cantidadFinal=(new Long(this.cantidadProducida[indiceVector])).longValue();
									}
								}else{
									consultaVistaArticuloDTO.setNpCantidadParcialEstado(new Long(0));
									consultaVistaArticuloDTO.setNpCantidadParcialEstadoFija(consultaVistaArticuloDTO.getNpCantidadParcialEstado());
								}
								session.setAttribute("indiceVector1", new Integer(indiceVector+1));	
							}
							//se valida que no exeda la cantidad reservada
							long cantidadProducir = consultaVistaArticuloDTO.getCantidadParcialEstado().longValue() + cantidadFinal;
							if(cantidadProducir > consultaVistaArticuloDTO.getCantidadReservadaEstado().longValue()){
								long cantidadMaxima = consultaVistaArticuloDTO.getCantidadReservadaEstado().longValue() - consultaVistaArticuloDTO.getCantidadParcialEstado().longValue();
								consultaVistaArticuloDTO.setNpErrorCantidadAProducir("ok");
								//error en caso de que se supere la cantidad m\u00E1xima de art\u00EDculos a producir
								errors.add("CantidadExcedida",new ActionMessage("errors.cantidadAProducir.excedida",consultaVistaArticuloDTO.getDescripcionArticulo(),String.valueOf(cantidadMaxima)));
							}

						}catch(NumberFormatException e){
							//se valida que sea un campo requerido y de tipo long
							consultaVistaArticuloDTO.setNpCantidadParcialEstado(new Long(0));
							consultaVistaArticuloDTO.setNpCantidadParcialEstadoFija(consultaVistaArticuloDTO.getNpCantidadParcialEstado());
						}

					}
					session.removeAttribute("indiceVector1");
					session.removeAttribute("cantidad1");
					if(errors.size()>0){
						request.setAttribute("errores", "ok");
					}
				}else
					errors.add("cantidades", new ActionMessage("errors.actualizarProduccion.cantidadesIngresadas"));
			}
		}
		catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}
	/**
	 * Funci\u00F3n recursiva para obtener las cantidades de cada vistaArticuloDTO
	 */
	public long consultaRecursiva(Collection articulos,ActionErrors errors,HttpSession session,VistaArticuloDTO consultaVistaArticuloDTO){
		long cantidad=0;
		int indiceVector=((Integer)(session.getAttribute("indiceVector2"))).intValue();
		//indiceVector++;
		for(Iterator it = articulos.iterator(); it.hasNext(); ){
			
			LogSISPE.getLog().info("indiceR: {}" , indiceVector);
			//LogSISPE.getLog().info("cantidadIngresada[" + indiceVector + "]: " + this.cantidadProducida[indiceVector]);
			ArticuloDTO consultaArticuloDTO = (ArticuloDTO)it.next();
			/*if(consultaArticuloDTO.getArticulos()!=null && consultaArticuloDTO.getArticulos().size()>0){
				LogSISPE.getLog().info("tiene hijos");
				consultaRecursiva(consultaArticuloDTO.getArticulos(),articuloDTOCol,errors,session,consultaVistaArticuloDTO);
			}
			else{*/
				if(this.cantidadProducida[indiceVector]!=null && !this.cantidadProducida[indiceVector].equals("")){
					if(Long.parseLong(this.cantidadProducida[indiceVector])<0){
						errors.add("cantidadNegativa",new ActionMessage("errors.actualizarProduccion.long",consultaVistaArticuloDTO.getDescripcionArticulo()));
						consultaArticuloDTO.setNpCantidadAProducir(new Long(0));
						consultaArticuloDTO.setNpCantidadAProducirFija(consultaArticuloDTO.getNpCantidadAProducir());
					}else{
					consultaArticuloDTO.setNpCantidadAProducir(new Long(this.cantidadProducida[indiceVector]));
					consultaArticuloDTO.setNpCantidadAProducirFija(consultaArticuloDTO.getNpCantidadAProducir());
						if((new Long(this.cantidadProducida[indiceVector])).longValue()>0){
							cantidad=((Long)(session.getAttribute("cantidad1"))).longValue()+(new Long(this.cantidadProducida[indiceVector])).longValue();
							LogSISPE.getLog().info("cantidad[{}]: {}" ,indiceVector, this.cantidadProducida[indiceVector]);
							LogSISPE.getLog().info("cantidad: {}" , cantidad);
							session.setAttribute("cantidad1",new Long(cantidad));
						}
					}
				}else{
					consultaArticuloDTO.setNpCantidadAProducir(new Long(0));
					consultaArticuloDTO.setNpCantidadAProducirFija(consultaArticuloDTO.getNpCantidadAProducir());
				}
					
			//}
				indiceVector++;
		}
		session.setAttribute("indiceVector1", new Integer(indiceVector));
		return(cantidad);
	}

	/**
	 * Resetea los controles del formulario de la p\u00E1gina <code>controlProduccion.jsp</code>, en cada petici\u00F3n.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n que se est\u00E1 procesando
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		//llamada al m\u00E9todo de la clase padre que resetea
		super.reset(mapping, request);

		this.tabPedidos=null;
		this.tabDetArticulos=null;
		this.botonProducir=null;
		this.opSeleccionPedidos=null;
		this.opEscogerProducir=null;
		this.botonImprimir=null;
		this.botonActProduccion=null;
		this.tabPedidosProd=null;
		this.tabProduccion=null;
		this.cantidadProducida=null;
	}

	/**
	 * @return Devuelve botonProducir.
	 */
	public String getBotonProducir()
	{
		return botonProducir;
	}
	/**
	 * @param botonProducir El botonProducir a establecer.
	 */
	public void setBotonProducir(String botonProducir)
	{
		this.botonProducir = botonProducir;
	}

	/**
	 * @return Devuelve tabDetArticulos.
	 */
	public String getTabDetArticulos()
	{
		return tabDetArticulos;
	}
	/**
	 * @param tabDetArticulos El tabDetArticulos a establecer.
	 */
	public void setTabDetArticulos(String tabDetArticulos)
	{
		this.tabDetArticulos = tabDetArticulos;
	}
	/**
	 * @return Devuelve tabPedidos.
	 */
	public String getTabPedidos()
	{
		return tabPedidos;
	}
	/**
	 * @param tabPedidos El tabPedidos a establecer.
	 */
	public void setTabPedidos(String tabPedidos)
	{
		this.tabPedidos = tabPedidos;
	}
	/**
	 * @return Devuelve cantidadProducida.
	 */
	public String[] getCantidadProducida()
	{
		return cantidadProducida;
	}
	/**
	 * @param cantidadProducida El cantidadProducida a establecer.
	 */
	public void setCantidadProducida(String[] cantidadProducida)
	{
		this.cantidadProducida = cantidadProducida;
	}

	/**
	 * @return Devuelve opEscogerProducir.
	 */
	public String[] getOpEscogerProducir()
	{
		return opEscogerProducir;
	}
	/**
	 * @param opEscogerProducir El opEscogerProducir a establecer.
	 */
	public void setOpEscogerProducir(String[] opEscogerProducir)
	{
		this.opEscogerProducir = opEscogerProducir;
	}
	/**
	 * @return Devuelve botonActProduccion.
	 */
	public String getBotonActProduccion()
	{
		return botonActProduccion;
	}
	/**
	 * @param botonActProduccion El botonActProduccion a establecer.
	 */
	public void setBotonActProduccion(String botonActProduccion)
	{
		this.botonActProduccion = botonActProduccion;
	}

	/**
	 * @return Devuelve opSeleccionPedidos.
	 */
	public String[] getOpSeleccionPedidos()
	{
		return opSeleccionPedidos;
	}
	/**
	 * @param opSeleccionPedidos El opSeleccionPedidos a establecer.
	 */
	public void setOpSeleccionPedidos(String[] opSeleccionPedidos)
	{
		this.opSeleccionPedidos = opSeleccionPedidos;
	}

	/**
	 * @return Devuelve tabPedidosProd.
	 */
	public String getTabPedidosProd()
	{
		return tabPedidosProd;
	}
	/**
	 * @param tabPedidosProd El tabPedidosProd a establecer.
	 */
	public void setTabPedidosProd(String tabPedidosProd)
	{
		this.tabPedidosProd = tabPedidosProd;
	}
	/**
	 * @return Devuelve tabProduccion.
	 */
	public String getTabProduccion()
	{
		return tabProduccion;
	}
	/**
	 * @param tabProduccion El tabProduccion a establecer.
	 */
	public void setTabProduccion(String tabProduccion)
	{
		this.tabProduccion = tabProduccion;
	}
	/**
	 * @return Devuelve botonImprimir.
	 */
	public String getBotonImprimir() {

		return botonImprimir;
	}
	/**
	 * @param botonImprimir El botonImprimir a establecer.
	 */
	public void setBotonImprimir(String botonImprimir) {

		this.botonImprimir = botonImprimir;
	}
	public String getOpTipoAgrupacion() {
		return opTipoAgrupacion;
	}
	public void setOpTipoAgrupacion(String opTipoAgrupacion) {
		this.opTipoAgrupacion = opTipoAgrupacion;
	}
	public String getCodigoArtIntercambio() {
		return codigoArtIntercambio;
	}
	public void setCodigoArtIntercambio(String codigoArtIntercambio) {
		this.codigoArtIntercambio = codigoArtIntercambio;
	}
	public String[] getObservacionArticulo() {
		return observacionArticulo;
	}
	public void setObservacionArticulo(String[] observacionArticulo) {
		this.observacionArticulo = observacionArticulo;
	}
	
	
}
