import java.lang.InterruptedException;

public class BroadcastAgent implements Runnable{

	private LocalMemory localMemory;

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
	public BroadcastAgent( Thread broadcastSystemThread, LocalMemory localMemory, int processID, BroadcastSystem broadcastSystem){
		this.broadcastSystemThread = broadcastSystemThread;
		//This reference will be accessed during recieve
		this.localMemory = localMemory;
		this.id = processID;
		this.broadcastSystem = broadcastSystem;
		broadcastAgentThread = new Thread(this);
		broadcastSystem.addBroadcastAgent(this, broadcastAgentThread); // add this to the list of broadcastAgents
		officialName = "BroadcastAgent of processor id: " + processID;

		doABroadcast= false;
		doRecieve = false;
	}

	 public Thread startThread(){
		  broadcastAgentThread.start();
		  return broadcastAgentThread;
	  }

	public void doaBroadcast(int address, int value){
		doABroadcast = true;
		this.address = address;
		this.value = value;
	}

	public void recieveStore(int address, int value){
		storeAddress = address;
		storeValue = value;
		doRecieve = true;
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
	  			if(doABroadcast){
	  				broadcastSystem.doaBroadcast(address, value, id);
	  				broadcastSystemThread.interrupt();
	  				//finished broadcasting
	  				doABroadcast = false;
	  			}else{
	  				if(doRecieve){

	  					PrintToScreen.threadMessage(officialName, "called by BroadcastSystem to store " + storeValue + " into " + storeAddress);
	  					localMemory.store(storeAddress, storeValue);
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
