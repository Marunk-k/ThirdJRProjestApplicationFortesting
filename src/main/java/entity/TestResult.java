package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestResult {
    private UUID testId;
    private String testName;
    private String testTopic;
    private int totalQuestions;
    private int correctAnswers;
    private int score;
}