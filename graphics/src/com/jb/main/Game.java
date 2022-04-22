package com.jb.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.jb.entities.Entity;
import com.jb.entities.Player;
import com.jb.graphics.Spritesheet;

/**
 * Classe principal onde são definidos todos os métodos e atributos que compõem
 * o núcleo da game engine. É responsável pela inicialização do Frame e da
 * Thread, assim como, pela implementação dos loops de renderização e
 * atualização e controle de FPS (Frames Por Segundo).
 * 
 * @author Johny Lúcio: BTI/IMD - N - 20200039648 - UFRN.
 */
public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int WIDTH = 240;
	private final int HEIGHT = 160;
	private final int SCALE = 3;
	private final double FPS = 60.0;
	private BufferedImage biImage;
	public List<Entity> entities;
	public Spritesheet spritesheet;
	private Player player;

	public Game() {
		this.addKeyListener(this);
		this.setFocusable(true); // estabelece este canvas no 1º plano
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		/* Inicializando objetos */
		biImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
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
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
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

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}

		/* -- Seção de renderização escalonada | Fim */

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(biImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null); // DESENHO DA IMAGEM NO FRAME

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
}