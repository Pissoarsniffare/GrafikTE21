import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class episkkod extends Canvas implements Runnable {
    private BufferStrategy bs;

    private boolean running = false;
    private Thread thread;

    private int WIDTH = 600;
    private int HEIGHT = 400;
    private int paddelX = 260;
    private int paddelY = 350;

    private int paddelVX = 0;

    private int bollX = 290;
    private int bollY = 340;

    private int bollVX = 0;

    private int bollVY = 0;

    private int brickX = 290;

    private int brickY = 30;

    public episkkod() {
        setSize(WIDTH, HEIGHT);
        JFrame frame = new JFrame();
        frame.add(this);
        frame.addKeyListener(new MyKeyListener());
        this.addMouseMotionListener(new MyMouseMotionListener());
        this.addMouseListener(new MyMouseListener());
        requestFocus();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void render() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // Rita ut den nya bilden
        draw(g);

        g.dispose();
        bs.show();
    }

    public void draw(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLUE);
        g.fillRect(paddelX, paddelY, 80, 10);
        g.setColor(Color.RED);
        g.fillOval(bollX,bollY,10,10);
        g.setColor(Color.green);
        g.fillRect(brickX,brickY,50,20);
    }


    private void update() {
        paddelX += paddelVX;
        bollX += bollVX;
        bollY += bollVY;
        if (bollX < 0 || bollX > WIDTH) {
            bollVX = -bollVX;
        }

        if (bollY <= brickY + 20 && bollY >= brickY && bollX >= brickX && bollX <= brickX + 40) {
            bollVY = -bollVY;
            brickY = -100;
        }

        if (bollY < 0 || bollY > HEIGHT) {
            bollVY = -bollVY;
        }
        if (bollY >= paddelY - 10 && bollY <= paddelY && bollX >= paddelX && bollX <= paddelX + 80) {
            bollVY = -bollVY;
        }


    }




    public static void main(String[] args) {
        episkkod minGrafik = new episkkod();
        minGrafik.start();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        double ns = 1000000000.0 / 25.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                // Uppdatera koordinaterna
                update();
                // Rita ut bilden med updaterad data
                render();
                delta--;
            }
        }
        stop();
    }

    public class MyMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    public class MyMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    public class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (bollVX == 0) {
                bollVX = -10;
                bollVY = -10;
            }

            if (e.getKeyChar() == 'd') {
                paddelVX = 10;


            }
            if (e.getKeyChar() == 'a') {
                paddelVX = -10;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            paddelVX = 0;
        }
    }
}