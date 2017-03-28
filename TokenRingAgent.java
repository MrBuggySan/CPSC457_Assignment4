

public class TokenRingAgent 
{
		//instance variables
		private Token ID;
		private boolean Active;
		private int procID;
		private int RingPredecessor;
		private int RingSuccessor;
		
		// takes in the proccesser ID and the total number of processes.
		// Intially token ID is null, Active is false, and predecessor and successor is the procID -1 +1 respectively.
		// unless procID is either 0 or the last processor, which it loops around for
		public TokenRingAgent(int procID, int procNumber) {
			this.ID = null;
			this.Active = false;
			this.procID = procID;
			if(procID == 0)
				this.RingPredecessor = (procNumber-1);
			else
				this.RingPredecessor = (procID-1);
			if(procID == procNumber-1)
				this.RingSuccessor = 0;
			else
				this.RingSuccessor = (procID+1);
		}
		
		// TODO: says to return the unique identifier for the token received. not sure if this is all we need for this
		// but if it doesnt havea a token ID it'll just return null atm.
		public Token RecieveToken()
		{
			return this.ID;
		}
		
		// TODO: not entirely sure what's supposed to happen here. Says we need to send the token to the successor but technically
		// the process should already have the token inside it if this is called so not sure why it takes in a token. Also not sure how to send
		// the token over since the successor is another element inside the array. either way this and the above need a bit of work
		public void SendToken(Token t)
		{
			
		}
		
		// sets Active to false. Not sure if needed but we doesnt hurt to have
		public void SetFalse()
		{
			this.Active = false;
		}
		
		// sets Active to true.
		public void SetTrue()
		{
			this.Active = true;
		}
}