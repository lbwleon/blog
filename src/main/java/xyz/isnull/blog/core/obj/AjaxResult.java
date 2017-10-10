package xyz.isnull.blog.core.obj;

import lombok.Data;

@Data
public class AjaxResult {

    private boolean success = true;

    private String message;

    private Object data;

    public AjaxResult(Object data) {
        this(true, null, data);
    }

    public AjaxResult(boolean success, String message) {
        this(success, message, null);
    }

    public AjaxResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
