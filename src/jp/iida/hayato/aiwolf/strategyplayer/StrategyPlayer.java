package jp.iida.hayato.aiwolf.strategyplayer;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import org.aiwolf.client.base.player.AbstractRoleAssignPlayer;

public class StrategyPlayer extends AbstractRoleAssignPlayer {

	/** エージェント毎の統計情報 */
	protected AgentStatistics agentStatistics = new AgentStatistics();

	public StrategyPlayer() {
		setVillagerPlayer(new StrategyVillager(agentStatistics));
		setSeerPlayer(new StrategySeer(agentStatistics));
		setMediumPlayer(new StrategyMedium(agentStatistics));
		setBodyguardPlayer(new StrategyBodyGuard(agentStatistics));
		setPossessedPlayer(new StrategyPossessed(agentStatistics));
		setWerewolfPlayer(new StrategyWerewolf(agentStatistics));
	}


	@Override
	public String getName() {
//		return StrategyPlayer.class.getSimpleName();
		return "Udon";
	}


}
