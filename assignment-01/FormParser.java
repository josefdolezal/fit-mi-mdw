import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FormParser {
    public static void main(String[] args) {
        FormParser parser = new FormParser();

        String input = args[0];
        
        System.out.print(parser.parseForm(input));
    }
    
    private String parseForm(String form) {
        String bookingPattern =
            "^===\n" +
            "^Trip id: \"(.*)\"\\n" +
            "^Location: \"(.*)\"\\n" +
            "^Person: \"(.*) (.*)\"\\n" +
            "^===\n";
        
        Pattern regex = Pattern.compile(bookingPattern, Pattern.MULTILINE | Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = regex.matcher(form);
        
        matcher.find();
        
        String id = matcher.group(1);
        String location = matcher.group(2);
        String name = matcher.group(3);
        String surname = matcher.group(4);

        Person person = new Person(name, surname);
        Booking booking = new Booking(id, location, person);
        
        return booking.toString();
    }
}
