public class Labirinto {
    private static final int size = 12;
    private static char[][] maze;
    private static final double PROBABILITY = 0.7;
    private static final char start = 'S';
    private static final char end = 'E';
    private static final char FLOOR = '_';
    private static final char OUT = '|';
    private static final char WALL = '#';
    private static final char NOWAY = 'x';
    private static final char EMPTYWAY = ' ';
    private static final char WAY = ',';
    private static int lineStart;
    private static int columnStart;

    private static void createMaze(){

        for(int i = 0; i < size; i++){
            maze[0][i] = FLOOR;
            maze[size - 1][i] = FLOOR;
            maze[i][0] = OUT;
            maze[i][size - 1] = OUT;
        }


        for (int i = 1; i < size - 1; i++){
            for (int j = 1; j < size - 1; j++){
                if (Math.random() > PROBABILITY){
                    maze[i][j] = WALL;
                } else {
                    maze[i][j] = EMPTYWAY;
                }
            }
        }

        createLineColumn();
    }

    private static void createLineColumn(){
        lineStart = getNumber(1, size - 2);
        columnStart = getNumber(1, size - 2);

        int lineEnd = getNumber(1, size - 2);
        int columnEnd = getNumber(1, size - 2);

        if(!seeAvailability(lineStart, columnStart)
                || !seeAvailability(lineEnd, columnEnd)){
            createLineColumn();
        } else {
            maze[lineStart][columnStart] = start;
            maze[lineEnd][columnEnd] = end;
        }
    }

    private static int getNumber(int min, int max){

        int value = (int) Math.round(Math.random() * (max-min));

        return min + value;
    }

    private static boolean seeAvailability(int line, int column){
        if(
                maze[line - 1][column] == WALL ||
                        maze[line - 1][column] == FLOOR &&
                                maze[line + 1][column]  == WALL ||
                        maze[line + 1][column]  == FLOOR &&
                                maze[line][column - 1]  == WALL ||
                        maze[line][column - 1]  == OUT &&
                                maze[line][column + 1] == WALL ||
                        maze[line][column + 1] == OUT
        ) {
            return false;
        } else {
            return true;
        }
    }
    private static void drawMaze(){
        try {

            Thread.sleep(500);

            for (int i = 0; i < size; i++){
                for (int j = 0; j < size; j++ ){
                    System.out.print(maze[i][j]);
                }
                System.out.println();
            }
        } catch (Exception e){

            System.err.println("Nao foi possivel imprimir o labirinto.");
        }
    }

    private static boolean seekWay(int lineAtual, int columnAtual){
        int nextLine;
        int nextColumn;
        boolean found;


        nextLine = lineAtual - 1;
        nextColumn = columnAtual;
        found = tryWay(nextLine, nextColumn);


        if(!found){
            nextLine = lineAtual + 1;
            nextColumn = columnAtual;
            found = tryWay(nextLine, nextColumn);
        }


        if(!found){
            nextLine = lineAtual;
            nextColumn = columnAtual - 1;
            found = tryWay(nextLine, nextColumn);
        }

        if(!found){
            nextLine = lineAtual;
            nextColumn = columnAtual + 1;
            found = tryWay(nextLine, nextColumn);
        }

        return found;
    }

    private static boolean tryWay(int nextLine, int nextColumn) {
        boolean found = false;

        if (maze[nextLine][nextColumn] == end){
            found = true;
        } else if (emptypPosition(nextLine,nextColumn)) {

            maze[nextLine][nextColumn] = WAY;
            drawMaze();
            found = seekWay(nextLine,nextColumn);
            if(!found){

                maze[nextLine][nextColumn] = NOWAY;
                drawMaze();
            }
        }
        return found;
    }

    private static boolean emptypPosition(int line, int column) {
        boolean empty = false;

        if (line >= 0 && column >= 0 && line < size && column < size){
            empty = (maze[line][column] == EMPTYWAY);
        }
        return empty;
    }

    public static void main(String[] args) {

        maze = new char[size][size];
        createMaze();
        drawMaze();

        System.out.println("\n - ACHOU FIM? -\n");
        boolean found = seekWay(lineStart, columnStart);
        if(found){
            System.out.println("ACHOU");
        } else {
            System.out.println("NÃO ACHOU");
        }
    }
}
