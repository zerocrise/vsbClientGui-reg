public class Indirizzo
{
    private String via;
    private String civico;
    private String cap;
    private String citta;

    public Indirizzo(String via, String civico, String cap, String citta) {
        this.via = via;
        this.civico = civico;
        this.cap = cap;
        this.citta = citta;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }
}
