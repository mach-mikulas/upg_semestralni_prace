import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class DrawingPanel extends JPanel {

    private ArrayList<ASpaceObject> spaceObjects;
    private Graphics2D g2;

    private AffineTransform old;
    private AffineTransform scaled;

    private double gConsatnt;
    private double step;
    public double simulationTimeStart;
    public double simulationTime = 0;
    public double simulationTimeStopped = 0;
    public double simulationTimeStoppedStart = 0;
    public double timeLastUpdate = 0;
    public boolean timeIsRunning = true;

    double scale;

    double x_min, x_max, y_min, y_max;
    double world_width, world_height;

    public DrawingPanel(ArrayList<ASpaceObject> spaceObjects, double gConstant, double step) {
        this.setPreferredSize(new Dimension(800, 600));
        this.spaceObjects = spaceObjects;
        this.gConsatnt = gConstant;
        this.step = step;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);

        g2 = (Graphics2D) g;


        setScale();
        double scale_x = this.getWidth() / world_width;
        double scale_y = this.getHeight() / world_height;
        scale = Math.min(scale_x,scale_y);

        old = g2.getTransform();

        g2.translate(this.getWidth()/2, this.getHeight()/2);
        g2.scale(scale,scale);
        g2.translate(-world_width/2, -world_height/2);

        scaled = g2.getTransform();

        g2.setColor(Color.GRAY);
        Rectangle2D rec = new Rectangle2D.Double(0,0, world_width, world_height);
        g2.fill(rec);

        g2.setColor(Color.BLUE);
        drawObjects(g2);


        g2.setTransform(old);
        drawObjectInfo(g2);
        drawSimulationTime(g2);

    }

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
     * Vykresli vsechny space objecty
     */
    private void drawObjects(Graphics2D g2){

        for (ASpaceObject sO : spaceObjects) {

            Ellipse2D.Double hitbox = new Ellipse2D.Double(sO.getPosX() - sO.getr() -x_min, sO.getPosY() - sO.getr() - y_min, sO.getr()*2, sO.getr()*2);

            if(sO.isClicked()){
                g2.setColor(Color.RED);
            }
            else{
                g2.setColor(Color.BLUE);

            }
            g2.fill(hitbox);

        }

    }

    /**
     * Zobrazi do okna informace o vybranem space objectu
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
     * Do praveho horniho rohu vypise cas simulace
     */
    private void drawSimulationTime(Graphics2D g2){
        g2.setTransform(old);

        g2.setFont(new Font("TimesRoman", Font.BOLD, 15));
        g2.setColor(Color.BLACK);


        g2.drawString(String.format("Simulation time: %.3fs", (simulationTime/1000.0)*step), this.getWidth()-195, 15);

        g2.setTransform(scaled);
    }

    /**
     * Vypocte aktualni zrychleni space objectu
     */
    private void calculateCurrentA(){

        double vector1, vector2, jmenovatel, citatelx, citately;

        for(int i = 0; i < spaceObjects.size(); i++){

            double zlomekx = 0;
            double zlomeky = 0;

            for(int j = 0; j < spaceObjects.size(); j++){

                if(i != j) {

                    vector1 = (spaceObjects.get(i).getPosX() - spaceObjects.get(j).getPosX()) * (spaceObjects.get(i).getPosX() - spaceObjects.get(j).getPosX());
                    vector2 = (spaceObjects.get(i).getPosY() - spaceObjects.get(j).getPosY()) * (spaceObjects.get(i).getPosY() - spaceObjects.get(j).getPosY());
                    jmenovatel = (Math.sqrt(vector1 + vector2)) * (Math.sqrt(vector1 + vector2)) * (Math.sqrt(vector1 + vector2));
                    citatelx = spaceObjects.get(j).getPosX() - spaceObjects.get(i).getPosX();
                    citately = spaceObjects.get(j).getPosY() - spaceObjects.get(i).getPosY();

                    zlomekx += (citatelx/jmenovatel)*spaceObjects.get(j).getWeight();
                    zlomeky += (citately/jmenovatel)*spaceObjects.get(j).getWeight();
                }

            }

            spaceObjects.get(i).setaX(zlomekx * gConsatnt);
            spaceObjects.get(i).setaY(zlomeky * gConsatnt);

        }
    }

    public void updateSystem(double t) {

        double deltaT_min = step / 10000;

        double velocityX, velocityY;

        while (t > 0) {

            double deltaT = Math.min(t,deltaT_min);

            calculateCurrentA();

            for (ASpaceObject spaceObject : spaceObjects) {

                velocityX = (deltaT / 2) * spaceObject.getaX();
                spaceObject.setVelX(spaceObject.getVelX() + velocityX);

                velocityY = (deltaT / 2) * spaceObject.getaY();
                spaceObject.setVelY(spaceObject.getVelY() + velocityY);

                spaceObject.setPosX(spaceObject.getPosX() + (deltaT * spaceObject.getVelX()));
                spaceObject.setPosY(spaceObject.getPosY() + (deltaT * spaceObject.getVelY()));

                velocityX = (deltaT / 2) * spaceObject.getaX();
                spaceObject.setVelX(spaceObject.getVelX() + velocityX);

                velocityY = (deltaT / 2) * spaceObject.getaY();
                spaceObject.setVelY(spaceObject.getVelY() + velocityY);
            }

            t = t - deltaT;
        }

    }

    /**
     * Metoda kontrolujici zda bylo kliknuto na space object
     */
    public void objectHit(double x, double y){

        double testX = ((x - this.getWidth() / 2.0) / scale) + world_width/2;
        double testY = ((y - this.getHeight() / 2.0) / scale) + world_height/2;

        for(ASpaceObject spaceObject : spaceObjects){

            Ellipse2D elipsa = new Ellipse2D.Double(spaceObject.getPosX() - spaceObject.getr() - x_min, spaceObject.getPosY() - spaceObject.getr() - y_min, spaceObject.getr()*2, spaceObject.getr()*2);

            if(elipsa.contains(testX, testY)){
                spaceObject.setClicked(true);
            }
            else {
                spaceObject.setClicked(false);
            }
        }

    }

    public void isSpaceBarPressed(){

        System.out.println("xd");

        if(timeIsRunning) {
            timeIsRunning = false;
            simulationTimeStoppedStart = System.currentTimeMillis();
            return;
        }
        simulationTimeStopped = System.currentTimeMillis() - simulationTimeStart - timeLastUpdate;
        timeIsRunning = true;
    }
}
