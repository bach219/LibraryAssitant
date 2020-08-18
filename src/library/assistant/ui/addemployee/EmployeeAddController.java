/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.addemployee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.data.model.Member;
import library.assistant.database.DataHelper;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.main.MainController;
import library.assistant.util.LibraryAssistantUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class EmployeeAddController implements Initializable {

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXPasswordField password;
//    @FXML
//    private JFXTextField id;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private Boolean isInEditMode = false;

    DatabaseHandler handler;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getInstance();
    }

    @FXML
    private void addEmployee(ActionEvent event) {
        String mName = StringUtils.trimToEmpty(name.getText());
//        String mID = StringUtils.trimToEmpty(id.getText());
        String mMobile = StringUtils.trimToEmpty(mobile.getText());
        String mEmail = StringUtils.trimToEmpty(email.getText());
        String pword = DigestUtils.shaHex(password.getText());

        Boolean flag = mName.isEmpty() /* || mMobile.isEmpty()*/ || mEmail.isEmpty() || password.getText().isEmpty();
        if (flag) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return;
        }

        if (isInEditMode) {
            handleUpdateMember();
            return;
        }

        Member member = DataHelper.getMemberByEmail(mEmail);
        if (member != null) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Duplicate member email", "Member with same id exists.\nPlease use new Email");
            return;
        }
//        if(LibraryAssistantUtil.validateEmailAddress(mEmail)){
//            AlertMaker.showErrorMessage("Failed", "Failed to parse email format");
//            return;
//        }

        member = new Member(mName, mMobile, mEmail);
        member.setPosition(2);
        member.setPassword(pword);
        boolean result = DataHelper.insertNewEmployee(member);
        if (result) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "New Employee added", mName + " has been added");
//            DatabaseHandler.getInstance().getBookGraphStatistics();
//            DatabaseHandler.getInstance().getMemberGraphStatistics();
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed to add new Employee", "Check you entries and try again.");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    public void infalteUI(Member member) {
        name.setText(member.getName());
//        id.setText(member.getId());
//        id.setEditable(false);
        mobile.setText(member.getMobile());
        email.setText(member.getEmail());
        password.setText(member.getPassword());

        isInEditMode = Boolean.TRUE;
    }

    private void clearEntries() {
        name.clear();
//        id.clear();
        mobile.clear();
        email.clear();
        password.clear();
    }

    private void handleUpdateMember() {
        Member member = new Member(name.getText(), mobile.getText(), email.getText());
        member.setPassword(DigestUtils.shaHex(password.getText()));
        if (DatabaseHandler.getInstance().updateEmployee(member)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success", "Employee data updated.");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed", "Cant update Employee.");
        }
    }

}
