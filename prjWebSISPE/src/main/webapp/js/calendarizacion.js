/**********************************
    FUNCION PARA CAMBIO DE ESTILOS
***********************************/

function cambiarEstilo (myObj, color, grosor) {
	var theObj;
	theObj = document.getElementById(myObj);
	if (theObj != null) {
		theObj.style.fontWeight = grosor;
		theObj.style.backgroundColor = color;
	}
}


