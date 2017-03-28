import java.util.Random;


public class BroadcastSystem implements Runnable{

  private static byte bLimit = 0;
  private BroadcastAgent[] broadcastAgentList;
  private Thread[] broadcastAgentThreadList;
  private String officialName;


  private boolean doABroadcast;
	private int address;
	private int value;
	private int callerID;
	private int procSize;

  private int numBrodAgentReady = 0;

  private BroadcastSystem(int procSize){
  	this.procSize = procSize;
  	broadcastAgentList = new BroadcastAgent[procSize];
  	broadcastAgentThreadList = new Thread[procSize];
    officialName = TextColor.ANSI_GREEN + "BroadcastSystem" + TextColor.ANSI_RESET;
  }

  //only one BroadcastSystem can exist
  public static BroadcastSystem getInstance(int procSize){
    if( bLimit < 1 ){
        BroadcastSystem broadcastSystem = new BroadcastSystem(procSize);
        bLimit = 1;
        return broadcastSystem;
    }
    return null;
  }


  public void setValue(int value){
	  this.value= value;
  }

  public void doaBroadcast(int address, int value, int id){
		doABroadcast = true;
		this.callerID = id;
		this.address = address;
		this.value = value;
	}

  public synchronized void incrNumBrodAgentReady(){
    numBrodAgentReady++;
  }

  public synchronized boolean isReady(){
	  return (numBrodAgentReady == (procSize - 1))?  true : false;
	}
  
  public void run(){
    PrintToScreen.threadMessage("BroadcastSystem", "Starting BroadcastAgent thread");
	  while(true){
	  		try{
	  		//wait for interrupt from DSM to do a store
	  	        while(true){
	  	        	Thread.sleep(100);
	  	        }
	  		}catch(InterruptedException e){
	  			try{
	  				if(doABroadcast){
              PrintToScreen.threadMessage(officialName, "proc "+ callerID + " wants everyone else to store " + value + ", at index " + address);
              for(int i = 0 ; i < procSize; i++){
		  					if(i == callerID) continue;
		  					broadcastAgentList[i].recieveStore(address, value);
		  					broadcastAgentThreadList[i].interrupt();

		  				}
              //simulate random delay in broadcastSystem
              Random rand = new Random();
              int n = rand.nextInt(20) + 1; //random delay between 1ms and 20ms
              PrintToScreen.threadMessage(officialName, "random sleep initiated");
              Thread.sleep(n);

              // //wait for the broadcastAgents to recieve the store
              while(!this.isReady()){
                // Thread.sleep(n);
              }
              numBrodAgentReady = 0;
		  				//finished broadcasting
		  				doABroadcast = false;
		  				broadcastAgentList[callerID].procInterrupt();
		  			}else{
		  				//we should never get here
		  			}
	  			}catch(InterruptedException t){
              //I hope we don't get here
              PrintToScreen.threadMessage(officialName, "interrupted while still broadcasting");

	  			}

	  		}
	  	}
  }

  public void addBroadcastAgent(BroadcastAgent broadcastAgent, Thread broadcastAgentThread){
	  broadcastAgentList[broadcastAgent.getID()] = broadcastAgent;
	  broadcastAgentThreadList[broadcastAgent.getID()]= broadcastAgentThread;
  }
}
