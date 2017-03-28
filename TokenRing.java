

public class TokenRing
{
	private TokenRingAgent[] ring;

	// Initializes an array of TokenRingAgent of a specified size
	public TokenRing(int elements)
	{
		ring = new TokenRingAgent[elements];
	}
	
	// Changes all elements inside the TokenRing to true.
	// TODO: needs to create a new token and pass it to an initially designated TokenRingAgent
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
	
	// changes all elements inside TokenRing to false.
	// TODO: Not sure if this is neccessary but again, having it just in case. If so, we need to search for any tokens and 
	// get rid of it.
	public void deactivateRing()
	{
		for(int i = 0; i < ring.length; i++)
		{
			ring[i].SetFalse();
			ring[i].SendToken(null);
		}
	}
	
	public void addToken(int index, TokenRingAgent tokenRingAgent)
	{
		ring[index] = tokenRingAgent;
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
		ring[ring[index].getSuccessor()].SendToken(ring[index].RecieveToken());
	}
}