public class UserInfo {
    private int id;
    private String usn;
    private String name;
    private String username;
    private String password;
    private int accessLevel;

    public UserInfo(int id, String usn, String name, String username, String password, int accessLevel) {
        this.id = id;
        this.usn = usn;
        this.name = name;
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public int getId() {
        return id;
    }

    public String getUsn() {
        return usn;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAccessLevel() {
        return accessLevel;
    }
}
