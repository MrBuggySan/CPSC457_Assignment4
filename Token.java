
/*
*	Not exactly sure what values need to be inside Token. As of the moment each token created will have a specific token ID
*	specific to it, and another value that holds the value of which TokenRingAgent is currently holding the token.
*/

public class Token
{
	private static int counter = 0;
	private int numID;
	private String tokenMessage;
	
	public Token(String message)
	{
		this.numID = counter;
		counter++;
		this.tokenMessage = message;
	}
	
	public int getTokenID()
	{
		return this.numID;
	}
	
	public String getTokenMessage()
	{
		return this.tokenMessage;
	}
}