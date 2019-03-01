
import java.io.*;
import java.util.Arrays;

public class Configuration {

    public static final int TournamentSize = 6;
    public static final int PopulationSize = 50;

    public static final int Generation = 10;

    public static final int MaximumDepth = 5;
    public static final int EachDepthTreeCount = 5;

    public static final int BestPopulation = 10;

    public static final String[] Terminals = {"TF_max", "TF_avg","TF", "IDF", "n", "Length", "R"};
    public static final String[] Functions = {"mult", "sum", "division", "Log"};


    public static final int TerminalSize = 7 ;
    public static final int FunctionSize = 3 ;

    public static final boolean isFunc(String func){

        return (Arrays.asList(Functions).contains(func));
    }

    public static final boolean isTerminal (String term){

        return (Arrays.asList(Terminals).contains(term));
    }

    public static final void writeObjToFile(Object obj, String path){

        FileOutputStream f = null;
        try {

            f = new FileOutputStream(new File(path+".txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(obj);

            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
