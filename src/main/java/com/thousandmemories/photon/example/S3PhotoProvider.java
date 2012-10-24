package com.thousandmemories.photon.example;

import com.thousandmemories.photon.core.PhotoProvider;

import com.google.common.io.Resources;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.AmazonS3Exception;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.InputStream;

public class S3PhotoProvider implements PhotoProvider {
    private final AmazonS3 client;

    public S3PhotoProvider() throws IOException {
        this.client = new AmazonS3Client(new PropertiesCredentials(
                    Resources.newInputStreamSupplier(Resources.getResource("AwsCredentials.properties")).getInput()));
    }

    @Override
    public InputStream getPhotoInputStream(String path) throws IOException {
        String[] components = path.split("/");

        // Pull out the bucket name (everything before the first /)
        String bucketName = null;
        if (components.length == 1) {
            throw new WebApplicationException(404);
        } else {
            bucketName = components[0];
        }

        // Try to read the object (anything after the first slash,
        // i.e. bucket name + 1 character)
        S3Object object = null;
        try {
            object = this.client.getObject(new GetObjectRequest(bucketName, path.substring(bucketName.length() + 1)));
        } catch (AmazonS3Exception e) {
            throw new WebApplicationException(404);
        }

        return object.getObjectContent();
    }
}
