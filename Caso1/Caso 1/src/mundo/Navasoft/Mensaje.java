package mundo.Navasoft;
public class Mensaje {
	//El contenido del mensaje
	private int mensaje;
	//Lugar donde se almacenará la respuesta que da el servidor
	private int respuesta;
	//El cliente que envió el mensaje
	private ClienteThread cliente;	
	public Mensaje (int pMen, ClienteThread pC){
		mensaje = pMen;
		cliente = pC;
		respuesta = -1;
	}	
	public int getMensaje(){
		return mensaje;
	}	
	public void setRespuesta(int pResp){
		respuesta = pResp;
	}	
	public int getRespuesta(){
		return respuesta;
	}	
	public ClienteThread getCliente(){
		return cliente;
	}

	public synchronized void esperar(){
		try{
			//Duerme al cliente en la bolsa del mensaje
			wait();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
