package proxy;

public class Service implements Comparable<Service> {
	private String url;
	private int status;
	
	public Service(String url) {
		this.url = url;
		this.status = 0;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public int compareTo(Service other) {
		return Integer.compare(status, other.status);
	}
}
