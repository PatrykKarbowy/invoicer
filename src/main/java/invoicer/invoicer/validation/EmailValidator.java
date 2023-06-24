package invoicer.invoicer.validation;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
        String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(s);
        boolean result = matcher.matches();
        return result;
    }
}
