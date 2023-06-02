package example.com.two.mine;


public class User {
//    private String avator;
    private long id;
    private String account;
    private String password;
    private String username;
    private String sex;
    private String type;

    public User(long id, String account, String password, String username, String sex) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.username = username;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +

                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User() {

    }

    public User(long id, String account, String password, String username, String sex, String type) {

        this.id = id;
        this.account = account;
        this.password = password;
        this.username = username;
        this.sex = sex;
        this.type = type;
    }
}
