package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.QuestionType;

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
    private List<Integer> rightAnswerIndexes;
    private QuestionType type;
}
