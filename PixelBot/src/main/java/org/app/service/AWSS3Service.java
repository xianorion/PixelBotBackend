package org.app.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AWSS3Service {

    @Value("${s3.bucketname}")
    private String bucketName;

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
        //pr = s3client.putObject(putObjectRequest);


        return pr;

    }

}
