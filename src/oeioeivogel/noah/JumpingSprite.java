package oeioeivogel.noah;

import java.awt.BorderLayout;
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

public class JumpingSprite {

    public static void main(String[] args) {
        new JumpingSprite();
    }

    public JumpingSprite() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static class TestPane extends JPanel {

        protected static final int SPRITE_HEIGHT = 10;
        protected static final int SPRITE_WIDTH = 10;
        private float vDelta; // The vertical detla...
        private float rbDelta; // Rebound delta...
        private float rbDegDelta; // The amount the rebound is degradation...
        private int yPos; // The vertical position...
        private float gDelta; // Gravity, how much the vDelta will be reduced by over time...
        private Timer engine;
        private boolean bounce = false;

        public TestPane() {

            yPos = getPreferredSize().height - SPRITE_HEIGHT;
            vDelta = 0;
            gDelta = 0.25f;
            // This is how much the re-bound will degrade on each cycle...
            rbDegDelta = 5f;

            InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "jump");
            am.put("jump", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Can only bound when we're actually on the ground...
                    // You might want to add fudge factor here so that the
                    // sprite can be within a given number of pixels in order to
                    // jump again...
                    if (yPos + SPRITE_HEIGHT == getHeight()) {
                        vDelta = -8;
                        rbDelta = vDelta;
                        bounce = true;
                    }
                }
            });

            engine = new Timer(40, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int height = getHeight();
                    // No point if we've not been sized...
                    if (height > 0) {
                        // Are we bouncing...
                        if (bounce) {
                            // Add the vDelta to the yPos
                            // vDelta may be postive or negative, allowing
                            // for both up and down movement...
                            yPos += vDelta;
                            // Add the gravity to the vDelta, this will slow down
                            // the upward movement and speed up the downward movement...
                            // You may wish to place a max speed to this
                            vDelta += gDelta;
                            // If the sprite is not on the ground...
                            if (yPos + SPRITE_HEIGHT >= height) {
                                // Seat the sprite on the ground
                                yPos = height - SPRITE_HEIGHT;
                                // If the re-bound delta is 0 or more then we've stopped
                                // bouncing...
                                if (rbDelta >= 0) {
                                    // Stop bouncing...
                                    bounce = false;
                                } else {
                                    // Add the re-bound degregation delta to the re-bound delta
                                    rbDelta += rbDegDelta;
                                    // Set the vDelta...
                                    vDelta = rbDelta;
                                }
                            }
                        }
                    }
                    repaint();
                }
            });
            engine.start();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth() - 1;
            int xPos = (width - SPRITE_WIDTH) / 2;
            g2d.drawOval(xPos, yPos, SPRITE_WIDTH, SPRITE_HEIGHT);
            g2d.dispose();
        }
    }
}
