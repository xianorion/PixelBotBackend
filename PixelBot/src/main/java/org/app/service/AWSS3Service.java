package org.app.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.juli.FileHandler.DEFAULT_BUFFER_SIZE;

@Service
public class AWSS3Service {

    private final String bucketName="pixelbotvoicebucket";

    private AmazonS3 s3client;

    public AWSS3Service(){
        AWSCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

         s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentialsProvider.getCredentials()))
                .withRegion(Regions.US_EAST_2)
                .build();
    }

    public PutObjectResult saveAudioToS3(File file){
        PutObjectResult pr = null;


        PutObjectRequest putObjectRequest =  new PutObjectRequest(bucketName, file.getName(), file);
        pr = s3client.putObject(putObjectRequest);


        return pr;

    }

}
