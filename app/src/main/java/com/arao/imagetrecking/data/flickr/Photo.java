package com.arao.imagetrecking.data.flickr;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Element(name = "photo")
class Photo {

    @Attribute(name = "id")
    private String id;
    @Attribute(name = "owner")
    private String owner;
    @Attribute(name = "secret")
    private String secret;
    @Attribute(name = "server")
    private int server;
    @Attribute(name = "farm")
    private int farm;
    @Attribute(name = "title")
    private String title;
    @Attribute(name = "ispublic")
    private int isPublic;
    @Attribute(name = "isfriend")
    private int isFriend;
    @Attribute(name = "isfamily")
    private int isFamily;

    String getId() {
        return id;
    }

    String getSecret() {
        return secret;
    }

    int getServer() {
        return server;
    }

    int getFarm() {
        return farm;
    }

}
