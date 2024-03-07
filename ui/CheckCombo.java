package ui;

import com.sun.tools.javac.Main;
import graphing.CreateGraph;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;

class CheckCombo{

    static JPanel currentPanel;
    public static JPanel setComboBox(ArrayList<String> lines, int xAxisIndex, Color color, int maxWidth, int maxHeight) {
        CheckComboStore[] stores = new CheckComboStore[lines.size()-1];
        boolean xAxis = false;
        for (int i = 0; i < lines.size(); i++)
        {
            if (i == xAxisIndex){
                xAxis = true;
                continue;
            }
            stores[xAxis ? i-1 : i] = new CheckComboStore(lines.get(i), CreateGraph.appWindow.enabledLines[xAxis ? i-1 : i]);
        }
        System.out.println("X-axis index: " + xAxisIndex);
        JComboBox combo = new JComboBox(stores);
        combo.setRenderer(new CheckComboRenderer());
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                CheckComboStore store = (CheckComboStore) cb.getSelectedItem();
                CheckComboRenderer ccr = (CheckComboRenderer) cb.getRenderer();
                ccr.checkBox.setSelected((store.state = !store.state));

                int s = cb.getSelectedIndex();
                if(s>= xAxisIndex) s++;
                CreateGraph.appWindow.enabledLines[s] = store.state;
                System.out.print("Setting visiblity of line " + store.id + "/"+ s + " to " + store.state);
                CreateGraph.appWindow.setLineVisibility();
            }
        });
        combo.setPreferredSize(new Dimension(maxWidth,maxHeight));
        if(currentPanel == null){
            System.out.print("Making panel for y-axis combo box");
            currentPanel = new JPanel();
            currentPanel.setBackground(color);
            currentPanel.add(combo);
            return currentPanel;
        }
        System.out.print("Refreshing y-axis combo box");
        currentPanel.remove(0);
        currentPanel.add(combo);
        return currentPanel;
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

class ComboBoxPopupMenuListener implements PopupMenuListener {
    int expandedWidth;
    int expandedHeight;
    int width;
    int height;
    JComboBox comboBox;
    ComboBoxPopupMenuListener(int eW, int eH, int w, int h, JComboBox cb){
        expandedWidth = eW;
        expandedHeight = eH;
        width = w;
        height = h;
        comboBox = cb;
    }
    public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
        System.out.println("Canceled");
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
        //comboBox.setPreferredSize(new Dimension(width, height));
        System.out.println(comboBox.getName() + " visible");
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
        //comboBox.setPreferredSize(new Dimension(expandedWidth, expandedHeight));
        System.out.println(comboBox.getName() + " invisible");
    }
}
