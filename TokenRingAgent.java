import java.lang.InterruptedException;

public class TokenRingAgent implements Runnable
{
		//instance variables
		private Token ID;
		private boolean Active;
		private int procID;
		private int RingPredecessor;
		private int RingSuccessor;
		private boolean Pass;
		
		private TokenRing Ring;
		private Thread tokenRingAgentThread;
		
		// takes in the proccesser ID and the total number of processes.
		// Intially token ID is null, Active is false, and predecessor and successor is the procID -1 +1 respectively.
		// unless procID is either 0 or the last processor, which it loops around for
		public TokenRingAgent(int procID, int procSize) {
			this.ID = null;
			this.Active = false;
			this.procID = procID;
			if(procID == 0)
				this.RingPredecessor = (procSize-1);
			else
				this.RingPredecessor = (procID-1);
			if(procID >= procSize-1)
				this.RingSuccessor = 0;
			else
				this.RingSuccessor = (procID+1);
			this.Pass = true;
			
			tokenRingAgentThread = new Thread(this);
		}
		
		public void getRing(TokenRing Ring)
		{
			this.Ring = Ring;
		}
		
		public Thread startThread(){
			tokenRingAgentThread.start();
			return tokenRingAgentThread;
		}
		
		// receives the token of the predecessor and sets it's ID to null
		public Token RecieveToken()
		{
			Token tPredecessor = this.ID;
			this.ID = null;
			return tPredecessor;
		}
		
		// sets the token value t as the token for the successor. call ReceiveToken for the predecessor of this element to get the Token t.
		public void SendToken(Token t)
		{
			this.ID = t;
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
		
		public Token getID()
		{
			return this.ID;
		}
		
		public int getSuccessor()
		{
			return this.RingSuccessor;
		}
		
		public int getPredecessor()
		{
			return this.RingPredecessor;
		}
		
		public void setPass()
		{
			this.Pass = false;
		}
		public boolean getPass()
		{
			return this.Pass;
		}
		
		public boolean holdingToken()
		{
			if((this.getID() == null)) return false;
			else return true;
		}

		public void run()
		{
			PrintToScreen.threadMessage("TokenRingAgent " + procID, " TokenAgent starting now");
			while(Active)
			{
				if(this.holdingToken())
				{
					System.out.println("This program is a bitch");
					if(!this.getPass())
					{
						System.out.println("Holding onto Token");
						try{
							while(true)
							{
								Thread.sleep(100);
							}
						} catch(InterruptedException e)
						{
							System.out.println("passing token");
							Ring.transferToken(procID);
							Pass = true;
						}
					}
					else
					{
						Ring.transferToken(procID);
					}
				}
			}

		}
}