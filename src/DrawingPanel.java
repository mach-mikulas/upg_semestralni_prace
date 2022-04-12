import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Trida provadi vykreslovani aktualniho stavu simulace
 * @author Mikulas Mach
 */
public class DrawingPanel extends JPanel {

    private ASpaceObject[] spaceObjects;
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
     */
    private void drawObjects(Graphics2D g2){

        for (ASpaceObject sO : spaceObjects) {

            sO.calculateR(sO.getWeight());

            Ellipse2D.Double elipsa;

            //Prevede polomer SpaceObjectu na prumer v px a zkontroluje zda je vetsí nez 3px
            //Pokud je mensi tak nastavi zobrazovaci polomer na 3px
            if(2*sO.getr()*scale < 3){
                elipsa = new Ellipse2D.Double(sO.getPosX() - (3/scale) -x_min, sO.getPosY() - (3/scale) - y_min, (3/scale)*2, (3/scale)*2);
            }else {
                elipsa = new Ellipse2D.Double(sO.getPosX() - sO.getr() -x_min, sO.getPosY() - sO.getr() - y_min, sO.getr()*2, sO.getr()*2);
            }


            if(sO.isClicked()){
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
    public void objectHit(double x, double y){

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
                spaceObject.setClicked(true);
            }
            else {
                spaceObject.setClicked(false);
            }
        }
    }
}
