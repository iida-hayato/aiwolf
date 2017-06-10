package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import jp.iida.hayato.aiwolf.lib.Judge;
import org.aiwolf.common.data.Role;

import java.util.ArrayList;

/**
 * �����u�P���ړ��[�錾�v�N���X
 */
public final class Learn_1dCompetitionDevine extends AbstractGuessStrategy {

	AgentStatistics agentStatistics;

	public Learn_1dCompetitionDevine(AgentStatistics agentStatistics){
		this.agentStatistics = agentStatistics;
	}

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {
		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();

		// �����͍s��Ȃ�
		if( args.agi.latestGameInfo.getDay() < 1 ){
			return guesses;
		}

		// 1����0�����ł͍s��Ȃ�
		if( args.agi.latestGameInfo.getDay() == 1 && args.agi.getMyTalkNum() == 0 ){
			return guesses;
		}

		for( int seer : args.agi.getEnableCOAgentNo(Role.SEER) ){
			for( Judge judge : args.agi.getSeerJudgeList() ){
				if( judge.agentNo == seer ){
					if( args.agi.getCORole(judge.targetAgentNo, 1, 0) != null ){
						// ��������肢
						ArrayList<Guess> rguesses = agentStatistics.statistics.get(judge.agentNo).getGuessFromEvent("1dCompetitionDevine", args.agi.gameSetting);

						for( Guess guess : rguesses ){
							guess.correlation = Math.pow(guess.correlation, 0.6);
							guesses.add(guess);
						}
					}else{
						// �O���[��肢
						ArrayList<Guess> rguesses = agentStatistics.statistics.get(judge.agentNo).getGuessFromEvent("1dNotCompetitionDevine", args.agi.gameSetting);

						for( Guess guess : rguesses ){
							guess.correlation = Math.pow(guess.correlation, 0.6);
							guesses.add(guess);
						}
					}
					break;
				}
			}
		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
