package uniandes.isis2203.logisticaAeroportuaria.carga;

import uniandes.gload.core.Task;
public class ClientServerTask  extends Task {

	private Cliente cliente;
	public ClientServerTask() {
		cliente = new Cliente();
	}
	
	@Override
	public void fail() {
		System.out.println(Task.MENSAJE_FAIL);
		
	}

	@Override
	public void success() {
		System.out.println(Task.OK_MESSAGE);
	}

	@Override
	public void execute() {
		try {
			System.out.println("Iniciando conexi�n");		
			cliente.establecerConexion();
			success();
		
			System.out.println("Escogiendo algoritmos");
			cliente.seleccionarAlgoritmos();
			success();
		
			System.out.println("Enviando certificado");
			cliente.enviarCertificado();
			success();
			
			System.out.println("Recibiendo certificado");
			cliente.recibirCertificado();
			success();
			
			System.out.println("Recibiendo llave simetrica");
			cliente.recibirLlaveSImetrica();
			success();
			
			System.out.println("Generando consulta");
			String rta = cliente.realizarConsulta();
			System.out.println("SERVER: "+rta);
			success();
			
			System.out.println("Cerrando sockets, lectores y escritores");
			cliente.finalizar();
			success();
			
		}
		catch (Exception e) {
			fail();
		}
		
	}
}