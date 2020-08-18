package library.assistant.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import library.assistant.data.model.Book;
import library.assistant.data.model.MailServerInfo;
import library.assistant.data.model.Member;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author afsal
 */
public class DataHelper {

    private final static Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());

    public static boolean insertNewBook(Book book) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO BOOK(title,author,publisher,isAvail,price) VALUES(?,?,?,?,?)");
//            statement.setString(1, "B"+countBookExists());
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setBoolean(4, book.getAvailability());
            statement.setDouble(5, book.getBookPrice());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }

    public static boolean insertNewMember(Member member) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO MEMBER(name,mobile,email,position) VALUES(?,?,?,?)");
//            statement.setString(1, "MEM"+countMemberExists());
            statement.setString(1, member.getName());
            statement.setString(2, member.getMobile());
            statement.setString(3, member.getEmail());
            statement.setInt(4, member.getPermission());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }

    public static boolean insertNewEmployee(Member member) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO MEMBER(name,mobile,email,position,password) VALUES(?,?,?,?,?)");
//            statement.setString(1, "NV"+countEmployeeExists());
            statement.setString(1, member.getName());
            statement.setString(2, member.getMobile());
            statement.setString(3, member.getEmail());
            statement.setInt(4, member.getPermission());
            statement.setString(5, member.getPassword());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }
    

//    public static boolean isBookExists(String id) {
//        try {
//            String checkstmt = "SELECT COUNT(*) FROM BOOK WHERE id=?";
//            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
//            stmt.setString(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                System.out.println(count);
//                return (count > 0);
//            }
//        } catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return false;
//    }
    
//    public static int countBookExists() {
//        try {
//            String checkstmt = "SELECT COUNT(*) FROM BOOK where isAvail = ?";
//            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
//            stmt.setBoolean(1, true);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                return count + 1;
//            }
//        } catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return 0;
//    }

//    public static int countBookExists() {
//        try {
//            String checkstmt = "SELECT COUNT(*) FROM BOOK";
//            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                return count + 1;
//            }
//        } catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return 0;
//    }
//    
//    public static int countMemberExists() {
//        try {
//            String checkstmt = "SELECT COUNT(*) FROM MEMBER where position = ?";
//            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
//            stmt.setInt(1, 3);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                return count + 1;
//            }
//        } catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return 0;
//    }
//
//    public static int countEmployeeExists() {
//        try {
//            String checkstmt = "SELECT COUNT(*) FROM MEMBER where position = ?";
//            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
//            stmt.setInt(1, 2);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                return count + 1;
//            }
//        } catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return 0;
//    }

    public static Member getMemberByEmail(String mEmail) {
        try {
            String checkstmt = "SELECT * FROM MEMBER WHERE email=?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
            stmt.setString(1, mEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");
                String id = rs.getString("id");
                String email = rs.getString("email");

                Member m = new Member(id, name, mobile, email);
                m.setPassword(rs.getString("password"));
                m.setPosition(rs.getInt("position"));
                return m;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return null;
    }

    public static ResultSet getBookInfoWithIssueData(String id) {
        try {
            String query = "SELECT BOOK.title, BOOK.author, BOOK.isAvail, ISSUE.issueTime FROM BOOK LEFT JOIN ISSUE on BOOK.id = ISSUE.bookID where BOOK.id = ?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return null;
    }

    public static void wipeTable(String tableName) {
        try {
            Statement statement = DatabaseHandler.getInstance().getConnection().createStatement();
            statement.execute("DELETE FROM " + tableName + " WHERE TRUE");
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }

    public static boolean updateMailServerInfo(MailServerInfo mailServerInfo) {
        try {
            wipeTable("MAIL_SERVER_INFO");
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO MAIL_SERVER_INFO(server_name,server_port,user_email,user_password,ssl_enabled) VALUES(?,?,?,?,?)");
            statement.setString(1, mailServerInfo.getMailServer());
            statement.setInt(2, mailServerInfo.getPort());
            statement.setString(3, mailServerInfo.getEmailID());
            statement.setString(4, mailServerInfo.getPassword());
            statement.setBoolean(5, mailServerInfo.getSslEnabled());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }

    public static MailServerInfo loadMailServerInfo() {
        try {
            String checkstmt = "SELECT * FROM MAIL_SERVER_INFO";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String mailServer = rs.getString("server_name");
                Integer port = rs.getInt("server_port");
                String emailID = rs.getString("user_email");
                String userPassword = rs.getString("user_password");
                Boolean sslEnabled = rs.getBoolean("ssl_enabled");
                return new MailServerInfo(mailServer, port, emailID, userPassword, sslEnabled);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return null;
    }
}
