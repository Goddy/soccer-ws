package com.soccer.ws.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.google.common.collect.Maps;
import com.soccer.ws.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

/**
 * Created by u0090265 on 11/28/15.
 */
@Service
public class CloudinaryImageServiceImpl implements ImageService {
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryImageServiceImpl.class);
    @Value("${cloudinary.name}")
    private String name;
    @Value("${cloudinary.key}")
    private String apiKey;
    @Value("${cloudinary.secret}")
    private String apiSecret;
    private Cloudinary cloudinary;

    @PostConstruct
    public void initialize() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", name,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    @Override
    public String upload(MultipartFile file) throws Exception {
        return (String) cloudinary.uploader().uploadLarge(file.getInputStream(), Maps.newHashMap()).get("public_id");
    }

    @Override
    public Image uploadProfileImage(MultipartFile file) throws Exception {
        var options = ObjectUtils.asMap("folder", "profile/");
        Image image = new Image();
        var result = cloudinary.uploader().uploadLarge(file.getInputStream(), options);
        image.setImageId((String) result.get("public_id"));
        image.setImageUrl((String) result.get("secure_url"));
        return image;
    }

    @Override
    public String getImage(String imageId, String format, int width, int height) {
        return cloudinary.url().transformation(new Transformation().width(width).height(height)).format(format)
                .imageTag(imageId);
    }

    @Override
    public String getImage(String imageId, String format) {
        return cloudinary.url().format(format)
                .imageTag(imageId);
    }

    @Override
    public String getImage(String imageId) {
        return getImage(imageId, "png");
    }
}
