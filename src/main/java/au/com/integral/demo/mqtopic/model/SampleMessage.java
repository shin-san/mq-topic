package au.com.integral.demo.mqtopic.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SampleMessage {

    String title;
    String body;

    @Override
    public String toString() {
        return "SampleMessage{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
