package library.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Supplier {
	private final IntegerProperty serial_number;
	private final IntegerProperty id;
	private final StringProperty name;
	private final StringProperty email;
	private final StringProperty location;
	private final StringProperty mobile_number;
	
	//dafault constructor
	public Supplier() {
		this(null,null,null,null,null,null);
	}
	
	//constructor //with some initial data//with no data
	public Supplier(Integer serial_numberIn,Integer idIn,String nameIn,String emailIn,String mobile_numberIn,String locationIn) {
		this.serial_number = new SimpleIntegerProperty(serial_numberIn);
		this.id = new SimpleIntegerProperty(idIn);
		this.name = new SimpleStringProperty(nameIn);
		this.email = new SimpleStringProperty(emailIn);
		this.mobile_number = new SimpleStringProperty(mobile_numberIn);
		this.location = new SimpleStringProperty(locationIn);
	}
	
	public int getSerialNumber() {
		return this.serial_number.get();
	}
	
	public void setSerialNumber(int serial_number) {
		this.serial_number.set(serial_number);
	}
	
	public IntegerProperty  serialNumberProperty() {
		return serial_number;
	}
	
	public int getId() {
		return this.id.get();
	}
	
	public void setId(int id) {
		this.id.set(id);
	}
	
	public IntegerProperty  idProperty() {
		return id;
	}
	public String getName() {
		return this.name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public String getLocation() {
		return this.location.get();
	}
	
	public void setLocation(String location) {
		this.location.set(location);
	}
	
	public StringProperty locationProperty() {
		return location;
	}
	
	public String getMobileNumber() {
		return this.mobile_number.get();
	}
	
	public void setMobileNumber(String mobile_number) {
		this.mobile_number.set(mobile_number);
	}
	
	public StringProperty  mobileNumberProperty() {
		return mobile_number;
	}
	
	public String getEmail() {
		return this.email.get();
	}
	
	public void setEmail(String email) {
		this.email.set(email);
	}
	
	public StringProperty emailProperty() {
		return email;
	
}
}
