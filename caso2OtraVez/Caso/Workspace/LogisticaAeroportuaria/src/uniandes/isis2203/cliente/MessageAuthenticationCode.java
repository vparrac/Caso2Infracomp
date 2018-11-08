package uniandes.isis2203.cliente;
import java.security.Key;
import javax.crypto.Mac;
/**
 * Clase para generar el MAC de una consulta
 * @author Valerie Parra Cortés
 * @author Pablo Andrés Suarez Murillo
 */
public class MessageAuthenticationCode {
	/**
	 * ALgoritmo que se utilizará para generar el MAC
	 */
	private String algoritmo;
	/**
	 * Método constructor de la clase
	 * Verifica que se soporten los algoritmo, sino envía Exception
	 * @param algoritmo a utilizar para generara el MAC
	 * @throws Exception si no se soporta el algoritmo introducido
	 */
	public MessageAuthenticationCode(String algoritmo) throws Exception {
		if(!(algoritmo.equalsIgnoreCase("HMACMD5")||algoritmo.equalsIgnoreCase("HMACSHA1")||algoritmo.equalsIgnoreCase("HMACSHA256"))){
			throw new Exception("El algoritmo ingresado no es soportado");
		}
		else {
			if(algoritmo.equals("HMACMD5")) {				
				setAlgoritmo("HMACMD5");		
				
			}
			else if (algoritmo.equals("HMACSHA1")) {
				setAlgoritmo("HMACSHA1");
			}
			else if (algoritmo.equals("HMACSHA256")) {
				setAlgoritmo("HMACSHA256");
			}
		}
	}	
	/**
	 * Retorna el hash del mensaje con la llave especificada 
	 * @param buffer arreglo de bytes del mensaje al cual se le quiere generar el MAC
	 * @param llave llave con la que se va a cifrar
	 * @return arreglo de bytes con el MAC del mensaje
	 */
	public  byte[] getDigest( byte[] buffer, Key llave ) {
		try {			
			    Mac mac = Mac.getInstance(algoritmo);
			    mac.init(llave);
			    return mac.doFinal(buffer);
					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	

	}
	/**
	 * Cambia el algoritmo por uno dado por parámetro
	 */
	 public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}
}
