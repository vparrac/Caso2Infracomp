package uniandes.isis2203.cliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Clase que representa un cliente que entra en contacto con el servidor siguiendo los protocolos
 * descritos en el caso
 * Este cliente trabaja con se servidor no cifrado
 * @author Valerie Parra Cortés
 * @author Pablo Andrés Suarez Murillo
 */
public class Cliente2 {	
	/**
	 * Puerto donde se correra el servidor
	 */
	public final static int PUERTO=8081;

	/**
	 * Socket para la comunicación
	 */
	private Socket sck_cliente;
	/**
	 * Lector para la comunicación
	 */
	private BufferedReader in;
	/**
	 * Escritor para la comunicación
	 */
	private  PrintWriter out;
	/**
	 * Lector para el input del usuario
	 */
	private Scanner stdIn;
	/**
	 * Método que implementa el protocolo de comunicación cliente-servidor para las consulta
	 * que quiera realizar el cliente
	 * @param args del mundo
	 */

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Cliente2 cli= new Cliente2();				
		cli.stdIn= new Scanner(System.in);

		try {
			//Método que permite establecer la conexión al puerto 
			cli.establecerConexion();
			//Etapa 1 del protocolo de comunicación: Establecer los algoitmos
			cli.seleccionarAlgoritmos();
			//Etapa 2 del protocolo de comunicación: autenticación del cliente
			cli.enviarCertificado();
			//Etapa 3 del protocolo de comuniación: autenticación del servidor
			cli.recibirCertificado();
			//Etapa 4 del protocolo de comunicación: verificación
			cli.recibirLlaveSImetrica();
			//Etapa 5 del protocolo de comunicación: consulta
			String fromServer;
			String fromUser;			

			System.out.println("Indique la consulta a realizar");
			fromUser= cli.stdIn.nextLine();
			if(fromUser!=null) {
				
				System.out.println("Cliente envía: "+fromUser);
				cli.out.println(fromUser);
				cli.out.println(fromUser);
				fromServer=cli.in.readLine();
				String[] rta= fromServer.split(Protocolo.SEPARADOR_PRINCIPAL);
				System.out.println("Server :"+ rta[1]);				
			}
		}
		catch(Exception e) {
			System.out.println("Ha ocurrido un error "+e.getMessage());
			cli.finalizar();
			try {
				cli.sck_cliente.close();
			} catch (IOException e1) {				
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Método que crea el socket con la dirección local en el puerto indicado en la constante de la clase
	 * El método inicia el protocolo de comunicación con el servidor
	 * Si hay algún error iniciando la comunicación imprime un mensaje y cierra el socket
	 */
	public void establecerConexion() throws Exception{
		System.out.println("Iniciando conexión");		
		sck_cliente=new Socket ("localhost",PUERTO);
		out=new PrintWriter(sck_cliente.getOutputStream(),true);
		in= new BufferedReader(new InputStreamReader(sck_cliente.getInputStream()));
		@SuppressWarnings("unused")
		BufferedReader stdIn= new BufferedReader(new InputStreamReader(System.in));
		out.println(Protocolo.HOLA);
		String msgResp = in.readLine();
		if(msgResp.equals(Protocolo.ERROR)){
			System.out.println("ERROR iniciando comunicación");
			sck_cliente.close();
		}
		else if(msgResp.equals(Protocolo.OK))
			System.out.println("Conexión exitosa");

	}
	/**
	 * Método que permite que el cliente introduzca los algoritmos, los algoritmos los introduce
	 * el cliente separados por dos puntos
	 */
	public  void seleccionarAlgoritmos() throws Exception{		
		System.out.println("Escriba los algoritmos a usar separados por dos puntos; ALs:Ala:AlHMAC");
		String algoritmos= stdIn.nextLine();		
		out.println(Protocolo.ALGORTIMOS+Protocolo.SEPARADOR_PRINCIPAL+algoritmos);
		String rta= in.readLine();		
		if(rta.equals(Protocolo.OK)){
			System.out.println("Selección de algoritmos: " + rta);
		}
		else if(rta.equals(Protocolo.ERROR)) {
			throw new Exception("El servidor no soporta alguno de los algoritmos");
		}

	}
	/**
	 * Método que crea un certificado local en el protocoloa X509
	 * Luego de creado el certificado es encapsulado y enviado por el Socket
	 */
	public void enviarCertificado() throws Exception{		
		out.println("CLICRT");		
		System.out.println("Certificado enviado con éxito");
		System.out.println("Server: "+in.readLine());
	}
	/**
	 * Recibe y convierte en un objeto el certificado enviado por el servidor,
	 * Una vez el certificado es recibido éxitosamente imprime "OK" en el socket de comunicación
	 */
	public void recibirCertificado() throws Exception{
			in.readLine();			
			out.println(Protocolo.OK);
			System.out.println("Certificado del servidor recibido con éxito");
	}
	/**
	 * Recibe la llave simétrica, la descifra con la llave privada del cliente,
	 * Envía al servidor la misma llave cifrada con la llave publica del servidor
	 */
	public void recibirLlaveSImetrica() throws Exception{
		
			String llave = in.readLine();
			System.out.println("SERV: "+llave);
			out.println(llave);
			String mensajeServidor=in.readLine();
			if(mensajeServidor.equals(Protocolo.ERROR)) {
				System.out.println("Ha ocurrido un error !!");
			}
			else if(mensajeServidor.equals(Protocolo.OK)) {
				System.out.println("Todo listo para empezar la consulta");
			}
			else if(mensajeServidor == null || mensajeServidor.trim().equals("")){
				System.out.println("No hay ningún mensaje");
			}
	}
	/**
	 * Cierra el socket, el escritor y el lector asociados
	 */
	public  void finalizar() {
		try {
			this.in.close();
			this.out.close();
			this.sck_cliente.close();
			System.out.println("Fin de la comunicación");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}