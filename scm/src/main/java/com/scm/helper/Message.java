package com.scm.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String content;
    //use enum MessageType for default blue
    @Builder.Default
    private MessageType type=MessageType.blue;
}
