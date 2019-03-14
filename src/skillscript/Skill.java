package skillscript;

import java.lang.reflect.Field;
import java.util.TimerTask;

import org.jdom2.Element;

import factory.*;
import keysimulator.KeySimulator;

public class Skill extends Task implements Comparable<Task> {
	
	public Skill(Manager taskManger, int cd, int[] tapDelay,
			String []keyStroke /*, int delay*/) {
		super(taskManger);
		this.cd = cd;
		this.tapDelay = tapDelay;
		this.keyStroke = keyStroke;
//		this.delay = delay;
		realCd = cd;
		for( int i : tapDelay ) {
			realCd -= i;
		}
	//	realCd -= delay;
		if( realCd <= 0 )
			realCd = 0;
	}

	@Override
	public int compareTo(Task o) {
		// TODO Auto-generated method stub
		Skill s = (Skill) o;
		if( priority > s.getPriority() )
			return -1;
		return 1;
	}
	
	public void reset() {
		 Field field; 
		 try { 
			 field = TimerTask.class.getDeclaredField("state");
			 field.setAccessible(true); 
			 field.set(this, 0);
		} catch (NoSuchFieldException e) {
			e.printStackTrace(); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	}
	
	
	/**
	 *  Ӧ�ü����Լ�����˯��ã� machine ��Ӧ��˯�ߣ� ִ����֮���������һ��
	 */
	@Override
	public void pending() {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < keyStroke.length; i++) {
				KeySimulator.keyDown(keyStroke[i]);
				KeySimulator.keyUp(keyStroke[i]);
				Thread.sleep(tapDelay[i]);
				
			}
		//	Thread.sleep(delay);
			// delay Ӧ��Ϊ���һ��������delay��
		} catch (Exception e) {
			// TODO: handle exception
			
		}
//		System.out.println("");
		super.setReady(false);
	}
	
	@Deprecated
	public Element saveSkill() {
		Element s = new Element("skill");
		
		s.addContent(new Element("name").setText(name));
		s.addContent(new Element("length").setText(""+keyStroke.length));
		StringBuilder sb = new StringBuilder();
		for(String tmp : keyStroke ) {
			sb.append(tmp);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		
		s.addContent(new Element("keystroke").setText(sb.toString()));
		s.addContent(new Element("cd").setText( ""+ cd ));
//		s.addContent(new Element("tapdelay").setText(""));
		
		sb.delete(0, sb.length()-1);
		for(int i : tapDelay) {
			sb.append(i);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		s.addContent(new Element("tapdelay").setText(sb.toString()));
			
		return s;
	}
	
	// new Skill().load(x);

	@Deprecated
	public static Skill loadSkill(SkillManager sm, Element skill) {
		int cd; /* ms */
		int length;
		int [] tapDelay;
		String [] delays;
		String [] keyStroke;
		String name;
		
		name = skill.getChildText("name");
		cd = Integer.parseInt(skill.getChildText("cd"));
		keyStroke = skill.getChildText("keystroke").split(",");
		length = Integer.parseInt(skill.getChildText("length"));
		tapDelay = new int[length];
		delays = skill.getChildText("tapdelay").split(",");
		
		if( length != delays.length || keyStroke.length != length ) {
			System.err.println("Parse error, " + "skill_name:" + name);
			System.exit(-1);
		}
		int i = 0;
		for( String tmp : delays ) {
			tapDelay[i++] = Integer.parseInt(tmp);
		}
		
		
		return new Skill(sm, cd, tapDelay, keyStroke);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.setReady(true);
	}
			
	public int getCd() {
		return cd;
	}
	
	public int getRealCd() {
		return realCd;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private int cd; /* ms */
	private int realCd;
	private int [] tapDelay;
	private String [] keyStroke;
//	private int delay;
	private int priority;
	private String name;

}
