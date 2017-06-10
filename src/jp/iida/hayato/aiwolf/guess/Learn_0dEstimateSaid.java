package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;

import java.util.ArrayList;

/**
 * �����u�P���ڐ����錾�v�N���X
 */
public final class Learn_0dEstimateSaid extends AbstractGuessStrategy {

	AgentStatistics agentStatistics;

	public Learn_0dEstimateSaid(AgentStatistics agentStatistics){
		this.agentStatistics = agentStatistics;
	}

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {
		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();


		// 0����2�����ڂ܂ł͍s��Ȃ�
		if( args.agi.latestGameInfo.getDay() == 0 && args.agi.getMyTalkNum() < 2 ){
			return guesses;
		}

		for( int agentNo = 1; agentNo <= args.agi.gameSetting.getPlayerNum(); agentNo++ ){

			boolean isSaidWerewolf = false;
			boolean isSaidVillager = false;
			for( Talk talk : args.agi.getTalkList(0) ){

				if( talk.getAgent().getAgentIdx() == agentNo ){
					Utterance ut = args.agi.getUtterance(talk.getContent());
					if( ut.getTopic() == Topic.ESTIMATE  ){
						if( ut.getRole().getTeam() == Team.WEREWOLF ){
							isSaidWerewolf = true;
						}else{
							isSaidVillager = true;
						}
					}
				}
			}


			if( isSaidWerewolf ){

				ArrayList<Guess> rguesses = agentStatistics.statistics.get(agentNo).getGuessFromEvent("Said0dEstimateWolf", args.agi.gameSetting);

				for( Guess guess : rguesses ){
					guess.correlation = Math.pow( guess.correlation, 0.8 );
					guesses.add(guess);
				}

			}else{

				ArrayList<Guess> rguesses = agentStatistics.statistics.get(agentNo).getGuessFromEvent("NoSaid0dEstimateWolf", args.agi.gameSetting);

				for( Guess guess : rguesses ){
					guess.correlation = Math.pow( guess.correlation, 0.8 );
					guesses.add(guess);
				}

			}

			if( isSaidVillager ){

				ArrayList<Guess> rguesses = agentStatistics.statistics.get(agentNo).getGuessFromEvent("Said0dEstimateVillager", args.agi.gameSetting);

				for( Guess guess : rguesses ){
					guess.correlation = Math.pow( guess.correlation, 0.8 );
					guesses.add(guess);
				}

			}else{

				ArrayList<Guess> rguesses = agentStatistics.statistics.get(agentNo).getGuessFromEvent("NoSaid0dEstimateVillager", args.agi.gameSetting);

				for( Guess guess : rguesses ){
					guess.correlation = Math.pow( guess.correlation, 0.8 );
					guesses.add(guess);
				}

			}

		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
