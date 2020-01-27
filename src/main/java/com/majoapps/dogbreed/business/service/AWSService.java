package com.majoapps.dogbreed.business.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import java.net.URL;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Data
@Service
public class AWSService {

    private final AmazonS3 s3client;

    @Value("${aws.bucketname}")
    private String bucketName;

    @Autowired
    public AWSService(AmazonS3 s3client){
        this.s3client = s3client;
    }

    public URL addFile(String fileName, String fileLocation) {
        try {
            s3client.putObject( bucketName, fileName, fileLocation);
            return s3client.getUrl(bucketName, fileName);
        } catch(AmazonClientException e) {
            throw new RuntimeException("AWS upload error " + e.getLocalizedMessage());
        }
    }

    public void deleteFile(String fileName) {
        try {
            s3client.deleteObject(bucketName, fileName);
        } catch(AmazonClientException e) {
            throw new RuntimeException("AWS delete error " + e.getLocalizedMessage());
        }
    }

}
