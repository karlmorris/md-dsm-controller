package dsvm.se;

import dsvm.executor.ExecutorManager;

public class Driver {
	public static void main(String[] args){
		ExecutorManager.getInstance().executeScript("SendSchema(\"11111\", \"cvmTest1\", \"cvmTest2\", \"null\", \"<negotiation></negotiation>\");");
	}
}
