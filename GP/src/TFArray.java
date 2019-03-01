import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.Scanner;

public class TFArray {

    Map <String,Map<String,Integer>> Corpus;

    Map <String, Integer> IDF;

    Vector<String> query;

    Vector<Vector<String>> judge;


    public TFArray(){

        this.query = new Vector<String >();
        this.judge = new Vector<Vector<String>>();
        this.Corpus = new HashMap<String,Map<String,Integer>>();
        this.IDF = new HashMap<String, Integer>();


//        this.addTFArray();
//        Configuration.writeObjToFile(Corpus,"corpusObj");
//
//        this.addIDF();
//        Configuration.writeObjToFile(IDF,"IDFObj");

        this.loadQueryAndJudge();
        this.loadObjFromFile();

    }


    private void loadQueryAndJudge() {

        query.add("تلفات جاده آمار");
        query.add("انرژي هسته قوانين");
        query.add("مربي فوتبال ايران");
        query.add("تحريم اقتصادي ايران");
        query.add("باغباني گل آموزش");

        Vector <String> q1 = new Vector<>();
        Vector <String> q2 = new Vector<>();
        Vector <String> q3 = new Vector<>();
        Vector <String> q4 = new Vector<>();
        Vector <String> q5 = new Vector<>();

        judge.add(q1);
        judge.add(q2);
        judge.add(q3);
        judge.add(q4);
        judge.add(q5);

        Scanner scan = null;

        try {

            scan = new Scanner(new File("judgment.txt"));

            while(scan.hasNextLine()){

                String line = scan.nextLine();
                //Here you can manipulate the string the way you want
                String[] token = line.split("\t");
                judge.elementAt(Integer.parseInt(token[0])-1).add(token[1].trim());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Map <String, Integer> tokenizeQuery(String query){


        Map <String,Integer> query_map = new HashMap<String, Integer>();
        String[] queryWords = query.split(" ");


        for (String word: queryWords){

            if (query_map.containsKey(word)){

                int i =query_map.get(word);
                i+=1;
                query_map.replace(word,i);
            }
            else {

                query_map.put(word,1);
            }
        }

        return query_map;

    }


    private void addTFArray(){

        String corpuses;
        String docIDs;

        try {

            corpuses = new String(Files.readAllBytes(Paths.get("corpus.txt")));
            String[] corpus = corpuses.split("\\*\\*\\*\\*\\*\\*\\*\\*\\*");

            docIDs = new String(Files.readAllBytes(Paths.get("docID.txt")));
            String[] docID = docIDs.split("\\*\\*\\*\\*\\*\\*\\*\\*\\*");

            int length= corpus.length;

            for(int i=0 ;i< length; i++){

                addDocToArray(docID[i], corpus[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void addIDF(){

        String distinctWord;
        String wordCounts;
        try {

            distinctWord = new String(Files.readAllBytes(Paths.get("distinct.txt")));
            String[] words = distinctWord.split("#####");

            wordCounts = new String(Files.readAllBytes(Paths.get("count.txt")));
            String[] counts = wordCounts.split("#####");

            int length =words.length;


            for(int i=0 ;i< length; i++){

                if (IDF.containsKey(words[i].trim())){

                    int tmp = IDF.get(words[i].trim());
                    tmp += Double.valueOf(counts[i]).intValue();
                    IDF.replace(words[i].trim(), tmp);
                }
                else {
                    IDF.put(words[i].trim(), Double.valueOf(counts[i]).intValue());
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    private void addDocToArray (String docID, String docCorpus){

        Map <String,Integer> doc = new HashMap<String, Integer>();
        String[] words = docCorpus.split(" ");

        for (String word: words){
            if (doc.containsKey(word.trim())){

                int i =doc.get(word.trim());
                i=i+1;
                doc.replace(word.trim(),i);
            }

            else {

                doc.put(word.trim(),1);
            }
        }

        this.Corpus.put(docID, doc);
    }


    public Map<String, Double> getDataForMap(String word, Map<String,Integer> data){

        Map <String, Double> res = new HashMap<String, Double>();

        int max_TF=0;
        int sum=0;

        for (Map.Entry<String, Integer> elem :data.entrySet() ){

            sum+= elem.getValue();

            if(elem.getValue()>max_TF)
                max_TF = elem.getValue();
        }

        if(IDF.containsKey(word))
            res.put("IDF", Double.valueOf(IDF.get(word)));

        else {
            res.put("IDF", 0.5);
//            System.out.println("not Found IDF of word: "+ word);
        }

        res.put("TF_max", Double.valueOf(max_TF));
        res.put("Length",Double.valueOf(sum));
        res.put("TF_avg", Double.valueOf(sum/data.size()));
        res.put("n",Double.valueOf(data.size()));

        if (data.get(word)!= null){
            res.put("TF", Double.valueOf(data.get(word)/sum));
        }

        return res;
    }


    private void loadObjFromFile(){

            try {

                FileInputStream fi = new FileInputStream(new File("IDFObj.txt"));
                ObjectInputStream oi = new ObjectInputStream(fi);

                IDF = (Map<String, Integer>) oi.readObject();

                fi = new FileInputStream(new File("corpusObj.txt"));
                oi = new ObjectInputStream(fi);

                // Read objects
                Corpus = (Map<String, Map<String, Integer>>) oi.readObject();


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


    public Map<String, Map<String, Integer>> getCorpus() {
        return Corpus;
    }


    public Map<String, Integer> getIDF() {
        return IDF;
    }

}
