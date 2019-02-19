package ui;

import org.jdom2.Element;

import skillscript.Skill;
import skillscript.SkillManager;

/**
 * 技能属性
 */
class SkillItem {
    private String skillName;//技能名
    private int skillLength;//技能长度
    private String key;//按键
    private String delay;//延迟
    int [] tapDelay;
    private int cd;//cd
    private int priority;//优先级

    public SkillItem() {
    	
    }
    
	public SkillItem(String skillName, String key, String delay, int cd, int priority) {
		super();
		this.skillName = skillName;
		this.key = key;
		this.delay = delay;
		this.cd = cd;
		this.priority = priority;
		setDelay(delay);
	}

	public Element saveSkill() {
		Element s = new Element("skill");
		
		s.addContent(new Element("name").setText(skillName));
		s.addContent(new Element("length").setText(""+key.length()));
		s.addContent(new Element("keystroke").setText(key));
		s.addContent(new Element("cd").setText( ""+ cd ));
//		s.addContent(new Element("tapdelay").setText(""));
		
//		StringBuilder sb = new StringBuilder();
//		for(int i : tapDelay) {
//			sb.append(i);
//			sb.append(",");
//		}
//		sb.deleteCharAt(sb.length()-1);
		s.addContent(new Element("tapdelay").setText(delay));
		s.addContent(new Element("priority").setText(priority+""));
			
		return s;
	}
	
	// new Skill().load(x);


	public static SkillItem loadSkill( Element skill) {
		int cd; /* ms */
		int length;
//		int [] tapDelay;
		String delays;
		String keyStroke;
		String name;
		int priority;
		
		name = skill.getChildText("name");
		cd = Integer.parseInt(skill.getChildText("cd"));
		keyStroke = skill.getChildText("keystroke");
		length = Integer.parseInt(skill.getChildText("length"));
//		tapDelay = new int[length];
		delays = skill.getChildText("tapdelay");
		priority = Integer.parseInt(skill.getChildText("priority"));
		
		if( /*length != delays.length() ||*/ keyStroke.length() != length ) {
			System.err.println("Parse error, " + "skill_name:" + name);
			System.exit(-1);
		}
//		int i = 0;
//		for( String tmp : delays ) {
//			tapDelay[i++] = Integer.parseInt(tmp);
//		}
//		
		return new SkillItem(name, keyStroke, delays, cd, priority);
	}
    
    public int[] getTapDelay() {
		return tapDelay;
	}

	public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getSkillLength() {
        return skillLength;
    }

    public void setSkillLength(int skillLength) {
        this.skillLength = skillLength;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDelay() {
        return delay;
    }

    /**
     *	 保证： 解析的字符串一定是符合规定的。
     * 
     * @param delay
     */
    public void setDelay(String delay) {
        this.delay = delay;
        int i = 0;
        String [] delays;
        
        delays = delay.split(",");
        tapDelay = new int[delays.length];
        for( String tmp : delays ) {
			tapDelay[i++] = Integer.parseInt(tmp);
		}
    }

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
