

public class TokenRing
{
	private TokenRingAgent[] ring;
	private Thread[] threadRing;
	private int successor;
	private Token token;
	

	// Initializes an array of TokenRingAgent and its threads of a specified size
	public TokenRing(int elements)
	{
		ring = new TokenRingAgent[elements];
		threadRing = new Thread[elements];
	}
	
	// Changes all elements inside the TokenRing to true.
	// Then a new Token is created and handed to the first element in the TokenRing
	public void activateRing(String tokenMessage)
	{
		for(int i = 0; i < ring.length; i++)
		{
			ring[i].SetTrue();
		}
		Token token = new Token(tokenMessage);
		ring[0].SendToken(token);
		System.out.println(token.getTokenMessage() + " : Token Created");
	}
	
	// changes all elements inside TokenRing to false
	// not used in the program's implementation
	public void deactivateRing()
	{
		for(int i = 0; i < ring.length; i++)
		{
			ring[i].SetFalse();
			ring[i].SendToken(null);
		}
	}
	
	// adds a tokenRingAgent and its thread to the TokenRing
	public void addToken(int index, TokenRingAgent tokenRingAgent, Thread tokenRingAgentThread)
	{
		ring[index] = tokenRingAgent;
		threadRing[index] = tokenRingAgentThread;
	}
	
	// returns true if a certain process has the token, otherwise returns false.
	public boolean checkToken(int index)
	{
		if (ring[index].getID() == null)
			return false;
		else
			return true;
	}
	
	// sets the ID of the successor of the specified index as the ID of the specified index, and sets the ID of the specified index as null.
	public void transferToken(int index)
	{
		successor = ring[index].getSuccessor();
		token = ring[index].RecieveToken();
		ring[successor].SendToken(token);
		threadRing[successor].interrupt();			// Sends an interrupt over to the index's successor
	}
}