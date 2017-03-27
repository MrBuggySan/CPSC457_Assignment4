import java.lang.InterruptedException;

public class DSM implements Runnable{
  private LocalMemory localMemory;
  private BroadcastAgent broadcastAgent;
  
  private ProcAndDSMComms procAndDSMComms;
  private DSMandBAgentComms dSMandBAgentComms;
  
  private String officialName;
  private int processID;
  
  
  private Thread dsmThread;
  private Thread procThread;
  private Thread broadcastAgentThread;
  
  public DSM(ProcAndDSMComms procAndDSMComms, Thread procThread, int processID, Thread broadcastSystemThread){
	  officialName = "DSM of " + procThread.getName() + ", processor id: " + processID;
	  this.procAndDSMComms = procAndDSMComms;
    this.procThread = procThread;
    this.processID = processID;
    
    localMemory = new LocalMemory(processID);
    dSMandBAgentComms = new DSMandBAgentComms();
    broadcastAgentThread = (new BroadcastAgent(dSMandBAgentComms, broadcastSystemThread, localMemory, processID)).startThread();
  
    dsmThread = new Thread(this);
  }
  
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
        if(procAndDSMComms.doILoad()){
          // do a load from local memory
          procAndDSMComms.setValue(this.load(procAndDSMComms.getIndex()));
//          PrintToScreen.threadMessage(officialName, "Finished loading, now waking up the processor");
          //wake up the processor
          procThread.interrupt();

        }else{
          if(procAndDSMComms.doIStore()){
            //do a store to local memory
        	  store(procAndDSMComms.getIndex(), procAndDSMComms.getValue());
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
    dSMandBAgentComms.doaBroadcast(address,value);
    broadcastAgentThread.interrupt();
    
  }
}
