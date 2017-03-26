public class DSM implements Runnable{
  private LocalMemory localMemory;
  private BroadcastAgent broadcastAgent;
  private ProcAndDSMComms procAndDSMComms;
  private Thread procThread;
  private String officialName;
  private int processID;

  public DSM(ProcAndDSMComms procAndDSMComms, Thread procThread, int processID){
    this.procAndDSMComms = procAndDSMComms;
    this.procThread = procThread;
    localMemory = new localMemory();
    this.processID = processID;
    officialName = "DSM of " + procThread.getName() + ", id: " + processID;
  }

  public void run(){
    while(1){
      try{
        //wait for interrupt from processor
        wait();
      }catch(InterrruptedException e){
        //determine what DSM has to do
        if(procAndDSMComms.doILoad()){
          // do a load from local memory
          procAndDSMComms.setValue(this.load(procAndDSMComms.getIndex()));
          PrintToScreen.threadMessage(officialName, "Starting DSM thread");
          //wake up the processor
          procThread.interrupt();

        }else{
          if(procAndDSMComms.doIStore()){
            //do a store to local memory


            //Use the broadcastAgent to tell the others of this write

          }else{
            //We should never get here
          }
        }

      }catch(){

      }
    }
  }

  private int load(int address){
    return localMemory.load(address);
  }

  private void store(int address, int value){
    //while(not have the token);
    localMemory.store(address, value);
    broadcastAgent.broadcast(address, value);
  }
}
