class Node
{
    String key;
    Node left, right;

    public Node(String item) {
        key = item;
        left = right = null;
    }

     public void updateValue (String value){

        this.key=value;
     }

    public String getKey() {
        return key;
    }

}