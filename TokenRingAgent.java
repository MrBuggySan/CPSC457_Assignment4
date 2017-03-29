import java.lang.InterruptedException;

public class TokenRingAgent implements Runnable{
		//instance variables
		private Token tokenRef;
		private boolean Active;
		private int procID;
		private int tokenRingPredecessor;
		private int tokenRingSuccessor;
		private boolean Pass;

		private TokenRing Ring;
		private Thread tokenRingAgentThread;

		// takes in the proccesser ID and the total number of processes.
		// Intially token ID is null, Active is false, and predecessor and successor is the procID -1 +1 respectively.
		// unless procID is either 0 or the last processor, which it loops around for
		public TokenRingAgent(int procID, int procSize) {
			this.tokenRef = null;								// Initially null (AKA not holding the token)
			this.Active = false;						// Initially false. RingAgents need to be activated on startup
			this.procID = procID;						// The corresponding processor ID is set to this value
			if(this.procID <= 0)
				this.tokenRingPredecessor = (procSize-1);	// If the processor is 0, it's predecessor is set as the last processor
			else
				this.tokenRingPredecessor = (procID-1);		// Else its predecessor is the previous ID
			if(procID == procSize-1)
				this.tokenRingSuccessor = 0;					// If the processor is the last processor, it's sucessor is set as the first processor
			else
				this.tokenRingSuccessor = (procID+1);		// Else its predecessor is the next ID
			this.mustPassToken();							// Pass is initially set as true. Dictates whether the processor will give away its token

			tokenRingAgentThread = new Thread(this);	// A new tokenRingAgent thread is created
		}

		// sets the TokenRing of the corresponding TokenRingAgent as the value being passed in
		public void setRing(TokenRing Ring){
			this.Ring = Ring;
		}

		// Starts the TokenRingAgent Thread
		public Thread startThread(){
			tokenRingAgentThread.start();
			return tokenRingAgentThread;
		}

		// returns the token it's holding and sets it's tokenRef to null
		public Token GiveToken(){
			Token tPredecessor = this.tokenRef;
			this.tokenRef = null;
			return tPredecessor;
		}

		// sets the token value t as the token for the successor.
		public void RecieveToken(Token t){
			this.tokenRef = t;
		}

		// sets Active to false.
		public void SetFalse(){
			this.Active = false;
		}

		// sets Active to true.
		public void SetTrue(){
			this.Active = true;
		}

		// returns the Token value
		public Token getTokenRef(){
			return this.tokenRef;
		}

		// returns the corresponding agent's successor
		public int getSuccessor(){
			return this.tokenRingSuccessor;
		}

		// returns the corresponding agent's predecessor
		public int getPredecessor(){
			return this.tokenRingPredecessor;
		}

		// sets Pass as false (when checked, will hold onto the token)
		public void mustNotPassToken(){
			this.Pass = false;
		}

		public void mustPassToken(){
			this.Pass = true;
		}

		// returns the Pas value
		public boolean mustPass(){
			return this.Pass;
		}

		// Checks if the TokenRingAgent is holding onto the token
		public boolean holdingToken(){
			if(this.getTokenRef() == null){
				return false;
			}
			return true;
		}

		// The execution of each TokenRingAgent Thread
		public void run(){
			String officialName = "TokenRingAgent " + procID;
			PrintToScreen.threadMessage(officialName, "TokenAgent starting now");					// Indicates the start of the TokenRingAgent thread
			while(true){// Continues to loop for as long as the program is running
				if(this.holdingToken()){// If the TokenRingAgent is holding the token, one of two things will happen
					if(!this.mustPass()){// If the value of Pass was changed (in processor; Indicates that the thread wishes to enter the critical) section
						PrintToScreen.threadMessage(officialName,"Holding onto Token");
						try{
							while(true){
								Thread.sleep(100);	// Until an interrupt from processor arrives indicating that the processor is exiting the critical section, the thread waits
							}
						}catch(InterruptedException e){
							PrintToScreen.threadMessage(officialName, "passing token");
							Ring.transferToken(procID);		// Prompts the TokenRing to transfer tokens from the current Agent to its successor
						}
					}
					else{// Pass was therefore true
						PrintToScreen.threadMessage(officialName, "Sending to next element");
						Ring.transferToken(procID);		// The token is passed to the agent's successor
					}
				}else{// If the processor did not have the token
					PrintToScreen.threadMessage(officialName, "Going to sleep");
					try{
						while(true){
							Thread.sleep(100);		// The Thread goes to sleep until an interrupt is made from TokenRing telling the Thread that a token has been passed to it
						}
					}catch(InterruptedException e){
						PrintToScreen.threadMessage(officialName, "Waking Up");
					}

				}
			}

		}
}
