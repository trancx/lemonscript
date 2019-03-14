package keysimulator;


import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.KeyStore.TrustedCertificateEntry;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.naming.InitialContext;
import javax.net.ssl.SSLException;
import javax.print.attribute.standard.RequestingUserName;
import javax.swing.JOptionPane;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

import ui.HomeFrame;




public class KeySimulator {
	public static final int CONTROL_PORT = 0x64;
	public static final int DATA_PORT = 0x60;
	private static String libPath;
	
	public static final Map<String,Integer> map= new HashMap<String,Integer>();
	
	static{
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
		map.put("f1",KeyEvent.VK_F1);
		map.put("f2",KeyEvent.VK_F2);
		map.put("f3",KeyEvent.VK_F3);
		map.put("f4",KeyEvent.VK_F4);
		map.put("f5",KeyEvent.VK_F5);
		map.put("f6",KeyEvent.VK_F6);
		map.put("f7",KeyEvent.VK_F7);
		map.put("f8",KeyEvent.VK_F8);
		map.put("f9",KeyEvent.VK_F9);
		map.put("f10",KeyEvent.VK_F10);
		map.put("f11",KeyEvent.VK_F11);
		map.put("f12",KeyEvent.VK_F12);
		map.put("Up", KeyEvent.VK_UP);
		map.put("Left", KeyEvent.VK_LEFT);
		map.put("Down", KeyEvent.VK_DOWN);
		map.put("Right", KeyEvent.VK_RIGHT);
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
	//ʹ��User32�������λֵת��
	public interface User32 extends StdCallLibrary {
		@SuppressWarnings("deprecation")
		User32 Instance = (User32)Native.loadLibrary("User32", User32.class);
		int MapVirtualKeyA(int key, int type);
	}
	//�˴���winIoʹ�ùؼ�
	public interface WinIo extends Library {	
		@SuppressWarnings("deprecation")	
		WinIo Instance = ( WinIo )Native.loadLibrary("WinIo64", WinIo.class);		
		boolean InitializeWinIo();
		boolean GetPortVal(int portAddr, int pPortVal, int size);
		boolean SetPortVal(int portAddr, int portVal, int size) ;
		void ShutdownWinIo();
	}
	
	
//	public interface GetPath extends Library {
//		@SuppressWarnings("deprecation")
//		GetPath Instance = (GetPath)Native.loadLibrary("lib\\GetPath", GetPath.class);
//		void printPath();
//	}
	
	//�������λֵת��ɨ����
	public static int toScanCode(String key) {
		try {
			Object o = map.get(key);
			if( o == null ) {
				JOptionPane.showMessageDialog(null, "Wrong key: " + key, "ERROR", JOptionPane.ERROR_MESSAGE);
				HomeFrame.fatalError();
			}
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
			 * but it does works!
			 */
			if( !WinIo.Instance.GetPortVal(CONTROL_PORT,val, 1) ) {
				System.err.println("Cannot get the Port");
			}
//			System.out.println("port busy");
		} while (( 0x2 & val )>0);
		// input buffer is full, so we're hanging
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
		
	/**
	 *  0xD2 means the data sent to the DATA_PORT(0x60) will be
	 *  put in OUT_PUT_REGISTER
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
	
	public static boolean initialize() throws SecurityException, IOException {
		
		try {
			if( WinIo.Instance.InitializeWinIo() ) {
				BugLogger.getInstance().log(Level.CONFIG, "WinIo init successfully!");
				return true;	
			}
		} catch(Exception e) {
			// weird situation
			e.printStackTrace();
			BugLogger.getInstance().log(Level.SEVERE, e.getMessage());
			return false;
		} catch (Throwable e) {
			// 这里是因为找不到dll 导致出错 对于JNA只能重启处理
			JOptionPane.showMessageDialog(null, "检测到缺少运行库，将安装后退出，请重新运行，如果反复提示该信息，请联系开发者", "注意", JOptionPane.WARNING_MESSAGE);
			String jarPath = "lib/WinIo64.dll";
			libPath = getLibPath();
			BugLogger.getInstance().info("libpath: " + getLibPath());
			String target = libPath + "\\WinIo64.dll";
			System.out.println("target: " + target);
			try {
				fromJarToFs(jarPath, target);
				BugLogger.getInstance().info("dll path locates at " + target);
				target = libPath + "\\WinIo64.sys";
				jarPath = "lib/WinIo64.sys";
				fromJarToFs(jarPath, target);
				BugLogger.getInstance().info("driver path locates at "+ target);		
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, e1.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
				BugLogger.getInstance().info(e1.getMessage());
				System.exit(-1);
			}
			// FIXME: how to make it? tell the user to restart 
			System.exit(0);
		}
		
		// 回到这里说明driver 路径出错 
		// 这里有个bug 如果之前 dll已经存在路径 那么就会有问题
		BugLogger.getInstance().log(Level.WARNING, "dll found, but cannot find the driver");
		BugLogger.getInstance().log(Level.WARNING, "libpath: " + getLibPath());
		try {
			if( WinIo.Instance.InitializeWinIo() )
				return true;	
			String jarPath = "lib/WinIo64.sys";
			String target = System.getProperty("java.home") + "\\bin\\WinIo64.sys";
			fromJarToFs(jarPath, target);
			BugLogger.getInstance().info("driver path locates at "+ target);
			if( WinIo.Instance.InitializeWinIo() )
				return true;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("cannot initialize WinIo ");
			System.exit(-1);
			
		}
		System.out.println("KeySimulator initialized error");	
		return false;
	}

	public static void shutdown() {
		WinIo.Instance.ShutdownWinIo();
	}
	
	

	private static void fromJarToFs(String jarPath, String filePath) throws IOException {
	      InputStream is = null;
	      OutputStream os = null;
	      try {
	         File file = new File(filePath);
	         if (file.exists()) {
//	            boolean success = file.delete();
//	            if (!success) {
//	               throw new IOException("Could not delete file: " + filePath);
//	            }
	        	 System.out.println("File already exist");
	        	 return;
	         }

	         is = ClassLoader.getSystemClassLoader().getResourceAsStream(jarPath);
	         os = new FileOutputStream(filePath);
	         byte[] buffer = new byte[8192];
	         int bytesRead;
	         while ((bytesRead = is.read(buffer)) != -1) {
	            os.write(buffer, 0, bytesRead);
	         }
	      } catch (Exception ex ) {
	    	  throw new IOException("FromJarToFileSystem could not load DLL: " + jarPath+"\r\n\t\t" + ex.getMessage(), ex);
	      } finally {
	         if (is != null) {
	            is.close();
	         }
	         if (os != null) {
	            os.close();
	         }
	      }
	   }
	
	private static String getLibPath() {
		String ret = null;
		
		
		Map<String, String> map = System.getenv();
		String libdir = map.get("Path");
		
		String [] path = libdir.split(";");
		for( String tmp : path ) {
			if(  (tmp.contains("Java") || tmp.contains("java") || tmp.contains("jre") ) ) {
					ret = tmp;
					break;
			}
		}
		if( ret == null ) 
			ret = path[0];

		System.out.println("libPathSlected: " + ret);

		return ret;
	}
	
	private static String getJavaPath() {
		Map<String, String> map = System.getenv();
		String topdir =  map.get("JAVA_HOME");
		if( topdir == null )
			topdir = System.getProperty("java.home");
		if( topdir == null ) {
	//		String tmpdir = System.getProperty("java.io.tmpdir");
	//		String libdir = System.getProperty("java.library.path");
	//		System.out.println(libdir);
			String libdir = map.get("Path");
			
			String [] path = libdir.split(";");
			for( String tmp : path ) {
				if(  (tmp.contains("Java") || tmp.contains("java"))
					&& (tmp.endsWith("bin") || tmp.endsWith("bin\\"))) {
						topdir = tmp;
						break;
				}
			}
			if( topdir != null)
				System.out.println("topdir: " + topdir);
			else {
				System.out.println("topdir cannot find ENV: ");
				for(Iterator<String> itr = map.keySet().iterator();itr.hasNext();){
		            String key = itr.next();
		            System.out.println(key + "=" + map.get(key));
		        } 
			}
			
		} else {
			topdir = topdir + "\\bin";
		}
		
		return topdir;
		
	}
}


class BugLogger {
	private static Logger logger = null;
	private static FileHandler fileHandler;
	
	private BugLogger() throws SecurityException, IOException {
		 
	}
	
	private static void init() throws IOException {
		logger = Logger.getLogger("LemonLogger");
		logger.setLevel(Level.INFO);
//		logger.setUseParentHandlers(false);
//		String path = System.getProperty("java.io.tmpdir");
		String path = System.getenv("APPDATA");
		if( path == null )
			throw new IOException("cannot get tmp path");
		// System.out.println(System.getenv("APPDATA"));
		path = path + "\\lemonscript\\";
		File file = new File(path);
		System.out.println("program configure path:  "+ path);
		if( !file.exists() )
			file.mkdirs();
		// mkdirs will be wrong
		file = new File(path +  System.currentTimeMillis() + ".log");
		file.createNewFile();
		fileHandler = new FileHandler(file.getAbsolutePath(), true);
		fileHandler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord record) {
				// TODO Auto-generated method stub
				return System.currentTimeMillis() + " : " + record.getMessage() + "\r\n"   ;
			}
		});
		logger.addHandler(fileHandler);
		logger.info("now program started!");
	}
	
	public static Logger getInstance()  {
		if( logger == null ) {
			try {
				init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return logger;
	}
	
}



