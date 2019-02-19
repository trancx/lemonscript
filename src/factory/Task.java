package factory;

import java.util.TimerTask;

public abstract class Task extends TimerTask {
	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public Task(Manager taskManger) {
		super();
		this.taskManger = taskManger;
		setReady(true);
	}

	public abstract void pending();
	
	private boolean ready;
	private Manager taskManger;
}
