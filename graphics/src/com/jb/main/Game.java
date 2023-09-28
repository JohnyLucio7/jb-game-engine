package com.jb.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.jb.entities.Bulletshoot;
import com.jb.entities.Enemy;
import com.jb.entities.Entity;
import com.jb.entities.Player;
import com.jb.enums.GameState;
import com.jb.graphics.Spritesheet;
import com.jb.graphics.UI;
import com.jb.world.World;

/**
 * Classe principal onde são definidos todos os métodos e atributos que compõem
 * o núcleo da game engine. É responsável pela inicialização do Frame e da
 * Thread, assim como, pela implementação dos loops de renderização e
 * atualização e controle de FPS (Frames Por Segundo).
 * 
 * @author Johny Lúcio: BTI/IMD - N - 20200039648 - UFRN.
 */
public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private boolean isRunning = true;
	private boolean restartGame = false;
	public static boolean enableShowMessageGameOver = true;

	private int currentLevel = 1;
	private int maxLevel = 2;
	private int framesShowMessageGameOver = 0;
	private int MaxFramesShowMessageGameOver = 15;

	public static final double FPS = 60.0;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;

	private Thread thread;
	private BufferedImage biImage;
	public static JFrame frame;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Bulletshoot> bulletshoot;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random rand;
	public static UI ui;
	public static GameState gameState = GameState.NORMAL;

	public Game() {
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setFocusable(true); // estabelece este canvas no 1º plano
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		/* Inicializando objetos */
		rand = new Random();
		biImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bulletshoot = new ArrayList<Bulletshoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		ui = new UI();
	}

	/** Método que inicializar a execução da thread */
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	/** Método que finaliza da execução da thread */
	public synchronized void stop() {
		isRunning = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	/** Método de atualização principal */
	public void tick() {

		if (gameState == GameState.NORMAL) {

			setRestartGame(false);

			entitiesTick();
			bulletshootTick();

			nextLevel();

		} else if (gameState == GameState.GAMEOVER) {

			animMessageGameOver();

			restartAfterGameOver();
		}

	}

	/** Método de renderização principal */
	public void render() {

		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = biImage.getGraphics();

		/* -- Seção de renderização escalonada | Início */

		g.setColor(new Color(40, 40, 40)); // COR DO PLANO DE FUNDO (BG)
		g.fillRect(0, 0, WIDTH, HEIGHT);

		world.render(g);

		this.entitiesRender(g);

		this.bulletshootRender(g);

		ui.render(g);

		/* -- Seção de renderização escalonada | Fim */

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(biImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null); // DESENHO DA IMAGEM NO FRAME

		ui.renderWithoutScale(g);
		ui.gameOverUI(g);
		bs.show();
	}

	public void run() {

		long lastTime = System.nanoTime();
		double amountOfTicks = FPS;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();

		while (isRunning) {

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}

		stop();
	}

	private void initFrame() {
		frame = new JFrame("Zelda Clone");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void reinitGame(String level) {
		entities.clear();
		enemies.clear();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/" + level);
	}

	private void nextLevel() {
		if (enemies.size() == 0) {
			currentLevel++;
			if (currentLevel > maxLevel)
				currentLevel = 1;
			String newWorld = "level" + currentLevel + ".png";
			reinitGame(newWorld);
		}
	}

	private void restartAfterGameOver() {
		if (this.getRestartGame()) {
			this.setRestartGame(false);
			gameState = GameState.NORMAL;
			String world = "level" + currentLevel + ".png";
			reinitGame(world);
		}
	}

	private void entitiesTick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	}

	private void bulletshootTick() {
		for (int i = 0; i < bulletshoot.size(); i++) {
			bulletshoot.get(i).tick();
		}
	}

	private void entitiesRender(Graphics g) {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
	}

	private void bulletshootRender(Graphics g) {
		for (int i = 0; i < bulletshoot.size(); i++) {
			bulletshoot.get(i).render(g);
		}
	}

	private void animMessageGameOver() {
		framesShowMessageGameOver++;
		framesShowMessageGameOver %= MaxFramesShowMessageGameOver;
		if (framesShowMessageGameOver == 0) {
			enableShowMessageGameOver = !enableShowMessageGameOver;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.setRight(true);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.setLeft(true);
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.setUp(true);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.setDown(true);
		}

		if (e.getKeyCode() == KeyEvent.VK_CONTROL && Game.player.getHasGun()
				&& Game.player.getWeapon().getAmmoInClip() > 0) {
			player.setIsShoot(true);
		}

		if (e.getKeyCode() == KeyEvent.VK_R && Game.player.getHasGun()) {
			Game.player.getWeapon().reload(Game.player.getAmmo());
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.setRestartGame(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.setRight(false);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.setLeft(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.setUp(false);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.setDown(false);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (Game.player.getHasGun() && Game.player.getWeapon().getAmmoInClip() > 0) {
			player.setIsMouseShoot(true);
		}
		player.setMouseX((e.getX() / SCALE));
		player.setMouseY((e.getY() / SCALE));
		// System.out.println("X: " + player.getMouseX() + " Y: " + player.getMouseY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/** getters and setters */

	public boolean getRestartGame() {
		return restartGame;
	}

	public void setRestartGame(boolean restartGame) {
		this.restartGame = restartGame;
	}
}