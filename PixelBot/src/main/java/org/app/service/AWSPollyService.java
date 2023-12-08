package org.app.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;

import com.amazonaws.services.s3.model.PutObjectResult;
import org.app.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AWSPollyService {

    private AmazonPollyClient polly;
    @Autowired
    AWSS3Service awss3Service;
    private Voice voiceDefault;

    private static final String SAMPLE = "Congratulations. " +
            "You have successfully built this working demo of Amazon Polly in Java. " +
            "Have fun building voice enabled apps with Amazon Polly (that's me!)," +
            " and alwaylook at the AWS website for tips and tricks on using Amazon Polly " +
            "and other great services from AWS";

    public AWSPollyService() {
        AWSCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        // create an Amazon Polly client in a specific region
        polly =  new AmazonPollyClient(credentialsProvider);
        polly.setRegion(Region.getRegion(Regions.US_EAST_2));
        // Create describe voices request.
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

        // Synchronously ask Amazon Polly to describe available TTS voices.
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        voiceDefault = describeVoicesResult.getVoices().stream().filter(vo -> vo.getId().equals("Matthew")).toList().get(0);
    }

    public byte[] synthesize(String text, String filename, String voice, OutputFormat format) throws IOException {
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

        // Synchronously ask Amazon Polly to describe available TTS voices.
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        List<Voice> voiceMatches = describeVoicesResult.getVoices().stream().filter(vo -> vo.getId().equals(voice)).toList();
        Voice voiceFound = voiceMatches.isEmpty()?voiceDefault: voiceMatches.get(0);
        SynthesizeSpeechRequest synthReq =
                new SynthesizeSpeechRequest().withText(text).withVoiceId(voiceFound.getId())
                        .withOutputFormat(format);
        SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);
        InputStream audioMade = synthRes.getAudioStream();

        //make file name
        File file = new File(filename);
        FileUtil.copyInputStreamToFile(audioMade, file);

        //save audio to s3 bucket
        awss3Service.saveAudioToS3(file);


        //turn file to bytes
        byte[] fileByte = Files.readAllBytes(file.toPath());


        return fileByte;
    }


}

