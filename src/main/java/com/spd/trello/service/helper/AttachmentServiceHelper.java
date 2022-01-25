package com.spd.trello.service.helper;

import com.spd.trello.domain.Attachment;
import com.spd.trello.domain.Card;
import com.spd.trello.domain.Comment;
import com.spd.trello.repository.impl.helper.AttachmentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AttachmentServiceHelper {

    AttachmentHelper helper;

    @Autowired
    public AttachmentServiceHelper(AttachmentHelper helper) {
        this.helper = helper;
    }

    public Attachment create(Attachment attachment, Card card) {
        return helper.create(attachment, card);
    }

    public Attachment create(Attachment attachment, Comment comment) {
        return helper.create(attachment, comment);
    }

    public Attachment findById(UUID id) {
        return helper.findById(id);
    }

    public boolean delete(UUID id) {
        return helper.delete(id);
    }
}
