package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import org.aiwolf.common.data.Role;

import java.util.ArrayList;

/**
 * �����u�b�n�Ȃ��Ɠ����v�N���X
 */
public final class Learn_COAndDay extends AbstractGuessStrategy {

	AgentStatistics agentStatistics;

	public Learn_COAndDay(AgentStatistics agentStatistics){
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

			int day = args.agi.latestGameInfo.getDay();

			Role role = args.agi.getCORole(agentNo, day, 0);

			// �b�n�Ȃ��A�������͑��b�n���Ώ�
			if( args.agi.agentState[agentNo].comingOutRole == null || args.agi.agentState[agentNo].comingOutRole == Role.VILLAGER ){

				// TODO �Y���҂������������Ȃ�O������ɂ���

				// ���S���͍Ō�ɐ������Ă���������ɂ���
				if( args.agi.agentState[agentNo].deathDay != null ){
					day = args.agi.agentState[agentNo].deathDay - 1;
				}

				// 4���ڂ܂łɊۂ߂�
				day = Math.min(day, 4);

				ArrayList<Guess> rguesses = agentStatistics.statistics.get(agentNo).getGuessFromEvent("NotCO_" + day + "d", args.agi.gameSetting);

				for( Guess guess : rguesses ){
					guess.correlation = Math.pow( guess.correlation, 0.7 );
					guesses.add(guess);
				}

			}

		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
