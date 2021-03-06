package oeioeivogel.noah;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MoveCircle {

    public static void main(String[] args) {
        new MoveCircle();
    }

    public MoveCircle() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        private int xDelta = 0;
        private int yDelta = 0;
        private int keyPressCount = 0;
        private Timer repaintTimer;
        private int xPos = 0;
        private int yPos = 0;
        private int radius = 10;

        public TestPane() {
            InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "pressed.left");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "pressed.right");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "released.left");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "released.right");


            am.put("pressed.left", new MoveAction(-2, true));
            am.put("pressed.right", new MoveAction(2, true));
            am.put("released.left", new MoveAction(0, false));
            am.put("released.right", new MoveAction(0, false));

            repaintTimer = new Timer(40, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    xPos += xDelta;
                    yPos += yDelta;
                    if (xPos < 0) {
                        xPos = 0;
                    } else if (xPos + radius > getWidth()) {
                        xPos = getWidth() - radius;
                    }
                    else if (yPos < 0) {
                        yPos = 0;
                    }
                    else if (yPos + radius > getHeight()) {
                        yPos = getHeight() - radius;
                    }
                    repaint();
                }
            });
            repaintTimer.setInitialDelay(0);
            repaintTimer.setRepeats(true);
            repaintTimer.setCoalesce(true);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 800);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.RED);
            g2d.drawOval(xPos, 0, radius, radius);
            g2d.dispose();
        }

        public class MoveAction extends AbstractAction {

            private int direction;
            private boolean keyDown;

            public MoveAction(int direction, boolean down) {
                this.direction = direction;
                keyDown = down;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                xDelta = direction;
                yDelta = direction;
                if (keyDown) {
                    if (!repaintTimer.isRunning()) {
                        repaintTimer.start();
                    }
                } else {
                    repaintTimer.stop();
                }
            }
        }
    }
}

