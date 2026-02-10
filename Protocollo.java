import java.io.Serializable;
import java.util.ArrayList;

public class Protocollo implements Serializable {
    private Opzione opzione;
    private ArrayList<Integer> voti;
    private String username;
    private String password;
    private String msg;
    private Indirizzo indirizzo;
    public Protocollo() {
        voti = new ArrayList<>();
    }

    public Opzione getOpzione() {
        return opzione;
    }

    public void setOpzione(Opzione opzione) {
        this.opzione = opzione;
    }

    public ArrayList<Integer> getVoti() {
        return voti;
    }

    public void setVoti(ArrayList<Integer> voti) {
        this.voti = voti;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }
}
