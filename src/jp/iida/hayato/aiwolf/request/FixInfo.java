package jp.iida.hayato.aiwolf.request;

import jp.iida.hayato.aiwolf.lib.Judge;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Status;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;


/**
 * �m���񂩂�s���v�����s���N���X
 */
public final class FixInfo extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		GameInfo gameInfo = args.agi.latestGameInfo;

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;


		// ���S�ς̎҂͊e�s���̑Ώۂɂ��Ȃ�
		for( Agent agent : gameInfo.getAgentList() ){
			Status status = gameInfo.getStatusMap().get(agent);
			if( status == Status.DEAD ){
				workReq = new Request( agent.getAgentIdx() );
				workReq.inspect = 0.0;
				workReq.guard = 0.0;
				workReq.vote =  0.0;
				workReq.attack = 0.0;
				Requests.add(workReq);
			}
		}

		// �l�T�͏P���Ώۂɂ��Ȃ�
		for( int agent : args.agi.getWolfList() ){
			workReq = new Request(agent);
			workReq.attack = 0.0001;
			Requests.add(workReq);
		}

		// ��������x������҂͐�̑Ώۂɂ��Ȃ�
		for( Judge judge : args.agi.selfInspectList ){
			workReq = new Request( judge.targetAgentNo );
			workReq.inspect = 0.0001;
			Requests.add(workReq);
		}

		// �����͊e�s���̑Ώۂɂ��Ȃ�
		workReq = new Request( gameInfo.getAgent().getAgentIdx() );
		workReq.inspect = 0.0001;
		workReq.guard = 0.0;
		workReq.vote =  0.0;
		workReq.attack = 0.0;
		Requests.add(workReq);

		return Requests;

	}

}
