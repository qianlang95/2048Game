
public class Tile {
    private int value;
    private int[] position;

    public Tile() {
        this.value = 0;
    }

    public Tile(int value, int[] position) {
        this.value = value;
        this.position = position;
    }

    public int getValue(){
        return this.value;
    }

    public int[] getPosition() {
        return this.position;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public boolean hasMoved(int row, int col) {
        return (row != position[0] || col != position[1]);
    }

    public String toString() {
        int row = position[0];
        int col = position[1];
        String output = "Tile is: " + this.value + "\n";
        output += "Position: [" + row + ", " + col + "]";
        return output;
    }

    public static void main(String[] args) {
        Tile tile = new Tile(4, new int[] {2, 3});
        System.out.println(tile);
    }

}
