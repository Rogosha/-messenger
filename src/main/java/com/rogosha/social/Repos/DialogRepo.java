package com.rogosha.social.Repos;

import com.rogosha.social.Entities.Dialog;
import com.rogosha.social.Entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DialogRepo extends CrudRepository<Dialog, Long> {
    @Query("SELECT d FROM Dialog d WHERE (d.user1 = :user1 AND d.user2 = :user2) OR (d.user1 = :user2 AND d.user2 = :user1)")
    Optional<Dialog> findByUsers(@Param("user1") User user1, @Param("user2") User user2);
    List<Dialog> findByUser1OrUser2(User user1, User user2);
    Optional<Dialog> findByUser1AndUser2(User user1, User user2);
}
