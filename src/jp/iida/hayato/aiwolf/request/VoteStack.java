package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;


/**
 * �s����p�u�[�d�ˁv
 * �{���z�肷�铮���́A�ʂ�\��������݂���Ă��邱�ƁB
 */
public final class VoteStack extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;



		// �錾�ςݓ��[��̕��͂��擾
		VoteAnalyzer voteAnalyzer = VoteAnalyzer.loadSaidVote(args.agi);

		// ���Ă���[���ɉ����ĕ[���d�˂�
		for( Agent agent : gameInfo.getAliveAgentList() ){
			workReq = new Request(agent);
			workReq.vote = 1.00 + voteAnalyzer.getReceiveVoteCount(agent) * 0.05 * (1 + voteAnalyzer.getReceiveVoteCount(gameInfo.getAgent()) * 0.10);
			Requests.add(workReq);
		}

		// �S������ȍ~�A�����������ĂO�[�Ⴂ�͓��[�v����������i4���ڂ܂Łj
		if( gameInfo.getAliveAgentList().size() >= 7 && args.agi.getMyTalkNum() > 3 ){
			// �����̓��[����擾
			Agent voteAgent = voteAnalyzer.getVoteTarget(gameInfo.getAgent());
			for( Agent agent : gameInfo.getAliveAgentList() ){
				int voteCount = voteAnalyzer.getReceiveVoteCount(agent) - ( agent.equals(voteAgent) ? 1 : 0 );
				if( voteCount <= 0 ){
					workReq = new Request(agent);
					workReq.vote = 0.5;
					Requests.add(workReq);
				}
			}

		}

		return Requests;

	}

}
