import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        callMenu();
    }
    public static void callMenu() {
        Scanner scanner = new Scanner(System.in);

        //Supposed there are 2 teams competing with each others.
        Team team1 = new Team("Team 1", new ArrayList<Player>());
        Team team2 = new Team("Team 2", new ArrayList<Player>());

        Team[] teams = {team1, team2};
        while (true) {
            // show menu
            System.out.println("Let's play a game! Please choose a game type(input an integer):");
            System.out.println("1) Tic-Tac-Toe");
            System.out.println("2) Order and Chaos");
            System.out.println("3) Super Tic-Tac-Toe");
            System.out.println("4) QuoridorGame");
            System.out.println("5) Exit ");

            int choice = scanner.nextInt();

            if (choice == 1) {
                // Tic-Tac-Toe
                GeneralGame game = new TicTacToeGame(teams);
                game.start();
            } else if (choice == 2) {
                // Order and Chaos
                GeneralGame game = new OrderAndChaosGame(teams);
                game.start();
            } else if (choice == 3) {
                //super Tic-Tac-Toe
                GeneralGame game = new SuperTicTacToeGame(teams);
                game.start();
            } else if (choice == 5) {
                System.out.println("Good Game!");
                break;
            } else if (choice == 4) {
                GeneralGame game = new QuoridorGame(teams);
                game.start();
            }
            else{
                System.out.println("Wrong choice. Try again.");
            }
        }
    }
}
