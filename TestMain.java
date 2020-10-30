import java.util.*;

public class TestMain{
    public static void main(String [] args) {
        MySecureDataContainer<Integer> database = new MySecureDataContainer<>();

        //creo gli utenti
        database.createUser("peppe", "pippo");
        database.createUser("pluto", "poi");

        //inserisco dei dati relativi agli utenti
        try {
            database.put("peppe", "pippo", 22);
            database.put("peppe", "pippo", 13);
            database.put("peppe", "pippo", 13);
            database.put("peppe", "pippo", 13);
            database.put("peppe", "pippo", 67);
            database.put("peppe", "pippo", 88);

            database.put("pluto", "poi", 99);
            database.put("pluto", "poi", 55);
        }catch (NullPointerException e){e.printStackTrace();}
        catch (NotPresentException e){e.printStackTrace();}

        //restituisce un dato richiesto usando la get
        try {
            System.out.println(database.get("pluto", "poi", 55));
        }catch (NullPointerException e){e.printStackTrace();}
        catch (NotPresentException e){e.printStackTrace();}

        //utilizzo un iteratore e il metodo getSize che mostrano gli elementi relativi
        //agli utenti e il numero di dati in possesso per ogni utente
        try {
            Iterator itr = database.getIterator("peppe", "pippo");
            while(itr.hasNext())
            {
                System.out.println(itr.next());
            }
            System.out.println(database.getSize("peppe", "pippo"));
            Iterator itr2 = database.getIterator("pluto", "poi");
            while(itr2.hasNext())
            {
                System.out.println(itr2.next());
            }
            System.out.println(database.getSize("pluto", "poi"));
        }catch (NullPointerException e){e.printStackTrace();}
        catch (NotPresentException e){e.printStackTrace();}

        //viene mostrata la rimozione di un dato, e usando l'iteratore e il metodo getSize
        //viene visualizzato il mutamento dei dati in possesso all'utente
        try {
            database.remove("peppe", "pippo", 13);
            Iterator itr = database.getIterator("peppe", "pippo");
            while(itr.hasNext())
            {
                System.out.println(itr.next());
            }
            System.out.println(database.getSize("peppe", "pippo"));
        }catch (NullPointerException e){e.printStackTrace();}
        catch (NotPresentException e){e.printStackTrace();}

        //viene fatta la copia di un dato già esistente, ma prima di poter far vedere il cambiamento
        //viene fatta una remove sull'iteratore. Non essendo ammissibile viene bloccata l'esecuzione
        //del try e catturata l'eccezione UnsupportedOperationException
        try {
            database.copy("pluto", "poi", 99);
            Iterator itr2 = database.getIterator("pluto", "poi");
            itr2.next();
            itr2.remove();
            while(itr2.hasNext())
            {
                System.out.println(itr2.next());
            }
            System.out.println(database.getSize("pluto", "poi"));
        }catch (NullPointerException e){e.printStackTrace();}
        catch (NotPresentException e){e.printStackTrace();}
        catch (UnsupportedOperationException e){e.printStackTrace();}

        //viene fatta la share
        try {
            database.share("pluto", "poi", "peppe", 55);
        }catch (NullPointerException e){e.printStackTrace();}
        catch (NotPresentException e){e.printStackTrace();}

        //viene mostrato che il metodo implementato getDataifAllowed è funzionante
        //se viene correttamente autorizzato l'utente per quel dato
        try {
            System.out.println(database.getDataifAllowed("peppe", "pippo", "pluto", 55));
        }catch (NullPointerException e){e.printStackTrace();}
        catch (NotPresentException e){e.printStackTrace();}

        //viene rimossso un utente
        try {
            database.RemoveUser("pluto", "poi");
        }catch (NullPointerException e){e.printStackTrace();}
        catch (NotPresentException e){e.printStackTrace();}

        //si prova a rimuovere un dato di un utente che non esiste e viene dunque lanciata
        //l'eccezione NotPresentException
        try {
            database.remove("pluto", "poi", 99);
        }catch (NotPresentException e) {e.printStackTrace();}
        catch (NullPointerException e){e.printStackTrace();}

        //viene creato un nuovo utente (uguale al primo per dimostrare l'assenza di errori del tipo:
        //presenza dell'utente nella collezione)
        try {
            database.createUser("pluto", "poi");
        }catch (IllegalArgumentException e) {e.printStackTrace();}
        catch (NullPointerException e){e.printStackTrace();}

        //viene inserito un dato dall'utente appena creato
        try {
            database.put("pluto", "poi", 77);
        }catch (NotPresentException e) {e.printStackTrace();}
        catch (NullPointerException e){e.printStackTrace();}

        //vengono mostrati quali sono i dati appartenenti all'ultimo utente creato
        //dimostrando che non è rimasto alcun dato relativo all'utente prima della sua cancellazione
        //e che attualmente ne fa parte solo quello appena creato
        try {
            Iterator itr2 = database.getIterator("pluto", "poi");
            while(itr2.hasNext())
            {
                System.out.println(itr2.next());
            }
            System.out.println(database.getSize("pluto", "poi"));
        }catch (NotPresentException e) {e.printStackTrace();}
        catch (NullPointerException e){e.printStackTrace();}

    }

}

/*
OUTPUT PREVISTO:
55
22
13
13
13
67
88
6
99
55
2
22
67
88
3
eccezione (UnsupportedOperationException) si può presentare in posti diversi nell'esecuzione ma si riferisce alla remove dell'iteratore
55
eccezione (NotPresentException) si può presentare in posti diversi nell'esecuzione ma si riferisce alla remove del dato
77
1
*/
