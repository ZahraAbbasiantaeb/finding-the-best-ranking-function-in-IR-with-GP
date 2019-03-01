import java.util.*;

public class GP {

    private Map<BinaryTree, Double> individuals;

    private MyComparator comparator ;

    private Evaluate my_Evaluate ;

    private String treeRepresentation;

    public Random rand = new Random();

    private  Map<Integer, Map<String, Double>> best_Individuals;

    private Integer current_generation;


    public GP (){

        this.current_generation=1;

        individuals = new HashMap<BinaryTree, Double>() ;

        my_Evaluate = new Evaluate();

        best_Individuals = new HashMap<Integer, Map<String, Double>>();

        this.runGP();

    }


    private BinaryTree full (int treeDepth) {

        BinaryTree tree = new BinaryTree();
        tree.root = new Node("X");
        add_to_full(tree.root,1, treeDepth);
        return tree;

    }


    private void add_to_full(Node root, int depth, int max_depth){

        if (max_depth==depth) {

            updateNode(root);
            return;
        }

        else {

            root.right = new Node("X");
            root.left = new Node("X");
            add_to_full(root.right, depth+1, max_depth);
            add_to_full(root.left, depth+1, max_depth);
        }

        updateNode(root);
    }


    private BinaryTree grow (int max_depth){

        BinaryTree tree = new BinaryTree();
        tree.root = new Node("X");
        add_to_grow(tree.root,1, max_depth);
        return tree;

    }


    private void add_to_grow(Node root, int hight, int max_depth) {

        if (max_depth==hight) {

            updateNode(root);
            return;
        }

        else {

            double tmp = Math.random();

            if(tmp>0.3){

                root.right = new Node("X");
                add_to_grow(root.right, hight+1, max_depth);
            }

            tmp = Math.random();

            if(tmp > 0.3){

                root.left = new Node("X");
                add_to_grow(root.left, hight+1, max_depth);
            }

            updateNode(root);
        }
    }


    private void updateNode(Node root){

        if (root.right!=null && root.left!=null) {

            String func = Configuration.Functions[rand.nextInt(Configuration.FunctionSize)];
            root.updateValue(func);
        }

        else if (root.right==null && root.left==null){

            String term = Configuration.Terminals[rand.nextInt(Configuration.TerminalSize)];
            root.updateValue(term);
        }

        else
            root.updateValue("Log");
    }


    private int getDepth(Node n){

        if (n==null)
            return 0;

        int depth = 1+ Math.max(getDepth(n.right),getDepth(n.left));

        return depth;
    }


    private LinkedList<BinaryTree> crossOver(BinaryTree parent1, BinaryTree parent2){

        while (true){

            parent1.setRandomNum();
            parent2.setRandomNum();
            parent1.setNewRandomNode();
            parent2.setNewRandomNode();


            int first_child_length = getDepth(parent1.getRandNode());
            int second_child_length = getDepth(parent2.getRandNode());

            int parent_one_length = getDepth(parent1.getRoot());
            int parent_two_length = getDepth(parent2.getRoot());

            if ((parent_one_length + second_child_length - first_child_length <= 5) &&
                    (parent_two_length - second_child_length + first_child_length <= 5)) {

                return replaceSubTrees(parent1, parent2);

            }
        }

    }


    private LinkedList<BinaryTree> replaceSubTrees(BinaryTree parent1, BinaryTree parent2){

        LinkedList<BinaryTree> childs = new LinkedList<BinaryTree>();

        BinaryTree offspring_one = cloneBTree(parent1);
        BinaryTree offspring_two = cloneBTree(parent2);

        copySubTree(offspring_one.getRandNode(), parent2.getRandNode());
        copySubTree(offspring_two.getRandNode(), parent1.getRandNode());

        childs.add(offspring_one);
        childs.add(offspring_two);

        return childs;

    }


    private void copySubTree(Node randNode, Node randNode1) {

        randNode.updateValue(randNode1.getKey());

        if(randNode1.right == null){
            randNode.right=null;
        }

        else {
            randNode.right = new Node("");
            copySubTree(randNode.right, randNode1.right);
        }

        if(randNode1.left == null){
            randNode.left=null;
        }

        else {
            randNode.left = new Node("");
            copySubTree(randNode.left, randNode1.left);
        }
    }


    private void printInorder(Node node) {

        if (node == null)
            return;

        if(node.left!=null || node.right!=null)
            treeRepresentation+=" ( ";


        printInorder(node.left);



        treeRepresentation += " "+node.key+" ";

        printInorder(node.right);

        if(node.left!=null || node.right!=null)
            treeRepresentation+=" ) ";

    }


    private BinaryTree cloneBTree (BinaryTree obj){

        BinaryTree btree =  new BinaryTree();

        btree.root = new Node(obj.root.key);

        copy(btree.root,obj.root);

        btree.setRandomNum(obj.getRandomNum());

        btree.setNewRandomNode();

        return btree;
    }


    private void copy(Node root, Node root1) {

        if (root1.right!=null){

            root.right = new Node(root1.right.key);
            copy(root.right,root1.right);
        }

        if (root1.left!=null){

            root.left = new Node(root1.left.key);
            copy(root.left,root1.left);
        }

    }


    private LinkedList<BinaryTree> tournament (){

        int randomNum = 0;

        double first_best = 0;
        double second_best = 0;

        int first_index = 0;
        int second_index = 0;

        BinaryTree[] keys = individuals.keySet().toArray(new BinaryTree[individuals.size()]);

        for (int i = 0; i < Configuration.TournamentSize; i++){

            randomNum = rand.nextInt(Configuration.PopulationSize);

            if (individuals.get(keys[randomNum]) >= first_best){

                second_best = first_best;
                second_index = first_index;
                first_best = individuals.get(keys[randomNum]);
                first_index = randomNum;
            }

            else if(individuals.get(keys[randomNum]) >= second_best){

                second_best = individuals.get(keys[randomNum]);
                second_index = randomNum;
            }

        }

        return crossOver(keys[first_index],keys[second_index]);
    }


    private void initialPopulation(){

        for (int i=1; i<=Configuration.MaximumDepth; i++){

            for (int j=0; j<Configuration.EachDepthTreeCount; j++){

                BinaryTree bt1 = grow(i);
                BinaryTree bt2 = full(i);

                individuals.put(bt1,getFitness(bt1));
                individuals.put(bt2,getFitness(bt2));

            }
        }
    }


    private Double getFitness(BinaryTree binaryTree) {

        treeRepresentation = "";

        printInorder(binaryTree.root);
        System.out.println(treeRepresentation);
        return my_Evaluate.evaluateQueries(treeRepresentation,Math.random()*5);

    }


    private Map<BinaryTree, Double> nextGeneration(){

        comparator  = new MyComparator(individuals);

        Map<BinaryTree, Double> sorted_individual = new TreeMap<BinaryTree, Double>(comparator);

        sorted_individual.putAll(individuals);

        Map<BinaryTree, Double> next_generation = new HashMap<BinaryTree, Double>() ;

        Map<String, Double> best = new HashMap <String, Double>();

        int i = 0;

        // add BestPopulation count of individuals to next generation
        for (Map.Entry<BinaryTree, Double> elem: sorted_individual.entrySet()) {

            if (i<Configuration.BestPopulation){

                next_generation.put(elem.getKey(),elem.getValue());

                treeRepresentation = "";

                printInorder(elem.getKey().getRoot());

                best.put(treeRepresentation,elem.getValue());

                i++;
            }

            else
                break;
        }


        best_Individuals.put(current_generation, best);

        // obtain other individuals with croosOver
        i=1;

        while (next_generation.size() < Configuration.PopulationSize) {

            i++;

            LinkedList<BinaryTree> trees = tournament();

            for ( BinaryTree tree: trees) {
                next_generation.put(tree, getFitness(tree));
            }

        }

        return next_generation;
    }


    private void runGP(){

        initialPopulation();

        while (current_generation <= Configuration.Generation){

            setIndividuals(nextGeneration());
            Configuration.writeObjToFile(best_Individuals,"bestIndividualsObj");
            current_generation+=1;

        }

    }


    public void setIndividuals(Map<BinaryTree, Double> individuals) {

        this.individuals = individuals;
    }


}
