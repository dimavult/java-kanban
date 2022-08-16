package exception;

/*
Унаследовал от этой ошибки из-за того, что так выбрасывание ее в методе позволяет не менять сигнатуру.
Если честно, не понял, почему так сработало
 */
public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }
}
