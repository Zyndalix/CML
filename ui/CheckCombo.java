package ui;

import com.sun.tools.javac.Main;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
class CheckCombo implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        CheckComboStore store = (CheckComboStore) cb.getSelectedItem();
        CheckComboRenderer ccr = (CheckComboRenderer) cb.getRenderer();
        ccr.checkBox.setSelected((store.state = !store.state));
    }

    public JPanel setComboBox(ArrayList<String> lines, int xAxisIndex) {
        CheckComboStore[] stores = new CheckComboStore[lines.size()-1];
        boolean xAxis = false;
        for (int i = 0; i < lines.size(); i++)
        {
            if (i == xAxisIndex){
                xAxis = true;
                continue;
            }
            stores[xAxis ? i-1 : i] = new CheckComboStore(lines.get(i), true);
        }
        JComboBox combo = new JComboBox(stores);
        combo.setRenderer(new CheckComboRenderer());
        combo.addActionListener(this);
        JPanel panel = new JPanel();
        panel.add(combo);
        return panel;
    }
}

class CheckComboRenderer implements ListCellRenderer {
    JCheckBox checkBox;

    public CheckComboRenderer() {
        checkBox = new JCheckBox();
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        CheckComboStore store = (CheckComboStore) value;
        checkBox.setText(store.id);
        checkBox.setSelected((boolean) (store.state).booleanValue());
        checkBox.setBackground(isSelected ? Color.red : Color.white);
        checkBox.setForeground(isSelected ? Color.white : Color.black);
        return checkBox;
    }
}

class CheckComboStore {
    String id;
    Boolean state;

    public CheckComboStore(String id, Boolean state) {
        this.id = id;
        this.state = state;
    }
}
