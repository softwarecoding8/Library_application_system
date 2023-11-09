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

public class ReturnBook{
	private final IntegerProperty serial_number;
	private final StringProperty member_fname;
	private final StringProperty book_title;
	private final ObjectProperty<Date> issuedate;
	private final ObjectProperty<Date> reportdate;
	private final IntegerProperty elapdays;
	private final FloatProperty fine;
	private final FloatProperty paid;
	private final FloatProperty balance;
	
	
public ReturnBook(Integer serial_numberIn,String member_fnameIn,String book_titleIn,Date issuedateIn,Date reportdateIn,Integer elapdaysIn,Integer fineIn,Integer paidIn,Integer balanceIn) {
	this.serial_number = new SimpleIntegerProperty(serial_numberIn);
	this.member_fname = new SimpleStringProperty(member_fnameIn);
	this.book_title = new SimpleStringProperty(book_titleIn);
	this.issuedate = new SimpleObjectProperty(issuedateIn);
	this.reportdate = new SimpleObjectProperty(reportdateIn);
	this.elapdays = new SimpleIntegerProperty(elapdaysIn);
	this.fine = new SimpleFloatProperty(fineIn);
	this.paid = new SimpleFloatProperty(paidIn);
	this.balance = new SimpleFloatProperty(balanceIn);
}

	//getters
	public Integer getSerialNumber(){
		return this.serial_number.get();
	}
	
	
	public String getMemberFName() {
		
		return this.member_fname.get();
	}
	
	public String getBookTitle() {
		return this.book_title.get();
	}
	public Date getIssueDate() {
		return this.issuedate.get();
	}
	public Date getReportDate() {
		return this.reportdate.get();
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
	
	public void setMemberFName(String value) {
		this.member_fname.set(value);
	}
	
	public void setBookTitle(String value) {
		this.book_title.set(value);
	}
	public void setIssueDate(Date value) {
		this.issuedate.set(value);
	}
	public void setReportDate(Date value) {
		this.reportdate.set(value);
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
	
	public StringProperty memberFNameProperty() {
		return this.member_fname;
	}
	
	public StringProperty bookTitleProperty() {
		return this.book_title;
	}
	public ObjectProperty<Date> issuedateProperty(){
		return this.issuedate;
	}
	public ObjectProperty<Date> reportdateProperty(){
		return this.reportdate;
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
