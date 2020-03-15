package mundo.Navasoft;
import java.io.*;

public class Servidor {

	private ServidorThread[] servidores; //Los hilos del servidor
	private Buffer buff; //El Buffer al que hace referencia
	public int numClientes = -1; //El número de clientes
	private int numSer = -1; //El número de hilos servidores que habrá 
	public int numConsu = -1; //Número de consultas por cliente
	private static int capacidadBuf = -1; //La cantidad  de mensajes que caben en el buzón

	public Servidor(Buffer pBuff) {
		buff = pBuff;//Buffer

		File f = new File("docs/condicionesIniciales.txt"); 
		if(f.exists()){ //Si existe el archivo, voy a cargar de ahí
			try{
				BufferedReader in = new BufferedReader(new FileReader(f));
				String line = in.readLine();
				while(line != null){
					//Código que va guardando los datos del archivo en el objeto servidor.
					String[] datos = line.split(":");
					if(datos[0].equalsIgnoreCase("numClientes"))
						numClientes = Integer.valueOf(datos[1]);
					else if(datos[0].equalsIgnoreCase("numSer"))
						numSer = Integer.valueOf(datos[1]);
					else if(datos[0].equalsIgnoreCase("numConsu"))
						numConsu = Integer.valueOf(datos[1]);
					else if(datos[0].equalsIgnoreCase("capacidadBuf"))
						capacidadBuf = Integer.valueOf(datos[1]);
					line = in.readLine();					
				}
				in.close();
				System.out.println("Creación del servidor del archivo: ");
				System.out.println("Número de clientes: "+numClientes);
				System.out.println("Número de consultas: "+numConsu);
				System.out.println("Capacidad el Buffer: "+capacidadBuf);
				System.out.println("Número de hilos del servidor: " +numSer);
				System.out.println("=======================================================");
			}
			catch(IOException e){
				e.printStackTrace();	
			}
		}
		else{//Si el archivo no existe, lo creo con parámetros aleatorios
			numClientes = (int) (Math.random()*100)+1;
			numSer = (int) (Math.random()*50)+1;
			numConsu = (int) (Math.random()*5)+1;
			capacidadBuf = (int) (Math.random()*100)+1;
			try{
				f.createNewFile();
				PrintWriter out = new PrintWriter(f);
				out.println("numClientes:" + numClientes);
				out.println("numSer:" + numSer);
				out.println("numConsu:" + numConsu);
				out.println("capacidadBuf:" + capacidadBuf);
				out.close();

				System.out.println("Creación del servidor generado: ");
				System.out.println("Número de clientes: "+numClientes);
				System.out.println("Número de consultas: "+numConsu);
				System.out.println("Capacidad el Buffer: "+capacidadBuf);
				System.out.println("Número de hilos del servidor: " +numSer);
				System.out.println("=======================================================");
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		servidores = new ServidorThread[numSer];
		for(int i=0; i<numSer; i++)
			servidores[i] = new ServidorThread(buff,i);
	}

	public static void main(String[] args) {
		Buffer buffer = new Buffer(capacidadBuf); //Creación del Buffer
		Servidor servidor = new Servidor(buffer); //Creación del servidor
		buffer.setCapacidad(capacidadBuf); //Se inicializa la capacidad del Buffer
		for (int i = 0; i < servidor.numClientes; i++) {
			ClienteThread ct = new ClienteThread(servidor.numConsu, buffer,i); //Se crean los clientes
			ct.start();				//Se ponen a correr los clientes 
		}
		for (int i = 0; i < servidor.numSer; i++) {
			servidor.servidores[i].start(); //Se ponen a correr los hilos del servidor
		}
	}



}