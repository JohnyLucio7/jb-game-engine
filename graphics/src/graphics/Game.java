package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * Classe principal onde são definidos todos os métodos e atributos que compõem
 * o núcleo da game engine. É responsável pela inicialização do Frame e da
 * Thread, assim como, pela implementação dos loops de renderização e
 * atualização e controle de FPS (Frames Por Segundo).
 * 
 * @author Johny Lúcio: BTI/IMD - N - 20200039648 - UFRN.
 */
public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int WIDTH = 240;
	private final int HEIGHT = 160;
	private final int SCALE = 3;
	private final double FPS = 60.0;

	private BufferedImage biImage;
	private Spritesheet spritesheet;
	private BufferedImage[] player;
	private int playerAnimFrames = 0;
	private int playerAnimMaxFrames = 12;
	private int playerAnimCurrFrame = 0;
	private int playerAnimMaxCurrFrame = 4;

	// testes
	private int x = 50; // fins de teste
	private int y = 50; // fins de teste
	private int dir = 1; // fins de teste
	private boolean enableCollider = true; // fins de teste

	public Game() {
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new BufferedImage[4];
		for (int i = 0; i < 4; i++) {
			player[i] = spritesheet.getSprite((i * 16), 0, 16, 16);
		}

		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		biImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}

	public void initFrame() {
		frame = new JFrame("Game #01");
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
		
		playerAnimFrames++;

		if (playerAnimFrames > playerAnimMaxFrames) {
			playerAnimFrames = 0;
			playerAnimCurrFrame++;
			if (playerAnimCurrFrame >= playerAnimMaxCurrFrame) {
				playerAnimCurrFrame = 0;
			}
		}
		
		
		x += dir;
		if (x >= WIDTH - 16) {
			dir *= -1;
		} else if (x <= 0) {
			dir *= -1;
		}

	}

	public void render() {

		/*
		 * BufferStrategy é uma sequência de buffers colocados em tela para otimizar a
		 * renderização.
		 */
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = biImage.getGraphics();
		/*
		 * Tudo dentro desse bloco a partir da criação do objeto "g" está escalonado de
		 * acordo com a constante SCALE tendo em vista que ela só é desenhada no frame a
		 * partir da sentensa g.drawImage(...). Tudo está sendo desenhando na imagem e
		 * como ela está sendo multiplicada por SCALE qualquer coisa que for desenhada
		 * nela será escalonada.
		 * 
		 */

		// -- Seção de renderização escalonada | Início

		g.setColor(new Color(40, 40, 40)); // COR DO PLANO DE FUNDO (BG)
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// Exemplo de retangulo
//		g.setColor(Color.CYAN);
//		g.fillRect(20, 20, 80, 80); 

		// Exemplo de texto
//		g.setFont(new Font("Arial", Font.BOLD, 16));
//		g.setColor(Color.WHITE);
//		g.drawString("Hello World", 19, 19);

		// Exemplo de rotação com Graphics 2D
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.rotate(Math.toRadians(45), x+8, y+8);
//		g2d.rotate(Math.toRadians(-45), x+8, y+8);

		g.drawImage(player[playerAnimCurrFrame], x, y, null);

		// Exemplo de layer
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// Apenas para fins de teste, colisor amador
		if (enableCollider) {
			g.setColor(Color.GREEN);
			g.drawLine(x, y, x + 16, y);
			g.drawLine(x, y + 16, x + 16, y + 16);
			g.drawLine(x, y, x, y + 16);
			g.drawLine(x + 16, y, x + 16, y + 16);
		}

		// -- Seção de renderização escalonada | Fim

		g.dispose();
		g = bs.getDrawGraphics();
		/*
		 * O objeto da imagem foi criado com os valores de WIDTH e HEIGHT padrões e a
		 * partir da sentança a baixo foi desenhada sendo multiplicada pela constante
		 * SCALE. Assim tudo que for desenhado antes disso será SCALE vezes maior.
		 */
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
}