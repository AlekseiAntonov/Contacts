package contacts;

import java.io.Serializable;
import java.util.ArrayList;

import static contacts.Main.scanner;

class Contacts implements Serializable {
    private final ArrayList<Contact> contacts = new ArrayList<>();

    private String page = "[menu]";

    public String getPage() {
        return page;
    }

    int chosenRecord;
    ArrayList<Contact> chosenRecords = new ArrayList<>();
    public void setPage(String page) {
        if(page.equals("[menu]") || page.equals("[record]")
                || page.equals("[search]") || page.equals("[list]")) {
            this.page = page;
        }
    }

    public void add(String type) {
        contacts.add(ContactFabric.create(type, scanner));
        if(contacts.get(contacts.size() - 1) == null) contacts.remove(contacts.size() - 1);
    }
    public void edit(int idx){
        if(idx >= contacts.size()){
            System.out.println("Wrong index");
            return;
        }
        contacts.get(idx).edit();
        System.out.println(contacts.get(idx));
    }
    public void remove(int idx){
        if(idx >= contacts.size()){
            System.out.println("Wrong index");
            return;
        }
        contacts.remove(idx);
    }
    public void print(){
        int count = 1;
        for (Contact contact : contacts) {
            System.out.print(count++ + ". ");
            contact.getInfo();
        }
    }

    void printCommands(){
        if(page.equals("[menu]")) {
            System.out.print("[menu] Enter action (add, list, search, count, exit): ");
        } else if (page.equals("[search]")){
            System.out.print("[search] Enter action ([number], back, again):");
        } else if (page.equals("[record]")) {
            System.out.print("[record] Enter action (edit, delete, menu):");
        } else {
            System.out.print("[list] Enter action ([number], back):");
        }

    }

    public boolean isEmpty() {
        return contacts.isEmpty();
    }

    public int size() {
        return contacts.size();
    }
    public int findContactWithIdx(Contact contact){
        for (int i = 0; i < contacts.size(); i++) {
            if(contact == contacts.get(i)){
                return i;
            }
        }
        return -1;
    }
    public void chooseAll(){
        chosenRecords.clear();
        chosenRecords.addAll(contacts);
    }
    public Contact getContact(int idx){
        return (idx < 0 || idx >= contacts.size()) ? null : contacts.get(idx);
    }
}