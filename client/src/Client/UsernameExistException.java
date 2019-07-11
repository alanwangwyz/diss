package Client;
//to throw the username existence exception
public class UsernameExistException extends Exception {
	public UsernameExistException() {
		super("The user name already exist, please re-enter!");
	}
}
