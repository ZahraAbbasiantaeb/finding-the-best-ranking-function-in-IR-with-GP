import java.util.Random;

class BinaryTree
{
    Node root;
    Node randNode;
    int count;
    int randomNum;

    BinaryTree()
    {
        root = null;
    }


    public Node setNewRandomNode(){
        setCount(-1) ;
        randomTraversal(root);
        return getRandNode();
    }


    private void randomTraversal(Node root){

        count++;

        if(count == randomNum)
            setRandNode(root);

        if(root.left != null)
            randomTraversal(root.left);

        if(root.right != null)
            randomTraversal(root.right);

    }


    public Node getRandNode() {
        return randNode;
    }


    public void setRandNode(Node randNode) {
        this.randNode = randNode;
    }


    public void setCount(int count) {
        this.count = count;
    }


    public int getLength (Node root){

        if (root == null){
            return 0;
        }

        return 1+ getLength(root.right)+ getLength(root.left);

    }


    public void setRandomNum() {

        Random rand = new Random();
        int treesize = getLength(root) ;
        this.randomNum = rand.nextInt(treesize);

    }


    public void setRandomNum(int treesize) {

        this.randomNum = treesize;
    }


    public int getRandomNum() {
        return randomNum;
    }


    public Node getRoot() {
        return root;
    }


}