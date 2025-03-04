package com.mohand.SchoolManagmentSystem.service.azure;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.common.sas.SasProtocol;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
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

    public String generateSASToken(String blobName) {

        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);

        // Build the Blob Service Client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(storageEndpoint)
                .credential(credential)
                .buildClient();

        // Get the container client
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);


        // Get the blob client
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        BlobSasPermission permissions = new BlobSasPermission()
                .setReadPermission(true)
                .setWritePermission(true);

        // Set expiry time for the SAS token
        OffsetDateTime expiryTime = OffsetDateTime.now().plusHours(1);

        // Generate the SAS token
        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, permissions)
                .setProtocol(SasProtocol.HTTPS_ONLY)
                .setStartTime(OffsetDateTime.now());



        return blobClient.generateSas(sasValues);
    }
}

