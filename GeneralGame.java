public abstract class GeneralGame {
    protected TicTacToeBoard board;
    protected Team[] teams;

    public GeneralGame(Team[] teams) {
        this.teams = teams;
    }

    public abstract void start();

    //increase the draw counts and set the players' status when there is a draw
    protected void handleDraw() {
        for (Team team : teams) {
            team.incrementDrawCount();
            //The last player in the player list is the current player. Set the winning status to a draw.
            team.getPlayers().get(team.getPlayers().size() - 1).setDraw(true);
        }
        System.out.println("The game is a draw!");
    }

    // display the match details of the players in this round of the game
    public void printSummary() {
        for (int i = 0; i < 2; i++) {
            System.out.println("----------------" + teams[i].getName() + "----------------");
            for (Player player : teams[i].getPlayers()) {
                if (player.isDraw()) {
                    System.out.println("Player '" + player.getPlayerNumber() + "' plays piece: '" + player.getSymbol().getName() + "' draw");
                } else {
                    System.out.println("Player '" + player.getPlayerNumber() + "' plays piece: '" + player.getSymbol().getName() + "' " + (player.hasWon() ? "win" : "lose"));
                }
            }
            System.out.println("Total win: " + teams[i].getWinCount());
            System.out.println("Total draw: " + teams[i].getDrawCount());
        }
    }

    // reset all the team statistics
    public void resetAllTeamsStatistics() {
        for (Team team : teams) {
            team.resetStatistics();
        }
    }
}
