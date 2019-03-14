package skillscript;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import factory.*;

public class SkillManager extends Manager {
	public SkillManager(SkillMachine sMachine) {
		super();
		this.sMachine = sMachine;
	}

	/* �������Լ�������ô����  */
	@Override
	public Task getReadyTask() {
		// TODO Auto-generated method stub
		Task  best = null;
		Skill tmp;
//		System.out.println("getReadyTask called!");
		// 循环链表太麻烦了，还是就是单向 然后记住上一次的 old
		// 这个表的前提就是 表已经按优先级排好了
		// 如果是固定一个顺序释放 不妨就重复设置
		// 比如 Skill AABCB 就设置两个A 然后优先级降级 
		// 这样处理也符合批量任务的实际设计
		// 因为确实会有重复 也会有固定顺序的
		for ( Task task : tasks) {
			// do something 		                                                  
			// ready?
			if( !task.isReady() )
				continue;
			tmp = (Skill) task;
			// emergency? ����1s����Ϊ��������ִ�е�
			if( tmp.getCd() > 1000 ) {
				sMachine.doUrgent(tmp);
				continue;
			}
			// best?
			if( task != old ) {
				best = task;
				old = task; // save the tmp
				break;
			}	
			// now task == old;
			best = old;
		}
//		if( best != null )
//			System.out.println(((Skill)best).getName() + " is selected");
		return best;
	}


	/**
	 *  ����һ�� Task �Ƿ���Ҫ��ʱ
	 */
	@Override
	public int finished(Task t) {
		// TODO Auto-generated method stub
		if( t instanceof Skill) {
			Skill skill = (Skill)t;
			if( skill.getCd() > 0 ) {	
				skill.reset();
				taskTimer.schedule(skill, skill.getRealCd());
			} 	else {
				t.setReady(true);
			}
		}	
		return 0;
	}

	// Assumption: Tasks are added by priority order! 
	/**
	 * 
	 * @param t
	 */
	public void addTask(Task t) {
		tasks.add(t);
	}
	
	public void clear() {
		tasks.clear();
	}
	
	private Timer taskTimer = new Timer();
	private SkillMachine sMachine;
//	private ArrayList<Task> ready = new ArrayList<Task>();
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private Task old = null;
}
