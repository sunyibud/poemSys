package com.poemSys.admin.bean.Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyFeedBackForm
{
    private static final long serialVersionUID = 1L;

    private long feedbackId;
    private String replyContent;
}
