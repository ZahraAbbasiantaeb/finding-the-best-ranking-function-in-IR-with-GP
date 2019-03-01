import java.util.*;

public class Evaluate {

    private TFArray my_TFArray;
    private CalculateRankingFunc my_calculator;
    private MyComparator2 comparator;

    public Evaluate(){

        this.my_TFArray= new TFArray();
        this.my_calculator= new CalculateRankingFunc();
//        evaluateQueries(" ( Log  (  ( Length division TF_max  ) sum  ( TF_max division TF_max  )  )  ) ",1.2);

    }


    private Map <String ,Double> setWeightsForQuery(String query, String bTree, Double R){

        Map <String ,Integer> query_map = my_TFArray.tokenizeQuery(query);
        Map <String ,Double> query_weights = new HashMap<String ,Double>();

        double weight = 0.0 ;

        for (Map.Entry <String ,Integer> tuple: query_map.entrySet()){

            weight = my_calculator.calculate(bTree,my_TFArray.getDataForMap(tuple.getKey(),query_map), R);
            query_weights.put(tuple.getKey(), weight);

        }

        return query_weights;
    }


    private Map <String ,Double> setWeightsForDoc(String docKey, String bTree, Double R){

        Map <String ,Integer> doc_map = my_TFArray.getCorpus().get(docKey);
        Map <String ,Double> query_weights = new HashMap<String ,Double>();

        double weight = 0.0 ;

        for (Map.Entry <String ,Integer> tuple: doc_map.entrySet()){

            weight = my_calculator.calculate(bTree,my_TFArray.getDataForMap(tuple.getKey(),doc_map), R);
            query_weights.put(tuple.getKey(), weight);

        }

        return query_weights;
    }


    private Double cosineSimilarity (Map <String ,Double> vec1, Map <String ,Double> vec2){

        Double sum_vec1 = 0.0;
        Double sum_vec2 = 0.0;
        Double sum = 0.0;


        for (Map.Entry <String, Double> term1 : vec1.entrySet()){

            sum_vec1 += Math.pow(term1.getValue(), 2);

            if (vec2.containsKey(term1.getKey())){

                 sum += term1.getValue()*vec2.get(term1.getKey());

             }

        }

        for (Map.Entry<String, Double> term2 : vec2.entrySet()){

            sum_vec2+=Math.pow(term2.getValue(),2);

        }

        return sum/(sum_vec1+sum_vec2);

    }


    public Double evaluateQueries(String bTree, Double R){

        Double x = 0.0;

        for (int i = 0; i< 5; i++){

            Vector<String> relDocs = getQueryRelevantDocs(my_TFArray.query.elementAt(i) , bTree, R);

            x += getPrecisionAvg(relDocs, my_TFArray.judge.elementAt(i));

        }

        x=x/5;

        System.out.println(x);

        return (x);

    }


    private double getPrecisionAvg(Vector<String> retrieved, Vector<String> relevance){

        Vector<Integer> index = new Vector<Integer>();

        for (String term: relevance){

            if (retrieved.contains(term)){

                index.add(retrieved.indexOf(term));
            }
        }

        int total = relevance.size();
        Double sum = 0.0;
        int count = 1;

        Collections.sort(index);

        for (Integer rank: index){

            sum += (count/(rank+1));
            count += 1;
        }

        return (sum/total);
    }


    private Vector<String> getQueryRelevantDocs (String query, String bTree, Double R){

        Map <String ,Double> query_Map = setWeightsForQuery(query,bTree,R);
        Map <String ,Double> similarity = new HashMap<String ,Double>();

        for (Map.Entry <String,Map<String,Integer>> tuple : my_TFArray.getCorpus().entrySet()){

            Double sim = cosineSimilarity(setWeightsForDoc(tuple.getKey(), bTree, R),query_Map);

            if(sim>0){
                similarity.put(tuple.getKey().trim(), sim);
            }

        }

        // sort


        comparator  = new MyComparator2(similarity);

        Map<String, Double> sorted_sim = new TreeMap<String, Double>(comparator);

        sorted_sim.putAll(similarity);

        Vector<String> relevance = new Vector<String>();


        for( Map.Entry<String, Double> tuple : sorted_sim.entrySet()){

            relevance.add(tuple.getKey());
        }

        return relevance;
    }


}
