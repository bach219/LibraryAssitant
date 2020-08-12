package library.assistant.data.model;

/**
 *
 * @author afsal
 */
public class Member {
    private String id;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private int position;

    public Member(String id, String name, String mobile, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Member{" + "id=" + id + ", name=" + name + ", email=" + email + ", mobile=" + mobile + ", password=" + password + ", position=" + position + '}';
    }
    
    
}
