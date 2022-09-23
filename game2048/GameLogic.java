
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Random;

public class GameLogic {
    private int size;
    private int totalScore;
    private int numOfZeroTiles;
    private boolean gameOver;
    private boolean winWith2048;
    private int maxValueOnTile;
    private boolean boardStatusChanged;
    private Tile[][] board;

    public GameLogic() {
        size = 4;
        totalScore = 0;
        numOfZeroTiles = 16;
        gameOver = false;
        maxValueOnTile = 0;
        boardStatusChanged = true;
        board = new Tile[size][size];

        //two tiles with value 2 would be initialized for a new game
        for (int i = 0 ; i < 2; i++) {
            boardStatusChanged = true;
            generateRandomTile();

        }
        GameTrigger.USER_ACTION.invokeKey();
    }

    //generate a random tile with value 2 or 4 (80% : 20%) at an zero position
    public void  generateRandomTile() {
        if (isGameOver() || !boardStatusChanged) {
            return;
        }

        int value = 0;
        if (Math.random() < 0.8) {
            value = 2;
        } else {
            value = 4;
        }
        int row = 0;
        int col = 0;
        Random random = new Random();
        do {
            row = random.nextInt(size);
            col = random.nextInt(size);
        } while (board[row][col] != null);
        board[row][col] = new Tile(value, new int[]{row, col});
        maxValueOnTile = Math.max(maxValueOnTile, value);
        numOfZeroTiles--;
        boardStatusChanged = false;
    }

    public List<Tile> getTileValueListInRow(int row) {
        List<Tile> tileList = new ArrayList<>();
        for (int col = 0; col < size; col++) {
            if (board[row][col] != null) {
                tileList.add(board[row][col]);
            }
        }
        return tileList;
    }

    public List<Tile> getTileValueListInCol(int col) {
        List<Tile> tileList = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            if (board[row][col] != null) {
                tileList.add(board[row][col]);
            }
        }
        return tileList;
    }

    public List<Tile> mergeTileList(List<Tile> tileList) {
        for (int i = 0; i < tileList.size() - 1; i++) {
            if (tileList.get(i) == null) {
                continue;
            }
            tileList = mergeTwoTiles(tileList, i, i + 1);
        }
        return tileList;
    }

    public List<Tile> mergeTileListReversed(List<Tile> tileList) {
        for (int i = tileList.size() - 1; i > 0; i--) {
            if (tileList.get(i) == null) {
                continue;
            }
            tileList = mergeTwoTiles(tileList, i, i - 1);
        }
        return tileList;
    }

    public List<Tile> mergeTwoTiles(List<Tile> tileList, int tileOne, int tileTwo) {
        int currentValue = tileList.get(tileOne).getValue();
        if (currentValue == tileList.get(tileTwo).getValue()) {
            tileList.get(tileOne).setValue(currentValue * 2);
            tileList.set(tileTwo, null);
            numOfZeroTiles++;
            totalScore += currentValue;
            maxValueOnTile = Math.max(maxValueOnTile, tileList.get(tileOne).getValue());
            boardStatusChanged = true;
            if (maxValueOnTile >= 2048) {
                winWith2048 = true;
                gameOver = true;
            }
        }
        return tileList;
    }

    public void updateBoardInUpDirection(List<Tile> tileList, int col) {
        int tileAdded = 0;
        int index = 0;
        for (int row = 0; row < tileList.size(); row++) {
            Tile tile = tileList.get(row);
            if (tile == null) {
                continue;
            }
            if (tile.hasMoved(index, col)) {
                boardStatusChanged = true;
                tile.setPosition(new int[]{index, col});
                board[index][col] = tile;
            }

            index++;
            tileAdded++;
        }
        for (int i = tileAdded; i < size; i++) {
            board[index][col] = null;
            index++;
        }
    }

    public void updateBoardInDownDirection(List<Tile> tileList, int col) {
        int tileAdded = 0;
        int index = size - 1;
        for (int row = tileList.size() - 1; row >= 0; row--) {
            Tile tile = tileList.get(row);
            if (tile == null) {
                continue;
            }
            if (tile.hasMoved(index, col)) {
                boardStatusChanged = true;
                tile.setPosition(new int[]{index, col});
                board[index][col] = tile;
            }

            index--;
            tileAdded++;
        }
        for (int i = tileAdded; i < size; i++) {
            board[index][col] = null;
            index--;
        }
    }

    public void updateBoardInLeftDirection(List<Tile> tileList, int row) {
        int tileAdded = 0;
        int index = 0;
        for (int col = 0; col < tileList.size(); col++) {
            Tile tile = tileList.get(col);
            if (tile == null) {
                continue;
            }
            if (tile.hasMoved(row, index)) {
                boardStatusChanged = true;
                tile.setPosition(new int[]{row, index});
                board[row][index] = tile;
            }

            index ++;
            tileAdded++;
        }
        for (int i = tileAdded; i < size; i++) {
            board[row][index] = null;
            index ++;
        }
    }

    public void updateBoardInRightDirection(List<Tile> tileList, int row) {
        int tileAdded = 0;
        int index = size - 1;
        for (int col = tileList.size() - 1; col >= 0; col--) {
            Tile tile = tileList.get(col);
            if (tile == null) {
                continue;
            }

            if (tile.hasMoved(row, index)) {
                boardStatusChanged = true;
                tile.setPosition(new int[]{row, index});
                board[row][index] = tile;
            }

            index--;
            tileAdded++;
        }
        for (int i = tileAdded; i < size; i++) {
            board[row][index] = null;
            index--;
        }
    }

    public void shiftUp() {
        for (int col = 0; col < size; col++) {
            List<Tile> tileList = getTileValueListInCol(col);
            tileList = mergeTileList(tileList);
            updateBoardInUpDirection(tileList, col);
        }
        generateRandomTile();
    }

    public void shiftDown() {
        for (int col = 0; col < size; col++) {
            List<Tile> tileList = getTileValueListInCol(col);
            tileList = mergeTileListReversed(tileList);
            updateBoardInDownDirection(tileList, col);
        }
        generateRandomTile();
    }

    public void shiftTowardsLeft() {
        for (int row = 0; row < size; row++) {
            List<Tile> tileList = getTileValueListInRow(row);
            tileList = mergeTileList(tileList);
            updateBoardInLeftDirection(tileList, row);
        }
        generateRandomTile();
    }

    public void shiftTowardsRight(){
        for (int row = 0; row < size; row++) {
            List<Tile> tileList = getTileValueListInRow(row);
            tileList = mergeTileListReversed(tileList);
            updateBoardInRightDirection(tileList, row);
        }
        generateRandomTile();
    }

    public boolean canStillMerge() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {

                if ((board[i][j] != null && board[i][j + 1] != null)
                        && board[i][j].getValue() == board[i][j + 1].getValue()) {
                    return true;
                }
                if ((board[j][i] != null && board[j + 1][i] != null)
                        && board[j][i].getValue() == board[j + 1][i].getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWinWith2048() {
        return winWith2048;
    }

    public boolean isBoardFull() {
        return this.numOfZeroTiles == 0;
    }

    public boolean isGameOver() {
        if ((isBoardFull() && !canStillMerge())|| isWinWith2048()) {
            gameOver = true;
        }
        return gameOver;
    }

    public int getTotalScore() {
        return this.totalScore;
    }

    public Tile getTile(int row, int col) {
        return board[row][col];
    }

    public String toString() {
        Formatter boardPattern = new Formatter();
        boardPattern.format("---------------------\n");
        for (int r = 0; r < this.size; r++) {
            for (int c = 0; c < this.size; c++) {
                if (board[r][c] == null) {
                    boardPattern.format("|    ");
                } else {
                    boardPattern.format("|%4d", board[r][c].getValue());
                }
            }
            boardPattern.format("|\n");
            boardPattern.format("---------------------\n");
        }
        String output = boardPattern.toString();
        output += "Total score: " + totalScore + "\n";
        output += "Max tile value: " + maxValueOnTile + "\n";
        output += "Number of zero tiles: " + numOfZeroTiles + "\n";
        return output;
    }

    public static void main(String[] args) {
        GameLogic gameBoard = new GameLogic();
        System.out.println(gameBoard);

        System.out.println("Left");
        gameBoard.shiftTowardsLeft();
        System.out.println(gameBoard);

        System.out.println("Up");
        gameBoard.shiftUp();
        System.out.println(gameBoard);

        System.out.println("Down");
        gameBoard.shiftDown();
        System.out.println(gameBoard);

        System.out.println("Right");
        gameBoard.shiftTowardsRight();
        System.out.println(gameBoard);

        for (int i = 0; i < 50; i++) {
            System.out.println(i);
            System.out.println("Left");
            gameBoard.shiftTowardsLeft();
            System.out.println(gameBoard);
            System.out.println("Up");
            gameBoard.shiftUp();
            System.out.println(gameBoard);
            System.out.println("Down");
            gameBoard.shiftDown();
            System.out.println(gameBoard);
            System.out.println("Right");
            gameBoard.shiftTowardsRight();
            System.out.println(gameBoard);
        }
        System.out.println(gameBoard);
    }
}
