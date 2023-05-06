import java.util.List;

public class test {
    public static void main(String[] args) {
        Node init = new Node(0,0);
        Node end = new Node(1,9);
        int[][] blocked = {
                new int[]{1,4},
                new int[]{2,4},
                new int[]{3,2},
                new int[]{3,3},
                new int[]{2,2},
        };

        AStar search = new AStar(10, 10, init, end);
        search.setBlocks(blocked);

        List<Node> res = search.findPath();

        for (Node item : res){
            System.out.println(item);
        }
    }


}
