import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

public class GrafikMedCollision extends Canvas implements Runnable{
    private BufferStrategy bs;

    private boolean running = false;
    private Thread thread;

    private int x = 0;
    private int y = 0;
    private Rectangle hitbox = new Rectangle(x,y,30,30);

    private int targetX = (int) (Math.random()*560);
    private int targetY = (int) (Math.random()*360);
    private Rectangle target = new Rectangle(targetX, targetY, 40,40);
    private Color targetColor = Color.green;

    public GrafikMedCollision() {
        setSize(600,400);
        JFrame frame = new JFrame();
        frame.add(this);
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
        g.clearRect(0,0,getWidth(),getHeight());
        g.setColor(targetColor);
        g.fillRect(target.x, target.y, target.width, target.height);
        g.setColor(new Color(0xBE5817));
        g.fillOval(hitbox.x,hitbox.y,hitbox.width,hitbox.height);
    }

    private void update() {
        if (target.intersects(hitbox)) {
            target.x = (int) (Math.random()*560);
            target.y = (int) (Math.random()*360);
        }
    }

    public static void main(String[] args) {
        GrafikMedCollision minGrafik = new GrafikMedCollision();
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

            while(delta >= 1) {
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
            hitbox.x = e.getX();
            hitbox.y = e.getY();

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
            targetColor = Color.green;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            targetColor = Color.red;
        }
    }
}

