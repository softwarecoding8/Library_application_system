package library.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;




public class IssuedBook {
	//private final ObjectProperty<LocalDate> returndate;
	private final ObjectProperty<LocalDate> issue_date;
	//dafault constructor
		public IssuedBook() {
			this(null);
		}
		//constructor with some initial data
		public IssuedBook(LocalDate issue_dateIn) {
		
			this.issue_date = new SimpleObjectProperty<LocalDate>(issue_dateIn);
			//his.returndate = new SimpleObjectProperty(returndateIn);
		}
		public LocalDate getIssuedDate() {
			return issue_date.get();
		}
		
		public void setIssuedDate(LocalDate issue_dateIn) {
			this.issue_date.set(issue_dateIn);
		}
		
		public ObjectProperty<LocalDate> issuedDateProperty() {
			return issue_date;
		}
		
}
