package ru.otus.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Picture;
import ru.otus.spring.models.StephenKingArt;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.PictureRepository;

@Service
@Slf4j
public class StoringServiceImpl implements StoringService {

    private final BookRepository bookRepository;
    private final PictureRepository pictureRepository;

    public StoringServiceImpl(BookRepository bookRepository, PictureRepository pictureRepository) {
        this.bookRepository = bookRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    @Transactional
    public void storeArt(StephenKingArt art) {
        log.info("Storing Stephen King art " + art);
         if (art != null) {
             if (art instanceof Book) {
                 bookRepository.save((Book) art);
             } else {
                 pictureRepository.save((Picture) art);
             }
         }
    }
}
