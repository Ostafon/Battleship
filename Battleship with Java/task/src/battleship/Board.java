package battleship;

import java.util.*;

public class Board {
    private static final Scanner scanner = new Scanner(System.in);
    private final int rows = 10;
    private final int cols = 10;
    private final char[][] board = new char[rows][cols];
    private final char[][] gameboard = new char[rows][cols];
    private Map<List<String>, Integer> ships;

    public Board() {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(board[i], '~');
            Arrays.fill(gameboard[i], '~');
        }
        ships = new HashMap<>();
    }

    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int j = 1; j <= cols; j++) {
            sb.append(j).append(" ");
        }
        sb.append("\n");

        for (int i = 0; i < rows; i++) {
            sb.append((char) ('A' + i)).append(" ");
            for (int j = 0; j < cols; j++) {
                sb.append(board[i][j]).append(" ");
            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }

    public void printGameBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int j = 1; j <= cols; j++) {
            sb.append(j).append(" ");
        }
        sb.append("\n");

        for (int i = 0; i < rows; i++) {
            sb.append((char) ('A' + i)).append(" ");
            for (int j = 0; j < cols; j++) {
                sb.append(gameboard[i][j]).append(" ");
            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }

    private List<String> generateCoordinates(String start, String end) {
        List<String> coordinates = new ArrayList<>();
        char startCol = start.charAt(0);
        int startRow = Integer.parseInt(start.substring(1));
        char endCol = end.charAt(0);
        int endRow = Integer.parseInt(end.substring(1));

        if (startCol > endCol || startRow > endRow) {
            char tempCol = startCol;
            int tempRow = startRow;
            startCol = endCol;
            startRow = endRow;
            endCol = tempCol;
            endRow = tempRow;
        }

        if (startCol == endCol) {
            for (int row = startRow; row <= endRow; row++) {
                coordinates.add("" + startCol + row);
            }
        } else if (startRow == endRow) {
            for (char col = startCol; col <= endCol; col++) {
                coordinates.add("" + col + startRow);
            }
        }

        return coordinates;
    }

    private boolean checkCoordinate(String coordinate) {
        if (coordinate.length() < 2 || coordinate.length() > 3) return false;
        char col = coordinate.charAt(0);
        int row;
        try {
            row = Integer.parseInt(coordinate.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }
        return col >= 'A' && col <= 'J' && row >= 1 && row <= 10;
    }

    private boolean isValidShipPlacement(String[] coordinates) {
        if (coordinates.length != 2 || !checkCoordinate(coordinates[0]) || !checkCoordinate(coordinates[1])) {
            return false;
        }

        char startCol = coordinates[0].charAt(0);
        int startRow = Integer.parseInt(coordinates[0].substring(1));
        char endCol = coordinates[1].charAt(0);
        int endRow = Integer.parseInt(coordinates[1].substring(1));

        return startCol == endCol || startRow == endRow;
    }

    private boolean isSurroundingCellsValid(String[] coordinates) {
        List<String> parts = generateCoordinates(coordinates[0], coordinates[1]);
        for (String part : parts) {
            char row = part.charAt(0);
            int col = Integer.parseInt(part.substring(1));

            int startRow = row - 'A' - 1;
            int endRow = row - 'A' + 1;
            int startCol = col - 1;
            int endCol = col + 1;

            for (int i = startRow; i <= endRow; i++) {
                for (int j = startCol; j <= endCol; j++) {
                    if (i >= 0 && i < 10 && j >= 0 && j < 10) {
                        if (board[i][j] != '~') {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void placeShip(String shipName, int size) {
        while (true) {
            System.out.println("Enter the coordinates of the " + shipName + " (" + size + " cells):");
            String input = scanner.nextLine();
            String[] coordinates = input.split(" ");

            if (coordinates.length != 2) {
                System.out.println("Error! Invalid input. Try again:");
                continue;
            }

            char startRowChar = coordinates[0].charAt(0);
            int startCol = Integer.parseInt(coordinates[0].substring(1)) - 1;
            char endRowChar = coordinates[1].charAt(0);
            int endCol = Integer.parseInt(coordinates[1].substring(1)) - 1;

            int startRow = startRowChar - 'A';
            int endRow = endRowChar - 'A';

            boolean isHorizontal = startRowChar == endRowChar;
            int length = isHorizontal ? Math.abs(endCol - startCol) + 1 : Math.abs(endRow - startRow) + 1;

            if (length != size) {
                System.out.println("Error! Wrong length of the " + shipName + "! Try again:");
                continue;
            }

            if (!areCellsAvailable(coordinates)) {
                System.out.println("Error! Wrong ship location! Try again:");
                continue;
            }

            if (!isValidShipPlacement(coordinates)) {
                System.out.println("Error! Wrong ship location! Try again:");
                continue;
            }

            if (!isSurroundingCellsValid(coordinates)) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                continue;
            }

            List<String> coord = Arrays.asList(coordinates);
            ships.put(coord, size);
            generateShips(coordinates, size);
            break;
        }
    }

    private boolean areCellsAvailable(String[] coordinates) {
        List<String> parts = generateCoordinates(coordinates[0], coordinates[1]);
        for (String part : parts) {
            char row = part.charAt(0);
            int col = Integer.parseInt(part.substring(1));

            int rowIndex = row - 'A';
            int colIndex = col - 1;

            if (rowIndex < 0 || rowIndex >= 10 || colIndex < 0 || colIndex >= 10) {
                return false;
            }

            if (board[rowIndex][colIndex] != '~') {
                return false;
            }
        }
        return true;
    }

    private void generateShips(String[] coordinates, int length) {
        List<String> parts = generateCoordinates(coordinates[0], coordinates[1]);
        for (String part : parts) {
            char row = part.charAt(0);
            int col = Integer.parseInt(part.substring(1));

            int rowIndex = row - 'A';
            int colIndex = col - 1;

            board[rowIndex][colIndex] = 'O';
        }
        printBoard();
    }

    private void areShipsAvailable(String targetString, Map<List<String>, Integer> mapData) {
        for (Map.Entry<List<String>, Integer> entry : mapData.entrySet()) {
            List<String> key = entry.getKey();
            Integer value = entry.getValue();

            String start = key.get(0);
            String end = key.get(1);

            if (value == 0) {
                continue;
            }

            if (isInRange(targetString, start, end)) {
                mapData.put(key, value - 1);
                if (mapData.get(key) == 0) {
                    System.out.println("You sank a ship! Specify a new target:");
                } else {
                    System.out.println("You hit a ship! Try again:");
                }
                break;
            }
        }
    }

    private boolean isInRange(String target, String start, String end) {
        char targetCol = target.charAt(0);
        int targetRow = Integer.parseInt(target.substring(1));
        char startCol = start.charAt(0);
        int startRow = Integer.parseInt(start.substring(1));
        char endCol = end.charAt(0);
        int endRow = Integer.parseInt(end.substring(1));

        if (startCol == endCol) {
            return targetCol == startCol && targetRow >= startRow && targetRow <= endRow;
        } else {
            return targetRow == startRow && targetCol >= startCol && targetCol <= endCol;
        }
    }

    public void takeShot() {
        while (Arrays.stream(board).flatMapToInt(row -> new String(row).chars()).anyMatch(c -> c == 'O')) {
            String coordinate = scanner.nextLine();
            if (!checkCoordinate(coordinate)) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }

            char colChar = coordinate.charAt(0);
            int row = Integer.parseInt(coordinate.substring(1)) - 1;
            int col = colChar - 'A';

            if (board[col][row] == 'O') {
                board[col][row] = 'X';
                gameboard[col][row] = 'X';
                areShipsAvailable(coordinate, ships);
                break;
            } else if (board[col][row] == '~') {
                board[col][row] = 'M';
                gameboard[col][row] = 'M';
                System.out.println("You missed! Try again:");
                break;
            } else {
                System.out.println("You already shot there!");
                continue;
            }
        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public char[][] getBoard() {
        return board;
    }
}
