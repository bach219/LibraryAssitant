package library.assistant.ui.settings;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.data.model.MailServerInfo;
import library.assistant.data.model.Member;
import library.assistant.database.DataHelper;
import library.assistant.database.DatabaseHandler;
import library.assistant.database.export.DatabaseExporter;
import library.assistant.ui.login.LoginController;
import library.assistant.ui.mail.TestMailController;
import library.assistant.util.LibraryAssistantUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SettingsController implements Initializable {

    @FXML
    private JFXTextField nDaysWithoutFine;
    @FXML
    private JFXTextField finePerDay;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField serverName;
    @FXML
    private JFXTextField smtpPort;
    @FXML
    private JFXTextField emailAddress;

    private final static Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());
    @FXML
    private JFXPasswordField emailPassword;
    @FXML
    private JFXCheckBox sslCheckbox;
    @FXML
    private JFXSpinner progressSpinner;
    @FXML
    private StackPane rootContainer;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField email;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaultValues();
        loadData();
    }

    private void loadData(){
        Member m = LoginController.member;
        id.setEditable(false);
        username.setText(m.getName());
        id.setText(m.getId());
        mobile.setText(m.getMobile());
        email.setText(m.getEmail());
        password.setText(m.getPassword());
    }
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        int ndays = Integer.parseInt(nDaysWithoutFine.getText());
        float fine = Float.parseFloat(finePerDay.getText());

        Preferences preferences = Preferences.getPreferences();
        preferences.setnDaysWithoutFine(ndays);
        preferences.setFinePerDay(fine);

        Preferences.writePreferenceToFile(preferences);
    }

    private Stage getStage() {
        return ((Stage) nDaysWithoutFine.getScene().getWindow());
    }

    private void initDefaultValues() {
        Preferences preferences = Preferences.getPreferences();
        nDaysWithoutFine.setText(String.valueOf(preferences.getnDaysWithoutFine()));
        finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
//        username.setText(String.valueOf(preferences.getUsername()));
//        String passHash = String.valueOf(preferences.getPassword());
//        password.setText(passHash.substring(0, Math.min(passHash.length(), 10)));
        loadMailServerConfigurations();
    }

    @FXML
    private void handleTestMailAction(ActionEvent event) {
//        if(LibraryAssistantUtil.validateEmailAddress(emailAddress.getText())){
//            AlertMaker.showErrorMessage("Failed", "Failed to parse email format");
//            return;
//        }
        MailServerInfo mailServerInfo = readMailSererInfo();
        if (mailServerInfo != null) {
            TestMailController controller = (TestMailController) LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/mail/test_mail.fxml"), "Test Email", null);
            controller.setMailServerInfo(mailServerInfo);
        }
    }

    @FXML
    private void saveMailServerConfuration(ActionEvent event) {
//        if(LibraryAssistantUtil.validateEmailAddress(emailAddress.getText())){
//            AlertMaker.showErrorMessage("Failed", "Failed to parse email format");
//            return;
//        }
        MailServerInfo mailServerInfo = readMailSererInfo();
        if (mailServerInfo != null) {
            if (DataHelper.updateMailServerInfo(mailServerInfo)) {
                AlertMaker.showSimpleAlert("Success", "Saved successfully!");
            } else {
                AlertMaker.showErrorMessage("Failed", "Something went wrong!");
            }
        }
    }

    private MailServerInfo readMailSererInfo() {
        try {
            MailServerInfo mailServerInfo
                    = new MailServerInfo(serverName.getText(), Integer.parseInt(smtpPort.getText()), emailAddress.getText(), emailPassword.getText(), sslCheckbox.isSelected());
            if (!mailServerInfo.validate() || !LibraryAssistantUtil.validateEmailAddress(emailAddress.getText())) {
                throw new InvalidParameterException();
            }
            return mailServerInfo;
        } catch (Exception exp) {
            AlertMaker.showErrorMessage("Invalid Entries Found", "Correct input and try again");
            LOGGER.log(Level.WARN, exp);
        }
        return null;
    }

    private void loadMailServerConfigurations() {
        MailServerInfo mailServerInfo = DataHelper.loadMailServerInfo();
        if (mailServerInfo != null) {
            LOGGER.log(Level.INFO, "Mail server info loaded from DB");
            serverName.setText(mailServerInfo.getMailServer());
            smtpPort.setText(String.valueOf(mailServerInfo.getPort()));
            emailAddress.setText(mailServerInfo.getEmailID());
            emailPassword.setText(mailServerInfo.getPassword());
            sslCheckbox.setSelected(mailServerInfo.getSslEnabled());
        }
    }

    @FXML
    private void handleDatabaseExportAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Location to Create Backup");
        File selectedDirectory = directoryChooser.showDialog(getStage());
        if (selectedDirectory == null) {
            AlertMaker.showErrorMessage("Export cancelled", "No Valid Directory Found");
        } else {
            DatabaseExporter databaseExporter = new DatabaseExporter(selectedDirectory);
            progressSpinner.visibleProperty().bind(databaseExporter.runningProperty());
            new Thread(databaseExporter).start();
        }
    }

    @FXML
    private void handleSaveAccount(ActionEvent event) {
        String mName = StringUtils.trimToEmpty(username.getText());
        String mID = StringUtils.trimToEmpty(id.getText());
        String mMobile = StringUtils.trimToEmpty(mobile.getText());
        String mEmail = StringUtils.trimToEmpty(email.getText());
        String pword = DigestUtils.shaHex(password.getText());

        Boolean flag = mName.isEmpty() || mID.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty() || password.getText().isEmpty();
        if (flag) {
            AlertMaker.showErrorMessage("Insufficient Data", "Please enter data in all fields.");
            return;
        }

        Member member = new Member(id.getText(), username.getText(), mobile.getText(), email.getText());
        member.setPassword(DigestUtils.shaHex(pword));
        System.out.println(member);
        if (DatabaseHandler.getInstance().updateEmployee(member)) {
            AlertMaker.showErrorMessage("Success", "Employee data updated.");
        } else {
            AlertMaker.showErrorMessage("Failed", "Can't update Employee.");
        }
    }
}
