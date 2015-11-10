package ec.com.smx.sic.sispe.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.com.smx.framework.factory.FrameworkFactory;
import ec.com.smx.framework.multicompany.dto.SystemDto;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.sic.sispe.common.constantes.ConstantesGlobales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.VistaEntidadResponsableDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

//Clase para hacer una redirecci\u00F3n desde STRUTS hacia JSF
public class RedireccionarJSF {
	private static final RedireccionarJSF INSTANCIA = new RedireccionarJSF();
	
	public static final String URL_REDIRECT_SISPEV2 = "system.redireccionarsispev2";
		
	public static RedireccionarJSF  obtenerInstancia(){
		return INSTANCIA;
	} 
	public String formarUrlRetorno(String urlRedireccion, HttpServletRequest request, HttpSession session){
		try{
			
			VistaEntidadResponsableDTO vistaEntidadResponsableDTO = SessionManagerSISPE.getCurrentEntidadResponsable(request);
			if(vistaEntidadResponsableDTO.getCodigoLocal() != null){
				//codigo del local
				Integer codigoLocal = vistaEntidadResponsableDTO.getCodigoLocal();
				//codigo de la compania
				Integer codigoCompania = SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania();
				//usuario loggeado
				UserDto usuario = SessionManagerSISPE.getDefault().getLoggedUser(request);
				//sistema origen SISPE
				String idSistemaOrigen = MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID");
				SystemDto sistemaOrigen = FrameworkFactory.getMultiCompanyService().getSystemById(idSistemaOrigen);
				//token para la conexion
				String token = FrameworkFactory.getSecurityService().getTokenForUser(usuario.getUserId(), codigoCompania, sistemaOrigen.getSystemId());
				//Obtener la url de retorno
				String url=obtenerURLRetorno(request, session);
				//Parametros
				//rol del responsable VistaEntidadResponsableDTO
				String idRolEntidadResponsable= SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdRol();
				Integer codigoLocalEntidadResponsable=SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal();
				String codigoDivGeoPolEntidadResponsable=SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoDivGeoPol();
				String idUsuario=SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
				//estado activo
				String estadoActivo=SessionManagerSISPE.getEstadoActivo(request);
				//Area de Trabajo
				String areaTrabajo =SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo();
				//codigoTipoDescuentoPagoEfectivo
				
				String codigoTipoDescuentoPagoEfectivo=(String)request.getSession().getAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_PAGOEFECTIVO);
								
				//ACCION ACTUAL
				String accionActual = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
				//npCODIGO LOCAL
				Integer npCodigoLocal=SessionManagerSISPE.getCurrentLocal(request);
				
				String secuencialEstadoPedido=null;	
				if(request.getSession().getAttribute("ec.com.smx.sic.sispe.vistaPedidoDTO")!= null){
					VistaPedidoDTO vistaPedidoDTO=	(VistaPedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.vistaPedidoDTO");
					secuencialEstadoPedido=vistaPedidoDTO.getId().getSecuencialEstadoPedido();
				}
//				if(request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO)!= null){
//					//SECUENCIAL DEL ESTADO DEL PEDIDO
//					System.out.println("---------"+((VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO)).getId().getSecuencialEstadoPedido());
					
				//((VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO)).getId().getSecuencialEstadoPedido();			
//					}
				
				StringBuilder urlRetorno = new StringBuilder();
				urlRetorno.append(urlRedireccion)
				.append("&").append(ConstantesGlobales.PARAMETRO_TOKEN).append("=").append(token)
				.append("&").append(ConstantesGlobales.PARAMETRO_URL_RETORNO).append("=").append(url)
				.append("&").append(ConstantesGlobales.ID_ROL_ENTIDAD_RESPONSABLE).append("=").append(idRolEntidadResponsable)
				.append("&").append(ConstantesGlobales.CODIGO_LOCAL_ENTIDAD_RESPONSABLE).append("=").append(codigoLocalEntidadResponsable)
				.append("&").append(ConstantesGlobales.CODIGO_DIV_GEO_POL_ENTIDAD_RESPONSABLE).append("=").append(codigoDivGeoPolEntidadResponsable)
				.append("&").append(ConstantesGlobales.ID_USUARIO_ENTIDAD_RESPONSABLE).append("=").append(idUsuario)
				.append("&").append(ConstantesGlobales.ESTADO_ACTIVO).append("=").append(estadoActivo)
				.append("&").append(ConstantesGlobales.AREA_TRABAJO).append("=").append(areaTrabajo)
				.append("&").append(ConstantesGlobales.CODIGO_TIPO_DESCUENTO_PAGO_EFECTIVO).append("=").append(codigoTipoDescuentoPagoEfectivo)
				.append("&").append(ConstantesGlobales.SECUENCIAL_ESTADO_PEDIDO).append("=").append(secuencialEstadoPedido)
				.append("&").append(ConstantesGlobales.ACCION_ACTULA).append("=").append(accionActual)
				.append("&").append(ConstantesGlobales.NP_CODIGO_LOCAL).append("=").append(npCodigoLocal);

				if (session.getAttribute("sm.sic.currentProveedor.Administrador")!=null)
				{
					urlRetorno.append("&").append("esSuperUsr").append("=").append(Boolean.TRUE);
				}else{
					urlRetorno.append("&").append("esSuperUsr").append("=").append(Boolean.FALSE);
				}
				LogSISPE.getLog().info(":.url reg san .: " + urlRetorno.toString());
				session.setAttribute(URL_REDIRECT_SISPEV2, urlRetorno.toString());
				
			}
			
		}catch(Exception e){
			LogSISPE.getLog().error("-- Exception: ",e);
		}
		return null;
	}
	
	/**
	 * Metodo para obtener el url de retorno
	 * @param request
	 * @param session
	 * @return String
	 * @throws Exception
	 */
	private String obtenerURLRetorno(HttpServletRequest request, HttpSession session) throws  Exception {
        String urlorigen = request.getRequestURL().toString();
        LogSISPE.getLog().info("getRequestURL: " + urlorigen);
        String urlRetorno=null;
        String urlRetornoCompleta = (urlorigen.substring(0, urlorigen.lastIndexOf("/")));
        urlRetorno = urlRetornoCompleta + MessagesWebSISPE.getString("conexion.accion.retornar");
        LogSISPE.getLog().info("UrlRetornoContexto: " + urlRetorno);
        return urlRetorno;
	}
	

	}
