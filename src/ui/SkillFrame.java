package ui;

//import com.sun.istack.internal.Nullable;

import javax.swing.*;

import ui.SkillItem;

import java.awt.*;
 
/**
 * 技能界面
 */
public class SkillFrame extends JDialog {
    private JLabel[] labels;
    private JTextField[] textFields;
    private SkillItem skill;

    public SkillFrame() {
        JPanel panel = new JPanel();
        setTitle("技能");
        setSize(500, 300);
        setContentPane(panel);
        getContentPane().setBackground(new Color(0x3f9cf8));
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        panel.setLayout(new GridLayout(7, 2));
        setModal(true);

        labels = new JLabel[] {
                new JLabel("技能名"),
                new JLabel("技能长度"),
                new JLabel("按键"),
                new JLabel("延迟(ms)"),
                new JLabel("cd(ms)"),
                new JLabel("优先级")};
        textFields = new JTextField[6];
        for (int i = 0; i < 6; i++) {
            textFields[i] = new JTextField();
            textFields[i].setHorizontalAlignment(JTextField.CENTER);
        }
        for (int i = 0; i < 6; i++) {
            labels[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(labels[i]);
            panel.add(textFields[i]);
        }

        JButton yes = new JButton("保存"), no = new JButton("取消");
        panel.add(yes);
        panel.add(no);
        no.addActionListener(e -> setVisible(false));
        yes.addActionListener(e -> {
            if(skill==null)
            	skill=new SkillItem();
            skill.setSkillName(textFields[0].getText());
            skill.setSkillLength(Integer.parseInt(textFields[1].getText()));
            skill.setKey(textFields[2].getText());
            skill.setDelay(textFields[3].getText());
            skill.setCd(Integer.parseInt(textFields[4].getText()));
            skill.setPriority(Integer.parseInt(textFields[5].getText()));
            setVisible(false);
        });
    }

    public SkillItem getSkill() {
        return skill;
    }

    public void setSkill(SkillItem skill) {
        this.skill = skill;
        if (null != skill) {
            textFields[0].setText(skill.getSkillName());
            textFields[1].setText(skill.getSkillLength() + "");
            textFields[2].setText(skill.getKey());
            textFields[3].setText(skill.getDelay() + "");
            textFields[4].setText(skill.getCd() + "");
            textFields[5].setText(skill.getPriority() + "");
        } else {
            for (int i = 0; i < 6; i++) {
                textFields[i].setText("");
            }
        }
    }
}
