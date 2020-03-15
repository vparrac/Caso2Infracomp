package mundo.Navasoft;

public class ServidorThread extends Thread{
	private Buffer buf;
	private Mensaje msn;
	private int id;

	public ServidorThread(Buffer bf, int id){
		buf=bf;
		msn=null;
		this.id=id;
	}

	@Override
	public void run(){

		
		while(buf.getNumClientes()!=0){		
			msn= buf.retirarMensaje();
			if(msn!=null){				
				msn.setRespuesta(msn.getMensaje()+1);
				System.out.println("El Servidor con el id : "+id+ " Recibio el mensaje del cliente : " + msn.getCliente().getId());
				buf.enviarRespuesta(msn);	
				buf.notificar();
			}
			else{		
				try {
					sleep(1000);
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
			}
		}
	}
}
