import java.lang.InterruptedException;

public class BroadcastAgent implements Runnable{

	private LocalMemory localMemory;

	private Thread procThread;
	private Thread broadcastSystemThread;
	private BroadcastSystem broadcastSystem;

	private boolean doABroadcast;
	private int address;
	private int value;
	int id;

	private boolean doRecieve;
	private int storeAddress;
	private int storeValue;


	private Thread broadcastAgentThread;

	private String officialName;
	public BroadcastAgent( Thread broadcastSystemThread, LocalMemory localMemory, int processID, Thread procThread, BroadcastSystem broadcastSystem){
		this.broadcastSystemThread = broadcastSystemThread;
		//This reference will be accessed during recieve
		this.localMemory = localMemory;
		this.id = processID;
		this.broadcastSystem = broadcastSystem;
		this.procThread = procThread;
		broadcastAgentThread = new Thread(this);
		broadcastSystem.addBroadcastAgent(this, broadcastAgentThread); // add this to the list of broadcastAgents
		officialName = "BroadcastAgent of processor id: " + processID;

		doABroadcast= false;
		doRecieve = false;
	}

	// starts the broadcastAgent Thread and returns the thread
	 public Thread startThread(){
		  broadcastAgentThread.start();
		  return broadcastAgentThread;
	  }

	// sets flags indicating a broadcast
	public void doaBroadcast(int address, int value){
		doABroadcast = true;
		this.address = address;
		this.value = value;
	}
	
	// sets flags indicating a store being received
	public void recieveStore(int address, int value){
		storeAddress = address;
		storeValue = value;
		doRecieve = true;
	}

	// interrupts this thread
	public void procInterrupt(){
		this.procThread.interrupt();
	}

  public void run(){
		PrintToScreen.threadMessage(officialName, "Starting BroadcastAgent thread");
	  	while(true){
	  		try{
	  		//wait for interrupt from DSM to do a store
	  	        while(true){
	  	        	Thread.sleep(100);
	  	        }
	  		}catch(InterruptedException e){
	  			if(doABroadcast){					// If the flags indicate a is being made broadcast, sends an interrupt to broadcastSystem
	  				broadcastSystem.doaBroadcast(address, value, id);
	  				broadcastSystemThread.interrupt();
	  				//finished broadcasting
	  				doABroadcast = false;
	  			}else{
	  				if(doRecieve){					// If the flags indicate a broadcast is being received, store the values received into local memory

	  					PrintToScreen.threadMessage(officialName, "called by BroadcastSystem to store " + storeValue + " into " + storeAddress);
	  					localMemory.store(storeAddress, storeValue);
							//Let the BroadcastSystem know that we are done
							broadcastSystem.incrNumBrodAgentReady();
	  					doRecieve = false;
	  				}else{
	  				//we shound never get here
	  				}

	  			}
	  		}
	  	}
  }




  public int getID(){return id;}
}
