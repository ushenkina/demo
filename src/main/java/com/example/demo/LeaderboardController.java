package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.Set;
import org.springframework.data.redis.core.ZSetOperations;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @PostMapping("/add")
    public void addPlayer(@RequestParam String name, @RequestParam double score) {
        leaderboardService.addPlayer(name, score);
    }

    @GetMapping("/top")
    public Set<ZSetOperations.TypedTuple<String>> getTop() {
        return leaderboardService.getTopPlayers();
    }
}