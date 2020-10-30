import java.util.*;
/*
AF: a(users, data) = f: users -> datas dove users=[i_0...i_users.size()] è di tipo Users, e datas=[i_0...i_datas.size()] è un tipo Data
        ogni dato è associato a un utente tramite un indirizzo che identifica l'utente possessore del dato
IR: users != null && datas != null &&
    for all i. 0 <= i < users.size() ==> users[i] != null
    for all i. 0 <= i < datas.size() ==> datas[i] != null
    for all i,j. 0<= i < j < users.size() ==> users.get[i] != users.get[j]
*/
public class MySecureDataContainer <E> implements SecureDataContainer<E> {
    private Vector <Users> users;
    private Vector <Data> datas;

    private class Data <E>{
        private E data;
        private int indexU;
        private Vector<String> allowedUsers;

        private Data(E data, int indexU){
            this.data = data;
            this.indexU = indexU;
            this.allowedUsers = new Vector<>();
        }

        public E getData() {
            return data;
        }

        public int getIndexU() {
            return indexU;
        }

        public Vector<String> getAllowedUsers() {
            return allowedUsers;
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Data d = (Data) o;
            return Objects.equals(this.data, d.getData()) &&
                    Objects.equals(this.indexU, d.getIndexU());
        }

        public int hashCode() {
            return Objects.hash(data, indexU);
        }

    }

    public MySecureDataContainer (){
        users = new Vector<Users>();
        datas = new Vector<Data>();
    }

    public void createUser(String Id, String passw) throws NullPointerException, IllegalArgumentException {
        if(Id == null || passw == null) throw new NullPointerException();
        if(Id.isEmpty() || passw.isEmpty()) throw new IllegalArgumentException("Id o password non possono essere vuoti");
        if(check(Id)<0){
            Users u = new Users(Id, passw);
            users.add(u);
        }else throw new IllegalArgumentException("Utente non presente");
    }

    public void RemoveUser(String Id, String passw) throws NullPointerException, NotPresentException {
        if(Id == null || passw == null) throw new NullPointerException();
        Users u = new Users(Id,passw);
        int index = users.indexOf(u);
        if(index<0) throw new NotPresentException("Utente non presente");
        users.remove(index);
        datas.removeIf(Data -> Data.getIndexU() == index);

    }

    public int getSize(String Owner, String passw) throws NullPointerException, NotPresentException {
        if(Owner == null || passw == null) throw new NullPointerException();
        Users u = new Users(Owner,passw);
        int index = users.indexOf(u);
        if(index<0) throw new NotPresentException("Utente non presente");
        int count=0;
        for(Data d: this.datas){
            if(d.getIndexU() == index) count++;
        }
        return count;
    }

    public boolean put(String Owner, String passw, E data) throws NullPointerException, NotPresentException {
        if(Owner == null || passw == null || data == null) throw new NullPointerException();
        Users u = new Users(Owner,passw);
        int index = users.indexOf(u);
        if(index<0) throw new NotPresentException("Utente non presente");
        Data d = new Data(data, index);
        return this.datas.add(d);
    }

    public E get(String Owner, String passw, E data) throws NullPointerException, NotPresentException {
        if(Owner == null || passw == null || data == null) throw new NullPointerException();
        Users u = new Users(Owner,passw);
        int index = users.indexOf(u);
        if(index<0) throw new NotPresentException("Utente non presente");
        Data d = new Data(data, index);
        int dataind = datas.indexOf(d);
        if(dataind < 0) throw new NotPresentException("dato non presente");
        return (E) datas.get(dataind).getData();
    }

    public E remove(String Owner, String passw, E data) throws NullPointerException, NotPresentException {
        if(Owner == null || passw == null || data == null) throw new NullPointerException();
        Users u = new Users(Owner,passw);
        int index = users.indexOf(u);
        if(index<0) throw new NotPresentException("Utente non presente");
        Data d = new Data(data, index);
        if(datas.indexOf(d)<0) throw new NotPresentException("dato non presente");
        //elimino tutte le ricorrenze del dato
        datas.removeIf(Data -> Data.equals(d));
        return data;
    }

    public void copy(String Owner, String passw, E data) throws NullPointerException, NotPresentException {
        if(Owner == null || passw == null || data == null) throw new NullPointerException();
        Users u = new Users(Owner,passw);
        int index = users.indexOf(u);
        Data d = new Data(data, index);
        if(index<0) throw new NotPresentException("Utente non presente");
        int dataind = datas.indexOf(d);
        if(dataind < 0) throw new NotPresentException("dato non presente");
        datas.add(datas.get(dataind));
    }

    public void share(String Owner, String passw, String Other, E data) throws NullPointerException, NotPresentException {
        if(Owner == null || passw == null || data == null) throw new NullPointerException();
        Users u = new Users(Owner,passw);
        int index = users.indexOf(u);
        if(index<0) throw new NotPresentException("Utente non presente");
        Data d = new Data(data, index);
        int dataindx = datas.indexOf(d);
        if(dataindx < 0) throw new NotPresentException("dato non presente");
        if(check(Other)<0) throw new NotPresentException("Utente per lo share non presente");
        for(Data df: datas){
          if(df.equals(d)) df.getAllowedUsers().add(Other);
        }
    }

    public Iterator<E> getIterator(String Owner, String passw) throws NullPointerException, NotPresentException {
        if(Owner == null || passw == null) throw new NullPointerException();
        Users u = new Users(Owner,passw);
        int index = users.indexOf(u);
        if(index<0) throw new NotPresentException("Utente non presente");

        Vector<E> newDatas = getAllData(index);

        return new Iterator<E>(){
            private int index  = -1;

            @Override
            public boolean hasNext(){
                return (index < newDatas.size()-1);
            }

            @Override
            public E next() throws NoSuchElementException{
                if(hasNext()){
                    index++;
                    return newDatas.get(index);
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
        int index = users.indexOf(u);
        if(index<0) throw new NotPresentException("Utente non presente");
        int othind = check(Other);
        if (othind<0) throw new NotPresentException("Non c'è un altro utente con il nome "+Other);
        Data d = new Data(data, othind);
        int dataind = datas.indexOf(d);
        if(dataind < 0) throw new NotPresentException("Dato non presente");
        d = datas.get(dataind);
        int indpres = d.getAllowedUsers().indexOf(Owner);
        if(indpres < 0) throw new NotPresentException("Non sei autorizzato ad accedere a questo dato");
        return (E) d.getData();
    }

    private Vector<E> getAllData(int index){
        Vector<E> newVect = new Vector<>();
        for(Data d: datas){
            if(d.getIndexU() == index) newVect.add((E)d.getData());
        }
        return newVect;
    }

    private int check(String id){
        int i = 0;
        for(Users u: users){
            if(id.equals(u.getId())) return i;
            i++;
        }
        return -1;
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
