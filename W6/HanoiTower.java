import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

class CONS {
    public static int frameW = 1200;
    public static int frameH = 700;
    public static int rodX = frameW/4;
    public static int rodY = 50;
    public static int rodW = 10;
    public static int rodH = 400;
    public static int diskW = 250;
    public static int diskH = 10;
    public static int diskSpace = 5;
    public static int diskStep = 20;
    public static int sleep = 500;
    public static boolean play = false;

}

class Disks {
    public static int[] pos;
    public static int num;
}



public class HanoiTower {
    private Gui gui;
    private int countSteps = 0;
    public HanoiTower() {
        gui = new Gui();
        gui.setup(this);
        sleep();
    }

    public void sleep() {
        try {
            Thread.sleep(CONS.sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void playWait() {
        while(!CONS.play) sleep();
    }

    public void solveHanoi(int n, int source, int aux, int des) {
        if (n == 1) {
            playWait();
            move(n, source, des);
        } else {
            playWait();
            solveHanoi(n - 1, source, des, aux);
            move(n, source, des);
            solveHanoi(n - 1, aux, source, des);
        }
    }

    public void solveRestricted(int n, int source, int aux, int des) {
        if (n == 0)
            return;

        if (Math.abs(source - des) == 1) {
            solveRestricted(n - 1, source, des, aux);

            playWait();
            move(n, source, des);

            solveRestricted(n - 1, aux, source, des);
        } else {
            solveRestricted(n - 1, source, aux, des);

            playWait();
            move(n, source, aux);

            solveRestricted(n-1, des, aux, source);

            playWait();
            move(n, aux, des);

            solveRestricted(n-1, source, aux, des);
        }
    }

    public void move(int n, int source, int des) {
        //System.out.println(source + " -> " + des);
        countSteps++;
        Disks.pos[n-1] = des;
        gui.redraw();
        sleep();
    }

    public static void run() {
        HanoiTower hanoiTower = new HanoiTower();
        hanoiTower.solveRestricted(Disks.num,0, 1, 2 );

        System.out.println("Solved in " + hanoiTower.countSteps + " steps!");
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter number of disks:");

        Disks.num = input.nextInt();
        Disks.pos = new int[Disks.num];

        for (int i = 0; i < Disks.num; i++) {
            Disks.pos[i] = 0;
        }

        run();
    }
}

class Gui {
    private JFrame frame;
    private JPanel panel, controlPanel;
    private JButton playButton, resetButton;

    private HanoiTower hanoiTower;
    
    public void redraw() {
        panel.revalidate();
        panel.repaint();
    }
    public void setup(HanoiTower hanoiTower) {
        this.hanoiTower = hanoiTower;

        frame = new JFrame("Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(CONS.frameW, CONS.frameH);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc  = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        panel = new HanoiPanel();
        panel.setPreferredSize(new Dimension(CONS.frameW, 600));
        frame.add(panel, gbc);

        controlPanel = new JPanel();
        //controlPanel.setLayout(new );

        gbc.gridx = 0;
        gbc.gridy = 1;
        playButton = new JButton("Play/Pause");
        resetButton = new JButton("Reset");

        playButton.setSize(50,20);
        resetButton.setSize(50,20);

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CONS.play = !CONS.play;
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CONS.play = false;
                for (int i = 0; i < Disks.num; i++) {
                    Disks.pos[i] = 0;
                }
                redraw();

            }
        });

        controlPanel.add(playButton);
        controlPanel.add(resetButton);


        frame.add(controlPanel, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


class HanoiPanel extends JPanel {

    private void drawRods(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(new Color(150, 150, 150));

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        // xcord, ycord, width, length
        for (int i = 0; i < 3; i ++) {
            g2d.fillRect(CONS.rodX + CONS.frameW/4 * i, CONS.rodY, CONS.rodW, CONS.rodH);
        }

        //g2d.fillRoundRect(250, 20, 70, 60, 25, 25);
    }

    private void drawDisks(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(new Color(250, 0, 0));

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        int[] rodsSize = new int[3];
        for (int i = 0; i < 3; ++i) {
            rodsSize[i] = 0;
        }

        for (int i = Disks.num - 1; i >= 0; i--){
            int rodNum = Disks.pos[i];
            int l = CONS.diskW - CONS.diskStep*(Disks.num -1 - i);
            int x = CONS.rodX - l/2 + CONS.rodW/2 + CONS.rodX*rodNum;
            int y = CONS.rodY + CONS.rodH - CONS.diskH*rodsSize[rodNum] - CONS.diskSpace*(rodsSize[rodNum]);
            g2d.fillRect(x, y, l, CONS.diskH);
            rodsSize[rodNum] += 1;
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        drawRods(g);
        drawDisks(g);
    }
}