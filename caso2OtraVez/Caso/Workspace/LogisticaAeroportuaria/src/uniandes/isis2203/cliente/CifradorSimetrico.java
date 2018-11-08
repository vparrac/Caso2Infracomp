package uniandes.isis2203.cliente;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
 * Clase que representa un cifrador simétrico
 * @author Valerie Parra Cortés
 * @author Pablo Andrés Suarez Murillo 
 */
public class CifradorSimetrico implements ICifradorSimetrico {
	/**
	 * Constante para representar el cifrado AES
	 */
	public final static String AES="AES";
	/**
	 * El Padding para el algoritmo AES
	 */
	private final static String PADDING = "AES/ECB/PKCS5Padding";
	/**
	 * Constante para representar el cifrado blowfish
	 */
	public final static String BLOWFISH="Blowfish";
	/**
	 * Atributo para generar la llave secreta
	 */
	private SecretKeySpec desKey;
	/**
	 * Cadena de texto con el Padding a utilizar
	 */
	public String padding;
	/**
	 * Cifrador
	 */
	private Cipher cipher;
	/**
	 * Inicializa los padding utilizados para cifrar con cadena dada por parámetro
	 * @param cadena tipo de algoritmo a utilizar
	 * @throws Exception si el algoritmo es incorreco
	 */
	@SuppressWarnings("static-access")
	public CifradorSimetrico(String cadena) throws Exception {
		if(!(cadena.equalsIgnoreCase(AES)||cadena.equalsIgnoreCase(BLOWFISH))){
			throw new Exception("El algoritmo no es soportado");
		}
		else {
			if(cadena.equals(AES)) {				
				padding=this.PADDING;
			}
			else if(cadena.equals(BLOWFISH)) {								
				padding=this.BLOWFISH;					
			}
		}
	}	
	@Override
	public void setKey(byte[] llave) {
		desKey =  new SecretKeySpec(llave, 0, llave.length, padding);		
	}
	@Override
	public byte[] cifrar(String texto) throws Exception {
		cipher=Cipher.getInstance(padding);
		byte[] clearBytes=texto.getBytes();	
		cipher.init(Cipher.ENCRYPT_MODE, desKey);
		byte[] textoCifrado=cipher.doFinal(clearBytes);	
		return textoCifrado;
	}
	@Override
	public byte[] cifrar(byte[] texto) throws Exception {
		cipher=Cipher.getInstance(padding);
		cipher.init(Cipher.ENCRYPT_MODE, desKey);
		byte[] textoCifrado=cipher.doFinal(texto);	
		return textoCifrado;
	}

	@Override
	public byte[] descifrar(String texto) throws Exception {
		cipher=Cipher.getInstance(padding);
		byte[] clearBytes=texto.getBytes();	
		cipher.init(Cipher.DECRYPT_MODE, desKey);
		byte[] clearText= cipher.doFinal(clearBytes);
		return clearText;
	}

	@Override
	public SecretKey getKey() {
		return desKey;
	}
}
