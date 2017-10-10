package xyz.isnull.blog.core.exception;

public class DataException extends RuntimeException{
    public int code;

    public static final int noPermission = -100;
    public static final int noID = -1;

    public DataException(int code){ this.code = code; }

    public DataException(int code, String message){
        super(message);
        this.code = code;
    }

    public DataException(int code, String message, Throwable casue){
        super(message, casue);
        this.code = code;
    }

    public DataException(int code, Throwable cause){
        super(cause);
        this.code = code;
    }
}
