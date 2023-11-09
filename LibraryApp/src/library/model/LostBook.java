package library.model;

import java.sql.Date;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LostBook {
	private final IntegerProperty serial_number;
	private final StringProperty id;
	private final StringProperty isbn;
	private final StringProperty title;
	private final FloatProperty price;
	private final IntegerProperty elapdays;
	private final FloatProperty fine;
	private final FloatProperty paid;
	private final FloatProperty balance;
	
	
	public LostBook(Integer serial_numberIn,String idIn,String isbnIn,String titleIn,Float priceIn,Integer elapdaysIn,Float fineIn,Float paidIn,Float balanceIn) {
		this.serial_number = new SimpleIntegerProperty(serial_numberIn);
		this.id = new SimpleStringProperty(idIn);
		this.isbn = new SimpleStringProperty(isbnIn);
		this.title = new SimpleStringProperty(titleIn);
		this.price = new SimpleFloatProperty(priceIn);
		this.elapdays = new SimpleIntegerProperty(elapdaysIn);
		this.fine = new SimpleFloatProperty(fineIn);
		this.paid = new SimpleFloatProperty(paidIn);
		this.balance = new SimpleFloatProperty(balanceIn);
	}
	
	//getters
	public Integer getSerialNumber(){
		return this.serial_number.get();
	}
	
	
	public String getID() {
		
		return this.id.get();
	}
	
	public String getISBN() {
		return this.isbn.get();
	}
	
	public String getTitle() {
		return this.title.get();
	}
	
	public Float getPrice() {
		return this.price.get();
	}
	
	public Integer getElapDays(){
		return this.elapdays.get();
	}
	public Float getFine(){
		return this.fine.get();
	}
	public Float getPaid(){
		return this.paid.get();
	}
	public Float getBalance(){
		return this.balance.get();
	}
	
	//setters
	public void setSerialNumber(Integer value) {
		this.serial_number.set(value);
	}
	
	public void setID(String value) {
		this.id.set(value);
	}
	
	public void setISBN(String value) {
		this.isbn.set(value);
	}

	public void setTitle(String value) {
		this.title.set(value);
	}

	public void setPrice(Float value) {
		this.price.set(value);
	}
	public void setElapDays(Integer value) {
		this.elapdays.set(value);
	}
	public void setFine(Float value) {
		this.fine.set(value);
	}
	public void setPaid(Float value) {
		this.paid.set(value);
	}
	public void setBalance(Float value) {
		this.balance.set(value);
	}
	//property values
	public IntegerProperty serialNumberProperty() {
		return this.serial_number;
	}
	
	public StringProperty idProperty() {
		return this.id;
	}
	
	public StringProperty isbnProperty() {
		return this.isbn;
	}
	
	public StringProperty titleProperty() {
		return this.title;
	}
	public FloatProperty priceProperty() {
		return this.price;
	}
	public IntegerProperty elapDaysProperty() {
		return this.elapdays;
	}
	public FloatProperty fineProperty() {
		return this.fine;
	}
	public FloatProperty paidProperty() {
		return this.paid;
	}
	public FloatProperty balanceProperty() {
		return this.balance;
	}
}
