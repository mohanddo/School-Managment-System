package com.mohand.SchoolManagmentSystem.service.azure;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.options.BlobContainerCreateOptions;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.common.sas.SasProtocol;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

@Service
public class AzureBlobService {
    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Value("${azure.storage.endpoint}")
    private String storageEndpoint;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationTime;

    public String generateSASTokenForContainer(String containerName) {

        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);

        // Build the Blob Service Client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(storageEndpoint)
                .credential(credential)
                .buildClient();

        System.out.println(containerName);

        // Get the container client
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        BlobSasPermission permissions = new BlobSasPermission()
                .setReadPermission(true)
                .setWritePermission(true)
                .setDeletePermission(true);

        // Set expiry time for the SAS token
        OffsetDateTime expiryTime = OffsetDateTime.now().plusSeconds(jwtExpirationTime / 1000);

        // Generate the SAS token
        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, permissions)
                .setProtocol(SasProtocol.HTTPS_ONLY)
                .setStartTime(OffsetDateTime.now());

        String encodedSasToken = containerClient.generateSas(sasValues);
        return URLDecoder.decode(encodedSasToken, StandardCharsets.UTF_8);
    }

    public String signBlobUrl(String blobUrl, boolean isAuthenticated) {

        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);

        BlobClient blobClient = new BlobClientBuilder()
                .endpoint(blobUrl)
                .credential(credential)
                .buildClient();

        BlobSasPermission permissions = new BlobSasPermission()
                .setReadPermission(true);

        // Set expiry time for the SAS token
        OffsetDateTime expiryTime = isAuthenticated
                ? OffsetDateTime.now().plusSeconds(jwtExpirationTime / 1000)
                : OffsetDateTime.now().plusDays(1);


        // Generate the SAS token
        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, permissions)
                .setProtocol(SasProtocol.HTTPS_ONLY)
                .setStartTime(OffsetDateTime.now());

        String encodedSasToken = blobClient.generateSas(sasValues);
        return blobUrl + "?" + URLDecoder.decode(encodedSasToken, StandardCharsets.UTF_8);
    }


    public void createContainer(String containerName) {
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);

        // Build the Blob Service Client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(storageEndpoint)
                .credential(credential)
                .buildClient();

        // Get the container client
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        try {
            containerClient.create();
        } catch (BlobStorageException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

