package fi.tuni.compse110.project.UIView;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIFrame implements ActionListener {

    private static JButton PointButton;
    private static JButton RoadButton;
    private static JFrame frame;

    public static void main(String[] args) {

        frame = new JFrame();

        PointButton = new JButton("Select a Point");
        PointButton.setBounds(50,100,150,250);
        PointButton.setFocusable(false);
        PointButton.addActionListener(new UIFrame());

        RoadButton = new JButton("Select a Road");
        RoadButton.setBounds(210,100,150,250);
        RoadButton.setFocusable(false);
        RoadButton.addActionListener(new UIFrame());


        frame.add(PointButton);
        frame.add(RoadButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==PointButton){
            frame.dispose();
            CoordinateUI coordUi = new CoordinateUI();
        } else if (e.getSource()==RoadButton){
            frame.dispose();
            RoadUI roadUi = new RoadUI();
        }

    }
}