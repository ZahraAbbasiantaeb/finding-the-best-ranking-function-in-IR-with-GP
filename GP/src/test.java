import java.io.*;
import java.util.Map;

public class test {


    Map <Integer, Map<String, Double>> best_Individuals;

    public static void main(String[] args) {

        test t = new test();
        t.check();
//        TFArray tf = new TFArray();
//        Evaluate ev = new Evaluate();
        GP myGP = new GP();

    }

    public  void check(){

        try {

            FileInputStream fi = new FileInputStream(new File("bestIndividualsObj.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            best_Individuals = (Map <Integer, Map<String, Double>>) oi.readObject();

            for (Map.Entry<Integer, Map<String, Double>> generation: best_Individuals.entrySet()) {

                Double sum=0.0;
                Double max = 0.0;
                String best_index = "";

                for (Map.Entry<String, Double> fitness : generation.getValue().entrySet()){
                    sum += fitness.getValue();

                    if(fitness.getValue()>max){
                        max = fitness.getValue();
                        best_index=fitness.getKey();
                    }
                }

//                System.out.println(max);
//                System.out.println(sum/Configuration.BestPopulation);
//                System.out.println(best_index);
            }
            System.out.println(best_Individuals);
            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
