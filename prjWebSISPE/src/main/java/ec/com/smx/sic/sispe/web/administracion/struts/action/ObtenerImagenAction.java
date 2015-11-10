/*
 * CargarImagenAction.java
 * Creado el 19/03/2008 9:03:41
 *   	
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.ArticuloDatoImagenDTO;


/**
 * @author nperalta, fmunoz
 *
 */
@SuppressWarnings("unchecked")
public class ObtenerImagenAction extends BaseAction
{
	//private final String FORMATO_IMAGEN = "jpeg";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		HttpSession session = request.getSession();
		//variable global que almacena los datos de la imagen(bytes).
		byte[] bfile=null;

		//obtiene im\u00E1genes para colecci\u00F3n de canastos
		if(request.getParameter("indiceImagenCanasto")!=null){
			LogSISPE.getLog().info("** entra a indiceImagen Canasto **");
			
			//se obtiene la colecci\u00F3n de canastos
			List<ArticuloDatoImagenDTO> articuloDatoImagenDTOcol =(List<ArticuloDatoImagenDTO>)session.getAttribute(CatalogoCanastosAction.COL_ARTICULOS);
			//obtiene el art\u00EDculo actual de la colecci\u00F3n
			ArticuloDatoImagenDTO articuloDatoImagenDTO = articuloDatoImagenDTOcol.get(Integer.parseInt(request.getParameter("indiceImagenCanasto")));
			//este campo contiene los datos de la imagen que le corresponde
			bfile = articuloDatoImagenDTO.getDatoImagen();
		}
		
		//obtiene imagen para el detalle del canasto seleccionado
		else if(session.getAttribute(CatalogoCanastosAction.ARTICULO_DTO)!=null){
			//obtiene de sesi\u00F3n el detalle del articulo seleccionado
			
			ArticuloDatoImagenDTO articuloDatoImagenDTO = (ArticuloDatoImagenDTO)session.getAttribute(CatalogoCanastosAction.ARTICULO_DTO);
			LogSISPE.getLog().info("ARTICULODTO: "+articuloDatoImagenDTO.getDescripcionArticulo());
			//asigna a bfile el dato de bytes de la imagen
			bfile=articuloDatoImagenDTO.getDatoImagen();
		}
	
		//devuelve la imagen
		if(bfile!=null){
			String pathCompleto = request.getRequestURL().toString();
			int indice = pathCompleto.lastIndexOf("/");
			if(indice > 0){
				pathCompleto = pathCompleto.substring(0, indice);
			}
			session.setAttribute("ec.com.smx.sic.sispe.pathAplicacion", pathCompleto);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			
			try{
				response.setContentType("image/jpeg");
				//response.setContentLength(bfile.length);
				LogSISPE.getLog().info("TAMA\u00F1O: " + bfile.length);
				servletOutputStream.write(bfile);
				
				//BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bfile));
				//ImageIO.write(bufferedImage, FORMATO_IMAGEN, servletOutputStream);
			}finally{
				if(servletOutputStream != null){
					servletOutputStream.flush();
					servletOutputStream.close();
				}
			}
		}

		return null;
	}
}
