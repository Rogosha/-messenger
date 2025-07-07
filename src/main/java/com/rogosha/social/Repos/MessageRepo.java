package com.rogosha.social.Repos;

import com.rogosha.social.Entities.Dialog;
import com.rogosha.social.Entities.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {
    List<Message> findByDialog (Dialog dialog);
}
