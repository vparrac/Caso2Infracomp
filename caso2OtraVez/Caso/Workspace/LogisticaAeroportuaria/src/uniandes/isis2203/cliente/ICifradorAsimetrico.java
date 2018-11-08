package uniandes.isis2203.cliente;

import java.security.KeyPair;
import java.security.PublicKey;
/**
 * Interfaz que representa a un cifrado sim�trico
 * Esta interfaz protege los m�todos de modificaciones no autorizadas
 * @author Valerie Parra Cort�s
 * @author Pablo Andr�s Suarez Murillo
 */
public interface ICifradorAsimetrico {
	/**
	 * Cifra la cadena de String 
	 * @param texto que se cifrara
	 * @return cadena de bytes del texto cifrado
	 * @throws Exception si el algoritmo introducido en la construcci�n de la clase no es v�lido
	 */
	public byte[] cifrar(String texto) throws Exception;
	/**
	 * Descifra una cadena de String 
	 * @param texto que se descifrara
	 * @return cadena de bytes del texto descifrado
	 * @throws Exception si el algoritmo introducido en la construcci�n de la clase no es v�lido
	 */
	public byte[] descifrar(String texto) throws Exception;
	/**
	 * M�todo que retorna las llaves del cifradp
	 * @return KayPair con las llaves que est�n siendo utilizadas
	 */
	public KeyPair getKeys();
	/**
	 * Cifra un mensaje con la llave p�blica del servidor de forma asimetrica 
	 * @param texto con el mensaje a cifrar
	 * @param llaveServidor llave p�blica del servidor
	 * @return cadena de bytes
	 * @throws Exception si el algoritmo introducido en la construcci�n de la clase no es v�lido
	 */
	public byte[] cifrarParaElServidor(byte[] texto, PublicKey llaveServidor) throws Exception;
	/**
	 * Descifra un arreglo de bytes 
	 * @param bytes que se descifraran
	 * @return cadena de bytes descifrada
	 * @throws Exception si el algoritmo introducido en la construcci�n de la clase no es v�lido
	 */
	public byte[] descifrar(byte[] texto) throws Exception;
}
