public class ProcAndDSMComms{
  private int index;
  private int value;
  private boolean doALoad;
  private boolean doAWrite;

  public ProcAndDSMComms(){
    doALoad = false;
    doAWrite = false;
    
    
  }

  private void setLoadCommand(){
    doALoad = true;
    doAWrite = false;
  }

  private void setStoreCommand(){
    doALoad = false;
    doAWrite = true;
  }

//used by processor only
  public synchronized void doALoad(int index){
    this.index = index;
    setLoadCommand();
    
  }
  
  public synchronized void doAStore(int index, int value){  
	  this.index = index;
	  this.value = value;
	  setStoreCommand();
	    
  }


//used by DSM only
  public synchronized boolean doILoad(){
    return doALoad;
  }

  public synchronized boolean doIStore(){
    return doAWrite;
  }

//Either processor or dsm can use these
  public synchronized int getIndex(){return index;}

  public synchronized int getValue(){return value;}

  public synchronized void setValue(int value){this.value = value;}
}
