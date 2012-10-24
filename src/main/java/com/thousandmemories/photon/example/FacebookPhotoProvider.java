package com.thousandmemories.photon.example;

import com.thousandmemories.photon.core.PhotoProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FacebookPhotoProvider implements PhotoProvider {
    @Override
    public InputStream getPhotoInputStream(String path) throws IOException {
        return new URL("https://graph.facebook.com/" + path + "/picture?type=large").openStream();
    }
}
