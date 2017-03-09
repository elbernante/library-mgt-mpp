package application.model;

public class UserAddress extends Address {

	private String userId;
	
	public UserAddress() {
		this("", "", "", "", "", "");
	}
	
	public UserAddress(String userId, String street, String city, String state, String zip, String phone) {
		super(street, city, state, zip, phone);
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
