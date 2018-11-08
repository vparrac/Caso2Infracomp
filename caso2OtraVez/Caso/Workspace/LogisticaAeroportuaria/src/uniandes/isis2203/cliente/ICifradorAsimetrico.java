package uniandes.isis2203.cliente;

import java.security.KeyPair;
import java.security.PublicKey;
/**
 * Interfaz que representa a un cifrado simétrico
 * Esta interfaz protege los métodos de modificaciones no autorizadas
 * @author Valerie Parra Cortés
 * @author Pablo Andrés Suarez Murillo
 */
public interface ICifradorAsimetrico {
	/**
	 * Cifra la cadena de String 
	 * @param texto que se cifrara
	 * @return cadena de bytes del texto cifrado
	 * @throws Exception si el algoritmo introducido en la construcción de la clase no es válido
	 */
	public byte[] cifrar(String texto) throws Exception;
	/**
	 * Descifra una cadena de String 
	 * @param texto que se descifrara
	 * @return cadena de bytes del texto descifrado
	 * @throws Exception si el algoritmo introducido en la construcción de la clase no es válido
	 */
	public byte[] descifrar(String texto) throws Exception;
	/**
	 * Método que retorna las llaves del cifradp
	 * @return KayPair con las llaves que están siendo utilizadas
	 */
	public KeyPair getKeys();
	/**
	 * Cifra un mensaje con la llave pública del servidor de forma asimetrica 
	 * @param texto con el mensaje a cifrar
	 * @param llaveServidor llave pública del servidor
	 * @return cadena de bytes
	 * @throws Exception si el algoritmo introducido en la construcción de la clase no es válido
	 */
	public byte[] cifrarParaElServidor(byte[] texto, PublicKey llaveServidor) throws Exception;
	/**
	 * Descifra un arreglo de bytes 
	 * @param bytes que se descifraran
	 * @return cadena de bytes descifrada
	 * @throws Exception si el algoritmo introducido en la construcción de la clase no es válido
	 */
	public byte[] descifrar(byte[] texto) throws Exception;
}
