import java.awt.BorderLayout;
        import java.awt.Color;
        import java.awt.Container;
        import java.awt.Dimension;
        import java.awt.EventQueue;
        import java.awt.Rectangle;
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

//noah pull test

public class MovePane {

    protected static final Color GREEN = null;

    public static void main(String[] args) {
        new MovePane();
    }

    public MovePane() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("De Oei Oei Vogel");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public enum Direction {

        None, Up, Down, Left, Right;
    }
    //dit stuk maakt het rode vlak
    public class TestPane extends JPanel {

        private JPanel vogel;
        private Timer moveTimer;
        private Direction moveDirection = Direction.None;

        public TestPane() {
            vogel = new JPanel();
            vogel.setBackground(Color.RED);
            vogel.setSize(30, 30);
            vogel.setLocation(385, 770);
            setLayout(new BorderLayout());
            JPanel pool = new JPanel(null);
            pool.add(vogel);
            add(pool);


            //deze movetimer zet de snelheid van de beweging
            moveTimer = new Timer((int) 0.5, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Container parent = vogel.getParent();
                    Rectangle bounds = vogel.getBounds();
                    switch (moveDirection) {
                        case Up:
                            bounds.y--;
                            break;
                        case Down:
                            bounds.y++;
                            break;
                        case Left:
                            bounds.x--;
                            break;
                        case Right:
                            bounds.x++;
                            break;
                    }

                    if (bounds.x < 0) {
                        bounds.x = 0;
                    } else if (bounds.x + bounds.width > parent.getWidth()) {
                        bounds.x = parent.getWidth() - bounds.width;
                    }
                    if (bounds.y < 0) {
                        bounds.y = 0;
                    } else if (bounds.y + bounds.height > parent.getHeight()) {
                        bounds.y = parent.getHeight() - bounds.height;
                    }

                    vogel.setBounds(bounds);

                }
            });
            moveTimer.setInitialDelay(0);
            InputMap im = pool.getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = pool.getActionMap();

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "SpacePressed");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "SpaceReleased");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DownPressed");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "DownReleased");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LeftPressed");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "LeftReleased");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RightPressed");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "RightReleased");

            KeyUpAction keyUpAction = new KeyUpAction();
            am.put("SpaceReleased", new MoveAction(Direction.Down));
            am.put("DownReleased", keyUpAction);
            am.put("LeftReleased", keyUpAction);
            am.put("RightReleased", keyUpAction);

            am.put("SpacePressed", new MoveAction(Direction.Up));
            am.put("DownPressed", new MoveAction(Direction.Down));
            am.put("LeftPressed", new MoveAction(Direction.Left));
            am.put("RightPressed", new MoveAction(Direction.Right));

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1200, 800);
        }

        public class KeyUpAction extends AbstractAction {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveTimer.stop();
                moveDirection = Direction.None;
            }
        }

        public class MoveAction extends AbstractAction {

            private Direction direction;

            public MoveAction(Direction direction) {
                this.direction = direction;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                moveDirection = direction;
                moveTimer.start();
            }
        }
    }
}