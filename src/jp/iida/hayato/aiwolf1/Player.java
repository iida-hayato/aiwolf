package jp.iida.hayato.aiwolf1;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import jp.iida.hayato.aiwolf.strategyplayer.StrategyBodyGuard;
import jp.iida.hayato.aiwolf.strategyplayer.StrategyMedium;
import jp.iida.hayato.aiwolf.strategyplayer.StrategyPossessed;
import jp.iida.hayato.aiwolf.strategyplayer.StrategySeer;
import jp.iida.hayato.aiwolf.strategyplayer.StrategyVillager;
import jp.iida.hayato.aiwolf.strategyplayer.StrategyWerewolf;
import org.aiwolf.client.base.player.AbstractRoleAssignPlayer;

public class Player extends AbstractRoleAssignPlayer {

  /**
   * エージェント毎の統計情報
   */
  protected AgentStatistics agentStatistics = new AgentStatistics();

  public Player() {
    setVillagerPlayer(new StrategyVillager(agentStatistics));
    setSeerPlayer(new StrategySeer(agentStatistics));
    setMediumPlayer(new StrategyMedium(agentStatistics));
    setBodyguardPlayer(new StrategyBodyGuard(agentStatistics));
    setPossessedPlayer(new StrategyPossessed(agentStatistics));
    setWerewolfPlayer(new StrategyWerewolf(agentStatistics));
  }

  @Override
  public String getName() {
    return "wolf1";
  }
}
