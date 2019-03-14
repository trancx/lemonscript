package keysimulator;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;




public class KeySimulator {
	public static final int CONTROL_PORT = 0x64;
	public static final int DATA_PORT = 0x60;
	
	public static final Map<String,Integer> map= new HashMap<String,Integer>();
	
	static {
		map.put("0", KeyEvent.VK_0);
		map.put("1", KeyEvent.VK_1);
		map.put("2", KeyEvent.VK_2);
		map.put("3", KeyEvent.VK_3);
		map.put("4", KeyEvent.VK_4);
		map.put("5", KeyEvent.VK_5);
		map.put("6", KeyEvent.VK_6);
		map.put("7", KeyEvent.VK_7);
		map.put("8", KeyEvent.VK_8);
		map.put("9", KeyEvent.VK_9);
		map.put("a", KeyEvent.VK_A);
		map.put("b", KeyEvent.VK_B);
		map.put("c", KeyEvent.VK_C);
		map.put("d", KeyEvent.VK_D);
		map.put("e", KeyEvent.VK_E);
		map.put("f", KeyEvent.VK_F);
		map.put("g", KeyEvent.VK_G);
		map.put("h", KeyEvent.VK_H);
		map.put("i", KeyEvent.VK_I);
		map.put("j", KeyEvent.VK_J);
		map.put("k", KeyEvent.VK_K);
		map.put("l", KeyEvent.VK_L);
		map.put("m", KeyEvent.VK_M);
		map.put("n", KeyEvent.VK_N);
		map.put("o", KeyEvent.VK_O);
		map.put("p", KeyEvent.VK_P);
		map.put("q", KeyEvent.VK_Q);
		map.put("r", KeyEvent.VK_R);
		map.put("s", KeyEvent.VK_S);
		map.put("t", KeyEvent.VK_T);
		map.put("u", KeyEvent.VK_U);
		map.put("v", KeyEvent.VK_V);
		map.put("w", KeyEvent.VK_W);
		map.put("x", KeyEvent.VK_X);
		map.put("y", KeyEvent.VK_Y);
		map.put("z", KeyEvent.VK_Z);
		map.put("Tab", KeyEvent.VK_TAB);
		map.put("Space", KeyEvent.VK_SPACE);
		map.put("Shift", KeyEvent.VK_SHIFT);
		map.put("Cntl", KeyEvent.VK_CONTROL);
		map.put("Alt", KeyEvent.VK_ALT);
		map.put("F1",KeyEvent.VK_F1);
		map.put("F2",KeyEvent.VK_F2);
		map.put("F3",KeyEvent.VK_F3);
		map.put("F4",KeyEvent.VK_F4);
		map.put("F5",KeyEvent.VK_F5);
		map.put("F6",KeyEvent.VK_F6);
		map.put("F7",KeyEvent.VK_F7);
		map.put("F8",KeyEvent.VK_F8);
		map.put("F9",KeyEvent.VK_F9);
		map.put("F10",KeyEvent.VK_F10);
		map.put("F11",KeyEvent.VK_F11);
		map.put("F12",KeyEvent.VK_F12);	
		map.put("↑", KeyEvent.VK_UP);
		map.put("←", KeyEvent.VK_LEFT);
		map.put("↓", KeyEvent.VK_DOWN);
		map.put("→", KeyEvent.VK_RIGHT);
		/**
		 *   
		 *  public static final int VK_LEFT = 37;
  
  			// Field descriptor #497 I
  			public static final int VK_UP = 38;
  
  			// Field descriptor #497 I
  			public static final int VK_RIGHT = 39;
  
  			// Field descriptor #497 I
  			public static final int VK_DOWN = 40;
		 */
	}
	//使用User32库里面键位值转换
	public interface User32 extends StdCallLibrary {
		@SuppressWarnings("deprecation")
		User32 Instance = (User32)Native.loadLibrary("User32", User32.class);
		int MapVirtualKeyA(int key, int type);
	}
	//此处是winIo使用关键

	public interface WinIo extends Library {
		@SuppressWarnings("deprecation")
		WinIo Instance = ( WinIo )Native.loadLibrary("WinIo64", WinIo.class);		
		boolean InitializeWinIo();
		boolean GetPortVal(int portAddr, int pPortVal, int size);
		boolean SetPortVal(int portAddr, int portVal, int size) ;
		void ShutdownWinIo();
	}
	//将虚拟键位值转成扫描码
	public static int toScanCode(String key) {
		try {
			return User32.Instance.MapVirtualKeyA(map.get(key).intValue(),0);
		} catch (Exception e) {
			System.out.println("toScanCode exception; key: " + key);
			return 0;
		}
	}
	
	
	public static void KBCWait4IBE() throws Exception {
		int val=0;
		do {
			// simply read the 0x64 will get STATUS_REGESTER value
			/**
			 * 0: output buffer full if set
			 * 1: input buffer is full
			 * 2: After POST, will be clear
			 * 3: Content in InputBuffer is cmd
			 * 4: Set if KBD is disabled
			 * 5: Trans expired
			 * 6: Revc expired
			 * 7: Check error
			 */
			Thread.sleep(10);
			/**
			 * it's significant, but i don't know why
			 * but it does works! 睡眠10s可以缓解压力
			 */
			
			if( !WinIo.Instance.GetPortVal(CONTROL_PORT,val, 1)) {
				System.err.println("Cannot get the Port");
			}
//			System.out.println("port busy");
		} while (( 0x2 & val )>0);
		
	}
	
	/**
	 *  0xD2 means the data sent to the DATA_PORT(0x60) will be
	 *  put in OUT_PUT_REGISTER
	 *  
	 * 
	 */
	private static void KeyDown(int key) throws Exception {
		KBCWait4IBE();
		WinIo.Instance.SetPortVal(CONTROL_PORT,0xD2,1);
		KBCWait4IBE();
		WinIo.Instance.SetPortVal(DATA_PORT, key, 1);
	}
	
	private static void KeyUp(int key) throws Exception {
		KBCWait4IBE();
		WinIo.Instance.SetPortVal(CONTROL_PORT, 0xD2, 1);
		KBCWait4IBE();
		WinIo.Instance.SetPortVal(DATA_PORT, (key | 0x80), 1);		
	}
	
	public static void keyDown(String key) {
		try {
			KeyDown(toScanCode(key));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void keyUp(String key) {
		try {
			KeyUp(toScanCode(key));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return: true is on, false is off
	 */
	public static boolean checkInterrupt() throws Exception {
		int val = 0;
		KBCWait4IBE();
		WinIo.Instance.SetPortVal(CONTROL_PORT, 0x20, 1);
		WinIo.Instance.GetPortVal(DATA_PORT, val, 1);
		if(  (val & 0x1) == 1 )
			return true;
		return false;
	}
	public static MyFrame frame;
	
	public static void whenClosing() {
		JIntellitype.getInstance().unregisterHotKey(1);
//		JIntellitype.getInstance().unregisterHotKey(2);
	}
	public static void main(String[] args) throws Exception { 
		SkillsManager sm = new SkillsManager();
		Automation machine = new Automation(sm);
		//Skill stab = new Skill("Stab-1", "111", 0, 3, 620);
		Skill stab2 = new Skill("Stab-2", "ad", 0, 3, 0);
//		Skill vortex = new Skill("Vortex-2", "3222", 0, 2, 1000);
//		Skill doublespear = new Skill("DoubleSpear-3", "2111", 0, 1, 700);
//		Skill ironpatient = new Skill("IronPatient", "44", 8000, 7, 900);
//		Skill firespear = new Skill("FireSpear", "v", 20000, 19, 0);
//		Skill swapjoin = new Skill("Swapjoin", "r", 4000, 15, 80);
//		Skill redlotus = new Skill("RedLotus", "55", 15000, 11, 1600);
//		
		//stab.setEffect(1);
		//stab2.setEffect(1);
//		doublespear.setEffect(1);
//		vortex.setEffect(1);
//		ironpatient.setTriggerState(3);
//		ironpatient.setEffect(-3);
		
		//sm.addSkill(stab);
		sm.addSkill(stab2);
//		sm.addSkill(doublespear);
//		sm.addSkill(vortex);
//		sm.addSkill(ironpatient);
//		sm.addSkill(firespear);
//		sm.addSkill(swapjoin);
//		sm.addSkill(redlotus);
		
		
//		Skill dianjiang = new Skill("DianJiang", "r", 800, 6, 80);
//		Skill chuZhan = new Skill("ChuZhan", "q", 6000, 7, 700);
//		Skill caiYi = new Skill("CaiYi", "5", 10000, 8, 700);
////		
//		sm.addSkill(dianjiang);
//		sm.addSkill(chuZhan);
//		sm.addSkill(caiYi);

		MyFrame myFrame = null;
		myFrame = new MyFrame();
		frame = myFrame;
//		myFrame.setVisible(true);
		
		JIntellitype.getInstance().registerHotKey(1, 0, KeyEvent.VK_BACK_QUOTE);
//		JIntellitype.getInstance().registerHotKey(2,JIntellitype.MOD_ALT, KeyEvent.VK_TAB);
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			public void onHotKey(int identifier) {
				System.out.println("pressed!");
				switch (identifier) {
				case 1:
					// do something
					frame.switchState();
//					System.out.println("got");
					break;
				case 2:
					frame.switchOff();
					break;
				}
			}
		});
		System.out.println("hello!");
		File dir  = new File("");
		String pwd = dir.getCanonicalPath();
		System.out.println(pwd);
		try {
			 Process process = Runtime.getRuntime().exec("keyboard.exe");
			 BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			 Thread.sleep(1000);
			 System.out.println(br.read());
		} catch( final Exception e) {
			myFrame.printError("Error when execute keyboard.exe!");
			System.exit(-1);
		}
		if( WinIo.Instance.InitializeWinIo() ) {
//			Thread.sleep(1000);
			
			machine.start();
			myFrame.setVisible(true);
			
//			while(true) {
//				stab.act();
//				vortex.act();
//				doublespear.act();
//			}
		} else {
			myFrame.printError("Cannot initialize the WinIO!");
			System.exit(-1);
		}
		System.out.println("exit");
	}
}

class Skill extends TimerTask implements Comparable<Skill> {
	private final String key; /* ms */
	private final int cd;  /* ms */
	private String name = "default";
	private int priority; /* In general, short cd has low priot  */
	private int delay; /* time(ms) spent on this skill*/
	private boolean ready;
	private int triggerstate = 0;
	private int effect = 0;
	public final static int TAP_DELAY = 350; 
	
	public int getDelay() {
		return delay;
	}

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}

	public int getTriggerState() {
		return triggerstate;
	}

	public void setTriggerState(int triggerstate) {
		this.triggerstate = triggerstate;
	}

	public boolean isReady() {
		return ready;
	}
	
	public void setReady() {
		ready = true;
	}
	
	@Override
	// default  negative -> positive
	public int compareTo(Skill s) {
		// TODO Auto-generated method stub
		if( priority > s.getPriority() )
			return -1;
		return 1;
	}

	@Override
	public void run() {
		// when the cd is OK
		setReady();
//		System.out.println(this + " ready");
	}

	public void act()  throws Exception {
		for (int i = 0; i < key.length(); i++) {
			KeySimulator.keyDown(""+ key.charAt(i));
			Thread.sleep(50); /* FIXME:  may be we can delete it*/
			KeySimulator.keyUp(""+ key.charAt(i));
			Thread.sleep(400+i*65);  /* +i*65 */
//			Thread.sleep(100); 
		}
//		System.out.println(this + " executed");
		if( delay > 10 )
			Thread.sleep(delay);
	}
	
	Skill( String key, int cd ) {
		this.key = key;
		this.cd = cd;
	}
	
	Skill( String name, String key, int cd, int priority, int latency) {
		this(key, cd);
		this.priority = priority;
		ready = true;
		this.name = name;
		this.delay = latency;
	}
	@Override
	public String toString() {
		return name;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setName( String name ) {
		this.name = name;
	}
	public int getCd() { return cd; }
	
	public int getRealCd() {
		return (cd - key.length()*TAP_DELAY - delay);
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
		 ready = false;
	}

}

class SkillsManager {
	private int state = 0;
	public final static int MAX_STATE = 3;
	
	public void debug() {
		int i;
		for( Skill s : skills) {
			i = s.getDelay() / (s.getPriority() * 11) >> s.getPriority();
			System.out.println(s + "'s level is " + i  );
		}
	}
	

	public Skill getReadySkills() {
		Skill best = null;
		ready.clear();
		while( ready.isEmpty() ) {
			for( Skill skill : skills) {
				if( skill.isReady() && state >= skill.getTriggerState()) {
					ready.add(skill);
				}
			}
		}
		ready.sort(null);
		best = ready.get(0);
//		System.out.println(best + " selected PRI: " + best.getPriority());
		return best;
	}
	
	public void addSkill( Skill s) {
		skills.add(s);
	}
	
	/**
	 * FIXME: should be more portable
	 * let the skill(task) to determine 
	 * whether it should be reinserted to
	 * the timer_queue 
	 * 
	 * e.g. Skill.resume(tasktimer)
	 * would be okay!
	 * 
	 */
	public void finish( Skill s) {
		if( s.getCd() > 0 ) {	
			s.reset();
			tasktimer.schedule(s, s.getRealCd());
		} 
		if( s.getEffect() != 0 ) {	
			state += s.getEffect();
			if( state > MAX_STATE)
				state = MAX_STATE;
		}
		// change the priority by its priority! FIXME: just for blood_river
		for( Skill skill : skills) {
			if( s.getCd() < 4000 ) {
				if( skill != s)
					skill.setPriority(skill.getPriority() + 2);
			}
		}
		
		if( ready.remove(s) == false ) {
			System.err.println("remove a task not in the list!!");
		}
	}
	
	SkillsManager() {
		
	}
	
	private Timer tasktimer = new Timer();
	private ArrayList<Skill> ready = new ArrayList<Skill>();
	private ArrayList<Skill> skills = new ArrayList<Skill>();
}

class Automation extends Thread {
	public static int state = 0; /**/
	private SkillsManager sm;
	public final static int AUTOMATION_RUNNING = 1;
	public final static int AUTOMATION_SLEEPING = 0;
	 
	public Automation( SkillsManager sm) {
		// TODO Auto-generated constructor stub
		this.sm = sm;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			while( true ) {
				while( state == AUTOMATION_RUNNING) {
					Skill s = sm.getReadySkills();
					s.act();
					sm.finish(s);
				}
				Thread.sleep(200);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
}


class MyFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Button button= null;
   
    public MyFrame(){
        super();
        setSize(100,100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        setAlwaysOnTop(true);
        button= new Button("OFF");
        button.setSize(100,100);
        button.addActionListener(new ActionListener() {	
            @Override
            public void actionPerformed(ActionEvent e) {
              switchState();
            }
        });
        add(button);
    }
    public void printError(String errorMsg ) {
    	JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void switchState() {
    	 if( Automation.state == Automation.AUTOMATION_RUNNING ) {
    		 switchOff();
    	 } else {
    		 switchOn();
    		System.out.println("ON!");
    	 }
    }
    public void switchOff() {
    	if( Automation.state == Automation.AUTOMATION_RUNNING ) {
    		Automation.state = Automation.AUTOMATION_SLEEPING;
    		button.setLabel("OFF");
    	}
    }
    public void switchOn() {
    	if( Automation.state == Automation.AUTOMATION_SLEEPING ) {
    		Automation.state = Automation.AUTOMATION_RUNNING;
    		button.setLabel("ON");
    	}
    }
    
    @Override
	protected void processWindowEvent(WindowEvent e) {
		// TODO Auto-generated method stub
		super.processWindowEvent(e);
		if(e.getID() == WindowEvent.WINDOW_CLOSING)
	       {
	        	KeySimulator.whenClosing();
	        	System.out.println("Closing");
	        	System.exit(0);
	        	
	       }
	}

}
