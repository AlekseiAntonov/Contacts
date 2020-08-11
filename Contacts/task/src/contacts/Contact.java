package contacts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static contacts.Main.scanner;

public abstract class Contact implements Edit, Info{
    protected String phoneNumber = "";

    LocalDateTime dateOfCreation = LocalDateTime.now();

    LocalDateTime dateOfEditing = LocalDateTime.now();

    boolean hasNumber(){return !phoneNumber.isEmpty();}

    public boolean setPhoneNumber(String phoneNumber) {

        String[] parts = phoneNumber.split("[\\- ]");
        boolean isValid = true;
        boolean isUsedBrackets = false;
        for (int i = 0; i < parts.length; i++) {
            if (i == 0){
                if(parts[0].length() > 1 &&
                        (parts[0].charAt(0) == '(' || (parts[0].charAt(0) == '+' && parts[0].charAt(1) == '(') ||
                                parts[0].charAt(parts[0].length() - 1) == ')')) {
                    isValid = (parts[0].charAt(0) == '(' ||(parts[0].charAt(0) == '+' && parts[0].charAt(1) == '('))
                            && parts[0].charAt(parts[0].length() - 1) == ')';
                    if(!isValid) break;
                    isUsedBrackets = true;
                }
                Pattern pattern = Pattern.compile("[+]?[(]?\\w+[)]?",Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(parts[0]);
                isValid = matcher.matches();
            }
            if (i == 1){
                if(parts[1].charAt(0) == '(' && parts[1].charAt(parts[1].length() - 1) == ')') {
                    isValid = parts[1].charAt(0) == '(' && parts[1].charAt(parts[1].length() - 1) == ')';
                    if (isUsedBrackets) isValid = false;
                    isUsedBrackets = true;
                    if(!isValid) break;
                }
                Pattern pattern = Pattern.compile("[(]?\\w{2,}[)]?",Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(parts[1]);
                isValid = matcher.matches();
            }
            if(i > 1){
                Pattern pattern = Pattern.compile("\\w{2,}",Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(parts[i]);
                isValid = matcher.matches();
            }
            if(!isValid) break;
        }
        if (isValid){
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Wrong number format!");
            this.phoneNumber = "";
        }
        return isValid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    abstract public boolean search(String string);
}

class Person extends Contact{
    private String name;
    private String surname;
    LocalDate birthDate = null;
    String gender = "";
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        if(birthDate != null){
            try {
                this.birthDate = LocalDate.parse(birthDate);
            } catch (Exception e) {
                System.out.println("Bad birth date!");
            }
        }

    }

    public void setGender(String gender) {
        if (gender == null || !(gender.equals("M") || gender.equals("F"))){
            gender = "";
            System.out.println("Bad gender!");
        } else {
            this.gender = gender;
        }
    }

    @Override
    public String toString() {
        String name = "Name: " + this.name;
        String surname = "Surname: " + this.surname;
        String birthDate = "Birth date: " + (this.birthDate == null ? "[no data]" : this.birthDate.toString());
        String gender = "Gender: " + (this.gender.equals("") ? "[no data]" : this.gender);
        String number = "Number: " + (!hasNumber() ? "[no data]" : getPhoneNumber());
        String timeCreated = "Time created: " + dateOfCreation.withNano(0).toString();
        String timeEdited = "Time last edit: " + dateOfEditing.withNano(0).toString();
        return name + "\n" +
                surname + "\n" +
                birthDate + "\n" +
                gender + "\n" +
                number + "\n" +
                timeCreated + "\n" +
                timeEdited;
    }

    @Override
    public void edit() {
        LocalDateTime prevEdit = dateOfEditing;
        dateOfEditing = LocalDateTime.now();
        if (scanner == null) return;
        System.out.println("Select a field (name, surname, birth, gender, number):");
        String field = scanner.nextLine();
        boolean isEdited = true;
        switch (field) {
            case "name":
                System.out.println("Enter " + field + ":");
                setName(scanner.nextLine());
                break;
            case "surname":
                System.out.println("Enter " + field + ":");
                setSurname(scanner.nextLine());
                break;
            case "birth":
                System.out.println("Enter " + field + ":");
                setBirthDate(scanner.nextLine());
                break;
            case "gender":
                System.out.println("Enter " + field + ":");
                setGender(scanner.nextLine());
                break;
            case "number":
                System.out.println("Enter " + field + ":");
                setPhoneNumber(scanner.nextLine());
                break;
            default:
                dateOfEditing = prevEdit;
                System.out.println("Wrong field");
                isEdited = false;
        }
        if (isEdited){
            System.out.println("Saved");
        }
    }

    @Override
    public void getInfo() {
        System.out.println(name + " " + surname);
    }

    @Override
    public boolean search(String string) {
        Pattern pattern = Pattern.compile(string, Pattern.CASE_INSENSITIVE);
        Matcher matcherName = pattern.matcher(name);
        Matcher matcherSurname = pattern.matcher(surname);
        Matcher matcherPhone = pattern.matcher(phoneNumber);
        Matcher matcherGender = pattern.matcher(name);
        return matcherGender.find() || matcherName.find()
                || matcherPhone.find() || matcherSurname.find();
    }
}

class Organization extends Contact {
    String name;
    String address;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        String name = "Organization name: " + this.name;
        String address = "Address: " + this.address;
        String number = "Number: " + (!hasNumber() ? "[no data]" : getPhoneNumber());
        String timeCreated = "Time created: " + dateOfCreation.toString();
        String timeEdited = "Time last edit: " + dateOfEditing.toString();
        return name + "\n" +
                address + "\n" +
                number + "\n" +
                timeCreated + "\n" +
                timeEdited;
    }

    @Override
    public void edit() {
        LocalDateTime prevEdit = dateOfEditing;
        dateOfEditing = LocalDateTime.now();
        if (scanner == null) return;
        System.out.println("Select a field (name, address, number)");
        String field = scanner.nextLine();
        boolean isEdited = true;
        switch (field) {
            case "name":
                System.out.println("Enter " + field + ":");
                name = scanner.nextLine();
                break;
            case "address":
                System.out.println("Enter " + field + ":");
                address = scanner.nextLine();
                break;
            case "number":
                System.out.println("Enter " + field + ":");
                setPhoneNumber(scanner.nextLine());
                break;
            default:
                dateOfEditing = prevEdit;
                System.out.println("Wrong field");
                isEdited = false;
        }
        if (isEdited){
            System.out.println("The record updated!");
        }
    }

    @Override
    public void getInfo() {
        System.out.println(name);
    }

    @Override
    public boolean search(String string) {
        Pattern pattern = Pattern.compile(string, Pattern.CASE_INSENSITIVE);
        Matcher matcherPhone = pattern.matcher(phoneNumber);
        Matcher matcherAddress = pattern.matcher(address);
        Matcher matcherName = pattern.matcher(name);
        return matcherAddress.find() || matcherName.find() || matcherPhone.find();
    }
}

interface Edit {
    void edit();
}

interface Info {
    void getInfo();
}

class ContactFabric {
    static Contact create(String type, Scanner scanner) {
        if(type == null || scanner == null) return null;
        if (type.equals("person")){
            Person person = new Person();
            System.out.println("Enter the name:");
            person.setName(scanner.nextLine());
            System.out.println("Enter the surname:");
            person.setSurname(scanner.nextLine());
            System.out.println("Enter the birth date:");
            person.setBirthDate(scanner.nextLine());
            System.out.println("Enter the gender (M, F):");
            person.setGender(scanner.nextLine());
            System.out.println("Enter the number:");
            person.setPhoneNumber(scanner.nextLine());
            System.out.println("The record added.");
            return person;
        } else if (type.equals("organization")) {
            Organization organization = new Organization();
            System.out.println("Enter the organization name:");
            organization.setName(scanner.nextLine());
            System.out.println("Enter the address:");
            organization.setAddress(scanner.nextLine());
            System.out.println("Enter the number:");
            organization.setPhoneNumber(scanner.nextLine());
            System.out.println("The record added.");
            return organization;
        }
        return null;
    }
}

