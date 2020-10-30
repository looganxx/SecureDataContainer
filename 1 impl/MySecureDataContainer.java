import java.util.*;
/*
AF: a(map) = f: K -> V dove K è di tipo Users, e V è un vector di tipo Data
    for all Users ==> esiste un vettore di tipo Data
IR: map != null && for all i. map[i] != null &&
    for all i,j. 0 <= i < j < map.size() ==> map.users[i] != map.users[j]
*/
public class MySecureDataContainer<E> implements SecureDataContainer<E>{
    private HashMap<Users, Vector<Data>> map;

    public class Data <E>{
        private E data;
        private Vector<String> allowedUsers;

        private Data(E data){
            this.data = data;
            this.allowedUsers = new Vector<String>();
        }

        public E getData() {
            return data;
        }

        public Vector<String> getAllowedUsers() {
            return allowedUsers;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Data data = (Data) o;
            return Objects.equals(this.data, data.getData());
        }

        @Override
        public int hashCode() {
            return Objects.hash(data, allowedUsers);
        }

    }

    public MySecureDataContainer(){
        map = new HashMap();
    }

    public void createUser(String Id, String passw) throws NullPointerException, IllegalArgumentException {
        if (Id == null || passw == null) throw new NullPointerException();
        if (Id.isEmpty() || passw.isEmpty()) throw new IllegalArgumentException("Id o password non possono essere vuoti");
        if (check(Id)<0){
            Users u = new Users(Id, passw);
            map.put(u, new Vector<Data>());
        }else throw new IllegalArgumentException("Utente già presente");
    }

    public void RemoveUser(String Id, String passw)throws NotPresentException, NotPresentException{
        if(Id == null || passw == null) throw new NullPointerException();
        Users u = new Users(Id,passw);
        if(!map.containsKey(u)) throw new NotPresentException("Utente non presente");
        map.remove(u);
    }

    public int getSize(String Owner, String passw) throws NullPointerException, NotPresentException {
        if (Owner == null || passw == null) throw new NullPointerException();
        Users u = new Users(Owner, passw);
        if (!(map.containsKey(u))) throw new NotPresentException("Utente non presente");
        return map.get(u).size();
    }

    public boolean put(String Owner, String passw, E data) throws NullPointerException, NotPresentException {
        if(Owner==null || passw == null || data == null) throw new NullPointerException();
        Users u = new Users(Owner, passw);
        if(!map.containsKey(u)) throw new NotPresentException("Utente non presente");
        return map.get(u).add(new Data(data));
    }

    public E get(String Owner, String passw, E data) throws NullPointerException, NotPresentException {
        if(Owner==null || passw == null || data == null) throw new NullPointerException();
        Users u = new Users(Owner, passw);
        if(!map.containsKey(u)) throw new NotPresentException("Utente non presente");
        Data d = new Data(data);
        int i = map.get(u).indexOf(d);
        if(i<0) throw new NotPresentException("data not present");
        d = map.get(u).get(i);
        return (E) d.getData();
    }

    public E remove(String Owner, String passw, E data) throws NullPointerException, NotPresentException {
        if(Owner==null || passw == null || data == null) throw new NullPointerException();
        Users u = new Users(Owner, passw);
        if(!map.containsKey(u)) throw new NotPresentException("Utente non presente");
        Data d = new Data(data);
        int i = map.get(u).indexOf(d);
        if(i<0) throw new NotPresentException("data not present");
        map.get(u).removeIf(Data -> Data.equals(d));
        return data;
    }

    public void copy(String Owner, String passw, E data) throws NullPointerException, NotPresentException {
        if(Owner==null || passw == null || data == null) throw new NullPointerException();
        Users u = new Users(Owner, passw);
        if(!map.containsKey(u)) throw new NotPresentException("Utente non presente");
        Data d = new Data(data);
        int i = map.get(u).indexOf(d);
        if(i<0) throw new NotPresentException("Data not present");
        d = map.get(u).get(i); //in questo modo copio anche il vettore AllowedUsers
        map.get(u).add(d);
    }

    public void share(String Owner, String passw, String Other, E data) throws NullPointerException, NotPresentException {
        if(Owner==null || passw == null || data == null || Other == null) throw new NullPointerException();
        Users u = new Users(Owner, passw);
        if(!map.containsKey(u)) throw new NotPresentException("Utente non presente");
        if (check(Other)<0) throw new NotPresentException("Utente per lo share non presente");
        Data d = new Data(data);
        int i = map.get(u).indexOf(d);
        if(map.get(u).indexOf(d) < 0) throw new NotPresentException("Data not present");
        for(Data df : map.get(u)){
            //si fa il for per aggiungere Other a tutte le occorrenze di data
            if(df.equals(d)) df.getAllowedUsers().add(Other);
        }

    }

    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, NotPresentException {
        if(Owner==null || passw == null) throw new NullPointerException();
        Users u = new Users(Owner, passw);
        if(!map.containsKey(u)) throw new NotPresentException("Utente non presente");

        Vector<E> newVect = getAllData(Owner, passw);

        return new Iterator<E>(){
          private int index  = -1;

          @Override
          public boolean hasNext(){
              return (index < newVect.size()-1);
          }

          @Override
          public E next() throws NoSuchElementException{
              if(hasNext()){
                  index++;
                  return newVect.get(index);
              }else throw new NoSuchElementException();
          }

          @Override
          public void remove()throws UnsupportedOperationException {
              throw new UnsupportedOperationException();
          }
        };
    }

    public E getDataifAllowed (String Owner, String passw, String Other, E data) throws NullPointerException, NotPresentException {
        if (Owner == null || passw == null || data == null || Other == null) throw new NullPointerException();
        Users u = new Users(Owner, passw);
        if (!map.containsKey(u)) throw new NotPresentException("Utente non presente");
        if (check(Other) < 0) throw new NotPresentException("Non c'è un altro utente con il nome "+Other);
        String OthPass = getPassw(Other);
        Users oth = new Users(Other, OthPass);
        Data d = new Data(data);
        int dataind = map.get(oth).indexOf(d);
        if(dataind < 0) throw new NotPresentException("Dato non presente");
        d = map.get(oth).get(dataind);
        int indpres = d.getAllowedUsers().indexOf(Owner);
        if(indpres < 0) throw new NotPresentException("Non sei autorizzato ad accedere a questo dato");
        return (E) d.getData();
    }

    private Vector<E> getAllData(String Owner, String passw){
        //quando chiamo questa funzione Owner e passw sono di sicuro diversi da null
        Users u = new Users(Owner, passw);
        Vector<E> newVect = new Vector<>();
        for(Data d: map.get(u)){
            newVect.add((E) d.getData());
        }
        return newVect;
    }


    private int check(String id){
        int i=0;
        for(Users u: map.keySet()){
            if(u.getId().equals(id)) return i;
            i++;
        }
        return -1;
    }

    private String getPassw(String Other){
        for (Users u: map.keySet()) {
            if(u.getId() == Other){
                return u.getpassw();
            }
        }
        //non verrà mai eseguito questo return poichè la presenza di other è verificata nel metodo principale
        return "";
    }
}

class NotPresentException extends Exception{
    public NotPresentException(){
        super();
    }
    public NotPresentException(String s){
        super(s);
    }
}
