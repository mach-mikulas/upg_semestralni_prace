import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Ze souboru nacte udaje pro simulaci a vytvori pole spaceObjectu
 * @author Mikulas Mach
 */
public class Input {

    /** Cesta ke k souboru se vstupnimi daty*/
    private String file;
    /** Gravitacni konstanta*/
    private double gConstant;
    /** Krok simulace*/
    private double step;

    /**
     * Konstruktor priradi hodnoty atributum a vypocita polomer spaceObjectu
     * @param filePath cesta k souboru se vstupnimi daty
     */
    public Input(String filePath){
        this.file = filePath;
    }

    /**
     * Vrati Arraylist ASpaceObject ktery nacte z csv souboru
     * a ulozi co atributu tridy gConstant a step udaje z první radky
     * @return Arraylist ASpaceObject
     */
    public ArrayList<ASpaceObject> getInput(){

        ArrayList<ASpaceObject> input = new ArrayList<ASpaceObject>();
        String line;

        try {
            BufferedReader bfr = new BufferedReader(new FileReader(file));

            line = bfr.readLine();

            String[] firstLine = line.split(",");
            gConstant = Double.parseDouble(firstLine[0]);
            step = Double.parseDouble(firstLine[1]);

            while((line = bfr.readLine()) != null){
                String[] objectLine = line.split(",");

                if(objectLine[1].equals("Planet")){
                    input.add(new Planet(objectLine[0], Double.parseDouble(objectLine[2]), Double.parseDouble(objectLine[3]), Double.parseDouble(objectLine[4]), Double.parseDouble(objectLine[5]), Double.parseDouble(objectLine[6])));
                }

                else if(objectLine[1].equals("Comet")){
                    input.add(new Comet(objectLine[0], Double.parseDouble(objectLine[2]), Double.parseDouble(objectLine[3]), Double.parseDouble(objectLine[4]), Double.parseDouble(objectLine[5]), Double.parseDouble(objectLine[6])));
                }

                else{
                    input.add(new Rocket(objectLine[0], Double.parseDouble(objectLine[2]), Double.parseDouble(objectLine[3]), Double.parseDouble(objectLine[4]), Double.parseDouble(objectLine[5]), Double.parseDouble(objectLine[6])));
                }

            }
            bfr.close();
        }

        catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        return input;
    }

    /**
     * Getter
     * @return double gConstant - Gravitacni konstanta simulace
     */
    public double getgConstant() {
        return gConstant;
    }

    /**
     * Getter
     * @return double step - Krok simulace
     */
    public double getStep() {
        return step;
    }
}
