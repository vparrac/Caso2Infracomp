package uniandes.isis2203.cliente;
import javax.crypto.SecretKey;
/**
 * Interfaz que representa a un cifrador simetrico
 * Esta interfaz protege los métodos de modificaciones no autorizadas
 * @author Valerie Parra Cortés
 * @author Pablo Andrés Suarez Murillo
 */
public interface ICifradorSimetrico {
	/**
	 * Cifra la cadena de String 
	 * @param texto que se cifrara
	 * @return cadena de bytes del texto cifrado
	 * @throws Exception si el algoritmo introducido en la construcción de la clase no es válido
	 */
	public byte[] cifrar(String texto) throws Exception;
	/**
	 * Cifra un arreglo de bytes 
	 * @param bytes que se cifraran
	 * @return cadena de bytes del texto cifrado
	 * @throws Exception si el algoritmo introducido en la construcción de la clase no es válido
	 */
	public byte[] cifrar(byte[] texto) throws Exception;
	/**
	 * Descifra una cadena de String 
	 * @param texto que se descifrara
	 * @return cadena de bytes del texto descifrado
	 * @throws Exception si el algoritmo introducido en la construcción de la clase no es válido
	 */
	public byte[] descifrar(String texto) throws Exception;
	/**
	 * Método para modificar la llave con un arreglo de bytes dado por parametro
	 * @param Arreglo de bytes para inicializar la llave 
	 */
	public void setKey(byte[] llave);
	/**
	 * Método para obtener la llave que se esta usando para cifrar
	 * @return Llave que se esta usando para cifrar
	 */
	public SecretKey getKey();
}
