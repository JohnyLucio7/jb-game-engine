package com.jb.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	public static final int WIDTH = 240; // 240
	public static final int HEIGHT = 160; // 160
	public static final int SCALE = 3;
	private static final double FPS = 60.0;
	private int CURRENT_LEVEL = 1, MAX_LEVEL = 2;
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

	private boolean restartGame = false;
	private boolean enableShowMessageGameOver = true;
	private int framesShowMessageGameOver = 0;
	private int MaxFramesShowMessageGameOver = 15;

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

	public void initFrame() {
		frame = new JFrame("Zelda Clone");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void reinitGame(String level) {
		entities.clear();
		enemies.clear();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/" + level);
	}

	public void gameOver(Graphics g) {
		if (gameState == GameState.GAMEOVER) {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, 36));
			g.drawString("Game Over", (WIDTH * SCALE) / 2 - 95, (HEIGHT * SCALE) / 2);
			g.setFont(new Font("arial", Font.BOLD, 32));
			if (enableShowMessageGameOver) {
				g.drawString(">Enter para reiniciar<", (WIDTH * SCALE) / 2 - 165, (HEIGHT * SCALE) / 2 + 40);
			}
		}
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

			this.restartGame = false;

			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}

			for (int i = 0; i < bulletshoot.size(); i++) {
				bulletshoot.get(i).tick();
			}

			if (enemies.size() == 0) {
				System.out.println("next Level!");
				CURRENT_LEVEL++;
				if (CURRENT_LEVEL > MAX_LEVEL)
					CURRENT_LEVEL = 1;
				String newWorld = "level" + CURRENT_LEVEL + ".png";
				reinitGame(newWorld);
			}

			// mover para o player
			if (player.getLife() <= 0) {
				gameState = GameState.GAMEOVER;
			}
		} else if (gameState == GameState.GAMEOVER) {
			System.out.println("Game Over");

			framesShowMessageGameOver++;
			framesShowMessageGameOver %= MaxFramesShowMessageGameOver;
			if (framesShowMessageGameOver == 0) {
				enableShowMessageGameOver = !enableShowMessageGameOver;
			}

			if (restartGame) {
				this.restartGame = false;
				gameState = GameState.NORMAL;
				String world = "level" + CURRENT_LEVEL + ".png";
				reinitGame(world);
			}
		}

	}

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

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}

		for (int i = 0; i < bulletshoot.size(); i++) {
			bulletshoot.get(i).render(g);
		}

		ui.render(g);

		/* -- Seção de renderização escalonada | Fim */

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(biImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null); // DESENHO DA IMAGEM NO FRAME

		ui.renderWithoutScale(g);

		gameOver(g);

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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.setRight(true);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
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
			this.restartGame = true;
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
}