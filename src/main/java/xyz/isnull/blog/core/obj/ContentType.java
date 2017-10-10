package xyz.isnull.blog.core.obj;

public enum ContentType {
    MD("markdown"),HTML("html");

    private String name;

    ContentType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
