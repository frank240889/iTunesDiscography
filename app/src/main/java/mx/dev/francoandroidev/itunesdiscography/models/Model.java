package mx.dev.francoandroidev.itunesdiscography.models;

/**
 * Created by franco on 13/09/17.
 * This abstract class was created
 * because in this way we can create unique
 * type of list of Model objects, then reuse the adapter
 * and cast every extended Model to specific one
 *
 */

public abstract class Model {
    public static final int TYPE_ADAPTER_CATEGORY = 1;
    public static final int TYPE_ADAPTER_ARTIST = 2;
    public static final int TYPE_ADAPTER_DISCOGRAPHY = 3;

    public abstract Model setType(int model);
    public abstract int getType();

    public abstract Model setId(long id);
    public abstract long getId();
}
