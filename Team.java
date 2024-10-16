import java.util.List;

public class Team {
    private String name;
    private List<Player> players;    // All players in the team.
    private int winCount;        // The number of games won by the team.
    private int drawCount;       // The number of draws for the team.

    public Team(String name, List<Player> players) {
        this.name = name;
        this.players = players;
        this.winCount = 0;
        this.drawCount = 0;
    }

    //get the team name
    public String getName() {
        return name;
    }

    //set the team name
    public void setName(String name) {
        this.name = name;
    }

    //assign players to the team
    public void addTeamPlayer(Player player) {
        this.players.add(player);
    }

    //get player list from the team
    public List<Player> getPlayers() {
        return players;
    }

    //increase the total win of the team
    public void incrementWinCount() {
        winCount++;
    }

    //increase the total draw of the team
    public void incrementDrawCount() {
        drawCount++;
    }

    //get the number of win for summary table
    public int getWinCount() {
        return winCount;
    }

    //get the number of draw for summary table
    public int getDrawCount() {
        return drawCount;
    }

    //reset the team statistics
    public void resetStatistics() {
        winCount = 0;
        drawCount = 0;
        players.clear();
    }
}

