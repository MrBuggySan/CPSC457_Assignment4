public class TokenRing{
	private TokenRingAgent[] tokRingList;
	private Thread[] threadRing;
	private int successor;
	private Token token;


	// Initializes an array of TokenRingAgent and its threads of a specified size
	public TokenRing(int elements){
		tokRingList = new TokenRingAgent[elements];
		threadRing = new Thread[elements];
	}

	// Changes all elements inside the TokenRing to true.
	// Then a new Token is created and handed to the first element in the TokenRing
	public void activateRing(String tokenMessage){
		for(int i = 0; i < tokRingList.length; i++){
			tokRingList[i].SetTrue();
		}
		Token token = new Token(tokenMessage);
		tokRingList[0].RecieveToken(token);
		tokRingList[0].mustNotPassToken();
		System.out.println(token.getTokenMessage() + " : Token Created");
	}

	// changes all elements inside TokenRing to false
	// not used in the program's implementation
	public void deactivateRing(){
		for(int i = 0; i < tokRingList.length; i++){
			tokRingList[i].SetFalse();
			tokRingList[i].RecieveToken(null);
		}
	}

	// adds a tokenRingAgent and its thread to the TokenRing
	public void addTokenAgent(int index, TokenRingAgent tokenRingAgent, Thread tokenRingAgentThread){
		tokRingList[index] = tokenRingAgent;
		threadRing[index] = tokenRingAgentThread;
	}

	// returns true if a certain process has the token, otherwise returns false.
	public boolean checkToken(int index){
		if (tokRingList[index].getTokenRef() == null)
			return false;
		else
			return true;
	}

	// sets the ID of the successor of the specified index as the ID of the specified index, and sets the ID of the specified index as null.
	public void transferToken(int predecessor){
		successor = tokRingList[predecessor].getSuccessor();
		token = tokRingList[predecessor].GiveToken();
		tokRingList[successor].RecieveToken(token);


		tokRingList[predecessor].mustPassToken();
		tokRingList[successor].mustNotPassToken();

		threadRing[successor].interrupt();			// Sends an interrupt to the succesor
	}
}
