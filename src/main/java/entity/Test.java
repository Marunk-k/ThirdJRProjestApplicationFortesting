package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    private UUID id;
    private UUID createdBy;
    private String name;
    private String topic;
    private List<TestQuestion> questions;
    private int timeLimitMinutes;
}
