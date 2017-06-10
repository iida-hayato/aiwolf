package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;


/**
 * �s����p�u�݂����v
 */
public final class AvoidExecute_Werewolf extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;

		// �����͏��Y���������Ȃ��̂ŕK�v�Ȃ�
		if( gameInfo.getDay() <= 0 ){
			return Requests;
		}

		// �S�����ڂ܂ł͂Ƃ肠�����s��Ȃ�
		if( args.agi.getMyTalkNum() < 4 ){
			return Requests;
		}


		// �錾�ςݓ��[��̕��͂��擾
		VoteAnalyzer voteAnalyzer = VoteAnalyzer.loadSaidVote(args.agi);



		// �P�[�ł������Ă���
		if( !voteAnalyzer.getMaxReceiveVoteAgent().isEmpty() ){

			// ���[��MAX���擾
			int receiveVoteCountMax = voteAnalyzer.getReceiveVoteCount(voteAnalyzer.getMaxReceiveVoteAgent().get(0));

			// �����̓��[�����擾
			int receiveVoteCountWolf = 0;

			for( int wolf : args.agi.getAliveWolfList() ){
				int receiveVoteCount = voteAnalyzer.receiveVoteCount.getOrDefault(Agent.getAgent(wolf), 0);
				receiveVoteCountWolf = Math.max(receiveVoteCountWolf, receiveVoteCount);
			}

			// ���[�����ɒ[�ɏ��Ȃ��Ȃ�s��Ȃ�
			if( receiveVoteCountWolf <= 1 ){
				return Requests;
			}

			// �����̓��[�������[��MAX-1�ȏ�Ȃ�݂����v�Z���K�v�i-1�̓q�X�e���V�X�̂��߁j
			if( receiveVoteCountWolf >= receiveVoteCountMax - 1 ){

				// �����G�[�W�F���g����
				for( Agent agent : gameInfo.getAliveAgentList() ){

					// �T�̓X�L�b�v
					if( args.agi.getAliveWolfList().contains(agent.getAgentIdx()) ){
						continue;
					}

					// �G�[�W�F���g�̓��[�����擾
					int receiveVoteCount = voteAnalyzer.receiveVoteCount.getOrDefault(agent, 0);

					// �����̓��[��ł���Ε[����-1�v�Z����
					if( agent.equals( voteAnalyzer.getVoteTarget(gameInfo.getAgent()) ) ){
						receiveVoteCount--;
					}

					if( receiveVoteCount + 1 > receiveVoteCountWolf ){
						// ���ƂP�[�ŘT��蓾�[���������Ȃ�
						workReq = new Request(agent);
						workReq.vote = 1.15;
						Requests.add(workReq);
					}else if( receiveVoteCount + 1 >= receiveVoteCountWolf ){
						// ���ƂP�[�ŘT�Ɠ��[���������ɂȂ�
						workReq = new Request(agent);
						workReq.vote = 1.1;
						Requests.add(workReq);
					}

				}

			}

		}

		return Requests;

	}

}
