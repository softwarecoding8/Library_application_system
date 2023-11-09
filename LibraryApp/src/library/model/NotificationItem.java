package library.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class NotificationItem {

    private final SimpleBooleanProperty notify;
    private final SimpleStringProperty memberID;
    private final SimpleStringProperty memberFName;
    private final SimpleStringProperty memberEmail;
    private final SimpleStringProperty bookTitle;
    private final SimpleIntegerProperty dayCount;
    private final SimpleFloatProperty fineAmount;
    private final SimpleStringProperty issueDate;

    public NotificationItem(boolean notify, String memberID, String memberFName, String memberEmail,String bookTitle, String issueDate, int dayCount, float fineAmount) {
        this.notify = new SimpleBooleanProperty(notify);
        this.memberID = new SimpleStringProperty(memberID);
        this.memberFName = new SimpleStringProperty(memberFName);
        this.memberEmail = new SimpleStringProperty(memberEmail);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.dayCount = new SimpleIntegerProperty(dayCount);
        this.fineAmount = new SimpleFloatProperty(fineAmount);
        this.issueDate = new SimpleStringProperty(issueDate);
    }

    public Boolean getNotify() {
        return notify.get();
    }

    public String getMemberID() {
        return memberID.get();
    }

    public String getMemberFName() {
        return memberFName.get();
    }

    public String getMemberEmail() {
        return memberEmail.get();
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public Integer getDayCount() {
        return dayCount.get();
    }

    public String getFineAmount() {
        return String.format("Ksh %.2f", fineAmount.get());
    }

    public void setNotify(Boolean val) {
        notify.set(val);
    }

    public String getIssueDate() {
        return issueDate.get();
    }
}
