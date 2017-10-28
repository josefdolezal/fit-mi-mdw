public class Booking {
    public String id;
    public String location;
    public Person person;
  
    public Booking(String id, String location, Person person) {
        this.id = id;
        this.location = location;
        this.person = person;
    }
    
    public String toString() {
        return "{\n" +
        "  id: \"" + id + "\",\n" +
        "  location: \"" + location + "\",\n" +
        "  person: {\n" +
        "    name: \"" + person.name + "\",\n" +
        "    surname: \"" + person.surname + "\",\n" +
        "  }\n" +
        "}\n";
    }
}
