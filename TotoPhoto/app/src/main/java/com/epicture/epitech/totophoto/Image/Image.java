package com.totophoto.Image;

public class Image {
    protected String name;
    protected String link;

    /**
     *
     * @param _name The name of the image
     * @param _link The link of the image
     */
    public Image(String _name, String _link) {
        name = _name;
        link = _link;

        name = name.replace("\"", "'");

        link = link.replace("\"", "'");
        if (link.contains(".mp4"))
            name = "(MP4) " + name;
        link = link.replace(".mp4", ".gif");
    }
    /**
     *
     * @return A String who contain the name of the image
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return A String who contain the link of the image
     */
    public String getLink(){
        return link;
    }
}
