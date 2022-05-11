import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * Trida provadi vykreslovani aktualniho stavu simulace
 * @author Mikulas Mach
 */
public class DrawingPanel extends JPanel {

    private static ASpaceObject[] spaceObjects;
    private final double step;
    private double scale;

    private double x_min, x_max, y_min, y_max, world_width, world_height;

    private AffineTransform old;
    private AffineTransform scaled;

    /**
     * Konstruktor priradi hodnoty atributum
     * @param spaceObjects pole vsech spaceObjectu
     * @param step krok simulace
     */
    public DrawingPanel(ASpaceObject[] spaceObjects, double step) {
        this.setPreferredSize(new Dimension(800, 600));
        this.spaceObjects = spaceObjects;
        this.step = step;
    }

    public static void setSpaceObjects(ASpaceObject[] newSpaceObjects){
        spaceObjects = newSpaceObjects;
    }

    /**
     * Nastavi aktualni scale a vola metody ktere zajisti vykresleni
     */
    @Override
    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        setScale();
        double scale_x = this.getWidth() / world_width;
        double scale_y = this.getHeight() / world_height;
        scale = Math.min(scale_x,scale_y);

        old = g2.getTransform();

        g2.translate(this.getWidth()/2, this.getHeight()/2);
        g2.scale(scale,scale);
        g2.translate(-world_width/2, -world_height/2);

        scaled = g2.getTransform();

        g2.setColor(Color.BLUE);
        drawObjects(g2);

        drawObjectInfo(g2);
        drawSimulationTime(g2);

    }

    /**
     * Aktualizuje minimalni a maximalni souradnici X a Y vesmiru
     */
    private void setScale(){

        x_min = Double.MAX_VALUE;
        x_max = 0;
        y_min = Double.MAX_VALUE;
        y_max = 0;

        for (ASpaceObject spaceObject : spaceObjects) {

            double left = spaceObject.getPosX() - spaceObject.getr();
            double right = spaceObject.getPosX() + spaceObject.getr();
            double up = spaceObject.getPosY() - spaceObject.getr();
            double down = spaceObject.getPosY() + spaceObject.getr();

            x_min = Math.min(left, x_min);
            x_max = Math.max(right,x_max);
            y_min = Math.min(up, y_min);
            y_max = Math.max(down,y_max);

        }

        world_width = x_max - x_min;
        world_height = y_max - y_min;
    }

    /**
     * Vykresli vsechny spaceObjecty
     * Kontroluje, zda neni nejaka planeta mensi nez 3px, pokud ano tak zmeni vykreslovaci polomer na 3px
     * a take kontroluje, zda nedoslo ke kolizi, pokud ano vytvori se novy object
     */
    private void drawObjects(Graphics2D g2){

        for(ASpaceObject spaceObject : spaceObjects){
            if(Calculations.calculateCollision(spaceObject)){
                break;
            }

        }

        for (ASpaceObject spaceObject : spaceObjects) {

            spaceObject.addGraphData(spaceObject.getVelX(), spaceObject.getVelY());

            if(Calculations.timeIsRunning) {
                spaceObject.addTrajectory(spaceObject.getPosX(), spaceObject.getPosY());
            }

            g2.setColor(new Color(70, 70,70,10));

            spaceObject.trajectory.forEach((time, position) -> {

                Ellipse2D.Double trajectory;

                if(2*spaceObject.getr()*scale < 3){
                    trajectory = new Ellipse2D.Double(position[0] - (3/scale) -x_min, position[1] - (3/scale) - y_min, (3/scale)*2, (3/scale)*2);
                }else {
                    trajectory = new Ellipse2D.Double(position[0] - spaceObject.getr() -x_min, position[1] - spaceObject.getr() - y_min, spaceObject.getr()*2, spaceObject.getr()*2);
                }



                g2.fill(trajectory);
            });

            spaceObject.calculateR(spaceObject.getWeight());

            Ellipse2D.Double elipsa;

            //Prevede polomer SpaceObjectu na prumer v px a zkontroluje zda je vetsí nez 3px
            //Pokud je mensi tak nastavi zobrazovaci polomer na 3px
            if(2*spaceObject.getr()*scale < 3){
                elipsa = new Ellipse2D.Double(spaceObject.getPosX() - (3/scale) -x_min, spaceObject.getPosY() - (3/scale) - y_min, (3/scale)*2, (3/scale)*2);
            }else {
                elipsa = new Ellipse2D.Double(spaceObject.getPosX() - spaceObject.getr() -x_min, spaceObject.getPosY() - spaceObject.getr() - y_min, spaceObject.getr()*2, spaceObject.getr()*2);
            }


            if(spaceObject.isClicked()){
                g2.setColor(Color.RED);
            }
            else{
                g2.setColor(Color.BLUE);

            }
            g2.fill(elipsa);

        }
    }

    /**
     * Zobrazi do okna informace o vybranem spaceObjectu a zvyrazni ho
     */
    private void drawObjectInfo(Graphics2D g2){

        for(ASpaceObject spaceObject : spaceObjects) {
            if (spaceObject.isClicked()) {

                g2.setTransform(old);

                g2.setFont(new Font("TimesRoman", Font.BOLD, 15));
                g2.setColor(Color.BLACK);

                g2.drawString("Name: " + spaceObject.getName(), 0, 15);
                g2.drawString(String.format("Position X, Y: %.3f, %.3f", spaceObject.getPosX(), spaceObject.getPosY()), 0, 30);
                g2.drawString(String.format("Velocity X, Y: %.3f, %.3f", spaceObject.getVelX(), spaceObject.getVelY()), 0, 45);
                g2.setColor(Color.BLUE);

                g2.setTransform(scaled);
            }
        }
    }

    /**
     * Do praveho horniho rohu vypise aktualni cas simulace
     */
    private void drawSimulationTime(Graphics2D g2){
        g2.setTransform(old);

        g2.setFont(new Font("TimesRoman", Font.BOLD, 15));
        g2.setColor(Color.BLACK);

        String simTime = String.format("Simulation time: %.3fs", (Calculations.simulationTime/1000.0)*step);

        g2.drawString(simTime, this.getWidth() - g2.getFontMetrics().stringWidth(simTime), 15);

        g2.setTransform(scaled);
    }

    /**
     * Metoda kontrolujici zda bylo kliknuto na spaceObject
     */
    public void objectHit(double x, double y, int whatClicked){

        double testX = ((x - this.getWidth() / 2.0) / scale) + world_width/2;
        double testY = ((y - this.getHeight() / 2.0) / scale) + world_height/2;

        for(ASpaceObject spaceObject : spaceObjects){

            Ellipse2D hitbox;

            //Prevede polomer SpaceObjectu na prumer v px a zkontroluje zda je vetsí nez 3px
            //Pokud je mensi tak nastavi zobrazovaci polomer na 3px
            if(2*spaceObject.getr()*scale < 3){
                hitbox = new Ellipse2D.Double(spaceObject.getPosX() - (3/scale) -x_min, spaceObject.getPosY() - (3/scale) - y_min, (3/scale)*2, (3/scale)*2);
            }else {
                hitbox = new Ellipse2D.Double(spaceObject.getPosX() - spaceObject.getr() -x_min, spaceObject.getPosY() - spaceObject.getr() - y_min, spaceObject.getr()*2, spaceObject.getr()*2);
            }

            if(hitbox.contains(testX, testY)){
                if(whatClicked == 0){
                    spaceObject.setClicked(true);
                }
                else {
                    createChart(spaceObject.getGraphData(), spaceObject);
                }

            }
            else {
                spaceObject.setClicked(false);
            }
        }
    }

    private void createChart(TreeMap<Long, Double> graphData, ASpaceObject spaceObject){

        JFrame window = new JFrame();

        window.setTitle("Graph");
        window.setSize(600, 600);

        ChartPanel chartPanel = new ChartPanel(createXYLineChart(graphData, spaceObject));
        window.add(chartPanel);

        window.pack();

        //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null); //vycentrovat na obrazovce
        window.setVisible(true);
    }



    private JFreeChart createXYLineChart(TreeMap<Long,Double> data, ASpaceObject spaceObject) {

        XYSeries serie = new XYSeries("Rychlost");

        data.forEach((time, velocity) -> serie.add(time/1000.0,velocity));

        XYSeriesCollection dataset = new XYSeriesCollection(serie);

        JFreeChart chart = ChartFactory.createXYLineChart("Rychlost spaceObjectu " + spaceObject.getName(),"čas [s]","Rychlost [km/h]",dataset);

        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                SwingUtilities.invokeLater(() -> {
                    if(data.lastEntry() != null) {
                        serie.add(data.lastEntry().getKey() / 1000.0, data.lastEntry().getValue());
                    }

                    if (System.currentTimeMillis() - Calculations.simulationTimeStart > 30000) {
                        serie.remove(0);
                    }
                });


            }
        }, 0, 20);

        return chart;
    }

}
