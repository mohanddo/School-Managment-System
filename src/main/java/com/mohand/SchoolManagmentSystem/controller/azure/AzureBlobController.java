package com.mohand.SchoolManagmentSystem.controller.azure;

import com.mohand.SchoolManagmentSystem.service.azure.AzureBlobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/blob")
@RequiredArgsConstructor
public class AzureBlobController {

    private final AzureBlobService azureBlobService;
    
    @GetMapping("/sas")
    public String getSignedUrlForTeacher(@RequestParam Long teacherId, @RequestParam Long courseId, @RequestParam Long chapterId, @RequestParam String resourceName) {
        String blobName = teacherId + "/" + courseId + "/" +
                chapterId + "/" + resourceName;
        return azureBlobService.generateSASToken(blobName);
    }
}

