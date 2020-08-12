/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.data.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author HP
 */
public class IssueInfo {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty bookID;
    private final SimpleStringProperty bookName;
    private final SimpleStringProperty holderName;
    private final SimpleStringProperty dateOfIssue;
    private final SimpleIntegerProperty nDays;
    private final SimpleFloatProperty fine;

    public IssueInfo(int id, String bookID, String bookName, String holderName, String dateOfIssue, Integer nDays, float fine) {
        this.id = new SimpleIntegerProperty(id);
        this.bookID = new SimpleStringProperty(bookID);
        this.bookName = new SimpleStringProperty(bookName);
        this.holderName = new SimpleStringProperty(holderName);
        this.dateOfIssue = new SimpleStringProperty(dateOfIssue);
        this.nDays = new SimpleIntegerProperty(nDays);
        this.fine = new SimpleFloatProperty(fine);
        System.out.println(this.nDays.get());
    }

    public Integer getId() {
        return id.get();
    }

    public String getBookID() {
        return bookID.get();
    }

    public String getBookName() {
        return bookName.get();
    }

    public String getHolderName() {
        return holderName.get();
    }

    public String getDateOfIssue() {
        return dateOfIssue.get();
    }

    public Integer getDays() {
        return nDays.get();
    }

    public Float getFine() {
        return fine.get();
    }

}
