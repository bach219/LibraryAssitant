package library.assistant.data.model;

/**
 *
 * @author afsal
 */
public class Book {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private Boolean isAvail;

    public Book(String id, String title, String author, String publisher, Boolean isAvail) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isAvail = isAvail;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Boolean getAvailability() {
        return isAvail;
    }
    
    public String getIsAvail() {
        return isAvail ? "Available" : "Issued";
    }

    public void setIsAvail(Boolean isAvail) {
        this.isAvail = isAvail;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", author=" + author + ", publisher=" + publisher + ", isAvail=" + isAvail + '}';
    }

    
    
    
}
