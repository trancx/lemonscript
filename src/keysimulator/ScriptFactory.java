package keysimulator;

import java.awt.HeadlessException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;
import skillscript.SkillMachine;
import skillscript.SkillManager;
import ui.HomeFrame;




public class ScriptFactory {
	public static void main(String[] args){
		SkillMachine machine = new SkillMachine();
		SkillManager sm = new SkillManager(machine);
		machine.setManager(sm);
		
        machine.start();
		HomeFrame home;
		try {
			home = new HomeFrame(machine, sm);
			home.setVisible(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "程序出错，请联系开发人员查看日志", "ERROR", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
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

/**
 1.首先获取调用日志记录的类的名称

static String strclassname=你的类.class.getName();

2.初始化一个logger

static Logger mylogger= Logger.getLogger(strclassname);

3.初始化一个    FileHandler 指针

    FileHandler fh=null;

4.还可以设置logger记录的信息是否在控制台上输出

        mylogger.setUseParentHandlers(false);//让logger信息不在控制台输出，true即为在控制台输出

5.设置要写入的文件

       fh = new FileHandler("....你的路径Logger.log",true);

6.设置你所要设置的写入文件的日志格式

      fh.setFormatter(new MyFileFormatter());//其中new MyFileFormatter()是你需要自己设计的格式类，这里不做详细解释
	  fileHandler.setFormatter(new Formatter() {//定义一个匿名类
                @Override
                public String format(LogRecord record) {
                    return record.getLevel() + ":" + record.getMessage() + "\n";
                }
            });

7. 最后将此日志信息写入文件

      mylogger.addHandler(fh);
      mylogger.info("你所需要用日志记录的信息");

      fh.close();//关掉日志文本

8.这样就成功实现了将日志记录进文件了
 */
