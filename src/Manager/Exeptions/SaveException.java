package Manager.Exeptions;

import java.io.IOException;

public class SaveException extends RuntimeException {
    public SaveException(String message, IOException e) {
        super(message, e);
    }
}
