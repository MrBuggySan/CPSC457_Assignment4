public class Processor implements Runnable{
  private Thread dsmThread;
  private TokenRingAgent tokenRingAgent;

  private int numProcessors;
  private int[] Flag;
  private int[] Turn;
  private int processID;

  public Processor(int processID, int[] Flag, int[] Turn){
    this.processID = processID;
    this.Flag = Flag;
    this.Turn = Turn;
  }

  private void loadData(){
    //message the DSM to load the data

    //TODO:Are we communicating with an already running DSM or do we start the thread of DSM here?
  }

  private void storeData(){
    //Initialize the data to be used by the DSM

    //try to get into critical section
    lock();
  }

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

  private void unlock(){
    Flag[processorID] = -1;
  }

  public void run(){

    PrintToScreen.threadMessage(Thread.currentThread().getName() + " with id " + processID, "Starting DSM thread");
    Thread dsmThread = new Thread(new DSM());

    while(1){
      //load some data
      loadData();

      //store some data
      storeData();
    }
  }
}
