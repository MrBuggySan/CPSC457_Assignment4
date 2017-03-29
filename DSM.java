import java.lang.InterruptedException;

public class DSM implements Runnable{
  private LocalMemory localMemory;
  private BroadcastAgent broadcastAgent;
  private TokenRingAgent tokenRingAgent;

  private String officialName;
  private int processID;

  private int index;
  private int value;
  private boolean doALoad;
  private boolean doAWrite;

  private Thread dsmThread;
  private Thread procThread;
  private Thread broadcastAgentThread;
  private Thread TokenRingAgentThread;

  public DSM(Thread procThread, int processID, Thread broadcastSystemThread, BroadcastSystem broadcastSystem, Thread tokenRingAgentThread, TokenRingAgent tokenRingAgent){
	  officialName = "DSM of processor id: " + processID;

    this.procThread = procThread;
    this.processID = processID;

    localMemory = new LocalMemory(processID); // initializes the processor's local memory

    broadcastAgent = new BroadcastAgent( broadcastSystemThread, localMemory, processID, procThread, broadcastSystem); // initializes a new broadcastAgent
    broadcastAgentThread = broadcastAgent.startThread(); // Starts the broadcastAgent's thread

    this.tokenRingAgent = tokenRingAgent;					// creates a reference to TokenRingAgent of the corresponding ID 
    this.TokenRingAgentThread = tokenRingAgentThread;		// creates a reference to TokenRingAgent Thread of the corresponding ID

    dsmThread = new Thread(this);		// Initializes a new Thread for this DSM
  }

  // sets flags to indicate the processor is trying to do a load
  private void setLoadCommand(){
    doALoad = true;
    doAWrite = false;
  }

  // sets flags to indicate the processor is trying to do a store
  private void setStoreCommand(){
    doALoad = false;
    doAWrite = true;
  }

  // sets flags indicating a load and the index of where the load will happen
  public  void doALoad(int index){
    this.index = index;
    setLoadCommand();

  }
  
  // sets flags indicating a store and gets the index of the store and the value we wish to store
  public void doAStore(int index, int value){
	  this.index = index;
	  this.value = value;
	  setStoreCommand();

  }

  // returns the value that was set as the value to store
  public int getValue(){return value;}


  // starts the DSM thread and returns the thread
  public Thread startThread(){
	  dsmThread.start();
	  return dsmThread;
  }

  public void run(){
	  PrintToScreen.threadMessage(officialName, "Starting DSM thread");
    while(true){
      try{
        //wait for interrupt from processor
        while(true){
        	Thread.sleep(100);
        }
      }catch(InterruptedException e){
        //determine what DSM has to do
        if(doALoad){
          // do a load from local memory
          value = this.load(index);
//          PrintToScreen.threadMessage(officialName, "Finished loading, now waking up the processor");
          //wake up the processor
          procThread.interrupt();

        }else{
          if(doAWrite){
            //do a store to local memory
        	  store(index, value);
          }else{
            //We should never get here
          }
        }

      }
    }
  }

  // returns the value stored inside the specified address
  private int load(int address){
    return localMemory.load(address);
  }


  private void store(int address, int value){
    while(tokenRingAgent.getID() == null);			// Prevents a processor from following through with it's store until it's TokenRingAgent is holding the Token
    localMemory.store(address, value);

    //Use the broadcastAgent to tell the others of this write
    broadcastAgent.doaBroadcast(address,value);
    broadcastAgentThread.interrupt();

  }
}
