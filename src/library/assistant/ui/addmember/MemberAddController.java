package library.assistant.ui.addmember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import library.assistant.database.DataHelper;
import library.assistant.database.DatabaseHandler;
import library.assistant.data.model.Member;
import library.assistant.util.LibraryAssistantUtil;
import org.apache.commons.lang3.StringUtils;

public class MemberAddController implements Initializable {

    DatabaseHandler handler;

    @FXML
    private JFXTextField name;
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
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getInstance();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addMember(ActionEvent event) {
        String mName = StringUtils.trimToEmpty(name.getText());
//        String mID = StringUtils.trimToEmpty(id.getText());
        String mMobile = StringUtils.trimToEmpty(mobile.getText());
        String mEmail = StringUtils.trimToEmpty(email.getText());

        Boolean flag = mName.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty();
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
        member.setPosition(3);
        boolean result = DataHelper.insertNewMember(member);
        if (result) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "New member added", mName + " has been added");
//            DatabaseHandler.getInstance().getBookGraphStatistics();
//            DatabaseHandler.getInstance().getMemberGraphStatistics();
            clearEntries();
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed to add new member", "Check you entries and try again.");
        }
    }

    public void infalteUI(Member member) {
        name.setText(member.getName());
//        id.setText(member.getId());
//        id.setEditable(false);
        mobile.setText(member.getMobile());
        email.setText(member.getEmail());

        isInEditMode = Boolean.TRUE;
    }

    private void clearEntries() {
        name.clear();
//        id.clear();
        mobile.clear();
        email.clear();
    }

    private void handleUpdateMember() {
        Member member = new Member( name.getText(), mobile.getText(), email.getText());
        if (DatabaseHandler.getInstance().updateMember(member)) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success", "Member data updated.");
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed", "Cant update member.");
        }
    }

}
