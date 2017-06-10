package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;

import java.util.ArrayList;

/**
 * �����u�P���ړ��[�錾�v�N���X
 */
public final class Learn_1dVoteSaid extends AbstractGuessStrategy {

	AgentStatistics agentStatistics;

	public Learn_1dVoteSaid(AgentStatistics agentStatistics){
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

		for( int agentNo = 1; agentNo <= args.agi.gameSetting.getPlayerNum(); agentNo++ ){

			Integer target = args.agi.getSaidVoteAgent(agentNo, 1);

			// ���錾�̏ꍇ
			if( target == null ){

				ArrayList<Guess> rguesses = agentStatistics.statistics.get(agentNo).getGuessFromEvent("NoSaid1dVote", args.agi.gameSetting);

				for( Guess guess : rguesses ){
					guesses.add(guess);
				}

			}else{

				ArrayList<Guess> rguesses = agentStatistics.statistics.get(agentNo).getGuessFromEvent("Said1dVote", args.agi.gameSetting);

				for( Guess guess : rguesses ){
					guesses.add(guess);
				}

			}

		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
