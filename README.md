#  Library Application System.

A desktop application to ease the work of librarians. The library application system allows a librarian to track books and their quantity, books issued to members, fine charges, generate and send order list of books to supplier via email, view total registered suppliers ,and total registered members ,and  generate reports. Librarian  can also notify members with overdue via email. Librarian can view the total number of books issued per month.

It is  developed using JavaFX programming language. 

# Libraries/Dependencies Used

* GSon - JSON Library. Used for storing configuration.
*	Pdfjet/itextpdf – Export/Generate PDF.
*	JavaMail API - Email Notification.
*	MySQL JDBC.
*	JDK 64 bit 18.0.1.1.
*	JavaFx -  javafx-sdk 19 .
*	Jaspersoft Studio 6.20.6 -eclipse marketplace.
*	e(fx)clipse 3.8.0 plugins- eclipse marketplace.
*	JavaFx-scene builder 2.0.
*	XAMPP.
*	Eclipse IDE.

# Features

*	Perform general CRUD operations on Books ,Suppliers ,Issued Books and Members
*	Issue a book to a member
*	Receive  a book return from a member
*	Search for a book by title.
*	Search for a member by firstname and lastname.
*	Search for a supplier by name and location.
*	Charge fee on lost/damaged book.
*	Charge a rent fee on book returns
*	Members with overdue can be notify via email instantly.
*	Can send email with/without attached file  to supplier.
*	User authorization (Admin, Librarian)
*	The system can generate reports like total registered members, available books, issued books, fine of the lost /damaged books collected , and overdue fine.
*	The system can export records in PDF.
*	User can change password.
*	User can reset password-through Forgot Password module.
*	Has a dashboard.
*	Statistics-the system can compute the total number of issued books per month.

# Setup

1.	Download the project source code.
1.	To eliminate SQL error, cut the database folder and paste it to where accessible in your computer system.
1.	Create an SQL database named "library".
1.	Import the library.sql file to the database.
1.	Import the source code project to the IDE. (I used Eclipse to develop this project.)
1.	Import the Jar library files to the project build path.
1.	Go to the marketplace in Eclipse. Install Jaspersoft Studio,e(fx)clipse library.
1.	Run the project. (If the project fails to run, check  JavaFX and JDK configuration, and ensure that all Jar library files are imported to the project build path).
1.	Configure Basic settings and Mail Server information under settings in the File menu.
1.	 If the Database connection is not established, check the configuration
1.	Now, you're good to go!!! Modify it for your own use-case.

## Default Login Credentials

 **Username	 Password**

 Admin	    12345

## References. 
https://github.com/afsalashyana/Library-Assistant

https://code.makery.ch/library/javafx-tutorial/

https://github.com/hanlinag/point-of-sale-system

JavaFX with Jasper Report and Database-
https://www.youtube.com/watch?v=5HP0EqDtmTU&list=PLoL1XrgyvFSw3fccsLbDPuKo3jPHdwVrr





