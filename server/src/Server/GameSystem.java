//Bug-killer
//Lakshmi
//Thursday 10am
package Server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//class to store game information
public class GameSystem {

	static List<ClientConnection> players;
	ArrayList<String> result;
	private static int pass;
	private static int numPlayers;
	private static StandardBoard board;
	static String whoPlay;
	private static int playRound;
	private static int numVote;
	static String voteResult;
	static boolean voteComplete;
	static boolean passComplete;
	private static int wordScore;
	private static HashMap<String, Integer> score;
	private static int playerReady = 0;
	static boolean inGame = false;
	

	public GameSystem() {
	}

	public static void initialize() {
		players = ServerState.getInstance().getConnectedPlayers();// need to change to playerlist
		score = new HashMap<String, Integer>();
		for (ClientConnection player : players) {
			player.setInGame(true);
			score.put(player.getUsername(), 0);
		}
		numPlayers = players.size();
		playRound = 0;
		pass = 0;
		voteComplete = false;
		passComplete = false;
		whoPlay = players.get(playRound % numPlayers).getUsername();

		board = new StandardBoard();
		playerReady++;
	}
	public static int getPlayerReady() {
		return playerReady;
	}
	public static void setPlayerReady(int x) {
		playerReady = x;
	}


	public static void putCharacter(String msg) {
		String[] input = msg.split(",");
		char c = input[0].charAt(0);
		int x = Integer.parseInt(input[1]);
		int y = Integer.parseInt(input[2]);
		board.placeCharacter(c, x, y);
		pass = 0;
		board.printBoard();
	}

	public static void pass() {
		playRound++;
		whoPlay = players.get(playRound % numPlayers).getUsername();
	}

	public static void passWithoutPut() {
		pass();
		pass++;
		System.out.println("pass: " + pass);
		System.out.println("numPlayers: " + numPlayers);
		if (pass == numPlayers) {
			passComplete = true;
		}
	}

	public static void vote(String index) {
		String[] wordIndex = index.split(",");
		int x1 = Integer.parseInt(wordIndex[0]);
		int y1 = Integer.parseInt(wordIndex[1]);
		int x2 = Integer.parseInt(wordIndex[2]);
		int y2 = Integer.parseInt(wordIndex[3]);
		wordScore = Math.abs(x2 - x1 + y2 - y1) + 1;
		voteComplete = false;
		numVote = 0;
		voteResult = "true";
	}

	public static void voteResult(String msg) {
		numVote++;
		if (msg.equals("disapprove")) {
			voteResult = "false";
		} else {
		}
		if (numVote == numPlayers) {
			voteComplete = true;
			if (voteResult.equals("true")) {
				score.put(whoPlay, score.get(whoPlay) + wordScore);
			}
		}
	}

	public static ArrayList<String> getFinalScore() {
		ArrayList<String> result = new ArrayList<String>();
		int i = 0;
		System.out.println("playersize2:"+players.size());
		while (i < numPlayers) {
			int max = 0;
			for (int j = 0; j < numPlayers; j++) {
				int tem = score.get(players.get(j).getUsername());
				if (tem > max) {
					max = tem;
				}
			}
			for (int j = 0; j < numPlayers; j++) {
				int temp = score.get(players.get(j).getUsername());
				if (temp == max) {
					result.add(players.get(j).getUsername() + "," + temp);
					score.put(players.get(j).getUsername(), -1);
					i++;
				}
			}
		}

		return result;
	}

	public static String voteComplete() {
		return whoPlay + "," + score.get(whoPlay);
	}
	
	public static HashMap<String, Integer> getMap(){
		return score;
	}
}
