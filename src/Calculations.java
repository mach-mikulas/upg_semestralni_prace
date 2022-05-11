
/**
 * Trida provadi vsechny vypocty, ktere nepotrebuji instanci {@link DrawingPanel}
 * @author Mikulas Mach
 */
public class Calculations {

    private static ASpaceObject[] spaceObjects;
    private final double gConstant;
    private final double step;

    /** systemovi cas startu simulace*/
    public static long simulationTimeStart;
    /** aktualni cas simulace*/
    public static double simulationTime = 0;
    /** cas trvani stopnuti simulace*/
    public double simulationTimeStopped = 0;
    /** cas simulace v predchozim kroku*/
    public double simulationTimeBefore = 0;
    /** urcuje zda simulace bezi nebo je stopnuta*/
    public static boolean timeIsRunning = true;

    /**
     * Konstruktor priradi hodnoty atributum
     * @param spaceObjects pole vsech spaceObjectu
     * @param gConstant gravitacni konstanta
     * @param step krok simulace
     */
    public Calculations(ASpaceObject[] spaceObjects, double gConstant, double step){
        this.spaceObjects = spaceObjects;
        this.gConstant = gConstant;
        this.step = step;
    }

    /**
     * Vypocte aktualni zrychleni space objectu
     */
    private void calculateCurrentA(){

        double vector1, vector2, jmenovatel, jmenovatelNaTreti, citatelx, citately;

        for(int i = 0; i < spaceObjects.length; i++){

            double zlomekx = 0;
            double zlomeky = 0;

            for(int j = 0; j < spaceObjects.length; j++){

                if(i != j) {

                    vector1 = (spaceObjects[i].getPosX() - spaceObjects[j].getPosX()) * (spaceObjects[i].getPosX() - spaceObjects[j].getPosX());
                    vector2 = (spaceObjects[i].getPosY() - spaceObjects[j].getPosY()) * (spaceObjects[i].getPosY() - spaceObjects[j].getPosY());

                    jmenovatel = Math.sqrt(vector1 + vector2);
                    jmenovatelNaTreti = jmenovatel * jmenovatel * jmenovatel;
                    citatelx = spaceObjects[j].getPosX() - spaceObjects[i].getPosX();
                    citately = spaceObjects[j].getPosY() - spaceObjects[i].getPosY();

                    zlomekx += (citatelx/jmenovatelNaTreti)*spaceObjects[j].getWeight();
                    zlomeky += (citately/jmenovatelNaTreti)*spaceObjects[j].getWeight();
                }

            }

            spaceObjects[i].setaX(zlomekx * gConstant);
            spaceObjects[i].setaY(zlomeky * gConstant);

        }
    }

    /** Prepocitava rychlosti a zrychleni spaceObjectu*/
    public void updateSystem(double t) {

        double deltaT_min = spaceObjects.length *  step / 10000;

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
     * Zastavi nebo spusti beh simulace, vypocitava trvani celkoveho casu stopnuti
     */
    public void isSpaceBarPressed(){

        if(timeIsRunning) {
            timeIsRunning = false;
            return;
        }
        simulationTimeStopped = System.currentTimeMillis() - simulationTimeStart - simulationTimeBefore;
        timeIsRunning = true;
    }

    /**
     * Funkce vypocte zda doslo ke kolizi
     * Pokud dojde ke kolizi, vytvori nove pole do ktereho vlozi vznikly object a vynecha objecty ktere se ucastnili kolize
     * @param object spaceObject u ktereho kontrolujeme zda se nesrazil s ostatnima
     * @return pokud doslo ke kolizi vrati true
     */
    public static boolean calculateCollision(ASpaceObject object){

        double objectX = object.getPosX();
        double objectY = object.getPosY();
        double objectR = object.getr();
        double objectWeight = object.getWeight();

        for(int i = 0; i < spaceObjects.length; i++){
            if(spaceObjects[i] != object){

                double distanceX = (spaceObjects[i].getPosX() - objectX) * (spaceObjects[i].getPosX() - objectX);
                double distanceY = (spaceObjects[i].getPosY() - objectY) * (spaceObjects[i].getPosY() - objectY);
                double distance = Math.sqrt(distanceX + distanceY);

                if(distance < objectR + spaceObjects[i].getr()){

                    ASpaceObject[] newSpaceObjects = new ASpaceObject[spaceObjects.length-1];

                    double newX = (objectX + spaceObjects[i].getPosX())/2;
                    double newY = (objectY + spaceObjects[i].getPosY())/2;

                    double newVelX = ((objectWeight * object.getVelX()) + (spaceObjects[i].getWeight() * spaceObjects[i].getVelX())) / (objectWeight + spaceObjects[i].getWeight());
                    double newVelY = ((objectWeight * object.getVelY()) + (spaceObjects[i].getWeight() * spaceObjects[i].getVelY())) / (objectWeight + spaceObjects[i].getWeight());

                    ASpaceObject newCollision = new Planet("newCollision", newX, newY, newVelX, newVelY, objectWeight + spaceObjects[i].getWeight());

                    int j = 0;
                    for (ASpaceObject spaceObject: spaceObjects) {
                        if(spaceObject != spaceObjects[i] && spaceObject != object){
                            newSpaceObjects[j] = spaceObject;
                            j++;
                        }
                    }

                    newSpaceObjects[j] = newCollision;

                    spaceObjects = newSpaceObjects;

                    DrawingPanel.setSpaceObjects(newSpaceObjects);

                    return true;

                }



            }
        }

        return false;
    }



}
