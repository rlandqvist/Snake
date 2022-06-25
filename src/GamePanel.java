import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600; // KAN LEKA RUNT OCH SE OLIKA FORMER PÅ SKÄRMEN.
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75; // HÖGRE = SLOWER GAME VICE VERSA.
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6; // HUR STOR ORMEN ÄR FRÅN BÖRJAN.
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	static boolean gameOn = false;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black); // FÄRGEN PÅ BAKGRUNDEN.
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();

	}
	
	public void pause() {
		GamePanel.gameOn = true;
		timer.stop();
	}

	public void resume() {
		GamePanel.gameOn = false;
		timer.start();
	}
	

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);

	}

	public void draw(Graphics g) {

//		for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) { // KAN KOMMENTERAS BORT, GÄLLER GRID
//			g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // KAN KOMMENTERAS BORT, GÄLLER GRID
//			
//			g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // KAN KOMMENTERAS BORT, GÄLLER GRID
//			
//		}
		if (running) {
			g.setColor(Color.red); // FÄRG PÅ ÄPPLET
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // FORMEN PÅ ÄPPLET

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green); // ORMENS FÄRG.
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0)); // SKUGGAN PÅ ORMEN
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))); // GER EN RANDOM FÄRG PÅ ORMEN, TAS BORT FÖR ENDAST GRÖN
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
		//	g.setFont(new Font("Ink Free", Font.BOLD, 40)); // SCOREBOARD TEXTENS FÄRG, STORLEK OCH TYPSNITT, OFF: MINDRE TEXT HELA TIDEN. ON:STOR TEXT HELA TIDEN;
			FontMetrics metrics = getFontMetrics(g.getFont()); // TA AV ALLA5 RADER FÖR ATT INTE HA NÅGON TEXT ALLS MEDAN MAN SPELAR
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());

		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}

	public void checkCollisions() {
		for (int i = bodyParts; i > 0; i--) { // KOLLAR OM HUVUDET KROCKAR MED KROPPEN
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		if (x[0] < 0) { // KOLLAR OM HUVUDET NUDDAR VÄNSTER KANT
			running = false;
		}
		if (x[0] > SCREEN_WIDTH) { // KOLLAR OM HUVUDET NUDDAR HÖGER KANT
			running = false;
		}
		if (y[0] < 0) { // KOLLAR OM HUVUDET NUDDAR ÖVRE KANTEN
			running = false;
		}
		if (y[0] > SCREEN_HEIGHT) { // KOLLAR OM HUVUDET NUDDAR NEDRE KANTEN
			running = false;
		}
		if (!running) {
			timer.stop();
		}

	}

	public void gameOver(Graphics g) {

		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40)); // SCOREBOARD TEXTENS FÄRG, STORLEK OCH TYPSNITT
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());

		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75)); // GAME OVER TEXTENS FÄRG, STORLEK OCH TYPSNITT
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2); // CENTRERAR
																												// TEXTEN
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
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_SPACE: //KNAPPEN FÖR ATT PAUSA
				if(GamePanel.gameOn) {
					resume();
				} else {
					pause();
				}
				break;
			}
		}
	}
}