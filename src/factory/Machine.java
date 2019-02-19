package factory;

public abstract class Machine extends Thread {

	public final static int AUTOMATION_RUNNING = 1;
	public final static int AUTOMATION_SLEEPING = 0;


	public void setState(int state) {
		this.state = state;
//		System.out.println("state changed: " + state);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			while( true ) {
				while( state == AUTOMATION_RUNNING) {
//					System.out.println("sss");
					Task task = manager.getReadyTask();
					if( task != null )
						execute(task);
					else
						sleep(100);
				}
				sleep(100);
				// FIXME why???

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ����Ϊ�˷������task��ִ�У���Ҫ��ôһ�ӿڣ���managerֱ�ӵ���
	 * �������ӳٵ����񣬻��߽����������ֱ��ִ��
	 * 
	 * @param t ��Ҫִ�е�����
	 */
	public void execute(Task t) {
		t.pending();
		manager.finished(t);
	}
	
	/**
	 * ����֧�ֶ��ֹ��ܣ���ͬ�Ĺ��ܶ�Ӧ�ľ��ǲ�ͬ�� Manager
	 * 
	 * @param m manager to set
	 */
	public void setManager(Manager m) {
		manager = m;
	}

	public int getManagerState() {
		return state;
	}
	
	protected Machine(Manager m) {
		this.manager = m;
		this.state = Machine.AUTOMATION_SLEEPING;
	}
	
	protected Machine( ) {
		this.state = Machine.AUTOMATION_SLEEPING;
	}
	
	protected Manager manager;
	private int state; /**/
}
