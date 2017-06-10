package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;


/**
 * �s����p�u��{���p�v
 */
public final class BasicSeer extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;

		// �錾�ςݓ��[��̕��͂��擾
		VoteAnalyzer voteAnalyzer = VoteAnalyzer.loadSaidVote(args.agi);

		// �ő��[�𓾂Ă���G�[�W�F���g�͐肢�悩�珜�O����
		for( Agent agent : voteAnalyzer.getMaxReceiveVoteAgent() ){
			workReq = new Request(agent);
			workReq.inspect = 0.05;
			Requests.add(workReq);
		}

		return Requests;
	}

}
