import java.lang.InterruptedException;

public class BroadcastAgent implements Runnable{
	private Thread broadcastSystemThread;
	private LocalMemory localMemory;
	
	private boolean doABroadcast;
	private int address;
	private int value;
	
	
	private Thread broadcastAgentThread;
	
	private String officialName;
	public BroadcastAgent( Thread broadcastSystemThread, LocalMemory localMemory, int processID){
		
		officialName = "BroadcastAgent of processor id: " + processID;
		
		
		this.broadcastSystemThread = broadcastSystemThread;
		//This reference will be accessed during recieve
		this.localMemory = localMemory;
		
		broadcastAgentThread = new Thread(this);
	}
	
	public void doaBroadcast(int address, int value){
		doABroadcast = true;
		this.address = address;
		this.value = value;
	}
	
	  public Thread startThread(){
		  broadcastAgentThread.start();
		  return broadcastAgentThread;
	  }

	
  public void run(){
	  	while(true){
	  		try{
	  		//wait for interrupt from DSM to do a store
	  	        while(true){
	  	        	Thread.sleep(100);
	  	        }
	  		}catch(InterruptedException e){
	  			if(doABroadcast){
	  				broadcast(address, value);
	  				
	  				//finished broadcasting
	  				doABroadcast = false;
	  			}else{
	  				//we shound never get here
	  			}
	  		}
	  	}
  }
  

  public void broadcast(int index, int value){
	  PrintToScreen.threadMessage(officialName, "broadcasting a store of index:"+index+" value:"+value);
  }
  
}
