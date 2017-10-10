package xyz.isnull.blog.core.obj;

public enum MetaType{

    TAGS("tags", 1),
    CATEGORY("category",2),
    LINKS("links", 3);

    private String name;
    private Integer index;

    private MetaType(String name, Integer index){
        this.index = index;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
