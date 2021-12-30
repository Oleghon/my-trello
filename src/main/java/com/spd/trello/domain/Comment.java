package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends Resource{
    private String text;
    private List<Attachment> attachments;
}
