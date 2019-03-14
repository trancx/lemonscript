package ui;

import javax.swing.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import factory.Machine;
import keysimulator.KeySimulator;
import ui.SkillItem;
import skillscript.Skill;
import skillscript.SkillMachine;
import skillscript.SkillManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
  BIGCHANGES: 改为由UI解析xml文件
 */
public class HomeFrame extends JFrame {
    private JFileChooser fileChooser;//文件选择器
    private File defaultPath;//储存默认路径
    List<SkillItem> skills;//技能集合
    private boolean isRunning;
    private boolean isChanged; 
    private boolean isSaved;
    private SkillMachine machine;
	private SkillManager sm;
	private JButton bt1, bt2, bt3, bt4;
	
	public static final int GLOBAL_KEY_SWITCH = 7;
	public static HomeFrame myself;
	
    /**
                *    初始化
     * @throws IOException 
     * @throws SecurityException 
     * @throws HeadlessException 
     */
    public HomeFrame(SkillMachine machine, SkillManager sm) throws HeadlessException, SecurityException, IOException {
        defaultPath=new File(".");
        fileChooser=new JFileChooser(defaultPath);
        skills = new ArrayList<>();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        setTitle("吃柠檬");
        setSize(300, 300);
        getContentPane().setBackground(Color.CYAN);
        setLayout(new GridLayout(4, 1));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
   //     WindowConstants.EXIT_ON_CLOSE
        
        bt1 = new JButton();
        bt2 = new JButton();
        bt3 = new JButton();
        bt4 = new JButton();
        initButtons(bt1, bt2, bt3, bt4);

        add(bt1);
        add(bt2);
        add(bt3);
        add(bt4);
        
        isRunning = false;
        isSaved = true;
        this.machine = machine;
        this.sm = sm;
        
        initWinio();
        
        initShortcut();   // 初始化快捷键
        myself = this;
  
    }

    public static void fatalError() {
    	if( myself != null )
    		myself.destroy();
    	System.exit(-1);
    	
    }
    
    public void update() { 	
    	isChanged = true;
    	isSaved = false;
    }
    
    private void destroy() {
    	JIntellitype.getInstance().unregisterHotKey(GLOBAL_KEY_SWITCH);  	
    	KeySimulator.shutdown();
    }
    
    private void initWinio() throws HeadlessException, SecurityException, IOException {
    	if( !KeySimulator.initialize() ) {
    		JOptionPane.showMessageDialog(null, "初始化Winio出错", "error", JOptionPane.ERROR_MESSAGE);
    		System.exit(-1);
    	}
    }
    
    private void initShortcut() {
    	JIntellitype.getInstance().registerHotKey(
    			GLOBAL_KEY_SWITCH, 0, KeyEvent.VK_BACK_QUOTE);
    	JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			
			@Override
			public void onHotKey(int id ) {
				switch (id) {
				case GLOBAL_KEY_SWITCH:
					isRunning = !isRunning;
					// FIXME: 这里和 bt4 的处理耦合了，没有复用
					if( isRunning ) {
						if( isChanged ) {
							sm.clear();
							addSkills();
							isChanged = false;
						}
						machine.setState(Machine.AUTOMATION_RUNNING);
						bt4.setText("停止");
					} else {
						machine.setState(Machine.AUTOMATION_SLEEPING);
						HomeFrame.this.bt4.setText("运行");
					}
					break;
				default:
					break;
				}
				
			}
		});
    	
    }
    
    private void addSkills() {
    	Skill skill = null;
		 for( SkillItem sItem : skills ) {
			 skill = new Skill(sm, sItem.getCd(), 
					 sItem.getTapDelay(), sItem.getKey().split(","));
			 skill.setName(sItem.getSkillName());
			 sm.addTask(skill);
			 System.out.println(skill.getName() + " is added!");
		 }
    }
    
    /**
     * @param bt1 按钮1
     * @param bt2 按钮2
     * @param bt3 按钮3
     * 初始化按钮点击事件
     */
    private void initButtons(JButton bt1, JButton bt2, JButton bt3, JButton bt4) {
        bt1.setText("预览");
        bt2.setText("更改配置");
        bt3.setText("保存配置");
        bt4.setText("Run/Stop");
        
        PreviewFrame previewFrame  = new PreviewFrame();
        bt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               previewFrame.setSkills(skills);//为预览界面设置技能集合
               previewFrame.setVisible(true);//显示预览界面
               List<SkillItem> _skills = previewFrame.getSkills();//获取预览界面操作后的技能集合
               if(  skills != _skills ) {
            	   HomeFrame.this.update();
            	   skills = _skills;
               }             
            }
        });
  
        // load another configure
        // FIXED: 文件没有确定的时候，不应该执行后面的几步
        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int ret;
            	fileChooser.setSelectedFile(defaultPath);
                ret = fileChooser.showOpenDialog(HomeFrame.this);
                
                if( ret == JFileChooser.APPROVE_OPTION ) {
	                File file = fileChooser.getSelectedFile();//file是选中的文件
	                defaultPath = file;
	                HomeFrame.this.setTitle(file.getName().replace(".xml", ""));
	                SAXBuilder builder = new SAXBuilder();
	        		try {
	        			Document doc = builder.build(file);
	        			Element ss = doc.getRootElement();
	        			
	        			List<Element> skillList = ss.getChild("skills").getChildren("skill");
	        			for( Iterator<Element> iter = skillList.iterator(); iter.hasNext();) {
	        				Element s = (Element)iter.next();
	        				skills.add(SkillItem.loadSkill(s));		
	        			}
	        			
	        		} catch (JDOMException ee) {
	        			// TODO Auto-generated catch block
	        			ee.printStackTrace();
	        		} catch (IOException ee) {
	        			// TODO Auto-generated catch block
	        			ee.printStackTrace();
	        		}
	                
	        		isChanged = true;         
                }
            }
        });

        // save configure
        bt3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int ret;
            	fileChooser.setSelectedFile(defaultPath);
                ret = fileChooser.showSaveDialog(HomeFrame.this);            
                if( ret == JFileChooser.APPROVE_OPTION ) {
	                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());//后缀名自己加
	                defaultPath = file;
	                //待完成
	               // Element skill = test.save();
	                Element ss = new Element("skillscript");
	                ss.addContent(new Element("skillstotal").setText(""+ skills.size()));
	                ss.addContent(new Element("name").setText(file.getName().replace(".xml", "")));
	                Element skill = new Element("skills");
	                for( SkillItem sItem : skills ) {
	                	skill.addContent(sItem.saveSkill());
	                }
	                ss.addContent(skill);
	        		Document doc = new Document(ss);//�ðѸ��ڵ㴴��һ��Document
	        		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
	        		try {
	        			xmlOut.output(doc, new FileOutputStream(file));
	        		} catch (FileNotFoundException ee) {
	        			
	        			ee.printStackTrace();
	        		} catch (IOException ee) {
	
	        			ee.printStackTrace();
	        		}
	        		isSaved = true;
	            }
            }
        });
        
        
        /** FIXME
         *  UI的skillitem在用户输入的时候就会判断数据的合法性，所以后面的处理不需要
         	纠错，因为不好查找，还有可能是用户加载了非法的配置，这个需要处理
         */
        bt4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if( isRunning ) {
					machine.setState(Machine.AUTOMATION_SLEEPING);
					isRunning = false;
//					System.out.println("stopped");
					bt4.setText("运行");
				} else { // 停止 或者 从未开始
					if( isChanged ) {
						sm.clear();	
						// 添加新技能
//						Skill skill = null;
//						 for( SkillItem sItem : skills ) {
//							 skill = new Skill(sm, sItem.getCd(), 
//									 sItem.getTapDelay(), sItem.getKey());
//							 skill.setName(sItem.getSkillName());
//							 sm.addTask(skill);
//							 System.out.println(skill.getName() + " is added!");
//						 }
						addSkills();
						isChanged = false;
					}
					machine.setState(Machine.AUTOMATION_RUNNING);
					isRunning = true;
					bt4.setText("暂停");
				}			
			} // bt4
		});
        
    }
    
	protected void processWindowEvent(WindowEvent e) {
		// TODO Auto-generated method stub
		super.processWindowEvent(e);
		if(e.getID() == WindowEvent.WINDOW_CLOSING)
	       {
//	        	System.out.println("Closing");
	        	if( !isSaved ) {
	        		int ret = JOptionPane.showConfirmDialog(null, "你当前的配置还没有保存，确定要退出吗");
	        		if( ret != JOptionPane.YES_OPTION ) {
	        			return;
	        		} 
	        	} 
		        destroy();
	        	System.exit(0); 	
	       }
	}
}
