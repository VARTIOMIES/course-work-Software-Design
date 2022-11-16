package fi.tuni.compse110.project.UIView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RoadUI implements ActionListener{
    JFrame frame = new JFrame();
    JLabel welcomeLabel = new JLabel("Select the Road Number to get the forecast");
    JButton BackButton = new JButton("Back");
    JButton SearchButton = new JButton("Search");
    JButton resetButton = new JButton("Reset");
    JLabel RoadNumberLabel = new JLabel("Input the Road Number ");
    JTextField RoadNumberField = new JTextField();

    RoadUI(){
        welcomeLabel.setBounds(150, 35, 900, 35);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));

        RoadNumberLabel.setBounds(200,75,250,25);
        RoadNumberField.setBounds(200,95,175,25);

        resetButton.setBounds(100,150,100,25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        SearchButton.setBounds(220,150,100,25);
        SearchButton.setFocusable(false);
        SearchButton.addActionListener(this);

        BackButton.setBounds(320, 150, 100, 25);
        BackButton.setFocusable(false);
        BackButton.addActionListener(this);


        frame.add(welcomeLabel);
        frame.add(RoadNumberLabel);
        frame.add(RoadNumberField);
        frame.add(BackButton);
        frame.add(resetButton);
        frame.add(SearchButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 900);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == BackButton) {
            frame.dispose();
            UIFrame mainFrame = new UIFrame();
        } else if (e.getSource()== resetButton) {
            RoadNumberField.setText("");
        } else if (e.getSource()== SearchButton){
            Integer roadNumber = Integer.valueOf(RoadNumberField.getText());

            //System.out.println("Guys, this is a highway to hell number" + roadNumber);
        }
    }
    }

