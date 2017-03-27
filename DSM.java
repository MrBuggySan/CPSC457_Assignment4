import java.lang.InterruptedException;

public class DSM implements Runnable{
  private LocalMemory localMemory;
  private BroadcastAgent broadcastAgent;



  private String officialName;
  private int processID;

  private int index;
  private int value;
  private boolean doALoad;
  private boolean doAWrite;

  private Thread dsmThread;
  private Thread procThread;
  private Thread broadcastAgentThread;

  public DSM(Thread procThread, int processID, Thread broadcastSystemThread, BroadcastSystem broadcastSystem){
	  officialName = "DSM of processor id: " + processID;

    this.procThread = procThread;
    this.processID = processID;

    localMemory = new LocalMemory(processID);

    broadcastAgent = new BroadcastAgent( broadcastSystemThread, localMemory, processID, broadcastSystem);
    broadcastAgentThread = broadcastAgent.startThread();

    dsmThread = new Thread(this);
  }

  private void setLoadCommand(){
    doALoad = true;
    doAWrite = false;
  }

  private void setStoreCommand(){
    doALoad = false;
    doAWrite = true;
  }


  public  void doALoad(int index){
    this.index = index;
    setLoadCommand();

  }

  public void doAStore(int index, int value){
	  this.index = index;
	  this.value = value;
	  setStoreCommand();

  }

  public int getValue(){return value;}




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

  private int load(int address){
    return localMemory.load(address);
  }


  private void store(int address, int value){
    //while(not have the token);
    localMemory.store(address, value);

    //Use the broadcastAgent to tell the others of this write
    broadcastAgent.doaBroadcast(address,value);
    broadcastAgentThread.interrupt();

  }
}
