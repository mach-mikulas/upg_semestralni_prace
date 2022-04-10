import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Galaxy_SP2022 {

	public static void main(String[] args) {

		//Input input = new Input("data/collision.csv");
		//Input input = new Input("data/negative.csv");
		Input input = new Input("data/pulsar.csv");
		//Input input = new Input("data/random100.csv");
		//Input input = new Input("data/random500.csv");
		//Input input = new Input("data/solar.csv");



		ArrayList<ISpaceObject> spaceObjects = input.getInput();
		double gConstant = input.getgConstant();
		double step = input.getStep();
		input = null;



		JFrame okno = new JFrame();
		okno.setTitle("Mikuláš Mach, A21B0202P");
		okno.setSize(800, 600);

		DrawingPanel panel = new DrawingPanel(spaceObjects, gConstant, step);
		okno.add(panel);

		okno.pack();

		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		okno.setLocationRelativeTo(null); //vycentrovat na obrazovce
		okno.setVisible(true);


		/*
		System.out.println(gConstant + "," + step);
		for(int i = 0; i < spaceObjects.size(); i++){
			System.out.println(spaceObjects.get(i).getName() + "," + spaceObjects.get(i).getPosX() + "," + spaceObjects.get(i).getPosY() + "," + spaceObjects.get(i).getVelX() + "," + spaceObjects.get(i).getVelY() + "," + spaceObjects.get(i).getr() + ",a:" + spaceObjects.get(i).getaX());
		}
		*/

		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				//System.out.println(e.getX() + " " + e.getY() );
				panel.objectHit(e.getX(),e.getY());
			}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});




		java.util.Timer tm = new Timer();
		tm.schedule(new TimerTask() {
						@Override
						public void run() {
							panel.repaint();
						}
					}, 0, 20 //50x za sekundu
		);









	}
}
