import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600 ;
    static final int SCREEN_HEIGHT = 600 ;
    static final int UNIT_SIZE = 25 ;
    static final int APPLE_SIZE = 25 ;

    static final int DELAY = 75 ;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE ;
    final int x [] = new int [GAME_UNITS];
    final int y [] = new int [GAME_UNITS];
    int snackLength = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    Timer timer;
    boolean running = false;
    Random random;

    GamePanel () {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame () {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent (Graphics g) {
        super.paintComponents(g);
        draw(g);
    }

    public void draw (Graphics g) {

        if (running) {
        for (int i = 0 ; i < SCREEN_HEIGHT / UNIT_SIZE; i ++) {
            g.drawLine(i * UNIT_SIZE, 0 , i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        g.setColor(Color.red);
        g.fillOval(appleX,appleY, APPLE_SIZE, APPLE_SIZE );

        for (int i = 0; i < snackLength;  i ++) {
            if (i == 0) {
                g.setColor(Color.green);
                g.fillRect(x [i], y [i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(Color.CYAN);
                g.setColor(new Color (random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                g.fillRect(x [i], y [i], UNIT_SIZE, UNIT_SIZE);
            }
        }

    } else {
            gameOver(g);
        }

    }

    public void newApple () {
        appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE))  * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))  * UNIT_SIZE;
    }

    public void move () {
        for (int i = snackLength; i > 0; i -- ) {
            x [i] = x [i -1];
            y [i] = y [i -1];
        }

        switch (direction) {
            case 'U':
                y [0] = y [0] - UNIT_SIZE;
                break;
            case 'D':
                y [0] = y [0] + UNIT_SIZE;
                break;
            case 'L':
                x [0] = x [0] - UNIT_SIZE;
                break;
            case 'R':
                x [0] = x [0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple () {
        if (( x [0] == appleX) && (y [0] == appleY)) {
            snackLength ++;
            applesEaten ++ ;
            newApple();
        }
    }

    public void checkCollisions () {
     // если змея съела сама себя
        for (int i = snackLength; i > 0; i --) {
            if ( (x [0] == x [i] ) && (y[0] == y [i]) ) {
                        running = false;
            }
        }

        // левый край
        if ( x [0] < 0 ) {
            running = false;
        }

        // правый край
        if ( x [0] > SCREEN_WIDTH ) {
            running = false;
        }

        // верхний край
        if ( y [0] < 0 ) {
            running = false;
        }

        // нижний край
        if ( y [0] > SCREEN_HEIGHT ) {
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }

    public void gameOver (Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed (KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT :
                    if (direction != 'R') {
                        direction = 'L';
                    } break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    } break;
                case KeyEvent.VK_UP :
                    if (direction != 'D') {
                        direction = 'U';
                    } break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    } break;
            }
        }
    }
}
