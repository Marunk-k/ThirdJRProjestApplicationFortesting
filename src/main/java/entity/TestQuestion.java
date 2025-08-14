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
public class TestQuestion {
    private UUID id;
    private String description;
    private List<String> answers;
    private int rightAnswerIndex;
}
