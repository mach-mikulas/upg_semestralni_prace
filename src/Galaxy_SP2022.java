import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

public class Galaxy_SP2022 {

	public static void main(String[] args) {

		//Input input = new Input(args[0]);

		//Input input = new Input("data/collision.csv");
		Input input = new Input("data/negative.csv");
		//Input input = new Input("data/pulsar.csv");
		//Input input = new Input("data/random100.csv");
		//Input input = new Input("data/random500.csv");
		//Input input = new Input("data/solar.csv");



		ASpaceObject[] spaceObjects = input.getInput();
		double gConstant = input.getgConstant();
		double step = input.getStep();
		input = null;



		JFrame okno = new JFrame();
		okno.setTitle("Mikuláš Mach, A21B0202P");
		okno.setSize(800, 600);


		Calculations calc = new Calculations(spaceObjects, gConstant, step);
		DrawingPanel panel = new DrawingPanel(spaceObjects, step);
		okno.add(panel);

		okno.pack();

		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		okno.setLocationRelativeTo(null); //vycentrovat na obrazovce

		okno.setVisible(true);

		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				panel.objectHit(e.getX(),e.getY());
			}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {

				if (e.getID() == KeyEvent.KEY_PRESSED
						&& e.getKeyChar() == ' ') {
					calc.isSpaceBarPressed();
				}

				return false;
			}
		});

		calc.simulationTimeStart = System.currentTimeMillis();

		java.util.Timer tm = new Timer();
		tm.schedule(new TimerTask() {

						@Override
						public void run() {

							panel.repaint();

							if(calc.timeIsRunning){

								calc.simulationTime = System.currentTimeMillis() - calc.simulationTimeStart - calc.simulationTimeStopped;
								double t = ((calc.simulationTime) - calc.simulationTimeBefore) / 1000.0;
								calc.updateSystem(t * step);
								calc.simulationTimeBefore = calc.simulationTime;
							}
						}
					}, 0, 20 //50x za sekundu
		);

	}
}
