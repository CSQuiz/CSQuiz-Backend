package backend.csquiz.controller;

import backend.csquiz.dto.request.CheckAnswerRequestDTO;
import backend.csquiz.dto.request.StartGameRequestDTO;
import backend.csquiz.dto.response.CheckAnswerResponseDTO;
import backend.csquiz.dto.response.GameFinishResponseDTO;
import backend.csquiz.dto.response.GameStartResponseDTO;
import backend.csquiz.dto.response.QuestionResponseDTO;
import backend.csquiz.entity.Question;
import backend.csquiz.service.GameService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 게임 시작
    @PostMapping("/start")
    public GameStartResponseDTO startGame(@RequestBody StartGameRequestDTO request) {
        String gameId = gameService.startGame(
                request.getNickname(),
                request.getQuestionCount(),
                request.getDifficulty()
        );
        return new GameStartResponseDTO(gameId);
    }

    // 게임의 문제 조회
    @GetMapping("/{gameId}/questions")
    public List<QuestionResponseDTO> getQuestionByGame(@PathVariable String gameId) {
        return gameService.getQuestionsByGame(gameId);
    }

    // 정답 확인
    @PostMapping("/{gameId}/answer")
    public CheckAnswerResponseDTO checkAnswer(@PathVariable String gameId, @RequestBody CheckAnswerRequestDTO request) {
        boolean isCorrect = gameService.checkAnswer(gameId, request.getQuestionId(), request.getAnswer());
        return new CheckAnswerResponseDTO(isCorrect);
    }

    // 게임 종료
    @PostMapping("/{gameId}/end")
    public GameFinishResponseDTO finishGame(@PathVariable String gameId) {
        int finalScore = gameService.finishGame(gameId); // 최종 점수 가져오기
        return new GameFinishResponseDTO("게임이 끝났습니다.", finalScore);
    }

    // 다음 문제로 넘어가기
    @GetMapping("/{gameId}/next")
    public QuestionResponseDTO getNextQuestion(@PathVariable String gameId) {
        return gameService.getNextQuestion(gameId);
    }
}