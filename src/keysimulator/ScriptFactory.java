package keysimulator;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.PlainDocument;
import skillscript.SkillMachine;
import skillscript.SkillManager;
import ui.HomeFrame;




public class ScriptFactory {
	public static void main(String[] args) {
		SkillMachine machine = new SkillMachine();
		SkillManager sm = new SkillManager(machine);
		machine.setManager(sm);
		
        machine.start();
		HomeFrame home = new HomeFrame(machine, sm);
		home.setVisible(true);
//		System.out.println("home: " + System.getProperty("java.home"));
		
//		System.loadLibrary("testdll");
		
//		 for(Iterator<String> itr = map.keySet().iterator();itr.hasNext();){
//	            String key = itr.next();
//	            System.out.println(key + "=" + map.get(key));
//	      }
//		try {
	
//		} catch(Throwable t) {
//			System.err.println("oh hell\n\n\n\n");
//		}
//		System.out.println(System.getProperty("java.library.path"));
		
		

		
	}
}


