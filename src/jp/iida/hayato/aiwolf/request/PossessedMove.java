package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;


/**
 * �s����p�u�����[�u�v
 */
public final class PossessedMove extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;



		// �錾�ςݓ��[��̕��͂��擾
		VoteAnalyzer voteAnalyzer = VoteAnalyzer.loadSaidVote(args.agi);


		for( Agent agent : gameInfo.getAliveAgentList() ){
			// ���������_�Ŋm����
			if( args.agi.selfRealRoleViewInfo.isFixBlack(agent.getAgentIdx()) ){
				// �T�l�ɓ��[���Ȃ�
				workReq = new Request(agent);
				workReq.vote = 0.9;
				Requests.add(workReq);

				Agent target = voteAnalyzer.getVoteTarget(agent);
				// ���[���錾���Ă��邩
				if( target != null ){
					// �T�l�Ɠ����ꏊ�ɓ��[����
					workReq = new Request(target);
					workReq.vote = 1.2;
					Requests.add(workReq);
				}
			}else if( args.agi.selfRealRoleViewInfo.isFixWhite(agent.getAgentIdx()) ){
				// ��T�ɓ��[����
				workReq = new Request(agent);
				workReq.vote = 1.1;
				Requests.add(workReq);
			}
		}


		return Requests;

	}

}
