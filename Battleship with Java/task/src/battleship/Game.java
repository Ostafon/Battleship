package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private final Board player1Board = new Board();
    private final Board player2Board = new Board();
    private final Scanner scanner = new Scanner(System.in);

    public void startGame() {
        System.out.println("Player 1, place your ships on the game field:");
        player1Board.printBoard();
        placeShips(player1Board);

        System.out.println("Press Enter and pass the move to Player 2");
        scanner.nextLine();

        System.out.println("Player 2, place your ships on the game field:");
        player2Board.printBoard();
        placeShips(player2Board);

        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        playGame();
    }

    private void placeShips(Board board) {
        board.placeShip("Aircraft Carrier", 5);
        board.placeShip("Battleship", 4);
        board.placeShip("Submarine", 3);
        board.placeShip("Cruiser", 3);
        board.placeShip("Destroyer", 2);
    }

    private void playGame() {
        boolean gameOver = false;
        boolean player1Turn = true;

        while (!gameOver) {
            if (player1Turn) {
                player2Board.printGameBoard();
                System.out.println("---------------------");
                player1Board.printBoard();
                System.out.println("Player 1, it's your turn:");
                player2Board.takeShot();

                if (isGameOver(player2Board)) {
                    System.out.println("Player 1 wins!");
                    gameOver = true;
                }
            } else {
                player1Board.printGameBoard();
                System.out.println("---------------------");
                player2Board.printBoard();
                System.out.println("Player 2, it's your turn:");
                player1Board.takeShot();


                if (isGameOver(player1Board)) {
                    System.out.println("Player 2 wins!");
                    gameOver = true;
                }
            }

            player1Turn = !player1Turn;
            if (!gameOver) {
                System.out.println("Press Enter and pass the move to the other player");
                scanner.nextLine();
            }
        }
    }

    private boolean isGameOver(Board board) {
        return Arrays.stream(board.getBoard())
                .flatMapToInt(row -> new String(row).chars())
                .noneMatch(c -> c == 'O');
    }

}
