import java.util.*;

public class CalculateRankingFunc {

    private Map<String, Double> values;
    private double R;

    public CalculateRankingFunc(){

        //new class for values

    }


    public void setValues(Map<String, Double> values, double R) {
        this.values = values;
        this.R = R;
    }


    public double calculate(String tree,Map<String, Double> values_ , double R){

        setValues(values_, R);
        Stack operand = new Stack<String>();
        Stack values = new Stack<Double>();

        String[] array = tree.split(" ");

        for (String elem:array) {

            if (elem.equalsIgnoreCase("(")){
                operand.push("(");
            }

            else if (Configuration.isFunc(elem)) {

                operand.push(elem);
            }

            else if (Configuration.isTerminal(elem))
                values.push(getValueofTerm(elem));

            else if (elem.equalsIgnoreCase(")")){

                String op = (String) operand.pop();

                if(op.equalsIgnoreCase("("))
                    continue;

                double val1 = (double) values.pop();

                double val2 =0.0;

                if(!op.equalsIgnoreCase("Log"))
                    val2 = (double) values.pop();

                values.push(calc(op,val1,val2));
            }

        }

        return (double) values.pop();
    }


    private double calc(String op, double val1, double val2) {

        if (op.equalsIgnoreCase("Log")){

            if (val1>0)
                return Math.log10(val1);

            return 0;
        }

        if (op.equalsIgnoreCase("mult")){

            return val1*val2;
        }

        if (op.equalsIgnoreCase("sum")){

            return val1+val2;
        }

        if (op.equalsIgnoreCase("division")){

            if (val2!=0)
                return val1/val2;

            return 0.0;
        }

        return 0.0;
    }


    private double getValueofTerm(String term){

        if (term.equalsIgnoreCase("TF"))
            return values.get("TF");

        if (term.equalsIgnoreCase("IDF"))
            return values.get("IDF");

        if (term.equalsIgnoreCase("TF_avg"))
            return values.get("TF_avg");

        if (term.equalsIgnoreCase("TF_max"))
            return values.get("TF_max");

        if (term.equalsIgnoreCase("Length"))
            return values.get("Length");

        if (term.equalsIgnoreCase("R"))
            return R;

        if (term.equalsIgnoreCase("n"))
            return values.get("n");

        return 0;
    }

}

