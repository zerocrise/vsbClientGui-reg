// ClientGUI.java - Interfaccia Swing
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {
    private Client client;


    // Componenti GUI
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextArea txtArea;
    private JButton btnLogin, btnLogout, btnVoti, btnModifica;

    // Componenti per modifica indirizzo
    private JTextField txtVia, txtCitta, txtCivico;

    public ClientGUI() {
        super("Client Application");
        client = new Client("localhost", 11234,this);
        inizializzaGUI();
    }

    private void inizializzaGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Panel superiore - Login
        JPanel panelLogin = new JPanel(new FlowLayout());
        panelLogin.add(new JLabel("Username:"));
        txtUsername = new JTextField(15);
        panelLogin.add(txtUsername);

        panelLogin.add(new JLabel("Password:"));
        txtPassword = new JPasswordField(15);
        panelLogin.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                effettuaLogin();
            }
        });
        panelLogin.add(btnLogin);

        btnLogout = new JButton("Logout");
        btnLogout.setEnabled(false);
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                effettuaLogout();
            }
        });
        panelLogin.add(btnLogout);

        add(panelLogin, BorderLayout.NORTH);

        // Area centrale - Output
        txtArea = new JTextArea();
        txtArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferiore - Azioni
        JPanel panelAzioni = new JPanel(new GridLayout(2, 1));

        // Riga 1 - Pulsanti azioni
        JPanel panelPulsanti = new JPanel(new FlowLayout());
        btnVoti = new JButton("Visualizza Voti");
        btnVoti.setEnabled(false);
        btnVoti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                visualizzaVoti();
            }
        });
        panelPulsanti.add(btnVoti);

        btnModifica = new JButton("Modifica Indirizzo");
        btnModifica.setEnabled(false);
        btnModifica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificaIndirizzo();
            }
        });
        panelPulsanti.add(btnModifica);

        panelAzioni.add(panelPulsanti);

        // Riga 2 - Campi indirizzo
        JPanel panelIndirizzo = new JPanel(new FlowLayout());
        panelIndirizzo.add(new JLabel("Via:"));
        txtVia = new JTextField(15);
        txtVia.setEnabled(false);
        panelIndirizzo.add(txtVia);

        panelIndirizzo.add(new JLabel("Città:"));
        txtCitta = new JTextField(10);
        txtCitta.setEnabled(false);
        panelIndirizzo.add(txtCitta);

        panelIndirizzo.add(new JLabel("CIVICO:"));
        txtCivico = new JTextField(5);
        txtCivico.setEnabled(false);
        panelIndirizzo.add(txtCivico);

        panelAzioni.add(panelIndirizzo);

        add(panelAzioni, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void effettuaLogin() {
        if (!client.connetti()) {
            mostraMessaggio("Errore di connessione al server!");
            return;
        }

        try {
            Protocollo richiesta = new Protocollo();
            richiesta.setOpzione(Opzione.LOGIN);
            richiesta.setUsername(txtUsername.getText());
            richiesta.setPassword(new String(txtPassword.getPassword()));

            client.inviaRichiesta(richiesta);




        } catch (Exception e) {
            mostraMessaggio("Errore durante il login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void effettuaLogout() {
        try {
            Protocollo richiesta = new Protocollo();
            richiesta.setOpzione(Opzione.LOGOUT);
            richiesta.setUsername(txtUsername.getText());

            client.inviaRichiesta(richiesta);



            Thread.sleep(500); // Attende che il thread termini
            client.disconnetti();

            mostraMessaggio("Logout effettuato");
            abilitaFunzioni(false);
            txtUsername.setText("");
            txtPassword.setText("");

        } catch (Exception e) {
            mostraMessaggio("Errore durante il logout: " + e.getMessage());
        }
    }

    private void visualizzaVoti() {
        try {
            Protocollo richiesta = new Protocollo();
            richiesta.setOpzione(Opzione.VOTI);
            richiesta.setUsername(txtUsername.getText());

            client.inviaRichiesta(richiesta);

        } catch (Exception e) {
            mostraMessaggio("Errore nella richiesta voti: " + e.getMessage());
        }
    }

    private void modificaIndirizzo() {
        try {
            String via = txtVia.getText();
            String citta = txtCitta.getText();
            String civico = txtCivico.getText();

            if (via.isEmpty() || citta.isEmpty() || civico.isEmpty()) {
                mostraMessaggio("Compilare tutti i campi dell'indirizzo!");
                return;
            }

            Protocollo richiesta = new Protocollo();
             richiesta.setOpzione(Opzione.MODIFICA);
            richiesta.setUsername(txtUsername.getText());

            Indirizzo nuovoIndirizzo = new Indirizzo(via, citta, civico);
            richiesta.setIndirizzo(nuovoIndirizzo);

            client.inviaRichiesta(richiesta);

        } catch (Exception e) {
            mostraMessaggio("Errore nella modifica indirizzo: " + e.getMessage());
        }
    }

    // Metodo chiamato dal ThreadRicezione
    public synchronized void gestisciRisposta(Protocollo risposta) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                switch (risposta.getOpzione()) {
                    case ACCETTATO:
                        mostraMessaggio("Login effettuato con successo!");
                        abilitaFunzioni(true);
                        break;

                    case NEGATO:
                        mostraMessaggio("Login negato: " + risposta.getMsg());
                        client.disconnetti();

                        break;

                    case VOTI:
                        if (risposta.getVoti() != null && !risposta.getVoti().isEmpty()) {
                            // Risposta per richiesta voti
                            StringBuilder sb = new StringBuilder("Voti ricevuti:\n");
                            for (Integer voto : risposta.getVoti()) {
                                sb.append("- ").append(voto).append("\n");
                            }
                            mostraMessaggio(sb.toString());
                        } else {

                            mostraMessaggio("Nessun voto trovato");
                        }
                        break;
                    case OK:
                        mostraMessaggio("indirizzo modificato con successo");
                    case NO:
                        mostraMessaggio("Operazione fallita: " + risposta.getMsg());
                        break;

                    default:
                        mostraMessaggio("Risposta non gestita: " + risposta.getOpzione());
                        break;
                }
            }
        });
    }

    private void abilitaFunzioni(boolean abilita) {
        btnLogin.setEnabled(!abilita);
        btnLogout.setEnabled(abilita);
        btnVoti.setEnabled(abilita);
        btnModifica.setEnabled(abilita);
        txtUsername.setEnabled(!abilita);
        txtPassword.setEnabled(!abilita);
        txtVia.setEnabled(abilita);
        txtCitta.setEnabled(abilita);
        txtCivico.setEnabled(abilita);
    }

    public synchronized void mostraMessaggio(String messaggio) {
        txtArea.append(messaggio + "\n\n");
        txtArea.setCaretPosition(txtArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ClientGUI gui = new ClientGUI();
                gui.setVisible(true);
            }
        });
    }
}
