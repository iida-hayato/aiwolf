package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.condition.AbstractCondition;
import jp.iida.hayato.aiwolf.condition.RoleCondition;
import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import jp.iida.hayato.aiwolf.lib.CauseOfDeath;
import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * �����u�[�d�ˁv�N���X
 */
public final class Learn_VoteStack extends AbstractGuessStrategy {

	AgentStatistics agentStatistics;

	public Learn_VoteStack(AgentStatistics agentStatistics){
		this.agentStatistics = agentStatistics;
	}

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {
		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();

		GameInfo gameInfo = args.agi.latestGameInfo;

		// 5���ڂ��琄�����s��
		if( gameInfo.getDay() < 5 ){
			return guesses;
		}

		HashMap<Integer,Integer> voteCount = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> maxVoteCount = new HashMap<Integer,Integer>();

		// ���Q�[���̃J�E���g
		for( int day = 2; day < gameInfo.getDay(); day++ ){
			VoteAnalyzer vaResult = new VoteAnalyzer(args.agi.getVoteList(day-1));
			VoteAnalyzer vaSaid = VoteAnalyzer.loadSaidVote(args.agi, day-1);

			for( Agent agent : gameInfo.getAgentList() ){
				Agent target = vaResult.getVoteTarget(agent);
				if( target != null ){
					voteCount.put( agent.getAgentIdx(), voteCount.getOrDefault(agent.getAgentIdx(), 0) + 1 );
					if( vaSaid.getMaxReceiveVoteAgent().contains(target) ){
						maxVoteCount.put( agent.getAgentIdx(), voteCount.getOrDefault(agent.getAgentIdx(), 0) + 1 );
					}
				}
			}
		}


		for( int agentNo = 1; agentNo <= args.agi.gameSetting.getPlayerNum(); agentNo++ ){

			// �S��ȏ㓊�[���Ă��Ȃ��ꍇ�͏��s���Ƃ��ď������Ȃ�
			if( args.agi.agentState[agentNo].causeofDeath != CauseOfDeath.ALIVE && args.agi.agentState[agentNo].deathDay < 5 ){
				continue;
			}


			int vilVoteCount = 0;
			int vilMaxVoteCount = 0;
			for( Role role : Role.values() ){
				if( role.getTeam() == Team.VILLAGER ){
					vilVoteCount += agentStatistics.statistics.get(agentNo).eventCount.get(role).getOrDefault("Vote", 0);
					vilMaxVoteCount += agentStatistics.statistics.get(agentNo).eventCount.get(role).getOrDefault("VoteToMostVote", 0);
				}
			}

			int wolfVoteCount = agentStatistics.statistics.get(agentNo).eventCount.get(Role.WEREWOLF).getOrDefault("Vote", 0);
			int wolfMaxVoteCount = agentStatistics.statistics.get(agentNo).eventCount.get(Role.WEREWOLF).getOrDefault("VoteToMostVote", 0);

			int posVoteCount = agentStatistics.statistics.get(agentNo).eventCount.get(Role.POSSESSED).getOrDefault("Vote", 0);
			int posMaxVoteCount = agentStatistics.statistics.get(agentNo).eventCount.get(Role.POSSESSED).getOrDefault("VoteToMostVote", 0);


			double measuredMaxVoteRate = (double)maxVoteCount.getOrDefault(agentNo, 0) / voteCount.getOrDefault(agentNo, 0);
			double vilMaxVoteRate = (double)vilMaxVoteCount / vilVoteCount;
			double wolfMaxVoteRate = (double)wolfMaxVoteCount / wolfVoteCount;
			double posMaxVoteRate = (double)posMaxVoteCount / posVoteCount;

			double vilDistance = Math.abs(vilMaxVoteRate - measuredMaxVoteRate);
			double wolfDistance = Math.abs(wolfMaxVoteRate - measuredMaxVoteRate);
			double posDistance = Math.abs(posMaxVoteRate - measuredMaxVoteRate);


			AbstractCondition agentWolf = RoleCondition.getRoleCondition( agentNo, Role.WEREWOLF );
			AbstractCondition agentPossessed = RoleCondition.getRoleCondition( agentNo, Role.POSSESSED );

			Guess guess;

			if(agentStatistics.statistics.get(agentNo).roleCount.getOrDefault(Role.WEREWOLF, 0) >= 6 &&
			   agentStatistics.statistics.get(agentNo).roleCount.getOrDefault(Role.POSSESSED, 0) >= 2){
				guess = new Guess();
				guess.condition = agentWolf;
				guess.correlation = Math.pow(Math.max(1.0 + vilDistance - wolfDistance, 0.1), 0.5);
				guesses.add(guess);

				guess = new Guess();
				guess.condition = agentPossessed;
				guess.correlation = Math.pow(Math.max(1.0 + vilDistance - posDistance, 0.1), 0.5);
				guesses.add(guess);
			}

		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
