package ui;

//import com.sun.istack.internal.NotNull;

import ui.SkillItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 预览界面
 */
public class PreviewFrame extends JDialog {
    private List<SkillItem> _skills = new ArrayList<>();//实际操作的集合
    private List<SkillItem> skills;//传入的集合
    private boolean isSave;
    JComboBox<String> comboBox = new JComboBox<>();

    public List<SkillItem> getSkills() {
        if (isSave) {
            List<SkillItem> t = _skills;
            _skills = skills; 
            skills = t;
            isSave = false;
        }
        return skills;
    }

    public void setSkills(List<SkillItem> skills) {
        isSave = false;
        this.skills = skills;
        comboBox.removeAllItems();
        for (SkillItem skill : skills) {
            comboBox.addItem(skill.getSkillName());
        }
        if (comboBox.getItemCount() != 0)
            comboBox.setSelectedIndex(0);

        _skills.clear();
        _skills.addAll(skills);
        Collections.copy(_skills, skills);
    }

    public PreviewFrame() {
        JPanel jPanel = new JPanel();
        setTitle("预览");
        setSize(500, 300);
        setContentPane(jPanel);
        getContentPane().setBackground(new Color(0x3f9cf8));
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        jPanel.setLayout(new GridLayout(2, 3));
        setModal(true);

        JLabel l1 = new JLabel("技能名");
        l1.setHorizontalAlignment(JLabel.CENTER);
        JLabel l2 = new JLabel("编号");
        l2.setHorizontalAlignment(JLabel.CENTER);
        JTextField textField = new JTextField();

        JPanel p1 = new JPanel(), p2 = new JPanel();
        p1.setLayout(new GridLayout(2, 1));
        p2.setLayout(new GridLayout(2, 1));
        JButton bt_edit = new JButton("修改"),
                bt_add = new JButton("添加新技能"),
                bt_delete = new JButton("删除"),
                bt_save = new JButton("保存");
        p1.add(bt_edit);
        p1.add(bt_add);
        p2.add(bt_delete);
        p2.add(bt_save);

        jPanel.add(l1);
        jPanel.add(l2);
        jPanel.add(p1);
        jPanel.add(comboBox);
        jPanel.add(textField);
        jPanel.add(p2);

        initButtons(bt_edit, bt_add, bt_delete, bt_save);
    }

    private void initButtons(JButton bt1, JButton bt2, JButton bt3, JButton bt4) {
        SkillFrame skillFrame = new SkillFrame();
        bt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(PreviewFrame.this, "未选中任何项目", "错误", JOptionPane.OK_OPTION);
                    return;
                }
                String oldname=_skills.get(comboBox.getSelectedIndex()).getSkillName();
                skillFrame.setSkill(_skills.get(comboBox.getSelectedIndex()));
                skillFrame.setVisible(true);
                if(!oldname.equals(skillFrame.getSkill().getSkillName())){
                    int index=comboBox.getSelectedIndex();
                    comboBox.removeItemAt(index);
                    comboBox.insertItemAt(skillFrame.getSkill().getSkillName(),index);
                    comboBox.setSelectedIndex(index);
                }
            }
        });
        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skillFrame.setSkill(null);
                skillFrame.setVisible(true);
                if (skillFrame.getSkill() != null) {
                    _skills.add(skillFrame.getSkill());
                    comboBox.addItem(skillFrame.getSkill().getSkillName());
                }
            }
        });
        bt3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(PreviewFrame.this, "未选中任何项目", "错误", JOptionPane.OK_OPTION);
                    return;
                }
                _skills.remove(comboBox.getSelectedIndex());
                comboBox.removeItemAt(comboBox.getSelectedIndex());
            }
        });
        bt4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSave = true;
                setVisible(false);
            }
        });
    }
}
