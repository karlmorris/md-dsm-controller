package dsvm.broker;

import dsvm.executor.ExecutorManager;

public class I_Manager_Stub {
	public static void APICall(Object object){
		System.out.println("API Call made with: " + object.toString());
		new Thread(new ReturnEvent()).start();
	}
	
	public static void APICall(){
		System.out.println("API Call made with no parameters");
		System.out.println("return event");
	}
}

class ReturnEvent implements Runnable {
    public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("return event");
		ExecutorManager.getInstance().handleEvent("EVENT_FILE_SENT");
    }
}
