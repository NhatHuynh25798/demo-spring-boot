package com.demo.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class FileUtils {
    public static final String IMAGE_FOLDER = "src/main/resources/static/images";
    Path root;

    // Upload file image to folder "image"
    public FileUtils() {
        this.root = new File(IMAGE_FOLDER).getAbsoluteFile().toPath();
        if (!Files.exists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Upload file image to children folder in folder "image"
    public FileUtils(String folder) {
        this.root = new File(IMAGE_FOLDER + "/" + folder).getAbsoluteFile().toPath();
        if (!Files.exists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Upload file image to another folder
    public FileUtils(Path root) {
        this.root = root;
        if (!Files.exists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public String randomFileName() {
//        String SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder salt = new StringBuilder();
//        Random rnd = new Random();
//        while (salt.length() < 18) { // length of the random string.
//            int index = (int) (rnd.nextFloat() * SALT_CHARS.length());
//            salt.append(SALT_CHARS.charAt(index));
//        }
//        return salt.toString();
//    }

    public String save(MultipartFile file, boolean isOverwrite) {
        try {
            Path path = this.root.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            File fileIsExists = new File(path.toString());
            if (fileIsExists.exists()) {
                if (isOverwrite) {
                    Files.delete(path);
                } else {
                    throw new RuntimeException("file is exists, overwrite?");
                }
            }
            Files.copy(file.getInputStream(), path);
            return Objects.requireNonNull(file.getOriginalFilename());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ArrayList<String> saveMultiFile(MultipartFile[] files, boolean isOverwrite) {
        ArrayList<String> result = new ArrayList<>();
        for (MultipartFile file : files) {
            result.add(save(file, isOverwrite));
        }
        return result;
    }

    public void delete(String filename) {
        try {
            Path file = this.root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                Files.delete(this.root.resolve(file));
            } else {
                throw new RuntimeException("Could not delete the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteMultiFile(String[] filenames) {
        for (String filename : filenames) {
            delete(filename);
        }
    }

    public Resource load(String filename) {
        try {
            Path file = this.root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}