package com.Blog.Blog.Service;

import com.Blog.Blog.Entity.Comment;
import com.Blog.Blog.Entity.Post;
import com.Blog.Blog.Repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment saveComment(String comment){
       Comment com=new Comment();
       com.setMessage(comment);

        return commentRepository.save(com);
    }


    public List<Comment> getAllComments(){
        return  commentRepository.findAll();
    }


    public Integer deleteComment(Integer comId){
        Optional<Comment> commentTODelete =commentRepository.findById(comId);
        if(commentTODelete.isPresent()){
            Comment comment = commentTODelete.get();
            commentRepository.delete(comment);
            return comId;

        }else {
            throw new EntityNotFoundException
                    ("No such entity");
        }
    }
}




