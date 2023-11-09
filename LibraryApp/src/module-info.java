module LibraryApp {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.desktop;
	requires java.sql;
	requires javafx.base;
	requires com.google.gson;
	//requires com.google.common.collect;
	requires com.jfoenix;
	requires boxable;
	requires pdfbox;
	requires org.apache.commons.codec;
	requires java.mail;
	requires org.apache.logging.log4j;
	requires guava;
	requires java.base;
	requires activation;
	requires javafx.web;
	requires pdfjet;
	requires jasperreports;
	requires itextpdf;
	requires org.controlsfx.controls;
	
	opens library to javafx.graphics, javafx.fxml;
	//exports library.view to javafx.fxml;
	opens library.view to javafx.fxml;
	opens library.model to javafx.base;
	opens library.util to com.google.gson;
}
