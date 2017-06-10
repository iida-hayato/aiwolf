package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.lib.Judge;
import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * �s����p�u�肢�݂�v
 */
public final class SeerExecute extends AbstractActionStrategy {

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

			List<Integer> seers = args.agi.getEnableCOAgentNo(Role.SEER);

			for( Integer seer : seers ){
				// �l�O�̓X�L�b�v
				if( args.agi.getAliveWolfList().contains(seer) ){
					continue;
				}
				for( Judge judge : args.agi.getSeerJudgeList() ){
					// �Ԉ����������o������
					if( (judge.result == Species.WEREWOLF) != args.agi.isWolf(judge.targetAgentNo) ){
						continue;
					}
				}

				// �G�[�W�F���g�̓��[�����擾
				int receiveVoteCount = voteAnalyzer.receiveVoteCount.getOrDefault(Agent.getAgent(seer), 0);

				// ����̓��[�������[��MAX-1�ȏ�Ȃ牟������
				if( receiveVoteCount >= receiveVoteCountMax - 1 ){
					workReq = new Request(seer);
					workReq.vote = 12.00;
					Requests.add(workReq);
				}
			}

		}

		return Requests;

	}

}
