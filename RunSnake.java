package org.cis1200.Snake;

import javax.swing.*;
import java.awt.*;

public class RunSnake implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Snake Game");
        frame.setLocation(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        final GameGrid court = new GameGrid(status);
        frame.add(court, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);

        Dimension buttonSize = new Dimension(90, 30);

        // Instructions
        final JButton instructions = new JButton("Help");
        instructions.addActionListener(e -> court.instructions());
        instructions.setPreferredSize(buttonSize);
        control_panel.add(instructions);

        // Reset
        final JButton reset = new JButton("Restart");
        reset.addActionListener(e -> court.reset());
        reset.setPreferredSize(buttonSize);
        control_panel.add(reset);

        // Save
        final JButton save = new JButton("Save");
        save.addActionListener(e -> court.saveGame());
        save.setPreferredSize(buttonSize);
        control_panel.add(save);

        // Load
        final JButton load = new JButton("Load");
        load.addActionListener(e -> court.loadGame());
        load.setPreferredSize(buttonSize);
        control_panel.add(load);

        frame.pack();
        frame.setVisible(true);
        court.reset();
    }
}