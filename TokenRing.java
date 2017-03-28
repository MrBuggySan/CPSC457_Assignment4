

public class TokenRing
{
	private TokenRingAgent[] ring;

	// Initializes an array of TokenRingAgent of a specified size
	public TokenRing(int elements)
	{
		ring = new TokenRingAgent[elements];
		for(int i = 0; i < elements; i++)
		{
			ring[i] = new TokenRingAgent(i, elements);
		}
	}
	
	// Changes all elements inside the TokenRing to true.
	// TODO: needs to create a new token and pass it to an initially designated TokenRingAgent
	public void activateRing()
	{
		for(int i = 0; i < ring.length; i++)
		{
			ring[i].setTrue();
		}
	}
	
	// changes all elements inside TokenRing to false.
	// TODO: Not sure if this is neccessary but again, having it just in case. If so, we need to search for any tokens and 
	// get rid of it.
	public void deactivateRing()
	{
		for(int i = 0; i < ring.length; i++)
		{
			ring[i].setFalse();
		}
	}
}