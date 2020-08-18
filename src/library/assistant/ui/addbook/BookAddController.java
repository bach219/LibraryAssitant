package library.assistant.ui.addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.data.model.Book;
import library.assistant.database.DataHelper;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbook.BookListController;
import org.apache.commons.lang3.StringUtils;

public class BookAddController implements Initializable {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField price;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    private DatabaseHandler databaseHandler;
    private Boolean isInEditMode = Boolean.FALSE;

    Double price1 = 0.0;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
    }
    
    

    @FXML
    private void addBook(ActionEvent event) {
        String bookPrice = StringUtils.trimToEmpty(price.getText());
        String bookAuthor = StringUtils.trimToEmpty(author.getText());
        String bookName = StringUtils.trimToEmpty(title.getText());
        String bookPublisher = StringUtils.trimToEmpty(publisher.getText());

        if (bookPrice.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookPublisher.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return;
        }
        
        try {
            price1 = Double.parseDouble(bookPrice);
        } catch (NumberFormatException e) {
            AlertMaker.showErrorMessage("Failed", "Failed to parse price");
            return;
        }

        if (isInEditMode) {
            handleEditOperation();
            return;
        }
//
//        if (DataHelper.isBookExists(bookID)) {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Duplicate book id", "Book with same Book ID exists.\nPlease use new ID");
//            return;
//        }

        Book book = new Book(bookName, bookAuthor, bookPublisher, Boolean.TRUE, price1);
        System.out.println(book);
        boolean result = DataHelper.insertNewBook(book);
        if (result) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "New book added", bookName + " has been added");
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed to add new book", "Check all the entries and try again");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void checkData() {
        String qu = "SELECT title FROM BOOK";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String titlex = rs.getString("title");
                System.out.println(titlex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void inflateUI(Book book) {
        title.setText(book.getTitle());
//        id.setText(book.getId());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        price.setEditable(false);
        isInEditMode = Boolean.TRUE;
    }

    private void clearEntries() {
        title.clear();
        price.clear();
        author.clear();
        publisher.clear();
    }

    private void handleEditOperation() {
        Book book = new Book(title.getText(), author.getText(), publisher.getText(), true, price1);
        if (databaseHandler.updateBook(book)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success", "Update complete");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed", "Could not update data");
        }
    }
}
