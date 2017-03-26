import java.lang.Exception;
import java.lang.InterruptedException;

public class Processor implements Runnable{
  private Thread dsmThread;
  private Thread processorThread;
  
  private TokenRingAgent tokenRingAgent;

  private int numProcessors;
  private int[] Flag;
  private int[] Turn;
  private int processID;

  private ProcAndDSMComms procAndDSMComms;
  private String officialName;

  public Processor(int processID, int[] Flag, int[] Turn){
    this.processID = processID;
    this.Flag = Flag;
    this.Turn = Turn;
    
    
    processorThread = new Thread(this);
    processorThread.start();
  }
  
  //temporary
  public Processor(){
	  
	  processorThread = new Thread(this);
	  processorThread.start();
  }

  private void loadData(int index){
    //init the load
    procAndDSMComms.doALoad(index);
    //interrupt the DSM to load the data
    dsmThread.interrupt();
    //wait for the result
    try{
    	while(true){
    		Thread.sleep(100);
    	}
    }catch(InterruptedException e){
//      PrintToScreen.threadMessage(officialName, "from InterrruptedException");
      //read the result from ProcAndDSMComms
      int result = procAndDSMComms.getValue();
      PrintToScreen.threadMessage(officialName, result + " is the value in index " + index);
      return;
    }catch(Exception e){
    	//unexpected error
    	PrintToScreen.threadMessage(officialName, e.getStackTrace().toString());
    }

  }

  private void storeData(){
    //Initialize the data to be used by the DSM

    //try to get into critical section
    //lock();
  }

  /*
  private void lock(){
    int criticalSectionLevel = numPlayers - 1; // is this - 1 or - 2?
    for(int currentLevel = 0; j < criticalSectionLevel; currentLevel++ ){ //TODO: check the conditions, should we test at the level before Critical section ?
      Flag[processID] = currentLevel; // keept track of the processsor's level
      Turn[currentLevel] = processID; // keep track of the last processor to enter the currentLevel
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
*/
  private boolean thereIsProcessAtHigherLevel(int currentLevel){
    //loop through and see if any processID is at a higher level than currentLevel
    for(int i = 0; i < numProcessors; i++){
      if(Flag[i] >= currentLevel) return true;
    }
    return false;
  }

  private void enterCriticalSection(){
      //TODO:Are we communicating with an already running DSM or do we start the thread of DSM here?

      //message the DSM to store
  }

  /*
  private void unlock(){
    Flag[processorID] = -1;
  }
*/
  public void run(){
	officialName = Thread.currentThread().getName() + ", id: " + processID;
	PrintToScreen.threadMessage(officialName, "Starting DSM thread");
	procAndDSMComms = new ProcAndDSMComms();
	dsmThread = (new DSM(procAndDSMComms, Thread.currentThread(), processID)).startThread();
	
	
	for(int i = 0; i < 10; i++){
	  //load some data
	  loadData(i);
	
	  //store some data
	  // storeData();
	}
  }
}
