import java.util.*;

public interface SecureDataContainer<E>{
  /*OVERVIEW: tipo di dato modificabile che permette
            la memorizzazione e condivisione di dati
  TYPICAL ELEMENT: insieme di oggetti che rappresentano un utente (tutti distinti) e i suoi dati relativi di tipo generico E
  */

    // Crea l’identità di un nuovo utente della collezione
    public void createUser(String Id, String passw) throws NullPointerException, IllegalArgumentException;
    /*
    REQUIRES: Id != null && passw != null
    THROWS: se Id, passw == null lancia un NullPointerException (unchecked)
            se le stringhe Id o passw sono vuote lancia un IllegalArgumentException (unchecked)
            se Id esiste già tra gli utenti lancia un IllegalArgumentException (unchecked)
    MODIFIES: this
    EFFECTS: aggiunge un nuovo utente alla collezione
    */

    // Rimuove l’utente dalla collezione
    public void RemoveUser(String Id, String passw)throws NullPointerException, NotPresentException;
    /*
   REQUIRES: Id != null && passw != null
   THROWS: se Id, passw == null lancia un NullPointerException (unchecked)
           se la coppia Id e passw non fa parte della collezione lancia un NotPresentException (checked)
   MODIFIES: this
   EFFECTS: rimuove un utente dalla collezione eliminando i suoi rispettivi dati
   */

    // Restituisce il numero degli elementi di un utente presenti nella
    // collezione
    public int getSize(String Owner, String passw) throws NullPointerException, NotPresentException;
    /*
    REQUIRES: Owner != null && passw != null
    THROWS: se Owner, passw == null lancia un NullPointerException (unchecked)
            se Owner non appartiene a SecureDataContainer lancia un NotPresentException (checked)
    MODIFIES: -
    EFFECTS: Restituisce il numero di dati appartenenti ad Owner presenti nella collezione
    */

    // Inserisce il valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public boolean put(String Owner, String passw, E data) throws NullPointerException, NotPresentException;
    /*
    REQUIRES: Owner != null && passw != null && data != null
    THROWS:  se Owner, passw, data == null lancia un NullPointerException (unchecked)
             se le credenziali di Owner non sono rispettate lancia un NotPresentException (checked)
    MODIFIES: this
    EFFECTS: se le credenziali sono corrette si inserisce il dato e restituisce true
              altrimenti non lo inserisce
    */

    // Ottiene una copia del valore del dato nella collezione
    // se vengono rispettati i controlli di identità
    public E get(String Owner, String passw, E data) throws NullPointerException, NotPresentException;
    /*
    REQUIRES: Owner != null && passw != null && data != null
    THROWS: se Owner, passw, data == null lancia un NullPointerException (unchecked)
            se Owner non appartiene o se le credenziali non sono rispettate lancia un NotPresentException (checked)
            se data non appartiene alla SecureDataContainer lancia NotPresentException (checked)
    MODIFIES: -
    EFFECTS: restituisce una copia del dato relativo all'Owner se presente e se le credenziali sono corrette
    */

    // Rimuove il dato nella collezione
    // se vengono rispettati i controlli di identità
    public E remove(String Owner, String passw, E data) throws NullPointerException, NotPresentException;
    /*
    REQUIRES: Owner != null && passw != null && data != null
    THROWS: se Owner, passw, data == null lancia un NullPointerException (unchecked)
            se le credenziali di Owner non sono rispettate lancia un NotPresentException (checked)
            se data non appartiene alla SecureDataContainer lancia NotPresentException (checked)
    MODIFIES: this
    EFFECTS: se le credenziali sono corrette rimuove il dato corrispondente a Owner e ne restituisce una copia.
    */

    // Crea una copia del dato nella collezione
    // se vengono rispettati i controlli di identità
    public void copy(String Owner, String passw, E data) throws NullPointerException, NotPresentException;
    /*
    REQUIRES: Owner != null && passw != null && data != null
    THROWS: se Owner, passw, data == null lancia un NullPointerException (unchecked)
            se le credenziali di Owner non sono rispettate lancia un NotPresentException (checked)
            se data non appartiene alla SecureDataContainer lancia NotPresentException (checked)
    MODIFIES: this
    EFFECTS: se le credenziali vengono rispettate crea una copia del dato nella SecureDataContainer
    */

    // Condivide il dato nella collezione con un altro utente
    // se vengono rispettati i controlli di identità
    public void share(String Owner, String passw, String Other, E data) throws NullPointerException, NotPresentException;
    /*
    REQUIRES: Owner != null && passw != null && data != null && Other != null
    THROWS: se Other non appartiene alla SecureDataContainer lancia un NotPresentException (checked)
            se Owner, Other, passw, data == null lancia un NullPointerException (unchecked)
            se le credenziali di Owner non sono rispettate lancia un NotPresentException (checked)
            se data non appartiene alla SecureDataContainer lancia NotPresentException (checked)
    MODIFIES: this
    EFFECTS: Condivide il dato nella collezione con un altro utente se vengono rispettati i controlli di identità
    */

    // restituisce un iteratore (senza remove) che genera tutti i dati
    //dell’utente in ordine arbitrario
    // se vengono rispettati i controlli di identità
    public Iterator<E> getIterator(String Owner, String passw)throws NullPointerException, NotPresentException;
    /*
    REQUIRES: Owner != null && passw != null
    THROWS: se Owner, passw == null lancia un NullPointerException (unchecked)
            se Owner non appartiene o se le credenziali non sono rispettate lancia un NotPresentException (checked)
    MODIFIES: -
    EFFECTS: se le credenziali vengono rispettate restituisce un iteratore di tipo E appartenenti a Owner
    */

    public E getDataifAllowed (String Owner, String passw, String Other, E data) throws NullPointerException, NotPresentException;
    /*
    REQUIRES: Owner != null && passw != null && data != null && Other != null
    THROWS: se Other non appartiene alla SecureDataContainer lancia un NotPresentException (checked)
            se Owner, Other, passw, data == null lancia un NullPointerException (unchecked)
            se le credenziali di Owner non sono rispettate lancia un NotPresentException (checked)
            se data non appartiene alla SecureDataContainer lancia NotPresentException (checked)
    MODIFIES: this
    EFFECTS: se le credenziali sono rispettate e se l'utente Other ha autorizzato Owner ad accedere al dato,
              restituisce il dato richiesto da Owner
    */
}
