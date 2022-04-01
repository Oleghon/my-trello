package com.spd.trello.service.attachment_helper;

import com.spd.trello.domain.items.Attachment;
import com.spd.trello.repository_jpa.AttachmentRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component(value = "dbStorage")
public class AttachmentDBService extends AbstractAttachmentService{

    public AttachmentDBService(AttachmentRepository repository) {
        super(repository);
    }

    @Override
    public Attachment load(MultipartFile file) {
        Attachment attachment = convert(file);
        try {
            attachment.setFile(file.getBytes());
            return repository.save(attachment);
        } catch (IOException e) {
            throw new IllegalArgumentException("File not added to db", e);
        }
    }

}
