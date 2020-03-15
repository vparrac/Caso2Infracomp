package uniandes.issi2203.logisticaAeroportuaria.ServidorCifrado;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Security;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import uniandes.gload.core.Task;

public class Coordinador {
	
	public static final int N_THREADS = 8;
	private static ServerSocket ss;	
	private static final String MAESTRO = "MAESTRO: ";
	private static final int PUERTO=8081;
	static java.security.cert.X509Certificate certSer; /* acceso default */
	static KeyPair keyPairServidor; /* acceso default */
	private static ExecutorService executor;
	private static double porcentaje;

	
	public static void main(String[] args) throws Exception{
		porcentaje=0;
		executor = Executors.newFixedThreadPool(N_THREADS);
		System.out.println(MAESTRO + "Empezando servidor maestro en puerto " + PUERTO);
		// Adiciona la libreria como un proveedor de seguridad.
		// Necesario para crear certificados.
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());		
		keyPairServidor = Seg.grsa();
		certSer = Seg.gc(keyPairServidor);
		int idThread = 0;
		// Crea el socket que escucha en el puerto seleccionado.
		ss = new ServerSocket(PUERTO);
		System.out.println(MAESTRO + "Socket creado.");	
		
		while (true) {
			try { 
				Socket sc = ss.accept();
				System.out.println(MAESTRO + "Cliente " + idThread + " aceptado.");
				executor.submit(new Delegado3(sc,idThread));
				idThread++;
				
				//Guardaremos el mayor uso de la CPU
				double aux=getProcessCpuLoad();
				if(aux>=porcentaje){
					porcentaje=aux;
				}
				
				if(idThread==100){
					BufferedWriter bw = null;
				    FileWriter fw = null;
				    try {
				        File file = new File("./data/log.txt");	        
				        synchronized (file) {
				        	if (!file.exists()) {
					            file.createNewFile();
					        }
					        // flag true, indica adjuntar información al archivo.
					        fw = new FileWriter(file.getAbsoluteFile(), true);
					        bw = new BufferedWriter(fw);
					        bw.write(porcentaje+"");	  
						}
				        
				    } catch (IOException e) {
				        e.printStackTrace();
				    } finally {
				        try {
				            if (bw != null)
				                bw.close();
				            if (fw != null)
				                fw.close();
				        } catch (IOException ex) {
				            ex.printStackTrace();
				        }
				    }
				}
			} catch (IOException e) {
				System.out.println(MAESTRO + "Error creando el socket cliente");
				//Aquí se guardan los perdidos 				
				BufferedWriter bw = null;
			    FileWriter fw = null;
			    try {
			        File file = new File("./data/solicitudesPerdidas.txt");	        
			        synchronized (file) {
			        	if (!file.exists()) {
				            file.createNewFile();
				        }
				        // flag true, indica adjuntar información al archivo.
				        fw = new FileWriter(file.getAbsoluteFile(), true);
				        bw = new BufferedWriter(fw);
				        bw.write("1,");	  
					}
			    } catch (IOException e1) {
			        System.out.println("No se pudo escribir algún cliente rechazado");
			    } finally {
			        try {
			            if (bw != null)
			                bw.close();
			            if (fw != null)
			                fw.close();
			        } catch (IOException ex) {
			            ex.printStackTrace();
			        }
			    }
				e.printStackTrace();
			}
		}
	}
	

	public static double getProcessCpuLoad() throws Exception {
	    MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
	    ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
	    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });
	    if (list.isEmpty())     return Double.NaN;
	    Attribute att = (Attribute)list.get(0);
	    Double value  = (Double)att.getValue();
	    // usually takes a couple of seconds before we get real values
	    if (value == -1.0)      return Double.NaN;
	    // returns a percentage value with 1 decimal point precision
	    return ((int)(value * 1000) / 10.0);
	}
	
}