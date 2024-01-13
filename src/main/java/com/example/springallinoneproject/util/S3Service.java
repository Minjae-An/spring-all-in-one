package com.example.springallinoneproject.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")

    private String region;

    private String createFilename(String filename) {
        return UUID.randomUUID().toString().concat(getFileExtension(filename));
    }

    private String getFileExtension(String filename) {
        try {
            return filename.substring(filename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalStateException("잘못된 형식의 파일(" + filename + ") 입니다.");
        }
    }

    public GetS3Res uploadSingleFile(MultipartFile multipartFile) {
        String filename = createFilename(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalStateException("파일 업로드 실패");
        }

        return new GetS3Res(s3Client.getUrl(bucket, filename).toString(), filename);
    }

    public List<GetS3Res> uploadFiles(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .map(this::uploadSingleFile)
                .toList();
    }

    public void deleteFile(String filename) {
        s3Client.deleteObject(new DeleteObjectRequest(bucket, filename));
    }
}
