package owl.cs.analysis.utilities;

public class WSConfig {
	
	public final String SERVER ;
	public final String PORT;
	
	//TODO Make configurable
	public WSConfig() {
		this("http://localhost","8080");
	}
	
	public WSConfig(String SERVER, String PORT) {
		//TODO Make sure all well formed!
		this.SERVER = SERVER;
		this.PORT = PORT;
	}

	public String getBaseAdress(String WS) {
		return SERVER + ":" + PORT+WS;
	}

}
