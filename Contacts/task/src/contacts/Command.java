package contacts;


import static contacts.Main.main;
import static contacts.Main.scanner;

/*public class Command {
    static final String[] menuCommands = {"add", "list", "search", "count", "exit"};
    static final String[] searchCommands = {"[number]", "back", "again"};
    static final String[] recordCommands = {"edit", "delete", "menu"};

    public void execute(Contacts contacts, String command){
        if(contacts == null || command == null) return;
        switch (contacts.getPage()){
            case "menu":

        }

    }
    private void operateMenu(Contacts contacts, String command){
        switch (command) {
            case "add" :

        }
    }
}*/

interface Command {
    void execute();
}

abstract class ContactsCommand implements Command {
    public static Contacts contacts = null;

    @Override
    public void execute() {

    }
}

class AddRecordCommand extends ContactsCommand {
    @Override
    public void execute() {
        if (contacts!= null) {
            System.out.println("Enter the type (person, organization):");
            contacts.add(scanner.nextLine());
        }
    }
}

class EditRecordCommand extends ContactsCommand {
    @Override
    public void execute() {
        contacts.edit(contacts.chosenRecord);
    }
}

class ListCommand extends ContactsCommand {
    @Override
    public void execute() {
        contacts.print();
        contacts.setPage("[list]");
        contacts.chooseAll();
    }
}

class SearchCommand extends ContactsCommand {
    @Override
    public void execute() {
        System.out.println("Enter search query:");
        String query = scanner.nextLine();
        contacts.chosenRecords.clear();
        int count = 1;
        for (int i = 0; i < contacts.size() ; i++) {
            Contact contact = contacts.getContact(i);
            if(contact.search(query)){
                System.out.print(count++ + ". ");
                contact.getInfo();
                contacts.chosenRecords.add(contact);
            }
        }
        contacts.setPage("[search]");
    }
}

class CountCommand extends ContactsCommand {
    @Override
    public void execute() {
        System.out.println("The Phone Book has " + contacts.size() + " records.");
    }
}

class DeleteCommand extends ContactsCommand {

    @Override
    public void execute() {
        contacts.remove(contacts.chosenRecord);
    }
}

class ChooseRecordCommand extends ContactsCommand {
    int chosenRecord;
    ChooseRecordCommand(int chosenRecord){
        this.chosenRecord = chosenRecord;
    }
    @Override
    public void execute() {
        contacts.chosenRecord = contacts.findContactWithIdx(contacts.chosenRecords.get(chosenRecord));
        System.out.println(contacts.chosenRecords.get(chosenRecord));
        contacts.setPage("[record]");
    }
}

class ToTheMenuCommand extends ContactsCommand {
    @Override
    public void execute() {
        contacts.setPage("[menu]");
    }
}

class Controller {

    private Command command = null;

    public boolean setCommand(Command command) {
        this.command = command;
        return true;
    }

    public boolean setCommand(String command) {
        boolean isNotEnd = true;
        switch (command) {
            case "add":
                this.command = new AddRecordCommand();
                break;
            case "list":
                this.command = new ListCommand();
                break;
            case "search":
                this.command = new SearchCommand();
                break;
            case "exit":
                isNotEnd = false;
                break;
            case "edit":
                this.command = new EditRecordCommand();
                break;
            case "delete":
                this.command = new DeleteCommand();
                break;
            case "count":
                this.command = new CountCommand();
                break;
            case "back":
            case "menu":
                this.command = new ToTheMenuCommand();
                break;
            case "again":
                break;
            default:
                if (command.matches("\\d+")){
                   this.command = new ChooseRecordCommand(Integer.parseInt(command) - 1);

                }else {
                    System.out.println("WRONG COMMAND");
                    this.command = null;
                }
                
        }
        return isNotEnd;
    }

    public void executeCommand() {
        if (command != null) {
            command.execute();
            System.out.println();
        }
    }
}