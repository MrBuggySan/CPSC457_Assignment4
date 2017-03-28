import java.lang.Exception;
import java.lang.InterruptedException;

public class Processor implements Runnable{
  private Thread dsmThread;
  private DSM dsm;

  private Thread processorThread;

  private TokenRingAgent tokenRingAgent;

  private int numProcessors;
  private int[] Flag;
  private int[] Turn;
  private int processorID;


  private String officialName;

  public Processor(int processorID, int[] Flag, int[] Turn, Thread broadcastSystemThread, BroadcastSystem broadcastSystem){
    this.processorID = processorID;
    this.Flag = Flag;
    this.Turn = Turn;

    processorThread = new Thread(this);

    dsm = new DSM(processorThread, processorID, broadcastSystemThread, broadcastSystem);
  	dsmThread = dsm.startThread();
  	officialName = TextColor.ANSI_RED + "processor id: " + processorID + TextColor.ANSI_RESET;


  }

  public Thread startThread(){
    processorThread.start();
    return processorThread;
  }
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
    int criticalSectionLevel = numPlayers - 1; // is this - 1 or - 2?
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
        //Adjust the value here so that other objects under DSM and other DSMs can catch up
        //the BroadcastSystem has its own random delay
        Thread.sleep(500);
        //problem: this sleep is still quicker than the cascading interrupts to broadcast the store. Therefore some of the interrupts are made while the
        //BroadcastSystem is still broadcasting to other BroadcastAgents
      }catch(InterruptedException e){
          PrintToScreen.threadMessage(officialName, " interrupted while in critical section");
      }
      unlock();
  }


  private void unlock(){
    Flag[processorID] = -1;
  }

  public void run(){
  	PrintToScreen.threadMessage(officialName, "ready to start instructions");
    try{
      Assignment4.numReady++;
      while(true){
        Thread.sleep(100);
      }
    }catch(InterruptedException e){
      try{
          Thread.sleep(100); // extra waiting time before we actually start
      }catch(InterruptedException t){
      }
    }


	PrintToScreen.threadMessage(officialName, "Starting now...");

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



  }


}
