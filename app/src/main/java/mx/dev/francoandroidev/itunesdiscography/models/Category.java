package mx.dev.francoandroidev.itunesdiscography.models;

/**
 * Created by franco on 13/09/17.
 */

public class Category extends Model{
    private String genre = "";
    private String urlImage = "";
    private long id = -1;

    //Option for construct objects
    //of two ways
    public Category(){

    }

    public Category(String genre, String urlImage, long id){
        this.genre = genre;
        this.urlImage = urlImage;
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public String getUrlImage() {
        return urlImage;
    }

    //returning "this" from setters
    //allow us to chain the methods calls;
    public Category setGenre(String genre) {
        this.genre = genre;
        return this;
    }


    public Category setUrlImage(String urlImage) {
        this.urlImage = urlImage;
        return this;
    }


    public long getId() {
        return id;
    }

    public Category setId(long id) {
        this.id = id;
        return this;
    }

}
