package library.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {
	private final IntegerProperty serial_number;
	private final StringProperty isbn;
	private final StringProperty title;
	private final StringProperty author;
	private final StringProperty edition;
	private final StringProperty publisher;
	private final FloatProperty price;
	private final IntegerProperty quantity;

	//dafault constructor
	public Book() {
		this(null,null,null,null,null,null,null,null);
	}
	
	//constructor //with some initial data//with no data
	public Book(Integer serial_numberIn,String isbnIn,String titleIn,String authorIn,String editionIn,String publisherIn,Float priceIn,Integer quantityIn) {
		this.serial_number = new SimpleIntegerProperty(serial_numberIn);
		this.isbn = new SimpleStringProperty(isbnIn);
		this.title = new SimpleStringProperty(titleIn);
		this.author = new SimpleStringProperty(authorIn);
		this.edition = new SimpleStringProperty(editionIn);
		this.publisher = new SimpleStringProperty(publisherIn);
		this.price = new SimpleFloatProperty(priceIn);
		this.quantity = new SimpleIntegerProperty(quantityIn);
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
	
	public String getISBN() {
		return this.isbn.get();
	}
	
	public void setISBN(String isbn) {
		this.isbn.set(isbn);
	}
	
	public StringProperty isbnProperty() {
		return isbn;
	}
	
	public String getTitle() {
		return this.title.get();
	}
	
	public void setTitle(String title) {
		this.title.set(title);
	}
	
	public StringProperty titleProperty() {
		return title;
	}
	public String getAuthor() {
		return this.author.get();
	}
	
	public void setAuthor(String author) {
		this.author.set(author);
	}
	
	public StringProperty authorProperty() {
		return author;
	}
	public String getEdition() {
		return this.edition.get();
	}
	
	public void setEdition(String edition) {
		this.edition.set(edition);
	}
	
	public StringProperty editionProperty() {
		return edition;
	}
	public String getPublisher() {
		return this.publisher.get();
	}
	
	public void setPublisher(String publisher) {
		this.publisher.set(publisher);
	}
	
	public StringProperty publisherProperty() {
		return publisher;
	}
	

	public Float getPrice() {
		return this.price.get();
	}
	
	public void setPrice(Float price) {
		this.price.set(price);
	}
	
	public FloatProperty priceProperty() {
		return price;
	}
	
	public Integer getQuantity() {
		return this.quantity.get();
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity.set(quantity);
	}
	
	public IntegerProperty quantityProperty() {
		return quantity;
	}
}
