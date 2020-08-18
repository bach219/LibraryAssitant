package library.assistant.ui.listmember;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.data.model.Member;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.BookAddController;
import library.assistant.ui.addemployee.EmployeeAddController;
import library.assistant.ui.addmember.MemberAddController;
import library.assistant.ui.login.LoginController;
import library.assistant.ui.main.MainController;
import library.assistant.util.LibraryAssistantUtil;

public class MemberListController implements Initializable {

    ObservableList<Member> list = FXCollections.observableArrayList();

    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> mobileCol;
    @FXML
    private TableColumn<Member, String> emailCol;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private TableColumn<Member, String> positionCol;
    @FXML
    private JFXTextField search;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData("");
    }

    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    private void loadData(String search) {
        list.clear();

        try {
            String qu = "SELECT * FROM MEMBER WHERE CONCAT(id, name, mobile, email) like '%" + search + "%'";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(qu);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");
                String id = rs.getString("id");
                String email = rs.getString("email");

                Member m = new Member(id, name, mobile, email);
                m.setPassword(rs.getString("password"));
                m.setPosition(rs.getInt("position"));
                list.add(m);

            }
        } catch (SQLException ex) {
            Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.setItems(list);
    }

    @FXML
    private void handleMemberDelete(ActionEvent event) {
        //Fetch the selected row
        Member selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("No member selected", "Please select a member for deletion.");
            return;
        }
        if (DatabaseHandler.getInstance().isMemberHasAnyBooks(selectedForDeletion)) {
            AlertMaker.showErrorMessage("Cant be deleted", "This member has some books.");
            return;
        }
        if (LoginController.member.getPermission() >= selectedForDeletion.getPermission()) {
            AlertMaker.showErrorMessage("Cant be deleted", "You dont't have permission to delete this employee.");
            return;
        }
        String delete = LoginController.member.getPosition();
        Optional<ButtonType> answer = AlertMaker.showQuestionDeleting("Deleting " + delete, "Are you sure want to delete " + selectedForDeletion.getName() + " ?");
        if (answer.get() == ButtonType.OK) {
            Boolean result = DatabaseHandler.getInstance().deleteMember(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert(delete + " deleted", selectedForDeletion.getName() + " was deleted successfully.");
                list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Failed", selectedForDeletion.getName() + " could not be deleted");
            }
        } else {
            AlertMaker.showSimpleAlert("Deletion cancelled", "Deletion process cancelled");
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData("");
    }

    @FXML
    private void handleMemberEdit(ActionEvent event) {
        System.out.println(LoginController.member);
        Parent parent = null;
        Member selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("No member selected", "Please select a member for edit.");
            return;
        }
        try {
            Stage stage = new Stage(StageStyle.DECORATED);
            if (selectedForEdit.getPermission() == 3) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addmember/member_add.fxml"));
                parent = loader.load();

                MemberAddController controller = (MemberAddController) loader.getController();
                controller.infalteUI(selectedForEdit);
                stage.setTitle("Edit Member");
            } else {
                if (LoginController.member.getPermission() < selectedForEdit.getPermission() || (LoginController.member.getPermission() == 1 && LoginController.member.getPermission() == selectedForEdit.getPermission())) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addemployee/employee_add.fxml"));
                    parent = loader.load();

                    EmployeeAddController controller = (EmployeeAddController) loader.getController();
                    controller.infalteUI(selectedForEdit);
                    stage.setTitle("Edit Employee");
                } else {
                    AlertMaker.showErrorMessage("Cant be edited", "You dont't have permission to edit this employee.");
                    return;
                }
            }

            stage.setScene(new Scene(parent));
            stage.show();
            LibraryAssistantUtil.setStageIcon(stage);

            stage.setOnHiding((e) -> {
                handleRefresh(new ActionEvent());
            });

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void exportAsPDF(ActionEvent event) {
        List<List> printData = new ArrayList<>();
        String[] headers = {"   Name    ", "ID", "Mobile", "    Email   "};
        printData.add(Arrays.asList(headers));
        for (Member member : list) {
            List<String> row = new ArrayList<>();
            row.add(member.getName());
            row.add(member.getId());
            row.add(member.getMobile());
            row.add(member.getEmail());
            printData.add(row);
        }
        LibraryAssistantUtil.initPDFExprot(rootPane, contentPane, getStage(), printData);
    }

    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        loadData(search.getText());
    }

}
