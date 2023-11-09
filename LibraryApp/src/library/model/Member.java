package library.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {
	private final IntegerProperty serial_number;
	private final IntegerProperty member_id;
	private final StringProperty firstName;
	private final StringProperty lastName;
	private final StringProperty location;
	private final StringProperty mobile_number;
	private final StringProperty email;
	
	//dafault constructor
	public Member() {
		this(null,null,null,null,null,null,null);
	}
	
	//constructor //with some initial data//with no data
	public Member(Integer serial_numberIn,Integer member_idIn,String firstNameIn,String lastNameIn,String locationIn,String mobile_numberIn,String emailIn) {
		this.serial_number = new SimpleIntegerProperty(serial_numberIn);
		this.member_id = new SimpleIntegerProperty(member_idIn);
		this.firstName = new SimpleStringProperty(firstNameIn);
		this.lastName = new SimpleStringProperty(lastNameIn);
		this.location = new SimpleStringProperty(locationIn);
		this.mobile_number = new SimpleStringProperty(mobile_numberIn);
		this.email = new SimpleStringProperty(emailIn);
		
	}
	
	public Integer getSerialNumber(){
		return this.serial_number.get();
	}
	
	public void setSerialNumber(Integer value) {
		this.serial_number.set(value);
	}
	
	public IntegerProperty serialNumberProperty() {
		return this.serial_number;
	}
	
	public int getMemberId() {
		return this.member_id.get();
	}
	
	public void setMemberId(int member_id) {
		this.member_id.set(member_id);
	}
	
	public IntegerProperty  memberIdProperty() {
		return member_id;
	}
	
	public String getFirstName() {
		return this.firstName.get();
	}
	
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	
	public StringProperty firstNameProperty() {
		return firstName;
	}
	
	public String getLastName() {
		return this.lastName.get();
	}
	
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	
	public StringProperty lastNameProperty() {
		return lastName;
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
		return this. mobile_number.get();
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

