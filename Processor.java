import java.lang.Exception;
import java.lang.InterruptedException;

public class Processor implements Runnable{
  private Thread dsmThread;
  private DSM dsm;

  private Thread processorThread;

  private Thread tokenRingAgentThread;
  private TokenRingAgent tokenRingAgent;

  private int numProcessors;
  private int[] Flag;
  private int[] Turn;
  private int processorID;


  private String officialName;

  public Processor(int processorID, int[] Flag, int[] Turn, Thread broadcastSystemThread, BroadcastSystem broadcastSystem, TokenRing tokenRing, int numProcessors){
    this.processorID = processorID;
    this.Flag = Flag;
    this.Turn = Turn;

    processorThread = new Thread(this);										// A new thread is created for this processor
    tokenRingAgent = new TokenRingAgent(processorID, numProcessors);		// A new TokenRingAgent is initialized using the same processor ID
    tokenRingAgent.setRing(tokenRing);										// gives the new TokenRingAgent a reference to the TokenRing

    tokenRingAgentThread = tokenRingAgent.startThread();					// Starts the TokenRingAgent Thread and gives the processor a reference to it
    tokenRing.addTokenAgent(processorID, tokenRingAgent, tokenRingAgentThread);	// the TokenRingAgent and its thread is added into the TokenRing
    dsm = new DSM(processorThread, processorID, broadcastSystemThread, broadcastSystem, tokenRingAgentThread, tokenRingAgent);	// A new DSM is initialized
  	dsmThread = dsm.startThread();											// the DSM thread is started
  	officialName = TextColor.ANSI_RED + "processor id: " + processorID + TextColor.ANSI_RESET;


  }

  // When called, runs the processor's thread and returns the thread
  public Thread startThread(){
    processorThread.start();
    return processorThread;
  }
  // Starts up a load
  private void loadData(int index){
    //init the load
    dsm.doALoad(index);
    //interrupt the DSM to load the data
    dsmThread.interrupt();
    //wait for the result
    try{
    	while(true){
    		Thread.sleep(100);
    	}
    }catch(InterruptedException e){
      //read the result from ProcAndDSMComms
      int result = dsm.getValue();
      // PrintToScreen.threadMessage(officialName, result + " is the value in index " + index);
      return;
    }catch(Exception e){
    	//unexpected error
    	PrintToScreen.threadMessage(officialName, e.getStackTrace().toString());
    }

  }

  private void storeData(int index, int value){
    //Initialize the data to be used by the DSM
    dsm.doAStore(index, value);

    //try to get into critical section
    lock();
  }


  private void lock(){
    int numPlayers = Assignment4.numProcessors;
    int criticalSectionLevel = numPlayers - 1; // indicates the level of the critical section
    for(int currentLevel = 0; currentLevel < criticalSectionLevel; currentLevel++ ){ //TODO: check the conditions, should we test at the level before Critical section ?
      Flag[processorID] = currentLevel; // keept track of the processsor's level
      Turn[currentLevel] = processorID; // keep track of the last processor to enter the currentLevel
      //While there is another process at a higher level and the board at the current level has the id
      //of the ith player on the level board, stay in the current level.
      while( thereIsProcessAtHigherLevel(currentLevel) && Turn[currentLevel] == processorID){
        //do nothing, is there a call I can do to do a nop;?
      }
    }
    Flag[processorID] = criticalSectionLevel;
    //enter the critical section
    //tokenRingAgent.setPass();		// The Pass in the tokenRingAgent that corresponds to this processor is set to false
    enterCriticalSection();
  }

  private boolean thereIsProcessAtHigherLevel(int currentLevel){
    //loop through and see if any processorID is at a higher level than currentLevel
    for(int pid = 0; pid < numProcessors; pid++){
      if(Flag[pid] >= currentLevel) return true;
    }
    return false;
  }

  private void enterCriticalSection(){
    PrintToScreen.threadMessage(officialName, TextColor.ANSI_BLUE + "Now in critical section, to store " + TextColor.ANSI_RESET);
    //interrupt the DSM to store the data
      dsmThread.interrupt();
      try{
      while(true){
    	  Thread.sleep(500);
    	  	}
      }catch(InterruptedException e){
    	  PrintToScreen.threadMessage(officialName, " exiting critical section");
      }
      tokenRingAgentThread.interrupt();		// Sends an interrupt to the TokenRingAgent Thread indicating that the Thread is exiting the critical section
      unlock();								// exits the critical section
  }


  private void unlock(){
    Flag[processorID] = -1;
  }

  public void run(){
  	PrintToScreen.threadMessage(officialName, "ready to start instructions");

    try{
      Thread.sleep(10);
      Assignment4.incrementNumReady();			// indicates to the main that this particular processor is ready to start
      while(true){
        Thread.sleep(100);						// waits until an interrupt from the main is received indicating that all processors are now ready
      }
    }catch(InterruptedException e){
      try{
          Thread.sleep(50); // extra waiting time before we actually start
      }catch(InterruptedException t){
      }
    }


	PrintToScreen.threadMessage(officialName, "Starting now...");

while(true){
  for(int i = 0; i < 10; i++){
	  //load some data
	  loadData(i);
	}
		for(int i = 0; i < 10; i++){
      int storeVal = processorID * 10;
      PrintToScreen.threadMessage(officialName, TextColor.ANSI_BLUE + "Attempting to store " + storeVal + TextColor.ANSI_RESET);
			  //store some data
			  storeData(i, storeVal);
		}
  try{
      Thread.sleep(10000);
  }catch(InterruptedException e){

  }

  PrintToScreen.threadMessage(officialName, "done doing my job, I will start over");
}
}


}
