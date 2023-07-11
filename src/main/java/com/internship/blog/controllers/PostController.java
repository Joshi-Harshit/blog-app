package com.internship.blog.controllers;

import com.internship.blog.config.AppConstants;
import com.internship.blog.payloads.ApiResponse;
import com.internship.blog.payloads.PostDto;
import com.internship.blog.services.FileService;
import com.internship.blog.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto,
                                              @PathVariable Integer userId,
                                              @PathVariable Integer categoryId){
        PostDto createdPost = this.postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity(createdPost, HttpStatus.CREATED);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,
                                              @PathVariable Integer postId){
        PostDto updatedPost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId){
        this.postService.deletePost(postId);
        return new ResponseEntity(new ApiResponse("Post deleted successfully", true), HttpStatus.OK);
    }


    @GetMapping("/posts")
    public ResponseEntity<PostDto> getAllPosts(@RequestParam(value="pageNo" , defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNo,
                                               @RequestParam(value="pageSize" , defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                               @RequestParam(value="sortBy" , defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                               @RequestParam(value="sortDirection" , defaultValue = AppConstants.SORT_DIR, required = false) String sortDirection){
        return new ResponseEntity(this.postService.getAllPosts(pageNo, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    // Get Post by post id
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
        return ResponseEntity.ok(this.postService.getPostById(postId));
    }


    // Gets all the posts corresponding to a user id
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<PostDto> searchByUser(@PathVariable Integer userId){
        return new ResponseEntity(this.postService.getPostsByUser(userId), HttpStatus.OK);
    }

    // Gets all the posts corresponding to a category id
    @GetMapping("category/{categoryId}/posts")
    public ResponseEntity<PostDto> searchByCategory(@PathVariable Integer categoryId){
        return new ResponseEntity(this.postService.getPostsByCategory(categoryId), HttpStatus.OK);
    }

    // Search for the posts by title
    @GetMapping("/posts/search/{keyword}")
    public ResponseEntity<PostDto> searchByTitle(@PathVariable String keyword){
        return new ResponseEntity(this.postService.searchPosts(keyword), HttpStatus.OK);
    }

    // Image Upload
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@PathVariable Integer postId,
                                                   @RequestParam("image")MultipartFile image) throws IOException {

        PostDto postDto = this.postService.getPostById(postId);

        String fileName = this.fileService.uploadImage(path, image);
        postDto.setImageName(fileName);
        PostDto updatedPost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity(updatedPost, HttpStatus.OK);
    }

    // Method to serve images
    @GetMapping(value="/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName,
                              HttpServletResponse response) throws IOException{
        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
