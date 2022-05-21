package com.poemSys.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FatherComment implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;
    private long ownerUserId;
    private String content;
    private LocalDateTime createdTime;
    private boolean isOwner;
    private List<SonComment> sonComments;
}
