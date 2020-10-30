import java.util.*;

public class Users {
    private String Id;
    private String passw;

    public Users(String Id, String passw){
        this.Id = Id;
        this.passw = passw;
    }

    public String getId(){
        return this.Id;
    }

    public String getpassw(){
        return this.passw;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users u = (Users) o;
        return Objects.equals(Id, u.getId()) &&
                Objects.equals(passw, u.getpassw());
    }

    public int hashCode() {
        return Objects.hash(Id, passw);
    }
}