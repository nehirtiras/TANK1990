package core;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.List;


import map.GameMap;
//import powerup.PowerUp;
//import powerup.ShovelPowerUp;
import entity.Bullet;
import entity.FastTank;
import entity.PlayerTank;
import entity.PowerTank;
import entity.BasicTank;
import entity.ArmorTank;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 17;
    public final int maxScreenRow = 17;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    private GameMap gameMap;
    private PlayerTank player;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<BasicTank> basicTanks = new ArrayList<>();
    private ArrayList<FastTank> fastTanks = new ArrayList<>();
    private ArrayList<PowerTank> powerTanks = new ArrayList<>();
    private ArrayList<ArmorTank> armorTanks = new ArrayList<>();
    private ArrayList<Point> enemySpawnPoints;
    private Random random = new Random();

    private boolean gameOver = false;
    private int totalEnemiesKilled = 0;
    private final int maxEnemies = 20;
    private boolean isPaused = false;

    private JButton pauseButton;
    private ImageIcon playIcon, stopIcon;

    private boolean showStartScreen = true;
    private Image startImage;


    private int score = 0;
    private final int basicTankScore = 100;
    private final int fastTankScore = 150;
    private final int powerTankScore = 200;
    private final int armorTankScore = 300;

    private int killedBasic = 0;
    private int killedFast = 0;
    private int killedPower = 0;
    private int killedArmor = 0;

    private boolean gameWon = false;
//    private List<PowerUp> powerUps = new ArrayList<>();

    int FPS = 60;
    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocusInWindow();
        init();
    }
    
    private Clip clip;
//arka plan müziği
    private void playMusic(String filePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//oyun başlangıç ayarları
    private void init() {
    	
    	startImage = new ImageIcon("res/ui/startscreen.png").getImage();

    	
        gameMap = new GameMap(tileSize);
        enemySpawnPoints = gameMap.getEnemySpawnPoints();

        player = new PlayerTank(gameMap.getPlayerStartX(), gameMap.getPlayerStartY(), gameMap);

        for (Point p : gameMap.getBasicTankSpawns()) basicTanks.add(new BasicTank(p.x, p.y, gameMap));
        for (Point p : gameMap.getFastTankSpawns()) fastTanks.add(new FastTank(p.x, p.y, gameMap));
        for (Point p : gameMap.getPowerTankSpawns()) powerTanks.add(new PowerTank(p.x, p.y, gameMap));
        for (Point p : gameMap.getArmorTankSpawns()) armorTanks.add(new ArmorTank(p.x, p.y, gameMap));

        gameThread = new Thread(this);
        gameThread.start();
        playIcon = new ImageIcon("res/ui/play.png");
        stopIcon = new ImageIcon("res/ui/stop.png");

        pauseButton = new JButton(stopIcon);
        pauseButton.setBounds(700, 20, 64, 64);
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusPainted(false);

        pauseButton.addActionListener(e -> {
            isPaused = !isPaused;
            pauseButton.setIcon(isPaused ? playIcon : stopIcon);
        });

        this.setLayout(null);
        this.add(pauseButton);


        playMusic("res/sound/music.wav");

    }
//oyun döngüsü
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                if (!gameOver && !isPaused) {
                    updateBullets();
                    updateBasicTanks();
                    updateFastTanks();
                    updatePowerTanks();
                    updateArmorTanks();
                    checkBulletCollision();
                    checkEnemyBulletCollision();
//                    checkPowerUpCollisions();

                }
                repaint();
                delta--;
            }
        }

    }
//ekranı çizme    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (showStartScreen || gameWon || gameOver) {
            pauseButton.setVisible(false);
        } else {
            pauseButton.setVisible(true);
        }

        if (showStartScreen) {
            drawStartScreen(g);
            return;
        }

        if (gameWon) {
            drawWinScreen(g);
            return;
        }

        List<Rectangle> tankRects = new ArrayList<>();
        for (BasicTank t : basicTanks) tankRects.add(new Rectangle(t.getX(), t.getY(), 48, 48));
        for (FastTank t : fastTanks) tankRects.add(new Rectangle(t.getX(), t.getY(), 48, 48));
        for (PowerTank t : powerTanks) tankRects.add(new Rectangle(t.getX(), t.getY(), 48, 48));
        for (ArmorTank t : armorTanks) tankRects.add(new Rectangle(t.getX(), t.getY(), 48, 48));
        tankRects.add(new Rectangle(player.getX(), player.getY(), 48, 48));

        gameMap.drawTiles(g);


        player.draw(g);
        for (Bullet b : bullets) b.draw(g);
        for (BasicTank t : basicTanks) t.draw(g);
        for (FastTank t : fastTanks) t.draw(g);
        for (PowerTank t : powerTanks) t.draw(g);
        for (ArmorTank t : armorTanks) t.draw(g);
        
//        for (PowerUp p : powerUps) {
//            if (p.isActive()) p.draw(g, this);
//        }
//        for (ArmorTank t : armorTanks) t.draw(g);
//
//        for (PowerUp p : powerUps) {
//            if (p.isActive()) p.draw(g, this);
//        }

        gameMap.drawTreeOverlays(g, tankRects);


        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.ITALIC, 30));
        g.drawString("" + (player.getHealth() - 1), 765, 520);
        g.drawString("KILLED: ", 690, 200);
        g.drawString("x " + totalEnemiesKilled, 710, 240);
        



        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 64));
            g.drawString("GAME OVER", screenWidth / 2 - 200, screenHeight / 2);
        }
    }
//başlangıç ekranı çizme
    private void drawStartScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.drawImage(startImage, screenWidth / 2 - 180, 60, 360, 180, null);


        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("PRESS ENTER TO START", screenWidth / 2 - 140, 300);
    }
//kazanma ekranı çizme
    private void drawWinScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("YOU WIN!", screenWidth / 2 - 130, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        int y = 160;
        g.drawString("Basic Tanks: " + killedBasic + " x " + basicTankScore + " = " + (killedBasic * basicTankScore), 150, y); y += 30;
        g.drawString("Fast Tanks: " + killedFast + " x " + fastTankScore + " = " + (killedFast * fastTankScore), 150, y); y += 30;
        g.drawString("Power Tanks: " + killedPower + " x " + powerTankScore + " = " + (killedPower * powerTankScore), 150, y); y += 30;
        g.drawString("Armor Tanks: " + killedArmor + " x " + armorTankScore + " = " + (killedArmor * armorTankScore), 150, y); y += 40;

        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("TOTAL SCORE: " + score, 150, y); y += 50;

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press R to Restart", 150, y);
    }
//mermi güncelleme
    private void updateBullets() {
        bullets.removeIf(b -> {
            b.update();
            return !b.isActive();
        });
    }
//düşman takların güncellenmesi
    private void updateBasicTanks() {
        for (BasicTank tank : basicTanks) tank.update();
    }

    private void updateFastTanks() {
        for (FastTank tank : fastTanks) tank.update();
    }

    private void updatePowerTanks() {
        for (PowerTank tank : powerTanks) tank.update();
    }

    private void updateArmorTanks() {
        for (ArmorTank tank : armorTanks) tank.update();
    }
 //mermi çarpışma kontrolleri   
    private void checkBulletCollision() {
        Iterator<Bullet> bit = bullets.iterator();
        while (bit.hasNext()) {
            Bullet b = bit.next();
            Rectangle bulletRect = new Rectangle(b.getX(), b.getY(), 16, 16);

            Iterator<BasicTank> tit = basicTanks.iterator();
            while (tit.hasNext()) {
                BasicTank t = tit.next();
                Rectangle tankRect = new Rectangle(t.getX(), t.getY(), t.getSize(), t.getSize());
                if (bulletRect.intersects(tankRect)) {
                    if (t.hit()) {
                        tit.remove();
                        totalEnemiesKilled++;
                        score += basicTankScore;
                        killedBasic++;
                        if (totalEnemiesKilled >= maxEnemies) {
                            gameWon = true;
                            return;
                        }
                        if (!enemySpawnPoints.isEmpty()) {
                            Point spawn = enemySpawnPoints.get(random.nextInt(enemySpawnPoints.size()));
                            basicTanks.add(new BasicTank(spawn.x, spawn.y, gameMap));
                        }
                    }
                    bit.remove();
                    break;
                }
            }

            Iterator<FastTank> tit2 = fastTanks.iterator();
            while (tit2.hasNext()) {
                FastTank t = tit2.next();
                Rectangle tankRect = new Rectangle(t.getX(), t.getY(), t.getSize(), t.getSize());
                if (bulletRect.intersects(tankRect)) {
                    if (t.hit()) {
                        tit2.remove();
                        totalEnemiesKilled++;
                        score += fastTankScore;
                        killedFast++;
                        if (totalEnemiesKilled >= maxEnemies) {
                            gameWon = true;
                            return;
                        }
                        if (!enemySpawnPoints.isEmpty()) {
                            Point spawn = enemySpawnPoints.get(random.nextInt(enemySpawnPoints.size()));
                            fastTanks.add(new FastTank(spawn.x, spawn.y, gameMap));
                        }
                    }
                    bit.remove();
                    break;
                }
            }

            Iterator<PowerTank> tit3 = powerTanks.iterator();
            while (tit3.hasNext()) {
                PowerTank t = tit3.next();
                Rectangle tankRect = new Rectangle(t.getX(), t.getY(), t.getSize(), t.getSize());
                if (bulletRect.intersects(tankRect)) {
                    if (t.hit()) {
                        tit3.remove();
                        totalEnemiesKilled++;
                        score += powerTankScore;
                        killedPower++;
                        if (totalEnemiesKilled >= maxEnemies) {
                            gameWon = true;
                            return;
                        }
                        if (!enemySpawnPoints.isEmpty()) {
                            Point spawn = enemySpawnPoints.get(random.nextInt(enemySpawnPoints.size()));
                            powerTanks.add(new PowerTank(spawn.x, spawn.y, gameMap));
                        }
                    }
                    bit.remove();
                    break;
                }
            }

            Iterator<ArmorTank> tit4 = armorTanks.iterator();
            while (tit4.hasNext()) {
                ArmorTank t = tit4.next();
                Rectangle tankRect = new Rectangle(t.getX(), t.getY(), t.getSize(), t.getSize());
                if (bulletRect.intersects(tankRect)) {
                    if (t.hit()) {
                        tit4.remove();
                        totalEnemiesKilled++;
                        score += armorTankScore;
                        killedArmor++;
                        if (totalEnemiesKilled >= maxEnemies) {
                            gameWon = true;
                            return;
                        }
                        if (!enemySpawnPoints.isEmpty()) {
                            Point spawn = enemySpawnPoints.get(random.nextInt(enemySpawnPoints.size()));
                            armorTanks.add(new ArmorTank(spawn.x, spawn.y, gameMap));
                        }
                    }
                    bit.remove();
                    break;
                }
            }

            Rectangle eagleRect = gameMap.getEagleRect();
            if (!gameMap.isEagleDestroyed() && eagleRect.intersects(bulletRect)) {
                gameMap.destroyEagle();
                bit.remove();
                gameOver = true;
                return;
            }
        }
    }

//düşman mermilerinin oyuncuya veya kartala çarpması durumu
    private void checkEnemyBulletCollision() {
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), 48, 48);

        for (BasicTank tank : basicTanks) {
            for (Bullet b : tank.getBullets()) {
                Rectangle bulletRect = new Rectangle(b.getX(), b.getY(), 16, 16);
                if (bulletRect.intersects(playerRect)) {
                    b.setInactive();
                    if (player.hit()) {
                        gameOver = true;
                        player.setHealth(0);
                    } else {
                        player.resetToTile(2);
                    }
                    return;
                }
            }
        }

        for (FastTank tank : fastTanks) {
            for (Bullet b : tank.getBullets()) {
                Rectangle bulletRect = new Rectangle(b.getX(), b.getY(), 16, 16);
                if (bulletRect.intersects(playerRect)) {
                    b.setInactive();
                    if (player.hit()) {
                        gameOver = true;
                        player.setHealth(0);
                    } else {
                        player.resetToTile(2);
                    }
                    return;
                }
            }
        }

        for (PowerTank tank : powerTanks) {
            for (Bullet b : tank.getBullets()) {
                Rectangle bulletRect = new Rectangle(b.getX(), b.getY(), 16, 16);
                if (bulletRect.intersects(playerRect)) {
                    b.setInactive();
                    if (player.hit()) {
                        gameOver = true;
                        player.setHealth(0);
                    } else {
                        player.resetToTile(2);
                    }
                    return;
                }
            }
        }

        for (ArmorTank tank : armorTanks) {
            for (Bullet b : tank.getBullets()) {
                Rectangle bulletRect = new Rectangle(b.getX(), b.getY(), 16, 16);
                if (bulletRect.intersects(playerRect)) {
                    b.setInactive();
                    if (player.hit()) {
                        gameOver = true;
                        player.setHealth(0);
                    } else {
                        player.resetToTile(2);
                    }
                    return;
                }
            }
        }

        Rectangle eagleRect = gameMap.getEagleRect();
        if (!gameMap.isEagleDestroyed()) {
            for (BasicTank tank : basicTanks) {
                for (Bullet b : tank.getBullets()) {
                    Rectangle bulletRect = new Rectangle(b.getX(), b.getY(), 16, 16);
                    if (bulletRect.intersects(eagleRect)) {
                        b.setInactive();
                        gameMap.destroyEagle();
                        gameOver = true;
                        return;
                    }
                }
            }

            for (FastTank tank : fastTanks) {
                for (Bullet b : tank.getBullets()) {
                    Rectangle bulletRect = new Rectangle(b.getX(), b.getY(), 16, 16);
                    if (bulletRect.intersects(eagleRect)) {
                        b.setInactive();
                        gameMap.destroyEagle();
                        gameOver = true;
                        return;
                    }
                }
            }

            for (PowerTank tank : powerTanks) {
                for (Bullet b : tank.getBullets()) {
                    Rectangle bulletRect = new Rectangle(b.getX(), b.getY(), 16, 16);
                    if (bulletRect.intersects(eagleRect)) {
                        b.setInactive();
                        gameMap.destroyEagle();
                        gameOver = true;
                        return;
                    }
                }
            }

            for (ArmorTank tank : armorTanks) {
                for (Bullet b : tank.getBullets()) {
                    Rectangle bulletRect = new Rectangle(b.getX(), b.getY(), 16, 16);
                    if (bulletRect.intersects(eagleRect)) {
                        b.setInactive();
                        gameMap.destroyEagle();
                        gameOver = true;
                        return;
                    }
                }
            }
        }

    }
    
//    private void checkPowerUpCollisions() {
//        Iterator<PowerUp> it = powerUps.iterator();
//        while (it.hasNext()) {
//            PowerUp p = it.next();
//            if (player.getBounds().intersects(p.getBounds())) {
//                p.applyToPlayer();
//
//                if (p instanceof ShovelPowerUp) {
//                    new Thread(() -> {
//                        gameMap.upgradeEagleWalls();
//                        try {
//                            Thread.sleep(10000); // 10 saniye sonra
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        gameMap.restoreEagleWalls();
//                    }).start();
//                }
//
//                p.deactivate();
//                it.remove();
//            }
//        }
//    }

// tuş kontrolleri
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (showStartScreen && code == KeyEvent.VK_ENTER) {
            showStartScreen = false;
            return;
        }

        if (gameWon && code == KeyEvent.VK_R) {
            restartGame();
            return;
        }

        if (!showStartScreen && !gameOver) {
            if (code == KeyEvent.VK_UP) player.setDirection("up");
            else if (code == KeyEvent.VK_DOWN) player.setDirection("down");
            else if (code == KeyEvent.VK_LEFT) player.setDirection("left");
            else if (code == KeyEvent.VK_RIGHT) player.setDirection("right");
            else if (code == KeyEvent.VK_Z) {
                int bx = player.getX() + tileSize / 2 - 8;
                int by = player.getY() + tileSize / 2 - 8;
                bullets.add(new Bullet(bx, by, player.getDirection(), gameMap));

            }

            if (code != KeyEvent.VK_Z) player.move();
        }
    }
//oyunu yeniden bbaşlatma
    private void restartGame() {
        score = 0;
        killedBasic = 0;
        killedFast = 0;
        killedPower = 0;
        killedArmor = 0;
        totalEnemiesKilled = 0;
        gameWon = false;
        gameOver = false;

        basicTanks.clear();
        fastTanks.clear();
        powerTanks.clear();
        armorTanks.clear();
        bullets.clear();

        for (Point p : gameMap.getBasicTankSpawns()) basicTanks.add(new BasicTank(p.x, p.y, gameMap));
        for (Point p : gameMap.getFastTankSpawns()) fastTanks.add(new FastTank(p.x, p.y, gameMap));
        for (Point p : gameMap.getPowerTankSpawns()) powerTanks.add(new PowerTank(p.x, p.y, gameMap));
        for (Point p : gameMap.getArmorTankSpawns()) armorTanks.add(new ArmorTank(p.x, p.y, gameMap));

        player = new PlayerTank(gameMap.getPlayerStartX(), gameMap.getPlayerStartY(), gameMap);

        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}