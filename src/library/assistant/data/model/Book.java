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
    private double price;

    public Book(String title, String author, String publisher, Boolean isAvail, double price) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isAvail = isAvail;
        this.price = price;
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

    public String getPrice() {
        return price+"$";
    }
    
    public double getBookPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    
    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", author=" + author + ", publisher=" + publisher + ", isAvail=" + isAvail + '}';
    }

    
    
    
}
