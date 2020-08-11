package contacts;
import java.io.*;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Contacts contacts;
        if(args.length > 0) {
            System.out.println(args[0]);
            File file = new File(args[0]);
            if (file.isFile()) {
                contacts = (Contacts) SerializationUtils.deserialize(args[0]);
            } else {
                contacts = new Contacts();
            }
        } else {
            contacts = new Contacts();
        }
        ContactsCommand.contacts = contacts;
        Controller controller = new Controller();
        do {
            controller.executeCommand();
            contacts.printCommands();
        } while (controller.setCommand(scanner.nextLine()));
    }
}
class SerializationUtils {
    /**
     * Serialize the given object to the file
     */
    public static void serialize(Object obj, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }

    /**
     * Deserialize to an object from the file
     */
    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }
}