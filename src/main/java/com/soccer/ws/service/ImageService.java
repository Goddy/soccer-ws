package com.soccer.ws.service;

import com.soccer.ws.model.Image;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by u0090265 on 11/28/15.
 */
public interface ImageService {

    String upload(MultipartFile file) throws Exception;

    Image uploadProfileImage(MultipartFile file) throws Exception;

    String getImage(String imageId, String format, int width, int height);

    String getImage(String imageId, String format);

    String getImage(String imageId);
}
