
/*
*	Not exactly sure what values need to be inside Token. As of the moment each token created will have a specific token ID
*	specific to it, and another value that holds the value of which TokenRingAgent is currently holding the token.
*/

public class Token
{
	private static int counter = 0;		// Corresponds to the Token's unique ID. If multiple tokens are created, each will have a unique vale
	private int numID;
	private String tokenMessage;		// The message inside the Token
	
	public Token(String message)
	{
		this.numID = counter;
		counter++;
		this.tokenMessage = message;
	}
	
	// Returns the token's ID
	public int getTokenID()
	{
		return this.numID;
	}
	
	// Returns the message given to the Token
	public String getTokenMessage()
	{
		return this.tokenMessage;
	}
}