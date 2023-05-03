package com.bobocode.dao;

import com.bobocode.model.Photo;
import com.bobocode.model.PhotoComment;
import com.bobocode.util.ExerciseNotCompletedException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Please note that you should not use auto-commit mode for your implementation.
 */
public class PhotoDaoImpl implements PhotoDao {
    private EntityManagerFactory entityManagerFactory;

    public PhotoDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Photo photo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(photo);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().commit();
            throw new RuntimeException("Can't save a photo: " + photo.toString());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Photo findById(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Photo photo = entityManager.find(Photo.class, id);
            entityManager.getTransaction().commit();
            return photo;
        } catch (Exception e) {
            entityManager.getTransaction().commit();
            throw new RuntimeException("Can't find a photo by id: " + id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Photo> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            String query = "select p from Photo p";
            List<Photo> photos = entityManager.createQuery(query, Photo.class).getResultList();
            entityManager.getTransaction().commit();
            return photos;
        } catch (Exception e) {
            entityManager.getTransaction().commit();
            throw new RuntimeException("Can't find all photos");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void remove(Photo photo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.merge(photo));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().commit();
            throw new RuntimeException("Can't remove photo: " + photo.toString());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void addComment(long photoId, String comment) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Photo photo = entityManager.find(Photo.class, photoId);
            PhotoComment photoComment = new PhotoComment();
            photoComment.setText(comment);
            photoComment.setCreatedOn(LocalDateTime.now());
            photo.addComment(photoComment);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().commit();
            throw new RuntimeException("Can't add a comments to photo with id: " + photoId);
        } finally {
            entityManager.close();
        }
    }
}
