package com.totophoto.Image;

/**
 * Created by Thomas on 05/02/2018.
 */

public class Favorite extends Image{
    private int id;

    /**
     *
     * @param _name The name of the image
     * @param _link The link of the image
     */
    public Favorite(String _name, String _link) {
        super(_name, _link);
        id = 0;
    }

    /**
     *
     * @param _name The name of the image
     * @param _link The link of the image
     * @param _id The id of the image
     */
    public Favorite(String _name, String _link, int _id) {
        super(_name, _link);
        id = _id;
    }

    /**
     *
     * @return A int who contain the id of the image
     */
    public int getId(){
        return id;
    }

    public void setName(String _name) {
        name = _name;
    }

}
