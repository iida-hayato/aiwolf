package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;

import java.util.ArrayList;

/**
 * �����u���v�v�N���X
 */
public final class FromAgentStatistics extends AbstractGuessStrategy {

	AgentStatistics agentStatistics;

	public FromAgentStatistics(AgentStatistics agentStatistics){
		this.agentStatistics = agentStatistics;
	}

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {

		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();
		Guess guess;


//		for( Agent agent : args.agi.latestGameInfo.getAgentList() ){
//
//			AbstractCondition agentWolf = RoleCondition.getRoleCondition( agent, Role.WEREWOLF );
//			AbstractCondition agentPossessed = RoleCondition.getRoleCondition( agent, Role.POSSESSED );
//
//			int agentNo = agent.getAgentIdx();
//			Statistics statistics = agentStatistics.statistics.get(agentNo);
//			Role coRole = args.agi.agentState[agentNo].comingOutRole;
//
//			//TODO 1.0����ƂȂ�悤�A�l������l�����Čv�Z����
//			if( coRole != null && coRole != Role.VILLAGER ){
//
//				// �l���U�ꂷ���Ȃ��悤�ɂ��邽�߂̐������l
//				int padNum = 80;
//
//				double rate;
//
//				// �l�T���v
//				rate = getRate(statistics.wolfCOCount.get(coRole), statistics.wolfCount, padNum);
//
//				guess = new Guess();
//				guess.condition = agentWolf;
//				guess.correlation = rate;
//				guesses.add(guess);
//
//				// ���l���v
//				rate = getRate(statistics.posCOCount.get(coRole), statistics.posCount, padNum);
//
//				guess = new Guess();
//				guess.condition = agentPossessed;
//				guess.correlation = rate;
//				guesses.add(guess);
//
//			}else{
//
//				// �l���U�ꂷ���Ȃ��悤�ɂ��邽�߂̐������l
//				int padNum = 200;
//
//				double rate;
//
//				// �l�T���v
//				int count = statistics.wolfCount;
//				count -= statistics.wolfCOCount.get(Role.SEER);
//				count -= statistics.wolfCOCount.get(Role.MEDIUM);
//				count -= statistics.wolfCOCount.get(Role.BODYGUARD);
//				rate = getRate(count, statistics.wolfCount, padNum);
//
//				guess = new Guess();
//				guess.condition = agentWolf;
//				guess.correlation = rate;
//				guesses.add(guess);
//
//				// ���l���v
//				count = statistics.posCount;
//				count -= statistics.posCOCount.get(Role.SEER);
//				count -= statistics.posCOCount.get(Role.MEDIUM);
//				count -= statistics.posCOCount.get(Role.BODYGUARD);
//				rate = getRate(count, statistics.posCount, padNum);
//
//				guess = new Guess();
//				guess.condition = agentPossessed;
//				guess.correlation = rate;
//				guesses.add(guess);
//			}
//
//
//		}

		// �������X�g��Ԃ�
		return guesses;
	}



	private double getRate(double fakeCOCount, double allCount, int padCount){
		double rate;

		// �x��m��
		rate = (fakeCOCount + padCount) / (allCount + padCount);

		return rate;
	}


}
