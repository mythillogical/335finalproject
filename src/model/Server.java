package model;

public class Server {
	private String serverID;
	private String name;
	private double totalTips;
	
	public Server (String serverID, String name) {
		this.serverID = serverID;
		this.name = name;
		this.totalTips = 0;
	}
	public void addTips(double amount) {
		this.totalTips += amount; 
	}
	public String getServerID() {
		return serverID;
	}
	public String getName() {
		return name;
	}
	public double getTotalTips() {
		return totalTips;
	}
	
	@Override
    public String toString() {
    	return serverID + "\t" + name + "\t" + Double.toString(totalTips);
    }
}
