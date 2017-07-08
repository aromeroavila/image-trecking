package com.arao.imagetrecking.data.flickr;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rsp")
class FlickrResponse {

    @Attribute(name = "stat")
    private String stat;

    @ElementList(name = "photos")
    private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }
}
