package skillscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import factory.*;
import keysimulator.KeySimulator;
import ui.HomeFrame;

public class SkillMachine extends Machine {

	SkillMachine(Manager m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	public SkillMachine() {
		super();
		// TODO Auto-generated constructor stub
	}
	     
	public void changeManger(SkillManager sm) {
		super.setManager(sm);
	}
	
	public void doUrgent(Skill s) {
		execute(s);
	}
	

	

}
