public class Assignment4{
	public static final int numProcessors = 10;
	private static int numReady = 0;

	public static synchronized void incrementNumReady(){
		numReady++;
	}

	public static synchronized boolean isReady(){
		return (numReady == 10)?  true : false;
	}

  public static void main(String[] args){
    //initialize everything
	int[] flag = new int[numProcessors];	// One flag for each processor
	int[] turn = new int[numProcessors-1];	// One turn for each level in the critical section

	TokenRing tokenRing = new TokenRing(numProcessors);		// A new TokenRing is intialized that holds arrays that have lengths matching the number of processors

	//This is the only broadcastSystem available
	BroadcastSystem broadcastSystem = BroadcastSystem.getInstance(numProcessors);
	Thread broadcastSysThread = new Thread(broadcastSystem);	// A new broadcastSystem Thread is initialized
//	broadcastSystem.setValue(990);
	broadcastSysThread.start();									// The broadcastSystem Thread is started

	Thread[] procThreadList = new Thread[numProcessors];		//creates an array of threads holding the threads of the processors
	//start all processors
	for(int id = 0; id < numProcessors; id++){
		procThreadList[id] = (new Processor(id, flag, turn, broadcastSysThread, broadcastSystem, tokenRing, numProcessors)).startThread();
	}

	while(!isReady()){		// the main program waits for all it's processors to indicate that they are ready for startup

	}
	tokenRing.activateRing("token");	// The tokenRing is initialized with a token holding the message "token"

	//start all the processor at the same time
	for(int id = 0; id < numProcessors; id++){
		procThreadList[id].interrupt();
	}


  }
}
