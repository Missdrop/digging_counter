package mod.missdrop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Objects;

public class Digging_counter implements ModInitializer {

  public static Scoreboard Scoreboard;
  public static ScoreboardObjective ScoreboardObj;

  @Override
  public void onInitialize() {
    ServerLifecycleEvents.SERVER_STARTED.register(
        server -> {
          Scoreboard = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getScoreboard();
          ScoreboardObj = Scoreboard.getNullableObjective("digging_counter");
          if (ScoreboardObj == null) {
            ScoreboardObj =
                Scoreboard.addObjective(
                    "digging_counter",
                    ScoreboardCriterion.DUMMY,
                    Text.literal("挖掘榜"),
                    ScoreboardCriterion.RenderType.INTEGER,
                    true,
                    null);
          } else {
            ScoreboardObj.setDisplayName(Text.literal("挖掘榜"));
          }
          Scoreboard.setObjectiveSlot(ScoreboardDisplaySlot.SIDEBAR, ScoreboardObj);
        });
    PlayerBlockBreakEvents.AFTER.register(
        (world, player, pos, state, entity) -> {
          ScoreAccess access = Scoreboard.getOrCreateScore(player, ScoreboardObj);
          int score = access.getScore();
          score++;
          access.setScore(score);
        });
  }
}
