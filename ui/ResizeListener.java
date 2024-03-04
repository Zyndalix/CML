package ui;

import com.sun.tools.javac.Main;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.*;

public class ResizeListener implements ComponentListener {
    JFrameWindow jFrameWindow;
    public ResizeListener(JFrameWindow j){
        jFrameWindow = j;
    }
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        System.out.print(e.getComponent().getClass().getName() + " --- Resized ");
    }

    public void componentShown(ComponentEvent e) {

    }
}
