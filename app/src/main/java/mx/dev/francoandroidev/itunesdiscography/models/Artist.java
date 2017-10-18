package mx.dev.francoandroidev.itunesdiscography.models;

/**
 * Created by franco on 13/09/17.
 * Model for artist objects from API
 */

public final class Artist extends Model {
    private long id = -1;
    private String artistName = "";
    private String albumName = "";
    private String pricePerDisc = "";
    private String pricePerTrack = "";
    private String country = "";
    private String currency = "";
    private int type = -1;

    public Artist(){

    }

    public long getId() {
        return id;
    }

    public Artist setId(long id) {
        this.id = id;
        return this;
    }

    public Artist setType(int type){
        this.type = type;
        return this;
    }

    public int getType(){
        return this.type;
    }
    public String getArtistName() {
        return artistName;
    }

    public Artist setArtistName(String artistName) {
        this.artistName = artistName;
        return this;
    }

    public String getAlbumName() {
        return albumName;
    }

    public Artist setAlbumName(String albumName) {
        this.albumName = albumName;
        return this;
    }

    public String getPricePerDisc() {
        return pricePerDisc;
    }

    public Artist setPricePerDisc(String pricePerDisc) {
        this.pricePerDisc = pricePerDisc;
        return this;
    }

    public String getPricePerTrack() {
        return pricePerTrack;
    }

    public Artist setPricePerTrack(String pricePerTrack) {
        this.pricePerTrack = pricePerTrack;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Artist setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Artist setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

}
