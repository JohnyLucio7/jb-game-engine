package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * Classe principal onde são definidos todos os métodos e atributos que compõem o núcleo da game engine.
 * É responsável pela inicialização do Frame e da Thread, assim como, pela implementação dos loops de 
 * renderização e atualização e controle de FPS (Frames Por Segundo).
 * @author Johny Lúcio: BTI/IMD - N - 20200039648 - UFRN.
 * */
public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int WIDTH = 160;
	private final int HEIGHT = 120;
	private final int SCALE = 4;

	private BufferedImage biImage;

	public Game() {
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

	}

	public void render() {

		/*
		 * BufferStrategy � uma sequ�ncia de buffers colocados em tela para otimizar a
		 * renderização.
		 */
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = biImage.getGraphics();
		/*
		 * Tudo dentro desse bloco a partir da criação do objeto "g" está escalonado de acordo com a constante SCALE
		 * tendo em vista que ela só é desenhada no frame a partir da sentensa g.drawImage(...). Tudo está sendo desenhando
		 * na imagem e como ela está sendo multiplicada por SCALE qualquer coisa que for desenhada nela será escalonada.
		 * 
		 * */
		
		// -- Seção de renderização escalonada | Início
		
		g.setColor(new Color(40, 40, 40)); // COR DO PLANO DE FUNDO (BG)
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		g.setColor(Color.CYAN);
		g.fillRect(20, 20, 80, 80);

		// -- Seção de renderização escalonada | Fim
		
		g = bs.getDrawGraphics();
		/*
		 * O objeto da imagem foi criado com os valores de WIDTH e HEIGHT padrões e a partir da sentança a baixo foi
		 * desenhada sendo multiplicada pela constante SCALE. Assim tudo que for desenhado antes disso será SCALE vezes
		 * maior. 
		 * */
		g.drawImage(biImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null); // DESENHO DA IMAGEM NO FRAME
		bs.show();
	}

	public void run() {

		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
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