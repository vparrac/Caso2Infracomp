package uniandes.isis2203.cliente;
import javax.crypto.SecretKey;
/**
 * Interfaz que representa a un cifrador simetrico
 * Esta interfaz protege los m�todos de modificaciones no autorizadas
 * @author Valerie Parra Cort�s
 * @author Pablo Andr�s Suarez Murillo
 */
public interface ICifradorSimetrico {
	/**
	 * Cifra la cadena de String 
	 * @param texto que se cifrara
	 * @return cadena de bytes del texto cifrado
	 * @throws Exception si el algoritmo introducido en la construcci�n de la clase no es v�lido
	 */
	public byte[] cifrar(String texto) throws Exception;
	/**
	 * Cifra un arreglo de bytes 
	 * @param bytes que se cifraran
	 * @return cadena de bytes del texto cifrado
	 * @throws Exception si el algoritmo introducido en la construcci�n de la clase no es v�lido
	 */
	public byte[] cifrar(byte[] texto) throws Exception;
	/**
	 * Descifra una cadena de String 
	 * @param texto que se descifrara
	 * @return cadena de bytes del texto descifrado
	 * @throws Exception si el algoritmo introducido en la construcci�n de la clase no es v�lido
	 */
	public byte[] descifrar(String texto) throws Exception;
	/**
	 * M�todo para modificar la llave con un arreglo de bytes dado por parametro
	 * @param Arreglo de bytes para inicializar la llave 
	 */
	public void setKey(byte[] llave);
	/**
	 * M�todo para obtener la llave que se esta usando para cifrar
	 * @return Llave que se esta usando para cifrar
	 */
	public SecretKey getKey();
}
