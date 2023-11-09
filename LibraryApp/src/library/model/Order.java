package library.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order {
	private final IntegerProperty serial_number;
	private final StringProperty isbn;
	private final StringProperty title;
	private final FloatProperty price;
	private final IntegerProperty quantity;
	private final FloatProperty subtotal;
	
	//dafault constructor
	public Order() {
		this(null,null,null,null,null,null);
		
	}
	
	//constructor //with some initial data//with no data
	public  Order(Integer serial_numberIn,String isbnIn,String titleIn,Float priceIn,Integer quantityIn,Float subtotalIn) {
		this.serial_number = new SimpleIntegerProperty(serial_numberIn);
		this.isbn = new SimpleStringProperty(isbnIn);
		this.title = new SimpleStringProperty(titleIn);
		this.price = new SimpleFloatProperty(priceIn);
		this.quantity = new SimpleIntegerProperty(quantityIn);
		this.subtotal = new SimpleFloatProperty(subtotalIn);
	}
	
	public int getSerialNumber() {
		return this.serial_number.get();
	}
	
	public void setSerialNumber(int serial_numberIn) {
		this.serial_number.set(serial_numberIn);
	}
	
	public IntegerProperty  serialNumberProperty() {
		return serial_number;
	}
	
	public String getBookISBN() {
		return this.isbn.get();
	}
	
	public void setBookISBN(String isbn) {
		this.isbn.set(isbn);
	}
	
	public StringProperty bookISBNProperty() {
		return isbn;
	}
	
	public String getTitle() {
		return this.title.get();
	}
	
	public float getPrice() {
		return this.price.get();
	}
	
	public void setPrice(float price) {
		this.price.set(price);
	}
	
	public FloatProperty priceProperty() {
		return price;
	}
	
	public void setTitle(String title) {
		this.title.set(title);
	}
	
	public StringProperty titleProperty() {
		return title;
	}
	
	public int getQuantity() {
		return this.quantity.get();
	}
	
	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}
	
	public IntegerProperty  quantityProperty() {
		return quantity;
	}

	public float getSubTotal() {
		return this.subtotal.get();
	}
	
	public void setSubTotal(float subtotal) {
		this.subtotal.set(subtotal);
	}
	
	public FloatProperty subTotalProperty() {
		return subtotal;
	}
	

}
