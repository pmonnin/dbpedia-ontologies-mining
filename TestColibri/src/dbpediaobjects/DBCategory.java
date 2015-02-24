package dbpediaobjects;

public class DBCategory {
    private String name = "";
    private String uri = "";

    public DBCategory(String name, String uri) {
        super();
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
