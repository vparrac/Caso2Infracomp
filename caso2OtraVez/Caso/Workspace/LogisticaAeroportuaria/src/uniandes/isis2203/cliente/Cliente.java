package uniandes.isis2203.cliente;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Scanner;
import javax.xml.bind.DatatypeConverter;
/**
 * Clase que representa un cliente que entra en contacto con el servidor siguiendo los protocolos
 * descritos en el caso
 * Este cliente trabaja con se servidor cifrado
 * @author Valerie Parra Cort�s
 * @author Pablo Andr�s Suarez Murillo
 */
public class Cliente {	
	/**
	 * Puerto donde se correra el servidor
	 */
	public final static int PUERTO=8081;
	/**
	 * El cifrador para la parte de cifrado sim�trico
	 */
	private ICifradorSimetrico cifradoSimetrico;
	/**
	 * El cifrador para la parde del cifrado asimetrico
	 */
	private ICifradorAsimetrico cifradorAsimetrico;
	/**
	 * El cifrador pra el HMAC
	 */
	private MessageAuthenticationCode codigo;
	/**
	 * Socket para la comunicaci�n
	 */
	private Socket sck_cliente;
	/**
	 * Lector para la comunicaci�n
	 */
	private BufferedReader in;
	/**
	 * Escritor para la comunicaci�n
	 */
	private  PrintWriter out;
	/**
	 * Lector para el input del usuario
	 */
	private Scanner stdIn;
	/**
	 * Atributo que representa el certificado que se va a recibir del servidor
	 */
	private X509Certificate certificadoServidor;

	/**
	 * M�todo que implementa el protocolo de comunicaci�n cliente-servidor para las consulta
	 * que quiera realizar el cliente
	 * @param args del mundo
	 */

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Cliente cli= new Cliente();				
		cli.stdIn= new Scanner(System.in);

		try {
			//M�todo que permite establecer la conexi�n al puerto 
			cli.establecerConexion();
			//Etapa 1 del protocolo de comunicaci�n: Establecer los algoitmos
			cli.seleccionarAlgoritmos();
			//Etapa 2 del protocolo de comunicaci�n: autenticaci�n del cliente
			cli.enviarCertificado();
			//Etapa 3 del protocolo de comuniaci�n: autenticaci�n del servidor
			cli.recibirCertificado();
			//Etapa 4 del protocolo de comunicaci�n: verificaci�n
			cli.recibirLlaveSImetrica();
			//Etapa 5 del protocolo de comunicaci�n: consulta
			String fromServer;
			String fromUser;			

			System.out.println("Indique la consulta a realizar");
			fromUser= cli.stdIn.nextLine();
			if(fromUser!=null) {				
				System.out.println("Cliente env�a: "+fromUser);
				//Se cifra la consulta
				byte[] consultaCifradaBytes=cli.cifradoSimetrico.cifrar(fromUser);
				//Se encapsula
				String consultaString= DatatypeConverter.printHexBinary(consultaCifradaBytes);
				//Se envia
				cli.out.println(consultaString);


				//Se genera el HMAC
				byte[] hmacConsulta= cli.codigo.getDigest(fromUser.getBytes(),cli.cifradoSimetrico.getKey());					
				//Se encapsula
				String hmacConsultaString= DatatypeConverter.printHexBinary(hmacConsulta);
				//Se envia
				cli.out.println(hmacConsultaString);
				//Se termina (no se si se hace m�s de una consulta, puede que el while sea innecesario igual lo dejar�
				fromServer=cli.in.readLine();
				String[] rta= fromServer.split(Protocolo.SEPARADOR_PRINCIPAL);
				System.out.println("Server :"+ rta[1]);					
			}
		}
		catch(Exception e) {
			System.out.println("Ha ocurrido un error "+e.getMessage());
			e.printStackTrace();
			cli.finalizar();
			try {
				cli.sck_cliente.close();
			} catch (IOException e1) {				
				e1.printStackTrace();
			}
		}
	}

	/**
	 * M�todo que crea el socket con la direcci�n local en el puerto indicado en la constante de la clase
	 * El m�todo inicia el protocolo de comunicaci�n con el servidor
	 * Si hay alg�n error iniciando la comunicaci�n imprime un mensaje y cierra el socket
	 */
	public void establecerConexion() throws Exception{
		System.out.println("Iniciando conexi�n");		
		sck_cliente=new Socket ("localhost",PUERTO);
		out=new PrintWriter(sck_cliente.getOutputStream(),true);
		in= new BufferedReader(new InputStreamReader(sck_cliente.getInputStream()));
		@SuppressWarnings("unused")
		BufferedReader stdIn= new BufferedReader(new InputStreamReader(System.in));
		out.println(Protocolo.HOLA);
		String msgResp = in.readLine();
		if(msgResp.equals(Protocolo.ERROR)){			
			sck_cliente.close();
			throw new Exception("Se produjo un error creando la conexi�n");
		}
		else if(msgResp.equals(Protocolo.OK))
			System.out.println("Conexi�n exitosa");

	}
	/**
	 * M�todo que permite que el cliente introduzca los algoritmos, los algoritmos los introduce
	 * el cliente separados por dos puntos
	 */
	public  void seleccionarAlgoritmos() throws Exception{		
		System.out.println("Escriba los algoritmos a usar separados por dos puntos; ALs:Ala:AlHMAC.");
		System.out.println("los algoritmos deben estar tal como se indica en el caso Ej: Blowfish:RSA:HMACSHA1");
		String algoritmos= stdIn.nextLine();
		String[] algo= algoritmos.split(":");
		cifradoSimetrico=new CifradorSimetrico(algo[0]);				
		cifradorAsimetrico= new CifradorAsimetrico(algo[1]);
		codigo=new MessageAuthenticationCode(algo[2]);
		out.println(Protocolo.ALGORTIMOS+Protocolo.SEPARADOR_PRINCIPAL+algoritmos);
		String rta= in.readLine();		
		if(rta.equals(Protocolo.OK)){
			System.out.println("Selecci�n de algoritmos: " + rta);
		}
		else if(rta.equals(Protocolo.ERROR)) {
			throw new Exception("El servidor no soporta alguno de losalgoritmos");
		}

	}
	/**
	 * M�todo que crea un certificado local en el protocoloa X509
	 * Luego de creado el certificado es encapsulado y enviado por el Socket
	 */
	public void enviarCertificado() throws Exception{
		String certificadoEnString = "";
		X509Certificate certificado= GeneradorCertificados.createCertificate(cifradorAsimetrico.getKeys());
		byte[] certificadoEnBytes = certificado.getEncoded( );
		certificadoEnString = printHexBinary(certificadoEnBytes);
		out.println(certificadoEnString);		
		System.out.println("Certificado enviado con �xito");
		System.out.println("Server: "+in.readLine());
	}
	/**
	 * Recibe y convierte en un objeto el certificado enviado por el servidor,
	 * Una vez el certificado es recibido �xitosamente imprime "OK" en el socket de comunicaci�n
	 */
	public void recibirCertificado() throws Exception{
			String h = in.readLine();			
			byte[] cer=DatatypeConverter.parseHexBinary(h);			
			certificadoServidor = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(cer));
			out.println(Protocolo.OK);
			System.out.println("Certificado del servidor recibido con �xito");
	}
	/**
	 * Recibe la llave sim�trica, la descifra con la llave privada del cliente,
	 * Env�a al servidor la misma llave cifrada con la llave publica del servidor
	 */
	public void recibirLlaveSImetrica() throws Exception{
		
			String llave = in.readLine();
			byte[] llaveBytess=DatatypeConverter.parseHexBinary(llave);
			byte[] llaveBytesDescifrada=cifradorAsimetrico.descifrar(llaveBytess);			

			cifradoSimetrico.setKey(llaveBytesDescifrada);
			byte[] llaveParaEnviar =cifradorAsimetrico.cifrarParaElServidor(llaveBytesDescifrada, certificadoServidor.getPublicKey());		
			String mensajeAEnviar= DatatypeConverter.printHexBinary(llaveParaEnviar);
			out.println(mensajeAEnviar);
			String mensajeServidor=in.readLine();
			if(mensajeServidor.equals(Protocolo.ERROR)) {
				System.out.println("Ha ocurrido un error !!");
			}
			else if(mensajeServidor.equals(Protocolo.OK)) {
				System.out.println("Todo listo para empezar la consulta");
			}
			else if(mensajeServidor == null || mensajeServidor.trim().equals("")){
				System.out.println("No hay ning�n mensaje");
			}
	}

	/**
	 * Cambia un arreglo de bits por una cadena en Hexa
	 * @param byteArray el arreglo que se quiere convertir
	 * @return String con la cadena 
	 */

	public  String printHexBinary(byte[] byteArray) {
		String out = "";
		for (int i = 0; i < byteArray.length; i++) {
			if ((byteArray[i] & 0xff) <= 0xf) {
				out += "0";
			}
			out += Integer.toHexString(byteArray[i] & 0xff).toUpperCase();
		}
		return out;
	}
	/**
	 * Cierra el socket, el escritor y el lector asociados
	 */
	public  void finalizar() {
		try {
			this.in.close();
			this.out.close();
			this.sck_cliente.close();
			System.out.println("Fin de la comunicaci�n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}