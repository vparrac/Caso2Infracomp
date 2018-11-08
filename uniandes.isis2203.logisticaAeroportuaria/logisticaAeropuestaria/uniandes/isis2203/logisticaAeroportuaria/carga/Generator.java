package uniandes.isis2203.logisticaAeroportuaria.carga;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;
import uniandes.gload.examples.clientserver.generator.ClientServerTask;

public class Generator {
	/**
	 * Load Generator Service (From Gload 1.0)
	 */
	private LoadGenerator generator;
	
	/**
	 * Constructor
	 */
	public Generator() {
		Task work = createTask();
		int numberOfTasks=100;
		int gapsBetweenTask=1000;
		generator=new LoadGenerator("Cliente - Server Load Test", numberOfTasks, work, gapsBetweenTask);
	}

	private Task createTask() {
		return new ClientServerTask();
	}
	
}
