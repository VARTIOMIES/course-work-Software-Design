package fi.tuni.compse110.project.UIView;

import javax.swing.*;

import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CoordinateUI implements ActionListener {

    JFrame frame = new JFrame();
    JLabel welcomeLabel = new JLabel("Select the coordinate to get the forecast");
    JButton BackButton = new JButton("Back");
    JButton SearchButton = new JButton("Search");
    JButton resetButton = new JButton("Reset");

    JLabel minLonLabel = new JLabel("Lon Min");
    JLabel maxLonLabel = new JLabel("Lon Max");
    JLabel minLatLabel = new JLabel("Lat Min");
    JLabel maxLatLabel = new JLabel("Lat Max ");
    JTextField minLonField = new JTextField();
    JTextField maxLonField = new JTextField();
    JTextField minLatField = new JTextField();
    JTextField maxLatField = new JTextField();



    CoordinateUI() {

        welcomeLabel.setBounds(150, 35, 500, 35);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));

        minLatLabel.setBounds(80,75,100,25);
        minLatField.setBounds(80,95,175,25);

        maxLatLabel.setBounds(350,75,100,25);
        maxLatField.setBounds(350,95,175,25);

        minLonLabel.setBounds(80,115,100,25);
        minLonField.setBounds(80,135,175,25);

        maxLonLabel.setBounds(350,115,100,25);
        maxLonField.setBounds(350,135,170,25);

        resetButton.setBounds(330,200,100,25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        SearchButton.setBounds(220,200,100,25);
        SearchButton.setFocusable(false);
        SearchButton.addActionListener(this);

        BackButton.setBounds(80, 200, 100, 25);
        BackButton.setFocusable(false);
        BackButton.addActionListener(this);


        frame.add(welcomeLabel);
        frame.add(BackButton);

        frame.add(minLatLabel);
        frame.add(minLatField);
        frame.add(maxLatLabel);
        frame.add(maxLatField);
        frame.add(minLonLabel);
        frame.add(minLonField);
        frame.add(maxLonLabel);
        frame.add(maxLonField);

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
            minLatField.setText("");
            maxLatField.setText("");
            minLonField.setText("");
            maxLonField.setText("");
        } else if (e.getSource()== SearchButton){
            // add null safety
            Integer minLat = Integer.valueOf(minLatField.getText());
            Integer maxLat = Integer.valueOf(maxLatField.getText());
            Integer minLon = Integer.valueOf(minLonField.getText());
            Integer maxLon = Integer.valueOf(maxLonField.getText());

            //System.out.println(minLat + maxLat + minLon + maxLon);
            frame.dispose();


           
        }
    }
}
