package factory;


public abstract class Manager {	
	
	
	
	public Manager() {
		// TODO Auto-generated constructor stub
		
	}
	
	public abstract Task getReadyTask();
	public abstract int finished(Task t);

}

