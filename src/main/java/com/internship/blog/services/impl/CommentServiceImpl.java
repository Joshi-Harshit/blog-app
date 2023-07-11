package com.internship.blog.services.impl;

import com.internship.blog.entities.Comment;
import com.internship.blog.entities.Post;
import com.internship.blog.exceptions.ResourceNotFoundException;
import com.internship.blog.payloads.CommentDto;
import com.internship.blog.repositories.CommentRepo;
import com.internship.blog.repositories.PostRepo;
import com.internship.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {

        Post post = this.postRepo.findById(postId).
                orElseThrow(()->new ResourceNotFoundException("Post","PostId",postId.toString()));

        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);
        Comment savedComment = this.commentRepo.save(comment);
        return this.modelMapper.map(savedComment, CommentDto.class);

    }

    @Override
    public void deleteComment(Integer commentId) {

        Comment comment = this.commentRepo.findById(commentId).
                orElseThrow(()->new ResourceNotFoundException("Comment","CommentId",commentId.toString()));

        this.commentRepo.delete(comment);

    }
}
