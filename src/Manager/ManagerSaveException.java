package Manager;

import java.io.IOException;

public class ManagerSaveException extends Exception {
    public ManagerSaveException(String message, IOException e) {
        super(message, e);
    }
}
