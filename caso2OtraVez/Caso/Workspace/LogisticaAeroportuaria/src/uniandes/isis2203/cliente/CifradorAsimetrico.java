package uniandes.isis2203.cliente;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import javax.crypto.Cipher;
/**
 * Esta clase será la encargada de cifrar o descifrar con cifrado asimetrico
 * El único cifrado soportado es RSA
 * @author Valerie Parra Cortés
 * @author Pablo Andrés Suarez Murillo
  */
public class CifradorAsimetrico implements ICifradorAsimetrico{
	/**
	 * El algoritmo para utilizar soportado solo es RSA
	 */
	private final static String ALGORITMO = "RSA";
	/**
	 * Atributo para representar el par de llaves
	 */
	private KeyPair keyPair;
	
	/**
	 * El cifrador de la clase
	 */
	private Cipher cipher;
	/**
	 * Método que construye un nuevo cifrador simético
	 * @param algortimo que se utilizará
	 * @throws Exception Si el algoritmo introducido no es válido
	 */
	public CifradorAsimetrico(String algortimo) throws Exception {
		if(!(algortimo.equalsIgnoreCase(ALGORITMO))){
			throw new Exception("El algoritmo introducido no es soportado");
		}		
		KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITMO);
		generator.initialize(1024);
		keyPair = generator.generateKeyPair();
		cipher= Cipher.getInstance(ALGORITMO);
	}

	
	@Override
	public byte[] cifrar(String texto) throws Exception {
		byte[] clearBytes= texto.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
		byte[] textoCifrado=cipher.doFinal(clearBytes);		
		return textoCifrado;
	}

	@Override
	public byte[] descifrar(String texto) throws Exception {
		byte[] input= texto.getBytes();
		cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
		byte[] textoOriginal=cipher.doFinal(input);
		return textoOriginal;
	}
	@Override
	public byte[] descifrar(byte[] texto) throws Exception {		
		cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
		byte[] textoOriginal=cipher.doFinal(texto);
		return textoOriginal;
	}
	@Override
	public byte[] cifrarParaElServidor(byte[] input, PublicKey llaveServidor) throws Exception {		
		cipher.init(Cipher.ENCRYPT_MODE, llaveServidor);
		byte[] textoOriginal=cipher.doFinal(input);
		return textoOriginal;
	}
	@Override
	public KeyPair getKeys() {
		return keyPair;
	}

}
